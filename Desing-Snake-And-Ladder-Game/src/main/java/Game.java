import java.util.Deque;
import java.util.LinkedList;

/**
 * The core engine orchestrating the Snake and Ladder game flow.
 * Manages players' turns, dice rolling, and jump validations.
 */
public class Game {
    Board board;
    Dice dice;
    Deque<Player> playersList = new LinkedList<>();
    Player winner;

    /**
     * Constructs and initializes a new game session.
     */
    public Game() {
        initializeGame();
    }

    /**
     * Sets up the board, dice, and registers the players.
     */
    private void initializeGame() {
        // Creates a 10x10 board with 5 snakes and 4 ladders.
        board = new Board(10, 5, 4);
        // Creates a setup with 1 die.
        dice = new Dice(1);
        winner = null;
        addPlayers();
    }

    /**
     * Adds players to the Double-Ended Queue (Deque) for turn management.
     */
    private void addPlayers() {
        Player player1 = new Player("Player-1", 0);
        Player player2 = new Player("Player-2", 0);
        playersList.add(player1);
        playersList.add(player2);
    }

    /**
     * Starts the main game loop. Continues until a player precisely reaches the final cell.
     */
    public void startGame() {
        // Game loops until a winner is declared
        while(winner == null) {
            // Check whose turn it is
            Player playerTurn = findPlayerTurn();
            System.out.println("Player turn: " + playerTurn.id + " | Current position: " + playerTurn.currentPosition);

            // Roll the dice
            int diceNumbers = dice.rollDice();
            System.out.println("Dice rolled: " + diceNumbers);

            // Calculate new hypothetical position
            int playerNewPosition = playerTurn.currentPosition + diceNumbers;
            
            // Validation: The player must EXACTLY land on the final square to win.
            // If the dice roll exceeds the board size, the turn is skipped.
            if(playerNewPosition > board.cells.length * board.cells.length - 1) {
                System.out.println("Invalid move! " + playerTurn.id + " needs exactly " + 
                    (board.cells.length * board.cells.length - 1 - playerTurn.currentPosition) + " to win.\n");
                continue; 
            }

            // Check if there's a snake/ladder jump at the new position
            playerNewPosition = jumpCheck(playerNewPosition);
            
            // Execute move
            playerTurn.currentPosition = playerNewPosition;
            System.out.println("Player turn: " + playerTurn.id + " | New Position is: " + playerNewPosition + "\n");
            
            // Check for winning condition
            if(playerNewPosition == board.cells.length * board.cells.length - 1) {
                winner = playerTurn;
            }
        }
        System.out.println("===> The Winner is: " + winner.id);
    }

    /**
     * Fetches the next player in the queue and cycles them to the end of the line.
     */
    private Player findPlayerTurn() {
        Player playerTurns = playersList.removeFirst();
        playersList.addLast(playerTurns);
        return playerTurns;
    }

    /**
     * Validates and processes multiple jumps sequentially.
     * Bug Fix: Uses a 'while' loop to handle chained jumps (e.g., a ladder lands exactly on a snake).
     *
     * @param playerNewPosition The intended position before jumps.
     * @return The final position after traversing all consecutive jumps.
     */
    private int jumpCheck(int playerNewPosition) {
        if (playerNewPosition >= board.cells.length * board.cells.length - 1) {
            return playerNewPosition;
        }

        Cell cell = board.getCell(playerNewPosition);
        
        // BUG FIX: While loop added to correctly handle multiple consecutive jumps
        while(cell != null && cell.jump != null && cell.jump.start == playerNewPosition) {
            String jumpBy = (cell.jump.start < cell.jump.end) ? "Ladder" : "Snake";
            System.out.println("[+] Jump triggered by: " + jumpBy + " from " + cell.jump.start + " to " + cell.jump.end);
            
            playerNewPosition = cell.jump.end;
            
            if(playerNewPosition >= board.cells.length * board.cells.length - 1) {
                break;
            }
            
            // Fetch the next cell in case there's another jump mapped to it
            cell = board.getCell(playerNewPosition);
        }
        return playerNewPosition;
    }
}
