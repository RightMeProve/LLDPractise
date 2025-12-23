
/**
 * Classic Singleton (Lazy Initialization).
 * 
 * NOTE: This implementation is NOT thread-safe.
 * It works fine in a single-threaded environment but can cause issues
 * in multi-threaded environments where multiple threads might enter
 * the 'if (uniqueInstance == null)' block simultaneously.
 */
public class ClassicSingleton {
    private static ClassicSingleton uniqueInstance;

    private ClassicSingleton() {
        // Private constructor so no one else can instantiate this class
        System.out.println("ClassicSingleton: Instance created.");
    }

    public static ClassicSingleton getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ClassicSingleton();
        }
        return uniqueInstance;
    }
}
