package org.example.LookupStrategy;

import org.example.Entity.ParkingSpot;

import java.util.List;

/**
 * Strategy Pattern Interface for finding an available parking spot.
 * 
 * By using this interface, we can change the algorithm used to find a spot
 * (e.g., Random, Nearest-To-Elevator, specific wing) without altering the
 * SpotManager code.
 * This adheres to the Open-Closed Principle (OCP).
 */
public interface ParkingSpotLookupStrategy {
    ParkingSpot selectSpot(List<ParkingSpot> spots);
}
