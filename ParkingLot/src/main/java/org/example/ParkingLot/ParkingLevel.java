package org.example.ParkingLot;

import org.example.Entity.ParkingSpot;
import org.example.Enum.VehicleType;
import org.example.SpotManager.ParkingSpotManager;

import java.util.Map;

/**
 * Represents a single floor/level in the Parking Building.
 * Holds multiple Spot Managers, segregated by VehicleType.
 * 
 * Design Details:
 * - Uses a Map<VehicleType, ParkingSpotManager> for quick O(1) lookup of the
 * correct manager.
 * - This level acts as a Facade or an intermediator to its SpotManagers.
 */
public class ParkingLevel {

    private final int levelNumber; // E.g., Level 1, Level 2
    private final Map<VehicleType, ParkingSpotManager> managers;

    public ParkingLevel(Integer levelNumber, Map<VehicleType, ParkingSpotManager> managers) {
        this.levelNumber = levelNumber;
        this.managers = managers;
    }

    /**
     * Checks if this level has an available spot for the given vehicle type.
     */
    public boolean hasAvailability(VehicleType type) {
        ParkingSpotManager manager = managers.get(type);
        return manager != null && manager.hasFreeSpot();
    }

    /**
     * Attempts to park the vehicle on this level by delegating to the appropriate
     * manager.
     */
    public ParkingSpot park(VehicleType type) {
        ParkingSpotManager manager = managers.get(type);
        if (manager == null) {
            throw new IllegalArgumentException(
                    "No parking Manager for Vehicle Type: " + type);
        }
        return manager.park();
    }

    /**
     * Unparks the vehicle by delegating to the appropriate manager.
     */
    public void unPark(VehicleType type, ParkingSpot spot) {
        ParkingSpotManager manager = managers.get(type);
        if (manager == null) {
            throw new IllegalArgumentException(
                    "No parking Manager for Vehicle Type: " + type);
        }
        manager.unPark(spot);
    }

    public int getLevelNumber() {
        return levelNumber;
    }
}
