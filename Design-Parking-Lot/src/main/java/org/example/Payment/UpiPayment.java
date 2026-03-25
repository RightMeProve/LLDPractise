package org.example.Payment;

/**
 * Concrete implementation of the Payment strategy for UPI.
 */
public class UpiPayment implements Payment {
    @Override
    public boolean pay(double amount) {
        System.out.println("Paid through upi: " + amount);
        return true; // Simplified for the purpose of LLD demonstration
    }
}
