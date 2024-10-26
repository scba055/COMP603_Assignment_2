package Controller;

/**
 *
 * @author cabal
 */

import Model.*;
import java.util.Map;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseController extends Model.Database {
    private final Database db;
    private final String enemyFile = "./resources/enemies.txt";
    
    public DatabaseController() {
        this.db = Database.getInstance();
    }
    
    // retrieves the active connection
    public Connection getConnection() {
        Connection conn = db.getConnection();
        if (conn == null) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE,
                    "Database connection is null.");
        }
        return conn;
    }
    
    // closes the database connection when done
    public void closeDatabase() {
        db.closeConnection();
    }
    
    // verifies whether the user already exists within the database
    public boolean login(String username, String password) {
        return db.login(username, password);
    }
    
    // creates new user
    public Player signup(String username, String password, String charName) {
        return db.newPlayer(username, password, charName);
    }
    
    // loads a player from database
    public Player loadPlayer(String username) {
        return db.loadPlayer(username);
    }
    
    // loads enemies into database
    public Map<String, Enemy> loadEnemies() {
        db.importEnemies(enemyFile);
        return db.loadEnemies();
    }
    
    // loads the map
    public GameMap loadMap() {
        return db.checkAndLoadMap();
    }
    
    // saves the map
    public boolean saveMap(GameMap map) {
        return db.saveMap(map);
    }
    
    public boolean savePlayer(Player player) {
        return db.savePlayer(player);
    }
}
