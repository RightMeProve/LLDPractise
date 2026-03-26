package product;

/**
 * Represents a Vehicle available for rent in the system.
 * Contains base properties like ID, type, pricing, and current status.
 */
public class Vehicle {

    private final int vehicleID;
    private final String vehicleNumber;
    private final VehicleType vehicleType;
    private double dailyRentalCost;
    private volatile VehicleStatus vehicleStatus;

    // --------- Constructors ---------
    public Vehicle(int vehicleID, String vehicleNumber, VehicleType vehicleType) {
        this.vehicleID = vehicleID;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.vehicleStatus = VehicleStatus.AVAILABLE;
    }


    // --------- Getters ---------

    public int getVehicleID() {
        return vehicleID;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public VehicleStatus getVehicleStatus() {
        return vehicleStatus;
    }

    public double getDailyRentalCost() {
        return dailyRentalCost;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    // --------- Setters ---------

    public void setDailyRentalCost(double dailyRentalCost) {
        this.dailyRentalCost = dailyRentalCost;
    }


    public void setStatus(VehicleStatus vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }
}
