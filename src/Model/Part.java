package Model;

/** A class that forms an inventoriable object, called a part. The class has ID, name, price, stock, min, and max as its
 data values. The class implements various methods for accessing and mutating each of these properties.
 */
public abstract class Part
{
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** The class constructor for part objects.
     @param id An int variable holding the part's ID.
     @param name A String variable holding the part's name.
     @param price A Double variable part's price.
     @param stock An int variable holding the part's stock level.
     @param min An int variable holding the part's minimum stock level required.
     @param max An int variable holding the part's maximum stock level required.
     */
    public Part(int id, String name, double price, int stock, int min, int max)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /** Sets the part's ID property.
     @param id An int variable holding the current part's ID.
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /** Sets the part's name property.
     @param name A String variable holding the current part's name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Sets the part's price property.
     @param price A Double variable holding the current part's price.
     */
    public void setPrice(double price)
    {
        this.price = price;
    }

    /** Sets the part's current stock property.
     @param stock A Double variable holding the current part's stock.
     */
    public void setStock(int stock)
    {
        this.stock = stock;
    }

    /** Sets the part's minimum stock property.
     @param min An int variable holding the current part's minimum stock.
     */
    public void setMin(int min)
    {
        this.min = min;
    }

    /** Sets the part's maximum stock property.
     @param max An int variable holding the current part's maximum stock.
     */
    public void setMax(int max)
    {
        this.max = max;
    }

    /** Gets the part's ID property.
     @return An int variable holding the current part's ID.
     */
    public int getId()
    {
        return id;
    }

    /** Gets the part's name property.
     @return A String variable holding the current part's name.
     */
    public String getName()
    {
        return name;
    }

    /** Gets the part's price property.
     @return A Double variable holding the current part's price.
     */
    public double getPrice()
    {
        return price;
    }

    /** Gets the part's current stock property.
     @return An int variable holding the current part's stock.
     */
    public int getStock()
    {
        return stock;
    }

    /** Gets the part's minimum stock property.
     @return An int variable holding the current part's minimum stock.
     */
    public int getMin()
    {
        return min;
    }

    /** Gets the part's maximum stock property.
     @return An int variable holding the current part's maximum stock.
     */
    public int getMax()
    {
        return max;
    }

}
