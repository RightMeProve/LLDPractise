package org.example;

import org.example.model.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * The core game engine for TicTacToe.
 * Manages players, the game board, and the game loop.
 */
public class TicTacToe {
    // ArrayDeque is used as a Queue to manage turns.
    // The player at the front of the deque takes the next turn.
    Deque<Player> players;
    Board gameBoard;
    Player winner;

    /**
     * Initializes the game by setting up the players and the board.
     * Hardcoded to initialize a 2-player game with a 3x3 board.
     */
    public void initializeGame() {
        players = new ArrayDeque<>();

        // Initialize Player 1 with piece type X
        PlayingPieceX xPiece = new PlayingPieceX();
        Player player1 = new Player("Player1", xPiece);
        players.offerFirst(player1);

        // Initialize Player 2 with piece type O
        PlayingPieceO oPiece = new PlayingPieceO();
        Player player2 = new Player("Player2", oPiece);
        players.offerFirst(player2);

        // Initialize a 3x3 game board
        gameBoard = new Board(3);
    }

    /**
     * Starts the main game loop.
     * Continues until a player wins or the board is completely filled (draw).
     * 
     * @return GameStatus.WIN if a player won, GameStatus.DRAW if it's a tie.
     */
    public GameStatus startGame() {
        boolean noWinner = true;

        // Run until we have a winner or a draw
        while (noWinner) {
            // Get the player whose turn it is currently
            Player currentTurn = players.removeFirst();

            gameBoard.printBoard();

            // If there are no free cells left, and no one has won, it's a draw.
            if (gameBoard.checkFreeCells() == false) {
                noWinner = false;
                continue; // Exit the loop on the next iteration
            }

            // Prompt the user for input
            System.out.print("Player: " + currentTurn.getName() + "- Please enter [row, column]: ");
            Scanner inputScanner = new Scanner(System.in);
            String s = inputScanner.nextLine();
            String[] values = s.split(",");
            int inputRow = Integer.valueOf(values[0]);
            int inputCol = Integer.valueOf(values[1]);

            // Attempt to add the piece to the chosen location
            boolean validMove = gameBoard.addPiece(inputRow, inputCol, currentTurn.getPlayingPiece());
            if (!validMove) {
                // If the cell was occupied, notify the player and let them try again.
                // Put them back at the front of the queue so it remains their turn.
                System.out.println("Incorrect position chosen! Please Try again!");
                players.offerFirst(currentTurn);
                continue;
            }
            // Move was successful, so put the player at the back of the queue.
            players.offerLast(currentTurn);

            // Check if this move won the game
            boolean isWinner = checkForWinner(inputRow, inputCol, currentTurn.getPlayingPiece().pieceType);
            if (isWinner) {
                gameBoard.printBoard();
                winner = currentTurn;
                return GameStatus.WIN;
            }

        }
        return GameStatus.DRAW;
    }

    /**
     * Checks if placing a piece at the specified position resulted in a win.
     * Evaluates the row, column, and both diagonals.
     * 
     * @param row       The row where the piece was just placed.
     * @param col       The column where the piece was just placed.
     * @param pieceType The type of piece (X or O) to check for a win.
     * @return true if the piece type has won after this move.
     */
    public boolean checkForWinner(int row, int col, PieceType pieceType) {
        boolean rowMatch = true;
        boolean colMatch = true;
        boolean diagMatch = true;
        boolean antiDiagMatch = true;

        // Check the entire row for a win
        for (int j = 0; j < gameBoard.getSize(); j++) {
            if (gameBoard.getBoard()[row][j] == null || gameBoard.getBoard()[row][j].pieceType != pieceType) {
                rowMatch = false;
                break;
            }
        }

        // Check the entire column for a win
        for (int i = 0; i < gameBoard.getSize(); i++) {
            if (gameBoard.getBoard()[i][col] == null || gameBoard.getBoard()[i][col].pieceType != pieceType) {
                colMatch = false;
                break;
            }
        }

        // Check the main diagonal (top-left to bottom-right)
        for (int i = 0; i < gameBoard.getSize(); i++) {
            if (gameBoard.getBoard()[i][i] == null || gameBoard.getBoard()[i][i].pieceType != pieceType) {
                diagMatch = false;
                break;
            }
        }

        // Check the anti-diagonal (top-right to bottom-left)
        for (int i = 0, j = gameBoard.getSize() - 1; i < gameBoard.getSize(); i++, j--) {
            if (gameBoard.getBoard()[i][j] == null || gameBoard.getBoard()[i][j].pieceType != pieceType) {
                antiDiagMatch = false;
                break;
            }
        }

        return rowMatch || colMatch || diagMatch || antiDiagMatch;
    }
}
