package Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import Model.Enemy;

/**
 *
 * @author ywj5422
 */
public class GameController {
    public static boolean enemyEncounter = false;
    public static boolean storeEncounter = false;
    public static boolean treasureEncounter = false;
    public static boolean bossEncounter = false;
    
    private Map<String, Enemy> enemies = new HashMap<>();
    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Enemy> mapEnemies = new HashMap<>();
    private Random rand;
    private Model.GameMap map;
    private Model.Player player;
    private Controller.EncounterController ec;
    private Controller.PlayerController pc;
    private View.GameView gv;
    
    public GameController(EncounterController ec) {
        this.rand = new Random();
        this.enemies = Enemy.loadEnemies();
        this.ec = ec;
    }
    
    // places enemies onto the map based on the dedicated placements
    private void placeEnemiesOnMap() {
        List<Enemy> enemyList = new ArrayList<>(enemies.values());
        // checks for 'E' then saves a random enemy from enemies.txt and its coords
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[0].length; j++) {
                if (map.getCell(i,j) == 'E') {
                    Enemy enemy = enemyList.get(rand.nextInt(enemyList.size()));
                    mapEnemies.put(i + "," + j, enemy);
                }
            }
        }
    }
    
    public void interact(char cell) {
        char currentCell = cell;
        switch (currentCell) {
            case 'S':
                // store handling
                storeEncounter = true;
                displayEncounter();
                break;
            case 'E':
                // enemy handling
                enemyEncounter = true;
                displayEncounter();
                break;
            case 'T':
                // treasure handling
                treasureEncounter = true;
                displayEncounter();
                break;
            case 'B':
                // boss handling
                bossEncounter = true;
                displayEncounter();
                break;
        } 
    }
    
    // checks if the boolean is true then 
    public void displayEncounter() {
        if (enemyEncounter) {
            // gets the player's row,col and uses it as a key for an enemy within
            // mapEnemies that holds a specific enemy's info and coords
            String key = player.getRow() + "," + player.getCol();
            Enemy enemy = mapEnemies.get(key);
            ec.enemyEncounter(enemy);
            gv.toggleEnemyEncounterButtons(true);
            enemyEncounter = false;
        } else if (storeEncounter) {
            ec.storeEncounter();
            storeEncounter = false;
            gv.toggleStoreEncounterButtons(true);
        } else if (treasureEncounter) {
            ec.treasureEncounter();
            treasureEncounter = false;
        } else if (bossEncounter) {
            ec.bossEncounter();
            gv.toggleEnemyEncounterButtons(true);
            bossEncounter = false;
        }
    }
}
