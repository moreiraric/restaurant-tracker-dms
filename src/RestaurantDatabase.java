import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Loads and saves restaurant records using a text file or MySQL database.
 */
public class RestaurantDatabase {

    // File name or file path for the restaurant data file.
    private String fileName;
    private Connection connection;

    /**
     * Creates a database helper that loads restaurants from a text file.
     *
     * @param fileName the name or path of the restaurant data file
     */
    public RestaurantDatabase(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Creates a database helper that uses an active MySQL connection.
     *
     * @param connection the active MySQL connection
     */
    public RestaurantDatabase(Connection connection) {
        this.connection = connection;
    }

    /**
     * Updates the restaurant data file name or path.
     *
     * @param fileName the new file name or path
     * @return the updated file name or path
     */
    public String setFileName(String fileName) {
        this.fileName = fileName;
        return this.fileName;
    }

    /**
     * Returns the current restaurant data file name or path.
     *
     * @return the current file name or path
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Loads valid restaurant records from the selected text file.
     *
     * @return a list of restaurants loaded from the file, or an empty list if the file is missing
     */
    public ArrayList<Restaurant> loadRestaurants() {
        ArrayList<Restaurant> loadedRestaurants = new ArrayList<Restaurant>();

        try {
            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);

            // Read each line from the file and convert it into a Restaurant object.
            while (fileScanner.hasNextLine()) {
                String recordLine = fileScanner.nextLine();
                Restaurant restaurant = parseRestaurant(recordLine);

                if (restaurant != null) {
                    loadedRestaurants.add(restaurant);
                }
            }

            fileScanner.close();
        } catch (FileNotFoundException error) {
            System.out.println("/nFile not found. No restaurants were loaded.");
        }

        return loadedRestaurants;
    }

    /**
     * Loads all restaurant records from the MySQL database.
     *
     * @return a list containing all restaurant records in the database
     * @throws SQLException if the restaurant records cannot be loaded
     */
    public ArrayList<Restaurant> loadRestaurantsFromDatabase() throws SQLException {
        ArrayList<Restaurant> loadedRestaurants = new ArrayList<Restaurant>();
        String sql = "SELECT * FROM restaurants ORDER BY restaurant_id";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                String dateVisited = "N/A";

                if (results.getDate("date_visited") != null) {
                    dateVisited = results.getDate("date_visited").toLocalDate().format(dateFormat);
                }

                Restaurant restaurant = new Restaurant(
                        results.getInt("restaurant_id"),
                        results.getString("name"),
                        results.getString("cuisine_type"),
                        results.getString("location"),
                        results.getInt("price_level"),
                        results.getDouble("user_rating"),
                        results.getBoolean("visited_status"),
                        dateVisited,
                        results.getString("notes")
                );

                loadedRestaurants.add(restaurant);
            }
        }

        return loadedRestaurants;
    }

    /**
     * Adds one restaurant to the MySQL database and stores its generated ID.
     *
     * @param restaurant the restaurant to add
     * @return true if one record was added, or false otherwise
     * @throws SQLException if the restaurant cannot be added
     */
    public boolean addRestaurantToDatabase(Restaurant restaurant) throws SQLException {
        String sql = "INSERT INTO restaurants " +
                "(name, cuisine_type, location, price_level, user_rating, " +
                "visited_status, date_visited, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, restaurant.getName());
            statement.setString(2, restaurant.getCuisineType());
            statement.setString(3, restaurant.getLocation());
            statement.setInt(4, restaurant.getPriceLevel());
            statement.setDouble(5, restaurant.getUserRating());
            statement.setBoolean(6, restaurant.getVisitedStatus());

            if (restaurant.getVisitedStatus()) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate dateVisited =
                        LocalDate.parse(restaurant.getDateVisited(), dateFormat);
                statement.setDate(7, java.sql.Date.valueOf(dateVisited));
            } else {
                statement.setNull(7, Types.DATE);
            }

            statement.setString(8, restaurant.getNotes());

            int rowsAdded = statement.executeUpdate();

            if (rowsAdded == 1) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        restaurant.setRestaurantId(generatedKeys.getInt(1));
                    }
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Updates an existing restaurant record in the MySQL database.
     *
     * @param restaurant the restaurant containing the updated information
     * @return true if one record was updated, or false otherwise
     * @throws SQLException if the restaurant cannot be updated
     */
    public boolean updateRestaurantInDatabase(Restaurant restaurant) throws SQLException {
        String sql = "UPDATE restaurants SET name = ?, cuisine_type = ?, location = ?, " +
                "price_level = ?, user_rating = ?, visited_status = ?, " +
                "date_visited = ?, notes = ? WHERE restaurant_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, restaurant.getName());
            statement.setString(2, restaurant.getCuisineType());
            statement.setString(3, restaurant.getLocation());
            statement.setInt(4, restaurant.getPriceLevel());
            statement.setDouble(5, restaurant.getUserRating());
            statement.setBoolean(6, restaurant.getVisitedStatus());

            if (restaurant.getVisitedStatus()) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate dateVisited =
                        LocalDate.parse(restaurant.getDateVisited(), dateFormat);
                statement.setDate(7, java.sql.Date.valueOf(dateVisited));
            } else {
                statement.setNull(7, Types.DATE);
            }

            statement.setString(8, restaurant.getNotes());
            statement.setInt(9, restaurant.getRestaurantId());

            return statement.executeUpdate() == 1;
        }
    }

    /**
     * Deletes a restaurant record from the MySQL database.
     *
     * @param restaurantId the database ID of the restaurant to delete
     * @return true if one record was deleted, or false otherwise
     * @throws SQLException if the restaurant cannot be deleted
     */
    public boolean deleteRestaurantFromDatabase(int restaurantId) throws SQLException {
        String sql = "DELETE FROM restaurants WHERE restaurant_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurantId);
            return statement.executeUpdate() == 1;
        }
    }

    /**
     * Closes the active MySQL connection if it is open.
     *
     * @throws SQLException if the connection cannot be closed
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Converts a comma-separated line of text into a Restaurant object.
     *
     * @param recordLine the line containing the restaurant information
     * @return the parsed restaurant, or null if the line is invalid
     */
    public Restaurant parseRestaurant(String recordLine) {
        String[] fields = recordLine.split(",");

        // Each restaurant record should have 8 fields.
        if (fields.length != 8) {
            return null;
        }

        try {
            String name = fields[0].trim();
            String cuisineType = fields[1].trim();
            String location = fields[2].trim();
            int priceLevel = Integer.parseInt(fields[3].trim());
            double userRating = Double.parseDouble(fields[4].trim());
            boolean visitedStatus = Boolean.parseBoolean(fields[5].trim());
            String dateVisited = fields[6].trim();
            String notes = fields[7].trim();

            return new Restaurant(name, cuisineType, location, priceLevel,
                    userRating, visitedStatus, dateVisited, notes);
        } catch (NumberFormatException error) {
            return null;
        }
    }
}
