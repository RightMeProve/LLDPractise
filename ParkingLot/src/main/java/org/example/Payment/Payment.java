package org.example.Payment;

/**
 * Strategy Pattern Interface for Payments.
 * Decouples the ExitGate from specific payment methods (Cash, UPI, Card).
 * Applying the Open-Closed Principle (OCP) makes it easy to add new methods.
 */
public interface Payment {
    boolean pay(double amount);
}
