package reservation;

import java.time.LocalDate;
import java.time.temporal.Temporal;

/**
 * Represents a reservation of a vehicle by a user for a specific date range.
 * Tracks the booking lifecycle status from SCHEDULED to IN_USE to COMPLETED or CANCELLED.
 */
public class Reservation {

    private final int reservationId;
    private final int vehicleId;
    private final int userId;
    private final LocalDate dateBookedFrom;
    private final LocalDate dateBookedTo;
    private final ReservationType reservationType;
    private ReservationStatus reservationStatus;

    public Reservation(int reservationId,
                       int vehicleId,
                       int userId,
                       LocalDate dateBookedFrom,
                       LocalDate dateBookedTo,
                       ReservationType reservationType) {

        this.reservationId = reservationId;
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.dateBookedFrom = dateBookedFrom;
        this.dateBookedTo = dateBookedTo;
        this.reservationType = reservationType;
        this.reservationStatus = ReservationStatus.SCHEDULED;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDate getDateBookedFrom() {
        return dateBookedFrom;
    }

    public LocalDate getDateBookedTo() {
        return dateBookedTo;
    }

    public ReservationType getReservationType() {
        return reservationType;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
