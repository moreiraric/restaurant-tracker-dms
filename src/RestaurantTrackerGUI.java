import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;

// GUI class for the Restaurant Tracker DMS
public class RestaurantTrackerGUI extends JFrame {

    private RestaurantManager manager;
    private JPanel cardPanel;

    public RestaurantTrackerGUI(){
        manager = new RestaurantManager();

        setTitle("Restaurant Tracker");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(230, 230, 230));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCardScrollPane(), BorderLayout.CENTER);

        refreshCards(manager.getAllRestaurants());

        setVisible(true);
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
        JButton editButton = createMenuButton("Edit");
        JButton deleteButton = createMenuButton("Delete");
        JButton loadButton = createMenuButton("Load File");
        JButton cardButton = createMenuButton("Card View");
        JButton listButton = createMenuButton("List View");
        JButton sortButton = createMenuButton("Sort by Rating");
        JButton recentButton = createMenuButton("Sort by Recently Visited");
        JButton costButton = createMenuButton("Sort by Cost");
        JButton reportButton = createMenuButton("Generate Report");

        toolBar.add(addButton);
        toolBar.add(loadButton);
        toolBar.addSeparator();
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(cardButton);
        toolBar.add(listButton);
        toolBar.add(sortButton);
        toolBar.add(recentButton);
        toolBar.add(costButton);
        toolBar.addSeparator();
        toolBar.add(reportButton);

        topPanel.add(toolBar, BorderLayout.CENTER);

        addButton.addActionListener(event -> showAddRestaurantDialog());
        editButton.addActionListener(event -> showEditRestaurantDialog());
        deleteButton.addActionListener(event -> showDeleteRestaurantDialog());
        loadButton.addActionListener(event -> showLoadFileDialog());
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
        cardPanel.setLayout(new GridLayout(0, 3, 25, 25));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    // Refreshes the restaurant cards on the screen
    private void refreshCards(ArrayList<Restaurant> restaurants){
        cardPanel.removeAll();
        cardPanel.setLayout(new GridLayout(0, 3, 25, 25));

        if (restaurants.isEmpty()){
            JLabel emptyLabel = new JLabel("No restaurants to show.");
            emptyLabel.setFont(new Font("Inter", Font.PLAIN, 18));
            cardPanel.add(emptyLabel);
        } else {
            for (Restaurant restaurant : restaurants){
                int restaurantNumber = getRestaurantNumberForManager(restaurant);
                cardPanel.add(createRestaurantCard(restaurant, restaurantNumber));
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
                int restaurantNumber = getRestaurantNumberForManager(restaurant);
                cardPanel.add(createRestaurantListItem(restaurant, restaurantNumber));
                cardPanel.add(Box.createVerticalStrut(10));
            }
        }

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    // Gets the restaurant number from the main restaurant list
    private int getRestaurantNumberForManager(Restaurant restaurant){
        ArrayList<Restaurant> allRestaurants = manager.getAllRestaurants();
        int index = allRestaurants.indexOf(restaurant);

        if (index == -1){
            return 0;
        }

        return index + 1;
    }

    // Creates one row for the list view
    private JPanel createRestaurantListItem(Restaurant restaurant, int restaurantNumber){
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(245, 245, 245));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel nameLabel = new JLabel(restaurantNumber + ". " + restaurant.getName());
        nameLabel.setFont(new Font("Inter", Font.BOLD, 18));

        JLabel detailsLabel = new JLabel(restaurant.getCuisineType() + " | "
                + getRatingStars(restaurant.getUserRating()) + " | "
                + getPriceSymbols(restaurant.getPriceLevel()) + " | "
                + restaurant.getLocation());
        detailsLabel.setFont(new Font("Inter", Font.PLAIN, 14));

        row.add(nameLabel, BorderLayout.WEST);
        row.add(detailsLabel, BorderLayout.EAST);

        return row;
    }

    // Creates one visual card for a restaurant
    private JPanel createRestaurantCard(Restaurant restaurant, int restaurantNumber){
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JLabel nameLabel = new JLabel(restaurantNumber + ". " + restaurant.getName());
        nameLabel.setFont(new Font("Inter", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(createCardLine("Cuisine type: ", restaurant.getCuisineType()));
        card.add(createCardLine("Rating: ", getRatingStars(restaurant.getUserRating())));
        card.add(createCardLine("Location: ", restaurant.getLocation()));
        card.add(createCardLine("Price Level: ", getPriceSymbols(restaurant.getPriceLevel())));
        card.add(createCardLine("Visited? ", restaurant.getVisitedStatus() ? "Yes" : "No"));
        card.add(createCardLine("Date Visited: ", restaurant.getDateVisited()));
        card.add(createCardLine("Notes: ", restaurant.getNotes()));

        return card;
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
            boolean added = manager.addRestaurant(restaurant);

            if (added){
                refreshCards(manager.getAllRestaurants());
                JOptionPane.showMessageDialog(this, "Restaurant added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Restaurant could not be added. Please check the input.");
            }
        }
    }

    // Shows dialog for editing a restaurant
    private void showEditRestaurantDialog(){
        ArrayList<Restaurant> restaurants = manager.getAllRestaurants();

        if (restaurants.isEmpty()){
            JOptionPane.showMessageDialog(this, "There are no restaurants to edit.");
            return;
        }

        int restaurantNumber = getRestaurantNumber("Enter the restaurant number to edit:");

        if (restaurantNumber == -1){
            return;
        }

        int index = restaurantNumber - 1;
        Restaurant currentRestaurant = restaurants.get(index);
        Restaurant updatedRestaurant = getRestaurantFromDialog(currentRestaurant);

        if (updatedRestaurant != null){
            boolean updated = manager.updateRestaurant(index, updatedRestaurant);

            if (updated){
                refreshCards(manager.getAllRestaurants());
                JOptionPane.showMessageDialog(this, "Restaurant updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Restaurant could not be updated.");
            }
        }
    }

    // Shows dialog for deleting a restaurant
    private void showDeleteRestaurantDialog(){
        ArrayList<Restaurant> restaurants = manager.getAllRestaurants();

        if (restaurants.isEmpty()){
            JOptionPane.showMessageDialog(this, "There are no restaurants to delete.");
            return;
        }

        int restaurantNumber = getRestaurantNumber("Enter the restaurant number to delete:");

        if (restaurantNumber == -1){
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete restaurant #" + restaurantNumber + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION){
            boolean deleted = manager.deleteRestaurant(restaurantNumber - 1);

            if (deleted){
                refreshCards(manager.getAllRestaurants());
                JOptionPane.showMessageDialog(this, "Restaurant deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Restaurant could not be deleted.");
            }
        }
    }

    // Gets restaurant number from the user
    private int getRestaurantNumber(String message){
        String input = JOptionPane.showInputDialog(this, message);

        if (input == null){
            return -1;
        }

        try {
            int restaurantNumber = Integer.parseInt(input);
            int size = manager.getAllRestaurants().size();

            if (restaurantNumber < 1 || restaurantNumber > size){
                JOptionPane.showMessageDialog(this, "Please enter a number from 1 to " + size + ".");
                return -1;
            }

            return restaurantNumber;
        } catch (NumberFormatException error){
            JOptionPane.showMessageDialog(this, "Please enter a valid whole number.");
            return -1;
        }
    }

    // Shows dialog for loading restaurants from a text file
    private void showLoadFileDialog(){
        String fileName = JOptionPane.showInputDialog(this, "Enter the file name or file path:");

        if (fileName == null || fileName.trim().isEmpty()){
            return;
        }

        RestaurantDatabase database = new RestaurantDatabase(fileName.trim());
        ArrayList<Restaurant> loadedRestaurants = database.loadRestaurants();

        int loadedCount = 0;

        for (Restaurant restaurant : loadedRestaurants){
            boolean added = manager.addRestaurant(restaurant);

            if (added){
                loadedCount++;
            }
        }

        refreshCards(manager.getAllRestaurants());
        JOptionPane.showMessageDialog(this, loadedCount + " restaurant records loaded successfully!");
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
        JOptionPane.showMessageDialog(this, manager.generateSummaryReport(),
                "Restaurant Summary Report", JOptionPane.INFORMATION_MESSAGE);
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