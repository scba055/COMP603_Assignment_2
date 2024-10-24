package Model;

/**
 *
 * @author ywj5422
 */

import java.sql.*;
import View.GameView;
        
public class Database {
    private Connection conn;
    private String url = "jdbc:derby:db;create=true";  // URL of the DB host
    private String dbusername = "pdc";  // your DB username
    private String dbpassword = "pdc";  // your DB password
    private String username;
    private String password;
    //private View.GameView gv; commenting for now
    
    public Database() {
        connect();
        createPlayerTable();
        createInventoryTable();
    }
    
    // ensures that the database is connected properly
    private boolean connect() {
        boolean isConned = false;
        try {
            conn = DriverManager.getConnection(url, dbusername, dbpassword);
            System.out.println("Database successfully connected.");
            isConned = true;
        } catch (SQLException e) {
            // prints error message for debugging
            System.out.println("Error connecting to database: " 
                    + e.getMessage());
        }
        return isConned;
    }
    
    // adds player stats into the database
    private void createPlayerTable() {
        try (Statement pst = conn.createStatement()){
            String createPlayerTable = 
                    "CREATE TABLE Player (" +
                    "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "name VARCHAR(50)," +
                    "health INT," +
                    "level INT," +
                    "attack INT," +
                    "defense INT," +
                    "exp INT," +
                    "gold INT," +
                    "row INT," +
                    "col INT" +
                    ")";
            pst.executeUpdate(createPlayerTable);
        } catch (SQLException e) {
            System.out.println("Error creating player table: " + e.getMessage());
        }
    }
    
    // adds the player inventory to the database
    private void createInventoryTable() {
        try (Statement ist = conn.createStatement()){
            String createInventoryTable = 
                    "CREATE TABLE Inventory (" +
                    "player_id INT," +
                    "item_name VARCHAR(50)," +
                    "quantity INT," +
                    "FOREIGN KEY (player_id) REFERENCES Player(id) DELETE ON CASCADE" +
                    ")";
        } catch (SQLException e) {
            System.out.println("Error creating inventory table: " + e.getMessage());
        }
    }
    
}
