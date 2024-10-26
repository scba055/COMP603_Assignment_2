package Controller;

/**
 *
 * @author cabal
 */

import Model.*;
import java.util.Map;

public class DatabaseController extends Model.Database {
    private final Database db;
    private final String enemyFile = "./resources/enemies.txt";
    
    public DatabaseController() {
        this.db = Database.getInstance();
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
        return db.loadMap();
    }
    
    // saves the map
    public boolean saveMap(GameMap map) {
        return db.saveMap(map);
    }
    
    public boolean savePlayer(Player player) {
        return db.savePlayer(player);
    }
}
