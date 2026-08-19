package bloodbank.io;

import bloodbank.structures.BranchGraph;
import java.io.*;

/**
 * Loads the branches.txt edge list (branchA,branchB,distanceKm) into a BranchGraph.
 * Each line in the file represents a route between two branches with a distance.
 */
public class BranchFileManager {

    // Method: read file and insert routes into the graph
    public void loadIntoGraph(String path, BranchGraph graph) {
        File file = new File(path);
        if (!file.exists()) return; // if file not found, do nothing

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Read file line by line
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip empty lines
                String[] p = line.split(","); // split by comma
                if (p.length == 3) {
                    // p[0] = branchA, p[1] = branchB, p[2] = distance
                    graph.addRoute(
                            p[0].trim(),                     // branch A name
                            p[1].trim(),                     // branch B name
                            Integer.parseInt(p[2].trim())    // distance in km
                    );
                }
            }
        } catch (IOException e) {
            // Handle file read errors gracefully
            System.out.println("Error reading branches file: " + e.getMessage());
        }
    }
}
