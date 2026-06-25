package OptimAlgorithms;

import airlink.Flight;
import java.util.*;

public class OptimAlgorithms {
    
    // Gate Allocation - Activity Selection
    public List<String> allocateGates(List<Flight> flights) {
        List<Flight> sortedFlights = new ArrayList<>(flights);
        sortedFlights.sort(Comparator.comparing(Flight::getDepartureTime));
        
        List<String> allocatedGates = new ArrayList<>();
        Map<String, List<Flight>> gateSchedule = new HashMap<>();
        
        for (Flight flight : sortedFlights) {
            String gate = findAvailableGate(gateSchedule, flight);
            if (gate != null) {
                allocatedGates.add(gate);
                gateSchedule.computeIfAbsent(gate, k -> new ArrayList<>()).add(flight);
            } else {
                allocatedGates.add("No Gate Available");
            }
        }
        
        return allocatedGates;
    }
    
    private String findAvailableGate(Map<String, List<Flight>> gateSchedule, Flight flight) {
        String[] gates = {"G1", "G2", "G3", "G4", "G5"};
        
        for (String gate : gates) {
            List<Flight> scheduled = gateSchedule.getOrDefault(gate, new ArrayList<>());
            if (scheduled.isEmpty() || scheduled.get(scheduled.size() - 1).getArrivalTime()
                .isBefore(flight.getDepartureTime())) {
                return gate;
            }
        }
        return null;
    }
    
    // Fractional Knapsack
    public double optimizeCargoFractional(List<Cargoltem> items, double capacity) {
        items.sort((a, b) -> Double.compare(b.value / b.weight, a.value / a.weight));
        
        double totalValue = 0;
        double remainingCapacity = capacity;
        
        for (Cargoltem item : items) {
            if (remainingCapacity >= item.weight) {
                totalValue += item.value;
                remainingCapacity -= item.weight;
            } else {
                totalValue += item.value * (remainingCapacity / item.weight);
                break;
            }
        }
        
        return totalValue;
    }
    
    // 0/1 Knapsack
    public double optimizeCargo01(List<Cargoltem> items, double capacity) {
        int n = items.size();
        int W = (int) capacity;
        double[][] dp = new double[n + 1][W + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= W; w++) {
                if (items.get(i - 1).weight <= w) {
                    dp[i][w] = Math.max(
                        dp[i - 1][w],
                        dp[i - 1][w - (int) items.get(i - 1).weight] + items.get(i - 1).value
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        
        return dp[n][W];
    }
    
    // LIS - Passenger Traffic Growth
    public int longestIncreasingSubsequence(int[] arr) {
        int n = arr.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[i] > arr[j] && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1;
                }
            }
        }
        
        int max = 0;
        for (int val : dp) {
            max = Math.max(max, val);
        }
        return max;
    }
    
    // Activity Selection for Gate Scheduling
    public List<Flight> activitySelectionGateScheduling(List<Flight> flights) {
        List<Flight> sorted = new ArrayList<>(flights);
        sorted.sort(Comparator.comparing(Flight::getDepartureTime));
        
        List<Flight> selected = new ArrayList<>();
        if (!sorted.isEmpty()) {
            selected.add(sorted.get(0));
            Flight lastSelected = sorted.get(0);
            
            for (int i = 1; i < sorted.size(); i++) {
                Flight current = sorted.get(i);
                if (current.getDepartureTime().isAfter(lastSelected.getArrivalTime())) {
                    selected.add(current);
                    lastSelected = current;
                }
            }
        }
        
        return selected;
    }
}