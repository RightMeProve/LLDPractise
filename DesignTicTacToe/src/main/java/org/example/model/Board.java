package org.example.model;

/**
 * Represents the playing area for the TicTacToe game.
 * The board is a square grid of a specified size (e.g., 3x3).
 */
public class Board {
    private int size;
    // 2D Array representing the grid where pieces will be placed.
    // Null indicates an empty cell.
    private PlayingPiece[][] board;

    /**
     * Initializes a new empty board of the given size.
     * 
     * @param size The grid size (e.g., 3 for a standard 3x3 game).
     */
    public Board(int size) {
        this.size = size;
        board = new PlayingPiece[size][size];
    }

    /**
     * Attempts to place a playing piece on the board at the specified coordinates.
     * 
     * @param row          The row index (0-based).
     * @param column       The column index (0-based).
     * @param playingPiece The piece to place.
     * @return true if the piece was placed successfully, false if the cell is
     *         already occupied.
     */
    public boolean addPiece(int row, int column, PlayingPiece playingPiece) {
        // Check if the target cell is already occupied.
        if (board[row][column] != null) {
            return false;
        }
        // Place the piece.
        board[row][column] = playingPiece;
        return true;
    }

    /**
     * Checks if there are any unoccupied cells left on the board.
     * 
     * @return true if at least one cell is empty, false otherwise.
     */
    public boolean checkFreeCells() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Renders the current state of the board to standard output.
     * Prints 'X', 'O', or blank spaces based on the pieces on the grid.
     */
    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != null) {
                    System.out.print(board[i][j].pieceType.name() + " ");
                } else {
                    System.out.print("  "); // Display blank space for empty cells
                }
                System.out.print(" | ");
            }
            System.out.println("\n---------------");
        }
    }

    public int getSize() {
        return size;
    }

    public PlayingPiece[][] getBoard() {
        return board;
    }
}
