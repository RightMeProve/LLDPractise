/**
 * Abstract Factory Interface.
 * 
 * Defines the interface for creating families of related products (ingredients)
 * without specifying their concrete classes.
 */
public interface PizzaIngredientFactory {
    public Dough createDough();

    public Sauce createSauce();

    public Cheese createCheese();

    public Veggies[] createVeggies();

    public Pepperoni createPepperoni();

    public Clams createClam();
}
