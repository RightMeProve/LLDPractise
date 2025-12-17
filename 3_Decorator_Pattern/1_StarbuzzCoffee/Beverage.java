public abstract class Beverage {
    // This is the abstract 'Component' class.
    // It defines the interface that both concrete components (Espresso)
    // and decorators (Mocha) must strictly follow.

    public String description = "Unknown Beverage";

    public String getDescription() {
        return description;
    }

    // The cost method is abstract. Subclasses MUST define their own cost.
    public abstract double cost();
}