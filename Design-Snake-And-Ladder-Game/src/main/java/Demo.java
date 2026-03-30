/**
 * Main application client simulating the setup and start of the Snake and Ladder Game.
 */
public class Demo {
    /**
     * Entry point for running the Snake and Ladder simulation.
     */
    public static void main(String[] args){
        // Initializes the game dependencies internally and executes the loop
        Game obj = new Game();
        obj.startGame();
    }
}
