package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/** A class that forms an inventoriable object, called a product. The class has ID, name, price, stock, min, and max as 
 its data values. The class implements various methods for accessing and mutating each of these properties.
 */
public class Product
{

    private final ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** The class constructor for product objects. 
     @param id An int variable holding the product's ID.
     @param name A String variable holding the product's name.
     @param price A Double variable product's price.
     @param stock An int variable holding the product's stock level.
     @param min An int variable holding the product's minimum stock level required.
     @param max An int variable holding the product's maximum stock level required.
     */
    public Product(int id, String name, double price, int stock, int min, int max)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        associatedParts = FXCollections.observableArrayList();
    }

    /** LOGIC/RUNTIME ERROR: Without a no-parameter constructor, using the default class constructor in the Add Product
     Controller creates a runtime error where "this.preAddedProduct is null", when attempting to access the Product class
     methods. This is because in order for the Product class methods to be accessed, a Product object must be instantiated.
     Therefore, I've created a no-parameter constructor for instantiating a Product during the Add Product and Modify
     Product screens, so that the Product class functions (for instance, the Add/Remove Associated Part functions) may
     be accessed while a) in the case of the Add Product controller, the product does not yet exist in inventory, and
     b) in the case of the Modify Product controller, the live data of the currently-selected product is kept intact and
     not altered until the user saves the modifications. This is a no-parameter constructor that has only one data member,
     an observableArrayList for mediating the addition and removal of parts during the Add Product screen. In addition,
     this constructor enables a 'copy' of the selected product to be modified, and later replace the currently-selected
     product upon saving the modifications.
     */
    public Product()
    {
        associatedParts = FXCollections.observableArrayList();
    }

    /** Sets the product's ID property.
     @param id An int variable holding the current product's ID.
     */
    public void setId(int id) { this.id = id; }

    /** Sets the product's name property.
     @param name A String variable holding the current product's name.
     */
    public void setName(String name) { this.name = name; }

    /** Sets the product's price property.
     @param price A Double variable holding the current product's price.
     */
    public void setPrice(double price) { this.price = price; }

    /** Sets the product's current stock property.
     @param stock A Double variable holding the current product's stock.
     */
    public void setStock(int stock) { this.stock = stock; }

    /** Sets the product's minimum stock property.
     @param min An int variable holding the current product's minimum stock.
     */
    public void setMin(int min) { this.min = min; }

    /** Sets the product's maximum stock property.
     @param max An int variable holding the current product's maximum stock.
     */
    public void setMax(int max) { this.max = max; }

    /** Sets the product's price property.
     @param price A Double variable holding the current product's price.
     */
    public void setPrice(int price) { this.price = (double)price; }

    /** Gets the product's ID property.
     @return An int variable holding the current product's ID.
     */
    public int getId() { return id; }

    /** Gets the product's name property.
     @return A String variable holding the current product's name.
     */
    public String getName() { return name; }

    /** Gets the product's price property.
     @return A Double variable holding the current product's price.
     */
    public double getPrice() { return price; }

    /** Gets the product's current stock property.
     @return An int variable holding the current product's stock.
     */
    public int getStock() { return stock; }

    /** Gets the product's minimum stock property.
     @return An int variable holding the current product's minimum stock.
     */
    public int getMin() { return min; }

    /** Gets the product's maximum stock property.
     @return An int variable holding the current product's maximum stock.
     */
    public int getMax() { return max; }

    /** Adds parts to a product.
     @param selectedAssociatedPart A part instance that is selected by the user during the Add or Modify Product screen.
     */
    public void addAssociatedParts(Part selectedAssociatedPart)
    {
        associatedParts.add(selectedAssociatedPart);
    }

    /** Removes parts from a product.
     @param selectedAssociatedPart A part instance that is selected by the user during the Add or Modify Product screen.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart)
    {
        if(selectedAssociatedPart != null)
        {
            // Generate alert to verify intent to delete
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Associated Parts");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete " + selectedAssociatedPart.getName() + "?");
            Optional<ButtonType> decision = alert.showAndWait();

            if (decision.get() == ButtonType.OK)
            {
                associatedParts.remove(selectedAssociatedPart);

                Alert deleteAlert = new Alert(Alert.AlertType.INFORMATION);
                deleteAlert.setTitle("Associated Parts");
                deleteAlert.setHeaderText("Deletion Complete");
                deleteAlert.setContentText(selectedAssociatedPart.getName() + " is removed.");
                deleteAlert.showAndWait();

                return true;
            }
            else
            {
                Alert cancelDeleteAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelDeleteAlert.setTitle("Associated Parts");
                cancelDeleteAlert.setHeaderText("Deletion Cancelled");
                cancelDeleteAlert.setContentText(selectedAssociatedPart.getName() + " is not removed.");
                cancelDeleteAlert.showAndWait();

                return false;
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Associated Parts");
            alert.setHeaderText("No Part Selected");
            alert.setContentText("Please select a part to delete");
            alert.showAndWait();
            return false;
        }
    }

    /** Gets the product's associated parts.
     @return A list of associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() { return associatedParts; }

}
