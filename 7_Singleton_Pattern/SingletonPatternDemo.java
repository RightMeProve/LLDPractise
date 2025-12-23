
public class SingletonPatternDemo {
    public static void main(String[] args) {
        System.out.println("--- Singleton Pattern Demonstration ---\n");

        // 1. Classic Singleton
        System.out.println("1. Classic Singleton (Lazy, Unsafe):");
        ClassicSingleton classic1 = ClassicSingleton.getInstance();
        ClassicSingleton classic2 = ClassicSingleton.getInstance();
        System.out.println("   Instance 1: " + classic1);
        System.out.println("   Instance 2: " + classic2);
        System.out.println("   Same reference? " + (classic1 == classic2));
        System.out.println();

        // 2. Synchronized Singleton
        System.out.println("2. Synchronized Singleton (Thread-Safe, Slow):");
        SynchronizedSingleton sync1 = SynchronizedSingleton.getInstance();
        SynchronizedSingleton sync2 = SynchronizedSingleton.getInstance();
        System.out.println("   Instance 1: " + sync1);
        System.out.println("   Instance 2: " + sync2);
        System.out.println("   Same reference? " + (sync1 == sync2));
        System.out.println();

        // 3. Eager Singleton
        System.out.println("3. Eager Singleton (Thread-Safe, Eager):");
        EagerSingleton eager1 = EagerSingleton.getInstance();
        EagerSingleton eager2 = EagerSingleton.getInstance();
        System.out.println("   Instance 1: " + eager1);
        System.out.println("   Instance 2: " + eager2);
        System.out.println("   Same reference? " + (eager1 == eager2));
        System.out.println();

        // 4. Double-Checked Locking Singleton
        System.out.println("4. Double-Checked Locking Singleton (Thread-Safe, Fast):");
        DoubleCheckedLockingSingleton dcl1 = DoubleCheckedLockingSingleton.getInstance();
        DoubleCheckedLockingSingleton dcl2 = DoubleCheckedLockingSingleton.getInstance();
        System.out.println("   Instance 1: " + dcl1);
        System.out.println("   Instance 2: " + dcl2);
        System.out.println("   Same reference? " + (dcl1 == dcl2));
        System.out.println();

        // 5. Enum Singleton
        System.out.println("5. Enum Singleton (Thread-Safe, Serialization Safe):");
        EnumSingleton enum1 = EnumSingleton.UNIQUE_INSTANCE;
        EnumSingleton enum2 = EnumSingleton.UNIQUE_INSTANCE;
        System.out.println("   Instance 1: " + enum1);
        System.out.println("   Instance 2: " + enum2);
        System.out.println("   Same reference? " + (enum1 == enum2));
        enum1.performAction();
    }
}
