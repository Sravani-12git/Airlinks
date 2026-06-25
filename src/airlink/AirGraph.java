package airlink;

import java.util.*;

public class AirGraph {
    private Map<String, List<Edge>> adjacencyList;
    private Map<String, Airport> airports;
    private Map<String, Route> routes;

    // Make Edge class public static so it can be accessed from other classes
    public static class Edge {
        public String destination;
        public double weight;
        public String routeId;

        public Edge(String destination, double weight, String routeId) {
            this.destination = destination;
            this.weight = weight;
            this.routeId = routeId;
        }
        
        @Override
        public String toString() {
            return String.format("-> %s (%.0fkm, %s)", destination, weight, routeId);
        }
    }

    public AirGraph() {
        adjacencyList = new HashMap<>();
        airports = new HashMap<>();
        routes = new HashMap<>();
    }

    public void addAirport(Airport airport) {
        airports.put(airport.getAirportId(), airport);
        adjacencyList.putIfAbsent(airport.getAirportId(), new ArrayList<>());
    }

    public void addRoute(Route route) {
        routes.put(route.getRouteId(), route);
        
        // Make sure the source airport exists in the adjacency list
        String sourceId = route.getSourceAirportId();
        if (!adjacencyList.containsKey(sourceId)) {
            adjacencyList.put(sourceId, new ArrayList<>());
        }
        
        // Add the edge
        adjacencyList.get(sourceId).add(
            new Edge(route.getDestAirportId(), route.getDistanceKm(), route.getRouteId())
        );
        
        // Also ensure destination airport exists in adjacency list
        String destId = route.getDestAirportId();
        if (!adjacencyList.containsKey(destId)) {
            adjacencyList.put(destId, new ArrayList<>());
        }
    }

    // BFS - Find all airports reachable within certain distance
    public List<String> bfs(String startId, double maxDistance) {
        List<String> reachable = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        
        visited.add(startId);
        queue.add(startId);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            reachable.add(current);
            
            for (Edge edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                if (edge.weight <= maxDistance && !visited.contains(edge.destination)) {
                    visited.add(edge.destination);
                    queue.add(edge.destination);
                }
            }
        }
        return reachable;
    }

    // DFS - Detect cycles in flight network
    public boolean detectCycle() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        
        for (String airportId : adjacencyList.keySet()) {
            if (detectCycleUtil(airportId, visited, recursionStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean detectCycleUtil(String node, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(node)) {
            return true;
        }
        
        if (visited.contains(node)) {
            return false;
        }
        
        visited.add(node);
        recursionStack.add(node);
        
        for (Edge edge : adjacencyList.getOrDefault(node, new ArrayList<>())) {
            if (detectCycleUtil(edge.destination, visited, recursionStack)) {
                return true;
            }
        }
        
        recursionStack.remove(node);
        return false;
    }

    // Find minimum spanning tree using Prim's algorithm
    public List<Edge> primMST(String startId) {
        List<Edge> mst = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));
        
        if (!adjacencyList.containsKey(startId)) {
            return mst;
        }
        
        visited.add(startId);
        pq.addAll(adjacencyList.getOrDefault(startId, new ArrayList<>()));
        
        while (!pq.isEmpty() && visited.size() < adjacencyList.size()) {
            Edge edge = pq.poll();
            if (visited.contains(edge.destination)) {
                continue;
            }
            
            visited.add(edge.destination);
            mst.add(edge);
            
            for (Edge nextEdge : adjacencyList.getOrDefault(edge.destination, new ArrayList<>())) {
                if (!visited.contains(nextEdge.destination)) {
                    pq.add(nextEdge);
                }
            }
        }
        
        return mst;
    }

    // Dijkstra's Algorithm - Shortest path
    public List<String> dijkstra(String startId, String endId) {
        Map<String, String> parent = new HashMap<>();
        Map<String, Double> distance = new HashMap<>();
        PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(
            Comparator.comparingDouble(Map.Entry::getValue)
        );
        
        for (String airportId : adjacencyList.keySet()) {
            distance.put(airportId, Double.MAX_VALUE);
        }
        
        if (!distance.containsKey(startId) || !distance.containsKey(endId)) {
            return new ArrayList<>();
        }
        
        distance.put(startId, 0.0);
        pq.add(new AbstractMap.SimpleEntry<>(startId, 0.0));
        
        while (!pq.isEmpty()) {
            Map.Entry<String, Double> entry = pq.poll();
            String current = entry.getKey();
            double currentDist = entry.getValue();
            
            if (currentDist > distance.get(current)) {
                continue;
            }
            
            if (current.equals(endId)) {
                break;
            }
            
            for (Edge edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                double newDist = currentDist + edge.weight;
                if (newDist < distance.get(edge.destination)) {
                    distance.put(edge.destination, newDist);
                    parent.put(edge.destination, current);
                    pq.add(new AbstractMap.SimpleEntry<>(edge.destination, newDist));
                }
            }
        }
        
        List<String> path = new ArrayList<>();
        String current = endId;
        while (current != null && parent.containsKey(current)) {
            path.add(0, current);
            current = parent.get(current);
        }
        if (!path.isEmpty() && !path.get(0).equals(startId)) {
            path.add(0, startId);
        }
        
        return path;
    }

    // Bellman-Ford Algorithm
    public Map<String, Double> bellmanFord(String startId) {
        Map<String, Double> distance = new HashMap<>();
        
        for (String airportId : adjacencyList.keySet()) {
            distance.put(airportId, Double.MAX_VALUE);
        }
        distance.put(startId, 0.0);
        
        int V = adjacencyList.size();
        
        List<Map.Entry<String, Edge>> allEdges = new ArrayList<>();
        for (String source : adjacencyList.keySet()) {
            for (Edge edge : adjacencyList.get(source)) {
                allEdges.add(new AbstractMap.SimpleEntry<>(source, edge));
            }
        }
        
        for (int i = 0; i < V - 1; i++) {
            for (Map.Entry<String, Edge> entry : allEdges) {
                String source = entry.getKey();
                Edge edge = entry.getValue();
                if (distance.get(source) != Double.MAX_VALUE && 
                    distance.get(source) + edge.weight < distance.get(edge.destination)) {
                    distance.put(edge.destination, distance.get(source) + edge.weight);
                }
            }
        }
        
        return distance;
    }

    // Floyd-Warshall Algorithm - All pairs shortest path
    public double[][] floydWarshall() {
        int n = airports.size();
        List<String> airportIds = new ArrayList<>(airports.keySet());
        double[][] dist = new double[n][n];
        
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Double.MAX_VALUE);
            dist[i][i] = 0;
        }
        
        for (int i = 0; i < n; i++) {
            String source = airportIds.get(i);
            for (Edge edge : adjacencyList.getOrDefault(source, new ArrayList<>())) {
                int j = airportIds.indexOf(edge.destination);
                if (j != -1) {
                    dist[i][j] = edge.weight;
                }
            }
        }
        
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Double.MAX_VALUE && dist[k][j] != Double.MAX_VALUE) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }
        
        return dist;
    }

    // Topological sort for scheduling
    public List<String> topologicalSort() {
        Map<String, Integer> inDegree = new HashMap<>();
        for (String airportId : adjacencyList.keySet()) {
            inDegree.put(airportId, 0);
        }
        
        for (String source : adjacencyList.keySet()) {
            for (Edge edge : adjacencyList.get(source)) {
                inDegree.put(edge.destination, inDegree.getOrDefault(edge.destination, 0) + 1);
            }
        }
        
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }
        
        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);
            
            for (Edge edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                int degree = inDegree.get(edge.destination) - 1;
                inDegree.put(edge.destination, degree);
                if (degree == 0) {
                    queue.add(edge.destination);
                }
            }
        }
        
        return result;
    }

    public void printGraph() {
        System.out.println("Airport Network:");
        for (String airportId : adjacencyList.keySet()) {
            Airport airport = airports.get(airportId);
            String airportName = (airport != null) ? airport.getAirportName() : "Unknown";
            System.out.println(airportId + " (" + airportName + "):");
            for (Edge edge : adjacencyList.get(airportId)) {
                Airport dest = airports.get(edge.destination);
                String destName = (dest != null) ? dest.getAirportName() : "Unknown";
                System.out.println("  -> " + edge.destination + " (" + destName + ") - " + edge.weight + " km");
            }
        }
    }

    public Map<String, Airport> getAirports() {
        return airports;
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }
}