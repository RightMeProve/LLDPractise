package org.example.model;

/**
 * Represents the final state or result of the TicTacToe game.
 * It is used to determine if the game has concluded and the outcome.
 */
public enum GameStatus {
    /**
     * Indicates that all cells on the board are filled and neither player has won.
     */
    DRAW,

    /**
     * Indicates that a player has successfully aligned their pieces to win the
     * game.
     */
    WIN
}
