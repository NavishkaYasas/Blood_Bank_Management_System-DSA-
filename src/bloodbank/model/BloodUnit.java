package bloodbank.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents one unit of blood in inventory.
 * Implements Comparable<BloodUnit> by expiryDate so it can be stored
 * in the BST/AVL tree (ordered by expiry) and sorted.
 */
public class BloodUnit implements Comparable<BloodUnit> {

    // Formatter for expiry dates (yyyy-MM-dd format)
    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Unique ID for the blood unit
    private String unitId;
    // Blood group (A+, O-, etc.)
    private String bloodGroup;
    // Expiry date in yyyy-MM-dd format
    private String expiryDate;
    // Donor ID who donated this unit
    private String donorId;
    // Current status: Available, Reserved, Issued, Expired
    private String status;

    // Constructor: initialize a blood unit with given details
    public BloodUnit(String unitId, String bloodGroup, String expiryDate, String donorId, String status) {
        this.unitId = unitId;
        this.bloodGroup = bloodGroup;
        this.expiryDate = expiryDate;
        this.donorId = donorId;
        this.status = status;
    }

    // Getters and setters
    public String getUnitId() { return unitId; }
    public String getBloodGroup() { return bloodGroup; }
    public String getExpiryDate() { return expiryDate; }
    public String getDonorId() { return donorId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Check if the blood unit is expired compared to today's date
    public boolean isExpired() {
        try {
            return LocalDate.parse(expiryDate, DATE_FMT).isBefore(LocalDate.now());
        } catch (Exception e) {
            // If parsing fails, assume not expired
            return false;
        }
    }

    // Convert blood unit to a CSV line for file storage
    public String toFileString() {
        return unitId + "," + bloodGroup + "," + expiryDate + "," + donorId + "," + status;
    }

    // Rebuild a BloodUnit object from a CSV line
    public static BloodUnit fromFileString(String line) {
        String[] p = line.split(",", -1);
        return new BloodUnit(p[0], p[1], p[2], p[3], p[4]);
    }

    // Compare blood units by expiry date (used in BST/AVL sorting)
    @Override
    public int compareTo(BloodUnit other) {
        return this.expiryDate.compareTo(other.expiryDate);
    }

    // String representation for logging/display
    @Override
    public String toString() {
        return "Unit " + unitId + " | Group: " + bloodGroup + " | Expiry: " + expiryDate
                + " | Donor: " + donorId + " | Status: " + status
                + (isExpired() ? " [EXPIRED]" : "");
    }
}
