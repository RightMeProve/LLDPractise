import java.util.concurrent.ThreadLocalRandom;

/**
 * Simulates one or multiple dice to be rolled by the players.
 */
public class Dice {
    /**
     * Number of dice being used in the game.
     */
    int diceCount;
    
    /**
     * Minimum value on a single die face.
     */
    int min = 1;
    
    /**
     * Maximum value on a single die face.
     */
    int max = 6;

    /**
     * Initializes the dice configuration.
     * @param diceCount The number of dice to roll at once.
     */
    public Dice(int diceCount){
        this.diceCount = diceCount;
    }

    /**
     * Rolls the configured number of dice and returns the total sum.
     *
     * @return The sum of the values from rolling the dice.
     */
    public int rollDice(){
        int totalSum = 0;
        for(int i = 0; i < diceCount; i++){
            // ThreadLocalRandom is used for efficient and thread-safe random generation
            totalSum += ThreadLocalRandom.current().nextInt(min, max + 1);
        }
        return totalSum;
    }
}
