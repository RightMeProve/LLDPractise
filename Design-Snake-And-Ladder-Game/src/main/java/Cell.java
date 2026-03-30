/**
 * Represents a single cell/square on the Snake and Ladder board.
 * A cell may or may not contain a 'Jump' (either a Snake or a Ladder).
 */
public class Cell {
    /**
     * The Jump object residing on this cell.
     * Null if the cell is a normal square.
     */
    Jump jump;
}
