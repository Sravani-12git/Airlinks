package airlink;

public class Route {
    private String routeId;
    private String sourceAirportId;
    private String destAirportId;
    private double distanceKm;
    private double baseCostUsd;
    private int flightDurationMins;
    private double fuelCostUsd;
    private double crewCostUsd;
    private double maintenanceCostUsd;
    private double totalCostUsd;
    private String demandLevel;

    // Constructor
    public Route(String routeId, String sourceAirportId, String destAirportId, 
                 double distanceKm, double baseCostUsd, int flightDurationMins,
                 double fuelCostUsd, double crewCostUsd, double maintenanceCostUsd,
                 double totalCostUsd, String demandLevel) {
        this.routeId = routeId;
        this.sourceAirportId = sourceAirportId;
        this.destAirportId = destAirportId;
        this.distanceKm = distanceKm;
        this.baseCostUsd = baseCostUsd;
        this.flightDurationMins = flightDurationMins;
        this.fuelCostUsd = fuelCostUsd;
        this.crewCostUsd = crewCostUsd;
        this.maintenanceCostUsd = maintenanceCostUsd;
        this.totalCostUsd = totalCostUsd;
        this.demandLevel = demandLevel;
    }

    // Getters and Setters
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public String getSourceAirportId() { return sourceAirportId; }
    public void setSourceAirportId(String sourceAirportId) { this.sourceAirportId = sourceAirportId; }
    
    public String getDestAirportId() { return destAirportId; }
    public void setDestAirportId(String destAirportId) { this.destAirportId = destAirportId; }
    
    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    
    public double getBaseCostUsd() { return baseCostUsd; }
    public void setBaseCostUsd(double baseCostUsd) { this.baseCostUsd = baseCostUsd; }
    
    public int getFlightDurationMins() { return flightDurationMins; }
    public void setFlightDurationMins(int flightDurationMins) { this.flightDurationMins = flightDurationMins; }
    
    public double getFuelCostUsd() { return fuelCostUsd; }
    public void setFuelCostUsd(double fuelCostUsd) { this.fuelCostUsd = fuelCostUsd; }
    
    public double getCrewCostUsd() { return crewCostUsd; }
    public void setCrewCostUsd(double crewCostUsd) { this.crewCostUsd = crewCostUsd; }
    
    public double getMaintenanceCostUsd() { return maintenanceCostUsd; }
    public void setMaintenanceCostUsd(double maintenanceCostUsd) { this.maintenanceCostUsd = maintenanceCostUsd; }
    
    public double getTotalCostUsd() { return totalCostUsd; }
    public void setTotalCostUsd(double totalCostUsd) { this.totalCostUsd = totalCostUsd; }
    
    public String getDemandLevel() { return demandLevel; }
    public void setDemandLevel(String demandLevel) { this.demandLevel = demandLevel; }

    // Utility methods
    public double getCostPerKm() {
        return distanceKm > 0 ? totalCostUsd / distanceKm : 0;
    }

    // isHighDemand method - checks if route has high demand
    public boolean isHighDemand() {
        return demandLevel != null && demandLevel.equalsIgnoreCase("High");
    }

    @Override
    public String toString() {
        return String.format("Route{id='%s', from='%s', to='%s', distance=%.0fkm, cost=%.2f, demand=%s}", 
                           routeId, sourceAirportId, destAirportId, distanceKm, totalCostUsd, demandLevel);
    }
}