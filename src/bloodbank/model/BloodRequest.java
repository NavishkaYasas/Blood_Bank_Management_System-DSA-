package bloodbank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Member 2's component: Task 2 - BloodRequest model class.
 * Represents a request for blood from a hospital or patient.
 * Contains details about the requester, blood type, quantity, urgency, and timestamp.
 * Provides methods to convert to/from CSV format for file storage
 * Represents one blood request submitted by a Requester and tracked through the RequestQueue.
 * Each request has an ID, requester details, status, creation date, and (optionally) a fulfilled unit ID.
 */
public class BloodRequest {

    // Formatter for timestamps (yyyy-MM-dd HH:mm:ss)
    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Unique ID for the request
    private String requestId;
    // The requester (hospital/person) who submitted the request
    private Requester requester;
    // Current status: Pending, Fulfilled, Rejected, Waitlisted
    private String status;
    // Date/time when the request was created
    private String dateCreated;
    // ID of the blood unit issued to fulfill this request (empty until fulfilled)
    private String fulfilledUnitId;

    // Constructor: initializes a new BloodRequest with given details
    public BloodRequest(String requestId, Requester requester, String status, String dateCreated) {
        this.requestId = requestId;
        this.requester = requester;
        this.status = status;
        this.dateCreated = dateCreated;
        this.fulfilledUnitId = ""; // initially empty
    }

    // Utility method: returns current timestamp in formatted string
    public static String now() {
        return LocalDateTime.now().format(TS_FMT);
    }

    // Getters and setters
    public String getRequestId() { return requestId; }
    public Requester getRequester() { return requester; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDateCreated() { return dateCreated; }
    public String getFulfilledUnitId() { return fulfilledUnitId; }

    // Fulfill the request: mark status as Fulfilled and link to a blood unit ID
    public void fulfill(String unitId) {
        this.status = "Fulfilled";
        this.fulfilledUnitId = unitId;
    }

    /**
     * Reset a fulfilled request back to Pending.
     * Clears the issued unit link. Used when undoing an ISSUE_UNIT transaction.
     */
    public void resetToPending() {
        this.status = "Pending";
        this.fulfilledUnitId = "";
    }

    // Convert request to a CSV line for file storage
    public String toFileString() {
        return requestId + "," + requester.toFileString() + "," + status + "," + dateCreated
                + "," + fulfilledUnitId;
    }

    /**
     * Rebuilds a BloodRequest from a CSV (Comma-Separated Values) line.
     * requester.toFileString() has 9 fields (id..urgency),
     * so the full line has: requestId + 9 requester fields + status + date + unitId = 13 fields.
     */
    public static BloodRequest fromFileString(String line) {
        String[] p = line.split(",", -1);
        // Reconstruct requester from its 9 fields
        Requester r = new Requester(p[1], p[2], Integer.parseInt(p[3]), p[4], p[5], p[6], p[7],
                Integer.parseInt(p[8]), p[9]);
        // Build BloodRequest with requestId, requester, status, date
        BloodRequest req = new BloodRequest(p[0], r, p[10], p[11]);
        // If fulfilled unit ID exists, set it
        if (p.length > 12 && !p[12].isEmpty()) req.fulfilledUnitId = p[12];
        return req;
    }

    // String representation for logging/display
    @Override
    public String toString() {
        return "Request " + requestId + " | " + requester.getHospitalName()
                + " needs " + requester.getQuantity() + " x " + requester.getRequiredBloodGroup()
                + " | Urgency: " + requester.getUrgency() + " | Status: " + status
                + " | Created: " + dateCreated;
    }
}
