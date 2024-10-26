package Model;

/**
 *
 * @author ywj5422
 */

import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

        
public class Database {
    private Connection conn;
    private static Database instance;
    private String url = "jdbc:derby:GameDB;create=true";  // DB URL
    private String testUrl = "jdbc:derby:TestDB;create=true"; // test db
    private String dbusername = "pdc";  // from tutorials
    private String dbpassword = "pdc";  // from tutorials
    private String username;
    private String password;
    private Map<String, Enemy> enemies = new HashMap<>();
    
    public Database() {
        connect();
        createTables();
    }
    
    // applying Singleton principle
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    
    // ensures that the database is connected properly
    private boolean connect() {
        boolean isConned = false;
        try {
            conn = DriverManager.getConnection(testUrl, dbusername, dbpassword);
            System.out.println("Database successfully connected.");
            isConned = true;
        } catch (SQLException e) {
            // prints error message for debugging
            System.out.println("Error connecting to database: " 
                    + e.getMessage());
        }
        return isConned;
    }
    
    private void createTables() {
        createPlayerTable();
        createInventoryTable();
        createEnemiesTable();
    }
    
    // adds player stats into the database
    private void createPlayerTable() {
        try (Statement pst = conn.createStatement()){
            String createPlayerTable = 
                    "CREATE TABLE Player (" +
                    "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(50) NOT NULL, " +
                    "name VARCHAR(50), " +
                    "health INT, " +
                    "level INT, " +
                    "attack INT, " +
                    "defense INT, " +
                    "exp INT, " +
                    "gold INT, " +
                    "row INT, " +
                    "col INT" +
                    ")";
            pst.executeUpdate(createPlayerTable);
            System.out.println("Player table has been created or already exists."); // for debugging
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) { // sees if an inventory table exists in the database
                System.out.println("Player table already exists");
            } else {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    // adds the player inventory to the database
    private void createInventoryTable() {
        try (Statement ist = conn.createStatement()){
            String createInventoryTable = 
                    "CREATE TABLE Inventory (" +
                    "player_id INT," +
                    "item_name VARCHAR(50)," +
                    "quantity INT," +
                    "FOREIGN KEY (player_id) REFERENCES Player(id) ON DELETE CASCADE" +
                    ")";
            ist.executeUpdate(createInventoryTable);
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) { // sees if an inventory table exists in the database
                System.out.println("Inventory table already exists");
            } else {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    private void createEnemiesTable() {
        String enemyTable = 
                "CREATE TABLE Enemies (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                "name VARCHAR(50), " +
                "level INT, health INT, attack INT, defense INT)";
        
        try (Statement est = conn.createStatement()) {
            est.executeUpdate(enemyTable);
            System.out.println("Enemies table created.");
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) { // sees if an inventory table exists in the database
                System.out.println("Enemies table already exists");
            } else {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    // using old code from project 1 to allow the database save the entries in 
    // the txt file
    public void importEnemies(String file) {
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
                    enemyToDatabase(name, health, level, attack, defense);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + file);
        }
    }
    
    private void enemyToDatabase(String name, int lvl, int health, int attack,
            int defense) {
        String insertEnemy = 
                "INSERT INTO Enemies (name, health, level, attack, defense)" +
                "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement epst = conn.prepareStatement(insertEnemy)) {
            epst.setString(1, name);
            epst.setInt(2, lvl);
            epst.setInt(3, health);
            epst.setInt(4, attack);
            epst.setInt(5, defense);
            epst.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    // loads enemies from the database
    public Map<String, Enemy> loadEnemies() {
        String enemySelect = "SELECT * FROM Enemies";
        try (Statement est = conn.createStatement(); 
             ResultSet rs = est.executeQuery(enemySelect)) {
            while (rs.next()) {
                String name = rs.getString("name");
                int level = rs.getInt("level");
                int health = rs.getInt("health");
                int attack = rs.getInt("attack");
                int defense = rs.getInt("defense");
                
                Enemy enemy = new Enemy(name, level, health, attack, defense);
                enemies.put(name,enemy);
            }
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
        return enemies;
    }
    
    // from tutorial 9_3 without the score mechanics
    // Login method to validate user credentials
    public boolean login(String username, String password) {
        this.username = username;
        this.password = password;
        boolean matchFound = false;

        // Check if the username exists in the database
        if (checkName(username)) {
            // Username exists, so check the password
            String query = "SELECT password FROM Player WHERE username = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, username);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String pass = rs.getString("password");
                    if (password.equals(pass)) {
                        System.out.println("Welcome back " + username + "!");
                        matchFound = true;
                    }
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return matchFound;
    }
    
    // used in the signUp process;
    public Player newPlayer(String username, String password, String characterName) {
        Player player = null;
        // inserts default values for new characters
        String insertPlayerSQL = 
            "INSERT INTO Player (username, password, name, health, level, attack, defense, exp, gold, row, col) " +
            "VALUES (?, ?, ?, 100, 1, 10, 10, 0, 0, 0, 0)";
    
            try (PreparedStatement pst = conn.prepareStatement(insertPlayerSQL)) {
                pst.setString(1, username);  // Username provided by the user
                pst.setString(2, password);  // password provided by the user
                pst.setString(3, characterName);  // Character name provided by the user

                pst.executeUpdate();  // Execute the SQL statement
                System.out.println("New player created successfully.");
                player = loadPlayer(username);
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        return player;
    }
    
    // from tutorial 9_3 without the score mechanics
    // Method to check if a username exists in the database without logging in
    private boolean checkName(String username) {
        boolean userExists = false;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT username FROM Player WHERE username='" + username + "'");
            if (rs.next()) {
                userExists = true;  // The user exists
            }
            rs.close();  // Close ResultSet
            statement.close();  // Close Statement
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
        return userExists;  // Return whether the user exists
    }
    
    // saves player data into the database
    public boolean savePlayer(Player player) {
        boolean isSaved = false;
        String insertPlayerSQL = 
                "MERGE INTO Player AS P " +
                "USING (VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)) AS V (" +
                "username, password, name, health, level, attack," +
                "defense, exp, gold, row, col" +
                ")" +
                "ON P.username = V.username" +
                "WHEN MATCHED THEN" + // checks for a matching username
                    "UPDATE SET" + // inserts the stats
                        "password = V.password, name = V.name, health = V.health,"
                        + "level = V.level, attack = V.attack, defense = V.defense"
                        + "exp = V.exp, gold = V.gold, row = V.row, col = V.col" +
                "WHEN NOT MATCHED THEN" // when no matching username
                + "INSERT (username, password, name, health, level, attack"
                + "defense, exp, gold, row, col)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement newpst = conn.prepareStatement(insertPlayerSQL, 
                Statement.RETURN_GENERATED_KEYS)) {
            newpst.setString(1, player.getUsername());
            newpst.setString(2, player.getPassword());
            newpst.setString(3, player.getName());
            newpst.setInt(4, player.getHealth());
            newpst.setInt(5, player.getLevel());
            newpst.setInt(6, player.getAttack());
            newpst.setInt(7, player.getDefense());
            newpst.setInt(8, player.getEXP());
            newpst.setInt(9, player.getGold());
            newpst.setInt(10, player.getRow());
            newpst.setInt(11, player.getCol());
            newpst.executeUpdate();
            
            // generates player ID (if new entry)
            try (ResultSet keys = newpst.getGeneratedKeys()) {
                if (keys.next()) {
                    int playerId = keys.getInt(1); // retrieve player's id
                    System.out.println("Player saved with unique ID: " + playerId);
                    
                    // saves inventory to player
                    saveInventory(playerId, player.getInventory());
                }
            }
            isSaved = true;
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
        return isSaved;
    }
    
    // helper function for savePlayer, links inventory to the player via unique id
    private void saveInventory(int id, Map<String, Integer> inventory) {
        String insertInvSQL = 
                "INSERT INTO Inventory (player_id, item_name, quantity)"
                + "VALUES (?, ?, ?)";
        
        try (PreparedStatement ist = conn.prepareStatement(insertInvSQL)){
            for (Map.Entry<String, Integer> item : inventory.entrySet()) {
                ist.setInt(1, id); // link to player unique id
                ist.setString(2, item.getKey()); // Item name
                ist.setInt(3, item.getValue()); // item quantity
                ist.addBatch();
            }
            ist.executeBatch();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public Player loadPlayer(String username) {
        String selectPlayer = "SELECT * FROM Player WHERE username = ?";
        Player player = null;
        
        try (PreparedStatement pst = conn.prepareStatement(selectPlayer)) {
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                // create Player object with data gathered from databse
                player = new Player(
                        rs.getString("name"),
                        rs.getInt("health"),
                        rs.getInt("level"),
                        rs.getInt("attack"),
                        rs.getInt("defense"),
                        rs.getInt("row"),
                        rs.getInt("col"));
                player.setUsername(rs.getString("username"));
                player.setPassword(rs.getString("password"));
                player.setEXP(rs.getInt("exp"));
                player.setGold(rs.getInt("gold"));
                
                player.setInventory(loadInventory(rs.getInt("id")));
                Logger.getLogger(Database.class.getName()).log(Level.INFO, "Player {0} loaded successfully.", username);
            }
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } 
        
        return player;
    }
    
    // helper function for loadPlayer, inserts the inventory that the player has
    // under their name
    private Map<String,Integer> loadInventory(int playerId) {
        String selectInv = "SELECT * FROM Inventory WHERE player_id = ?";
        Map<String, Integer> inventory = new HashMap<>();
        
        try (PreparedStatement ist = conn.prepareStatement(selectInv)) {
            ist.setInt(1, playerId);
            ResultSet rs = ist.executeQuery();
            
            while (rs.next()) {
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                inventory.put(itemName, quantity);
            }
            Logger.getLogger(Database.class.getName()).log(Level.INFO, "Inventory loaded for player {0}", playerId);
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
        return inventory;
    }
    
    public boolean saveMap(GameMap map) {
        boolean isSaved = false;
        String deleteOldMapData = "DELETE FROM MapCells";
        String deleteOldMapDimensions = "DELETE FROM MapInfo";
        String insertNewMap = "INSERT INTO MapCells (row, col, value)"
                + "VALUES (?, ?, ?)";
        String insertNewMapDimensions = "INSERT INTO MapInfo (rows, cols)"
                + "VALUES (?,?)";
        
        try (Statement deletest = conn.createStatement();
                PreparedStatement cellst = conn.prepareStatement(insertNewMap);
                PreparedStatement infost = conn.prepareStatement(insertNewMapDimensions)) {
            // clear old map cells
            deletest.executeUpdate(deleteOldMapData);
            deletest.executeUpdate(deleteOldMapDimensions);
            
            // save new map dimensions
            infost.setInt(1, map.getMap().length);
            infost.setInt(2, map.getMap()[0].length);
            infost.executeUpdate();
            
            // save new map
            char[][] layout = map.getMap();
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[i].length; j++) {
                    cellst.setInt(1,j);
                    cellst.setInt(2,j);
                    cellst.setString(3, String.valueOf(layout[i][j]));
                    cellst.addBatch();
                }
            }
            
            cellst.executeBatch();
            System.out.println("Map successfully saved.");
            isSaved = true;
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
        return isSaved;
    }
    
    // inserts the necessary dimensions from the saved map in the database
    // also inserts the value within an index in the map
    // if there is no map due to new player, then make a new map
    public GameMap loadMap() {
        String selectMapInfo = "SELECT rows, cols FROM MapInfo";
        String selectMapData = "Select row, col, value FROM MapCells";
        char[][] layout = null;
        
        try(Statement st = conn.createStatement();
                ResultSet infors = st.executeQuery(selectMapInfo)) {
            
            if (infors.next()) {
                int rows = infors.getInt("rows");
                int cols = infors.getInt("cols");
                layout = new char[rows][cols];
            }
            
            if (layout != null) {
                try(PreparedStatement cellst = conn.prepareStatement(selectMapData)) {
                    ResultSet cellrs = cellst.executeQuery();
                    
                    while(cellrs.next()) {
                        int row = cellrs.getInt("row");
                        int col = cellrs.getInt("col");
                        char value = cellrs.getString("value").charAt(0);
                        layout[row][col] = value;
                    }
                }
            }
            
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
        // if game map is null, makes a new game map
        return layout != null ? new GameMap(layout.length, layout[0].length,
        layout) : new GameMap(5,10);
    }
}
