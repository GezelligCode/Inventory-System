package Model;

/** A class that forms a subclass of a Part object, called Outsourced Part. This Outsourced class extends the Part class,
 and adds a new property, companyName. An String variable is used to hold the company name that created the part.
 */
public class Outsourced extends Part
{
    private String companyName;

    /** The class constructor for Outsourced part objects.
     @param id An int variable holding the part's ID.
     @param name A String variable holding the part's name.
     @param price A Double variable part's price.
     @param stock An int variable holding the part's stock level.
     @param min An int variable holding the part's minimum stock level required.
     @param max An int variable holding the part's maximum stock level required.
     @param companyName An String variable holding the company name that created the part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName)
    {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** Sets the part's company name property.
     @param companyName An int variable holding the current part's company name.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /** Gets the part's company name property.
     @return An String variable holding the current part's company name.
     */
    public String getCompanyName() {
        return companyName;
    }
}
