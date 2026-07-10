import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantDatabaseTest {

    // Tests that a valid text file can be loaded
    @Test
    public void testLoadValidFile(){
        RestaurantDatabase database = new RestaurantDatabase("restaurants.txt");

        ArrayList<Restaurant> restaurants = database.loadRestaurants();

        assertEquals(20, restaurants.size());
        assertEquals("Chipotle", restaurants.get(0).getName());
        assertEquals("Mexican", restaurants.get(0).getCuisineType());
        assertEquals("Orlando", restaurants.get(0).getLocation());
    }

    // Tests that a missing text file returns an empty list
    @Test
    public void testLoadMissingFile(){
        RestaurantDatabase database = new RestaurantDatabase("restaurants_404.txt");

        ArrayList<Restaurant> restaurants = database.loadRestaurants();

        assertEquals(0, restaurants.size());
    }
}