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

import static View_Controller.MainController.*;

/** FXML Controller Class*/
public class ModifyProductController implements Initializable
{

    @FXML private TableView<Part> addPartsTable;
    @FXML private TableColumn<Part, String> partID;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, String> partStock;
    @FXML private TableColumn<Part, String> partPrice;

    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part, String> associatedPartId;
    @FXML private TableColumn<Part, String> associatedPartName;
    @FXML private TableColumn<Part, String> associatedPartStock;
    @FXML private TableColumn<Part, String> associatedPartPrice;

    @FXML private TextField partSearchInput;
    @FXML private Button addPart;

    @FXML private TextField modifiedProductId;
    @FXML private TextField modifiedProductName;
    @FXML private TextField modifiedStockInput;
    @FXML private TextField modifiedProductPrice;
    @FXML private TextField modifiedProductMin;
    @FXML private TextField modifiedProductMax;

    @FXML private Label errorOutput;
    @FXML private TextField eventSourceForSearch;

    @FXML private TextField associatedPartSearchInput;
    @FXML private Button removePart;
    @FXML private Button saveAddedProduct;
    @FXML private Button cancelAddedProduct;


    /** A string variable that will store all the error outputs generated from the Inventory Class method, inputValidator.
     This variable is used during the saveProductHandler, where all user inputs are validated prior to saving the product.
     If any errors are caught, the contents of the errorOutPutString are displayed on the Modify Product window in red text.
     */
    private String errorOutputString = new String();

    /** Gets the current inventory to update with modified product.
     */
    private Inventory inventory = getCurrentInventory();

    /** Gets the product to modify, selected by the user during the Main Screen.
     */
    private Product modifiedProduct = getSelectedProduct();

    /** Gets the index of the user-selected product, for identifying the correct index at which to update the inventory.
     */
    int modifiedProductIndex = getSelectedProductIndex();

    /** Gets the associated parts of the user-selected product. This will be used only for loading the initial state
     of the associatedParts table.
     */
    ObservableList<Part> associatedPartsOfSelectedProduct = getSelectedProductAssociatedParts();

    /** Instantiates a Product object to copy and store the currently-selected product's "live" data and allow the user
     to modify this copy of the selected product and, if chosen, update the currently-selected product by updating it
     with the modified instance of the product class.
     */
    private Product preModifiedProduct = new Product();

    /** Holds the modified state of the selected product's associated parts prior to saving. Upon saving, this list will
     replace the contents of the selected product's associated parts.
     */
    ObservableList<Part> preAssociatedParts = FXCollections.observableArrayList();

    /** Sets the initial conditions of the Modify Product scene. This method sets the ID field to disabled, with
     pre-populated text, "Auto-generated". In addition, it pre-populates all fields of the form with the properties of
     the selected product. Finally, this method pre-populates the addParts and associatedParts tables with the current
     inventory of parts to add and the current associated parts of the product, respectively.
     @param url Resolves the relative file path of the root object.
     @param rb Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        modifiedProductId.setDisable(true);
        modifiedProductId.setText(Integer.toString(modifiedProduct.getId()));
        modifiedProductName.setText(modifiedProduct.getName());
        modifiedStockInput.setText(Integer.toString(modifiedProduct.getStock()));
        modifiedProductPrice.setText(Double.toString(modifiedProduct.getPrice()));
        modifiedProductMin.setText(Integer.toString(modifiedProduct.getMin()));
        modifiedProductMax.setText(Integer.toString(modifiedProduct.getMax()));

        populateAddTable();
        populateAssociatedPartsTable();
    }

    /** Searches for and filters all Parts whose IDs or names match the string entered via user input. The method first
     determines whether the event source is from the addParts table or the associatedParts table, and updates the
     'eventSourceForSearch' variable accordingly. The eventSourceForSearch variable's value will determine whether the
     search is carried out for the addParts or the associatedParts table. In either case, the search function checks if
     the search term is numeric or alphabetic; if numeric, the search function will filter results by Part ID; otherwise
     if search term is alphabetic, the search function filters results by Part Name. If no parts matching the search term
     are found, then an error will output accordingly. Finally, if the search term is blank, then the search will produce
     an unfiltered list of all parts.
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

                for(Part part : modifiedProduct.getAllAssociatedParts())
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
                for(Part part : modifiedProduct.getAllAssociatedParts())
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
                associatedPartsTable.setItems(modifiedProduct.getAllAssociatedParts());
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
    private void addPartHandler(ActionEvent event) throws IOException
    {
        Part part = addPartsTable.getSelectionModel().getSelectedItem();

        if(part != null)
        {
            preModifiedProduct.addAssociatedParts(part);
            associatedPartsTable.setItems(preModifiedProduct.getAllAssociatedParts());
            associatedParts();
        }
        else
        {
            alertHandler("partadd");
        }
    }

    /** Removes a part that the user selects from the associatedPartsTable.
     FUTURE ENHANCEMENT: Removing an associated part should increment the current stock of the part in inventory.
     @param event An object generated by the ActionEvent that is triggered by the user clicking the Remove button.
     */
    @FXML
    private void removePartHandler(ActionEvent event) throws IOException
    {
        Part part = associatedPartsTable.getSelectionModel().getSelectedItem();

        if(preModifiedProduct.deleteAssociatedPart(part))
        {
            associatedPartsTable.setItems(preModifiedProduct.getAllAssociatedParts());
        }
    }

    /** Saves the modified product into the current inventory. The method will validate all entries in the form and output
     any applicable errors on the form in red text. Otherwise if no errors are found, then the method updates the
     current inventory with the modified product.
     @param event An object generated by the ActionEvent that is triggered by the user clicking the Save button.
     */
    @FXML
    private void saveModifiedProductHandler(ActionEvent event) throws IOException
    {
        try
        {
            int productId = Integer.parseInt(modifiedProductId.getText());
            String productName = modifiedProductName.getText();
            int productStock = Integer.parseInt(modifiedStockInput.getText());
            double productPrice = Double.parseDouble(modifiedProductPrice.getText());
            int productMin = Integer.parseInt(modifiedProductMin.getText());
            int productMax = Integer.parseInt(modifiedProductMax.getText());

            errorOutputString = Inventory.inputValidator(productName, productStock, productPrice, productMin,
                    productMax, errorOutputString);

            if(errorOutputString.length() > 0)
            {
                errorOutput.setText(errorOutputString);
                errorOutputString = "";
            }
            else
            {
                preModifiedProduct.setId(productId);
                preModifiedProduct.setName(productName);
                preModifiedProduct.setPrice(productPrice);
                preModifiedProduct.setStock(productStock);
                preModifiedProduct.setMin(productMin);
                preModifiedProduct.setMax(productMax);

                inventory.updateProduct(modifiedProductIndex, preModifiedProduct);

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
    private void cancelModifiedProductHandler(ActionEvent event) throws IOException
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
        associatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
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

    /** Loads the current product's associated parts into the associatedParts table.
     */
    public void populateAssociatedPartsTable()
    {
        for(Part part : associatedPartsOfSelectedProduct)
        {
            preModifiedProduct.addAssociatedParts(part);
        }

        associatedPartsTable.setItems(preModifiedProduct.getAllAssociatedParts());
        associatedParts();
    }
}
