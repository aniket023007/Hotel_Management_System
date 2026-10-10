import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuestDAO {

    // Save a new guest
    public void addGuest(
            String fullName,
            String phone,
            String email,
            String address) throws SQLException {

        String sql = "INSERT INTO guests "
                + "(full_name, phone, email, address) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);

            ps.executeUpdate();

            System.out.println("Guest saved to MySQL successfully!");
        }
    }

    // Display all guests
    public void displayGuests() throws SQLException {

        String sql = "SELECT * FROM guests";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== GUESTS FROM MYSQL =====");

            while (rs.next()) {
                System.out.println(
                    "Guest ID: " + rs.getInt("guest_id")
                );
                System.out.println(
                    "Name: " + rs.getString("full_name")
                );
                System.out.println(
                    "Phone: " + rs.getString("phone")
                );
                System.out.println(
                    "Email: " + rs.getString("email")
                );
                System.out.println(
                    "Address: " + rs.getString("address")
                );
                System.out.println("----------------------");
            }
        }
    }
}