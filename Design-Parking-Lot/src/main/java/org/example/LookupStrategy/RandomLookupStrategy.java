package org.example.LookupStrategy;

import org.example.Entity.ParkingSpot;

import java.util.List;

/**
 * A concrete implementation of the Strategy pattern for finding a parking spot.
 * It simply iterates through the list and returns the first free spot it
 * encounters.
 */
public class RandomLookupStrategy implements ParkingSpotLookupStrategy {

    @Override
    public ParkingSpot selectSpot(List<ParkingSpot> spots) {
        for (ParkingSpot spot : spots) {
            if (spot.isSpotFree()) {
                // We found our first available empty spot
                return spot;
            }
        }
        // There is no empty spot available
        return null;
    }
}
