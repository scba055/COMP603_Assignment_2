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
import Controller.SaveLoadController;

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
        
        // adding movement and interactions buttons for user
        JPanel controlPanel = new JPanel(new GridLayout(2,3));
        JButton upButton = new JButton("△");
        JButton rightButton = new JButton("▷");
        JButton downButton = new JButton("▽");
        JButton leftButton = new JButton("◁");
        JButton interactButton = new JButton("◖"); // wanting to emulate a gameboy
        JButton backButton = new JButton("◗");
        // symbols from: https://www.alt-codes.net/
        
        // adding buttons to control panel
        controlPanel.add(upButton);
        controlPanel.add(leftButton);
        controlPanel.add(interactButton);
        controlPanel.add(backButton);
        controlPanel.add(rightButton);
        controlPanel.add(downButton);
        
        add(controlPanel, BorderLayout.SOUTH);
        
        // implementing Player info and sign out buttons
        JPanel optionsPanel = new JPanel(new GridLayout(1,2));
        JButton infoButton = new JButton("Info");
        JButton signOutButton = new JButton("Sign Out");
        // adding buttons to options panel
        optionsPanel.add(infoButton);
        optionsPanel.add(signOutButton);
        add(optionsPanel, BorderLayout.NORTH);
        
        // setting up the event listeners for the buttons
        upButton.addActionListener(e -> player.movePlayer('w', map));
        leftButton.addActionListener(e -> player.movePlayer('a', map));
        downButton.addActionListener(e -> player.movePlayer('s', map));
        rightButton.addActionListener(e -> player.movePlayer('d', map));
        interactButton.addActionListener(e -> interact());
        infoButton.addActionListener(e -> displayPlayerStats(player));
        signOutButton.addActionListener(e -> signOut());
        
        // wanting to make the default close window ops to ask the user to save
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleCloseOperation();
            }
        });
        
        // display initial game state
        displayMap(map, player, enemies);
        setVisible(true);
    }
    
    // operations done in SaveLoadController, but UI done here
    private void handleCloseOperation() {
        int choice = JOptionPane.showConfirmDialog(this, 
                "Do you want to save your progress before exiting?", "Exit Game",
                JOptionPane.YES_NO_CANCEL_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            boolean isSaved = SaveLoadController.saveGame(player, map);
            if (isSaved) {
                System.exit(0); // if successful, then exit
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save", "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0); // exit without saving
        }
        // cancel option just brings the user back to the game
    }
    
    // implementing saving the game before signing out, brings user back to main menu
    private void signOut() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Do you want to save your progress before returning to the main menu?",
                "Sign Out", JOptionPane.YES_NO_CANCEL_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            boolean isSaved = SaveLoadController.saveGame(player, map);
            if (isSaved) {
                JOptionPane.showMessageDialog(this, "Game saved successfully.");
                returnToMainMenu(); // goes to main menu GUI
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            returnToMainMenu();
        }
        // cancel does nothing, the game continues
    }
    
    private void returnToMainMenu() {
        // gets rid of the existing GUI to bring the user back to first GUI
        // i.e. Main Menu
        this.dispose();
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
