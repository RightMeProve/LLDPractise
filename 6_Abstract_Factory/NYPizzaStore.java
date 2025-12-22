public class NYPizzaStore extends PizzaStore {

    protected Pizza createPizza(String item) {
        Pizza pizza = null;
        // Create the NY specific ingredient factory
        // This factory knows how to create Thin Crust Dough, Marinara Sauce, etc.
        PizzaIngredientFactory ingredientFactory = new NYPizzaIngredientFactory();

        if (item.equals("cheese")) {

            // Pass the factory to the pizza so it can use it to create ingredients
            pizza = new CheesePizza(ingredientFactory);
            pizza.setName("New York Style Cheese Pizza");

        } else if (item.equals("veggie")) {

            // pizza = new VeggiePizza(ingredientFactory);
            // pizza.setName("New York Style Veggie Pizza");

        } else if (item.equals("clam")) {

            pizza = new ClamPizza(ingredientFactory);
            pizza.setName("New York Style Clam Pizza");

        } else if (item.equals("pepperoni")) {

            // pizza = new PepperoniPizza(ingredientFactory);
            // pizza.setName("New York Style Pepperoni Pizza");

        }
        return pizza;
    }
}
