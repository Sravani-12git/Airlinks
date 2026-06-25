package airlink;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Flight {
    private String flightId;
    private String routeId;
    private String flightNumber;
    private String airline;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String flightDate;
    private String status;
    private String gateAssigned;
    private int passengerCapacity;
    private int checkinCount;
    private int boardingCount;
    private int delayMins;
    private int priorityLevel;

    // Constructor
    public Flight(String flightId, String routeId, String flightNumber, String airline, 
                  String departureTime, String arrivalTime, String flightDate, String status,
                  String gateAssigned, int passengerCapacity, int checkinCount, 
                  int boardingCount, int delayMins, int priorityLevel) {
        this.flightId = flightId;
        this.routeId = routeId;
        this.flightNumber = flightNumber;
        this.airline = airline;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.departureTime = LocalDateTime.parse(flightDate + "T" + departureTime);
        this.arrivalTime = LocalDateTime.parse(flightDate + "T" + arrivalTime);
        this.flightDate = flightDate;
        this.status = status;
        this.gateAssigned = gateAssigned;
        this.passengerCapacity = passengerCapacity;
        this.checkinCount = checkinCount;
        this.boardingCount = boardingCount;
        this.delayMins = delayMins;
        this.priorityLevel = priorityLevel;
    }

    // Getters and Setters
    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }
    
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    
    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }
    
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    
    public String getFlightDate() { return flightDate; }
    public void setFlightDate(String flightDate) { this.flightDate = flightDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getGateAssigned() { return gateAssigned; }
    public void setGateAssigned(String gateAssigned) { this.gateAssigned = gateAssigned; }
    
    public int getPassengerCapacity() { return passengerCapacity; }
    public void setPassengerCapacity(int passengerCapacity) { this.passengerCapacity = passengerCapacity; }
    
    public int getCheckinCount() { return checkinCount; }
    public void setCheckinCount(int checkinCount) { this.checkinCount = checkinCount; }
    
    public int getBoardingCount() { return boardingCount; }
    public void setBoardingCount(int boardingCount) { this.boardingCount = boardingCount; }
    
    public int getDelayMins() { return delayMins; }
    public void setDelayMins(int delayMins) { this.delayMins = delayMins; }
    
    public int getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(int priorityLevel) { this.priorityLevel = priorityLevel; }

    // Utility methods
    public double getOccupancyRate() {
        return (double) boardingCount / passengerCapacity * 100;
    }

    public boolean isOverbooked() {
        return checkinCount > passengerCapacity;
    }

    public boolean isDelayed() {
        return delayMins > 0;
    }

    @Override
    public String toString() {
        return String.format("Flight{id='%s', number='%s', airline='%s', status='%s', gate='%s', delay=%dmin}", 
                           flightId, flightNumber, airline, status, gateAssigned, delayMins);
    }
}