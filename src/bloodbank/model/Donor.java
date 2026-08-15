package bloodbank.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Donor extends Person (INHERITANCE).
 * Implements Comparable<Donor> so donors can be sorted (POLYMORPHISM via
 * the SortStrategy interface, which relies on Comparator/Comparable).
 */
public class Donor extends Person implements Comparable<Donor> {

    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String bloodGroup;
    private String lastDonationDate;   // yyyy-MM-dd
    private String healthAnswers;      // semicolon-separated answers
    private boolean certUploaded;      // placeholder flag for future feature

    public Donor(String id, String name, int age, String contactNo, String address,
                 String bloodGroup, String lastDonationDate, String healthAnswers, boolean certUploaded) {
        super(id, name, age, contactNo, address);
        this.bloodGroup = bloodGroup;
        this.lastDonationDate = lastDonationDate;
        this.healthAnswers = healthAnswers;
        this.certUploaded = certUploaded;
    }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(String lastDonationDate) { this.lastDonationDate = lastDonationDate; }

    public String getHealthAnswers() { return healthAnswers; }
    public void setHealthAnswers(String healthAnswers) { this.healthAnswers = healthAnswers; }

    public boolean isCertUploaded() { return certUploaded; }
    public void setCertUploaded(boolean certUploaded) { this.certUploaded = certUploaded; }

    /**
     * A donor is eligible only if BOTH conditions hold:
     *  - at least 90 days have passed since their last donation date, AND
     *  - none of their health-questionnaire answers disqualify them
     *    (must be over 50kg, and must answer "no" to chronic illness,
     *    recent surgery, and currently on medication).
     * Records that don't use the "Key:answer;..." format (e.g. older test
     * data) are unaffected by the questionnaire check and fall back to the
     * 90-day rule alone.
     */
    public boolean isEligible() {
        boolean dateOk;
        try {
            LocalDate last = LocalDate.parse(lastDonationDate, DATE_FMT);
            long days = ChronoUnit.DAYS.between(last, LocalDate.now());
            dateOk = days >= 90;
        } catch (Exception e) {
            dateOk = true; // no valid previous donation on record => doesn't block eligibility
        }
        return dateOk && passesHealthQuestionnaire();
    }

    /** Checks the four health-questionnaire flags recorded in healthAnswers, if present. */
    public boolean passesHealthQuestionnaire() {
        if (healthAnswers == null) return true;
        String a = healthAnswers.toLowerCase();
        if (a.contains("weight>50kg:no")) return false;
        if (a.contains("chronicillness:yes")) return false;
        if (a.contains("recentsurgery:yes")) return false;
        if (a.contains("onmedication:yes")) return false;
        return true;
    }

    /** Serialises this donor into one CSV line for donors.txt */
    public String toFileString() {
        String safeHealth = healthAnswers == null ? "" : healthAnswers.replace(",", ";");
        return getId() + "," + getName() + "," + getAge() + "," + getContactNo() + ","
                + getAddress().replace(",", ";") + "," + bloodGroup + "," + lastDonationDate + ","
                + safeHealth + "," + certUploaded;
    }

    /** Rebuilds a Donor from one CSV line of donors.txt */
    public static Donor fromFileString(String line) {
        String[] p = line.split(",", -1);
        return new Donor(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], p[5], p[6], p[7],
                Boolean.parseBoolean(p[8]));
    }

    @Override
    public int compareTo(Donor other) {
        return this.getName().compareToIgnoreCase(other.getName());
    }

    @Override
    public String toString() {
        return super.toString() + " | Blood Group: " + bloodGroup
                + " | Last Donation: " + lastDonationDate
                + " | Eligible: " + (isEligible() ? "Yes" : "No")
                + " | Cert Uploaded: " + certUploaded;
    }
}
