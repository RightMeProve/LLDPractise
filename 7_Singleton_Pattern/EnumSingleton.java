
/**
 * Enum Singleton.
 * 
 * NOTE: This is considered the BEST way to implement a Singleton in Java.
 * - It is thread-safe.
 * - It provides serialization machinery for free.
 * - It prevents multiple instantiation even in the face of complex
 * serialization or reflection attacks.
 */
public enum EnumSingleton {
    UNIQUE_INSTANCE;

    // You can add fields and methods here just like a normal class
    public void performAction() {
        System.out.println("EnumSingleton: Action performed.");
    }
}
