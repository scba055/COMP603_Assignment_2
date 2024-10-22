package Model;

/**
 *
 * @author ywj5422
 */

// class repurposed from Project 1
public abstract class GameCharacter {
    protected String name;
    protected int health;
    protected int level;
    private int attack;
    private int defense;
    private int row;
    private int col;
    
    public GameCharacter(String name, int health, int level, int attack, int defense, 
            int startRow, int startCol) {
        setName(name);
        setHealth(health);
        setLevel(level);
        setAttack(attack);
        setDefense(defense);
        setRow(startRow);
        setCol(startCol);
    }
    
    // feature that all characters share
    // should be useful to fix the original issue found in Project 1
    public boolean isAlive() {
        boolean status = false;
        if (getHealth() <= 0) {
            status = true;
        }
        return status;
    }
    
    // getters and setters
    // name
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    //level
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    // health
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    
    // attack
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    
    // defense
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    
    // rows and columns
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }
}
