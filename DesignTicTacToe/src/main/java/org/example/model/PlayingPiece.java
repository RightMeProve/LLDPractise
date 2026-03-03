package org.example.model;

/**
 * Base class representing a playing piece on the board.
 * Extended by specific piece classes (e.g., PlayingPieceX, PlayingPieceO).
 */
public class PlayingPiece {
    public PieceType pieceType;

    /**
     * Constructor setting the specific piece type.
     * 
     * @param pieceType The type (X or O) to assign to this playing piece.
     */
    PlayingPiece(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    // Default constructor for flexibility
    public PlayingPiece() {
    }
}
