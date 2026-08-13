package bloodbank.modules;

import bloodbank.io.BranchFileManager;
import bloodbank.structures.BranchGraph;

import java.util.List;
import java.util.Set;

/**
 * MEMBER 5 MODULE - owns: Graph representation of the branch/hospital
 * distribution network, plus BFS and DFS traversal.
 * Merge Sort lives in bloodbank.sort.MergeSorter.
 */
public class GraphModule {

    private final BranchGraph graph = new BranchGraph();
    private final BranchFileManager fileManager = new BranchFileManager();

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

    public void addRoute(String a, String b, int distanceKm) {
        graph.addRoute(a, b, distanceKm);
    }

    public Set<String> getBranches() {
        return graph.getBranches();
    }

    public List<String> bfs(String start) {
        return graph.bfs(start);
    }

    public List<String> dfs(String start) {
        return graph.dfs(start);
    }

    public String nearestBranchWithStock(String start, Set<String> stockedBranches) {
        return graph.nearestBranchWithStock(start, stockedBranches);
    }

    public void printGraph() {
        graph.printGraph();
    }
}
