// Restaurant class represents one restaurant record
public class Restaurant {
    // Private attributes store data for the restaurant
    private String name;
    private String cuisineType;
    private String location;
    private int priceLevel;
    private double userRating;
    private boolean visitedStatus;
    private String dateVisited;
    private String notes;

    // Constructor creates restaurant object with all required data
    public Restaurant(String name, String cuisineType, String location, int priceLevel, double userRating, boolean visitedStates, String dateVisited, String notes) {
        this.name = name;
        this.cuisineType = cuisineType;
        this.location = location;
        this.priceLevel = priceLevel;
        this.userRating = userRating;
        this.visitedStatus = visitedStates;
        this.dateVisited = dateVisited;
        this.notes = notes;
    }

    // Returns the restaurant name
    public String getName() {
        return name;
    }

    // Updates the restaurant name and returns the updated value
    public String setName(String name){
        this.name = name;
        return name;
    }

    // Returns the type of cuisine for the restaurant
    public String getCuisineType(){
        return cuisineType;
    }

    // Updates the cuisine type
    public String setCuisineType(String cuisineType){
        this.cuisineType = cuisineType;
        return cuisineType;
    }

    // Returns the location
    public String getLocation() {
        return location;
    }

    // Sets the location
    public String setLocation(String location){
        this.location = location;
        return location;
    }

    // Returns price level
    public int getPriceLevel() {
        return priceLevel;
    }

    // Sets the price level
    public String setPriceLevel(int priceLevel){
        this.priceLevel = priceLevel;
        return Integer.toString(priceLevel);
    }

    // Returns the user rating
    public double getUserRating() {
        return userRating;
    }

    // Updates the user rating
    public double setUserRating(double userRating) {
        this.userRating = userRating;
        return this.userRating;
    }

    // Returns visited status
    public boolean getVisitedStatus() {
        return visitedStatus;
    }

    // Updates visited status
    public boolean setVisitedStatus(boolean visitedStatus) {
        this.visitedStatus = visitedStatus;
        return this.visitedStatus;
    }

    // Returns date visited
    public String getDateVisited() {
        return dateVisited;
    }

    // Sets the date visited
    public String setDateVisited(String dateVisited) {
        this.dateVisited = dateVisited;
        return dateVisited;
    }

    // Returns the notes relating to the restaurant
    public String getNotes() {
        return notes;
    }

    // Sets the notes related to the restaurants
    public String setNotes(String notes) {
        this.notes = notes;
        return notes;
    }

    // Returns a structured string containing all restaurant data
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

