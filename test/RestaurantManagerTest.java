import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantManagerTest {

    // Tests that a valid restaurant can be added
    @Test
    public void testAddValidRestaurant(){
        RestaurantManager manager = new RestaurantManager();

        Restaurant restaurant = new Restaurant("Chipotle", "Mexican", "Orlando",
                2, 4.5, true, "07/05/2026", "Good quick lunch");

        boolean result = manager.addRestaurant(restaurant);

        assertTrue(result);
        assertEquals(1, manager.getAllRestaurants().size());
    }

    // Tests that an invalid restaurant is rejected
    @Test
    public void testAddInvalidRestaurant(){
        RestaurantManager manager = new RestaurantManager();

        Restaurant restaurant = new Restaurant("", "Mexican", "Orlando",
                2, 4.5, true, "07/05/2026", "Good quick lunch");

        boolean result = manager.addRestaurant(restaurant);

        assertFalse(result);
        assertEquals(0, manager.getAllRestaurants().size());
    }

    // Tests whether restaurants can be deleted
    @Test
    public void testDeleteValidRestaurant(){
        RestaurantManager manager = new RestaurantManager();

        Restaurant restaurant = new Restaurant("Chipotle", "Mexican", "Orlando",
                2, 4.5, true, "07/05/2026", "Good quick lunch");

        manager.addRestaurant(restaurant);

        boolean result = manager.deleteRestaurant(0);

        assertTrue(result);
        assertEquals(0, manager.getAllRestaurants().size());
    }

    // Tests that an invalid delete index is rejected
    @Test
    public void testDeleteInvalidIndex(){
        RestaurantManager manager = new RestaurantManager();

        Restaurant restaurant = new Restaurant("Chipotle", "Mexican", "Orlando",
                2, 4.5, true, "07/05/2026", "Good quick lunch");

        manager.addRestaurant(restaurant);

        boolean result = manager.deleteRestaurant(5);

        assertFalse(result);
        assertEquals(1, manager.getAllRestaurants().size());
    }

    // Tests whether a restaurant can be updated
    @Test
    public void testUpdateValidRestaurant(){
        RestaurantManager manager = new RestaurantManager();

        Restaurant originalRestaurant = new Restaurant("Chipotle", "Mexican", "Orlando",
                2, 4.5, true, "07/05/2026", "Good quick lunch");

        Restaurant updatedRestaurant = new Restaurant("Panda Express", "Chinese", "Winter Park",
                2, 4.0, true, "06/28/2026", "Fast and reliable");

        manager.addRestaurant(originalRestaurant);

        boolean result = manager.updateRestaurant(0, updatedRestaurant);

        assertTrue(result);
        assertEquals("Panda Express", manager.getAllRestaurants().get(0).getName());
        assertEquals("Chinese", manager.getAllRestaurants().get(0).getCuisineType());
    }


    // Tests that an invalid index is rejected
    @Test
    public void testUpdateInvalidIndex(){
        RestaurantManager manager = new RestaurantManager();

        Restaurant originalRestaurant = new Restaurant("Chipotle", "Mexican", "Orlando",
                2, 4.5, true, "07/05/2026", "Good quick lunch");

        Restaurant updatedRestaurant = new Restaurant("Panda Express", "Chinese", "Winter Park",
                2, 4.0, true, "06/28/2026", "Fast and reliable");

        manager.addRestaurant(originalRestaurant);

        boolean result = manager.updateRestaurant(3, updatedRestaurant);

        assertFalse(result);
        assertEquals("Chipotle", manager.getAllRestaurants().get(0).getName());
    }

    // Tests whether summary reports are correctly generated
    @Test
    public void testGenerateSummaryReport(){
        RestaurantManager manager = new RestaurantManager();

        manager.addRestaurant(new Restaurant("Chipotle", "Mexican", "Orlando",
                2, 4.5, true, "07/05/2026", "Good quick lunch"));

        manager.addRestaurant(new Restaurant("Olive Garden", "Italian", "Clermont",
                3, 3.5, true, "07/04/2026", "Decent pasta"));

        manager.addRestaurant(new Restaurant("Taco Bell", "Mexican", "Orlando",
                1, 4.0, false, "N/A", "Cheap option"));

        String report = manager.generateSummaryReport();

        assertTrue(report.contains("Total Restaurants: 3"));
        assertTrue(report.contains("Average Rating: 4.00"));
        assertTrue(report.contains("Most Common Cuisine: Mexican"));
        assertTrue(report.contains("Highest Rated Restaurant: Chipotle"));
        assertTrue(report.contains("Best Budget Restaurant: Chipotle"));
    }

    // Tests whether the application tells the user that they need to add restaurants before generating a report
    @Test
    public void testGenerateSummaryReportWithEmptyList(){
        RestaurantManager manager = new RestaurantManager();

        String report = manager.generateSummaryReport();

        assertEquals("No restaurants available. Add restaurants before generating a report.", report);
    }
}