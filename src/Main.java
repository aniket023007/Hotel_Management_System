import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        RoomDAO roomDAO = new RoomDAO();

        try {
            roomDAO.displayRooms();

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}