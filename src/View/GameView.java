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
import Controller.*;

// includes old code and code that would create the UI for the game
public class GameView extends JFrame {
    private final Player player;
    private final GameMap map;
    private final Map<String, Enemy> enemies;
    private final JTextArea mapDisplay;
    private final Controller.PlayerController pc;
    //private final JTextArea playerStats;
    private JPanel controlPanel;
    private JPanel optionsPanel;
    private JTextArea gameLog;
    
    public GameView(Player player, GameMap map, Map<String, Enemy> enemies, Controller.PlayerController pc) {
        this.player = player;
        this.map = map;
        this.enemies = enemies;
        this.pc = pc;
        
        // setting up the UI
        setTitle("YWJ5422's World");
        setSize(600, 400); // will change these dimensions as I go
        setLayout(new BorderLayout());
        
        mapDisplay = new JTextArea(10,30);
        mapDisplay.setEditable(false);
        gameLog = new JTextArea();
        gameLog.setEditable(false);
        JPanel optionsPanel = new JPanel(new GridLayout(1,2));
        
        addControlButtons();
        addOptionsButtons();
        
        add(new JScrollPane(mapDisplay), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(optionsPanel, BorderLayout.NORTH);
        
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
    
    // adds directional buttons and interaction buttons
    private void addControlButtons() {
        // adding movement and interactions buttons for user
        JPanel controlPanel = new JPanel(new GridLayout(2,3));
        JButton upButton = new JButton("△");
        JButton rightButton = new JButton("▷");
        JButton downButton = new JButton("▽");
        JButton leftButton = new JButton("◁");
        JButton interactButton = new JButton("◖"); // wanting to emulate a gameboy
        JButton backButton = new JButton("◗");
        // symbols from: https://www.alt-codes.net/
        
        upButton.addActionListener(e -> pc.movePlayer('w', map, player));
        leftButton.addActionListener(e -> pc.movePlayer('a', map, player));
        downButton.addActionListener(e -> pc.movePlayer('s', map, player));
        rightButton.addActionListener(e -> pc.movePlayer('d', map, player));
        interactButton.addActionListener(e -> interact(pc.getInteraction()));
        
        // adding buttons to control panel
        controlPanel.add(upButton);
        controlPanel.add(leftButton);
        controlPanel.add(interactButton);
        controlPanel.add(backButton);
        controlPanel.add(rightButton);
        controlPanel.add(downButton);
    }
    
    // adding buttons for the options menu
    private void addOptionsButtons() {
        JButton infoButton = new JButton("Info");
        JButton signOutButton = new JButton("Sign Out");
        
        infoButton.addActionListener(e -> displayPlayerStats(player));
        signOutButton.addActionListener(e -> signOut());
        
        optionsPanel.add(infoButton);
        optionsPanel.add(signOutButton);
    }
    
    // display game messages
    public void displayMessage(String message) {
        gameLog.append(message + "\n");
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
        new MainMenuView(); // initialises Main Menu
    }
    
    private void interact(String cell) {
        String currentCell = cell;
        switch (currentCell) {
            case "S ":
                // store handling
                EncounterController.storeEncounter = true;
                break;
            case "E ":
                // enemy handling
                EncounterController.enemyEncounter = true;
                break;
            case "T ":
                // treasure handling
                EncounterController.treasureEncounter = true;
                break;
            case "B ":
                // boss handling
                EncounterController.bossEncounter = true;
                break;
        } 
    }
    
    // this will handle the attack/guard/heal and run options for the user
    private int showEncounterOptions(String message, String[] options) {
        return JOptionPane.showOptionDialog(this, message, "Encounter",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
    }
    
    
    // displays the entire game state: map and player stats
    private void displayGame(GameMap map, Player player, Map<String, Enemy> enemies) {
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
