package Controller;

/**
 *
 * @author ywj5422
 */
public class PlayerController {
    private String interaction;
    private Model.Player player;
    private int oldRow, oldCol;
    private final Controller.GameController gc;
    private final Model.GameMap map;
    private final View.GameView gv;
    
    // gets the player's current row and col position
    public PlayerController(Model.Player player, Controller.GameController gc, 
            Model.GameMap map, Model.Player user, View.GameView view){
        this.oldRow = player.getRow();
        this.oldCol = player.getCol();
        this.gc = gc;
        this.map = map;
        this.player = user;
        this.gv = view;
    }
    
    private void processPlayerMovement(char move) {
        if (movePlayer(move, map, player)) {
            // checks if there is an encounter within the new row,col  
            gc.interact(getInteraction());
        } else {
            gv.displayMessage("Invalid move. You cannot leave the world map.");
        }
    }
    
    // class that allows the player to move positions in the map
    public boolean movePlayer(char direction, Model.GameMap map, Model.Player player) {
        int newRow = player.getRow();
        int newCol = player.getCol();
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
            player.move(newRow, newCol, map);
            map.setCell(newRow, newCol, 'P');
            validMove = true;
        } else {
            // do nothing since invalid
        }
        return validMove;
    }
    
    // gets the interaction between cells
    public String getInteraction() { return interaction; }
}
