/**
 * Concrete Creator - ChicagoPizzaStore
 * 
 * Implements the factory method createPizza to produce
 * products (Pizzas) specific to the Chicago style.
 */
public class ChicagoPizzaStore extends PizzaStore {

    /**
     * The implementation of the factory method.
     * Based on the type, it instantiates the correct concrete Pizza object.
     */
    @Override
    protected Pizza createPizza(String type) {
        if (type.equals("cheese")) {
            return new ChicagoStyleCheesePizza();
        }
        return null;
    }
}
