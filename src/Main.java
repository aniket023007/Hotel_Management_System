
public class Main {

    public static void main(String[] args) {

        // Create hotel service
        HotelService hotel = new HotelService();

        // Create guest
        Guest guest = new Guest(
            101,
            "Rahul Sharma",
            "9876543210"
        );

        // Create room
        Room room = new Room(
            201,
            "Deluxe",
            2000
        );

        // Register guest and room
        hotel.addGuest(guest);
        hotel.addRoom(room);

        // Display rooms
        hotel.displayRooms();

        // Book room for 3 nights
        Booking booking = hotel.bookRoom(
            1,
            guest,
            room,
            3
        );

        // Display bookings
        hotel.displayBookings();

        // Checkout
        System.out.println("\n===== CHECKOUT =====");
        hotel.checkOut(booking.getBookingId());

        // Display room status after checkout
        System.out.println("\n===== AFTER CHECKOUT =====");
        hotel.displayRooms();
    }
}
