package Model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ywj5422
 */

public class DatabaseTest {
    private Database db;

    @Before
    public void setUp() {
        db = new Database(); // Ensure the Database object is initialized
        assertNotNull("Database connection should not be null", db); // Check that db is not null
    }

    @Test
    public void testLogin_Success() {
        boolean result = db.login("existingUser", "password");
        assertTrue("Login should succeed for valid credentials", result);
    }

    @Test
    public void testLogin_Failure() {
        boolean result = db.login("nonExistentUser", "password");
        assertFalse("Login should fail for invalid credentials", result);
    }

    @Test
    public void testNewPlayer() {
        Player player = db.newPlayer("newUser", "password", "Hero");
        assertNotNull("New player should be created", player);
        assertEquals("newUser", player.getUsername());
    }
}
