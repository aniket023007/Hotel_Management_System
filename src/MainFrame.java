
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainFrame extends JFrame {

    private final RoomDAO roomDAO = new RoomDAO();
    private final GuestDAO guestDAO = new GuestDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

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

        heading.setFont(
            new Font("Times New Roman", Font.BOLD, 26)
        );
        heading.setForeground(new Color(25, 55, 90));

        JLabel subtitle = new JLabel(
            "Receptionist Dashboard",
            SwingConstants.CENTER
        );

        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
        header.add(heading);
        header.add(subtitle);

        // Dashboard buttons
        JPanel buttonPanel = new JPanel(
            new GridLayout(4, 2, 15, 15)
        );

        JButton roomsButton = new JButton("View Rooms");
        JButton guestsButton = new JButton("View Guests");
        JButton addGuestButton = new JButton("Add Guest");
        JButton bookButton = new JButton("Book Room");
        JButton bookingButton = new JButton("View Bookings");
        JButton billButton = new JButton("Calculate Bill");
        JButton checkoutButton = new JButton("Check Out");

        JButton[] buttons = {
            roomsButton,
            guestsButton,
            addGuestButton,
            bookButton,
            bookingButton,
            billButton,
            checkoutButton
        };

        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setFocusPainted(false);
            buttonPanel.add(button);
        }

        // Button actions
        roomsButton.addActionListener(e -> showRooms());
        guestsButton.addActionListener(e -> showGuests());
        addGuestButton.addActionListener(e -> addGuest());
        bookButton.addActionListener(e -> bookRoom());
        bookingButton.addActionListener(e -> viewBooking());
        billButton.addActionListener(e -> calculateBill());
        checkoutButton.addActionListener(e -> checkout());

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void showRooms() {
        try {
            ByteArrayOutputStream output =
                new ByteArrayOutputStream();

            PrintStream oldOut = System.out;

            try (PrintStream newOut = new PrintStream(output)) {
                System.setOut(newOut);
                roomDAO.displayRooms();
            } finally {
                System.setOut(oldOut);
            }

            JTextArea area = new JTextArea(output.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));

            JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "Room Details",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showGuests() {
        try {
            ByteArrayOutputStream output =
                new ByteArrayOutputStream();

            PrintStream oldOut = System.out;

            try (PrintStream newOut = new PrintStream(output)) {
                System.setOut(newOut);
                guestDAO.displayGuests();
            } finally {
                System.setOut(oldOut);
            }

            JTextArea area = new JTextArea(output.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));

            JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "Guest Details",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            showError(ex);
        }
    }

    // NEW FEATURE: View who booked which room
  
private void viewBooking() {
    try {
        javax.swing.table.DefaultTableModel model =
            bookingDAO.getBookingsTableModel();

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        int[] widths = {90, 160, 120, 90, 120, 70, 110};

        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel()
                 .getColumn(i)
                 .setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(780, 350));

        String message = model.getRowCount() == 0
            ? "No bookings found."
            : "Total bookings: " + model.getRowCount();

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(message), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
            this,
            panel,
            "Hotel Booking Details",
            JOptionPane.INFORMATION_MESSAGE
        );

    } catch (Exception ex) {
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
            this,
            fields,
            "Add New Guest",
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

        } catch (Exception ex) {
            showError(ex);
        }
    }

    
private void bookRoom() {

    JTextField bookingIdField = new JTextField();
    JTextField guestIdField = new JTextField();
    JTextField roomIdField = new JTextField();
    JTextField nightsField = new JTextField();

    Object[] fields = {
        "Booking ID:", bookingIdField,
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
        int bookingId = Integer.parseInt(
            bookingIdField.getText().trim()
        );

        int guestId = Integer.parseInt(
            guestIdField.getText().trim()
        );

        int roomId = Integer.parseInt(
            roomIdField.getText().trim()
        );

        int nights = Integer.parseInt(
            nightsField.getText().trim()
        );

        if (bookingId <= 0 || guestId <= 0 ||
                roomId <= 0 || nights <= 0) {
            throw new IllegalArgumentException(
                "All IDs and nights must be positive."
            );
        }

        bookingDAO.bookRoom(
            bookingId, guestId, roomId, nights
        );

        JOptionPane.showMessageDialog(
            this,
            "Room booked successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Please enter valid numeric IDs and nights.",
            "Input Error",
            JOptionPane.ERROR_MESSAGE
        );

    } catch (Exception ex) {
        showError(ex);
    }
}

    private void calculateBill() {
        String input = JOptionPane.showInputDialog(
            this, "Enter Booking ID:"
        );

        if (input == null) {
            return;
        }

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

        if (input == null) {
            return;
        }

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
}
