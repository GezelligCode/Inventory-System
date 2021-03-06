package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static Model.Inventory.inputValidator;
import static View_Controller.MainController.*;

/** FXML Controller Class*/
public class AddProductController implements Initializable
{

    @FXML private TableView<Part> addPartsTable;
    @FXML private TableColumn<Part, String> partID;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, String> partStock;
    @FXML private TableColumn<Part, String> partPrice;

    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part, String> associatedPartID;
    @FXML private TableColumn<Part, String> associatedPartName;
    @FXML private TableColumn<Part, String> associatedPartStock;
    @FXML private TableColumn<Part, String> associatedPartPrice;

    @FXML private TextField partSearchInput;
    @FXML private TextField addedProductId;
    @FXML private TextField addedProductName;
    @FXML private TextField addedStockInput;
    @FXML private TextField addedProductPrice;
    @FXML private TextField addedProductMin;
    @FXML private TextField addedProductMax;
    @FXML private Button addPart;

    @FXML private Label errorOutput;

    @FXML private TextField associatedPartSearchInput;
    @FXML private Button removePart;
    @FXML private Button saveAddedProduct;
    @FXML private Button cancelAddedProduct;

    private TextField eventSourceForSearch;

    /** A string variable that will store all the error outputs generated from the Inventory Class method, inputValidator.
     This variable is used during the saveProductHandler, where all user inputs are validated prior to saving the product.
     If any errors are caught, the contents of the errorOutPutString are displayed on the Add Product window in red text.
     */
    private String errorOutputString = new String();

    /** Gets the current inventory to update with an added product.
     */
    private Inventory inventory = getCurrentInventory();

    /** Instantiates a Product object to define and add to inventory.
     */
    private Product preAddedProduct = new Product();

    /** Instantiates a list of associated parts to create with an added product.
     */
    private static ObservableList<Part> associatedPartsOfAddedProduct = FXCollections.observableArrayList();

    public static ObservableList<Part> getAssociatedPartsOfAddedProduct()
    {
        return associatedPartsOfAddedProduct;
    }


    /** Sets the initial conditions of the Add Product scene. This method sets the ID field to disabled, with pre-populated
     text, "Auto-generated". In addition, it pre-populates the Parts table with current inventory of parts to add.
     @param url Resolves the relative file path of the root object.
     @param rb Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addedProductId.setDisable(true);
        addedProductId.setText("Auto-generated");
        populateAddTable();
    }

    /** Searches for and filters all Parts whose IDs or names match the string entered via user input. The method first
     determines whether the event source is from the addParts table or the associatedParts table, and thereby determines
     whether the search is carried out for the addParts or the associatedParts table. In either case, the search function
     checks if the search term is numeric or alphabetic; if numeric, the search function will filter results by Part ID;
     otherwise if search term is alphabetic, the search function filters results by Part Name. If no parts matching the
     search term are found, then an error will output accordingly. Finally, if the search term is blank, then the search
     will produce an unfiltered list of all parts.
     @param event An object generated by the ActionEvent that is triggered by the user entering a search input in either
     the Parts table or Associated Parts table.
     */
    @FXML
    private void searchPartHandler(ActionEvent event) throws IOException
    {
        ObservableList<Part> partsFiltered = FXCollections.observableArrayList();

        if(event.getSource() == partSearchInput)
        {
           //output to partsToAddTable
           String searchTerm = partSearchInput.getText().toLowerCase();

           if(isNumeric(searchTerm) && !(searchTerm.isEmpty() || searchTerm.isBlank()))
           {
               int searchInt = Integer.parseInt(searchTerm);

               if(inventory.lookupPart(searchInt) != null)
               {
                   partsFiltered.add(inventory.lookupPart(searchInt));
                   addPartsTable.setItems(partsFiltered);
               }
               else
               {
                    alertHandler("nopartfound");
               }
           }
           else if(isAlpha(searchTerm) && !(searchTerm.isEmpty() || searchTerm.isBlank()))
           {
               if(!(inventory.lookupPart(searchTerm)).isEmpty())
               {
                   addPartsTable.setItems(inventory.lookupPart(searchTerm));
               }
               else
               {
                   alertHandler("nopartfound");
               }
           }
           else if(searchTerm.isEmpty() || searchTerm.isBlank())
           {
               addPartsTable.setItems(inventory.getPartInventory());
           }
           else
           {
               alertHandler("nopartfound");
           }
       }
       else
       {
           //output to associatedPartsTable
           String searchTerm = associatedPartSearchInput.getText().toLowerCase();

           if(isNumeric(searchTerm) && searchTerm != null)
           {
               int searchInt = Integer.parseInt(searchTerm);

               for(Part part : preAddedProduct.getAllAssociatedParts())
               {
                   if(part.getId() == searchInt)
                   {
                       partsFiltered.add(part);
                   }
               }
               if(!partsFiltered.isEmpty())
               {
                   associatedPartsTable.setItems(partsFiltered);
               }
               else
               {
                   alertHandler("nopartfound");
               }
           }
           else if(isAlpha(searchTerm) && !(inventory.lookupPart(searchTerm).isEmpty()))
           {
               for(Part part : preAddedProduct.getAllAssociatedParts())
               {
                   if(part.getName().toLowerCase().contains(searchTerm))
                   {
                       partsFiltered.add(part);
                   }
               }

               if(!partsFiltered.isEmpty())
               {
                   associatedPartsTable.setItems(partsFiltered);
               }
               else
               {
                   alertHandler("nopartfound");
               }

           }
           else if(searchTerm == null)
           {
               associatedPartsTable.setItems(preAddedProduct.getAllAssociatedParts());
           }
           else
           {
               alertHandler("nopartfound");
           }
       }
    }

    /** Adds a part that the user selects from the addParts table to the associatedParts table.
     FUTURE ENHANCEMENT: Adding an associated part should decrement the current stock of the part in inventory.
     @param event An object generated by the ActionEvent that is triggered by the user clicking the Add button.
     */
    @FXML
    private void addAssociatedPartHandler(ActionEvent event) throws IOException
    {

        Part part = addPartsTable.getSelectionModel().getSelectedItem();

        if(part != null)
        {
            preAddedProduct.addAssociatedParts(part);
            associatedPartsTable.setItems(preAddedProduct.getAllAssociatedParts());
            associatedParts();
        }
        else
        {
            alertHandler("nopartadd");
        }
    }

    /** Removes a part that the user selects from the associatedPartsTable.
     FUTURE ENHANCEMENT: Removing an associated part should increment the current stock of the part in inventory.
     @param event An object generated by the ActionEvent that is triggered by the user clicking the Remove button.
     */
    @FXML
    private void removeAssociatedPartHandler(ActionEvent event) throws IOException
    {
        Part part = associatedPartsTable.getSelectionModel().getSelectedItem();

        if(preAddedProduct.deleteAssociatedPart(part))
        {
            associatedPartsTable.setItems(preAddedProduct.getAllAssociatedParts());
        }
    }

    /** Saves the added product into the current inventory. The method will validate all entries in the form and output
     any applicable errors on the form in red text. Otherwise if no errors are found, then the method updates the
     current inventory with the added product.
     @param event An object generated by the ActionEvent that is triggered by the user clicking the Save button.
     */
    @FXML
    private void saveAddedProductHandler(ActionEvent event) throws IOException
    {
        try
        {
            String productName = addedProductName.getText();
            double productPrice = Double.parseDouble(addedProductPrice.getText());
            int productStock = Integer.parseInt(addedStockInput.getText());
            int productMin = Integer.parseInt(addedProductMin.getText());
            int productMax = Integer.parseInt(addedProductMax.getText());

            errorOutputString = inputValidator(productName, productStock, productPrice, productMin,
                    productMax, errorOutputString);

            if(errorOutputString.length() > 0)
            {
                errorOutput.setText(errorOutputString);
                errorOutputString = "";
            }
            else
            {
                preAddedProduct.setId(inventory.getProductIdIndex());
                preAddedProduct.setName(productName);
                preAddedProduct.setPrice(productPrice);
                preAddedProduct.setStock(productStock);
                preAddedProduct.setMin(productMin);
                preAddedProduct.setMax(productMax);

                inventory.addProduct(preAddedProduct);

                // Switch back to main scene after clicking save
                Parent prevScene = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Scene scene = new Scene(prevScene);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e)
        {
            if(e.getLocalizedMessage().contains("empty"))
            {
                errorOutput.setText("One or more fields are blank");
            }
            else
            {
                errorOutput.setText("Improper value entered for " + e.getLocalizedMessage().substring(3));
            }

        }
    }

    /** Redirects user to the Main screen.
     @param event An object generated by the ActionEvent that is triggered by the user clicking the Cancel button.
     */
    @FXML
    private void cancelAddedProductHandler(ActionEvent event) throws IOException
    {
        // Switch back to main screen
        Parent prevScene = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(prevScene);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Maps all properties of parts to be displayed in table columns for the addParts table.
     */
    private void associatedPartsToAdd()
    {
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** Maps all properties of parts to be displayed in table columns for the associatedParts table.
     */
    private void associatedParts()
    {
        associatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** Loads the current inventory of parts into the addParts table.
     */
    public void populateAddTable()
    {
        associatedPartsToAdd();
        ObservableList<Part> partInventory = inventory.getPartInventory();
        addPartsTable.setItems(partInventory);
    }

}
