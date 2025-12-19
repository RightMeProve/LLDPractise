/**
 * Client Code
 * 
 * Demonstrates the Factory Method Pattern.
 * The client code (main method) uses the different Concrete Creator classes
 * (Stores)
 * to order pizzas. Note that the client code deals with the Pizza abstract type
 * for the most part, but chooses which Store to instantiate.
 */
public class PizzaTestDrive {
    public static void main(String[] args) {
        // Instantiate NY Store (Concrete Creator)
        PizzaStore nyStore = new NYPizzaStore();
        // Instantiate Chicago Store (Concrete Creator)
        PizzaStore chicagoStore = new ChicagoPizzaStore();

        // Order a pizza from NY Store
        Pizza pizza = nyStore.orderPizza("cheese");
        System.out.println("Ethan ordered a " + pizza.getName() + "\n");

        // Order a pizza from Chicago Store
        pizza = chicagoStore.orderPizza("cheese");
        System.out.println("Joel ordered a " + pizza.getName() + "\n");
    }
}
