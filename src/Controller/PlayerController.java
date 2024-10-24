package Controller;

import Model.GameMap;

/**
 *
 * @author ywj5422
 */
public class PlayerController {
    private String interaction;
    private Model.Player player;
    private int oldRow, oldCol;
    
    // gets the player's current row and col position
    public PlayerController(Model.Player player){
        this.oldRow = player.getRow();
        this.oldCol = player.getCol();
    }
    
    // class that allows the player to move positions in the map
    public boolean movePlayer(char direction, GameMap map, Model.Player player) {
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
