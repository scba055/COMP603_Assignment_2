package Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import Model.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author ywj5422
 */

// added old code but including username and password soon
public class SaveLoadController {
    // txt file that both save and load uses
    private static final String file = "./resources/player_save.txt";
    
    // variables that load uses
    private static Map<String, Integer> inventory = new HashMap<>();
    private static GameMap map = null;
    private static Player player = null;
    private static boolean saveExists = true;
    
    // ------------------------- Save Operations -----------------------------
    
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
    
    // ------------------------ Load Operations ------------------------------
    // simply calls the longer functions
    public static void loadGame() {
        if (!new File(file).exists()) {
            saveExists = false;
            System.out.println();
            return;
        }
        
        if (getSaveResponse() == true) {
            loadPlayer();
            loadMap();
        } else {
            // handled in GameLogic loginScreen() function
        }
    }
    
    // reads the information within the file player_save.txt
    // separates the information into 3 sections: player stats, inventory, map
    // loads the given information within the file
    private static Player loadPlayer() {
       
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String currentSection = "";
            
            // initialising variables
            String name = null;
            int health = 0, level = 0, attack = 0, defense = 0, exp = 0;
            int gold = 0, row = 0, col = 0;
            boolean playerLoaded = false;
            boolean mapLoaded = false;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // skips empty lines in the file
                // grabs the sections within the file
                if (line.equals("Player_Stats")) {
                    currentSection = "Player";
                    continue;
                } else if (line.equals("Player_Inventory")) {
                    currentSection = "Inventory";
                    continue;
                } else if (line.equals("Map")) {
                    currentSection = "Map";
                    break; // stops processing data when the reader finds the final section
                }
                // sections logic
                // player section
                switch (currentSection) {
                    case "Player":
                        String[] parts = line.split(": ");
                        // if there is no saved name, then a game has not been saved game
                        if (parts.length < 2) {
                            saveExists = false;
                            break;
                        }
                        
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        // debugging
                        //System.out.println("Parsed Key: " + key + ", value: " + value);
                        
                        switch (key) {
                            case "Name":
                                name = value;
                                break;
                            case "Health":
                                health = Integer.parseInt(value);
                                break;
                            case "Level":
                                level = Integer.parseInt(value);
                                break;
                            case "Attack":
                                attack = Integer.parseInt(value);
                                break;
                            case "Defense":
                                defense = Integer.parseInt(value);
                                break;
                            case "EXP":
                                exp = Integer.parseInt(value);
                                break;
                            case "Gold":
                                gold = Integer.parseInt(value);
                                break;
                            case "Position":
                                StringTokenizer posToken = new StringTokenizer(value, ",");
                                if (posToken.countTokens() == 2) {
                                    row = Integer.parseInt(posToken.nextToken().trim());
                                    col = Integer.parseInt(posToken.nextToken().trim());
                                } else {
                                    saveExists = false;
                                    break;
                                }
                                break;
                        }
                        player = new Player(name, health, level, attack, defense, row, col);
                        player.setEXP(exp);
                        player.setGold(gold);
                        playerLoaded = true;
                        break;
                    case "Inventory":
                        StringTokenizer invToken = new StringTokenizer(line, ",");
                        if (invToken.countTokens() == 2) {
                            String itemName = invToken.nextToken().trim();
                            try {
                                int itemQuantity = Integer.parseInt(invToken.nextToken().trim());
                                inventory.put(itemName, itemQuantity);
                            } catch (NumberFormatException e) {
                                System.out.println("Error: Invalid inventory quantity format for " + itemName);
                                saveExists = false;
                            }
                        } else {
                            saveExists = false;
                            System.out.println("Error: Invalid inventory data format.");
                        }
                        player.setInventory(inventory);
                        break;
                    case "Map":
                        StringTokenizer mapToken = new StringTokenizer(line, ", ");
                        if (mapToken.countTokens() == 2) {
                            int mapRows = Integer.parseInt(mapToken.nextToken().trim());
                            int mapCols = Integer.parseInt(mapToken.nextToken().trim());
                            char[][] mapLayout = new char[mapRows][mapCols];
                            
                            // goes through each row and column for the chars at each index
                            for (int i = 0; i < mapRows; i++) {
                                line = br.readLine().trim();
                                if (line != null) {
                                    line = line.trim();
                                    if (line.length() != mapCols) {
                                        saveExists = false;
                                        System.out.println("Error: Invalid map row length.");
                                        break;
                                    }
                                    for (int j = 0; j < mapCols; j++) {
                                    mapLayout[i][j] = line.charAt(j);
                                    }
                                } else {
                                    saveExists = false;
                                    System.out.println("Error: Insufficient map data.");
                                    break;
                                }
                            }
                            // loads the new map
                            map = new GameMap(mapRows, mapCols, mapLayout);
                            mapLoaded = true;
                        } else {
                            saveExists = false;
                            System.out.println("Error: Invalid map size format.");
                        }
                        break; 
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading file: " + file);
        }
        
        return player;
    }
    
    private static void loadMap() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String currentSection = "";
            
            // skips to the Map Section
            while ((line = br.readLine()) != null) {
                line = br.readLine();
                if (line.equals("Map")) {
                    currentSection = "Map";
                    break;
                }
            }
            
            if (currentSection.equals("Map")) {
                line = br.readLine().trim(); // Read the next line for map size
                StringTokenizer mapToken = new StringTokenizer(line, ", ");
                if (mapToken.countTokens() == 2) {
                    int mapRows = Integer.parseInt(mapToken.nextToken().trim());
                    int mapCols = Integer.parseInt(mapToken.nextToken().trim());
                    char[][] mapLayout = new char[mapRows][mapCols];

                    // Read the map layout
                    for (int i = 0; i < mapRows; i++) {
                        line = br.readLine();
                        if (line != null) {
                            line = line.trim();
                            if (line.length() != mapCols) {
                                saveExists = false;
                                System.out.println("Error: Invalid map row length.");
                                return;
                            }
                            for (int j = 0; j < mapCols; j++) {
                                mapLayout[i][j] = line.charAt(j);
                            }
                        } else {
                            saveExists = false;
                            System.out.println("Error: Insufficient map data.");
                            return;
                        }
                    }
                    map = new GameMap(mapRows, mapCols, mapLayout);
                } else {
                    saveExists = false;
                    System.out.println("Error: Invalid map size format.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading the map from " + file);
        }
    }
    
    // provide the loaded player object
    public static boolean getSaveResponse() { return saveExists; }
    public static GameMap getMap() { return map; }
    public static Player getPlayer() { return player; }
}
