public class Mocha extends CondimentDecorator {
    // COMPOSITION: We hold a reference to the specific object we are wrapping.
    // This could be an Espresso, or it could be ANOTHER Decorator (like another
    // Mocha).
    Beverage beverage;

    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription() + ", Mocha";
    }

    // DELEGATION:
    // First, we delegate the call to the object we are decorating (beverage.cost)
    // Then, we add our own small part of the behavior logic (+ 0.20)
    public double cost() {
        return 0.20 + beverage.cost();
    }
}
