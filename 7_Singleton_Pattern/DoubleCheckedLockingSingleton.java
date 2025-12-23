
/**
 * Double-Checked Locking Singleton.
 * 
 * NOTE: This implementation IS thread-safe and performant.
 * With double-checked locking, we first check to see if an instance is created,
 * and if not, THEN we synchronize. This way, we only synchronize the first
 * time.
 * 
 * The 'volatile' keyword ensures that multiple threads handle the
 * uniqueInstance variable
 * correctly when it is being initialized to the instance.
 */
public class DoubleCheckedLockingSingleton {
    // volatile is crucial here for thread safety
    private volatile static DoubleCheckedLockingSingleton uniqueInstance;

    private DoubleCheckedLockingSingleton() {
        System.out.println("DoubleCheckedLockingSingleton: Instance created.");
    }

    public static DoubleCheckedLockingSingleton getInstance() {
        if (uniqueInstance == null) {
            synchronized (DoubleCheckedLockingSingleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return uniqueInstance;
    }
}
