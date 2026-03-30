import java.util.ArrayDeque;
import java.util.Objects;

/**
 * Interview-friendly HashMap-like implementation:
 * - Uses separate chaining (linked list) for collisions.
 * - Resizes based on load factor (capacity * loadFactor).
 * - "Treeifies" a bucket when it becomes too large (high collision rate).
 *
 * Notes for interviews:
 * - The real JDK HashMap uses Red-Black trees with additional production-grade details.
 * - Here we keep the behavior and thresholds very close to JDK concepts while making the code
 *   readable and easy to extend.
 */
public class TreeifiedHashMap<K, V> {
    // Capacity is always rounded to a power of two.
    private static final int DEFAULT_INITIAL_CAPACITY = 16; // 2^4
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Treeification thresholds (mirrors HashMap's intent)
    private static final int TREEIFY_THRESHOLD = 8;       // list length threshold to treeify
    private static final int UNTREEIFY_THRESHOLD = 6;     // kept for explanation/extension
    private static final int MIN_TREEIFY_CAPACITY = 64;  // only treeify if table is large enough

    private transient Node<K, V>[] table;
    private int size;
    private int threshold; // resize when size > threshold
    private final float loadFactor;

    public TreeifiedHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public TreeifiedHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public TreeifiedHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) initialCapacity = DEFAULT_INITIAL_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) throw new IllegalArgumentException("Invalid loadFactor");
        this.loadFactor = loadFactor;

        int cap = tableSizeFor(initialCapacity);
        this.table = newTable(cap);
        this.threshold = (int) (cap * loadFactor);
    }

    // -------------------- Public API --------------------

    public int size() {
        // Number of key-value pairs currently stored in this map.
        return size;
    }

    /**
     * Returns the value mapped to {@code key}, or {@code null} if the key is not present.
     *
     * Lookup is a two-step process:
     * 1) compute bucket index using spreadHash + indexFor
     * 2) search inside that bucket
     *    - if the bucket is a linked list => linear search
     *    - if the bucket is treeified => BST search using compareKeys()
     */
    public V get(Object key) {
        Node<K, V>[] tab = table;
        int hash = spreadHash(key);
        int index = indexFor(hash, tab.length);

        Node<K, V> first = tab[index];
        if (first == null) return null;

        if (first instanceof TreeNode<K, V> treeRoot) {
            TreeNode<K, V> node = getTreeNode(treeRoot, hash, key);
            return node == null ? null : node.value;
        }

        // Linked list search
        for (Node<K, V> e = first; e != null; e = e.next) {
            if (Objects.equals(e.key, key)) return e.value;
        }
        return null;
    }

    /**
     * Associates {@code key -> value} in this map.
     *
     * Collision strategy:
     * - Start with a linked list per bucket.
     * - If a bucket chain becomes too large, treeify it into a Red-Black tree bin.
     *
     * Resizing (load factor):
     * - We keep: threshold = capacity * loadFactor
     * - When insertion would make size > threshold, we resize (double capacity) and rehash.
     *
     * Returns:
     * - the previous value for the key if it existed
     * - otherwise null
     */
    public V put(K key, V value) {
        Node<K, V>[] tab = table;
        int hash = spreadHash(key);
        int index = indexFor(hash, tab.length);

        Node<K, V> first = tab[index];
        if (first == null) {
            tab[index] = new Node<>(hash, key, value, null);
            size++;
            if (size > threshold) resize();
            return null;
        }

        // Treeified bucket: insert/update into the Red-Black tree bin.
        if (first instanceof TreeNode<K, V> treeRoot) {
            TreePutResult<K, V> res = putTreeVal(treeRoot, hash, key, value);
            tab[index] = res.root;
            if (res.inserted) {
                size++;
                if (size > threshold) resize();
            }
            return res.oldValue;
        }

        // List bucket:
        // - walk the chain to find an existing key (update) or find the end (append)
        // - count chain length so we can decide whether to treeify the bucket
        int binCount = 0;
        Node<K, V> prev = null;
        for (Node<K, V> e = first; e != null; e = e.next) {
            binCount++;
            if (Objects.equals(e.key, key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
            prev = e;
        }

        // Append new node at the end of the chain (keeps logic simple for interviews).
        prev.next = new Node<>(hash, key, value, null);
        size++;

        // If the chain is too long:
        // - Treeify only when table is reasonably large. Otherwise resizing is better.
        if (binCount + 1 >= TREEIFY_THRESHOLD) {
            if (tab.length >= MIN_TREEIFY_CAPACITY) {
                tab[index] = treeifyBin(first);
            } else {
                resize();
            }
        } else if (size > threshold) {
            resize();
        }

        // Safety guard: depending on insertion order (treeify vs resize), we might have crossed
        // threshold. Resizing again is harmless because it doubles capacity.
        if (size > threshold) resize();
        return null;
    }

    // -------------------- Core HashMap mechanics --------------------

    /**
     * Spreads bits of hashCode into the lower bits.
     *
     * Why is this needed?
     * - bucket index uses low bits: index = hash & (length - 1)
     * - many hashCode() implementations are not great at mixing (especially in low bits)
     * - XORing with shifted hash increases distribution across buckets
     */
    private static int spreadHash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    // Table length is always a power of two, so index is just the low bits of hash.
    private static int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    // Helper to create an array of generic Node with unchecked cast.
    @SuppressWarnings("unchecked")
    private static <K, V> Node<K, V>[] newTable(int capacity) {
        return (Node<K, V>[]) new Node[capacity];
    }

    /**
     * Rounds the requested capacity up to the smallest power of two >= cap.
     *
     * This is a classic JDK-style optimization used by HashMap:
     * - If table length is a power of two, bucket index can be computed fast as:
     *     index = hash & (length - 1)
     *
     * Why do we need the "next power of two"?
     * - Using a power of two turns the expensive modulo (%) into a cheap bit-mask.
     * - Also helps keep bucket indexing uniform when combined with hash spreading.
     *
     * Bit trick explanation (line-by-line intent):
     * 1) n = cap - 1
     *    - If cap is already a power of two, subtracting 1 turns it into an "all ones"
     *      number below that power.
     *      Example: cap=16 (10000b) => n=15 (01111b)
     *      After the rounding operations, n+1 will come back to 16 (not 32).
     *    - If cap is not a power of two (cap between two powers), we force all bits
     *      below the highest set bit to 1, so adding 1 carries into the next power.
     *
     * 2) n |= n >>> shift (shift = 1,2,4,8,16)
     *    - Right shift (>>>): moves bits down, filling with zeros on the left.
     *    - OR-ing (|=): copies the highest set bit "downwards" across the lower bits.
     *    - Repeating for 1,2,4,8,16 covers all bit positions within a 32-bit int.
     *
     * After these OR+shifts, n becomes:
     *   n = 2^k - 1
     * where 2^k is the next power of two >= original cap.
     *
     * 3) return n + 1
     *    - If n is (2^k - 1), then n+1 is exactly 2^k.
     *
     * Concrete example A: cap = 10
     * - cap in binary: 1010b
     * - n = cap - 1 = 9 = 1001b
     * - After spreading, n becomes 1111b (15)
     * - n + 1 = 16 (next power of two)
     *
     * Concrete example B: cap = 16
     * - cap is already power of two
     * - n = cap - 1 = 15 = 1111b
     * - After spreading, n stays 1111b
     * - n + 1 = 16 (does not overshoot)
     *
     * Bounds behavior:
     * - If cap <= 0: n will be negative, so we return 1.
     * - If cap is extremely large: clamp to MAXIMUM_CAPACITY to avoid overflow.
     */
    private static int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * Resize policy (Load factor):
     * - threshold = capacity * loadFactor
     * - when size > threshold -> double capacity
     *
     * Collision buckets are rehashed into the new array because:
     * - the bucket index depends on capacity (different length => different mask)
     *
     * Production HashMap has a more optimized resize that can sometimes keep tree bins.
     * Here we "untreeify" tree bins to keep the implementation readable for interviews:
     * - convert tree bin -> linked list bin
     * - then rehash nodes like normal list bins
     */
    private void resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = oldTab.length;
        if (oldCap >= MAXIMUM_CAPACITY) return;

        int newCap = oldCap << 1;
        if (newCap > MAXIMUM_CAPACITY) newCap = MAXIMUM_CAPACITY;

        @SuppressWarnings("unchecked")
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];

        threshold = (int) (newCap * loadFactor);
        table = newTab;

        for (int i = 0; i < oldCap; i++) {
            Node<K, V> e = oldTab[i];
            if (e == null) continue;

            // Convert tree -> list to keep resize code small and understandable.
            if (e instanceof TreeNode<K, V> treeRoot) {
                e = untreeify(treeRoot);
            }

            while (e != null) {
                Node<K, V> next = e.next;
                int newIndex = indexFor(e.hash, newCap);
                e.next = newTab[newIndex];
                newTab[newIndex] = e;
                e = next;
            }
        }
    }

    // -------------------- Treeification --------------------

    /**
     * Converts a tree bin back into a linked list bin.
     *
     * We do this during resize (to keep the resize logic simple).
     * Complexity is O(m) for m nodes in that bucket.
     *
     * Implementation approach:
     * - Use an explicit stack for in-order traversal (no recursion to avoid interview stack worries)
     * - Produce a simple linked list via the shared `next` pointer
     * - Clear tree pointers so the nodes behave as plain list nodes afterward
     */
    private Node<K, V> untreeify(TreeNode<K, V> root) {
        // In-order traversal using an explicit stack.
        ArrayDeque<TreeNode<K, V>> stack = new ArrayDeque<>();
        TreeNode<K, V> curr = root;

        Node<K, V> head = null;
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            TreeNode<K, V> node = stack.pop();

            // Detach tree pointers; we only keep 'next' as list linkage.
            TreeNode<K, V> right = node.right;
            node.left = null;
            node.right = null;
            node.parent = null;

            node.next = head;
            head = node;

            // Continue traversal from the right subtree (we already popped the node).
            curr = right;
        }

        return head;
    }

    /**
     * Converts a list bin into a tree bin.
     *
     * In JDK HashMap, treeification uses more careful rules around ordering and balanced insertion.
     * For interview clarity we:
     * - traverse the list nodes
     * - insert each node into a Red-Black tree using putTreeVal()
     * - return the final tree root
     */
    private TreeNode<K, V> treeifyBin(Node<K, V> head) {
        TreeNode<K, V> root = null;
        for (Node<K, V> e = head; e != null; e = e.next) {
            TreePutResult<K, V> res = putTreeVal(root, e.hash, e.key, e.value);
            root = res.root;
        }
        if (root != null) root.red = false;
        return root;
    }

    /**
     * Searches for a key inside a tree bin.
     *
     * The tree is ordered by compareKeys():
     * - primarily by hash
     * - then by key comparability or deterministic tie-breakers
     *
     * If compareKeys returns:
     * - < 0 => go left
     * - > 0 => go right
     * - == 0 => keys are treated as equivalent in that ordering; we still verify equals()
     */
    private TreeNode<K, V> getTreeNode(TreeNode<K, V> root, int hash, Object key) {
        TreeNode<K, V> p = root;
        while (p != null) {
            int cmp = compareKeys(hash, key, p.hash, p.key);
            if (cmp < 0) {
                p = p.left;
            } else if (cmp > 0) {
                p = p.right;
            } else {
                // cmp == 0 should mean keys are equal (otherwise compareKeys might have a collision tie).
                return Objects.equals(p.key, key) ? p : null;
            }
        }
        return null;
    }

    private static final class TreePutResult<K, V> {
        final TreeNode<K, V> root;
        final boolean inserted;
        final V oldValue;

        // Small value object so putTreeVal can return:
        // - the (possibly new) root after rebalancing
        // - whether we inserted a new node vs updated an existing key
        // - the previous value if it was updated
        TreePutResult(TreeNode<K, V> root, boolean inserted, V oldValue) {
            this.root = root;
            this.inserted = inserted;
            this.oldValue = oldValue;
        }
    }

    /**
     * Inserts a node into the tree bin or updates an existing node.
     *
     * Important: This method assumes the bucket is already treeified, so `root` is a tree root.
     *
     * Algorithm:
     * 1) Walk the tree using compareKeys() until we find:
     *    - an existing key (update) OR
     *    - a null child position (insert)
     * 2) If inserted, restore Red-Black properties via fixAfterInsertion().
     * 3) Return the (possibly rotated) new root.
     */
    private TreePutResult<K, V> putTreeVal(TreeNode<K, V> root, int hash, K key, V value) {
        if (root == null) {
            TreeNode<K, V> node = new TreeNode<>(hash, key, value);
            node.red = false;
            return new TreePutResult<>(node, true, null);
        }

        TreeNode<K, V> parent = null;
        TreeNode<K, V> p = root;
        int cmp = 0;

        while (p != null) {
            parent = p;
            cmp = compareKeys(hash, key, p.hash, p.key);
            if (cmp < 0) {
                p = p.left;
            } else if (cmp > 0) {
                p = p.right;
            } else {
                // Only update when keys are actually equal.
                if (Objects.equals(p.key, key)) {
                    V old = p.value;
                    p.value = value;
                    return new TreePutResult<>(rootOf(p), false, old);
                }
                // Extremely rare compare tie collision: force direction.
                cmp = 1;
                p = p.right;
            }
        }

        TreeNode<K, V> x = new TreeNode<>(hash, key, value);
        x.parent = parent;
        // Place the new node as a left or right child based on the final compare direction.
        if (cmp < 0) parent.left = x;
        else parent.right = x;

        fixAfterInsertion(x);
        TreeNode<K, V> newRoot = rootOf(x);
        newRoot.red = false;
        return new TreePutResult<>(newRoot, true, null);
    }

    /**
     * Comparison rule used by the tree to keep a consistent BST ordering.
     *
     * We must guarantee that:
     * - if two keys are "equivalent" for ordering purposes, they end up in the same position
     * - otherwise the BST property breaks and search could fail
     *
     * Ordering strategy:
     * 1) if keys are equals() => compare result is 0
     * 2) if hashes differ => order by hash
     * 3) if hashes are equal but keys are not equals():
     *    - if keys are comparable and of compatible runtime type => compareTo()
     *    - otherwise => tieBreakOrder() to ensure deterministic ordering
     */
    private int compareKeys(int hash1, Object k1, int hash2, Object k2) {
        if (Objects.equals(k1, k2)) return 0;
        if (hash1 != hash2) return Integer.compare(hash1, hash2);

        // Hash equal but keys not equal:
        // - if keys are comparable and same type, use compareTo
        // - else use tieBreakOrder to keep deterministic ordering.
        if (k1 instanceof Comparable<?> c && c.getClass().isInstance(k2)) {
            @SuppressWarnings("unchecked")
            int res = ((Comparable<Object>) c).compareTo(k2);
            if (res != 0) return res;
        }

        return tieBreakOrder(k1, k2);
    }

    /**
     * Deterministic tie-breaker when:
     * - hash values are equal
     * - keys are not equal (Objects.equals() == false)
     * - and we cannot safely/consistently compare using compareTo()
     *
     * We use:
     * 1) class name ordering
     * 2) identityHashCode ordering as a stable-ish fallback
     *
     * Why not use equals() only?
     * - A Red-Black tree still needs a total order to decide left vs right.
     * - tieBreakOrder makes that order total and deterministic.
     */
    private int tieBreakOrder(Object a, Object b) {
        if (a == b) return 0;
        int d = a.getClass().getName().compareTo(b.getClass().getName());
        if (d != 0) return d;
        int ha = System.identityHashCode(a);
        int hb = System.identityHashCode(b);
        if (ha != hb) return Integer.compare(ha, hb);

        // Very rare: identityHashCode collision for same class and non-equal keys.
        // We need a non-zero, deterministic answer; direction affects ordering but keeps the tree usable.
        return 1;
    }

    // -------------------- Red-Black Tree balancing --------------------

    /**
     * Finds the current root of a tree given any node `x` inside it.
     * Red-Black rotations can change the root, so we recompute it by climbing parents.
     */
    private static <K, V> TreeNode<K, V> rootOf(TreeNode<K, V> x) {
        while (x.parent != null) x = x.parent;
        return x;
    }

    /**
     * Left rotation around node x.
     *
     * In rotation terms:
     *      x                 y
     *       \               / \
     *        y     =>     x   yr
     *       / \             \
     *      yl  yr           yl
     *
     * We update parent pointers for correctness.
     */
    private void rotateLeft(TreeNode<K, V> x) {
        TreeNode<K, V> r = x.right;
        if (r == null) return;

        x.right = r.left;
        if (r.left != null) r.left.parent = x;

        r.parent = x.parent;
        if (x.parent == null) {
            // root will be discovered via rootOf() by the caller
        } else if (x == x.parent.left) {
            x.parent.left = r;
        } else {
            x.parent.right = r;
        }

        r.left = x;
        x.parent = r;
    }

    /**
     * Right rotation around node x (mirror of rotateLeft).
     */
    private void rotateRight(TreeNode<K, V> x) {
        TreeNode<K, V> l = x.left;
        if (l == null) return;

        x.left = l.right;
        if (l.right != null) l.right.parent = x;

        l.parent = x.parent;
        if (x.parent == null) {
            // root will be discovered via rootOf() by the caller
        } else if (x == x.parent.right) {
            x.parent.right = l;
        } else {
            x.parent.left = l;
        }

        l.right = x;
        x.parent = l;
    }

    /**
     * Restores Red-Black properties after inserting a new red node `x`.
     *
     * Red-Black properties (intuition-level):
     * - Every node is either red or black
     * - Root must always be black
     * - Red nodes cannot have red children
     * - Every path from a node to descendant leaves contains the same number of black nodes
     *
     * This method fixes violations by:
     * - recoloring (when uncle is red)
     * - rotating (when uncle is black, depending on triangle/line shape)
     */
    private void fixAfterInsertion(TreeNode<K, V> x) {
        x.red = true;

        while (x != null && x.parent != null && x.parent.red) {
            TreeNode<K, V> parent = x.parent;
            TreeNode<K, V> grandparent = parent.parent;
            if (grandparent == null) break;

            if (parent == grandparent.left) {
                TreeNode<K, V> uncle = grandparent.right;
                if (uncle != null && uncle.red) {
                    parent.red = false;
                    uncle.red = false;
                    grandparent.red = true;
                    x = grandparent;
                } else {
                    if (x == parent.right) {
                        x = parent;
                        rotateLeft(x);
                        parent = x.parent;
                        grandparent = parent == null ? null : parent.parent;
                    }
                    if (parent != null) parent.red = false;
                    if (grandparent != null) {
                        grandparent.red = true;
                        rotateRight(grandparent);
                    }
                }
            } else {
                // symmetric case: parent is right child
                TreeNode<K, V> uncle = grandparent.left;
                if (uncle != null && uncle.red) {
                    parent.red = false;
                    uncle.red = false;
                    grandparent.red = true;
                    x = grandparent;
                } else {
                    if (x == parent.left) {
                        x = parent;
                        rotateRight(x);
                        parent = x.parent;
                        grandparent = parent == null ? null : parent.parent;
                    }
                    if (parent != null) parent.red = false;
                    if (grandparent != null) {
                        grandparent.red = true;
                        rotateLeft(grandparent);
                    }
                }
            }
        }

        TreeNode<K, V> r = rootOf(x);
        r.red = false;
    }

    // -------------------- Nodes --------------------

    /**
     * Base node used for linked-list buckets.
     * Has:
     * - hash: the spread hash at insertion time
     * - key/value: payload
     * - next: linked-list pointer
     */
    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * Tree node used when a bucket is treeified.
     *
     * Extends Node so tree logic can still reuse:
     * - hash/key/value for comparisons
     * - next for keeping a bucket-level list linkage if needed
     *
     * Adds:
     * - left/right/parent pointers for BST structure
     * - `red` color for Red-Black balancing
     */
    private static final class TreeNode<K, V> extends Node<K, V> {
        TreeNode<K, V> parent;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        boolean red;

        TreeNode(int hash, K key, V value) {
            super(hash, key, value, null);
        }
    }

    // -------------------- Demo (optional in interview) --------------------
    /**
     * Quick demo to force many collisions and show treeification behavior.
     *
     * In a real interview you would call `put()` with custom keys that share hashes.
     */
    public static void main(String[] args) {
        TreeifiedHashMap<Key, Integer> map = new TreeifiedHashMap<>(16);
        for (int i = 1; i <= 30; i++) {
            map.put(new Key(i), i);
        }
        System.out.println(map.get(new Key(10)));
    }

    /**
     * Key wrapper used only for the demo.
     *
     * We return a constant hash so all keys collide into the same bucket,
     * which makes it easier to observe bucket treeification without inserting millions of items.
     */
    static final class Key {
        final int id;

        Key(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return 42; // constant hash => high collision
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof Key other) && this.id == other.id;
        }
    }
}

