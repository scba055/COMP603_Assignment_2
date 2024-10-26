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
    private GameMap map;
    private final Map<String, Enemy> enemies;
    private final Controller.PlayerController pc;
    private final Controller.EncounterController ec;
    private final Controller.GameController gc;
    private final Controller.SaveLoadController slc;
    private boolean enemyButtonFlag = false;
    private boolean storeButtonFlag = false;
    // private UI components;
    private JPanel controlPanel;
    private JPanel optionsPanel;
    private JTextArea gameLog;
    private JPanel mapPanel;
    
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
        this.map = new GameMap(5,10);
        this.enemies = enemies;
        this.pc = pc;
        this.ec = ec;
        this.gc = gc;
        this.slc = slc;
        
        setupUI();
    }
    
    private void setupUI() {
        setTitle("YWJ5422's World");
        setSize(800, 600); 
        setLayout(new BorderLayout());

        // Map Display Panel
        mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(map.getMap().length, map.getMap()[0].length)); // Rows x Columns
        add(mapPanel, BorderLayout.CENTER);

        // Control Panel (Bottom)
        controlPanel = new JPanel(new GridLayout(2, 3));
        addControlButtons();
        add(controlPanel, BorderLayout.SOUTH);

        // Options Panel (Top)
        optionsPanel = new JPanel(new FlowLayout());
        addOptionsButtons();
        add(optionsPanel, BorderLayout.NORTH);

        // Game Log on the right side
        gameLog = new JTextArea(10, 20);
        gameLog.setEditable(false);
        add(new JScrollPane(gameLog), BorderLayout.EAST); // allows for scrollability

        // Window close operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleCloseOperation();
            }
        });

        displayMap(map, player);
        setVisible(true);
    }
    
    // adds directional buttons and interaction buttons
    private void addControlButtons() {
        // adding movement and interactions buttons for user
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
        upButton.addActionListener(e -> {
            if (pc.movePlayer('w', map, player)) { // checks if move is valid
                displayMap(map, player); //updates map
                gc.interact(pc.getInteraction()); // checks for interactable
                map.setCell(player.getRow(), player.getCol(), '.');
            } else {
                displayError("Invalid move. Try again.");
            }
        });    
            
        leftButton.addActionListener(e -> {
            if (pc.movePlayer('a', map, player)) {
                displayMap(map, player); //updates map
                gc.interact(pc.getInteraction());
                map.setCell(player.getRow(), player.getCol(), '.');
            } else {
                displayError("Invalid move. Try again.");
            }
        }); 
        downButton.addActionListener(e -> {
            if (pc.movePlayer('s', map, player)) {
                displayMap(map, player); //updates map
                gc.interact(pc.getInteraction());
                map.setCell(player.getRow(), player.getCol(), '.');
            } else {
                displayError("Invalid move. Try again.");
            }
        });
        rightButton.addActionListener(e -> {
            if (pc.movePlayer('d', map, player)) {
                displayMap(map, player); //updates map
                gc.interact(pc.getInteraction());
                map.setCell(player.getRow(), player.getCol(), '.');
            } else {
                displayError("Invalid move. Try again.");
            }
        });
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
        // enemy/boss panel
        controlPanel.add(attackButton);
        controlPanel.add(healButton);
        controlPanel.add(guardButton);
        controlPanel.add(runButton);
        // store panel
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
                toggleEnemyEncounterButtons(enemyButtonFlag);
            } else if (storeButtonFlag == true) {
                toggleStoreEncounterButtons(storeButtonFlag);
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
        // scroll to latest entry
        gameLog.setCaretPosition(gameLog.getDocument().getLength());
    }
    
    // display any invalid inputs from the user
    public void displayError(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    // operations done in SaveLoadController, but UI done here
    private void handleCloseOperation() {
        int choice = JOptionPane.showConfirmDialog(this, 
                "Do you want to save your progress before exiting?", "Exit Game",
                JOptionPane.YES_NO_CANCEL_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            boolean isSaved = slc.saveGame(player, map);
            if (isSaved) {
                System.exit(0); // if successful, then exit
                dbCon.closeConnection();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save", "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0); // exit without saving
            dbCon.closeConnection();
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
    public void displayMap(GameMap map, Player player) {
        mapPanel.removeAll(); // clear any previous components in the panel
        
        char[][] layout = this.map.getMap();
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                JLabel cellLabel = new JLabel(String.valueOf(layout[i][j]), SwingConstants.CENTER);
                cellLabel.setOpaque(true);
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));  // Optional border
                
                if (player.getRow() == i && player.getCol() == j) {
                    if (map.getCell(i, j) == 'S') {
                        displayMessage("You have entered a store!");
                    }
                }
                
                // customize specific cells (e.g., player's position, enemies, stores, etc.)
                if (i == player.getRow() && j == player.getCol()) {
                    cellLabel.setText("P"); // Player
                    cellLabel.setBackground(new Color(0xf8edeb));
                } else if (map.getCell(i, j) == 'E') {
                    cellLabel.setText("E");
                    cellLabel.setBackground(new Color(0xfae1dd));  // Enemy cell
                } else if (map.getCell(i, j) == 'S') {
                    cellLabel.setText("S");
                    cellLabel.setBackground(new Color(0xe8e8e4));  // Store cell
                } else if (map.getCell(i, j) == 'T') {
                    cellLabel.setText("T");
                    cellLabel.setBackground(new Color(0xd8e2dc));
                } else if (map.getCell(i, j) == 'B') {
                    cellLabel.setText("B");
                    cellLabel.setBackground(new Color(0xfec5bb));
                } else if (map.getCell(i, j) == '.'){ // terrain color
                    cellLabel.setText(".");
                    cellLabel.setBackground(new Color(0xfcd5ce));
                }
                
                // colours from https://coolors.co/palettes/popular/6%20colors
                mapPanel.add(cellLabel); // Add each cell to the GridLayout
                gc.displayEncounter();
            }
        }

        mapPanel.revalidate();  // Refresh the panel to apply changes
        mapPanel.repaint();
    } 
    
    // displays player's statuses and inventory, will be used underneath the game map
    private void displayPlayerStats(Player player) {
        StringBuilder playerStats = new StringBuilder();
        playerStats.append("                           \tPlayer's status:\n");
        playerStats.append("==================================================\n");
        playerStats.append("[[Name: ").append(player.getName()).append("]] ")
                  .append("[[Gold: ").append(player.getGold()).append("]]\n");
        playerStats.append("[[Health: ").append(player.getHealth()).append("]] ")
                  .append("[[EXP: ").append(player.getEXP()).append("]]\n");
        playerStats.append("[[Level: ").append(player.getLevel()).append("]]\n");
        playerStats.append("[[Attack: ").append(player.getAttack()).append("]]\n");
        playerStats.append("[[Defense: ").append(player.getDefense()).append("]]\n");
        playerStats.append("                          \tPlayer's Inventory:\n");
        playerStats.append("==================================================\n");
        playerStats.append(player.getInventory()).append("\n");
        
        JOptionPane.showMessageDialog(null, playerStats.toString(), "Player's Stats", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
}
