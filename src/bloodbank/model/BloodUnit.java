package bloodbank.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents one unit of blood in inventory.
 * Implements Comparable<BloodUnit> by expiryDate so it can be stored
 * in the BST/AVL tree (ordered by expiry) and sorted.
 */
public class BloodUnit implements Comparable<BloodUnit> {

    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String unitId;
    private String bloodGroup;
    private String expiryDate; // yyyy-MM-dd
    private String donorId;
    private String status;     // Available, Reserved, Issued, Expired

    public BloodUnit(String unitId, String bloodGroup, String expiryDate, String donorId, String status) {
        this.unitId = unitId;
        this.bloodGroup = bloodGroup;
        this.expiryDate = expiryDate;
        this.donorId = donorId;
        this.status = status;
    }

    public String getUnitId() { return unitId; }
    public String getBloodGroup() { return bloodGroup; }
    public String getExpiryDate() { return expiryDate; }
    public String getDonorId() { return donorId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isExpired() {
        try {
            return LocalDate.parse(expiryDate, DATE_FMT).isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    public String toFileString() {
        return unitId + "," + bloodGroup + "," + expiryDate + "," + donorId + "," + status;
    }

    public static BloodUnit fromFileString(String line) {
        String[] p = line.split(",", -1);
        return new BloodUnit(p[0], p[1], p[2], p[3], p[4]);
    }

    @Override
    public int compareTo(BloodUnit other) {
        return this.expiryDate.compareTo(other.expiryDate);
    }

    @Override
    public String toString() {
        return "Unit " + unitId + " | Group: " + bloodGroup + " | Expiry: " + expiryDate
                + " | Donor: " + donorId + " | Status: " + status
                + (isExpired() ? " [EXPIRED]" : "");
    }
}
