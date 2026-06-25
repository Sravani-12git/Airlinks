package airlink;

public class Passenger {
    private String passengerId;
    private String flightId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String nationality;
    private String ticketNumber;
    private String seatNumber;
    private String checkinTime;
    private String boardingTime;
    private double luggageWeight;
    private String classType;
    private String passengerType;
    private String frequentFlyer;

    // Constructor
    public Passenger(String passengerId, String flightId, String firstName, String lastName,
                     int age, String gender, String nationality, String ticketNumber,
                     String seatNumber, String checkinTime, String boardingTime,
                     double luggageWeight, String classType, String passengerType,
                     String frequentFlyer) {
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
        this.ticketNumber = ticketNumber;
        this.seatNumber = seatNumber;
        this.checkinTime = checkinTime;
        this.boardingTime = boardingTime;
        this.luggageWeight = luggageWeight;
        this.classType = classType;
        this.passengerType = passengerType;
        this.frequentFlyer = frequentFlyer;
    }

    // Getters and Setters
    public String getPassengerId() { return passengerId; }
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }
    
    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }
    
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    
    public String getCheckinTime() { return checkinTime; }
    public void setCheckinTime(String checkinTime) { this.checkinTime = checkinTime; }
    
    public String getBoardingTime() { return boardingTime; }
    public void setBoardingTime(String boardingTime) { this.boardingTime = boardingTime; }
    
    public double getLuggageWeight() { return luggageWeight; }
    public void setLuggageWeight(double luggageWeight) { this.luggageWeight = luggageWeight; }
    
    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }
    
    public String getPassengerType() { return passengerType; }
    public void setPassengerType(String passengerType) { this.passengerType = passengerType; }
    
    public String getFrequentFlyer() { return frequentFlyer; }
    public void setFrequentFlyer(String frequentFlyer) { this.frequentFlyer = frequentFlyer; }

    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isSeniorCitizen() {
        return age >= 60;
    }

    public boolean isMinor() {
        return age < 18;
    }

    public boolean hasFrequentFlyerStatus() {
        return frequentFlyer != null && !frequentFlyer.equals("None") && !frequentFlyer.equals("null");
    }

    public boolean isBusinessClass() {
        return classType != null && classType.equalsIgnoreCase("Business");
    }

    public boolean isEconomyClass() {
        return classType != null && classType.equalsIgnoreCase("Economy");
    }

    public boolean isTransitPassenger() {
        return passengerType != null && passengerType.equalsIgnoreCase("Transit");
    }

    public String getFrequentFlyerLevel() {
        if (!hasFrequentFlyerStatus()) {
            return "None";
        }
        return frequentFlyer;
    }

    public int getFrequentFlyerPoints() {
        if (frequentFlyer == null) return 0;
        switch (frequentFlyer.toLowerCase()) {
            case "platinum": return 50000;
            case "gold": return 25000;
            case "silver": return 10000;
            default: return 0;
        }
    }

    public double getBoardingPriority() {
        if (frequentFlyer == null) return 0;
        switch (frequentFlyer.toLowerCase()) {
            case "platinum": return 4.0;
            case "gold": return 3.0;
            case "silver": return 2.0;
            default: return 1.0;
        }
    }

    // Convert Passenger to CSV line
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%d,%s,%s,%s,%s,%s,%s,%.1f,%s,%s,%s",
            passengerId, flightId, firstName, lastName, age, gender, nationality,
            ticketNumber, seatNumber, checkinTime, boardingTime, luggageWeight,
            classType, passengerType, frequentFlyer);
    }

    @Override
    public String toString() {
        return String.format("Passenger{id='%s', name='%s %s', flight='%s', seat='%s', class='%s', ticket='%s'}", 
                           passengerId, firstName, lastName, flightId, seatNumber, classType, ticketNumber);
    }

    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════╗\n");
        sb.append("║                    PASSENGER DETAILS                    ║\n");
        sb.append("╚══════════════════════════════════════════════════════════╝\n");
        sb.append("  Passenger ID     : ").append(passengerId).append("\n");
        sb.append("  Name             : ").append(getFullName()).append("\n");
        sb.append("  Age              : ").append(age).append(" years\n");
        sb.append("  Gender           : ").append(gender).append("\n");
        sb.append("  Nationality      : ").append(nationality).append("\n");
        sb.append("  Flight ID        : ").append(flightId).append("\n");
        sb.append("  Ticket Number    : ").append(ticketNumber).append("\n");
        sb.append("  Seat Number      : ").append(seatNumber).append("\n");
        sb.append("  Class            : ").append(classType).append("\n");
        sb.append("  Passenger Type   : ").append(passengerType).append("\n");
        sb.append("  Frequent Flyer   : ").append(frequentFlyer).append("\n");
        sb.append("  Luggage Weight   : ").append(luggageWeight).append(" kg\n");
        sb.append("  Check-in Time    : ").append(checkinTime).append("\n");
        sb.append("  Boarding Time    : ").append(boardingTime).append("\n");
        sb.append("  Senior Citizen   : ").append(isSeniorCitizen() ? "Yes" : "No").append("\n");
        sb.append("  FF Points        : ").append(getFrequentFlyerPoints()).append("\n");
        return sb.toString();
    }

    public static Passenger fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 15) {
            return new Passenger(
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
        }
        return null;
    }

    public boolean isValid() {
        return passengerId != null && !passengerId.isEmpty() &&
               firstName != null && !firstName.isEmpty() &&
               lastName != null && !lastName.isEmpty() &&
               flightId != null && !flightId.isEmpty() &&
               ticketNumber != null && !ticketNumber.isEmpty() &&
               age >= 0 && age <= 120 &&
               luggageWeight >= 0;
    }
}