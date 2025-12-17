// 1. We extend 'Beverage' so that Decorators can be used WHEREVER a Beverage is expected.
//    (This is 'Type Matching' inheritance, not 'Behavior Inheritance').
public abstract class CondimentDecorator extends Beverage {

    // 2. We make this abstract to FORCE all decorators to re-implement it.
    // We want decorators to modify the description (e.g. "Espresso, Mocha"),
    // not just use the default.
    public abstract String getDescription();

}
