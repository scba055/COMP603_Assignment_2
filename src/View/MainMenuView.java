package View;

import Model.*;
import Controller.*;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MainMenuView extends JFrame {
    private final DatabaseController dbCon;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, exitButton;
    private JButton confirmLoginButton, confirmSignupButton;
    private JTextField characterNameField; // For signup

    public MainMenuView(DatabaseController dbCon) {
        this.dbCon = dbCon;

        // Setting up the main menu UI
        setTitle("Main Menu");
        setSize(400, 300);
        setLayout(new GridLayout(4, 1));

        usernameField = new JTextField("Username");
        passwordField = new JPasswordField("Password");
        characterNameField = new JTextField("Character Name (for signup)");
        characterNameField.setVisible(false); // Only visible on signup

        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");
        exitButton = new JButton("Exit");

        confirmLoginButton = new JButton("Confirm Login");
        confirmSignupButton = new JButton("Confirm Signup");
        confirmLoginButton.setVisible(false);
        confirmSignupButton.setVisible(false);

        add(loginButton);
        add(signupButton);
        add(exitButton);

        // Button listeners
        loginButton.addActionListener(e -> showLoginFields());
        signupButton.addActionListener(e -> showSignupFields());
        exitButton.addActionListener(e -> System.exit(0));

        confirmLoginButton.addActionListener(e -> login());
        confirmSignupButton.addActionListener(e -> signup());

        add(usernameField);
        add(passwordField);
        add(characterNameField);
        add(confirmLoginButton);
        add(confirmSignupButton);

        usernameField.setVisible(false);
        passwordField.setVisible(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void showLoginFields() {
        usernameField.setVisible(true);
        passwordField.setVisible(true);
        confirmLoginButton.setVisible(true);
        characterNameField.setVisible(false);
        confirmSignupButton.setVisible(false);
    }

    private void showSignupFields() {
        usernameField.setVisible(true);
        passwordField.setVisible(true);
        characterNameField.setVisible(true);
        confirmSignupButton.setVisible(true);
        confirmLoginButton.setVisible(false);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (dbCon.login(username, password)) {
            // Load existing game data
            Player player = dbCon.loadPlayer(username);
            GameMap map = dbCon.loadMap();
            Map<String, Enemy> enemies = dbCon.loadEnemies();

            // Initialize controllers
            PlayerController pc = new PlayerController(player);
            EncounterController ec = new EncounterController(pc, map, player);
            GameController gc = new GameController(ec);
            SaveLoadController slc = new SaveLoadController(dbCon);

            // Transition to GameView
            new GameView(player, map, enemies, pc, ec, gc, slc);
            dispose(); // Close the Main Menu
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
        }
    }

    private void signup() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String characterName = characterNameField.getText();

        Player newPlayer = dbCon.signup(username, password, characterName);

        boolean success = dbCon.login(newPlayer.getUsername(), newPlayer.getPassword());
        if (success) {
            JOptionPane.showMessageDialog(this, "Signup successful! Starting a new game.");

            // Initialize game data for the new player
            GameMap map = new GameMap(5, 5); // New game map
            Map<String, Enemy> enemies = dbCon.loadEnemies();

            // Initialize controllers
            PlayerController pc = new PlayerController(newPlayer);
            EncounterController ec = new EncounterController(pc, map, newPlayer);
            GameController gc = new GameController(ec);
            SaveLoadController slc = new SaveLoadController(dbCon);

            // Transition to GameView
            new GameView(newPlayer, map, enemies, pc, ec, gc, slc);
            dispose(); // Close the Main Menu
        } else {
            JOptionPane.showMessageDialog(this, "Signup failed. Please try again.");
        }
    }
}
