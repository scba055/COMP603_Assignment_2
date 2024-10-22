package Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import Model.*;

/**
 *
 * @author ywj5422
 */

// added old code but including username and password soon
public class SaveLoadController {
    private static final String file = "./resources/player_save.txt";
    
    public static boolean saveGame(Player player, GameMap map) {
        boolean saveSuccessful = false;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            saveSuccessful = true;
            savePlayer(bw, player);
            saveInventory(bw, player.getInventory());
            saveMap(bw, map);
            System.out.println("Save Successful!");
        } catch (IOException e) {
            System.out.println("Failed to save. Due to: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Failed to save game: Null data encountered.");
        }
        
        return saveSuccessful;
    }
    
    // ensures that the section names are the same as the ones being 
    // used for the LoadGame class, to ensure that it is functioning correctly
    private static void savePlayer(BufferedWriter bw, Player player) throws IOException {
        if (player == null) {
            System.out.println("Error: Player data is null. Cannot commence save.");
            return;
        }
        
        bw.write("Player_Stats"); 
        bw.newLine();
        bw.write("Name: " + (player.getName() != null ? player.getName() : "Unknown"));
        bw.newLine();
        bw.write("Health: " + player.getHealth());
        bw.newLine();
        bw.write("Level: " + player.getLevel());
        bw.newLine();
        bw.write("Attack: " + player.getAttack());
        bw.newLine();
        bw.write("Defense: " + player.getDefense());
        bw.newLine();
        bw.write("Experience: " + player.getEXP());
        bw.newLine();
        bw.write("Gold: " + player.getGold());
        bw.newLine();
        bw.write("Position: " + player.getRow() + "," + player.getCol());
        bw.newLine();
        bw.newLine(); // adds an empty line
    }
    
    private static void saveInventory(BufferedWriter bw, Map<String, Integer> inventory) throws IOException {
        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Warning: Inventory is empty or null");
            return;
        }
        
        bw.write("Player_Inventory");
        bw.newLine();
        // for each loop goes through each item in the inventory hashmap and saves each one
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            bw.write(entry.getKey() + "," + entry.getValue());
            bw.newLine();
        }
        bw.newLine(); // adds an empty line
    }
    
    private static void saveMap(BufferedWriter bw, GameMap map) throws IOException {
        if (map == null || map.getMap() == null) {
            System.out.println("Error: Map data is null. Cannot commence save.");
            return;
        }
        
        bw.write("Map");
        bw.newLine();
        // gets map's dimensions
        bw.write(map.getMap().length + "," + map.getMap()[0].length);
        bw.newLine();
        
        // iterates through each row of the map
        for (char[] row : map.getMap()) {
            // iterates through each cell within the current row of the map
            for (char cell : row) {
                bw.write(cell);
            }
            bw.newLine();
        }
    }
}
