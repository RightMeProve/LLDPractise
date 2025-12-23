
/**
 * Eager Initialization Singleton.
 * 
 * NOTE: This implementation IS thread-safe.
 * If your application always creates and uses an instance of the Singleton,
 * or the overhead of creation and runtime aspects of the Singleton are not
 * onerous,
 * you may want to create your Singleton eagerly.
 * 
 * We create the instance when the class is loaded. The JVM guarantees that the
 * instance will be created before any thread accesses the static uniqueInstance
 * variable.
 */
public class EagerSingleton {
    // Usage of static variable ensures instance is created at class loading time
    private static EagerSingleton uniqueInstance = new EagerSingleton();

    private EagerSingleton() {
        System.out.println("EagerSingleton: Instance created.");
    }

    public static EagerSingleton getInstance() {
        return uniqueInstance;
    }
}
