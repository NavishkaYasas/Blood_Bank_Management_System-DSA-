package bloodbank.io;  // Defines the package location of this class

import bloodbank.model.BloodUnit;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// This class manages saving and loading BloodUnit objects to/from a file.
// It implements the FileStorage interface for BloodUnit type.
public class BloodUnitFileManager implements FileStorage<BloodUnit> {

    @Override
    public List<BloodUnit> loadFromFile(String path){
        List<BloodUnit> units = new ArrayList<>();  // Stores loaded blood units
        File file = new File(path);  // Creates a File object pointing to the given path

        // If the file does not exist, return an empty list
        if (!file.exists()){
            return units;
        }

        // Try-with-resources ensures BufferedReader is closed automatically
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            // Read each line until end of file
            while ((line = br.readLine()) != null){
                // Skip empty lines
                if(line.trim().isEmpty()){
                    continue;
                }
                try{
                    // Convert line into a BloodUnit object and add to list
                    units.add(BloodUnit.fromFileString(line));
                }
                catch (Exception e){
                    // If parsing fails, skip that line and log a message
                    System.out.println("Skipping malformed blood unit line: " + line);
                }

            }
        }
        catch (IOException e){
            // Handles any file reading errors
            System.out.println("Error reading blood units file: " + e.getMessage());
        }
        return units;  // Return the list of loaded blood units
    }

    @Override
    public void saveToFile(String path, List<BloodUnit> data){
        // Ensure parent directory exists before writing
        File parentDir = new File(path).getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();

        // Try-with-resources ensures BufferedWriter is closed automatically
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            // Write each BloodUnit object to the file
            for(BloodUnit bu : data){
                bw.write(bu.toFileString());  // Convert object to string format
                bw.newLine();  // Add a newline after each entry
            }
        }
        catch (IOException e){
            // Handles any file writing errors
            System.out.println("Error saving blood units file: " + e.getMessage());
        }



    }


}
