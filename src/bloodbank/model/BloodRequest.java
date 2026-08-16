package bloodbank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Represents one blood request submitted by a Requester and tracked through the RequestQueue. */
public class BloodRequest {

    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String requestId;
    private Requester requester;
    private String status;       // Pending, Fulfilled, Rejected, Waitlisted
    private String dateCreated;
    private String fulfilledUnitId; // set once a matching unit has been issued

    public BloodRequest(String requestId, Requester requester, String status, String dateCreated) {
        this.requestId = requestId;
        this.requester = requester;
        this.status = status;
        this.dateCreated = dateCreated;
        this.fulfilledUnitId = "";
    }

    public static String now() { return LocalDateTime.now().format(TS_FMT); }

    public String getRequestId() { return requestId; }
    public Requester getRequester() { return requester; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDateCreated() { return dateCreated; }
    public String getFulfilledUnitId() { return fulfilledUnitId; }

    public void fulfill(String unitId) {
        this.status = "Fulfilled";
        this.fulfilledUnitId = unitId;
    }

    /** Reverts a Fulfilled request back to Pending, clearing the issued-unit link. Used when undoing an ISSUE_UNIT transaction. */
    public void resetToPending() {
        this.status = "Pending";
        this.fulfilledUnitId = "";
    }

    public String toFileString() {
        return requestId + "," + requester.toFileString() + "," + status + "," + dateCreated
                + "," + fulfilledUnitId;
    }

    /**
     * Rebuilds a BloodRequest from a CSV line. requester.toFileString() has 9 fields
     * (id..urgency), so the full line has: requestId + 9 requester fields + status + date + unitId = 13 fields.
     */
    public static BloodRequest fromFileString(String line) {
        String[] p = line.split(",", -1);
        Requester r = new Requester(p[1], p[2], Integer.parseInt(p[3]), p[4], p[5], p[6], p[7],
                Integer.parseInt(p[8]), p[9]);
        BloodRequest req = new BloodRequest(p[0], r, p[10], p[11]);
        if (p.length > 12 && !p[12].isEmpty()) req.fulfilledUnitId = p[12];
        return req;
    }

    @Override
    public String toString() {
        return "Request " + requestId + " | " + requester.getHospitalName()
                + " needs " + requester.getQuantity() + " x " + requester.getRequiredBloodGroup()
                + " | Urgency: " + requester.getUrgency() + " | Status: " + status
                + " | Created: " + dateCreated;
    }
}