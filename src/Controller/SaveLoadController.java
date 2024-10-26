package Controller;

import Model.GameMap;
import Model.Player;
import Controller.DatabaseController;
import Model.Enemy;
import java.util.Map;

/**
 *
 * @author ywj5422
 */

// smaller functions, but most of the saving is done through the Database
public class SaveLoadController {
    // variables that is needed for the functions
    private final DatabaseController dbCon;
    
    // connects this controller back to the database
    public SaveLoadController(DatabaseController dbCon) {
        this.dbCon = dbCon;
    }
    
    // ------------------------- Save Operations -----------------------------
    
    public boolean saveGame(Player player, GameMap map) {
        boolean playerSaved = dbCon.savePlayer(player);
        Map<String, Enemy> enemies = dbCon.loadEnemies();
        boolean mapSaved = dbCon.saveMap(map);
        return playerSaved && mapSaved;
    }
    
    // ------------------------ Load Operations ------------------------------
    
    public Player loadPlayer(String username) {
        return dbCon.loadPlayer(username);
    }
}
