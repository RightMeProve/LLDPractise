package org.example.SpotManager;

import org.example.Entity.ParkingSpot;
import org.example.LookupStrategy.ParkingSpotLookupStrategy;

import java.util.List;

/**
 * Concrete Manager for Four-Wheeler spots.
 * 
 * Inherits all concurrency and strategy logic from ParkingSpotManager.
 * Segregating by vehicle type allows us to easily scale (e.g., adding
 * HeavyVehicleSpotManager later).
 */
public class FourWheelerSpotManager extends ParkingSpotManager {
    public FourWheelerSpotManager(List<ParkingSpot> spots, ParkingSpotLookupStrategy strategy) {
        super(spots, strategy);
    }
}
