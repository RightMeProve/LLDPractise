package org.example.ParkingLot;

import org.example.Entity.ParkingSpot;
import org.example.Entity.Vehicle;
import org.example.Pricing.CostComputation;
import org.example.Ticket;

import java.util.List;

/**
 * Represents the entire Parking Structure encompassing multiple ParkingLevels.
 * 
 * Design Details:
 * - Iterates through levels to locate a free spot.
 * - Handles the actual creation/release of Tickets.
 */
public class ParkingBuilding {
    private final List<ParkingLevel> parkingLevels;

    // A CostComputation is injected, showing how global parameters can be scoped.
    // However, it's not being heavily used within allocate/release, it might be
    // better
    // suited closer to the ExitGate where prices are actually computed.
    public ParkingBuilding(List<ParkingLevel> parkingLevels, CostComputation costComputation) {
        this.parkingLevels = parkingLevels;
    }

    /**
     * Traverses through all the levels to allocate an available spot to the
     * Vehicle.
     * Generates and returns a Ticket upon successful allocation.
     */
    Ticket allocate(Vehicle vehicle) {
        for (ParkingLevel level : parkingLevels) {
            if (level.hasAvailability(vehicle.getVehicleType())) {
                ParkingSpot spot = level.park(vehicle.getVehicleType());
                if (spot != null) {
                    Ticket ticket = new Ticket(vehicle, level, spot);
                    System.out.println("Parking allocated at level: " + level.getLevelNumber()
                            + " spot: " + spot.getSpotId());

                    return ticket;
                }
            }
        }
        throw new RuntimeException("Parking is Full!"); // Simple error handling. Can make custom Exceptions.
    }

    /**
     * Delegates releasing the spot to the precise level specified in the Ticket.
     */
    void release(Ticket ticket) {
        ticket.getParkingLevel().unPark(ticket.getVehicle().getVehicleType(), ticket.getParkingSpot());
    }

}
