package bloodbank.io;

import bloodbank.model.BloodRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading blood requests to/from text files.
 * Each line in the file represents a BloodRequest, with fields separated by commas.
 * Implements FileStorage<BloodRequest> so it follows a common interface.
 */
public class RequestFileManager implements FileStorage<BloodRequest> {

    @Override
    public List<BloodRequest> loadFromFile(String path) {
        List<BloodRequest> requests = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return requests; // if file not found, return empty list

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Read file line by line
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip blank lines
                try {
                    // Convert line into BloodRequest object using model's helper method
                    requests.add(BloodRequest.fromFileString(line));
                } catch (Exception e) {
                    // If line is malformed, skip it instead of crashing
                    System.out.println("Skipping malformed request line: " + line);
                }
            }
        } catch (IOException e) {
            // Handle file read errors gracefully
            System.out.println("Error reading requests file: " + e.getMessage());
        }
        return requests; // return all loaded requests
    }

    @Override
    public void saveToFile(String path, List<BloodRequest> data) {
        // Ensure parent directory exists before writing
        File parentDir = new File(path).getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            // Write each request as a line in the file
            for (BloodRequest r : data) {
                bw.write(r.toFileString()); // convert request to string format
                bw.newLine();               // move to next line
            }
        } catch (IOException e) {
            // Handle file write errors gracefully
            System.out.println("Error saving requests file: " + e.getMessage());
        }
    }
}
