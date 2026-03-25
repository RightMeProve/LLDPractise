package org.example.SpotManager;

import org.example.Entity.ParkingSpot;
import org.example.LookupStrategy.ParkingSpotLookupStrategy;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract factory/manager class that manages a list of spots for a specific
 * vehicle type.
 * 
 * Design Details:
 * - Employs Strategy Pattern (ParkingSpotLookupStrategy) to find an empty spot.
 * - Handles concurrency: Multiple entrance gates could try to assign the same
 * parking spot
 * to different vehicles. We use a ReentrantLock to ensure thread-safety when
 * checking
 * and modifying spot availability.
 */
public abstract class ParkingSpotManager {
    protected final List<ParkingSpot> spots; // List of spots managed by this manager
    protected final ParkingSpotLookupStrategy strategy; // Strategy algorithm to find a spot

    // Fair lock ensures the longest waiting thread gets the lock first, reducing
    // starvation
    private final ReentrantLock lock = new ReentrantLock(true);

    protected ParkingSpotManager(List<ParkingSpot> spots, ParkingSpotLookupStrategy strategy) {
        this.spots = spots;
        this.strategy = strategy;
    }

    /**
     * Tries to find and occupy a spot.
     * Synchronized via ReentrantLock to prevent race conditions when multiple cars
     * enter simultaneously.
     */
    public ParkingSpot park() {
        lock.lock(); // Secure the critical section
        try {
            ParkingSpot spot = strategy.selectSpot(spots); // Delegate to strategy to find a spot
            if (spot == null) {
                return null; // No spot available
            }
            spot.occupySpot(); // Mark the found spot as occupied
            return spot;
        } finally {
            lock.unlock(); // Always unlock in a finally block to prevent deadlocks
        }
    }

    /**
     * Releases a previously occupied spot.
     * Also synchronized to prevent race conditions.
     */
    public void unPark(ParkingSpot spot) {
        lock.lock();
        try {
            spot.releaseSpot(); // Mark as free
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks if there's at least one free spot among the managed spots.
     */
    public boolean hasFreeSpot() {
        lock.lock();
        try {
            return spots.stream().anyMatch(ParkingSpot::isSpotFree);
        } finally {
            lock.unlock();
        }
    }

}
