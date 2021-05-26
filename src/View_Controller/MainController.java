package View_Controller;

import Model.*;
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
import java.util.Optional;
import java.util.ResourceBundle;

/** FXML Controller Class*/
public class MainController implements Initializable
{
    // TableView controls
    @FXML private TableView<Part> partsTable;
    @FXML private TableColumn<Part, String> partID;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, String> partStock;
    @FXML private TableColumn<Part, String> partPrice;

    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, String> productID;
    @FXML private TableColumn<Product, String> productName;
    @FXML private TableColumn<Product, String> productStock;
    @FXML private TableColumn<Product, String> productPrice;

    // Button & Input controls
    @FXML private TextField partSearchInput;
    @FXML private Button addPart;
    @FXML private Button modifyPart;
    @FXML private Button deletePart;
    @FXML private TextField productSearchInput;
    @FXML private Button exitProgram;

    // Private Data Members
    private static Inventory inventory = new Inventory();
    private static Part selectedPart = null;
    private static int selectedPartIndex;
    private static Product selectedProduct = null;
    private static int selectedProductIndex;
    private static ObservableList<Part> selectedProductAssociatedParts = null;

    // Accessors for other controllers to get above data members
    /** Allows other controllers to access the current state of the inventory.
     @return Returns current instance of inventory.
     */
    public static Inventory getCurrentInventory() { return inventory; }

    /** Allows other controllers to access the part selected by the user during the Main Screen.
     @return Returns the part currently selected by the user during the Main Screen.
     */
    public static Part getSelectedPart() { return selectedPart; }

    /** Allows other controllers to access the index of the part selected by the user during the Main Screen. This is
     used by the Modify Part Controller, so that it can identify correct index at which to update the current inventory
     of Parts.
     @return Returns the index of the part currently selected by the user during the Main Screen.
     */
    public static int getSelectedPartIndex() { return selectedPartIndex; }

    /** Allows other controllers to access the product selected by the user during the Main Screen.
     @return Returns the product currently selected by the user during the Main Screen.
     */
    public static Product getSelectedProduct() { return selectedProduct; }

    /** Allows other controllers to access the index of the product selected by the user during the Main Screen. This is
     used by the Modify Product Controller, so that it can identify correct index at which to update the current inventory
     of Products
     @return Returns the index of the product currently selected by the user during the Main Screen.
     */
    public static int getSelectedProductIndex() { return selectedProductIndex; }

    /** Allows other controllers to access the associated part(s) of a product selected by the user during the Main Screen.
     @return Returns the associated part(s) of the product currently selected by the user during the Main Screen.
     */
    public static ObservableList<Part> getSelectedProductAssociatedParts() { return selectedProductAssociatedParts; }

    /** The initialize method performs actions upon loading the scene for the first time.
     This method pre-populates the part and product tables with sample data if they have not already been entered.
     @param url Resolves the relative file path of the root object.
     @param rb Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Load sample data upon initialization of program
        if(!sampleDataEntered())
        {
            // populate sample data
            sampleData();
            sampleDataEntered(true);
        }

        // Update tables with current inventory upon reload of MainController
        partsTable.setItems(inventory.getPartInventory());
        productsTable.setItems(inventory.getProductInventory());

        // Load current inventory into table views
        populatePartsTable();
        populateProductsTable();

    }

    /** Searches for and filters all Parts whose IDs or names match the string entered via user input. This search
     function checks if the search term is numeric or alphabetic; if numeric, the search function will filter results by
     Part ID; otherwise if search term is alphabetic, the search function filters results by Part Name. If no parts
     matching the search term are found, then an error will output accordingly. Finally, if the search term is blank,
     then the search will produce an unfiltered list of all parts.
     */
    @FXML
    private void searchPartHandler()
    {
        ObservableList<Part> partsFilteredByID = FXCollections.observableArrayList();

        if(isNumeric(partSearchInput.getText()) && !partSearchInput.getText().isEmpty())
        {
            int searchID = Integer.parseInt(partSearchInput.getText());
            Part searchedPart = inventory.lookupPart(searchID);

            if(searchedPart == null)
            {
                alertHandler("nopartfound");
            }
            else
            {
                partsFilteredByID.add(searchedPart);
                partsTable.setItems(partsFilteredByID);
            }
        }
        else if(isAlpha(partSearchInput.getText()) && !partSearchInput.getText().isEmpty())
        {
            String searchName = partSearchInput.getText().toLowerCase();

            if(!(inventory.lookupPart(searchName).isEmpty()))
            {
                partsTable.setItems(inventory.lookupPart(searchName));
            }
            else
            {
                alertHandler("nopartfound");
            }
        }
        else if(partSearchInput.getText().isEmpty())
        {
            partsTable.setItems(inventory.getPartInventory());
        }
        else
        {
            alertHandler("nopartfound");
        }
    }

    /** Redirects user to the Add Part screen.
     @param event An object generated by the ActionEvent that is triggered by the user clicking the Add button.
     */
    @FXML
    private void addPartHandler(ActionEvent event) throws IOException
    {
        Parent addPart = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene scene = new Scene(addPart);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Redirects user to the Modify Part screen. This method assigns the currently-selected part to the private static
     variable selectedPart, which the ModifyPartController will be able to access via the public static
     getSelectedPart() accessor. In addition, the method also assigns the currently-selected part's index to the private
     static variable selectedPartIndex, which the ModifyPartController will access via the public static
     getSelectedPartIndex() accessor. The method performs an error check to ensure that the user has selected a part to
     modify before redirecting the user to the Modify Part screen. If no part is selected, a message is generated that
     advises the user accordingly.
     @param event An object generated by the ActionEvent that is triggered by the user clicking the Modify button.
     */
    @FXML
    private void modifyPartHandler(ActionEvent event) throws IOException
    {
        selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if(selectedPart != null)
        {
            selectedPartIndex = getCurrentInventory().getPartInventory().indexOf(selectedPart);
            Parent root = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));

            // Switch to Modify Part Scene
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            alertHandler("nopartmodify");
        }
    }

    /** Deletes a selected part from the Parts inventory. The method first checks whether a part is pre-selected and
     outputs a warning if no part is selected. Otherwise if a part is selected, the method then asks the user to confirm
     deletion; if user confirms then the method removes the part from the inventory; otherwise if the user denies
     the deletion, then the method alerts the user via alert message that the part is not deleted and returns to the
     main screen.
     */
    @FXML
    private void deletePartHandler() throws IOException
    {
        Part part = partsTable.getSelectionModel().getSelectedItem();

        if(inventory.deletePart(part))
        {
            partsTable.setItems(inventory.getPartInventory());
        }
    }

    /** Searches for and filters all Products whose IDs or names match the string entered via user input. This search
     function checks if the search term is numeric or alphabetic; if numeric, the search function will filter results by
     Product ID; otherwise if search term is alphabetic, the search function filters results by Product Name. If no 
     Products matching the search term are found, then an error will output accordingly. Finally, if the search term 
     is blank, then the search will produce an unfiltered list of all Products.
     */
    @FXML
    private void searchProductHandler()
    {
        ObservableList<Product> productsFilteredByID = FXCollections.observableArrayList();

        if(isNumeric(productSearchInput.getText()) && !productSearchInput.getText().isEmpty())
        {
            int searchID = Integer.parseInt(productSearchInput.getText());
            Product searchedProduct = inventory.lookupProduct(searchID);

            if(searchedProduct == null)
            {
                alertHandler("noproductfound");
            }
            else
            {
                productsFilteredByID.add(searchedProduct);
                productsTable.setItems(productsFilteredByID);
            }
        }
        else if(isAlpha(productSearchInput.getText()) && !productSearchInput.getText().isEmpty())
        {
            String searchName = productSearchInput.getText().toLowerCase();

            if(!(inventory.lookupProduct(searchName).isEmpty()))
            {
                productsTable.setItems(inventory.lookupProduct(searchName));
            }
            else
            {
                alertHandler("noproductfound");
            }
        }
        else if(productSearchInput.getText().isEmpty())
        {
            productsTable.setItems(inventory.getProductInventory());
        }
        else
        {
            alertHandler("noproductfound");
        }
    }

    /** Redirects user to the Add Product screen.
     @param event An object generated by the ActionEvent that is triggered by the user clicking the Add button.
     */
    @FXML
    private void addProductHandler(ActionEvent event) throws IOException
    {
        // Switch to Add Product Scene
        Parent addProduct = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Redirects user to the Modify Product screen. This method assigns the currently-selected product to the private
     static variable selectedProduct, which the ModifyProductController will be able to access via the public static
     getSelectedProduct() accessor. In addition, the method also assigns the currently-selected product's index to the
     private static variable selectedProductIndex, which the ModifyProductController will access via the public static
     getSelectedProductIndex() accessor. The method performs an error check to ensure that the user has selected a
     product to modify before redirecting the user to the Modify Product screen. If no product is selected, a message is
     generated that advises the user accordingly.
     @param event An object generated by the ActionEvent that is triggered by the user clicking the Modify button.
     */
    @FXML
    private void modifyProductHandler(ActionEvent event) throws IOException
    {
        selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null)
        {
            selectedProductIndex = getCurrentInventory().getProductInventory().indexOf(selectedProduct);
            selectedProductAssociatedParts = selectedProduct.getAllAssociatedParts();

            Parent root = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));

            // Switch to Modify Product Scene
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            alertHandler("noproductmodify");
        }
    }

    /** Deletes a selected Product from the Products inventory. The method first checks whether the selected Product
     currently has associated Parts. If the Product has associated Parts, then the user is notified via an alert message
     that the Part(s) must be deleted from the selected Product before the Product can be deleted. Otherwise, if the
     Product has no associated Parts, the method asks the user to confirm deletion; if user confirms then
     the method removes the Product from the inventory; otherwise if the user denies the deletion, then the method alerts
     the user via alert message that the Product is not deleted and returns to the main screen.
     */
    @FXML
    private void deleteProductHandler(ActionEvent event)
    {
        Product product = productsTable.getSelectionModel().getSelectedItem();

        if(inventory.deleteProduct(product))
        {
            productsTable.setItems(inventory.getProductInventory());
        }
    }

    /** Exits the program. The method first asks for the user to confirm the intent to exit program and proceeds
     accordingly.
     */
    @FXML
    private void exitProgramHandler()
    {
        if (alertHandler("exit").getResult() == ButtonType.OK)
        {
            System.exit(0);
        }
    }

    /** Pre-populates the current inventory with parts and products.
     */
    private void sampleData()
    {
        inventory.addPart(new InHouse(inventory.getPartIdIndex(), "Aluminum Frame", 100.00, 10, 1, 100, 42));
        inventory.addPart(new InHouse(inventory.getPartIdIndex(), "Carbon Frame", 300.00, 20, 1, 100, 44));
        inventory.addPart(new InHouse(inventory.getPartIdIndex(), "Steel Frame", 200.00, 50, 1, 100, 46));
        inventory.addPart(new Outsourced(inventory.getPartIdIndex(), "Carbon Fork", 150.00, 50, 1, 100, "Giant"));
        inventory.addPart(new Outsourced(inventory.getPartIdIndex(), "Carbon Seatpost", 150.00, 50, 1, 100, "Trek"));
        inventory.addProduct(new Product(inventory.getProductIdIndex(), "SWorks X90", 300.00, 20, 5, 100));
        inventory.addProduct(new Product(inventory.getProductIdIndex(), "Giant Sequoia", 250.00, 20, 5, 100));
    }

    /** Loads the pre-populated inventory of parts into the parts table.
     */
    public void updatePartsTable()
    {
        partsTable.setItems(inventory.getPartInventory());
    }

    /** Maps all properties of parts to be displayed in table columns.
     */
    private void populatePartsTable()
    {
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** Maps all properties of products to be displayed in table columns.
     */
    private void populateProductsTable()
    {
        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    ////////Helper Methods/////////

    private static boolean samplesEntered = false;

    /** Returns the current state of the samplesEntered boolean value, to indicate whether the sample data has already
     been loaded during initialization. This prevents duplication of entries upon reloading the MainController.
     @return Returns true sample data has already been entered into the table views, otherwise returns false.
     */
    private boolean sampleDataEntered()
    {
        return samplesEntered;
    }

    /** Sets the current state of the samplesEntered boolean value to true or false, to establish that the sample data
     is entered during initialization.
     @param entered A boolean value that will be assigned to the samplesEntered boolean variable.
     */
    private void sampleDataEntered(boolean entered) { samplesEntered = entered; }

    /** Computes whether an input is numeric.
     @param strNum A string input via TextFields for searching Parts and Products.
     @return Returns true if strNum is numeric, otherwise returns false.
     */
    public static boolean isNumeric(String strNum)
    {
        if (strNum == null)
        {
            return false;
        }
        try
        {
            int intString = Integer.parseInt(strNum);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    /** Computes whether an input is alphabetic.
     @param name A string input via TextFields for searching Parts and Products.
     @return Returns true if name is alphabetic, otherwise returns false.
     */
    public static boolean isAlpha(String name)
    {
        char[] chars = name.toCharArray();

        for (char c : chars)
        {
            if(!Character.isLetter(c))
            {
                return false;
            }
        }

        return true;
    }

    /** Handles the majority of the program's alerting system.
     @param function A string input specified via programmer input to select from the applicable list of alerts.
     @return Returns an alert object with pre-defined properties as designed by programmer input.
     */
    public static Alert alertHandler(String function)
    {
        Alert alert = new Alert(Alert.AlertType.NONE);

        if(function == "nopartadd")
        {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Parts to Add");
            alert.setHeaderText("No Part Selected");
            alert.setContentText("Please select a part to add");
            alert.showAndWait();

            return alert;
        }
        else if(function == "nopartmodify")
        {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Parts");
            alert.setHeaderText("No Part Selected");
            alert.setContentText("Please select a part to modify");
            alert.showAndWait();

            return alert;
        }
        else if(function == "nopartfound")
        {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("Search term entered does not match any known parts");
            alert.showAndWait();

            return alert;
        }
        else if(function == "noproductmodify")
        {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Products");
            alert.setHeaderText("No Product Selected");
            alert.setContentText("Please select a product to modify");
            alert.showAndWait();

            return alert;
        }
        else if(function == "noproductfound")
        {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Search Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("Search term entered does not match any known products");
            alert.showAndWait();

            return alert;
        }
        else if(function == "confirmdeletepart")
        {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Parts");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete " + selectedPart.getName() + "?");
            Optional<ButtonType> decision = alert.showAndWait();

            return alert;
        }
        else if(function == "canceldeletepart")
        {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("Parts");
            alert.setHeaderText("Deletion Cancelled");
            alert.setContentText(selectedPart.getName() + " is not removed.");
            alert.showAndWait();

            return alert;
        }
        else if(function == "nopartdelete")
        {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Parts");
            alert.setHeaderText("No Part Selected");
            alert.setContentText("Please select a part to delete");
            alert.showAndWait();

            return alert;
        }
        else if(function == "partsassociatedwithproduct")
        {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Products");
            alert.setHeaderText("Delete Error");
            alert.setContentText("Product has associated parts." + "\r\n" +
                    "Please remove all associated parts before deleting product.");
            alert.showAndWait();

            return alert;
        }
        else if(function == "confirmdeleteproduct")
        {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Products");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete " + selectedProduct.getName() + "?");
            Optional<ButtonType> decision = alert.showAndWait();

            return alert;
        }
        else if(function == "noproductdelete")
        {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Products");
            alert.setHeaderText("No Product Selected");
            alert.setContentText("Please select a product to delete");
            alert.showAndWait();

            return alert;
        }
        else if(function == "productremoved")
        {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("Products");
            alert.setHeaderText("Deletion Complete");
            alert.setContentText(selectedProduct.getName() + " is removed.");
            alert.showAndWait();

            return alert;
        }
        else if(function == "canceldeleteproduct")
        {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("Products");
            alert.setHeaderText("Deletion Cancelled");
            alert.setContentText(selectedProduct.getName() + " is not removed.");
            alert.showAndWait();

            return alert;
        }
        else if(function == "exit")
        {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Inventory Management System");
            alert.setHeaderText("Confirm Exit");
            alert.setContentText("Are you sure you want to exit?");
            Optional<ButtonType> result = alert.showAndWait();

            return alert;
        }

        return null;
    }
}
