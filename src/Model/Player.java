package Model;

/**
 *
 * @author ywj5422
 */
import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

// from project 1, will change things if needed
public class Player extends GameCharacter implements Serializable {
    // player-specific stats
    private int experience;
    private int gold;
    private String username, password;
    private Map<String, Integer> inventory = new HashMap<>();
    
    // constructor for new players
    public Player(String name, int health, int level, int attack, int defense, 
            int startRow, int startCol) {
        super(name, health, level, attack, defense, startRow, startCol);
        setEXP(0);
        setGold(0);
        setInventory(null);
        
    }
    
    // player-specific getters and setters
    // experience
    public int getEXP() { return experience; }
    public void setEXP(int experience) { this.experience = experience; }
    
    // gold
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public void addGold(int gold) { this.gold += gold; }
    public void subGold(int gold) { this.gold -= gold; }
    
    // inventory size
    public Map<String, Integer> getInventory() { return inventory; }
    public void setInventory(Map<String, Integer> inventory) { this.inventory = inventory; }
    
    // login that is related to the character
    public void setUsername(String user) {this.username = user; }
    public String getUsername() { return username; }
    public void setPassword(String pass) { this.password = pass; }
    public String getPassword() { return password; }
}
