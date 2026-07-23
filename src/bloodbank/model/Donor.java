package bloodbank.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Donor extends Person implements Comparable<Donor> {

    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String bloodGroup;
    private String lastDonationDate;
    private String healthAnswers;
    private boolean certUploaded;

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

    public boolean isEligible() {
        try {
            LocalDate last = LocalDate.parse(lastDonationDate, DATE_FMT);
            long days = ChronoUnit.DAYS.between(last, LocalDate.now());
            return days >= 90;
        } catch (Exception e) {
            return true;
        }
    }

    public String toFileString() {
        String safeHealth = healthAnswers == null ? "" : healthAnswers.replace(",", ";");
        return getId() + "," + getName() + "," + getAge() + "," + getContactNo() + ","
                + getAddress().replace(",", ";") + "," + bloodGroup + "," + lastDonationDate + ","
                + safeHealth + "," + certUploaded;
    }

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