package bloodbank.io;

import bloodbank.structures.BranchGraph;
import java.io.*;

/** Loads the branches.txt edge list (branchA,branchB,distanceKm) into a BranchGraph. */
public class BranchFileManager {

    public void loadIntoGraph(String path, BranchGraph graph) {
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length == 3) {
                    graph.addRoute(p[0].trim(), p[1].trim(), Integer.parseInt(p[2].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading branches file: " + e.getMessage());
        }
    }
}
