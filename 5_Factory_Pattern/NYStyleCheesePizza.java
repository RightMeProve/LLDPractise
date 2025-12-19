/**
 * Concrete Product - NY Style Cheese Pizza
 * 
 * Extends the Pizza abstract class and provides specific implementation
 * for New York style ingredients and behavior.
 */
public class NYStyleCheesePizza extends Pizza {

    public NYStyleCheesePizza() {
        name = "NY Style Sauce and Cheese Pizza";
        dough = "Thin Crust Dough";
        sauce = "Marinara Sauce";
        toppings.add("Grated Reggiano Cheese");
    }
}