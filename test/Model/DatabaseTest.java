package Model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import Model.Database;
import Model.Player;
import Controller.DatabaseController;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ywj5422
 */

public class DatabaseTest {
    private Database db;
    private DatabaseController dbCon;
    private Connection conn;

    @Before
    public void setUp() {
        db = Database.getInstance(); // Ensure the Database object is initialized
        dbCon = new DatabaseController();
        conn = db.getConnection();
        assertNotNull("Database connection should not be null", db); // Check that db is not null
    }
    
    // deletes old test data to avoid conflicts upon testing
//    @After
//    public void tearDown() throws SQLException {
//        Statement stmt = db.getConnection().createStatement();
//        stmt.executeUpdate("DELETE FROM Player WHERE username = 'Shaina'");
//        stmt.close();
//    }
    
    // verifies that the database connection is successful
    @Test
    public void testDatabaseConnection_Success() {
        boolean connected = db.connect();
        assertTrue("Database connection should be successful", connected);
    }
    
    // ensures singleton pattern is working
    @Test
    public void testSingletonInstance() {
        Database anotherDb = Database.getInstance();  // Retrieve another instance
        assertSame("Both instances should point to the same object", db, anotherDb);
    }
    
    @Test
    public void testTablesCreated() {
        assertTrue("Player table should exist", tableExists("Player"));
        assertTrue("Inventory table should exist", tableExists("Inventory"));
        assertTrue("Enemies table should exist", tableExists("Enemies"));
    }
    
    // helper function for the test above    
    private boolean tableExists(String tableName) {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, 
                tableName.toUpperCase(), null)) {
            return rs.next();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, 
                    "Error checking table's existence.", e);
            return false;
        }
    }

    @Test
    public void testLogin_Success() {
        boolean result = dbCon.login("Shaina", "Shaina");
        assertTrue("Login should succeed for valid credentials", result);
    }

    @Test
    public void testLogin_Failure() {
        boolean result = dbCon.login("nonExistentUser", "password");
        assertFalse("Login should fail for invalid credentials", result);
    }

    @Test
    public void testNewPlayer() {
        Player player = dbCon.newPlayer("newUser", "password", "Hero");
        assertNotNull("New player should be created", player);
        assertEquals("newUser", player.getUsername());
    }
}
