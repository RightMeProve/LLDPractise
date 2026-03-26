package product;

import reservation.Reservation;
import reservation.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Manages the inventory of vehicles for a specific store.
 * Handles concurrency using ReentrantLocks to prevent double-booking 
 * of the same vehicle across concurrent user requests.
 * Internally maintains mappings of Vehicle ID to Vehicle object,
 * and Vehicle ID to a list of Reservation IDs.
 */
public class VehicleInventoryManager {

    private final ConcurrentMap<Integer, Vehicle> vehicles = new ConcurrentHashMap<>();

    private final ConcurrentMap<Integer, List<Integer>> vehicleBookingIds = new ConcurrentHashMap<>();

    private final ConcurrentMap<Integer, ReentrantLock> vehicleLocks = new ConcurrentHashMap<>();

    private ReservationRepository reservationRepository;

    public void addVehicle(Vehicle vehicle) {
        vehicles.put(vehicle.getVehicleID(), vehicle);
    }

    public Optional<Vehicle> getVehicle(int vehicleId) {
        return Optional.ofNullable(vehicles.get(vehicleId));
    }

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    private ReentrantLock lockForVehicle(int vehicleId) {
        vehicleLocks.putIfAbsent(vehicleId, new ReentrantLock());
        return vehicleLocks.get(vehicleId);
    }

    /**
     * Checks if a vehicle is available for a specified date range.
     * Iterates through all existing reservations for the vehicle to ensure
     * there are no overlapping dates with the requested interval.
     */
    public boolean isAvailable(int vehicleId, LocalDate from, LocalDate to) {
        Vehicle vehicle = vehicles.get(vehicleId);

        if (vehicle == null) return false;
        if (vehicle.getVehicleStatus() == VehicleStatus.MAINTENANCE) return false;

        DateInterval requested = new DateInterval(from, to);

        List<Integer> reservationIDs = vehicleBookingIds.get(vehicleId);
        if(reservationIDs == null || reservationIDs.isEmpty()) {
            return true;
        }
        for (int reservationID : reservationIDs) {
            Reservation reservation = reservationRepository.findById(reservationID).get();
            LocalDate bookedFrom = reservation.getDateBookedFrom();
            LocalDate bookedTill = reservation.getDateBookedTo();
            DateInterval bookedInterval = new DateInterval(bookedFrom, bookedTill);
            if (bookedInterval.overlaps(requested)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Thread-safe method to reserve a vehicle.
     * Uses ReentrantLock per vehicle to ensure atomic check-and-reserve.
     */
    public boolean reserve(int vehicleId, int reservationId, LocalDate from, LocalDate to) {
        ReentrantLock lock = lockForVehicle(vehicleId);
        lock.lock();
        try {
            if (!isAvailable(vehicleId, from, to)) {
                return false;
            }

            vehicleBookingIds.putIfAbsent(vehicleId, new ArrayList<>());
            vehicleBookingIds.get(vehicleId).add(reservationId);

            vehicles.get(vehicleId).setStatus(VehicleStatus.BOOKED);

            return true;
        } finally {
            lock.unlock();
        }
    }

    public void release(int vehicleId, int reservationId) {

        ReentrantLock lock = lockForVehicle(vehicleId);
        lock.lock();

        try {
            // remove reservation Id
            List<Integer> ids = vehicleBookingIds.get(vehicleId);
            if (ids != null) {
                ids.remove(Integer.valueOf(reservationId));
            }

            // if no more bookings → available
            List<Integer> stillBooked = vehicleBookingIds.get(vehicleId);
            if (stillBooked == null || stillBooked.isEmpty()) {
                vehicles.get(vehicleId).setStatus(VehicleStatus.AVAILABLE);
            }

        } finally {
            lock.unlock();
        }
    }

    public List<Vehicle> getAvailableVehicles(
            VehicleType type,
            LocalDate from,
            LocalDate to
    ) {
        return vehicles.values()
                .stream()
                .filter(v -> v.getVehicleType() == type)
                .filter(v -> isAvailable(v.getVehicleID(), from, to))
                .collect(Collectors.toList());
    }

}
