
public class Bill {

    private Booking booking;
    private double totalAmount;

    public Bill(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException(
                "Booking cannot be null."
            );
        }

        this.booking = booking;
        this.totalAmount = calculateTotal();
    }

    public double calculateTotal() {
        return booking.getNumberOfNights()
                * booking.getRoom().getPricePerNight();
    }

    public void displayBill() {
        System.out.println("===== HOTEL BILL =====");
        System.out.println(
            "Guest: " + booking.getGuest().getName()
        );
        System.out.println(
            "Room: " + booking.getRoom().getRoomNumber()
        );
        System.out.println(
            "Nights: " + booking.getNumberOfNights()
        );
        System.out.println(
            "Rate per night: Rs. "
            + booking.getRoom().getPricePerNight()
        );
        System.out.println(
            "Total Amount: Rs. " + totalAmount
        );
    }
}
