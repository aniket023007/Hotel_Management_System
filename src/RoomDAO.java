import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomDAO {

    // Display all rooms from MySQL
    public void displayRooms() throws SQLException {

        String sql = "SELECT room_id, room_number, room_type, "
                   + "price_per_night, status FROM rooms";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== ROOMS FROM MYSQL =====");

            while (rs.next()) {

                System.out.println(
                    "Room ID: " + rs.getInt("room_id")
                );

                System.out.println(
                    "Room Number: " + rs.getString("room_number")
                );

                System.out.println(
                    "Room Type: " + rs.getString("room_type")
                );

                System.out.println(
                    "Price Per Night: Rs. "
                    + rs.getBigDecimal("price_per_night")
                );

                System.out.println(
                    "Status: " + rs.getString("status")
                );

                System.out.println("----------------------");
            }
        }
    }
}