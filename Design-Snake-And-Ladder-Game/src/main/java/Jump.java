/**
 * Represents a Jump on the board, which can be either a Snake or a Ladder.
 */
public class Jump {
    /**
     * The starting position of the jump.
     */
    int start;
    
    /**
     * The ending position of the jump.
     * If start > end, it's a Snake.
     * If start < end, it's a Ladder.
     */
    int end;
}
