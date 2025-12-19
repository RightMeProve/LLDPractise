/**
 * PizzaStore acting as the Abstract Creator in the Factory Method Pattern.
 * 
 * It defines the abstract factory method createPizza() that subclasses must
 * implement.
 * It also contains code (orderPizza) that depends on the abstract Product
 * (Pizza),
 * which is produced by the factory method. This ensures the high-level code
 * (PizzaStore)
 * is decoupled from the low-level code (Concrete Pizzas).
 */
public abstract class PizzaStore {

    /**
     * Orders a pizza. This method is the "client" of the Factory Method.
     * It relies on createPizza() to get the Pizza object, but doesn't know
     * which concrete Pizza class is actually created.
     */
    public Pizza orderPizza(String type) {
        Pizza pizza;

        // Call the factory method to create the pizza
        pizza = createPizza(type);

        // Operations on the product
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();

        return pizza;
    }

    /**
     * The Factory Method.
     * Isolated method to create the object.
     * Each subclass implements this to create the appropriate concrete product.
     */
    protected abstract Pizza createPizza(String type);
}
