import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// RestaurantDatabase class handles loading restaurant records from a text file.
public class RestaurantDatabase {

    // File name or file path for the restaurant data file.
    private String fileName;

    // Constructor stores the file name or file path.
    public RestaurantDatabase(String fileName) {
        this.fileName = fileName;
    }

    // Updates the file name and returns the updated value.
    public String setFileName(String fileName) {
        this.fileName = fileName;
        return this.fileName;
    }

    // Returns the current file name or file path.
    public String getFileName() {
        return fileName;
    }

    // Loads restaurant records from the text file and returns them as an ArrayList.
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

    // Converts one line of text into a Restaurant object.
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
