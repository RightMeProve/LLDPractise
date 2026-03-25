package org.example.Entity;

import org.example.Enum.VehicleType;

/**
 * Represents a Vehicle that wants to park in the Parking Lot.
 * This is a basic entity class.
 * 
 * Improvements:
 * - We could abstract this into a 'Vehicle' interface or abstract class and
 * have specific implementations like 'Car', 'Bike', 'Truck' if there are
 * vehicle-specific behaviors needed in the future.
 */
public class Vehicle {
    private String vehicleNumber; // License plate number, acts as a unique identifier for the vehicle
    private VehicleType vehicleType; // Determines which spot manager handles it

    public Vehicle(String vehicleNumber, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

}
