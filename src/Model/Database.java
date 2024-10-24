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
        createTables();
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
    
    private void createTables() {
        createPlayerTable();
        createInventoryTable();
    }
    
    // adds player stats into the database
    private void createPlayerTable() {
        try (Statement pst = conn.createStatement()){
            String createPlayerTable = 
                    "CREATE TABLE Player (" +
                    "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "username VARCHAR(50) UNIQUE NOT NULL," +
                    "password VARCHAR(50) NOT NULL," +
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
            System.out.println("Player table has been created or already exists."); // for debugging
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
            ist.executeUpdate(createInventoryTable);
        } catch (SQLException e) {
            System.out.println("Error creating inventory table: " + e.getMessage());
        }
    }
    
    // saves player data into the database
    public void savePlayer(Player player) {
        String insertPlayerSQL = 
                "MERGE INTO Player AS P" +
                "USING (VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)) AS V (" +
                "username, password, name, health, level, attack," +
                "defense, exp, gold, row, col" +
                ")" +
                "ON P.username = V.username" +
                "WHEN MATCHED THEN" +
                    "UPDATE SET" +
                        "password = V.password, name = V.name, health = V.health,"
                        + "level = V.level, attack = V.attack, defense = V.defense"
                        + "exp = V.exp, gold = V.gold, row = V.row, col = V.col" +
                "WHEN NOT MATCHED THEN"
                + "INSERT (username, password, name, health, level, attack"
                + "defense, exp, gold, row, col)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement newpst = conn.prepareStatement(insertPlayerSQL, 
                Statement.RETURN_GENERATED_KEYS)) {
            newpst.setString(1, player.getUsername());
            newpst.setString(2, PasswordUtils.hashPassword(player.getPassword()));
            newpst.setString(3, player.getName());
            newpst.setInt(4, player.getHealth());
            newpst.setInt(5, player.getLevel());
            newpst.setInt(6, player.getAttack());
            newpst.setInt(7, player.getDefense());
            newpst.setInt(8, player.getEXP());
            newpst.setInt(9, player.getGold());
            newpst.setInt(10, player.getRow());
            newpst.setInt(11, player.getCol());
            newpst.executeUpdate();
            
            
        } catch (SQLException e) {
        
        }
    }
}
