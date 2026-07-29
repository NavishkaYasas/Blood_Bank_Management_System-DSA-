package bloodbank.model;

/** Requester extends Person (INHERITANCE) - represents a hospital or individual requesting blood. */
public class Requester extends Person {

    private String hospitalName;
    private String requiredBloodGroup;
    private int quantity;
    private String urgency; // "Normal" or "Urgent"

    public Requester(String id, String name, int age, String contactNo, String address,
                      String hospitalName, String requiredBloodGroup, int quantity, String urgency) {
        super(id, name, age, contactNo, address);
        this.hospitalName = hospitalName;
        this.requiredBloodGroup = requiredBloodGroup;
        this.quantity = quantity;
        this.urgency = urgency;
    }

    public String getHospitalName() { return hospitalName; }
    public String getRequiredBloodGroup() { return requiredBloodGroup; }
    public int getQuantity() { return quantity; }
    public String getUrgency() { return urgency; }

    public String toFileString() {
        return getId() + "," + getName() + "," + getAge() + "," + getContactNo() + ","
                + getAddress().replace(",", ";") + "," + hospitalName.replace(",", ";") + ","
                + requiredBloodGroup + "," + quantity + "," + urgency;
    }

    public static Requester fromFileString(String line) {
        String[] p = line.split(",", -1);
        return new Requester(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], p[5], p[6],
                Integer.parseInt(p[7]), p[8]);
    }

    @Override
    public String toString() {
        return super.toString() + " | Hospital: " + hospitalName
                + " | Needs: " + quantity + " unit(s) of " + requiredBloodGroup
                + " | Urgency: " + urgency;
    }
}
