import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    private final RoomDAO roomDAO = new RoomDAO();
    private final GuestDAO guestDAO = new GuestDAO();

    public MainFrame() {

        setTitle("Hotel Management System");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        JLabel heading = new JLabel(
            "HOTEL MANAGEMENT SYSTEM",
            SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setForeground(new Color(25, 55, 90));

        JLabel subtitle = new JLabel(
            "Receptionist Dashboard",
            SwingConstants.CENTER
        );

        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
        header.add(heading);
        header.add(subtitle);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));

        JButton roomsButton = new JButton("View Rooms");
        JButton guestsButton = new JButton("View Guests");
        JButton addGuestButton = new JButton("Add Guest");
        JButton bookButton = new JButton("Book Room");
        JButton billButton = new JButton("Calculate Bill");
        JButton checkoutButton = new JButton("Check Out");

        JButton[] buttons = {
            roomsButton, guestsButton, addGuestButton,
            bookButton, billButton, checkoutButton
        };

        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setFocusPainted(false);
            buttonPanel.add(button);
        }

        roomsButton.addActionListener(e -> showRooms());
        guestsButton.addActionListener(e -> showGuests());
        addGuestButton.addActionListener(e -> addGuest());

        // We'll connect these operations in the next steps.
        bookButton.addActionListener(e -> bookRoom());

        billButton.addActionListener(e -> calculateBill());

        checkoutButton.addActionListener(e ->checkout());

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void showRooms() {

        try {
            // Redirect DAO output from the terminal into a dialog.
            java.io.ByteArrayOutputStream output =
                new java.io.ByteArrayOutputStream();

            java.io.PrintStream oldOut = System.out;

            try (java.io.PrintStream newOut =
                    new java.io.PrintStream(output)) {

                System.setOut(newOut);
                roomDAO.displayRooms();

            } finally {
                System.setOut(oldOut);
            }

            JTextArea area = new JTextArea(output.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));

            JOptionPane.showMessageDialog(
                this, new JScrollPane(area),
                "Room Details", JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void showGuests() {

        try {
            java.io.ByteArrayOutputStream output =
                new java.io.ByteArrayOutputStream();

            java.io.PrintStream oldOut = System.out;

            try (java.io.PrintStream newOut =
                    new java.io.PrintStream(output)) {

                System.setOut(newOut);
                guestDAO.displayGuests();

            } finally {
                System.setOut(oldOut);
            }

            JTextArea area = new JTextArea(output.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));

            JOptionPane.showMessageDialog(
                this, new JScrollPane(area),
                "Guest Details", JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void addGuest() {

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();

        Object[] fields = {
            "Full Name:", nameField,
            "Phone:", phoneField,
            "Email:", emailField,
            "Address:", addressField
        };

        int result = JOptionPane.showConfirmDialog(
            this, fields, "Add New Guest",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Guest name is required."
            );
            return;
        }

        try {
            guestDAO.addGuest(
                nameField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim(),
                addressField.getText().trim()
            );

            JOptionPane.showMessageDialog(
                this, "Guest saved successfully!"
            );

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(
            this,
            ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    private void bookRoom() {

    JTextField guestIdField = new JTextField();
    JTextField roomIdField = new JTextField();
    JTextField nightsField = new JTextField();

    Object[] fields = {
        "Guest ID:", guestIdField,
        "Room ID:", roomIdField,
        "Number of Nights:", nightsField
    };

    int result = JOptionPane.showConfirmDialog(
        this,
        fields,
        "Book a Room",
        JOptionPane.OK_CANCEL_OPTION
    );

    if (result != JOptionPane.OK_OPTION) {
        return;
    }

    try {
        int guestId = Integer.parseInt(
            guestIdField.getText().trim()
        );

        int roomId = Integer.parseInt(
            roomIdField.getText().trim()
        );

        int nights = Integer.parseInt(
            nightsField.getText().trim()
        );

        if (guestId <= 0 || roomId <= 0 || nights <= 0) {
            throw new IllegalArgumentException(
                "All IDs and nights must be positive numbers."
            );
        }

        BookingDAO bookingDAO = new BookingDAO();

        bookingDAO.bookRoom(guestId, roomId, nights);

        JOptionPane.showMessageDialog(
            this,
            "Room booked successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(
            this,
            "Please enter valid numeric IDs and number of nights."
        );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
            this,
            ex.getMessage(),
            "Booking Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

private void calculateBill() {
    String input = JOptionPane.showInputDialog(
        this, "Enter Booking ID:"
    );

    if (input == null) return;

    try {
        int bookingId = Integer.parseInt(input.trim());

        if (bookingId <= 0) {
            throw new IllegalArgumentException(
                "Booking ID must be positive."
            );
        }

        java.math.BigDecimal total =
            new CheckoutDAO().calculateBill(bookingId);

        JOptionPane.showMessageDialog(
            this,
            "Booking ID: " + bookingId +
            "\nTotal Bill: ₹" + total,
            "Bill Details",
            JOptionPane.INFORMATION_MESSAGE
        );

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
            this, "Please enter a valid Booking ID."
        );
    } catch (Exception ex) {
        showError(ex);
    }
}

private void checkout() {
    String input = JOptionPane.showInputDialog(
        this, "Enter Booking ID to check out:"
    );

    if (input == null) return;

    try {
        int bookingId = Integer.parseInt(input.trim());

        if (bookingId <= 0) {
            throw new IllegalArgumentException(
                "Booking ID must be positive."
            );
        }

        java.math.BigDecimal total =
            new CheckoutDAO().checkout(bookingId);

        JOptionPane.showMessageDialog(
            this,
            "Checkout successful!\n" +
            "Booking ID: " + bookingId +
            "\nFinal Bill: ₹" + total +
            "\nRoom is now available.",
            "Checkout Complete",
            JOptionPane.INFORMATION_MESSAGE
        );

        showRooms();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
            this, "Please enter a valid Booking ID."
        );
    } catch (Exception ex) {
        showError(ex);
    }
}




}