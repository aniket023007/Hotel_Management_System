
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class RevenueDAO {

    public BigDecimal getTotalRevenue() throws SQLException {

        String sql =
            "SELECT COALESCE(SUM(b.number_of_nights * r.price_per_night), 0) " +
            "AS total_revenue " +
            "FROM bookings b " +
            "JOIN rooms r ON b.room_id = r.room_id " +
            "WHERE b.status = 'Checked Out'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("total_revenue");
            }

            return BigDecimal.ZERO;
        }
    }
}
