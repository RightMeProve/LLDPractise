package org.example.ParkingLot;

import org.example.Entity.Vehicle;
import org.example.Ticket;

/**
 * Represents the Entry point of the Parking Lot.
 * Responsibilities: Request ticket generation and spot allocation from the
 * building.
 */
public class EntranceGate {
    public Ticket enter(ParkingBuilding parkingBuilding, Vehicle vehicle) {
        return parkingBuilding.allocate(vehicle); // Delegates to the building hierarchy
    }
}
