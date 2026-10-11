
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class BookingDAO {

    public void bookRoom(
            int bookingId,
            int guestId,
            int roomId,
            int numberOfNights) throws SQLException {

        if (bookingId <= 0 || guestId <= 0 ||
                roomId <= 0 || numberOfNights <= 0) {
            throw new IllegalArgumentException(
                "All IDs and nights must be positive."
            );
        }

        String bookingSQL =
            "INSERT INTO bookings " +
            "(booking_id, guest_id, room_id, number_of_nights, status) " +
            "VALUES (?, ?, ?, ?, 'Booked')";

        String roomSQL =
            "UPDATE rooms SET status = 'Booked' " +
            "WHERE room_id = ? AND status = 'Available'";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try (
                PreparedStatement roomStatement =
                    con.prepareStatement(roomSQL);
                PreparedStatement bookingStatement =
                    con.prepareStatement(bookingSQL)
            ) {
                roomStatement.setInt(1, roomId);

                int updated = roomStatement.executeUpdate();

                if (updated == 0) {
                    throw new SQLException(
                        "Room not found or not available."
                    );
                }

                bookingStatement.setInt(1, bookingId);
                bookingStatement.setInt(2, guestId);
                bookingStatement.setInt(3, roomId);
                bookingStatement.setInt(4, numberOfNights);

                bookingStatement.executeUpdate();
                con.commit();

                System.out.println("Room booked successfully!");

            } catch (SQLException | RuntimeException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    // Fetch bookings into a table model
    public DefaultTableModel getBookingsTableModel()
            throws SQLException {

        String sql =
            "SELECT b.booking_id, g.full_name, g.phone, " +
            "r.room_number, r.room_type, " +
            "b.number_of_nights, b.status " +
            "FROM bookings b " +
            "JOIN guests g ON b.guest_id = g.guest_id " +
            "JOIN rooms r ON b.room_id = r.room_id " +
            "ORDER BY b.booking_id";

        String[] columns = {
            "Booking ID",
            "Guest Name",
            "Phone",
            "Room No.",
            "Room Type",
            "Nights",
            "Status"
        };

        DefaultTableModel model =
            new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("booking_id"),
                    rs.getString("full_name"),
                    rs.getString("phone"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getInt("number_of_nights"),
                    rs.getString("status")
                };

                model.addRow(row);
            }
        }

        return model;
    }
}
