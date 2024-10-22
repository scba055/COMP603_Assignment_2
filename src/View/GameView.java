package View;

/**
 *
 * @author ywj5422
 */

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

// includes old code and code that would create the UI for the game
public class GameView extends JFrame {
    private final Player player;
    private final GameMap map;
    private final Map<String, Enemy> enemies;
    private final JTextArea mapDisplay;
    //private final JTextArea playerStats;
    
    public GameView(Player player, GameMap map, Map<String, Enemy> enemies) {
        this.player = player;
        this.map = map;
        this.enemies = enemies;
        
        // setting up the UI
        setTitle("ywj5422's world");
        setSize(600, 400); // will change these dimensions as I go
        setLayout(new BorderLayout());
        
        mapDisplay = new JTextArea(10,30);
        mapDisplay.setEditable(false);
        add(new JScrollPane(mapDisplay), BorderLayout.CENTER);
        
        // adding movement buttons for user
    }
    
    // displays the entire game state: map and player stats
    public void displayGame(GameMap map, Player player, Map<String, Enemy> enemies) {
        displayMap(map, player, enemies);
        displayPlayerStats(player);
    }
    
    // displays the map, showing 'P' as player's position on map
    private void displayMap(GameMap map, Player player, Map<String, Enemy> enemies) {
        StringBuilder mapDisplay = new StringBuilder();
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[0].length; j++) {
               if (i == player.getRow() && j == player.getCol()) {
                   // output player's position in the map
                   mapDisplay.append('P' + " "); // output player's pos
               } else if (map.getCell(i,j) == 'S') {
                   mapDisplay.append('S' + " "); // store's pos
               } else if (map.getCell(i,j) == 'E') {
                   mapDisplay.append('E' + " "); // enemy pos
               } else if (map.getCell(i, j) == 'T') {
                   mapDisplay.append('T' + " "); // treasure pos
               } else if (map.getCell(i, j) == 'B') {
                   mapDisplay.append('B' + " "); // boss pos
               } else {
                   mapDisplay.append('.' + " "); // grass terrain
               }
            }
            mapDisplay.append("\n");
        }
        JOptionPane.showMessageDialog(null, mapDisplay.toString(), "Game Map", JOptionPane.PLAIN_MESSAGE);
    } 
    
    // displays player's statuses and inventory, will be used underneath the game map
    private void displayPlayerStats(Player player) {
        StringBuilder playerStats = new StringBuilder();
        playerStats.append("                  Player's status:\n");
        playerStats.append("==================================================\n");
        playerStats.append("[[Name: ").append(player.getName()).append("]] ")
                  .append("[[Gold: ").append(player.getGold()).append("]]\n");
        playerStats.append("[[Health: ").append(player.getHealth()).append("]] ")
                  .append("[[EXP: ").append(player.getEXP()).append("]]\n");
        playerStats.append("[[Level: ").append(player.getLevel()).append("]]\n");
        playerStats.append("[[Attack: ").append(player.getAttack()).append("]]\n");
        playerStats.append("[[Defense: ").append(player.getDefense()).append("]]\n");
        playerStats.append("                Player's Inventory:\n");
        playerStats.append("==================================================\n");
        playerStats.append(player.getInventory()).append("\n");
        
        JOptionPane.showMessageDialog(null, playerStats.toString(), "Player's Stats", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
