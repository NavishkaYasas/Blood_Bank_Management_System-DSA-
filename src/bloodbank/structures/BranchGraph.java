package bloodbank.structures;

import java.util.*;

// Graph that represents blood bank branches connected by routes
public class BranchGraph {

    // Represents a connection to a neighbor branch with a distance
    public static class Edge {
        public String neighbor;
        public int distanceKm;
        public Edge(String neighbor, int distanceKm) {
            this.neighbor = neighbor;
            this.distanceKm = distanceKm;
        }
    }

    // Adjacency list - stores each branch and its connections
    private Map<String, List<Edge>> adjList = new LinkedHashMap<>();

    // Add a branch to the graph
    public void addBranch(String name) {
        adjList.putIfAbsent(name, new ArrayList<>());
    }

    // Add a route between two branches (both directions)
    public void addRoute(String a, String b, int distanceKm) {
        addBranch(a);
        addBranch(b);
        adjList.get(a).add(new Edge(b, distanceKm));
        adjList.get(b).add(new Edge(a, distanceKm));
    }

    // Get all branch names
    public Set<String> getBranches() { return adjList.keySet(); }

    // BFS - visits branches level by level using a queue
    public List<String> bfs(String start) {
        List<String> visitedOrder = new ArrayList<>();
        if (!adjList.containsKey(start)) return visitedOrder;

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll(); // Take next from queue
            visitedOrder.add(current);
            for (Edge e : adjList.get(current)) { // Visit all neighbors
                if (!visited.contains(e.neighbor)) {
                    visited.add(e.neighbor);
                    queue.add(e.neighbor);
                }
            }
        }
        return visitedOrder;
    }

    // DFS - visits branches by going as deep as possible first
    public List<String> dfs(String start) {
        List<String> visitedOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfsRec(start, visited, visitedOrder);
        return visitedOrder;
    }

    // Recursive helper for DFS
    private void dfsRec(String current, Set<String> visited, List<String> visitedOrder) {
        if (visited.contains(current) || !adjList.containsKey(current)) return;
        visited.add(current);
        visitedOrder.add(current);
        for (Edge e : adjList.get(current)) { // Go deeper into each unvisited neighbor
            if (!visited.contains(e.neighbor)) {
                dfsRec(e.neighbor, visited, visitedOrder);
            }
        }
    }

    // Find the nearest branch that has blood stock using BFS
    public String nearestBranchWithStock(String start, Set<String> stockedBranches) {
        if (!adjList.containsKey(start)) return null;
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (stockedBranches.contains(current)) return current; // Found a branch with stock
            for (Edge e : adjList.get(current)) {
                if (!visited.contains(e.neighbor)) {
                    visited.add(e.neighbor);
                    queue.add(e.neighbor);
                }
            }
        }
        return null; // No stocked branch found
    }

    // Print the graph showing each branch and its connections
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
