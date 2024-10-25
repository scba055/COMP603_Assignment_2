package Model;

/**
 *
 * @author ywj5422
 */
public class Boss extends GameCharacter {
    
    // initialises the boss the same way as the enemy
    public Boss(String name, int health, int level, int attack, int defense) {
        super(name, health, level, attack, defense, -1, -1);
    }
}
