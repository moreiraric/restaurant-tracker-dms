import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Provides the graphical user interface for viewing and managing restaurants.
 * The GUI uses a {@link RestaurantManager} for restaurant logic and a
 * {@link RestaurantDatabase} for database operations.
 */
public class RestaurantTrackerGUI extends JFrame {

    /** Manages the restaurant records displayed by the GUI. */
    private RestaurantManager manager;

    /** Holds the restaurant cards shown in the main window. */
    private JPanel cardPanel;

    /** Handles the GUI's MySQL database operations. */
    private RestaurantDatabase database;

    /**
     * Creates the restaurant tracker window and loads restaurants from the database.
     *
     * @param connection the active MySQL connection used by the application
     */
    public RestaurantTrackerGUI(Connection connection){
        manager = new RestaurantManager();
        database = new RestaurantDatabase(connection);

        setTitle("Restaurant Tracker");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            /**
             * Closes the database connection when the application window closes.
             *
             * @param event the window closing event
             */
            @Override
            public void windowClosing(WindowEvent event) {
                closeDatabaseConnection();
            }
        });

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(230, 230, 230));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCardScrollPane(), BorderLayout.CENTER);

        refreshDatabaseView();

        setVisible(true);
    }

    // Closes the MySQL connection before the application exits.
    private void closeDatabaseConnection(){
        try {
            database.closeConnection();
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(this,
                    "Database connection could not be closed: " + error.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Reloads the restaurant records stored in MySQL.
    private boolean refreshDatabaseView(){
        try {
            ArrayList<Restaurant> restaurants = database.loadRestaurantsFromDatabase();
            RestaurantManager refreshedManager = new RestaurantManager();

            for (Restaurant restaurant : restaurants){
                refreshedManager.addRestaurant(restaurant);
            }

            manager = refreshedManager;
            refreshCards(manager.getAllRestaurants());
            return true;
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(this,
                    "Restaurants could not be loaded: " + error.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Creates the top toolbar with regular buttons
    private JPanel createTopPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(230, 230, 230));
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel titleLabel = new JLabel("Restaurant Tracker");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(230, 230, 230));
        toolBar.setBorderPainted(false);

        JButton addButton = createMenuButton("Add Restaurant");
        JButton refreshButton = createMenuButton("Refresh Database");
        JButton cardButton = createMenuButton("Card View");
        JButton listButton = createMenuButton("List View");
        JButton sortButton = createMenuButton("Sort by Rating");
        JButton recentButton = createMenuButton("Sort by Recently Visited");
        JButton costButton = createMenuButton("Sort by Cost");
        JButton reportButton = createMenuButton("Generate Report");

        toolBar.add(addButton);
        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(cardButton);
        toolBar.add(listButton);
        toolBar.addSeparator();
        toolBar.add(sortButton);
        toolBar.add(recentButton);
        toolBar.add(costButton);
        toolBar.addSeparator();
        toolBar.add(reportButton);

        topPanel.add(toolBar, BorderLayout.CENTER);

        addButton.addActionListener(event -> showAddRestaurantDialog());
        refreshButton.addActionListener(event -> refreshDatabaseView());
        cardButton.addActionListener(event -> refreshCards(manager.getAllRestaurants()));
        listButton.addActionListener(event -> showListView());
        sortButton.addActionListener(event -> showSortedRestaurants());
        recentButton.addActionListener(event -> showRecentlyVisitedRestaurants());
        costButton.addActionListener(event -> showSortedByCost());
        reportButton.addActionListener(event -> showReportDialog());

        return topPanel;
    }

    // Creates the buttons used in the toolbar
    private JButton createMenuButton(String text){
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.PLAIN, 13));
        button.setFocusPainted(false);
        return button;
    }

    // Creates the scrollable card area
    private JScrollPane createCardScrollPane(){
        cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(0, 3, 16, 16));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    // Refreshes the restaurant cards on the screen
    private void refreshCards(ArrayList<Restaurant> restaurants){
        cardPanel.removeAll();
        cardPanel.setLayout(new GridLayout(0, 3, 16, 16));

        if (restaurants.isEmpty()){
            JLabel emptyLabel = new JLabel("No restaurants to show.");
            emptyLabel.setFont(new Font("Inter", Font.PLAIN, 18));
            cardPanel.add(emptyLabel);
        } else {
            for (Restaurant restaurant : restaurants){
                cardPanel.add(createRestaurantCard(restaurant));
            }
        }

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    // Shows restaurants in a compact list view
    private void showListView(){
        ArrayList<Restaurant> restaurants = manager.getAllRestaurants();
        cardPanel.removeAll();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));

        if (restaurants.isEmpty()){
            JLabel emptyLabel = new JLabel("No restaurants to show.");
            emptyLabel.setFont(new Font("Inter", Font.PLAIN, 18));
            cardPanel.add(emptyLabel);
        } else {
            for (Restaurant restaurant : restaurants){
                cardPanel.add(createRestaurantListItem(restaurant));
                cardPanel.add(Box.createVerticalStrut(10));
            }
        }

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    // Creates one row for the list view
    private JPanel createRestaurantListItem(Restaurant restaurant){
        JPanel row = new JPanel(new BorderLayout(30, 0));
        row.setBackground(new Color(245, 245, 245));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel nameLabel = new JLabel(restaurant.getName());
        nameLabel.setFont(new Font("Inter", Font.BOLD, 18));

        JLabel detailsLabel = new JLabel(restaurant.getCuisineType() + " | "
                + getRatingStars(restaurant.getUserRating()) + " | "
                + getPriceSymbols(restaurant.getPriceLevel()) + " | "
                + restaurant.getLocation());
        detailsLabel.setFont(new Font("Inter", Font.PLAIN, 14));

        JButton menuButton = createContextButton(restaurant);

        row.add(nameLabel, BorderLayout.WEST);
        row.add(detailsLabel, BorderLayout.CENTER);
        row.add(menuButton, BorderLayout.EAST);

        return row;
    }

    // Creates one visual card for a restaurant
    private JPanel createRestaurantCard(Restaurant restaurant){
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JLabel nameLabel = new JLabel(restaurant.getName());
        nameLabel.setFont(new Font("Inter", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton menuButton = createContextButton(restaurant);
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actionRow.setBackground(new Color(245, 245, 245));
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        actionRow.add(menuButton);

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(createCardLine("Cuisine type: ", restaurant.getCuisineType()));
        card.add(createCardLine("Rating: ", getRatingStars(restaurant.getUserRating())));
        card.add(createCardLine("Location: ", restaurant.getLocation()));
        card.add(createCardLine("Price Level: ", getPriceSymbols(restaurant.getPriceLevel())));
        card.add(createCardLine("Visited? ", restaurant.getVisitedStatus() ? "Yes" : "No"));
        card.add(createCardLine("Date Visited: ", restaurant.getDateVisited()));
        card.add(createCardLine("Notes: ", restaurant.getNotes()));
        card.add(Box.createVerticalStrut(12));
        card.add(actionRow);

        return card;
    }

    // Creates an edit and delete menu for one restaurant.
    private JButton createContextButton(Restaurant restaurant){
        JButton menuButton = new JButton("...");
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");

        editItem.addActionListener(event -> editRestaurant(restaurant));
        deleteItem.addActionListener(event -> deleteRestaurant(restaurant));

        contextMenu.add(editItem);
        contextMenu.add(deleteItem);

        menuButton.addActionListener(event ->
                contextMenu.show(menuButton, 0, menuButton.getHeight()));

        return menuButton;
    }

    // Creates one line of text for a restaurant card
    private JLabel createCardLine(String label, String value){
        JLabel line = new JLabel("<html><b>" + label + "</b>" + value + "</html>");
        line.setFont(new Font("Inter", Font.PLAIN, 16));
        line.setAlignmentX(Component.LEFT_ALIGNMENT);
        line.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        return line;
    }

    // Converts price level number into dollar signs
    private String getPriceSymbols(int priceLevel){
        String symbols = "";

        for (int i = 0; i < priceLevel; i++){
            symbols += "$";
        }

        return symbols;
    }

    // Converts rating number into star emojis
    private String getRatingStars(double userRating){
        String stars = "";
        int rating = (int) userRating;

        for (int i = 0; i < rating; i++){
            stars += "⭐";
        }

        return stars;
    }

    // Shows dialog for adding a restaurant
    private void showAddRestaurantDialog(){
        Restaurant restaurant = getRestaurantFromDialog(null);

        if (restaurant != null){
            if (!manager.isValidRestaurant(restaurant)){
                JOptionPane.showMessageDialog(this,
                        "Restaurant could not be added. Please check the input.");
                return;
            }

            try {
                boolean added = database.addRestaurantToDatabase(restaurant);

                if (added){
                    manager.addRestaurant(restaurant);
                    refreshCards(manager.getAllRestaurants());
                    JOptionPane.showMessageDialog(this, "Restaurant added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Restaurant could not be added.");
                }
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(this,
                        "Restaurant could not be added: " + error.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Shows the edit dialog for the selected restaurant.
    private void editRestaurant(Restaurant restaurant){
        ArrayList<Restaurant> restaurants = manager.getAllRestaurants();
        int index = restaurants.indexOf(restaurant);

        if (index == -1){
            JOptionPane.showMessageDialog(this, "Restaurant could not be found.");
            return;
        }

        Restaurant updatedRestaurant = getRestaurantFromDialog(restaurant);

        if (updatedRestaurant != null){
            updatedRestaurant.setRestaurantId(restaurant.getRestaurantId());

            if (!manager.isValidRestaurant(updatedRestaurant)){
                JOptionPane.showMessageDialog(this,
                        "Restaurant could not be updated. Please check the input.");
                return;
            }

            try {
                boolean updated = database.updateRestaurantInDatabase(updatedRestaurant);

                if (updated){
                    manager.updateRestaurant(index, updatedRestaurant);
                    refreshCards(manager.getAllRestaurants());
                    JOptionPane.showMessageDialog(this, "Restaurant updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Restaurant could not be updated. It may no longer exist.");
                }
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(this,
                        "Restaurant could not be updated: " + error.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Deletes the selected restaurant after confirmation.
    private void deleteRestaurant(Restaurant restaurant){
        ArrayList<Restaurant> restaurants = manager.getAllRestaurants();
        int index = restaurants.indexOf(restaurant);

        if (index == -1){
            JOptionPane.showMessageDialog(this, "Restaurant could not be found.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + restaurant.getName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION){
            try {
                boolean deleted =
                        database.deleteRestaurantFromDatabase(restaurant.getRestaurantId());

                if (deleted){
                    manager.deleteRestaurant(index);
                    refreshCards(manager.getAllRestaurants());
                    JOptionPane.showMessageDialog(this, "Restaurant deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Restaurant could not be deleted. It may no longer exist.");
                }
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(this,
                        "Restaurant could not be deleted: " + error.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Shows restaurants sorted by rating
    private void showSortedRestaurants(){
        ArrayList<Restaurant> sortedRestaurants = new ArrayList<Restaurant>(manager.getAllRestaurants());

        sortedRestaurants.sort(Comparator.comparingDouble(Restaurant::getUserRating).reversed());

        refreshCards(sortedRestaurants);
    }

    // Shows restaurants sorted by most recently visited
    private void showRecentlyVisitedRestaurants(){
        ArrayList<Restaurant> sortedRestaurants = new ArrayList<Restaurant>(manager.getAllRestaurants());

        sortedRestaurants.sort(Comparator.comparing(this::getDateForSorting).reversed());

        refreshCards(sortedRestaurants);
    }

    // Shows restaurants sorted by price level
    private void showSortedByCost(){
        ArrayList<Restaurant> sortedRestaurants = new ArrayList<Restaurant>(manager.getAllRestaurants());

        sortedRestaurants.sort(Comparator.comparingInt(Restaurant::getPriceLevel));

        refreshCards(sortedRestaurants);
    }

    // Converts the restaurant date into a date that can be sorted
    private LocalDate getDateForSorting(Restaurant restaurant){
        if (!restaurant.getVisitedStatus() || restaurant.getDateVisited().equals("N/A")){
            return LocalDate.of(1900, 1, 1);
        }

        String[] dateParts = restaurant.getDateVisited().split("/");

        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        return LocalDate.of(year, month, day);
    }

    // Shows summary report
    private void showReportDialog(){
        if (refreshDatabaseView()){
            JOptionPane.showMessageDialog(this, manager.generateSummaryReport(),
                    "Restaurant Summary Report", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Gets restaurant data from a dialog form
    private Restaurant getRestaurantFromDialog(Restaurant existingRestaurant){
        JTextField nameField = new JTextField();
        JTextField cuisineField = new JTextField();
        JTextField locationField = new JTextField();

        ButtonGroup priceButtonGroup = new ButtonGroup();
        JRadioButton oneDollarButton = new JRadioButton("$");
        JRadioButton twoDollarButton = new JRadioButton("$$");
        JRadioButton threeDollarButton = new JRadioButton("$$$");
        JRadioButton fourDollarButton = new JRadioButton("$$$$");
        JRadioButton fiveDollarButton = new JRadioButton("$$$$$");

        priceButtonGroup.add(oneDollarButton);
        priceButtonGroup.add(twoDollarButton);
        priceButtonGroup.add(threeDollarButton);
        priceButtonGroup.add(fourDollarButton);
        priceButtonGroup.add(fiveDollarButton);

        oneDollarButton.setSelected(true);

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pricePanel.add(oneDollarButton);
        pricePanel.add(twoDollarButton);
        pricePanel.add(threeDollarButton);
        pricePanel.add(fourDollarButton);
        pricePanel.add(fiveDollarButton);

        ButtonGroup ratingButtonGroup = new ButtonGroup();
        JRadioButton oneStarButton = new JRadioButton("⭐");
        JRadioButton twoStarButton = new JRadioButton("⭐⭐");
        JRadioButton threeStarButton = new JRadioButton("⭐⭐⭐");
        JRadioButton fourStarButton = new JRadioButton("⭐⭐⭐⭐");
        JRadioButton fiveStarButton = new JRadioButton("⭐⭐⭐⭐⭐");

        ratingButtonGroup.add(oneStarButton);
        ratingButtonGroup.add(twoStarButton);
        ratingButtonGroup.add(threeStarButton);
        ratingButtonGroup.add(fourStarButton);
        ratingButtonGroup.add(fiveStarButton);

        oneStarButton.setSelected(true);

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        ratingPanel.add(oneStarButton);
        ratingPanel.add(twoStarButton);
        ratingPanel.add(threeStarButton);
        ratingPanel.add(fourStarButton);
        ratingPanel.add(fiveStarButton);

        JCheckBox visitedBox = new JCheckBox("Visited");
        JTextField dateField = new JTextField();
        JTextField notesField = new JTextField();

        if (existingRestaurant != null){
            nameField.setText(existingRestaurant.getName());
            cuisineField.setText(existingRestaurant.getCuisineType());
            locationField.setText(existingRestaurant.getLocation());

            if (existingRestaurant.getPriceLevel() == 1){
                oneDollarButton.setSelected(true);
            } else if (existingRestaurant.getPriceLevel() == 2){
                twoDollarButton.setSelected(true);
            } else if (existingRestaurant.getPriceLevel() == 3){
                threeDollarButton.setSelected(true);
            } else if (existingRestaurant.getPriceLevel() == 4){
                fourDollarButton.setSelected(true);
            } else if (existingRestaurant.getPriceLevel() == 5){
                fiveDollarButton.setSelected(true);
            }

            if (existingRestaurant.getUserRating() == 1){
                oneStarButton.setSelected(true);
            } else if (existingRestaurant.getUserRating() == 2){
                twoStarButton.setSelected(true);
            } else if (existingRestaurant.getUserRating() == 3){
                threeStarButton.setSelected(true);
            } else if (existingRestaurant.getUserRating() == 4){
                fourStarButton.setSelected(true);
            } else if (existingRestaurant.getUserRating() == 5){
                fiveStarButton.setSelected(true);
            }

            visitedBox.setSelected(existingRestaurant.getVisitedStatus());
            dateField.setText(existingRestaurant.getDateVisited());
            notesField.setText(existingRestaurant.getNotes());
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Restaurant Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Cuisine Type:"));
        panel.add(cuisineField);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);
        panel.add(new JLabel("Price Level:"));
        panel.add(pricePanel);
        panel.add(new JLabel("User Rating:"));
        panel.add(ratingPanel);
        panel.add(new JLabel("Visited:"));
        panel.add(visitedBox);
        panel.add(new JLabel("Date Visited MM/DD/YYYY:"));
        panel.add(dateField);
        panel.add(new JLabel("Notes:"));
        panel.add(notesField);

        boolean validInput = false;
        Restaurant restaurant = null;

        while (!validInput){
            int result = JOptionPane.showConfirmDialog(this, panel,
                    "Restaurant Information", JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION){
                return null;
            }

            String name = nameField.getText().trim();
            String cuisineType = cuisineField.getText().trim();
            String location = locationField.getText().trim();
            int priceLevel = getSelectedPriceLevel(oneDollarButton, twoDollarButton,
                    threeDollarButton, fourDollarButton, fiveDollarButton);
            double userRating = getSelectedRating(oneStarButton, twoStarButton,
                    threeStarButton, fourStarButton, fiveStarButton);
            boolean visitedStatus = visitedBox.isSelected();
            String dateVisited = dateField.getText().trim();
            String notes = notesField.getText().trim();

            if (name.isEmpty() || cuisineType.isEmpty() || location.isEmpty()){
                JOptionPane.showMessageDialog(this, "Restaurant name, cuisine type, and location cannot be blank.");
            } else if (visitedStatus){
                String dateError = validateDate(dateVisited);

                if (!dateError.isEmpty()){
                    JOptionPane.showMessageDialog(this, dateError);
                } else {
                    restaurant = new Restaurant(name, cuisineType, location, priceLevel,
                            userRating, true, dateVisited, notes);
                    validInput = true;
                }
            } else {
                restaurant = new Restaurant(name, cuisineType, location, priceLevel,
                        userRating, false, "N/A", notes);
                validInput = true;
            }
        }

        return restaurant;
    }

    // Gets the selected price level from the price buttons
    private int getSelectedPriceLevel(JRadioButton oneDollarButton, JRadioButton twoDollarButton,
                                      JRadioButton threeDollarButton, JRadioButton fourDollarButton,
                                      JRadioButton fiveDollarButton){
        if (oneDollarButton.isSelected()){
            return 1;
        } else if (twoDollarButton.isSelected()){
            return 2;
        } else if (threeDollarButton.isSelected()){
            return 3;
        } else if (fourDollarButton.isSelected()){
            return 4;
        } else if (fiveDollarButton.isSelected()){
            return 5;
        }

        return 1;
    }

    // Gets the selected rating from the rating buttons
    private double getSelectedRating(JRadioButton oneStarButton, JRadioButton twoStarButton,
                                     JRadioButton threeStarButton, JRadioButton fourStarButton,
                                     JRadioButton fiveStarButton){
        if (oneStarButton.isSelected()){
            return 1;
        } else if (twoStarButton.isSelected()){
            return 2;
        } else if (threeStarButton.isSelected()){
            return 3;
        } else if (fourStarButton.isSelected()){
            return 4;
        } else if (fiveStarButton.isSelected()){
            return 5;
        }

        return 1;
    }

    // Checks whether a date is valid and uses MM/DD/YYYY format
    private String validateDate(String date){
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")){
            return "Invalid date format. Please enter the date as MM/DD/YYYY.";
        }

        String[] dateParts = date.split("/");

        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        if (month < 1 || month > 12){
            return "Invalid month. Please enter a month from 01 to 12.";
        }

        if (year < 1900 || year > 3000){
            return "Invalid year. Please enter a year from 1900 to 3000.";
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        int maxDay = yearMonth.lengthOfMonth();

        if (day < 1 || day > maxDay){
            return "Invalid day. Please enter a valid day for that month.";
        }

        return "";
    }
}
