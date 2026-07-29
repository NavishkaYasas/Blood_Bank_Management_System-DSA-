package bloodbank.io;

import bloodbank.model.Admin;
import java.io.*;

/** Loads (or creates, if missing) the single fixed admin credential pair from admin.txt. */
public class AdminFileManager {
    


    public Admin loadAdmin(String path) {
        File file = new File(path);
        if (!file.exists()) {
            createDefaultAdmin(path);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null && !line.trim().isEmpty()) {
                String[] p = line.split(",");
                return new Admin(p[0], p[1]);
            }
        } catch (IOException e) {
            System.out.println("Error reading admin file: " + e.getMessage());
        }
        return new Admin("admin", "admin123"); // fallback
    }

    private void createDefaultAdmin(String path) {
        File parentDir = new File(path).getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("admin,admin123");
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error creating default admin file: " + e.getMessage());
        }
    }
}