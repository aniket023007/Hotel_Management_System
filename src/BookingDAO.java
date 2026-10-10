import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookingDAO {

    public void bookRoom(
            int guestId,
            int roomId,
            int numberOfNights) throws SQLException {

        if (numberOfNights <= 0) {
            throw new IllegalArgumentException(
                "Number of nights must be greater than zero."
            );
        }

        String bookingSQL =
            "INSERT INTO bookings "
            + "(guest_id, room_id, number_of_nights, status) "
            + "VALUES (?, ?, ?, 'Booked')";

        String roomSQL =
            "UPDATE rooms SET status = 'Booked' "
            + "WHERE room_id = ? AND status = 'Available'";

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

                bookingStatement.setInt(1, guestId);
                bookingStatement.setInt(2, roomId);
                bookingStatement.setInt(3, numberOfNights);

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
}