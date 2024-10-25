package Main;

/**
 *
 * @author ywj5422
 */

import Controller.DatabaseController;
import View.MainMenuView;
import Model.Database;

public class GameMain {

    public static void main(String[] args) {
        Database db = new Database();
        DatabaseController dbCon = new DatabaseController(db);
        new MainMenuView(dbCon);
    }
    
}
