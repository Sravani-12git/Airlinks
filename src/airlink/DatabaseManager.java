package airlink;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseManager {

    public static void insertPassenger(Passenger p) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
            "INSERT INTO passengers "
            + "(passenger_id, flight_id, first_name, last_name, age,"
            + " gender, nationality, ticket_number, seat_number,"
            + " checkin_time, boarding_time, luggage_weight,"
            + " class_type, passenger_type, frequent_flyer)"
            + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1,p.getPassengerId());
            ps.setString(2,p.getFlightId());
            ps.setString(3,p.getFirstName());
            ps.setString(4,p.getLastName());
            ps.setInt(5,p.getAge());
            ps.setString(6,p.getGender());
            ps.setString(7,p.getNationality());
            ps.setString(8,p.getTicketNumber());
            ps.setString(9,p.getSeatNumber());
            ps.setString(10,p.getCheckinTime());
            ps.setString(11,p.getBoardingTime());
            ps.setDouble(12,p.getLuggageWeight());
            ps.setString(13,p.getClassType());
            ps.setString(14,p.getPassengerType());
            ps.setString(15,p.getFrequentFlyer());

            ps.executeUpdate();

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void insertFlight(Flight f) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
            "INSERT INTO flights "
            + "(flight_id, route_id, flight_number, airline,"
            + " status, gate_assigned, passenger_capacity,"
            + " checkin_count, boarding_count, delay_mins,"
            + " priority_level)"
            + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1,f.getFlightId());
            ps.setString(2,f.getRouteId());
            ps.setString(3,f.getFlightNumber());
            ps.setString(4,f.getAirline());
            ps.setString(5,f.getStatus());
            ps.setString(6,f.getGateAssigned());
            ps.setInt(7,f.getPassengerCapacity());
            ps.setInt(8,f.getCheckinCount());
            ps.setInt(9,f.getBoardingCount());
            ps.setInt(10,f.getDelayMins());
            ps.setInt(11,f.getPriorityLevel());

            ps.executeUpdate();

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}