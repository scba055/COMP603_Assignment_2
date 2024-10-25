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
    
    public DatabaseController(Database db) {
        this.db = db;
    }
    
    // verifies whether the user already exists within the database
    public boolean login(String username, String password) {
        return db.login(username, password);
    }
    
    // creates new user
    public boolean signup(String username, String password) {
        return db.login(username, password);
    }
    
    // loads a player from database
    public Player loadPlayer(String username) {
        return db.loadPlayer(username);
    }
    
    // loads enemies into database
    public Map<String, Enemy> loadEnemies() {
        db.importEnemies("./resources/enemies.txt");
        return db.loadEnemies();
    }
}
