/**
 * Abstract Product.
 * 
 * Defines the product (Pizza) which utilizes the ingredients.
 * The implementation of how ingredients are gathered is delegated to the
 * subclasses'
 * implementation of the prepare() method.
 */
public abstract class Pizza {
    String name;

    Dough dough;
    Sauce sauce;
    Veggies veggies[];
    Cheese cheese;
    Pepperoni pepperoni;
    Clams clam;

    /**
     * Abstract prepare method.
     * This is where we collect the ingredients needed for the pizza.
     * It will be implemented by concrete Pizza classes using a factory.
     */
    abstract void prepare();

    void bake() {
        System.out.println("Bake for 25 minutes at 350");
    }

    void cut() {
        System.out.println("Cutting the pizza into diagonal slices");
    }

    void box() {
        System.out.println("Place pizza in official PizzaStore box");
    }

    void setName(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    public String toString() {
        // Use StringBuilder for better performance
        StringBuilder display = new StringBuilder();
        display.append("---- " + name + " ----\n");
        if (dough != null) {
            display.append(dough + "\n");
        }
        if (sauce != null) {
            display.append(sauce + "\n");
        }
        if (cheese != null) {
            display.append(cheese + "\n");
        }
        if (veggies != null) {
            for (int i = 0; i < veggies.length; i++) {
                display.append(veggies[i] + "\n");
            }
        }
        if (clam != null) {
            display.append(clam + "\n");
        }
        if (pepperoni != null) {
            display.append(pepperoni + "\n");
        }
        return display.toString();
    }
}
