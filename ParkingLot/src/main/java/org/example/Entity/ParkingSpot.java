package org.example.Entity;

/**
 * Represents a single parking spot in the Parking Lot.
 * This is a core entity that holds the state of a spot (free or occupied).
 * 
 * Improvements:
 * - We can add 'SpotType' (e.g., COMPACT, LARGE, HANDICAPPED) if we want
 * more granular control over which vehicle parks where.
 * - We could add 'distanceFromElevator' to support a Nearest-First allocation
 * strategy.
 */
public class ParkingSpot {
    private final String spotId; // Unique identifier for the spot (e.g., "L1-S1")
    private boolean isFree = true; // State of the spot

    public ParkingSpot(String spotId) {
        this.spotId = spotId;
    }

    public boolean isSpotFree() {
        return isFree;
    }

    /**
     * Marks the spot as occupied.
     * Note: Thread-safety for this operation is handled by the ParkingSpotManager.
     */
    public void occupySpot() {
        isFree = false;
    }

    /**
     * Frees up the spot for the next vehicle.
     */
    public void releaseSpot() {
        isFree = true;
    }

    public String getSpotId() {
        return spotId;
    }
}
