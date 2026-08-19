package bloodbank.model;

/**
 * Member-2's component: Task 1 - Requester class, representing a blood requestor.
 * - Contains fields for requestor's name, contact info, blood type, and quantity requested.
 * - Provides getters and setters for each field.
 * Requester extends Person (INHERITANCE).
 * Represents a hospital or individual requesting blood.
 */
public class Requester extends Person {

    // Extra fields specific to a requester
    private String hospitalName;        // hospital or clinic name
    private String requiredBloodGroup;  // blood group needed (A+, O-, etc.)
    private int quantity;               // number of units requested
    private String urgency;             // "Normal" or "Urgent"

    // Constructor: initialize requester with personal + request-specific details
    public Requester(String id, String name, int age, String contactNo, String address,
                     String hospitalName, String requiredBloodGroup, int quantity, String urgency) {
        super(id, name, age, contactNo, address); // call Person constructor
        this.hospitalName = hospitalName;
        this.requiredBloodGroup = requiredBloodGroup;
        this.quantity = quantity;
        this.urgency = urgency;
    }

    // Getters (encapsulation: controlled access to private fields)
    public String getHospitalName() { return hospitalName; }
    public String getRequiredBloodGroup() { return requiredBloodGroup; }
    public int getQuantity() { return quantity; }
    public String getUrgency() { return urgency; }

    /**
     * Convert requester into a CSV line for file storage.
     * Commas in address/hospital name are replaced with semicolons to avoid breaking CSV format.
     */
    public String toFileString() {
        return getId() + "," + getName() + "," + getAge() + "," + getContactNo() + ","
                + getAddress().replace(",", ";") + "," + hospitalName.replace(",", ";") + ","
                + requiredBloodGroup + "," + quantity + "," + urgency;
    }

    /**
     * Rebuilds a Requester from one CSV line.
     * Splits the line into fields and constructs a new Requester object.
     */
    public static Requester fromFileString(String line) {
        String[] p = line.split(",", -1);
        return new Requester(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], p[5], p[6],
                Integer.parseInt(p[7]), p[8]);
    }

    // String representation for logging/display
    @Override
    public String toString() {
        return super.toString() + " | Hospital: " + hospitalName
                + " | Needs: " + quantity + " unit(s) of " + requiredBloodGroup
                + " | Urgency: " + urgency;
    }
}
