package bloodbank.io;

import bloodbank.model.Transaction;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 2's component: Task 1 - File I/O for Transaction history.
 * - Append new transactions to a log file.
 * - Load existing transactions from a log file.
 * Handles saving and loading transaction logs to/from text files.
 * Implements FileStorage<Transaction> so it follows a common interface.
 */
public class TransactionFileManager implements FileStorage<Transaction> {

    @Override
    public List<Transaction> loadFromFile(String path) {
        List<Transaction> log = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return log; // if file not found, return empty list

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Read file line by line
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip blank lines
                try {
                    // Convert line into Transaction object using model's helper method
                    log.add(Transaction.fromFileString(line));
                } catch (Exception e) {
                    // Skip malformed lines instead of crashing
                    System.out.println("Skipping malformed transaction line: " + line);
                }
            }
        } catch (IOException e) {
            // Handle file read errors gracefully
            System.out.println("Error reading transaction log: " + e.getMessage());
        }
        return log; // return all loaded transactions
    }

    /** Full rewrite (used rarely - normally we append instead). */
    @Override
    public void saveToFile(String path, List<Transaction> data) {
        // Ensure parent directory exists before writing
        File parentDir = new File(path).getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            // Write each transaction as a line in the file
            for (Transaction t : data) {
                bw.write(t.toFileString()); // convert transaction to string format
                bw.newLine();               // move to next line
            }
        } catch (IOException e) {
            // Handle file write errors gracefully
            System.out.println("Error saving transaction log: " + e.getMessage());
        }
    }

    /** Append-only write - used every time a new transaction happens. */
    public void appendTransaction(String path, Transaction t) {
        // Ensure parent directory exists before writing
        File parentDir = new File(path).getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            // Append new transaction at the end of the file
            bw.write(t.toFileString());
            bw.newLine();
        } catch (IOException e) {
            // Handle file write errors gracefully
            System.out.println("Error appending to transaction log: " + e.getMessage());
        }
    }
}
