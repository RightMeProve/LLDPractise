package org.example.Pricing;

import org.example.Ticket;

/**
 * Concrete Implementation of PricingStrategy.
 * 
 * Provides a flat rate regardless of duration or vehicle type.
 * In advanced implementations, this would inspect both Ticket.entryTime and
 * VehicleType.
 */
public class FixedPricingStrategy implements PricingStrategy {
    @Override
    public double calculate(Ticket ticket) {
        return 100; // Flat fee
    }
}
