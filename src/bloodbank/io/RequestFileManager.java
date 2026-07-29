package bloodbank.io;

import bloodbank.model.BloodRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RequestFileManager implements FileStorage<BloodRequest> {

    @Override
    public List<BloodRequest> loadFromFile(String path) {
        List<BloodRequest> requests = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return requests;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    requests.add(BloodRequest.fromFileString(line));
                } catch (Exception e) {
                    System.out.println("Skipping malformed request line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading requests file: " + e.getMessage());
        }
        return requests;
    }

    @Override
    public void saveToFile(String path, List<BloodRequest> data) {
        File parentDir = new File(path).getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (BloodRequest r : data) {
                bw.write(r.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving requests file: " + e.getMessage());
        }
    }
}
