package Model;

import View_Controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;

/** A class consisting of parts and products as its data members. Parts and products are each held in an ArrayList data
 structure, called ObservableList. All parts are held in the partInventory list; all products are held in the
 productInventory list. The Inventory class also provides methods for manipulating the stock of parts and
 products.
 */
public class Inventory
{

    private final ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private final ObservableList<Product> productInventory = FXCollections.observableArrayList();

    /** Adds parts to the part inventory.
     @param part An instance of a Part object to be added to the part inventory.
     */
    public void addPart(Part part)
    {
        partInventory.add(part);
    }

    /** Deletes parts from the part inventory. First, this method checks whether the part selected equates to null, i.e.
     whether a part was selected. If null, then user is advised to select a part. Otherwise, the method then proceeds to
     confirm the intent to delete and proceeds accordingly. Upon deletion, the method confirms that the item is deleted.
     @param selectedPart An instance of a Part object to be removed from the part inventory.
     */
    public boolean deletePart(Part selectedPart)
    {
        if(selectedPart != null)
        {
            if (MainController.alertHandler("confirmdeletepart").getResult() == ButtonType.OK)
            {
                partInventory.remove(selectedPart);
                return true;
            }
            else
            {
                MainController.alertHandler("canceldeletepart");
                return false;
            }
        }
        else
        {
            MainController.alertHandler("nopartdelete");
            return false;
        }
    }

    /** Updates a part within the current inventory.
     @param index The index of the part to be modified.
     @param part An instance of a Part object to be modified in the part inventory.
     */
    public void updatePart(int index, Part part)
    {
        partInventory.set(index, part);
    }

    /** Searches the part inventory by part name.
     @param partName The search term to match part names against.
     @return A list of all parts whose name matches the given search term, partName. Otherwise, returns null if no match
     is found.
     */
    public ObservableList<Part> lookupPart(String partName)
    {
        ObservableList<Part> partsFilteredByName = FXCollections.observableArrayList();

        for(Part part : partInventory)
        {
            if(part.getName().toLowerCase().contains(partName))
            {
                partsFilteredByName.add(part);
            }
        }

        return partsFilteredByName;
    }

    /** Searches the part inventory by part ID.
     @param partId The search term to match part IDs against.
     @return A part object whose ID matches the given search term, partId. Otherwise, returns null if no match is found.
     */
    public Part lookupPart(int partId)
    {
        for(Part part : partInventory)
        {
            if(part.getId() == partId)
            {
                return part;
            }
        }
        return null;
    }


    /** Outputs the current list of all parts in the inventory.
     @return The list of all part objects currently held in inventory.
     */
    public ObservableList<Part> getPartInventory()
    {
        return partInventory;
    }

    /** Outputs an incremental index to be used for indexing parts added to inventory.
     @return A numerical index that is incremented from the previously-assigned index.
     */
    public int getPartIdIndex()
    {
        int maxId = 0;
        for(Part part : partInventory)
        {
            if(part.getId() > maxId)
            {
                maxId = part.getId();
            }
        }

        return maxId + 1;
    }

    /** Adds a new product to the product inventory.
     */
    public void addProduct(Product product)
    {
        productInventory.add(product);
    }

    /** Searches the product inventory by product ID.
     @param productId The search term to match product IDs against.
     @return A product object whose ID matches the given search term, productId. Otherwise, returns null if no match
     is found.
     */
    public Product lookupProduct(int productId)
    {
        for(Product product : productInventory)
        {
            if(product.getId() == productId)
            {
                return product;
            }
        }
        return null;
    }

    /** Searches the product inventory by product name.
     @param productName The search term to match product names against.
     @return A list of products whose name matches the given search term, productName. Otherwise, returns null if no match
     is found.
     */
    public ObservableList<Product> lookupProduct(String productName)
    {
        ObservableList<Product> productsFilteredByName = FXCollections.observableArrayList();

        for(Product product : productInventory)
        {
            if(product.getName().toLowerCase().contains(productName))
            {
                productsFilteredByName.add(product);
            }
        }
        return productsFilteredByName;
    }

    /** Updates a product within the current inventory.
     @param index The index of the product to be modified.
     @param product An instance of a product object to be modified in the product inventory.
     */
    public void updateProduct(int index, Product product)
    {
        productInventory.set(index, product);
    }

    /** Deletes products from the product inventory. This method first checks if any product is selected (i.e. not null),
     and if so, proceeds check whether any parts are asosciated with that product. If selected product has associated
     parts, then a warning is output to advise that the product cannot be deleted until its associated parts are removed
     first. Otherwise, if no associated parts are found, the user is asked to confirm the intent to delete and then
     proceeds accordingly. If, at the outset, the selected product equates to null, then the user is advised that a
     a product must be selected first.
     @param selectedProduct An instance of a product object to be removed from the product inventory.
     */
    public boolean deleteProduct(Product selectedProduct)
    {
        if(selectedProduct != null)
        {
            if (selectedProduct.getAllAssociatedParts().size() > 0)
            {
                // Output warning if associated with a product
                MainController.alertHandler("partsassociatedwithproduct");

                return false;
            }
            else
            {
                if (MainController.alertHandler("confirmdeleteproduct").getResult() == ButtonType.OK)
                {
                    // delete Product
                    productInventory.remove(selectedProduct);

                    MainController.alertHandler("productdeleted");
                    return true;
                }
                else
                {
                    MainController.alertHandler("canceldeleteproduct");
                    return false;
                }
            }
        }
        else
        {
            MainController.alertHandler("noproductdelete");
            return false;
        }
    }

    /** Outputs the current list of all products in the inventory.
     @return The list of all product objects currently held in inventory.
     */
    public ObservableList<Product> getProductInventory()
    {
        return productInventory;
    }

    /** Outputs an incremental index to be used for indexing products added to inventory.
     @return A numerical index that is incremented from the previously-assigned index.
     */
    public int getProductIdIndex()
    {
        int maxId = 0;
        for(Product product : productInventory)
        {
            if(product.getId() > maxId)
            {
                maxId = product.getId();
            }
        }

        return maxId + 1;
    }


    /** Validates user inputs in text fields for all Add/Modify screens. Checks for blank names, stock and price values
     that are less than 1, and inventory values that have a minimum greater than maximum or a stock that is not within
     the range of maximum and minimum values. If any of these errors are detected, the string errorOutput will be
     updated accordingly and output to the screen.
     @return A string value that can hold one to five separate lines of error warnings.
     */
    public static String inputValidator(String name, int stock, double price, int min, int max, String errorOutput)
    {
        if (name == null || name == "")
        {
            errorOutput = errorOutput + ("Please enter a valid name " + "\r\n");
        }
        if (stock < 1)
        {
            errorOutput = errorOutput + ("The inventory must be greater than 0 " + "\r\n");
        }
        if (price < 1)
        {
            errorOutput = errorOutput + ("The price must be greater than $0 " + "\r\n");
        }
        if (min > max)
        {
            errorOutput = errorOutput + ("The inventory minimum must be less than the maximum " + "\r\n");
        }
        if (stock < min || stock > max)
        {
            errorOutput = errorOutput + ("Part inventory must be between maximum and minimum values " + "\r\n");
        }
        return errorOutput;
    }
}
