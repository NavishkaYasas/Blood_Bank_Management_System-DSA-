package bloodbank.modules;

import bloodbank.io.BranchFileManager;
import bloodbank.structures.BranchGraph;

import java.util.List;
import java.util.Set;

/**
 * MEMBER 5 MODULE
 * - Owns: Graph representation of the branch/hospital distribution network.
 * - Provides BFS and DFS traversal methods.
 * - Merge Sort lives separately in bloodbank.sort.MergeSorter.
 */
public class GraphModule {

    // Internal graph structure representing branches and routes
    private final BranchGraph graph = new BranchGraph();
    // File manager for loading routes from branches.txt
    private final BranchFileManager fileManager = new BranchFileManager();

    // Load branch network from file into the graph
    public void loadFromFile(String filePath) {
        fileManager.loadIntoGraph(filePath, graph);
    }

    /** Seeds a small sample network so the graph demo works without manual setup. */
    public void seedDefaultNetwork() {
        if (graph.getBranches().isEmpty()) {
            graph.addRoute("Colombo", "Negombo", 35);
            graph.addRoute("Colombo", "Kandy", 115);
            graph.addRoute("Kandy", "Kurunegala", 42);
            graph.addRoute("Negombo", "Kurunegala", 60);
            graph.addRoute("Kandy", "Badulla", 90);
        }
    }

    // Add a new route between two branches
    public void addRoute(String a, String b, int distanceKm) {
        graph.addRoute(a, b, distanceKm);
    }

    // Get all branch names in the network
    public Set<String> getBranches() {
        return graph.getBranches();
    }

    // Breadth-First Search traversal from a starting branch
    public List<String> bfs(String start) {
        return graph.bfs(start);
    }

    // Depth-First Search traversal from a starting branch
    public List<String> dfs(String start) {
        return graph.dfs(start);
    }

    // Find the nearest branch (by distance) that has stock available
    public String nearestBranchWithStock(String start, Set<String> stockedBranches) {
        return graph.nearestBranchWithStock(start, stockedBranches);
    }

    // Print the graph structure (for debugging/demo purposes)
    public void printGraph() {
        graph.printGraph();
    }
}
