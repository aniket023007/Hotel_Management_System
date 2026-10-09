
public class Booking {

    private int bookingId;
    private Guest guest;
    private Room room;
    private int numberOfNights;

    public Booking(int bookingId, Guest guest,
                   Room room, int numberOfNights) {

        if (guest == null || room == null) {
            throw new IllegalArgumentException(
                "Guest and room are required."
            );
        }

        if (numberOfNights <= 0) {
            throw new IllegalArgumentException(
                "Nights must be greater than zero."
            );
        }

        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.numberOfNights = numberOfNights;
    }

    public int getBookingId() {
        return bookingId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void displayBookingDetails() {
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Guest: " + guest.getName());
        System.out.println("Room: " + room.getRoomNumber());
        System.out.println("Number of Nights: " + numberOfNights);
    }
}
