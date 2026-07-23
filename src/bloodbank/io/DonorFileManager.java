package bloodbank.io;

import bloodbank.model.Donor;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DonorFileManager {

    public List<Donor> loadFromFile(String path) {
        List<Donor> donors = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return donors;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    donors.add(Donor.fromFileString(line));
                } catch (Exception e) {
                    System.out.println("Skipping malformed donor line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading donors file: " + e.getMessage());
        }
        return donors;
    }

    public void saveToFile(String path, List<Donor> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Donor d : data) {
                bw.write(d.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving donors file: " + e.getMessage());
        }
    }
}