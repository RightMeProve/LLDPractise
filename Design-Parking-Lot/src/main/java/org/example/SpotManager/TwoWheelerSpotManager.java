package org.example.SpotManager;

import org.example.Entity.ParkingSpot;
import org.example.LookupStrategy.ParkingSpotLookupStrategy;

import java.util.List;

/**
 * Concrete Manager for Two-Wheeler spots.
 * 
 * In a real application, this could have specific logic for bikes,
 * or distinct pricing multipliers compared to cars.
 * Inherits the locking and generic parking logic from ParkingSpotManager.
 */
public class TwoWheelerSpotManager extends ParkingSpotManager {
    public TwoWheelerSpotManager(List<ParkingSpot> spots, ParkingSpotLookupStrategy strategy) {
        super(spots, strategy);
    }
}
