import java.util.Objects;

public class MyHashMap<K,V> {

    private static final int INITIAL_SIZE = 1<<4; // 16 Size
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    Entry[] hashTable;

    // Similar to JDK HashMap: spreads high bits into low bits.
    private static int spreadHash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    // Table size returned by tableSizeFor() is always a power of two,
    // so we can compute the bucket index using a bit mask.
    private int indexFor(int hash) {
        return hash & (hashTable.length - 1);
    }

    // constructor without the custom initial value
    MyHashMap(){
        hashTable = new Entry[INITIAL_SIZE];
    }

    MyHashMap(int capacity){
        int tableSize = tableSizeFor(capacity);
        hashTable = new Entry[tableSize];
    }

    /**
     * Rounds the requested capacity up to the smallest power of two >= cap.
     *
     * Interview idea:
     * - HashMap tables are sized to powers of two.
     * - That makes bucket indexing fast: (hash & (capacity - 1)).
     *
     * How the bit trick works:
     * 1) We start with n = cap - 1.
     *    - Why cap - 1?
     *      If cap is already a power of two (say 16), we want the answer to stay 16,
     *      not jump to 32. Using cap - 1 turns 16 into 15 (1111b), so the "round up"
     *      will produce 16 again when we add 1 at the end.
     *
     * 2) Then we repeatedly "spread" the highest 1-bit to the right.
     *    - The operation: n |= (n >>> shift)
     *      means: for every bit position, if a higher bit is 1, copy that 1 downwards.
     *
     *    After these shifts (1, 2, 4, 8, 16), n becomes:
     *      n = 2^k - 1
     *    where 2^k is the next power of two >= cap.
     *
     * 3) Finally return n + 1.
     *    - If n is (2^k - 1) (all ones up to bit k-1),
     *      then n + 1 is exactly 2^k.
     *
     * Example A: cap = 10 (1010b)
     * - n = cap - 1 = 9 (1001b)
     * - spread bits so that we get n = 15 (1111b)
     * - return n + 1 = 16
     *
     * Example B: cap = 16 (10000b)
     * - n = cap - 1 = 15 (01111b)
     * - spreading keeps it 15
     * - return n + 1 = 16 (so it does not overshoot)
     *
     * Bounds:
     * - If cap <= 0, cap - 1 becomes negative, so (n < 0) => return 1.
     * - If cap is huge, (n >= MAXIMUM_CAPACITY) => clamp to MAXIMUM_CAPACITY.
     */
    final int tableSizeFor(int cap){
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    class Entry<K,V>{
        K key;
        V value;
        Entry next;

        Entry(K k,V v){
            key = k;
            value = v;
        }

        public K getKey(){ return  key;}

        public void setKey(K key){this.key = key;}

        public V getValue(){return value;}

        public void setValue(V value){ this.value = value;}
    }

    public void put(K key, V value){
        int hashCode = spreadHash(key);
        int hashIndex = indexFor(hashCode);
        Entry node = hashTable[hashIndex];

        if(node == null){
            Entry newNode = new Entry(key,value);
            hashTable[hashIndex] = newNode;
        }
        else{
            Entry previousNode = node;
            while (node != null){
                // Keys must be compared by equality, not reference identity.
                if(Objects.equals(node.key, key)){
                    node.value = value;
                    return;
                }

                previousNode = node;
                node = node.next;
            }

            Entry newNode = new Entry(key,value);
            previousNode.next = newNode;
        }
    }


    public V get(K key){
        int hashCode = spreadHash(key);
        int hashIndex = indexFor(hashCode);

        Entry node = hashTable[hashIndex];

        while (node != null){
            if(node.key.equals(key)){
                return (V) node.value;
            }
            node = node.next;
        }

        return null;
    }


    public static void main(String args[]){
        MyHashMap<Integer,Character> map = new MyHashMap<>(10);
        map.put(1,'A');
        map.put(2,'B');
        map.put(3,'C');
        map.put(4,'C');
        map.put(5,'D');

        for(int i = 1;i<=5;i++)
        {
            System.out.println(i + " " + map.get(i));
        }

    }

}
