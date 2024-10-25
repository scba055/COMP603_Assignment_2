package View;

/**
 *
 * @author ywj5422
 */

import javax.swing.*;
import java.awt.*;
import Controller.*;
import Model.*;

public class MainMenuView extends JFrame {
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton, signUpButton, newGameButton, loadGameButton;
    private JButton exitButton;
    private final DataBaseController dbCon;
    private Player currentPlayer;
    
}
