package bloodbank.io;

import bloodbank.model.BloodUnit;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BloodUnitFileManager {
    public List<BloodUnit> loadFromFile(String path){
        List<BloodUnit> units = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()){
            return units;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine()) != null){
                if(line.trim().isEmpty()){
                    continue;
                }
                try{
                    units.add(BloodUnit.fromFileString(line));
                }
                catch (Exception e){
                    System.out.println("Skipping malfromed blood unit line: " + line);
                }

            }
        }
        catch (IOException e){
            System.out.println("Error reading blood units file: " + e.getMessage());
        }
        return units;
    }

    public void saveToFile(String path, List<BloodUnit> data){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            for(BloodUnit bu : data){
                bw.write(bu.toFileString());
                bw.newLine();
            }
        }
        catch (IOException e){
            System.out.println("Error saving blood units file: " + e.getMessage());
        }



    }


}
