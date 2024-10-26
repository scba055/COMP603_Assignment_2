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
    private DatabaseController dbCon;
    private final Player player;
    private final GameMap map;
    private final Map<String, Enemy> enemies;
    private final JTextArea mapDisplay;
    private final Controller.PlayerController pc;
    private final Controller.EncounterController ec;
    private final Controller.GameController gc;
    private final Controller.SaveLoadController slc;
    private boolean enemyButtonFlag = false;
    private boolean storeButtonFlag = false;
    // private final JTextArea playerStats;
    private JPanel controlPanel;
    private JPanel optionsPanel;
    private JTextArea gameLog;
    
    // custom buttons for encounters (enemy and store)
    private JButton attackButton;
    private JButton healButton;
    private JButton guardButton;
    private JButton runButton;
    
    private JButton option1;
    private JButton option2;
    private JButton option3;
    private JButton option4;
    
    // custom area for enemy encounters
    private JTextArea enemyStatsArea;
    
    public GameView(Player player, GameMap map, Map<String, Enemy> enemies, 
            Controller.PlayerController pc, Controller.EncounterController ec,
            Controller.GameController gc, Controller.SaveLoadController slc){
        this.player = player;
        this.map = map;
        this.enemies = enemies;
        this.pc = pc;
        this.ec = ec;
        this.gc = gc;
        this.slc = slc;
        
        // setting up the UI
        setTitle("YWJ5422's World");
        setSize(600, 400); // will change these dimensions as I go
        setLayout(new BorderLayout());
        
        mapDisplay = new JTextArea(10,30);
        mapDisplay.setEditable(false);
        gameLog = new JTextArea();
        gameLog.setEditable(false);
        JPanel optionsPanel = new JPanel(new GridLayout(1,2));
        
        enemyStatsArea = new JTextArea();
        enemyStatsArea.setEditable(false);
        enemyStatsArea.setVisible(false); // should be hidden by default
        
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
        JButton interactButton = new JButton("●"); // wanting to emulate a gameboy
        // symbols from: https://www.alt-codes.net/
        
        // Enemy encounter buttons
        attackButton = new JButton("Attack");
        healButton = new JButton("Heal");
        guardButton = new JButton("Guard");
        runButton = new JButton("Run");
        toggleEnemyEncounterButtons(enemyButtonFlag);
        
        // Store encounter buttons
        option1 = new JButton("1");
        option2 = new JButton("2");
        option3 = new JButton("3");
        option4 = new JButton("4");
        toggleStoreEncounterButtons(storeButtonFlag);
         
        // navigation action listeners
        upButton.addActionListener(e -> pc.movePlayer('w', map, player));
        leftButton.addActionListener(e -> pc.movePlayer('a', map, player));
        downButton.addActionListener(e -> pc.movePlayer('s', map, player));
        rightButton.addActionListener(e -> pc.movePlayer('d', map, player));
        interactButton.addActionListener(e -> gc.interact(pc.getInteraction()));
        
        // enemy encounter action listeners
        attackButton.addActionListener(e -> ec.handleAttack());
        healButton.addActionListener(e -> ec.handleHeal());
        guardButton.addActionListener(e -> ec.handleGuard());
        runButton.addActionListener(e -> ec.handleRun());
        
        // store encounter action listeners
        option1.addActionListener(e -> ec.handleOption1());
        option2.addActionListener(e -> ec.handleOption2());
        option3.addActionListener(e -> ec.handleOption3());
        option4.addActionListener(e -> ec.handleOption4());
        
        // adding buttons to control panel
        controlPanel.add(upButton);
        controlPanel.add(leftButton);
        controlPanel.add(interactButton);
        controlPanel.add(rightButton);
        controlPanel.add(downButton);
        controlPanel.add(attackButton);
        controlPanel.add(healButton);
        controlPanel.add(guardButton);
        controlPanel.add(runButton);
        controlPanel.add(option1);
        controlPanel.add(option2);
        controlPanel.add(option3);
        controlPanel.add(option4);
        
        if (enemyButtonFlag == true || storeButtonFlag == true) {
            upButton.setVisible(false);
            leftButton.setVisible(false);
            rightButton.setVisible(false);
            downButton.setVisible(false);
            interactButton.setVisible(false);
            if (enemyButtonFlag == true) {
                storeButtonFlag = false;
                toggleStoreEncounterButtons(storeButtonFlag);
            } else if (storeButtonFlag == true) {
                enemyButtonFlag = false;
                toggleEnemyEncounterButtons(enemyButtonFlag);
            }
        }
    }
    
    // adding buttons for the options menu
    private void addOptionsButtons() {
        JButton infoButton = new JButton("Player Info");
        JButton signOutButton = new JButton("Sign Out");
        
        infoButton.addActionListener(e -> displayPlayerStats(player));
        signOutButton.addActionListener(e -> signOut());
        
        if (optionsPanel == null) {
            System.out.println("optionsPanel is null!"); // debugging purposes
        }
        
        optionsPanel.add(infoButton);
        optionsPanel.add(signOutButton);
    }
    
    public void toggleEnemyEncounterButtons(boolean isShowing) {
        attackButton.setVisible(isShowing);
        healButton.setVisible(isShowing);
        guardButton.setVisible(isShowing);
        runButton.setVisible(isShowing);
    }
    
    public void toggleStoreEncounterButtons(boolean isShowing) {
        option1.setVisible(isShowing);
        option2.setVisible(isShowing);
        option3.setVisible(isShowing);
        option4.setVisible(isShowing);
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
            boolean isSaved = slc.saveGame(player, map);
            if (isSaved) {
                dbCon.closeConnection();
                System.exit(0); // if successful, then exit
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save", "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            dbCon.closeConnection();
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
            boolean isSaved = slc.saveGame(player, map);
            if (isSaved) {
                JOptionPane.showMessageDialog(this, "Game saved successfully.");
                returnToMainMenu(); // goes to main menu GUI
            } else {
                JOptionPane.showMessageDialog(this, "Save was unsuccessful.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
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
        new MainMenuView(dbCon); // initialises Main Menu
    }
    
    public void returnToGameMap() {
    
    }
    

    
    // this will handle the attack/guard/heal and run options for the user
    private int showEncounterOptions(String message, String[] options) {
        return JOptionPane.showOptionDialog(this, message, "Encounter",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
    }
    
    // includes the information necessary to know within a fight against an enemy
    public void updateEnemyStats(Enemy enemy) {
        enemyStatsArea.setVisible(true);
        // should only show the relevant stats for a player to see
        StringBuilder stats = new StringBuilder();
        stats.append(enemy.getName()).append("\n")
                .append("Lvl: ").append(enemy.getLevel())
                .append("Health: ").append(enemy.getHealth());
        enemyStatsArea.setText(stats.toString());
    }
    
    // similar to the code above but for the boss
    public void updateBossStats(Boss boss) {
        enemyStatsArea.setVisible(true);
        // should only show the relevant stats for a player to see
        StringBuilder stats = new StringBuilder();
        stats.append(boss.getName()).append("\n")
                .append("Lvl: ").append(boss.getLevel())
                .append("Health: ").append(boss.getHealth());
        enemyStatsArea.setText(stats.toString());
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
