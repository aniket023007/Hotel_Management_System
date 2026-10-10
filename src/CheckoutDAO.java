import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class CheckoutDAO {

    public BigDecimal checkout(int bookingId) throws SQLException {

        String selectSQL =
            "SELECT b.room_id, b.number_of_nights, "
            + "r.price_per_night "
            + "FROM bookings b "
            + "JOIN rooms r ON b.room_id = r.room_id "
            + "WHERE b.booking_id = ? AND b.status = 'Booked'";

        String bookingSQL =
            "UPDATE bookings SET status = 'Checked Out' "
            + "WHERE booking_id = ? AND status = 'Booked'";

        String roomSQL =
            "UPDATE rooms SET status = 'Available' "
            + "WHERE room_id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int roomId;
                int nights;
                BigDecimal price;

                try (PreparedStatement ps =
                        con.prepareStatement(selectSQL)) {

                    ps.setInt(1, bookingId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                "Active booking not found."
                            );
                        }

                        roomId = rs.getInt("room_id");
                        nights = rs.getInt("number_of_nights");
                        price = rs.getBigDecimal("price_per_night");
                    }
                }

                BigDecimal total = price.multiply(
                    BigDecimal.valueOf(nights)
                );

                try (PreparedStatement ps =
                        con.prepareStatement(bookingSQL)) {

                    ps.setInt(1, bookingId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "Could not complete checkout."
                        );
                    }
                }

                try (PreparedStatement ps =
                        con.prepareStatement(roomSQL)) {

                    ps.setInt(1, roomId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "Could not update room availability."
                        );
                    }
                }

                con.commit();
                return total;

            } catch (SQLException | RuntimeException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }


public java.math.BigDecimal calculateBill(int bookingId)
        throws java.sql.SQLException {

    String sql = "SELECT b.number_of_nights, r.price_per_night " +
                 "FROM bookings b " +
                 "JOIN rooms r ON b.room_id = r.room_id " +
                 "WHERE b.booking_id = ? AND b.status = 'Booked'";

    try (java.sql.Connection con = DatabaseConnection.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, bookingId);

        try (java.sql.ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) {
                throw new java.sql.SQLException(
                    "Active booking not found. Check the Booking ID."
                );
            }

            java.math.BigDecimal price =
                rs.getBigDecimal("price_per_night");

            int nights = rs.getInt("number_of_nights");

            return price.multiply(
                java.math.BigDecimal.valueOf(nights)
            );
        }
    }
}


}