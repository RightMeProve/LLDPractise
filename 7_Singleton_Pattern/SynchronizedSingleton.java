
/**
 * Synchronized Singleton.
 * 
 * NOTE: This implementation IS thread-safe.
 * By adding the 'synchronized' keyword to getInstance(), we force every thread
 * to wait its turn before it can enter the method.
 * 
 * DISADVANTAGE: Synchronization is expensive. It decreases performance by a
 * factor of 100.
 * The only time synchronization is relevant is the first time. Once we've set
 * the
 * uniqueInstance variable, we have no further need to synchronize this method.
 */
public class SynchronizedSingleton {
    private static SynchronizedSingleton uniqueInstance;

    private SynchronizedSingleton() {
        System.out.println("SynchronizedSingleton: Instance created.");
    }

    public static synchronized SynchronizedSingleton getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SynchronizedSingleton();
        }
        return uniqueInstance;
    }
}
