import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

// Main class connects to MySQL and starts the Restaurant Tracker DMS program.
public class Main {
    public static void main(String[] args) {
        Connection connection = getDatabaseConnection();

        if (connection != null) {
            JOptionPane.showMessageDialog(null, "Connected to MySQL successfully!");
            new RestaurantTrackerGUI(connection);
        }
    }

    // Asks the user for the information needed to connect to MySQL.
    private static Connection getDatabaseConnection() {
        while (true) {
            JTextField serverField = new JTextField("localhost:3306");
            JTextField usernameField = new JTextField("root");
            JPasswordField passwordField = new JPasswordField();

            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
            panel.add(new JLabel("MySQL Server:"));
            panel.add(serverField);
            panel.add(new JLabel("Username:"));
            panel.add(usernameField);
            panel.add(new JLabel("Password:"));
            panel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(null, panel,
                    "Connect to Restaurant Tracker Database",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION) {
                return null;
            }

            String serverAddress = serverField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (serverAddress.isEmpty() || username.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Server address and username cannot be blank.");
                continue;
            }

            try {
                return DatabaseConnection.connect(serverAddress, username, password);
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null,
                        "Connection failed: " + error.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
