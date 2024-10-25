package Controller;

import Model.GameMap;
import Model.Player;
import Controller.DatabaseController;

/**
 *
 * @author ywj5422
 */

// added old code but including username and password soon
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
        boolean mapSaved = dbCon.saveMap(map);
        return playerSaved && mapSaved;
    }
    
    // ------------------------ Load Operations ------------------------------
    
    public Player loadPlayer(String username) {
        return dbCon.loadPlayer(username);
    }
    
    public GameMap loadMap() {
        return dbCon.loadMap();
    }
}
