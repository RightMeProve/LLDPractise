package reservation;
import user.User;


import product.VehicleInventoryManager;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles the creation, tracking, and lifecycle updates of reservations.
 * Works closely with VehicleInventoryManager to ensure vehicles are uniquely blocked.
 */
public class ReservationManager {

    private final VehicleInventoryManager inventory;
    private final ReservationRepository reservationRepository;

    // Use AtomicInteger to generate thread-safe unique reservation IDs
    private final AtomicInteger reservationIdGenerator = new AtomicInteger(20000);

    public ReservationManager(VehicleInventoryManager inventory) {
        this.inventory = inventory;
        this.reservationRepository = new ReservationRepository();
        this.inventory.setReservationRepository(this.reservationRepository);
    }

    public Optional<Reservation> findByID(int reservationId) {
        return reservationRepository.findById(reservationId);
    }

    // ----------------- Create Reservation -----------------

    /**
     * Attempts to create a reservation.
     * If the vehicle isn't available for the requested dates, throws an Exception.
     */
    public Reservation createReservation(int vehicleId, User user,
                                         LocalDate from,
                                         LocalDate to,
                                         ReservationType type) throws Exception {

        int reservationId = reservationIdGenerator.getAndIncrement();
        boolean reserved = inventory.reserve(vehicleId, reservationId, from, to);

        if (!reserved) {
            throw new RuntimeException("Vehicle not available for selected dates");
        }

        Reservation reservation = new Reservation(reservationId, vehicleId,
                user.getUserId(), from, to, type);

        reservationRepository.save(reservation);
        return reservation;
    }

    public void cancelReservation(int reservationId) {

        Optional<Reservation> opt = reservationRepository.findById(reservationId);
        if (!opt.isPresent()) {
            throw new RuntimeException("Reservation not found");
        }

        Reservation r = opt.get();
        r.setReservationStatus(ReservationStatus.CANCELLED);

        inventory.release(
                r.getVehicleId(),
                r.getReservationId());

        reservationRepository.remove(reservationId);
    }

    public void startTrip(int reservationId) {
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        r.setReservationStatus(ReservationStatus.IN_USE);
    }

    public void submitVehicle(int reservationId) {

        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        r.setReservationStatus(ReservationStatus.COMPLETED);

        inventory.release(
                r.getVehicleId(),
                r.getReservationId()
        );
    }

    public void remove(int reservationId) {
        reservationRepository.remove(reservationId);
    }

}
