package airlink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class DatabaseManager {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static void initializeSchema() {
        String createFlights = """
            CREATE TABLE IF NOT EXISTS flights (
                flight_id VARCHAR(20) PRIMARY KEY,
                route_id VARCHAR(20),
                flight_number VARCHAR(30),
                airline VARCHAR(50),
                departure_time VARCHAR(10),
                arrival_time VARCHAR(10),
                flight_date VARCHAR(20),
                status VARCHAR(30),
                gate_assigned VARCHAR(10),
                passenger_capacity INT,
                checkin_count INT,
                boarding_count INT,
                delay_mins INT,
                priority_level INT
            )
            """;

        String createPassengers = """
            CREATE TABLE IF NOT EXISTS passengers (
                passenger_id VARCHAR(20) PRIMARY KEY,
                flight_id VARCHAR(20),
                first_name VARCHAR(50),
                last_name VARCHAR(50),
                age INT,
                gender VARCHAR(10),
                nationality VARCHAR(50),
                ticket_number VARCHAR(30),
                seat_number VARCHAR(10),
                checkin_time VARCHAR(10),
                boarding_time VARCHAR(10),
                luggage_weight DOUBLE PRECISION,
                class_type VARCHAR(20),
                passenger_type VARCHAR(20),
                frequent_flyer VARCHAR(20)
            )
            """;

        String createRoutes = """
            CREATE TABLE IF NOT EXISTS routes (
                route_id VARCHAR(20) PRIMARY KEY,
                source_airport_id VARCHAR(20),
                dest_airport_id VARCHAR(20),
                distance_km DOUBLE PRECISION,
                base_cost_usd DOUBLE PRECISION,
                flight_duration_mins INT,
                fuel_cost_usd DOUBLE PRECISION,
                crew_cost_usd DOUBLE PRECISION,
                maintenance_cost_usd DOUBLE PRECISION,
                total_cost_usd DOUBLE PRECISION,
                demand_level VARCHAR(20)
            )
            """;

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return;
            }
            try (Statement st = con.createStatement()) {
                st.executeUpdate(createFlights);
                st.executeUpdate(createPassengers);
                st.executeUpdate(createRoutes);
            }
        } catch (SQLException e) {
            System.out.println("  ⚠ PostgreSQL schema sync skipped: " + e.getMessage());
        }
    }

    public static void insertPassenger(Passenger p) {
        initializeSchema();
        String sql = """
            INSERT INTO passengers (
                passenger_id, flight_id, first_name, last_name, age,
                gender, nationality, ticket_number, seat_number,
                checkin_time, boarding_time, luggage_weight,
                class_type, passenger_type, frequent_flyer
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.out.println("  ⚠ PostgreSQL unavailable; passenger saved locally only.");
                return;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getPassengerId());
                ps.setString(2, p.getFlightId());
                ps.setString(3, p.getFirstName());
                ps.setString(4, p.getLastName());
                ps.setInt(5, p.getAge());
                ps.setString(6, p.getGender());
                ps.setString(7, p.getNationality());
                ps.setString(8, p.getTicketNumber());
                ps.setString(9, p.getSeatNumber());
                ps.setString(10, p.getCheckinTime());
                ps.setString(11, p.getBoardingTime());
                ps.setDouble(12, p.getLuggageWeight());
                ps.setString(13, p.getClassType());
                ps.setString(14, p.getPassengerType());
                ps.setString(15, p.getFrequentFlyer());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("  ⚠ PostgreSQL save skipped: " + e.getMessage());
        }
    }

    public static boolean updatePassenger(Passenger p) {
        initializeSchema();
        String sql = """
            UPDATE passengers SET
                flight_id = ?, first_name = ?, last_name = ?, age = ?,
                gender = ?, nationality = ?, ticket_number = ?, seat_number = ?,
                checkin_time = ?, boarding_time = ?, luggage_weight = ?,
                class_type = ?, passenger_type = ?, frequent_flyer = ?
            WHERE passenger_id = ?
            """;

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getFlightId());
                ps.setString(2, p.getFirstName());
                ps.setString(3, p.getLastName());
                ps.setInt(4, p.getAge());
                ps.setString(5, p.getGender());
                ps.setString(6, p.getNationality());
                ps.setString(7, p.getTicketNumber());
                ps.setString(8, p.getSeatNumber());
                ps.setString(9, p.getCheckinTime());
                ps.setString(10, p.getBoardingTime());
                ps.setDouble(11, p.getLuggageWeight());
                ps.setString(12, p.getClassType());
                ps.setString(13, p.getPassengerType());
                ps.setString(14, p.getFrequentFlyer());
                ps.setString(15, p.getPassengerId());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("  ⚠ PostgreSQL update skipped: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePassenger(String passengerId) {
        initializeSchema();
        String sql = "DELETE FROM passengers WHERE passenger_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, passengerId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("  ⚠ PostgreSQL delete skipped: " + e.getMessage());
            return false;
        }
    }

    public static void insertFlight(Flight f) {
        initializeSchema();
        String sql = """
            INSERT INTO flights (
                flight_id, route_id, flight_number, airline,
                departure_time, arrival_time, flight_date,
                status, gate_assigned, passenger_capacity,
                checkin_count, boarding_count, delay_mins,
                priority_level
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.out.println("  ⚠ PostgreSQL unavailable; flight saved locally only.");
                return;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, f.getFlightId());
                ps.setString(2, f.getRouteId());
                ps.setString(3, f.getFlightNumber());
                ps.setString(4, f.getAirline());
                ps.setString(5, f.getDepartureTime().format(TIME_FORMATTER));
                ps.setString(6, f.getArrivalTime().format(TIME_FORMATTER));
                ps.setString(7, f.getFlightDate());
                ps.setString(8, f.getStatus());
                ps.setString(9, f.getGateAssigned());
                ps.setInt(10, f.getPassengerCapacity());
                ps.setInt(11, f.getCheckinCount());
                ps.setInt(12, f.getBoardingCount());
                ps.setInt(13, f.getDelayMins());
                ps.setInt(14, f.getPriorityLevel());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("  ⚠ PostgreSQL save skipped: " + e.getMessage());
        }
    }

    public static boolean updateFlight(Flight f) {
        initializeSchema();
        String sql = """
            UPDATE flights SET
                route_id = ?, flight_number = ?, airline = ?,
                departure_time = ?, arrival_time = ?, flight_date = ?,
                status = ?, gate_assigned = ?, passenger_capacity = ?,
                checkin_count = ?, boarding_count = ?, delay_mins = ?,
                priority_level = ?
            WHERE flight_id = ?
            """;

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, f.getRouteId());
                ps.setString(2, f.getFlightNumber());
                ps.setString(3, f.getAirline());
                ps.setString(4, f.getDepartureTime().format(TIME_FORMATTER));
                ps.setString(5, f.getArrivalTime().format(TIME_FORMATTER));
                ps.setString(6, f.getFlightDate());
                ps.setString(7, f.getStatus());
                ps.setString(8, f.getGateAssigned());
                ps.setInt(9, f.getPassengerCapacity());
                ps.setInt(10, f.getCheckinCount());
                ps.setInt(11, f.getBoardingCount());
                ps.setInt(12, f.getDelayMins());
                ps.setInt(13, f.getPriorityLevel());
                ps.setString(14, f.getFlightId());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("  ⚠ PostgreSQL update skipped: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteFlight(String flightId) {
        initializeSchema();
        String sql = "DELETE FROM flights WHERE flight_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, flightId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("  ⚠ PostgreSQL delete skipped: " + e.getMessage());
            return false;
        }
    }

    public static void insertRoute(Route r) {
        initializeSchema();
        String sql = """
            INSERT INTO routes (
                route_id, source_airport_id, dest_airport_id, distance_km,
                base_cost_usd, flight_duration_mins, fuel_cost_usd,
                crew_cost_usd, maintenance_cost_usd, total_cost_usd,
                demand_level
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?)
            """;

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.out.println("  ⚠ PostgreSQL unavailable; route saved locally only.");
                return;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, r.getRouteId());
                ps.setString(2, r.getSourceAirportId());
                ps.setString(3, r.getDestAirportId());
                ps.setDouble(4, r.getDistanceKm());
                ps.setDouble(5, r.getBaseCostUsd());
                ps.setInt(6, r.getFlightDurationMins());
                ps.setDouble(7, r.getFuelCostUsd());
                ps.setDouble(8, r.getCrewCostUsd());
                ps.setDouble(9, r.getMaintenanceCostUsd());
                ps.setDouble(10, r.getTotalCostUsd());
                ps.setString(11, r.getDemandLevel());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("  ⚠ PostgreSQL save skipped: " + e.getMessage());
        }
    }

    public static boolean updateRoute(Route r) {
        initializeSchema();
        String sql = """
            UPDATE routes SET
                source_airport_id = ?, dest_airport_id = ?, distance_km = ?,
                base_cost_usd = ?, flight_duration_mins = ?, fuel_cost_usd = ?,
                crew_cost_usd = ?, maintenance_cost_usd = ?, total_cost_usd = ?,
                demand_level = ?
            WHERE route_id = ?
            """;

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, r.getSourceAirportId());
                ps.setString(2, r.getDestAirportId());
                ps.setDouble(3, r.getDistanceKm());
                ps.setDouble(4, r.getBaseCostUsd());
                ps.setInt(5, r.getFlightDurationMins());
                ps.setDouble(6, r.getFuelCostUsd());
                ps.setDouble(7, r.getCrewCostUsd());
                ps.setDouble(8, r.getMaintenanceCostUsd());
                ps.setDouble(9, r.getTotalCostUsd());
                ps.setString(10, r.getDemandLevel());
                ps.setString(11, r.getRouteId());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("  ⚠ PostgreSQL update skipped: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteRoute(String routeId) {
        initializeSchema();
        String sql = "DELETE FROM routes WHERE route_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, routeId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("  ⚠ PostgreSQL delete skipped: " + e.getMessage());
            return false;
        }
    }
}