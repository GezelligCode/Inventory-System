package Model;

/** A class that forms a subclass of a Part object, called InHouse Part. This InHouse class extends the Part class, and
 adds a new property, machineID. An int variable is used to hold the machine ID that created the part in-house.
 */
public class InHouse extends Part
{
    private int machineId;

    /** The class constructor for InHouse part objects.
     @param id An int variable holding the part's ID.
     @param name A String variable holding the part's name.
     @param price A Double variable part's price.
     @param stock An int variable holding the part's stock level.
     @param min An int variable holding the part's minimum stock level required.
     @param max An int variable holding the part's maximum stock level required.
     @param machineId An int variable holding the machine ID that created the part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId)
    {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /** Gets the part's machine ID property.
     @return An int variable holding the current part's machine ID.
     */
    public int getMachineId() { return machineId; }

    /** Sets the part's machine ID property.
     @param machineId An int variable holding the current part's machine ID.
     */
    public void setMachineId(int machineId) { this.machineId = machineId; }
}
