package org.example.ParkingLot;

import org.example.Payment.Payment;
import org.example.Pricing.CostComputation;
import org.example.Ticket;

/**
 * Represents the Exit point of the Parking Lot.
 * Responsibilities:
 * 1. Compute the cost of parking.
 * 2. Process the payment.
 * 3. Release the parking spot if payment is successful.
 */
public class ExitGate {
    private final CostComputation costComputation; // Strategy context injected

    public ExitGate(CostComputation costComputation) {
        this.costComputation = costComputation;
    }

    /**
     * Handles the entire exit workflow.
     * Uses the Payment Strategy provided by the client to finalize the transaction.
     */
    public void completeExit(ParkingBuilding building, Ticket ticket, Payment payment) {
        double amount = calculatePrice(ticket);
        boolean isSuccess = payment.pay(amount); // Strategy pattern for payment
        if (!isSuccess) {
            throw new RuntimeException("Payment Failed. Exit Denied!");
        }
        building.release(ticket); // Free up the spot only after successful payment
        System.out.println("Exit successfully!");
    }

    private double calculatePrice(Ticket ticket) {
        return costComputation.compute(ticket);
    }
}
