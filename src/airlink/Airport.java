package airlink;

import java.util.ArrayList;
import java.util.List;

public class Airport {
    private String airportId;
    private String airportName;
    private String city;
    private String state;
    private String country;
    private double latitude;
    private double longitude;
    private String timezone;
    private int terminalCount;
    private int gatesCount;
    private List<Route> routes;

    // Constructor
    public Airport(String airportId, String airportName, String city, String state, 
                   String country, double latitude, double longitude, String timezone,
                   int terminalCount, int gatesCount) {
        this.airportId = airportId;
        this.airportName = airportName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.terminalCount = terminalCount;
        this.gatesCount = gatesCount;
        this.routes = new ArrayList<>();
    }

    // Getters and Setters
    public String getAirportId() { return airportId; }
    public void setAirportId(String airportId) { this.airportId = airportId; }
    
    public String getAirportName() { return airportName; }
    public void setAirportName(String airportName) { this.airportName = airportName; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public int getTerminalCount() { return terminalCount; }
    public void setTerminalCount(int terminalCount) { this.terminalCount = terminalCount; }
    
    public int getGatesCount() { return gatesCount; }
    public void setGatesCount(int gatesCount) { this.gatesCount = gatesCount; }
    
    public List<Route> getRoutes() { return routes; }
    public void setRoutes(List<Route> routes) { this.routes = routes; }

    // Utility methods
    public void addRoute(Route route) {
        routes.add(route);
    }

    public double getAverageDistance() {
        if (routes.isEmpty()) return 0;
        double total = 0;
        for (Route route : routes) {
            total += route.getDistanceKm();
        }
        return total / routes.size();
    }

    @Override
    public String toString() {
        return String.format("Airport{id='%s', name='%s', city='%s', country='%s', gates=%d}", 
                           airportId, airportName, city, country, gatesCount);
    }
}