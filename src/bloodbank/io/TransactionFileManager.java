package bloodbank.io;

import bloodbank.model.Transaction;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileManager implements FileStorage<Transaction> {

    @Override
    public List<Transaction> loadFromFile(String path) {
        List<Transaction> log = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return log;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    log.add(Transaction.fromFileString(line));
                } catch (Exception e) {
                    System.out.println("Skipping malformed transaction line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transaction log: " + e.getMessage());
        }
        return log;
    }

    /** Full rewrite (used rarely - normally we append instead). */
    @Override
    public void saveToFile(String path, List<Transaction> data) {
        File parentDir = new File(path).getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Transaction t : data) {
                bw.write(t.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving transaction log: " + e.getMessage());
        }
    }

    /** Append-only write - used every time a new transaction happens. */
    public void appendTransaction(String path, Transaction t) {
        File parentDir = new File(path).getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(t.toFileString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error appending to transaction log: " + e.getMessage());
        }
    }
}
