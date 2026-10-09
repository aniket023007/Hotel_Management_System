
import java.util.ArrayList;
import java.util.List;

public class HotelService {

    private List<Guest> guests = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    // Add a guest
    public void addGuest(Guest guest) {
        guests.add(guest);
        System.out.println("Guest added successfully!");
    }

    // Add a room
    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("Room added successfully!");
    }

    // Display all rooms
    public void displayRooms() {
        System.out.println("\n===== ROOM LIST =====");

        for (Room room : rooms) {
            room.displayRoomDetails();
            System.out.println("---------------------");
        }
    }

    // Book a room
    public Booking bookRoom(
            int bookingId,
            Guest guest,
            Room room,
            int numberOfNights) {

        if (!guests.contains(guest)) {
            throw new IllegalArgumentException(
                "Guest is not registered!"
            );
        }

        if (!rooms.contains(room)) {
            throw new IllegalArgumentException(
                "Room does not belong to this hotel!"
            );
        }

        if (numberOfNights <= 0) {
            throw new IllegalArgumentException(
                "Number of nights must be greater than zero!"
            );
        }

        if (!room.isAvailable()) {
            throw new IllegalStateException(
                "Room is already occupied!"
            );
        }

        Booking booking = new Booking(
            bookingId, guest, room, numberOfNights
        );

        room.bookRoom();
        bookings.add(booking);

        System.out.println("Booking created successfully!");

        return booking;
    }

    // Display bookings
    public void displayBookings() {
        System.out.println("\n===== BOOKING LIST =====");

        for (Booking booking : bookings) {
            System.out.println(
                "Booking ID: " + booking.getBookingId()
            );
            System.out.println(
                "Guest: " + booking.getGuest().getName()
            );
            System.out.println(
                "Room: " + booking.getRoom().getRoomNumber()
            );
            System.out.println(
                "Number of Nights: " + booking.getNumberOfNights()
            );
            System.out.println("---------------------");
        }
    }

    // Checkout and calculate bill
    public void checkOut(int bookingId) {

        for (int i = 0; i < bookings.size(); i++) {

            Booking booking = bookings.get(i);

            if (booking.getBookingId() == bookingId) {

                Bill bill = new Bill(booking);
                bill.displayBill();

                booking.getRoom().checkOut();

                bookings.remove(i);

                System.out.println("Checkout successful!");
                return;
            }
        }

        throw new IllegalArgumentException(
            "Booking not found!"
        );
    }
}
