public class CheesePizza extends Pizza {
    PizzaIngredientFactory ingredientFactory;

    /**
     * Constructor injection of the factory.
     * The pizza class is decoupled from the concrete ingredients;
     * it only knows about the factory interface.
     */
    public CheesePizza(PizzaIngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }

    /**
     * Prepare the pizza logic.
     * "programming to an interface" - we ask the factory for
     * dough, sauce, and cheese, without caring which specific
     * types we get (that mimics the decoupling).
     */
    void prepare() {
        System.out.println("Preparing " + name);
        dough = ingredientFactory.createDough();
        sauce = ingredientFactory.createSauce();
        cheese = ingredientFactory.createCheese();
    }
}
