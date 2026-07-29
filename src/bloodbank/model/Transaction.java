package bloodbank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Represents one logged admin action, stored in the TransactionStack and transactions_log.txt */
public class Transaction {

    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String action;   // e.g. "ADD_DONOR", "DELETE_DONOR", "ISSUE_UNIT"
    private String details;  // human-readable description
    private String timestamp;

    public Transaction(String action, String details) {
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now().format(TS_FMT);
    }

    public Transaction(String action, String details, String timestamp) {
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getAction() { return action; }
    public String getDetails() { return details; }
    public String getTimestamp() { return timestamp; }

    public String toFileString() {
        return timestamp + "," + action + "," + details.replace(",", ";");
    }

    public static Transaction fromFileString(String line) {
        String[] p = line.split(",", 3);
        return new Transaction(p[1], p.length > 2 ? p[2] : "", p[0]);
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + action + " - " + details;
    }
}
