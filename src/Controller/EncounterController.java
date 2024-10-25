package Controller;

/**
 *
 * @author ywj5422
 */

import Model.*;

import java.util.Random;
import javax.swing.JButton;

// handles the encounter logic that the player will have
public class EncounterController {
    private final Controller.PlayerController pc;
    private final View.GameView gv;
    private final Model.GameMap map;
    private final Model.Player user;
    private final Random rand = new Random();
    
    private Enemy currentEnemy; // tracks the enemy within a singular encounter
    
    public EncounterController(Controller.PlayerController controller, 
            View.GameView view, Model.GameMap map, Model.Player user) {
        this.pc = controller;
        this.gv = view;
        this.map = map;
        this.user = user;
    }   
    
    public void storeEncounter() {
        gv.displayMessage("Welcome to Sarina's store!" +
                "\n1. 3xPotion Pots [25G]" +
                "\n2. Sword Upgrade [50G]" +
                "\n3. Shield Upgrade [50G]" +
                "\n4. Exit");
        gv.toggleStoreEncounterButtons(true);
    }
    
    public void handleOption1() {
    
    }
    
    public void handleOption2() {
    
    }
    
    public void handleOption3() {
    
    }
    
    public void handleOption4() {
    
    }
    
    public void enemyEncounter(Enemy enemy) {
        this.currentEnemy = enemy;
        gv.displayMessage("A random " + enemy.getName() + " has appeared!");
        updateEnemyStats();
        gv.toggleEnemyEncounterButtons(true);
    }
    
    private void updateEnemyStats() {
        if (currentEnemy != null) {
            gv.updateEnemyStats(currentEnemy.getName(), currentEnemy.getLevel(),
                    currentEnemy.getHealth());
        }
        
        if (!currentEnemy.isAlive()) {
            handleVictory();
        } else if (!user.isAlive()) {
            handleDefeat();
        }
    }
    
    public void handleAttack() {
        gv.displayMessage("You attacked!");
    }
    
    public void handleGuard() {
        gv.displayMessage("You guarded!");
    }
    
    public void handleHeal() {
        gv.displayMessage("You healed!");
    }
    
    public void handleRun() {
        gv.displayMessage("You ran away safely.");
        currentEnemy = null;
        gv.returnToGameMap();
    }
    
    private void handleVictory() {
        gv.displayMessage("You have slain an enemy.");
        
    }
    
    private void handleDefeat() {
        gv.displayMessage("You have been slain.");
    }
    
    public void bossEncounter() {
    
    }
    
    public void treasureEncounter() {
    
    }
    
    
}
