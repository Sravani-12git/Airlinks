package OptimAlgorithms;

import airlink.Flight;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== AirLink Optimization Algorithms Demo ===\n");
        
        OptimAlgorithms optimizer = new OptimAlgorithms();
        
        // Test data
        java.util.List<Flight> flights = getSampleFlights();
        java.util.List<Cargoltem> cargo = getSampleCargo();
        int[] traffic = {1800, 1200, 5000, 6000, 4200, 5300, 5700, 4800, 6200, 5100};
        
        // Test 1: Gate Allocation
        System.out.println("1. Gate Allocation:");
        java.util.List<String> gates = optimizer.allocateGates(flights);
        for (int i = 0; i < flights.size(); i++) {
            System.out.println("  " + flights.get(i).getFlightNumber() + " -> " + gates.get(i));
        }
        
        // Test 2: Activity Selection
        System.out.println("\n2. Activity Selection Scheduling:");
        java.util.List<Flight> selected = optimizer.activitySelectionGateScheduling(flights);
        System.out.println("  Selected " + selected.size() + " flights out of " + flights.size());
        
        // Test 3: Fractional Knapsack
        System.out.println("\n3. Fractional Knapsack (2000kg):");
        double result1 = optimizer.optimizeCargoFractional(new java.util.ArrayList<>(cargo), 2000);
        System.out.println("  Max value: $" + result1);
        
        // Test 4: 0/1 Knapsack
        System.out.println("\n4. 0/1 Knapsack (1500kg):");
        double result2 = optimizer.optimizeCargo01(new java.util.ArrayList<>(cargo), 1500);
        System.out.println("  Max value: $" + result2);
        
        // Test 5: LIS
        System.out.println("\n5. LIS - Traffic Growth:");
        int lis = optimizer.longestIncreasingSubsequence(traffic);
        System.out.println("  LIS length: " + lis);
        
        System.out.println("\n=== All tests completed ===");
    }
    
    private static java.util.List<Flight> getSampleFlights() {
        java.util.List<Flight> flights = new java.util.ArrayList<>();
        flights.add(new Flight("FL001", "R001", "AI-101", "Air India", "06:00", "09:00", 
                              "2026-06-25", "On Time", "G1", 200, 150, 130, 0, 1));
        flights.add(new Flight("FL002", "R001", "6E-205", "IndiGo", "08:30", "11:30", 
                              "2026-06-25", "Delayed", "G2", 180, 140, 110, 30, 2));
        flights.add(new Flight("FL003", "R003", "UK-845", "Vistara", "07:15", "10:30", 
                              "2026-06-25", "On Time", "G3", 220, 200, 180, 0, 1));
        flights.add(new Flight("FL004", "R006", "AI-102", "Air India", "09:00", "10:45", 
                              "2026-06-25", "Boarding", "G4", 160, 150, 140, 0, 1));
        flights.add(new Flight("FL005", "R009", "6E-312", "IndiGo", "11:30", "12:45", 
                              "2026-06-25", "On Time", "G5", 180, 120, 100, 0, 2));
        return flights;
    }
    
    private static java.util.List<Cargoltem> getSampleCargo() {
        java.util.List<Cargoltem> cargo = new java.util.ArrayList<>();
        cargo.add(new Cargoltem("C001", "Electronics", 500, 12, 50000, "High"));
        cargo.add(new Cargoltem("C002", "Pharmaceuticals", 200, 4, 75000, "Critical"));
        cargo.add(new Cargoltem("C003", "Perishable", 300, 8, 30000, "High"));
        cargo.add(new Cargoltem("C004", "Machinery", 1500, 20, 100000, "Medium"));
        cargo.add(new Cargoltem("C005", "Documents", 100, 2, 5000, "Low"));
        cargo.add(new Cargoltem("C006", "Medical Supplies", 400, 6, 85000, "Critical"));
        return cargo;
    }
}