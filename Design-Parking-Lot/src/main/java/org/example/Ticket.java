package org.example;

import org.example.Entity.ParkingSpot;
import org.example.Entity.Vehicle;
import org.example.ParkingLot.ParkingLevel;

import java.time.LocalDateTime;

/**
 * Represents a parking ticket issued to a vehicle upon entry.
 * This object is immutable (fields are final) after creation which is a good
 * practice
 * for data-transfer-like objects that shouldn't change state during holding.
 */
public class Ticket {
    private final Vehicle vehicle;
    private final ParkingLevel parkingLevel; // Tracks which level the vehicle is parked on
    private final ParkingSpot parkingSpot; // Tracks the exact spot
    private final LocalDateTime entryTime; // Important for calculating parking duration and cost

    /**
     * Constructs a new Ticket capturing the exact state at entry time.
     */
    public Ticket(Vehicle vehicle, ParkingLevel parkingLevel, ParkingSpot parkingSpot) {
        this.vehicle = vehicle;
        this.parkingLevel = parkingLevel;
        this.parkingSpot = parkingSpot;
        this.entryTime = LocalDateTime.now(); // Captures the exact moment the ticket is created
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingLevel getParkingLevel() {
        return parkingLevel;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}
