package bloodbank.io; // Defines the package location of this class

import bloodbank.model.Admin; // Imports the Admin class from the model package
import java.io.*;  // Imports Java I/O classes for file handling

/** Loads (or creates, if missing) the single fixed admin credential pair from admin.txt. */
public class AdminFileManager {


    // Method to load admin credentials from a file
    public Admin loadAdmin(String path) {
        File file = new File(path);  // Creates a File object pointing to the given path

        // If the file does not exist, create a default admin file
        if (!file.exists()) {
            createDefaultAdmin(path);   // Creates a File object pointing to the given path
        }

        // Try-with-resources ensures BufferedReader is closed automatically
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            // If the line is not empty, split it into username and password
            if (line != null && !line.trim().isEmpty()) {
                String[] p = line.split(",");  // Splits by comma → [username, password]
                return new Admin(p[0], p[1]);      // Returns an Admin object with credentials
            }
        } catch (IOException e) {
            // Handles any file reading errors
            System.out.println("Error reading admin file: " + e.getMessage());
        }
        // If reading fails, return a fallback default admin
        return new Admin("admin", "admin123"); // fallback
    }

    // Method to create a default admin file if missing

    private void createDefaultAdmin(String path) {
        File parentDir = new File(path).getParentFile();   // Gets parent directory of the file
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();

        // Try-with-resources ensures BufferedWriter is closed automatically
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("admin,admin123");  // Writes default credentials into the file
            bw.newLine();    // Adds a newline after writing
        } catch (IOException e) {

            // Handles any file writing errors
            System.out.println("Error creating default admin file: " + e.getMessage());
        }
    }
}