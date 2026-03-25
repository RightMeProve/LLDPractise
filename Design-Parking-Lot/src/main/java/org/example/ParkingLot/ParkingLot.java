package org.example.ParkingLot;

import org.example.Entity.Vehicle;
import org.example.Payment.Payment;
import org.example.Ticket;

/**
 * Main Facade class representing the Parking Lot system.
 * It encapsulates the interactions between the Gates and the Building.
 * Clients interact with this class rather than dealing with Building or Gates
 * directly.
 */
public class ParkingLot {
    private final ParkingBuilding parkingBuilding;
    private final EntranceGate entranceGate;
    private final ExitGate exitGate;

    public ParkingLot(ParkingBuilding building, EntranceGate entranceGate, ExitGate exitGate) {
        this.parkingBuilding = building;
        this.entranceGate = entranceGate;
        this.exitGate = exitGate;
    }

    public Ticket vehicleArrives(Vehicle vehicle) {
        return entranceGate.enter(parkingBuilding, vehicle);
    }

    public void vehicleExits(Ticket ticket, Payment payment) {
        exitGate.completeExit(parkingBuilding, ticket, payment);
    }
}
