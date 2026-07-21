/**
 * Stores the information for one restaurant record in the tracker.
 */
public class Restaurant {
    // Private attributes store data for the restaurant
    private int restaurantId;
    private String name;
    private String cuisineType;
    private String location;
    private int priceLevel;
    private double userRating;
    private boolean visitedStatus;
    private String dateVisited;
    private String notes;

    /**
     * Creates a restaurant that has not yet received a database ID.
     *
     * @param name the restaurant name
     * @param cuisineType the type of cuisine served
     * @param location the restaurant location
     * @param priceLevel the price level from 1 to 5
     * @param userRating the user rating from 1 to 5
     * @param visitedStates whether the restaurant has been visited
     * @param dateVisited the date visited in MM/DD/YYYY format, or N/A if not visited
     * @param notes additional notes about the restaurant
     */
    public Restaurant(String name, String cuisineType, String location, int priceLevel, double userRating, boolean visitedStates, String dateVisited, String notes) {
        this(0, name, cuisineType, location, priceLevel, userRating,
                visitedStates, dateVisited, notes);
    }

    /**
     * Creates a restaurant with an existing database ID.
     *
     * @param restaurantId the restaurant's database ID
     * @param name the restaurant name
     * @param cuisineType the type of cuisine served
     * @param location the restaurant location
     * @param priceLevel the price level from 1 to 5
     * @param userRating the user rating from 1 to 5
     * @param visitedStates whether the restaurant has been visited
     * @param dateVisited the date visited in MM/DD/YYYY format, or N/A if not visited
     * @param notes additional notes about the restaurant
     */
    public Restaurant(int restaurantId, String name, String cuisineType, String location,
                      int priceLevel, double userRating, boolean visitedStates,
                      String dateVisited, String notes) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.cuisineType = cuisineType;
        this.location = location;
        this.priceLevel = priceLevel;
        this.userRating = userRating;
        this.visitedStatus = visitedStates;
        this.dateVisited = dateVisited;
        this.notes = notes;
    }

    /**
     * Returns the restaurant's database ID.
     *
     * @return the database ID
     */
    public int getRestaurantId() {
        return restaurantId;
    }

    /**
     * Updates the restaurant's database ID.
     *
     * @param restaurantId the new database ID
     */
    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    /**
     * Returns the restaurant name.
     *
     * @return the restaurant name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the restaurant name.
     *
     * @param name the new restaurant name
     * @return the updated restaurant name
     */
    public String setName(String name){
        this.name = name;
        return name;
    }

    /**
     * Returns the type of cuisine served by the restaurant.
     *
     * @return the cuisine type
     */
    public String getCuisineType(){
        return cuisineType;
    }

    /**
     * Updates the restaurant's cuisine type.
     *
     * @param cuisineType the new cuisine type
     * @return the updated cuisine type
     */
    public String setCuisineType(String cuisineType){
        this.cuisineType = cuisineType;
        return cuisineType;
    }

    /**
     * Returns the restaurant location.
     *
     * @return the restaurant location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Updates the restaurant location.
     *
     * @param location the new restaurant location
     * @return the updated location
     */
    public String setLocation(String location){
        this.location = location;
        return location;
    }

    /**
     * Returns the restaurant's price level.
     *
     * @return the price level from 1 to 5
     */
    public int getPriceLevel() {
        return priceLevel;
    }

    /**
     * Updates the restaurant's price level.
     *
     * @param priceLevel the new price level from 1 to 5
     * @return the updated price level as a String
     */
    public String setPriceLevel(int priceLevel){
        this.priceLevel = priceLevel;
        return Integer.toString(priceLevel);
    }

    /**
     * Returns the rating given to the restaurant.
     *
     * @return the user rating from 1 to 5
     */
    public double getUserRating() {
        return userRating;
    }

    /**
     * Updates the restaurant's user rating.
     *
     * @param userRating the new user rating from 1 to 5
     * @return the updated user rating
     */
    public double setUserRating(double userRating) {
        this.userRating = userRating;
        return this.userRating;
    }

    /**
     * Returns whether the restaurant has been visited.
     *
     * @return true if the restaurant has been visited, or false otherwise
     */
    public boolean getVisitedStatus() {
        return visitedStatus;
    }

    /**
     * Updates whether the restaurant has been visited.
     *
     * @param visitedStatus the new visited status
     * @return the updated visited status
     */
    public boolean setVisitedStatus(boolean visitedStatus) {
        this.visitedStatus = visitedStatus;
        return this.visitedStatus;
    }

    /**
     * Returns the date the restaurant was visited.
     *
     * @return the date visited, or N/A if the restaurant has not been visited
     */
    public String getDateVisited() {
        return dateVisited;
    }

    /**
     * Updates the date the restaurant was visited.
     *
     * @param dateVisited the new visit date in MM/DD/YYYY format
     * @return the updated visit date
     */
    public String setDateVisited(String dateVisited) {
        this.dateVisited = dateVisited;
        return dateVisited;
    }

    /**
     * Returns the notes about the restaurant.
     *
     * @return the restaurant notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Updates the notes about the restaurant.
     *
     * @param notes the new restaurant notes
     * @return the updated notes
     */
    public String setNotes(String notes) {
        this.notes = notes;
        return notes;
    }

    /**
     * Returns the restaurant information as a formatted String.
     *
     * @return a String containing the restaurant's information
     */
    @Override
    public String toString() {
        return "Restaurant Name: " + name +
                "\nCuisine Type: " + cuisineType +
                "\nLocation: " + location +
                "\nPrice Level: " + priceLevel +
                "\nUser Rating: " + userRating +
                "\nVisited: " + visitedStatus +
                "\nDate Visited: " + dateVisited +
                "\nNotes: " + notes;
    }
}
