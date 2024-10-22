package Model;

/**
 *
 * @author ywj5422
 */
import java.io.*;
import java.util.*;

// code from Project 1, will change in future if necessary
public class Enemy extends GameCharacter {
    private static final String file = "./resources/enemies.txt";
    private static Map<String, Enemy> enemies = new HashMap<>();
    
    public Enemy(String name, int health, int level, int attack, int defense) {
        // initialise w/o position
        super(name, health, level, attack, defense, -1, -1); 
    }
    
    // loads enemies from the enemies.txt file
    public static Map<String, Enemy> loadEnemies() {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitParts = line.split(", ");
                if (splitParts.length == 5) {
                    String name = splitParts[0];
                    int health = Integer.parseInt(splitParts[1]);
                    int level = Integer.parseInt(splitParts[2]);
                    int attack = Integer.parseInt(splitParts[3]);
                    int defense = Integer.parseInt(splitParts[4]);
                    enemies.put(name, new Enemy(name, health, level, attack, defense));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + file);
        }
        return enemies;
    }
    
    public Map<String, Enemy> getEnemyList() { return enemies; }
}
