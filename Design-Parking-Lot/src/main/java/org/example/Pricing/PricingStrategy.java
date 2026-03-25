package org.example.Pricing;

import org.example.Ticket;

/**
 * Strategy pattern interface for calculating parking fees.
 * By isolating it, we can have various strategies like:
 * - FixedPricingStrategy (e.g., $10 flat rate)
 * - HourlyPricingStrategy (e.g., $5/hour calculated using entry time from
 * Ticket)
 * - DynamicPricingStrategy (e.g., Surge pricing based on demand)
 */
public interface PricingStrategy {
    double calculate(Ticket ticket);
}
