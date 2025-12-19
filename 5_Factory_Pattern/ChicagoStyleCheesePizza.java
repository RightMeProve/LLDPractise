/**
 * Concrete Product - Chicago Style Cheese Pizza
 * 
 * Extends the Pizza abstract class and provides specific implementation
 * for Chicago style ingredients. It also overrides the cut() method.
 */
public class ChicagoStyleCheesePizza extends Pizza {

    public ChicagoStyleCheesePizza() {
        name = "Chicago Style Deep Dish Cheese Pizza";
        dough = "Extra Thick Crust Dough";
        sauce = "Plum Tomato Sauce";
        toppings.add("Shredded Mozzarella Cheese");
    }

    /**
     * Overrides the default cut() method because Chicago style pizza
     * is typically cut into squares, not slices.
     */
    @Override
    void cut() {
        System.out.println("Cutting the pizza into square slices");
    }
}
