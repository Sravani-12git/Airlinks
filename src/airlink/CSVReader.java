package airlink;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CSVReader {
    
    public static List<Airport> readAirports(String filePath) throws IOException {
        List<Airport> airports = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            System.err.println("Working directory: " + System.getProperty("user.dir"));
            return airports;
        }
        
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            
            if (parts.length >= 10) {
                try {
                    Airport airport = new Airport(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        Double.parseDouble(parts[5].trim()),
                        Double.parseDouble(parts[6].trim()),
                        parts[7].trim(),
                        Integer.parseInt(parts[8].trim()),
                        Integer.parseInt(parts[9].trim())
                    );
                    airports.add(airport);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing airport line: " + line);
                }
            }
        }
        return airports;
    }

    public static List<Route> readRoutes(String filePath) throws IOException {
        List<Route> routes = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return routes;
        }
        
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            
            if (parts.length >= 11) {
                try {
                    Route route = new Route(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        Double.parseDouble(parts[3].trim()),
                        Double.parseDouble(parts[4].trim()),
                        Integer.parseInt(parts[5].trim()),
                        Double.parseDouble(parts[6].trim()),
                        Double.parseDouble(parts[7].trim()),
                        Double.parseDouble(parts[8].trim()),
                        Double.parseDouble(parts[9].trim()),
                        parts[10].trim()
                    );
                    routes.add(route);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing route line: " + line);
                }
            }
        }
        return routes;
    }

    public static List<Flight> readFlights(String filePath) throws IOException {
        List<Flight> flights = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return flights;
        }
        
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            
            if (parts.length >= 14) {
                try {
                    Flight flight = new Flight(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim(),
                        parts[6].trim(),
                        parts[7].trim(),
                        parts[8].trim(),
                        Integer.parseInt(parts[9].trim()),
                        Integer.parseInt(parts[10].trim()),
                        Integer.parseInt(parts[11].trim()),
                        Integer.parseInt(parts[12].trim()),
                        Integer.parseInt(parts[13].trim())
                    );
                    flights.add(flight);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing flight line: " + line);
                }
            }
        }
        return flights;
    }

    public static List<Passenger> readPassengers(String filePath) throws IOException {
        List<Passenger> passengers = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return passengers;
        }
        
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            
            if (parts.length >= 15) {
                try {
                    Passenger passenger = new Passenger(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        Integer.parseInt(parts[4].trim()),
                        parts[5].trim(),
                        parts[6].trim(),
                        parts[7].trim(),
                        parts[8].trim(),
                        parts[9].trim(),
                        parts[10].trim(),
                        Double.parseDouble(parts[11].trim()),
                        parts[12].trim(),
                        parts[13].trim(),
                        parts[14].trim()
                    );
                    passengers.add(passenger);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing passenger line: " + line);
                }
            }
        }
        return passengers;
    }
}