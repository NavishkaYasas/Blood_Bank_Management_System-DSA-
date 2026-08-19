package bloodbank.structures;

import java.util.*;

/**
 * Member 5's component: Task 6 - Graphs.
 * Undirected, weighted graph of blood bank branches / partner hospitals.
 * - Adjacency list representation using Map<String, List<Edge>>.
 * - BFS and DFS implemented manually (no library graph algorithms).
 */
public class BranchGraph {

    // Inner class representing an edge (neighbor + distance)
    public static class Edge {
        public String neighbor;
        public int distanceKm;
        public Edge(String neighbor, int distanceKm) {
            this.neighbor = neighbor;
            this.distanceKm = distanceKm;
        }
    }

    // Adjacency list: branch name → list of edges
    private Map<String, List<Edge>> adjList = new LinkedHashMap<>();

    // ---- Graph construction ----
    public void addBranch(String name) {
        adjList.putIfAbsent(name, new ArrayList<>());
    }

    public void addRoute(String a, String b, int distanceKm) {
        addBranch(a);
        addBranch(b);
        adjList.get(a).add(new Edge(b, distanceKm));
        adjList.get(b).add(new Edge(a, distanceKm)); // undirected
    }

    public Set<String> getBranches() { return adjList.keySet(); }

    // ---- Traversals ----
    /** Breadth-First Search from a starting branch. O(V + E). */
    public List<String> bfs(String start) {
        List<String> visitedOrder = new ArrayList<>();
        if (!adjList.containsKey(start)) return visitedOrder;

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            visitedOrder.add(current);
            for (Edge e : adjList.get(current)) {
                if (!visited.contains(e.neighbor)) {
                    visited.add(e.neighbor);
                    queue.add(e.neighbor);
                }
            }
        }
        return visitedOrder;
    }

    /** Depth-First Search from a starting branch. O(V + E). */
    public List<String> dfs(String start) {
        List<String> visitedOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfsRec(start, visited, visitedOrder);
        return visitedOrder;
    }

    private void dfsRec(String current, Set<String> visited, List<String> visitedOrder) {
        if (visited.contains(current) || !adjList.containsKey(current)) return;
        visited.add(current);
        visitedOrder.add(current);
        for (Edge e : adjList.get(current)) {
            if (!visited.contains(e.neighbor)) {
                dfsRec(e.neighbor, visited, visitedOrder);
            }
        }
    }

    // ---- Practical BFS use case ----
    /**
     * Finds the nearest branch (by hop count via BFS) that appears in stockedBranches.
     * Returns null if none is reachable.
     * Example: "Which branch with required blood type can I reach fastest?"
     */
    public String nearestBranchWithStock(String start, Set<String> stockedBranches) {
        if (!adjList.containsKey(start)) return null;
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (stockedBranches.contains(current)) return current;
            for (Edge e : adjList.get(current)) {
                if (!visited.contains(e.neighbor)) {
                    visited.add(e.neighbor);
                    queue.add(e.neighbor);
                }
            }
        }
        return null;
    }

    // ---- Debugging / Demo ----
    public void printGraph() {
        for (String branch : adjList.keySet()) {
            StringBuilder sb = new StringBuilder(branch + " -> ");
            for (Edge e : adjList.get(branch)) {
                sb.append(e.neighbor).append(" (").append(e.distanceKm).append("km) ");
            }
            System.out.println(sb.toString());
        }
    }
}
