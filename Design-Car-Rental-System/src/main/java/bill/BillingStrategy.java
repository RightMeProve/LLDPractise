package bill;

import reservation.Reservation;

public interface BillingStrategy {
    Bill generateBill(Reservation reservation);
}
