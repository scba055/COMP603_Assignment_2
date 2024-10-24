package Controller;

/**
 *
 * @author ywj5422
 */
public class GameController {
    public static boolean enemyEncounter = false;
    public static boolean storeEncounter = false;
    public static boolean treasureEncounter = false;
    public static boolean bossEncounter = false;
    
    public void interact(String cell) {
        String currentCell = cell;
        switch (currentCell) {
            case "S ":
                // store handling
                storeEncounter = true;
                break;
            case "E ":
                // enemy handling
                enemyEncounter = true;
                break;
            case "T ":
                // treasure handling
                treasureEncounter = true;
                break;
            case "B ":
                // boss handling
                bossEncounter = true;
                break;
        } 
    }
}
