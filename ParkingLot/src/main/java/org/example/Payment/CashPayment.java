package org.example.Payment;

/**
 * Concrete implementation of the Payment strategy.
 */
public class CashPayment implements Payment {
    @Override
    public boolean pay(double amount) {
        System.out.println("Cash Paid: " + amount);
        return true; // Simplistic mock behavior. Real app would handle actual txns here.
    }
}
