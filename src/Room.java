
public class Room {

    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private boolean available;

    public Room(int roomNumber, String roomType,
                double pricePerNight) {

        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.available = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    public void bookRoom() {
        if (available) {
            available = false;
            System.out.println("Room booked successfully!");
        } else {
            System.out.println("Room is already occupied!");
        }
    }

    public void checkOut() {
        available = true;
        System.out.println("Checkout completed. Room is available.");
    }

    public void displayRoomDetails() {
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Room Type: " + roomType);
        System.out.println("Price Per Night: Rs. " + pricePerNight);
        System.out.println("Available: " + available);
    }
}
