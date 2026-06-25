package airlink;

import java.io.IOException;
import java.util.*;
import java.time.format.DateTimeFormatter;
import OptimAlgorithms.OptimAlgorithms;
import OptimAlgorithms.Cargoltem;

public class AirLinksystem {
    private AVLTree<String, Flight> flightIndex;
    private BTree<String, Passenger> passengerIndex;
    private BPlusTree<String, Flight> scheduleIndex;
    private AirGraph airportGraph;
    private List<Airport> airports;
    private List<Route> routes;
    private List<Flight> flights;
    private List<Passenger> passengers;
    private Scanner scanner;

    public AirLinksystem() {
        flightIndex = new AVLTree<>();
        passengerIndex = new BTree<>();
        scheduleIndex = new BPlusTree<>();
        airportGraph = new AirGraph();
        airports = new ArrayList<>();
        routes = new ArrayList<>();
        flights = new ArrayList<>();
        passengers = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void loadData(String dataDir) {
        try {
            airports = CSVReader.readAirports(dataDir + "/airports.csv");
            routes = CSVReader.readRoutes(dataDir + "/routes.csv");
            flights = CSVReader.readFlights(dataDir + "/flights.csv");
            passengers = CSVReader.readPassengers(dataDir + "/passengers.csv");

            buildDataStructures();
            
            System.out.println("✓ Data loaded successfully!");
            System.out.println("  Airports: " + airports.size());
            System.out.println("  Routes: " + routes.size());
            System.out.println("  Flights: " + flights.size());
            System.out.println("  Passengers: " + passengers.size());
            
        } catch (IOException e) {
            System.err.println("✗ Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void buildDataStructures() {
        for (Flight flight : flights) {
            flightIndex.insert(flight.getFlightId(), flight);
        }

        for (Passenger passenger : passengers) {
            passengerIndex.insert(passenger.getPassengerId(), passenger);
        }

        for (Flight flight : flights) {
            scheduleIndex.insert(flight.getDepartureTime().toString(), flight);
        }

        for (Airport airport : airports) {
            airportGraph.addAirport(airport);
        }
        for (Route route : routes) {
            airportGraph.addRoute(route);
        }
    }

    public void showSystemInfo() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║     AirLink System Information           ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("  Total Airports        : " + airports.size());
        System.out.println("  Total Routes          : " + routes.size());
        System.out.println("  Total Flights         : " + flights.size());
        System.out.println("  Total Passengers      : " + passengers.size());
        System.out.println("  Flight Index (AVL)    : " + flightIndex.size() + " nodes");
        System.out.println("  Passenger Index (B-Tree): " + passengerIndex.size() + " nodes");
        System.out.println("  Schedule Index (B+Tree): " + scheduleIndex.size() + " nodes");
        System.out.println("  Airports in Graph     : " + airportGraph.getAirports().size());
        System.out.println("  Routes in Graph       : " + airportGraph.getRoutes().size());
    }

    // ==================== MAIN MENU ====================
    
    public void showMainMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║               AIRLINK SYSTEM - MAIN MENU                 ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("  1. Flight Management");
            System.out.println("  2. Passenger Management");
            System.out.println("  3. Airport & Route Management");
            System.out.println("  4. Graph & Shortest Path Analysis");
            System.out.println("  5. Sorting & Analytics");
            System.out.println("  6. Optimization Algorithms");
            System.out.println("  7. System Information");
            System.out.println("  8. Exit");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.print("  Enter your choice (1-8): ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: flightManagementMenu(); break;
                case 2: passengerManagementMenu(); break;
                case 3: airportManagementMenu(); break;
                case 4: graphAnalysisMenu(); break;
                case 5: sortingAnalyticsMenu(); break;
                case 6: optimizationMenu(); break;
                case 7: showSystemInfo(); break;
                case 8: 
                    System.out.println("\n✓ Thank you for using AirLink System!");
                    return;
                default: System.out.println("✗ Invalid choice. Please try again.");
            }
        }
    }

    // ==================== FLIGHT MANAGEMENT MENU ====================
    
    private void flightManagementMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║              FLIGHT MANAGEMENT                           ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("  1. Search Flight by ID");
            System.out.println("  2. View All Flights");
            System.out.println("  3. View Flights by Time Range");
            System.out.println("  4. View Flights by Airline");
            System.out.println("  5. View Flights by Status");
            System.out.println("  6. Add New Flight");
            System.out.println("  7. Update Flight Details");
            System.out.println("  8. Delete Flight");
            System.out.println("  9. Back to Main Menu");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.print("  Enter your choice (1-9): ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: searchFlightById(); break;
                case 2: viewAllFlights(); break;
                case 3: viewFlightsByTimeRange(); break;
                case 4: viewFlightsByAirline(); break;
                case 5: viewFlightsByStatus(); break;
                case 6: addNewFlight(); break;
                case 7: updateFlightDetails(); break;
                case 8: deleteFlight(); break;
                case 9: return;
                default: System.out.println("✗ Invalid choice.");
            }
        }
    }

    private void searchFlightById() {
        System.out.print("\n  Enter Flight ID (e.g., FL001): ");
        String flightId = scanner.nextLine().trim().toUpperCase();
        Flight flight = flightIndex.search(flightId);
        if (flight != null) {
            System.out.println("\n  ✓ Flight Found:");
            System.out.println("  " + flight);
            System.out.println("  Occupancy: " + String.format("%.1f", flight.getOccupancyRate()) + "%");
            System.out.println("  Delayed: " + (flight.isDelayed() ? "Yes" : "No"));
            System.out.println("  Overbooked: " + (flight.isOverbooked() ? "Yes" : "No"));
        } else {
            System.out.println("  ✗ Flight not found!");
        }
    }

    private void viewAllFlights() {
        System.out.println("\n  All Flights:");
        System.out.println("  " + "-".repeat(80));
        System.out.printf("  %-10s %-12s %-15s %-12s %-10s %-10s%n", 
                         "Flight ID", "Number", "Airline", "Status", "Gate", "Delay");
        System.out.println("  " + "-".repeat(80));
        for (Flight f : flights) {
            System.out.printf("  %-10s %-12s %-15s %-12s %-10s %-10d%n",
                f.getFlightId(), f.getFlightNumber(), f.getAirline(),
                f.getStatus(), f.getGateAssigned(), f.getDelayMins());
        }
        System.out.println("  " + "-".repeat(80));
        System.out.println("  Total: " + flights.size() + " flights");
    }

    private void viewFlightsByTimeRange() {
        System.out.print("\n  Enter start time (HH:mm, e.g., 08:00): ");
        String startTime = scanner.nextLine().trim();
        System.out.print("  Enter end time (HH:mm, e.g., 14:00): ");
        String endTime = scanner.nextLine().trim();
        
        List<Flight> result = getFlightsByTimeRange(startTime, endTime);
        if (result.isEmpty()) {
            System.out.println("  No flights found in this time range.");
        } else {
            System.out.println("\n  Flights from " + startTime + " to " + endTime + ":");
            System.out.println("  " + "-".repeat(60));
            System.out.printf("  %-10s %-12s %-8s %-10s%n", "Number", "Airline", "Departure", "Status");
            System.out.println("  " + "-".repeat(60));
            for (Flight f : result) {
                System.out.printf("  %-10s %-12s %-8s %-10s%n",
                    f.getFlightNumber(), f.getAirline(),
                    f.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    f.getStatus());
            }
            System.out.println("  " + "-".repeat(60));
            System.out.println("  Total: " + result.size() + " flights");
        }
    }

    private void viewFlightsByAirline() {
        System.out.print("\n  Enter Airline name (e.g., Air India, IndiGo, Vistara): ");
        String airline = scanner.nextLine().trim();
        
        List<Flight> result = new ArrayList<>();
        for (Flight f : flights) {
            if (f.getAirline().equalsIgnoreCase(airline)) {
                result.add(f);
            }
        }
        
        if (result.isEmpty()) {
            System.out.println("  No flights found for " + airline);
        } else {
            System.out.println("\n  Flights for " + airline + ":");
            System.out.println("  " + "-".repeat(60));
            System.out.printf("  %-10s %-8s %-10s %-8s%n", "Number", "Departure", "Status", "Gate");
            System.out.println("  " + "-".repeat(60));
            for (Flight f : result) {
                System.out.printf("  %-10s %-8s %-10s %-8s%n",
                    f.getFlightNumber(), f.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    f.getStatus(), f.getGateAssigned());
            }
            System.out.println("  " + "-".repeat(60));
            System.out.println("  Total: " + result.size() + " flights");
        }
    }

    private void viewFlightsByStatus() {
        System.out.println("\n  Available Status: On Time, Delayed, Boarding, Check-in, Scheduled");
        System.out.print("  Enter Status: ");
        String status = scanner.nextLine().trim();
        
        List<Flight> result = new ArrayList<>();
        for (Flight f : flights) {
            if (f.getStatus().equalsIgnoreCase(status)) {
                result.add(f);
            }
        }
        
        if (result.isEmpty()) {
            System.out.println("  No flights with status: " + status);
        } else {
            System.out.println("\n  Flights with status '" + status + "':");
            System.out.println("  " + "-".repeat(60));
            System.out.printf("  %-10s %-12s %-8s %-8s%n", "Number", "Airline", "Departure", "Gate");
            System.out.println("  " + "-".repeat(60));
            for (Flight f : result) {
                System.out.printf("  %-10s %-12s %-8s %-8s%n",
                    f.getFlightNumber(), f.getAirline(),
                    f.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    f.getGateAssigned());
            }
            System.out.println("  " + "-".repeat(60));
            System.out.println("  Total: " + result.size() + " flights");
        }
    }

    private void addNewFlight() {
        System.out.println("\n  === Add New Flight ===");
        System.out.print("  Flight ID (e.g., FL016): ");
        String flightId = scanner.nextLine().trim().toUpperCase();
        
        if (flightIndex.search(flightId) != null) {
            System.out.println("  ✗ Flight already exists!");
            return;
        }
        
        System.out.print("  Route ID (e.g., R001): ");
        String routeId = scanner.nextLine().trim().toUpperCase();
        System.out.print("  Flight Number (e.g., AI-106): ");
        String flightNumber = scanner.nextLine().trim();
        System.out.print("  Airline: ");
        String airline = scanner.nextLine().trim();
        System.out.print("  Departure Time (HH:mm): ");
        String depTime = scanner.nextLine().trim();
        System.out.print("  Arrival Time (HH:mm): ");
        String arrTime = scanner.nextLine().trim();
        System.out.print("  Flight Date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        System.out.print("  Status: ");
        String status = scanner.nextLine().trim();
        System.out.print("  Gate Assigned: ");
        String gate = scanner.nextLine().trim();
        System.out.print("  Passenger Capacity: ");
        int capacity = getIntInput();
        System.out.print("  Check-in Count: ");
        int checkin = getIntInput();
        System.out.print("  Boarding Count: ");
        int boarding = getIntInput();
        System.out.print("  Delay Minutes: ");
        int delay = getIntInput();
        System.out.print("  Priority Level (1-3): ");
        int priority = getIntInput();
        
        Flight newFlight = new Flight(flightId, routeId, flightNumber, airline,
            depTime, arrTime, date, status, gate, capacity, checkin, boarding, delay, priority);
        
        flights.add(newFlight);
        flightIndex.insert(flightId, newFlight);
        scheduleIndex.insert(depTime, newFlight);

        DatabaseManager.insertFlight(newFlight);

        System.out.println("  ✓ Flight added successfully!");
        System.out.println("  ✓ Saved to PostgreSQL!");
    }

    private void updateFlightDetails() {
        System.out.print("\n  Enter Flight ID to update: ");
        String flightId = scanner.nextLine().trim().toUpperCase();
        Flight flight = flightIndex.search(flightId);
        
        if (flight == null) {
            System.out.println("  ✗ Flight not found!");
            return;
        }
        
        System.out.println("  Current Status: " + flight.getStatus());
        System.out.println("  Available Status: On Time, Delayed, Boarding, Check-in, Scheduled, Cancelled");
        System.out.print("  New Status: ");
        String newStatus = scanner.nextLine().trim();
        System.out.print("  New Gate: ");
        String newGate = scanner.nextLine().trim();
        System.out.print("  New Delay Minutes: ");
        int newDelay = getIntInput();
        System.out.print("  New Priority Level: ");
        int newPriority = getIntInput();
        
        flight.setStatus(newStatus);
        flight.setGateAssigned(newGate);
        flight.setDelayMins(newDelay);
        flight.setPriorityLevel(newPriority);

        boolean saved = DatabaseManager.updateFlight(flight);
        if (saved) {
            System.out.println("  ✓ Flight updated successfully!");
            System.out.println("  ✓ Updated in PostgreSQL!");
        } else {
            System.out.println("  ✗ Flight update failed in PostgreSQL.");
        }
    }

    private void deleteFlight() {
        System.out.print("\n  Enter Flight ID to delete: ");
        String flightId = scanner.nextLine().trim().toUpperCase();
        Flight flight = flightIndex.search(flightId);

        if (flight == null) {
            System.out.println("  ✗ Flight not found!");
            return;
        }

        boolean deleted = DatabaseManager.deleteFlight(flightId);
        if (deleted) {
            flights.remove(flight);
            flightIndex.delete(flightId);
            scheduleIndex.delete(flight.getDepartureTime().toString());
            System.out.println("  ✓ Flight deleted successfully!");
            System.out.println("  ✓ Removed from PostgreSQL!");
        } else {
            System.out.println("  ✗ Flight deletion failed in PostgreSQL.");
        }
    }

    // ==================== PASSENGER MANAGEMENT ====================
    
    private void passengerManagementMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║            PASSENGER MANAGEMENT                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("  1. Search Passenger by ID");
            System.out.println("  2. View All Passengers");
            System.out.println("  3. View Passengers by Flight");
            System.out.println("  4. View Passengers by Class");
            System.out.println("  5. View Senior Citizens");
            System.out.println("  6. View Frequent Flyers");
            System.out.println("  7. Add New Passenger");
            System.out.println("  8. Update Passenger");
            System.out.println("  9. Delete Passenger");
            System.out.println("  10. Back to Main Menu");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.print("  Enter your choice (1-10): ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: searchPassengerById(); break;
                case 2: viewAllPassengers(); break;
                case 3: viewPassengersByFlight(); break;
                case 4: viewPassengersByClass(); break;
                case 5: viewSeniorCitizens(); break;
                case 6: viewFrequentFlyers(); break;
                case 7: addNewPassenger(); break;
                case 8: updatePassenger(); break;
                case 9: deletePassenger(); break;
                case 10: return;
                default: System.out.println("✗ Invalid choice.");
            }
        }
    }

    private void searchPassengerById() {
        System.out.print("\n  Enter Passenger ID (e.g., P1001): ");
        String passengerId = scanner.nextLine().trim().toUpperCase();
        Passenger passenger = passengerIndex.search(passengerId);
        if (passenger != null) {
            System.out.println("\n  ✓ Passenger Found:");
            System.out.println(passenger.toDetailedString());
        } else {
            System.out.println("  ✗ Passenger not found!");
        }
    }

    private void viewAllPassengers() {
        System.out.println("\n  All Passengers:");
        System.out.println("  " + "-".repeat(80));
        System.out.printf("  %-10s %-20s %-10s %-12s %-10s %-10s%n", 
                         "ID", "Name", "Age", "Flight", "Class", "FF Status");
        System.out.println("  " + "-".repeat(80));
        for (Passenger p : passengers) {
            System.out.printf("  %-10s %-20s %-10d %-12s %-10s %-10s%n",
                p.getPassengerId(), p.getFullName(), p.getAge(),
                p.getFlightId(), p.getClassType(), 
                p.hasFrequentFlyerStatus() ? p.getFrequentFlyer() : "None");
        }
        System.out.println("  " + "-".repeat(80));
        System.out.println("  Total: " + passengers.size() + " passengers");
    }

    private void viewPassengersByFlight() {
        System.out.print("\n  Enter Flight ID (e.g., FL001): ");
        String flightId = scanner.nextLine().trim().toUpperCase();
        
        List<Passenger> result = new ArrayList<>();
        for (Passenger p : passengers) {
            if (p.getFlightId().equalsIgnoreCase(flightId)) {
                result.add(p);
            }
        }
        
        if (result.isEmpty()) {
            System.out.println("  No passengers found for flight: " + flightId);
        } else {
            System.out.println("\n  Passengers on Flight " + flightId + ":");
            System.out.println("  " + "-".repeat(60));
            System.out.printf("  %-10s %-20s %-8s %-10s%n", "ID", "Name", "Seat", "Class");
            System.out.println("  " + "-".repeat(60));
            for (Passenger p : result) {
                System.out.printf("  %-10s %-20s %-8s %-10s%n",
                    p.getPassengerId(), p.getFullName(), p.getSeatNumber(), p.getClassType());
            }
            System.out.println("  " + "-".repeat(60));
            System.out.println("  Total: " + result.size() + " passengers");
        }
    }

    private void viewPassengersByClass() {
        System.out.println("\n  Available Classes: Economy, Business");
        System.out.print("  Enter Class: ");
        String classType = scanner.nextLine().trim();
        
        List<Passenger> result = new ArrayList<>();
        for (Passenger p : passengers) {
            if (p.getClassType().equalsIgnoreCase(classType)) {
                result.add(p);
            }
        }
        
        if (result.isEmpty()) {
            System.out.println("  No passengers in " + classType + " class");
        } else {
            System.out.println("\n  Passengers in " + classType + " class:");
            System.out.println("  " + "-".repeat(60));
            System.out.printf("  %-10s %-20s %-8s %-10s%n", "ID", "Name", "Flight", "Seat");
            System.out.println("  " + "-".repeat(60));
            for (Passenger p : result) {
                System.out.printf("  %-10s %-20s %-8s %-10s%n",
                    p.getPassengerId(), p.getFullName(), p.getFlightId(), p.getSeatNumber());
            }
            System.out.println("  " + "-".repeat(60));
            System.out.println("  Total: " + result.size() + " passengers");
        }
    }

    private void viewSeniorCitizens() {
        List<Passenger> seniors = new ArrayList<>();
        for (Passenger p : passengers) {
            if (p.isSeniorCitizen()) {
                seniors.add(p);
            }
        }
        
        if (seniors.isEmpty()) {
            System.out.println("  No senior citizens found.");
        } else {
            System.out.println("\n  Senior Citizens (60+ years):");
            System.out.println("  " + "-".repeat(60));
            System.out.printf("  %-10s %-20s %-8s %-12s%n", "ID", "Name", "Age", "Flight");
            System.out.println("  " + "-".repeat(60));
            for (Passenger p : seniors) {
                System.out.printf("  %-10s %-20s %-8d %-12s%n",
                    p.getPassengerId(), p.getFullName(), p.getAge(), p.getFlightId());
            }
            System.out.println("  " + "-".repeat(60));
            System.out.println("  Total: " + seniors.size() + " senior citizens");
        }
    }

    private void viewFrequentFlyers() {
        List<Passenger> ff = new ArrayList<>();
        for (Passenger p : passengers) {
            if (p.hasFrequentFlyerStatus()) {
                ff.add(p);
            }
        }
        
        ff.sort((p1, p2) -> {
            int level1 = p1.getFrequentFlyer().equals("Platinum") ? 3 :
                        p1.getFrequentFlyer().equals("Gold") ? 2 : 1;
            int level2 = p2.getFrequentFlyer().equals("Platinum") ? 3 :
                        p2.getFrequentFlyer().equals("Gold") ? 2 : 1;
            return Integer.compare(level2, level1);
        });
        
        if (ff.isEmpty()) {
            System.out.println("  No frequent flyers found.");
        } else {
            System.out.println("\n  Frequent Flyers:");
            System.out.println("  " + "-".repeat(70));
            System.out.printf("  %-10s %-20s %-12s %-10s %-10s%n", 
                             "ID", "Name", "Level", "Points", "Flight");
            System.out.println("  " + "-".repeat(70));
            for (Passenger p : ff) {
                System.out.printf("  %-10s %-20s %-12s %-10d %-10s%n",
                    p.getPassengerId(), p.getFullName(), p.getFrequentFlyer(),
                    p.getFrequentFlyerPoints(), p.getFlightId());
            }
            System.out.println("  " + "-".repeat(70));
            System.out.println("  Total: " + ff.size() + " frequent flyers");
        }
    }

    private void addNewPassenger() {
        System.out.println("\n  === Add New Passenger ===");
        System.out.print("  Passenger ID (e.g., P1026): ");
        String passengerId = scanner.nextLine().trim().toUpperCase();
        
        if (passengerIndex.search(passengerId) != null) {
            System.out.println("  ✗ Passenger already exists!");
            return;
        }
        
        System.out.print("  Flight ID: ");
        String flightId = scanner.nextLine().trim().toUpperCase();
        System.out.print("  First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("  Last Name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("  Age: ");
        int age = getIntInput();
        System.out.print("  Gender (M/F): ");
        String gender = scanner.nextLine().trim();
        System.out.print("  Nationality: ");
        String nationality = scanner.nextLine().trim();
        System.out.print("  Ticket Number: ");
        String ticketNumber = scanner.nextLine().trim();
        System.out.print("  Seat Number: ");
        String seatNumber = scanner.nextLine().trim();
        System.out.print("  Check-in Time: ");
        String checkinTime = scanner.nextLine().trim();
        System.out.print("  Boarding Time: ");
        String boardingTime = scanner.nextLine().trim();
        System.out.print("  Luggage Weight (kg): ");
        double luggageWeight = getDoubleInput();
        System.out.print("  Class (Economy/Business): ");
        String classType = scanner.nextLine().trim();
        System.out.print("  Passenger Type (Regular/Transit): ");
        String passengerType = scanner.nextLine().trim();
        System.out.print("  Frequent Flyer (Platinum/Gold/Silver/None): ");
        String frequentFlyer = scanner.nextLine().trim();
        
        Passenger newPassenger = new Passenger(passengerId, flightId, firstName, lastName,
            age, gender, nationality, ticketNumber, seatNumber, checkinTime, boardingTime,
            luggageWeight, classType, passengerType, frequentFlyer);
        
        passengers.add(newPassenger);
        passengerIndex.insert(passengerId, newPassenger);

        DatabaseManager.insertPassenger(newPassenger);

        System.out.println("  ✓ Passenger added successfully!");
        System.out.println("  ✓ Saved to PostgreSQL!");
    }

    private void updatePassenger() {
        System.out.print("\n  Enter Passenger ID to update: ");
        String passengerId = scanner.nextLine().trim().toUpperCase();
        Passenger passenger = passengerIndex.search(passengerId);

        if (passenger == null) {
            System.out.println("  ✗ Passenger not found!");
            return;
        }

        System.out.print("  New Flight ID: ");
        String flightId = scanner.nextLine().trim().toUpperCase();
        System.out.print("  New First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("  New Last Name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("  New Age: ");
        int age = getIntInput();
        System.out.print("  New Seat Number: ");
        String seatNumber = scanner.nextLine().trim();
        System.out.print("  New Class: ");
        String classType = scanner.nextLine().trim();
        System.out.print("  New Frequent Flyer: ");
        String frequentFlyer = scanner.nextLine().trim();

        passenger.setFlightId(flightId);
        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passenger.setAge(age);
        passenger.setSeatNumber(seatNumber);
        passenger.setClassType(classType);
        passenger.setFrequentFlyer(frequentFlyer);

        boolean saved = DatabaseManager.updatePassenger(passenger);
        if (saved) {
            System.out.println("  ✓ Passenger updated successfully!");
            System.out.println("  ✓ Updated in PostgreSQL!");
        } else {
            System.out.println("  ✗ Passenger update failed in PostgreSQL.");
        }
    }

    private void deletePassenger() {
        System.out.print("\n  Enter Passenger ID to delete: ");
        String passengerId = scanner.nextLine().trim().toUpperCase();
        Passenger passenger = passengerIndex.search(passengerId);

        if (passenger == null) {
            System.out.println("  ✗ Passenger not found!");
            return;
        }

        boolean deleted = DatabaseManager.deletePassenger(passengerId);
        if (deleted) {
            passengers.remove(passenger);
            passengerIndex.delete(passengerId);
            System.out.println("  ✓ Passenger deleted successfully!");
            System.out.println("  ✓ Removed from PostgreSQL!");
        } else {
            System.out.println("  ✗ Passenger deletion failed in PostgreSQL.");
        }
    }

    // ==================== AIRPORT MANAGEMENT ====================
    
    private void airportManagementMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║          AIRPORT & ROUTE MANAGEMENT                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("  1. View All Airports");
            System.out.println("  2. View Routes from Airport");
            System.out.println("  3. View Airport Details");
            System.out.println("  4. View All Routes");
            System.out.println("  5. View High Demand Routes");
            System.out.println("  6. Add New Route");
            System.out.println("  7. Update Route");
            System.out.println("  8. Delete Route");
            System.out.println("  9. Back to Main Menu");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.print("  Enter your choice (1-9): ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: viewAllAirports(); break;
                case 2: viewRoutesFromAirport(); break;
                case 3: viewAirportDetails(); break;
                case 4: viewAllRoutes(); break;
                case 5: viewHighDemandRoutes(); break;
                case 6: addNewRoute(); break;
                case 7: updateRoute(); break;
                case 8: deleteRoute(); break;
                case 9: return;
                default: System.out.println("✗ Invalid choice.");
            }
        }
    }

    private void viewAllAirports() {
        System.out.println("\n  All Airports:");
        System.out.println("  " + "-".repeat(80));
        System.out.printf("  %-10s %-30s %-15s %-10s %-8s%n", 
                         "ID", "Name", "City", "State", "Gates");
        System.out.println("  " + "-".repeat(80));
        for (Airport a : airports) {
            String name = a.getAirportName();
            if (name.length() > 30) name = name.substring(0, 27) + "...";
            System.out.printf("  %-10s %-30s %-15s %-10s %-8d%n",
                a.getAirportId(), name,
                a.getCity(), a.getState(), a.getGatesCount());
        }
        System.out.println("  " + "-".repeat(80));
        System.out.println("  Total: " + airports.size() + " airports");
    }

    private void viewRoutesFromAirport() {
        System.out.print("\n  Enter Airport ID (e.g., AP001): ");
        String airportId = scanner.nextLine().trim().toUpperCase();
        
        List<Route> airportRoutes = new ArrayList<>();
        for (Route r : routes) {
            if (r.getSourceAirportId().equals(airportId)) {
                airportRoutes.add(r);
            }
        }
        
        if (airportRoutes.isEmpty()) {
            System.out.println("  No routes found from airport: " + airportId);
        } else {
            System.out.println("\n  Routes from " + airportId + ":");
            System.out.println("  " + "-".repeat(70));
            System.out.printf("  %-10s %-10s %-10s %-10s %-8s%n", 
                             "Route ID", "To", "Distance", "Cost", "Demand");
            System.out.println("  " + "-".repeat(70));
            for (Route r : airportRoutes) {
                String destName = getAirportName(r.getDestAirportId());
                System.out.printf("  %-10s %-10s %-10.0f %-10.2f %-8s%n",
                    r.getRouteId(), destName, r.getDistanceKm(), r.getTotalCostUsd(), r.getDemandLevel());
            }
            System.out.println("  " + "-".repeat(70));
            System.out.println("  Total: " + airportRoutes.size() + " routes");
        }
    }

    private void viewAirportDetails() {
        System.out.print("\n  Enter Airport ID (e.g., AP001): ");
        String airportId = scanner.nextLine().trim().toUpperCase();
        
        Airport airport = null;
        for (Airport a : airports) {
            if (a.getAirportId().equals(airportId)) {
                airport = a;
                break;
            }
        }
        
        if (airport == null) {
            System.out.println("  ✗ Airport not found!");
            return;
        }
        
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║                AIRPORT DETAILS                           ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
        System.out.println("    ID           : " + airport.getAirportId());
        System.out.println("    Name         : " + airport.getAirportName());
        System.out.println("    City         : " + airport.getCity());
        System.out.println("    State        : " + airport.getState());
        System.out.println("    Country      : " + airport.getCountry());
        System.out.println("    Latitude     : " + airport.getLatitude());
        System.out.println("    Longitude    : " + airport.getLongitude());
        System.out.println("    Timezone     : " + airport.getTimezone());
        System.out.println("    Terminals    : " + airport.getTerminalCount());
        System.out.println("    Gates        : " + airport.getGatesCount());
    }

    private void viewAllRoutes() {
        System.out.println("\n  All Routes:");
        System.out.println("  " + "-".repeat(90));
        System.out.printf("  %-10s %-10s %-10s %-10s %-12s %-10s%n", 
                         "Route ID", "From", "To", "Distance", "Duration", "Cost");
        System.out.println("  " + "-".repeat(90));
        for (Route r : routes) {
            String fromName = getAirportName(r.getSourceAirportId());
            String toName = getAirportName(r.getDestAirportId());
            System.out.printf("  %-10s %-10s %-10s %-10.0f %-12d %-10.2f%n",
                r.getRouteId(), fromName, toName, r.getDistanceKm(), 
                r.getFlightDurationMins(), r.getTotalCostUsd());
        }
        System.out.println("  " + "-".repeat(90));
        System.out.println("  Total: " + routes.size() + " routes");
    }

    private void viewHighDemandRoutes() {
        System.out.println("\n  High Demand Routes:");
        System.out.println("  " + "-".repeat(70));
        System.out.printf("  %-10s %-10s %-10s %-10s %-10s%n", 
                         "Route ID", "From", "To", "Distance", "Cost");
        System.out.println("  " + "-".repeat(70));
        int count = 0;
        for (Route r : routes) {
            if (r.isHighDemand()) {
                String fromName = getAirportName(r.getSourceAirportId());
                String toName = getAirportName(r.getDestAirportId());
                System.out.printf("  %-10s %-10s %-10s %-10.0f %-10.2f%n",
                    r.getRouteId(), fromName, toName, r.getDistanceKm(), r.getTotalCostUsd());
                count++;
            }
        }
        System.out.println("  " + "-".repeat(70));
        System.out.println("  Total: " + count + " high demand routes");
    }

    private void addNewRoute() {
        System.out.println("\n  === Add New Route ===");
        System.out.print("  Route ID: ");
        String routeId = scanner.nextLine().trim().toUpperCase();
        System.out.print("  Source Airport ID: ");
        String source = scanner.nextLine().trim().toUpperCase();
        System.out.print("  Destination Airport ID: ");
        String destination = scanner.nextLine().trim().toUpperCase();
        System.out.print("  Distance (km): ");
        double distance = getDoubleInput();
        System.out.print("  Base Cost (USD): ");
        double baseCost = getDoubleInput();
        System.out.print("  Flight Duration (mins): ");
        int duration = getIntInput();
        System.out.print("  Fuel Cost (USD): ");
        double fuelCost = getDoubleInput();
        System.out.print("  Crew Cost (USD): ");
        double crewCost = getDoubleInput();
        System.out.print("  Maintenance Cost (USD): ");
        double maintenanceCost = getDoubleInput();
        System.out.print("  Total Cost (USD): ");
        double totalCost = getDoubleInput();
        System.out.print("  Demand Level (High/Medium/Low): ");
        String demand = scanner.nextLine().trim();

        Route newRoute = new Route(routeId, source, destination, distance, baseCost, duration,
                fuelCost, crewCost, maintenanceCost, totalCost, demand);
        routes.add(newRoute);
        DatabaseManager.insertRoute(newRoute);
        System.out.println("  ✓ Route added successfully!");
        System.out.println("  ✓ Saved to PostgreSQL!");
    }

    private void updateRoute() {
        System.out.print("\n  Enter Route ID to update: ");
        String routeId = scanner.nextLine().trim().toUpperCase();
        Route route = null;
        for (Route r : routes) {
            if (r.getRouteId().equals(routeId)) {
                route = r;
                break;
            }
        }

        if (route == null) {
            System.out.println("  ✗ Route not found!");
            return;
        }

        System.out.print("  New Source Airport ID: ");
        String source = scanner.nextLine().trim().toUpperCase();
        System.out.print("  New Destination Airport ID: ");
        String destination = scanner.nextLine().trim().toUpperCase();
        System.out.print("  New Distance (km): ");
        double distance = getDoubleInput();
        System.out.print("  New Total Cost (USD): ");
        double totalCost = getDoubleInput();
        System.out.print("  New Demand Level: ");
        String demand = scanner.nextLine().trim();

        route.setSourceAirportId(source);
        route.setDestAirportId(destination);
        route.setDistanceKm(distance);
        route.setTotalCostUsd(totalCost);
        route.setDemandLevel(demand);

        boolean saved = DatabaseManager.updateRoute(route);
        if (saved) {
            System.out.println("  ✓ Route updated successfully!");
            System.out.println("  ✓ Updated in PostgreSQL!");
        } else {
            System.out.println("  ✗ Route update failed in PostgreSQL.");
        }
    }

    private void deleteRoute() {
        System.out.print("\n  Enter Route ID to delete: ");
        String routeId = scanner.nextLine().trim().toUpperCase();
        Route route = null;
        for (Route r : routes) {
            if (r.getRouteId().equals(routeId)) {
                route = r;
                break;
            }
        }

        if (route == null) {
            System.out.println("  ✗ Route not found!");
            return;
        }

        boolean deleted = DatabaseManager.deleteRoute(routeId);
        if (deleted) {
            routes.remove(route);
            System.out.println("  ✓ Route deleted successfully!");
            System.out.println("  ✓ Removed from PostgreSQL!");
        } else {
            System.out.println("  ✗ Route deletion failed in PostgreSQL.");
        }
    }

    // ==================== GRAPH & SHORTEST PATH ====================
    
    private void graphAnalysisMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║        GRAPH & SHORTEST PATH ANALYSIS                    ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("  1. Find Shortest Path (Dijkstra)");
            System.out.println("  2. Find Shortest Path (Bellman-Ford)");
            System.out.println("  3. All-Pairs Shortest Path (Floyd-Warshall)");
            System.out.println("  4. BFS - Reachable Airports");
            System.out.println("  5. Check for Cycles");
            System.out.println("  6. Minimum Spanning Tree (Prim's)");
            System.out.println("  7. Topological Sort");
            System.out.println("  8. Back to Main Menu");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.print("  Enter your choice (1-8): ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: shortestPathDijkstra(); break;
                case 2: shortestPathBellmanFord(); break;
                case 3: floydWarshall(); break;
                case 4: bfsReachable(); break;
                case 5: checkCycles(); break;
                case 6: primMST(); break;
                case 7: topologicalSort(); break;
                case 8: return;
                default: System.out.println("✗ Invalid choice.");
            }
        }
    }

    private void shortestPathDijkstra() {
        System.out.print("\n  Enter Source Airport ID (e.g., AP001): ");
        String source = scanner.nextLine().trim().toUpperCase();
        System.out.print("  Enter Destination Airport ID (e.g., AP004): ");
        String dest = scanner.nextLine().trim().toUpperCase();
        
        List<String> path = airportGraph.dijkstra(source, dest);
        if (path.isEmpty()) {
            System.out.println("  ✗ No path found between " + source + " and " + dest);
        } else {
            System.out.println("\n  Shortest Path (Dijkstra):");
            System.out.print("  ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i));
                if (i < path.size() - 1) {
                    System.out.print(" → ");
                }
            }
            System.out.println();
            System.out.println("  Total Distance: " + calculatePathDistance(path) + " km");
        }
    }

    private void shortestPathBellmanFord() {
        System.out.print("\n  Enter Source Airport ID (e.g., AP001): ");
        String source = scanner.nextLine().trim().toUpperCase();
        
        Map<String, Double> distances = airportGraph.bellmanFord(source);
        System.out.println("\n  Distances from " + source + " (Bellman-Ford):");
        System.out.println("  " + "-".repeat(40));
        System.out.printf("  %-10s %-20s %-10s%n", "Airport", "City", "Distance");
        System.out.println("  " + "-".repeat(40));
        for (Map.Entry<String, Double> entry : distances.entrySet()) {
            if (entry.getValue() != Double.MAX_VALUE) {
                String name = getAirportName(entry.getKey());
                System.out.printf("  %-10s %-20s %-10.0f km%n", 
                    entry.getKey(), name, entry.getValue());
            }
        }
        System.out.println("  " + "-".repeat(40));
    }

    private void floydWarshall() {
        System.out.println("\n  Computing all-pairs shortest paths (Floyd-Warshall)...");
        double[][] dist = airportGraph.floydWarshall();
        List<String> ids = new ArrayList<>(airportGraph.getAirports().keySet());
        
        System.out.println("\n  Distance Matrix (km):");
        System.out.print("  " + " ".repeat(8));
        for (int i = 0; i < Math.min(5, ids.size()); i++) {
            System.out.printf("%-10s", ids.get(i));
        }
        System.out.println();
        System.out.println("  " + "-".repeat(8 + 10 * Math.min(5, ids.size())));
        
        for (int i = 0; i < Math.min(5, ids.size()); i++) {
            System.out.printf("  %-8s", ids.get(i));
            for (int j = 0; j < Math.min(5, ids.size()); j++) {
                if (dist[i][j] == Double.MAX_VALUE) {
                    System.out.printf("%-10s", "∞");
                } else {
                    System.out.printf("%-10.0f", dist[i][j]);
                }
            }
            System.out.println();
        }
    }

    private void bfsReachable() {
        System.out.print("\n  Enter Airport ID (e.g., AP001): ");
        String start = scanner.nextLine().trim().toUpperCase();
        System.out.print("  Enter Max Distance (km): ");
        double maxDist = getDoubleInput();
        
        List<String> reachable = airportGraph.bfs(start, maxDist);
        System.out.println("\n  Airports reachable from " + start + " within " + maxDist + " km:");
        System.out.println("  " + "-".repeat(40));
        System.out.printf("  %-10s %-20s%n", "Airport", "City");
        System.out.println("  " + "-".repeat(40));
        for (String id : reachable) {
            String name = getAirportName(id);
            System.out.printf("  %-10s %-20s%n", id, name);
        }
        System.out.println("  " + "-".repeat(40));
        System.out.println("  Total: " + reachable.size() + " airports");
    }

    private void checkCycles() {
        boolean hasCycle = airportGraph.detectCycle();
        if (hasCycle) {
            System.out.println("\n  ⚠ Cycle detected in the flight network!");
        } else {
            System.out.println("\n  ✓ No cycles detected in the flight network.");
        }
    }

    private void primMST() {
        System.out.print("\n  Enter Airport ID to start MST (e.g., AP001): ");
        String start = scanner.nextLine().trim().toUpperCase();
        
        List<AirGraph.Edge> mst = airportGraph.primMST(start);
        if (mst.isEmpty()) {
            System.out.println("  No MST found starting from " + start);
        } else {
            System.out.println("\n  Minimum Spanning Tree (Prim's):");
            System.out.println("  " + "-".repeat(50));
            System.out.printf("  %-10s %-10s %-10s%n", "From", "To", "Distance");
            System.out.println("  " + "-".repeat(50));
            double totalWeight = 0;
            for (AirGraph.Edge edge : mst) {
                String fromName = getAirportName(start);
                String toName = getAirportName(edge.destination);
                System.out.printf("  %-10s → %-10s (%.0f km)%n", 
                    fromName, toName, edge.weight);
                totalWeight += edge.weight;
            }
            System.out.println("  " + "-".repeat(50));
            System.out.println("  Total Weight: " + totalWeight + " km");
        }
    }

    private void topologicalSort() {
        System.out.println("\n  Topological Sort of Airports:");
        List<String> sorted = airportGraph.topologicalSort();
        System.out.println("  " + "-".repeat(40));
        System.out.printf("  %-3s %-10s %-20s%n", "#", "Airport", "City");
        System.out.println("  " + "-".repeat(40));
        for (int i = 0; i < sorted.size(); i++) {
            String name = getAirportName(sorted.get(i));
            System.out.printf("  %-3d %-10s %-20s%n", i+1, sorted.get(i), name);
        }
        System.out.println("  " + "-".repeat(40));
        System.out.println("  Total: " + sorted.size() + " airports");
    }

    // ==================== SORTING & ANALYTICS ====================
    
    private void sortingAnalyticsMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║           SORTING & ANALYTICS                            ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("  1. Sort Flights by Departure (Merge Sort)");
            System.out.println("  2. Sort Flights by Delay (Quick Sort)");
            System.out.println("  3. Sort Routes by Cost (Heap Sort)");
            System.out.println("  4. Flight Statistics");
            System.out.println("  5. Passenger Statistics");
            System.out.println("  6. Route Statistics");
            System.out.println("  7. Back to Main Menu");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.print("  Enter your choice (1-7): ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: sortFlightsByDeparture(); break;
                case 2: sortFlightsByDelay(); break;
                case 3: sortRoutesByCost(); break;
                case 4: printFlightStatistics(); break;
                case 5: printPassengerStatistics(); break;
                case 6: printRouteStatistics(); break;
                case 7: return;
                default: System.out.println("✗ Invalid choice.");
            }
        }
    }

    private void sortFlightsByDeparture() {
        List<Flight> sorted = new ArrayList<>(flights);
        
        if (sorted.isEmpty()) {
            System.out.println("  No flights to sort.");
            return;
        }
        
        SortAlgorithms.mergeSort(sorted, 0, sorted.size() - 1);
        
        System.out.println("\n  Flights Sorted by Departure Time (Merge Sort):");
        System.out.println("  " + "-".repeat(70));
        System.out.printf("  %-10s %-12s %-15s %-10s %-10s%n", 
                         "Number", "Airline", "Departure", "Gate", "Status");
        System.out.println("  " + "-".repeat(70));
        for (Flight f : sorted) {
            System.out.printf("  %-10s %-12s %-15s %-10s %-10s%n",
                f.getFlightNumber(), f.getAirline(),
                f.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                f.getGateAssigned(), f.getStatus());
        }
        System.out.println("  " + "-".repeat(70));
        System.out.println("  Total: " + sorted.size() + " flights");
    }

    private void sortFlightsByDelay() {
        List<Flight> sorted = new ArrayList<>(flights);
        
        if (sorted.isEmpty()) {
            System.out.println("  No flights to sort.");
            return;
        }
        
        SortAlgorithms.quickSortByDelay(sorted, 0, sorted.size() - 1);
        
        System.out.println("\n  Flights Sorted by Delay (Quick Sort):");
        System.out.println("  " + "-".repeat(70));
        System.out.printf("  %-10s %-12s %-15s %-10s %-10s%n", 
                         "Number", "Airline", "Delay (min)", "Gate", "Status");
        System.out.println("  " + "-".repeat(70));
        for (Flight f : sorted) {
            System.out.printf("  %-10s %-12s %-15d %-10s %-10s%n",
                f.getFlightNumber(), f.getAirline(), f.getDelayMins(),
                f.getGateAssigned(), f.getStatus());
        }
        System.out.println("  " + "-".repeat(70));
        System.out.println("  Total: " + sorted.size() + " flights");
    }

    private void sortRoutesByCost() {
        List<Route> sorted = new ArrayList<>(routes);
        
        if (sorted.isEmpty()) {
            System.out.println("  No routes to sort.");
            return;
        }
        
        SortAlgorithms.heapSortRoutes(sorted);
        
        System.out.println("\n  Routes Sorted by Cost (Heap Sort):");
        System.out.println("  " + "-".repeat(70));
        System.out.printf("  %-10s %-10s %-10s %-10s %-10s%n", 
                         "Route ID", "From", "To", "Distance", "Cost");
        System.out.println("  " + "-".repeat(70));
        for (Route r : sorted) {
            String fromName = getAirportName(r.getSourceAirportId());
            String toName = getAirportName(r.getDestAirportId());
            System.out.printf("  %-10s %-10s %-10s %-10.0f %-10.2f%n",
                r.getRouteId(), fromName, toName, r.getDistanceKm(), r.getTotalCostUsd());
        }
        System.out.println("  " + "-".repeat(70));
        System.out.println("  Total: " + sorted.size() + " routes");
    }

    private void printFlightStatistics() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║               FLIGHT STATISTICS                          ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
        
        if (flights.isEmpty()) {
            System.out.println("  No flight data available.");
            return;
        }
        
        int totalDelayed = 0;
        double totalOccupancy = 0;
        Map<String, Integer> airlineCounts = new HashMap<>();
        Map<String, Integer> statusCounts = new HashMap<>();
        
        for (Flight flight : flights) {
            if (flight.isDelayed()) totalDelayed++;
            totalOccupancy += flight.getOccupancyRate();
            
            airlineCounts.put(flight.getAirline(), 
                airlineCounts.getOrDefault(flight.getAirline(), 0) + 1);
            statusCounts.put(flight.getStatus(), 
                statusCounts.getOrDefault(flight.getStatus(), 0) + 1);
        }
        
        System.out.println("\n  General Statistics:");
        System.out.println("    Total Flights       : " + flights.size());
        System.out.println("    Delayed Flights     : " + totalDelayed + 
                         " (" + String.format("%.1f", (double)totalDelayed/flights.size()*100) + "%)");
        System.out.println("    Average Occupancy   : " + String.format("%.1f", totalOccupancy/flights.size()) + "%");
        
        System.out.println("\n  Flights by Airline:");
        for (Map.Entry<String, Integer> entry : airlineCounts.entrySet()) {
            System.out.printf("    %-15s : %d%n", entry.getKey(), entry.getValue());
        }
        
        System.out.println("\n  Flights by Status:");
        for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
            System.out.printf("    %-15s : %d%n", entry.getKey(), entry.getValue());
        }
    }

    private void printPassengerStatistics() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║             PASSENGER STATISTICS                         ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
        
        if (passengers.isEmpty()) {
            System.out.println("  No passenger data available.");
            return;
        }
        
        int seniors = 0;
        int minors = 0;
        int ff = 0;
        Map<String, Integer> classCounts = new HashMap<>();
        Map<String, Integer> ffLevels = new HashMap<>();
        double totalLuggage = 0;
        
        for (Passenger p : passengers) {
            if (p.isSeniorCitizen()) seniors++;
            if (p.isMinor()) minors++;
            if (p.hasFrequentFlyerStatus()) {
                ff++;
                ffLevels.put(p.getFrequentFlyer(), 
                    ffLevels.getOrDefault(p.getFrequentFlyer(), 0) + 1);
            }
            classCounts.put(p.getClassType(), 
                classCounts.getOrDefault(p.getClassType(), 0) + 1);
            totalLuggage += p.getLuggageWeight();
        }
        
        System.out.println("\n  General Statistics:");
        System.out.println("    Total Passengers    : " + passengers.size());
        System.out.println("    Senior Citizens     : " + seniors);
        System.out.println("    Minors              : " + minors);
        System.out.println("    Frequent Flyers    : " + ff);
        System.out.println("    Avg Luggage Weight : " + String.format("%.1f", totalLuggage/passengers.size()) + " kg");
        
        System.out.println("\n  Passengers by Class:");
        for (Map.Entry<String, Integer> entry : classCounts.entrySet()) {
            System.out.printf("    %-15s : %d%n", entry.getKey(), entry.getValue());
        }
        
        System.out.println("\n  Frequent Flyer Levels:");
        for (Map.Entry<String, Integer> entry : ffLevels.entrySet()) {
            System.out.printf("    %-15s : %d%n", entry.getKey(), entry.getValue());
        }
    }

    private void printRouteStatistics() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║               ROUTE STATISTICS                           ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
        
        if (routes.isEmpty()) {
            System.out.println("  No route data available.");
            return;
        }
        
        double totalDistance = 0;
        double totalCost = 0;
        int highDemand = 0;
        
        for (Route r : routes) {
            totalDistance += r.getDistanceKm();
            totalCost += r.getTotalCostUsd();
            if (r.isHighDemand()) highDemand++;
        }
        
        System.out.println("\n  General Statistics:");
        System.out.println("    Total Routes       : " + routes.size());
        System.out.println("    Total Distance     : " + String.format("%.0f", totalDistance) + " km");
        System.out.println("    Avg Distance       : " + String.format("%.0f", totalDistance/routes.size()) + " km");
        System.out.println("    Total Cost         : $" + String.format("%.2f", totalCost));
        System.out.println("    Avg Cost           : $" + String.format("%.2f", totalCost/routes.size()));
        System.out.println("    High Demand Routes : " + highDemand);
        
        Route shortest = routes.stream().min(Comparator.comparing(Route::getDistanceKm)).orElse(null);
        Route longest = routes.stream().max(Comparator.comparing(Route::getDistanceKm)).orElse(null);
        
        if (shortest != null && longest != null) {
            System.out.println("\n  Extreme Routes:");
            System.out.printf("    Shortest : %s → %s (%.0f km)%n",
                getAirportName(shortest.getSourceAirportId()),
                getAirportName(shortest.getDestAirportId()),
                shortest.getDistanceKm());
            System.out.printf("    Longest  : %s → %s (%.0f km)%n",
                getAirportName(longest.getSourceAirportId()),
                getAirportName(longest.getDestAirportId()),
                longest.getDistanceKm());
        }
    }

    // ==================== OPTIMIZATION MENU ====================
    
    private void optimizationMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║           OPTIMIZATION ALGORITHMS                        ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("  1. Gate Allocation (Activity Selection)");
            System.out.println("  2. Cargo Optimization (Fractional Knapsack)");
            System.out.println("  3. Cargo Selection (0/1 Knapsack)");
            System.out.println("  4. Passenger Traffic Growth (LIS)");
            System.out.println("  5. Back to Main Menu");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.print("  Enter your choice (1-5): ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: gateAllocation(); break;
                case 2: cargoFractional(); break;
                case 3: cargo01(); break;
                case 4: passengerTrafficGrowth(); break;
                case 5: return;
                default: System.out.println("✗ Invalid choice.");
            }
        }
    }

    private void gateAllocation() {
        System.out.println("\n  === Gate Allocation (Activity Selection) ===");
        List<Flight> sortedFlights = new ArrayList<>(flights);
        sortedFlights.sort(Comparator.comparing(Flight::getDepartureTime));
        
        List<Flight> selected = new ArrayList<>();
        if (!sortedFlights.isEmpty()) {
            selected.add(sortedFlights.get(0));
            Flight lastSelected = sortedFlights.get(0);
            
            for (int i = 1; i < sortedFlights.size(); i++) {
                Flight current = sortedFlights.get(i);
                if (current.getDepartureTime().isAfter(lastSelected.getArrivalTime())) {
                    selected.add(current);
                    lastSelected = current;
                }
            }
        }
        
        System.out.println("\n  Optimal Gate Allocation (" + selected.size() + " flights):");
        System.out.println("  " + "-".repeat(60));
        System.out.printf("  %-10s %-12s %-15s %-8s%n", "Flight", "Airline", "Departure", "Gate");
        System.out.println("  " + "-".repeat(60));
        for (Flight f : selected) {
            System.out.printf("  %-10s %-12s %-15s %-8s%n",
                f.getFlightNumber(), f.getAirline(),
                f.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                f.getGateAssigned());
        }
        System.out.println("  " + "-".repeat(60));
        System.out.println("  Total gates required: " + selected.size());
    }

    private void cargoFractional() {
        System.out.println("\n  === Cargo Optimization (Fractional Knapsack) ===");
        List<Cargoltem> cargo = getSampleCargo();
        
        System.out.println("\n  Available Cargo Items:");
        System.out.println("  " + "-".repeat(60));
        System.out.printf("  %-10s %-15s %-10s %-10s%n", "ID", "Type", "Weight", "Value");
        System.out.println("  " + "-".repeat(60));
        for (Cargoltem c : cargo) {
            System.out.printf("  %-10s %-15s %-10.1f %-10.0f%n",
                c.id, c.type, c.weight, c.value);
        }
        System.out.println("  " + "-".repeat(60));
        
        System.out.print("\n  Enter Cargo Capacity (kg): ");
        double capacity = getDoubleInput();
        
        OptimAlgorithms optimizer = new OptimAlgorithms();
        double maxValue = optimizer.optimizeCargoFractional(cargo, capacity);
        System.out.println("\n  Maximum Value: $" + String.format("%.2f", maxValue));
    }

    private void cargo01() {
        System.out.println("\n  === Cargo Selection (0/1 Knapsack) ===");
        List<Cargoltem> cargo = getSampleCargo();
        
        System.out.println("\n  Available Cargo Items:");
        System.out.println("  " + "-".repeat(60));
        System.out.printf("  %-10s %-15s %-10s %-10s%n", "ID", "Type", "Weight", "Value");
        System.out.println("  " + "-".repeat(60));
        for (Cargoltem c : cargo) {
            System.out.printf("  %-10s %-15s %-10.1f %-10.0f%n",
                c.id, c.type, c.weight, c.value);
        }
        System.out.println("  " + "-".repeat(60));
        
        System.out.print("\n  Enter Cargo Capacity (kg): ");
        double capacity = getDoubleInput();
        
        OptimAlgorithms optimizer = new OptimAlgorithms();
        double maxValue = optimizer.optimizeCargo01(cargo, capacity);
        System.out.println("\n  Maximum Value: $" + String.format("%.2f", maxValue));
    }

    private void passengerTrafficGrowth() {
        System.out.println("\n  === Passenger Traffic Growth (LIS) ===");
        int[] traffic = {1800, 1200, 5000, 6000, 4200, 5300, 5700, 4800, 6200, 5100};
        
        System.out.println("\n  Hourly Traffic Data:");
        System.out.print("  ");
        for (int i = 0; i < traffic.length; i++) {
            System.out.printf("%6d", traffic[i]);
            if (i < traffic.length - 1) System.out.print(", ");
        }
        System.out.println();
        
        OptimAlgorithms optimizer = new OptimAlgorithms();
        int lisLength = optimizer.longestIncreasingSubsequence(traffic);
        
        System.out.println("\n  Longest Increasing Subsequence Length: " + lisLength);
        System.out.println("  This indicates a growing trend in passenger traffic.");
    }

    // ==================== HELPER METHODS ====================
    
    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  Please enter a valid number: ");
            }
        }
    }

    private double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  Please enter a valid number: ");
            }
        }
    }

    private String getAirportName(String airportId) {
        for (Airport a : airports) {
            if (a.getAirportId().equals(airportId)) {
                return a.getCity();
            }
        }
        return airportId;
    }

    private double calculatePathDistance(List<String> path) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            for (Route r : routes) {
                if (r.getSourceAirportId().equals(from) && r.getDestAirportId().equals(to)) {
                    total += r.getDistanceKm();
                    break;
                }
            }
        }
        return total;
    }

    private List<Cargoltem> getSampleCargo() {
        List<Cargoltem> cargo = new ArrayList<>();
        cargo.add(new Cargoltem("C001", "Electronics", 500, 12, 50000, "High"));
        cargo.add(new Cargoltem("C002", "Pharmaceuticals", 200, 4, 75000, "Critical"));
        cargo.add(new Cargoltem("C003", "Perishable", 300, 8, 30000, "High"));
        cargo.add(new Cargoltem("C004", "Machinery", 1500, 20, 100000, "Medium"));
        cargo.add(new Cargoltem("C005", "Documents", 100, 2, 5000, "Low"));
        cargo.add(new Cargoltem("C006", "Medical Supplies", 400, 6, 85000, "Critical"));
        return cargo;
    }

    public List<Flight> getFlightsByTimeRange(String startTime, String endTime) {
        List<String> flightTimes = scheduleIndex.rangeQuery(startTime, endTime);
        List<Flight> result = new ArrayList<>();
        for (String time : flightTimes) {
            Flight flight = scheduleIndex.search(time);
            if (flight != null) {
                result.add(flight);
            }
        }
        return result;
    }

    // ==================== MAIN METHOD ====================
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║            AIRLINK SYSTEM v2.0                           ║");
        System.out.println("║     Smart Airport Operations & Flight Management         ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        AirLinksystem system = new AirLinksystem();
        system.loadData("src/data");
        system.showSystemInfo();
        system.showMainMenu();
    }
}
















