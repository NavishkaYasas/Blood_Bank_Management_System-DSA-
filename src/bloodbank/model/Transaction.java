package bloodbank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Member 2's component: Task 2 - BloodRequest model class.
 * Represents a request for blood from a hospital or patient.
 * Contains details about the requester, blood type, quantity, urgency, and timestamp.
 * Provides methods to convert to/from CSV format for file storage
 * Represents one logged admin action.
 * Stored in the TransactionStack and transactions_log.txt.
 */
public class Transaction {

    // Formatter for timestamps (yyyy-MM-dd HH:mm:ss)
    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Fields
    private String action;    // e.g. "ADD_DONOR", "DELETE_DONOR", "ISSUE_UNIT"
    private String details;   // human-readable description of the action
    private String timestamp; // when the action occurred

    // Constructor: create a new transaction with current timestamp
    public Transaction(String action, String details) {
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now().format(TS_FMT); // auto-generate timestamp
    }

    // Constructor: create a transaction with a given timestamp (used when loading from file)
    public Transaction(String action, String details, String timestamp) {
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    // Getters
    public String getAction() { return action; }
    public String getDetails() { return details; }
    public String getTimestamp() { return timestamp; }

    // Convert transaction into a CSV line for file storage
    public String toFileString() {
        return timestamp + "," + action + "," + details.replace(",", ";");
    }

    // Rebuild a Transaction object from a CSV line
    public static Transaction fromFileString(String line) {
        String[] p = line.split(",", 3); // split into 3 parts: timestamp, action, details
        return new Transaction(p[1], p.length > 2 ? p[2] : "", p[0]);
    }

    // String representation for logging/display
    @Override
    public String toString() {
        return "[" + timestamp + "] " + action + " - " + details;
    }
}
