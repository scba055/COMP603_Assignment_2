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
    private Map<String, Integer> inventory = new HashMap<>();
    private String interaction;
    private int[][] newPos;
    
    // constructor for new players
    public Player(String name, int health, int level, int attack, int defense, 
            int startRow, int startCol) {
        super(name, health, level, attack, defense, startRow, startCol);
        setEXP(0);
        setGold(0);
        setInventory(null);
    }
    
    // class that allows the player to move positions in the map
    public boolean movePlayer(char direction, GameMap map) {
        int oldRow = super.getRow();
        int oldCol = super.getCol();
        int newRow = super.getRow();
        int newCol = super.getCol();
        boolean validMove= false;
        
        switch (direction) {
            case 'w':
                // saves value in future position to check for encounter
                newRow--; // move up
                break;
            case 's':
                newRow++; // move down
                break;
            case 'a':
                newCol--; // move left
                break;
            case 'd':
                newCol++; // move right
                break;
        }
        
        // ensures that the player cannot go out-of-bounds
        if (newRow >= 0 && newRow < map.getMap().length 
                && newCol >= 0 && newCol < map.getMap()[0].length) {
            interaction = map.getCell(newRow, newCol) + " ";
            map.setCell(oldRow, oldCol, '.'); // replaces the old 
            super.move(newRow, newCol, map);
            map.setCell(newRow, newCol, 'P');
            validMove = true;
        } else {
            // do nothing since invalid
        }
        return validMove;
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
    
    // gets the interaction between cells
    public String getInteraction() { return interaction; }
}
