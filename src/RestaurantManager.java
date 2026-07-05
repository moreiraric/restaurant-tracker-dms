import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// RestaurantManager class handles the main logic for the Restaurant Tracker DMS.
public class RestaurantManager {

    // ArrayList stores all restaurant records while the program is running.
    private ArrayList<Restaurant> restaurants;

    // Constructor initializes an empty list of restaurants.
    public RestaurantManager() {
        restaurants = new ArrayList<Restaurant>();
    }

    // Checks whether a Restaurant object contains valid data before it is added or updated.
    public boolean isValidRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            return false;
        }

        if (restaurant.getName() == null || restaurant.getName().trim().isEmpty()) {
            return false;
        }

        if (restaurant.getCuisineType() == null || restaurant.getCuisineType().trim().isEmpty()) {
            return false;
        }

        if (restaurant.getLocation() == null || restaurant.getLocation().trim().isEmpty()) {
            return false;
        }

        if (restaurant.getPriceLevel() < 1 || restaurant.getPriceLevel() > 5) {
            return false;
        }

        if (restaurant.getUserRating() < 1 || restaurant.getUserRating() > 5) {
            return false;
        }

        if (restaurant.getVisitedStatus() &&
                (restaurant.getDateVisited() == null || restaurant.getDateVisited().trim().isEmpty())) {
            return false;
        }

        return true;
    }

    // Adds a restaurant to the list if the restaurant object is valid.
    public boolean addRestaurant(Restaurant restaurant) {
        if (!isValidRestaurant(restaurant)) {
            return false;
        }

        restaurants.add(restaurant);
        return true;
    }

    // Returns the full list of restaurants.
    public ArrayList<Restaurant> getAllRestaurants() {
        return restaurants;
    }

    // Replaces an existing restaurant record with an updated restaurant object.
    public boolean updateRestaurant(int index, Restaurant updatedRestaurant) {
        if (index < 0 || index >= restaurants.size() || !isValidRestaurant(updatedRestaurant)) {
            return false;
        }

        restaurants.set(index, updatedRestaurant);
        return true;
    }

    // Deletes a restaurant record based on its index in the list.
    public boolean deleteRestaurant(int index) {
        if (index < 0 || index >= restaurants.size()) {
            return false;
        }

        restaurants.remove(index);
        return true;
    }

    // Finds and returns the restaurant with the highest user rating.
    public Restaurant getHighestRatedRestaurant() {
        if (restaurants.isEmpty()) {
            return null;
        }

        Restaurant highestRated = restaurants.get(0);

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getUserRating() > highestRated.getUserRating()) {
                highestRated = restaurant;
            }
        }

        return highestRated;
    }

    // Finds and returns the cuisine type that appears most often in the list.
    public String getMostCommonCuisine() {
        if (restaurants.isEmpty()) {
            return "No restaurants available.";
        }

        HashMap<String, Integer> cuisineCounts = new HashMap<String, Integer>();

        // Count how many times each cuisine type appears.
        for (Restaurant restaurant : restaurants) {
            String cuisine = restaurant.getCuisineType();

            if (cuisineCounts.containsKey(cuisine)) {
                cuisineCounts.put(cuisine, cuisineCounts.get(cuisine) + 1);
            } else {
                cuisineCounts.put(cuisine, 1);
            }
        }

        String mostCommonCuisine = "";
        int highestCount = 0;

        // Find the cuisine type with the highest count.
        for (Map.Entry<String, Integer> entry : cuisineCounts.entrySet()) {
            if (entry.getValue() > highestCount) {
                mostCommonCuisine = entry.getKey();
                highestCount = entry.getValue();
            }
        }

        return mostCommonCuisine;
    }

    // Finds the best budget restaurant based on price level and rating.
    public Restaurant getBestBudgetRestaurant() {
        if (restaurants.isEmpty()) {
            return null;
        }

        Restaurant bestBudgetRestaurant = null;

        // Budget restaurants are defined as price level 1 or 2.
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getPriceLevel() <= 2) {
                if (bestBudgetRestaurant == null ||
                        restaurant.getUserRating() > bestBudgetRestaurant.getUserRating()) {
                    bestBudgetRestaurant = restaurant;
                }
            }
        }

        return bestBudgetRestaurant;
    }

    // Generates a summary report using calculations from the restaurant list.
    public String generateSummaryReport() {
        if (restaurants.isEmpty()) {
            return "No restaurants available. Add restaurants before generating a report.";
        }

        double totalRating = 0;

        // Add all ratings together so the average rating can be calculated.
        for (Restaurant restaurant : restaurants) {
            totalRating += restaurant.getUserRating();
        }

        double averageRating = totalRating / restaurants.size();
        Restaurant highestRated = getHighestRatedRestaurant();
        Restaurant bestBudget = getBestBudgetRestaurant();
        String mostCommonCuisine = getMostCommonCuisine();

        // Build the report as a formatted String.
        String report = "Restaurant Summary Report";
        report += "\n-------------------------";
        report += "\nTotal Restaurants: " + restaurants.size();
        report += "\nAverage Rating: " + String.format("%.2f", averageRating);
        report += "\nMost Common Cuisine: " + mostCommonCuisine;

        if (highestRated != null) {
            report += "\nHighest Rated Restaurant: " + highestRated.getName() +
                    " (" + highestRated.getUserRating() + ")";
        }

        if (bestBudget != null) {
            report += "\nBest Budget Restaurant: " + bestBudget.getName() +
                    " (" + bestBudget.getUserRating() + ")";
        } else {
            report += "\nBest Budget Restaurant: No budget restaurants available.";
        }

        return report;
    }
}