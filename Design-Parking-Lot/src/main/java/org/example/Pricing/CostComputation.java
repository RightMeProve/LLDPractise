package org.example.Pricing;

import org.example.Ticket;

/**
 * A Context/Wrapper class that holds a reference to the PricingStrategy.
 * This class applies Dependency Injection via its constructor.
 * It strictly follows the Single Responsibility Principle by simply dispatching
 * the work to the underlying strategy.
 */
public class CostComputation {

    private final PricingStrategy pricingStrategy;

    public CostComputation(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double compute(Ticket ticket) {
        return pricingStrategy.calculate(ticket);
    }
}
