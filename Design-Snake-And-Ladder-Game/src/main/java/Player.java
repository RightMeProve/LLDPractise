/**
 * Represents a player in the Snake and Ladder game.
 */
public class Player {
    /**
     * The unique identifier or name of the player.
     */
    String id;
    
    /**
     * The current numerical position of the player on the board.
     */
    int currentPosition;

    /**
     * Constructs a new Player.
     *
     * @param id The ID or name of the player.
     * @param currentPosition The starting position (usually 0).
     */
    public Player(String id, int currentPosition){
        this.id = id;
        this.currentPosition = currentPosition;
    }
}
