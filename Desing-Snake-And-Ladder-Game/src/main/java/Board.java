import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the playing board of the Snake and Ladder game.
 * It manages the grid of cells and the placement of snakes and ladders.
 */
public class Board {
    /**
     * 2D array matrix holding all the cells on the board.
     */
    Cell[][] cells;

    /**
     * Constructs and initializes the board with cells, snakes, and ladders.
     *
     * @param boardSize    The size of the N x N board (e.g., 10 creates a 10x10 board with 100 cells).
     * @param numOfSnakes  The number of snakes to place on the board.
     * @param numOfLadders The number of ladders to place on the board.
     */
    Board(int boardSize, int numOfSnakes, int numOfLadders){
        initalizeCells(boardSize);
        addSnakeLadders(cells, numOfSnakes, numOfLadders);
    }

    /**
     * Randomly populates the board with the given number of snakes and ladders.
     * Validates that snakes strictly go down, ladders go up, and no cell contains more than one jump.
     */
    private void addSnakeLadders(Cell[][] cells, int numOfSnakes, int numOfLadders) {
        // Place Snakes
        while(numOfSnakes > 0){
            // Bound random between 1 and board.length^2 - 1 (avoid 0 and the final winning cell)
            int snakeHead = ThreadLocalRandom.current().nextInt(1, cells.length * cells.length - 1);
            int snakeTail = ThreadLocalRandom.current().nextInt(1, cells.length * cells.length - 1);
            
            // Validation: Snake head must be strictly greater than tail to go down
            if(snakeHead <= snakeTail){
                continue;
            }

            Cell cell = getCell(snakeHead);
            // BUG FIX / Validation: Ensure cell does not already contain a snake or ladder
            if(cell.jump != null) {
                continue;
            }

            Jump snakeObj = new Jump();
            snakeObj.start = snakeHead;
            snakeObj.end = snakeTail;

            cell.jump = snakeObj;
            numOfSnakes--;
        }

        // Place Ladders
        while(numOfLadders > 0){
            int ladderStart = ThreadLocalRandom.current().nextInt(1, cells.length * cells.length - 1);
            int ladderEnd = ThreadLocalRandom.current().nextInt(1, cells.length * cells.length - 1);
            
            // Validation: Ladder start must be strictly less than end to go up
            if (ladderStart >= ladderEnd) {
                continue;
            }

            Cell cell = getCell(ladderStart);
            // BUG FIX / Validation: Ensure cell does not already contain a snake or ladder
            if(cell.jump != null) {
                continue;
            }

            Jump ladderObj = new Jump();
            ladderObj.start = ladderStart;
            ladderObj.end = ladderEnd;

            cell.jump = ladderObj;
            numOfLadders--;
        }
    }

    /**
     * Helper method to map a linear position (e.g., cell 45) to a 2D matrix location.
     *
     * @param playerPosition The 1D linear position.
     * @return The specific Cell object at that location.
     */
    public Cell getCell(int playerPosition) {
        int boardRow = playerPosition / cells.length;
        int boardColumn = (playerPosition % cells.length);
        return cells[boardRow][boardColumn];
    }

    /**
     * Initializes the 2D matrix of cells as empty squares.
     */
    private void initalizeCells(int boardSize){
        cells = new Cell[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                cells[i][j] = new Cell();
            }
        }
    }
}
