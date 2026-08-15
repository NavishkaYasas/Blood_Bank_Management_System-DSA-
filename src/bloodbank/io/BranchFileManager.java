package bloodbank.io;

import bloodbank.structures.BranchGraph;
import java.io.*;

// This class reads branch data from a text file and loads it into a graph
public class BranchFileManager {

    // Read the file and add routes to the graph
    public void loadIntoGraph(String path, BranchGraph graph) {
        File file = new File(path);
        if (!file.exists()) return; // If file doesn't exist, do nothing

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) { // Read each line
                if (line.trim().isEmpty()) continue; // Skip empty lines
                String[] p = line.split(","); // Split by comma
                if (p.length == 3) { // Each line should have: branchA, branchB, distance
                    graph.addRoute(p[0].trim(), p[1].trim(), Integer.parseInt(p[2].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading branches file: " + e.getMessage());
        }
    }
}
