package bloodbank.io;

import bloodbank.model.Donor;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading donor data to/from text files.
 * Implements FileStorage<Donor> so it follows a common interface.
 */
public class DonorFileManager implements FileStorage<Donor> {

    @Override
    public List<Donor> loadFromFile(String path) {
        List<Donor> donors = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return donors; // if file not found, return empty list

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Read file line by line
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip blank lines
                try {
                    // Convert line into Donor object using model's helper method
                    donors.add(Donor.fromFileString(line));
                } catch (Exception e) {
                    // If line is malformed, skip it instead of crashing
                    System.out.println("Skipping malformed donor line: " + line);
                }
            }
        } catch (IOException e) {
            // Handle file read errors gracefully
            System.out.println("Error reading donors file: " + e.getMessage());
        }
        return donors; // return all loaded donors
    }

    @Override
    public void saveToFile(String path, List<Donor> data) {
        // Ensure parent directory exists before writing
        File parentDir = new File(path).getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            // Write each donor as a line in the file
            for (Donor d : data) {
                bw.write(d.toFileString()); // convert donor to string format
                bw.newLine();               // move to next line
            }
        } catch (IOException e) {
            // Handle file write errors gracefully
            System.out.println("Error saving donors file: " + e.getMessage());
        }
    }
}
