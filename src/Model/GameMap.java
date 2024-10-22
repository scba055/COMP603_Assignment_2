package Model;

/**
 *
 * @author cabal
 */
import java.util.*;

// from Project 1, will change things if needed
public class GameMap {
    private char[][] map;
    private char[][] tempMap;
    private Random rand = new Random();
    
    // constructor for the map
    public GameMap(int rows, int cols) {
        map = new char[rows][cols];
        int numEnemies = 5;
        int numStores = 3;
        int numTreasure = rand.nextInt(4) + 1;
        initMap(numEnemies, numStores, numTreasure);
    }
    
    // constructor for getting a map from a save file
    public GameMap(int rows, int cols, char[][] layout) {
       map = layout;
    }
    
    // initialises the map to have special locations and
    // default to '.' for grass or dirt
    private void initMap(int enemies, int stores, int treasures) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                setCell(i,j,'.'); // non-descript terrain
            }
        }
        // special locations for the player to explore
        setRandomLocation('S', stores); // Store location
        setRandomLocation('E', enemies); // Enemy location
        setRandomLocation('T', treasures); // Treasure location
        setRandomLocation('B'); // final boss
    }
    
    // allows for the randomisation of the Boss pointer
    private void setRandomLocation(char point) {
        int row, col;
        row = rand.nextInt(map.length);
        col = rand.nextInt(map[0].length);
        setCell(row, col, point);
    }
    
    // allows for the randomisation of store, enemy, and treasure encounters
    private void setRandomLocation(char point, int spawnType) {
        for (int i = 0; i < spawnType; i++) {
            setRandomLocation(point);
        }
    }
    
    // setters and getters
    public char getCell(int row, int col) { return map[row][col]; }
    public void setCell(int row, int col, char val) { map[row][col] = val; }
    public char[][] getMap() { return map; }
}
