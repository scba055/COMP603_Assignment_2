package Controller;

/**
 *
 * @author ywj5422
 */

import View.GameView;
import Model.*;
import Controller.*;

// handles the encounter logic that the player will have
public class EncounterController {
    private final Controller.PlayerController pc;
    private final View.GameView gv;
    private final Model.GameMap map;
    private final Model.Player user;
    
    public EncounterController(Controller.PlayerController controller, View.GameView view, Model.GameMap map, Model.Player user) {
        this.pc = controller;
        this.gv = view;
        this.map = map;
        this.user = user;
    }
    
    public void storeEncounter() {
    
    }
    
    public void enemyEncounter() {
        
    }
    
    public void bossEncounter() {
    
    }
    
    public void treasureEncounter() {
    
    }
}
