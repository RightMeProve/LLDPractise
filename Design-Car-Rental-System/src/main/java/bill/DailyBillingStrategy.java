package bill;

import product.Vehicle;
import product.VehicleInventoryManager;
import reservation.Reservation;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DailyBillingStrategy implements BillingStrategy{

    VehicleInventoryManager vehicleInventoryManager;

    private final AtomicInteger billIdGenerator = new AtomicInteger(5000);

    public DailyBillingStrategy(VehicleInventoryManager vehicleInventoryManager){
        this.vehicleInventoryManager = vehicleInventoryManager;
    }

    @Override
    public Bill generateBill(Reservation reservation) {
        long days = ChronoUnit.DAYS.between(
                reservation.getDateBookedFrom(),
                reservation.getDateBookedTo()
        ) + 1;

        Vehicle vehicle = vehicleInventoryManager.getVehicle(reservation.getVehicleId()).get();

        double rate = vehicle.getDailyRentalCost();
        double total = rate * days;

        return new Bill(
                billIdGenerator.getAndIncrement(),
                reservation.getReservationId(),
                total
        );
    }
}
