package org.example;

import org.example.model.GameStatus;

/**
 * Entry point for the Application.
 * Bootstraps the game and handles the final result output.
 */
public class PlayGame {
    public static void main(String[] args) {
        System.out.println("\n>>>>>> Welcome to TicTacToe Game <<<<<<<\n");

        // Initialize the game engine
        TicTacToe game = new TicTacToe();
        game.initializeGame();

        // Start the game loop and wait for the result
        GameStatus status = game.startGame();

        System.out.println("\n>>>>>>> Game Over <<<<<<<<<\n");
        switch (status) {
            case WIN:
                // If WIN, print the winner's name
                System.out.println(game.winner.getName() + " won the game");
                break;
            case DRAW:
                // If DRAW, print a tie message
                System.out.println("It's a Draw!");
                break;
            default:
                System.out.println("Game Ends!");
                break;
        }

    }
}