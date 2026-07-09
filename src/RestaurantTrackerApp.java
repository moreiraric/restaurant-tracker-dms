
import java.util.ArrayList;
import java.util.Scanner;
import java.time.YearMonth;

// This class controls the menu and user input for the restaurant tracker dms
public class RestaurantTrackerApp {

    // Scanner reads input from the user.
    private Scanner scanner;

    // RestaurantManager handles the main restaurant logic
    private RestaurantManager manager;

    // Constructor creates the scanner and manager objects
    public RestaurantTrackerApp(){
        scanner = new Scanner(System.in);
        manager = new RestaurantManager();
    }

    // Starts the app and keeps the menu running until the user exits
    public boolean start(){
        boolean running = true;

        System.out.println("Welcome to Restaurant Tracker DMS!");

        while (running){
            int choice = displayMenu();

            if (choice == 1){
                handleAddRestaurant();
            } else if (choice == 2){
                handleViewRestaurant();
            } else if (choice == 3){
                handleUpdateRestaurant();
            } else if (choice == 4){
                handleDeleteRestaurant();
            } else if (choice == 5){
                handleGenerateReport();
            } else if (choice == 6){
                handleLoadFromFile();
            } else if (choice == 7){
                running = false;
                System.out.println("Exiting Restaurant Tracker DMS. Goodbye!");
            }
        }

        return true;
    }

    // Displays main menu and returns the user's choice
    public int displayMenu() {

        System.out.println("\nMain Menu");
        System.out.println("1. Add Restaurant");
        System.out.println("2. View All Restaurants");
        System.out.println("3. Update Restaurant");
        System.out.println("4. Delete Restaurant");
        System.out.println("5. Generate Summary Report");
        System.out.println("6. Load Restaurants from Text File");
        System.out.println("7. Exit");

        return getValidInt("Enter your choice: ", 1, 7);
    }

    // Handles adding new restaurants
    public boolean handleAddRestaurant(){
        Restaurant restaurant = collectRestaurantData();

        boolean added = manager.addRestaurant(restaurant);

        if (added){
            System.out.println("Restaurant added successfully!");
        } else {
            System.out.println("Restaurant could not be added!");
        }

        return added;
    }

    // Displays all saved restaurants
    public ArrayList<Restaurant> handleViewRestaurant(){
        ArrayList<Restaurant> restaurants = manager.getAllRestaurants();

        if (restaurants.isEmpty()){
            System.out.println("\nRestaurant list is empty!");
        } else {
            System.out.println("Restaurant list contains " + restaurants.size() + " restaurants!");

            for (int i = 0; i < restaurants.size(); i++) {
                System.out.println("\nRestaurant #" + (i + 1));
                System.out.println(restaurants.get(i));
            }
        }

        return restaurants;
    }

    // Handles updating an existing restaurant
    public boolean handleUpdateRestaurant(){
        ArrayList<Restaurant> restaurants = manager.getAllRestaurants();

        if (restaurants.isEmpty()){
            System.out.println("Restaurant list is empty!");
            return false;
        }

        handleViewRestaurant();

        int restaurantNumber = getValidInt("Enter the restaurant number to update: ", 1, restaurants.size());
        int index = restaurantNumber - 1;

        System.out.println("Enter the updated restaurant information.");
        Restaurant updatedRestaurant = collectRestaurantData();

        boolean updated = manager.updateRestaurant(index, updatedRestaurant);

        if (updated){
            System.out.println("Restaurant updated successfully!");
        } else {
            System.out.println("Restaurant could not be updated!");
        }

        return updated;
    }

    // Handles deleting an existing restaurant
    public boolean handleDeleteRestaurant(){
        ArrayList<Restaurant> restaurants = manager.getAllRestaurants();

        if (restaurants.isEmpty()){
            System.out.println("Restaurant list is empty!");
            return false;
        }

        handleViewRestaurant();

        int restaurantNumber = getValidInt("Enter the restaurant number to delete: ", 1, restaurants.size());
        int index = restaurantNumber - 1;

        boolean confirmed = getValidBoolean("Are you sure you want to delete this restaurant? Enter yes or no: ");

        if (!confirmed) {
            System.out.println("Restaurant deletion canceled!");
            return false;
        }

        boolean deleted = manager.deleteRestaurant(index);

        if (deleted){
            System.out.println("Restaurant deleted successfully!");
        } else {
            System.out.println("Restaurant could not be deleted!");
        }

        return deleted;
    }

    // Generates and displays the restaurant summary report
    public String handleGenerateReport(){
        String report = manager.generateSummaryReport();
        System.out.println("\n" + report);
        return report;
    }

    // Loads restaurant records from a text file
    public boolean handleLoadFromFile(){
        String fileName = getRequiredString("Enter the file name or file path: ");
        RestaurantDatabase database = new RestaurantDatabase(fileName);

        ArrayList<Restaurant> loadedRestaurants = database.loadRestaurants();

        if (loadedRestaurants.isEmpty()){
            System.out.println("No restaurants were loaded!");
            return false;
        }

        int loadedCount = 0;

        for (Restaurant restaurant : loadedRestaurants) {
            boolean added = manager.addRestaurant(restaurant);

            if (added) {
                loadedCount++;
            }
        }

        System.out.println(loadedCount + " restaurant records loaded successfully!");
        return loadedCount > 0;
    }

    // Collects restaurant information from the user
    private Restaurant collectRestaurantData(){
        String name = getRequiredString("Enter restaurant name: ");
        String cuisineType = getRequiredString("Enter cuisine type: ");
        String location = getRequiredString("Enter location: ");
        int priceLevel = getValidInt("Enter price level from 1 to 5 (1 = $1-10 // 5 = $100+): ", 1, 5);
        double userRating = getValidDouble("Enter user rating from 1 to 5: ", 1, 5);
        boolean visitedStatus = getValidBoolean("Have you visited this restaurant? Enter yes or no: ");

        String dateVisited;

        if (visitedStatus){
            dateVisited = getValidDate("Enter date visited in MM/DD/YYYY format: ");
        } else {
            dateVisited = "N/A";
        }

        System.out.print("Enter notes: ");
        String notes = scanner.nextLine();

        return new Restaurant(name, cuisineType, location, priceLevel,
                userRating, visitedStatus, dateVisited, notes);
    }

    // Gets text input and does not allow blank responses
    private String getRequiredString(String prompt){
        String input = "";

        while (input.trim().isEmpty()){
            System.out.print(prompt);
            input = scanner.nextLine();

            if (input.trim().isEmpty()){
                System.out.println("Input cannot be blank!");
            }
        }

        return input;
    }

    // Gets a whole number within a valid range
    private int getValidInt(String prompt, int min, int max){
        int value = min - 1;
        boolean validInput = false;

        while (!validInput){
            System.out.print(prompt);

            if (scanner.hasNextInt()){
                value = scanner.nextInt();
                scanner.nextLine();

                if (value >= min && value <= max){
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a whole number between 1 and 7.");
                scanner.nextLine();
            }
        }

        return value;
    }

    // Gets a decimal number within a valid range
    private double getValidDouble(String prompt, double min, double max){
        double value = min - 1;
        boolean validInput = false;

        while (!validInput){
            System.out.print(prompt);

            if (scanner.hasNextDouble()){
                value = scanner.nextDouble();
                scanner.nextLine();

                if (value >= min && value <= max){
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }

        return value;
    }

    // Gets a valid date in MM/DD/YYYY format
    private String getValidDate(String prompt){
        String input = "";
        boolean validInput = false;

        while (!validInput){
            System.out.print(prompt);
            input = scanner.nextLine().trim();

            if (!input.matches("\\d{2}/\\d{2}/\\d{4}")){
                System.out.println("Invalid date format. Please enter the date as MM/DD/YYYY.");
            } else {
                String[] dateParts = input.split("/");

                int month = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);

                if (month < 1 || month > 12){
                    System.out.println("Invalid month. Please enter a month from 01 to 12.");
                } else if (year < 1900 || year > 3000){
                    System.out.println("Invalid year. Please enter a year from 1900 to 3000.");
                } else {
                    YearMonth yearMonth = YearMonth.of(year, month);
                    int maxDay = yearMonth.lengthOfMonth();

                    if (day < 1 || day > maxDay){
                        System.out.println("Invalid day. Please enter a valid day for that month.");
                    } else {
                        validInput = true;
                    }
                }
            }
        }

        return input;
    }

    // Gets a yes/no or true/false response from the user
    private boolean getValidBoolean(String prompt){
        boolean value = false;
        boolean validInput = false;

        while (!validInput){
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("true")){
                value = true;
                validInput = true;
            } else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("false")){
                value = false;
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter yes/no or true/false.");
            }
        }

        return value;
    }
}