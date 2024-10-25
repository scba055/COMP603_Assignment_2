package Controller;

/**
 *
 * @author ywj5422
 */

import Model.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// handles the encounter logic that the player will have
public class EncounterController {
    private final Controller.PlayerController pc;
    private final View.GameView gv;
    private final Model.GameMap map;
    private final Model.Player player;
    private final Map<String, Integer> inventory;
    private final Random rand = new Random();
    
    private Enemy currentEnemy; // tracks the enemy within a singular encounter
    
    public EncounterController(Controller.PlayerController controller, 
            View.GameView view, Model.GameMap map, Model.Player user) {
        this.pc = controller;
        this.gv = view;
        this.map = map;
        this.player = user;
        this.inventory = new HashMap<>();
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
        } else if (!player.isAlive()) {
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
        // modified old code from project 1 to connect to GameView
        // player finds treasure which is randomised, adds found gold,
        // changes the player's sword and attack level if Excalibur was found
        // increases health potion quantity if health potions were found
        gv.displayMessage("You have found some treasure!");
        int possibleOptions = rand.nextInt(4) + 1;
        switch (possibleOptions) {
            case 1: 
                gv.displayMessage("You found 25 Gold!");
                player.addGold(25);
                break;
            case 2:
                gv.displayMessage("You found 2 Health Potions");
                String key = "Health Potion";
                int foundQuantity = 2;
                if (inventory.containsKey(key)) {
                    int currentQuantity = inventory.get(key);
                    inventory.put(key, currentQuantity + foundQuantity);
                } else {
                    inventory.put(key, foundQuantity);
                }
                break;
            case 3:
                gv.displayMessage("You have found 100 Gold!");
                player.addGold(100);
                break;
            case 4:
                gv.displayMessage("You have found the Legendary Excalibur!" +
                        "Your current sword disintegrates due to Excalibur's magnificence!");
                if (inventory.containsKey("Rusty Sword")) {
                    inventory.remove("Rusty Sword");
                } else if (inventory.containsKey("Silver Sword")) {
                    inventory.remove("Silver Sword");
                } else if (inventory.containsKey("Gold Sword")) {
                    inventory.remove("Gold Sword");
                }
                inventory.put("Excalibur", 1);
                player.setAttack(100);
                break;
        }
    }
    
    
}
