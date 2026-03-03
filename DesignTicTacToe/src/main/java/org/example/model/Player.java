package org.example.model;

/**
 * Represents a player in the game.
 * Each player has a name for display purposes and a dedicated playing piece
 * type (X or O).
 */
public class Player {
    private String name;
    private PlayingPiece playingPiece;

    /**
     * Initializes a new player.
     * 
     * @param name         The display name of the player.
     * @param playingPiece The type of playing piece assigned to this player.
     */
    public Player(String name, PlayingPiece playingPiece) {
        this.name = name;
        this.playingPiece = playingPiece;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayingPiece getPlayingPiece() {
        return playingPiece;
    }

    public void setPlayingPiece(PlayingPiece playingPiece) {
        this.playingPiece = playingPiece;
    }
}
