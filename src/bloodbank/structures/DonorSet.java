package bloodbank.structures;

/**
 * Member 6's component: Task 2 - Set ADT.
 * Custom hash-based Set (does not use java.util.HashSet).
 * - Stores donor IDs only.
 * - Used to check for and prevent duplicate registrations.
 * - Collision resolution: separate chaining (linked lists in buckets).
 */
public class DonorSet {
    private static final int CAPACITY = 101;   // prime number reduces clustering
    private Node<String>[] buckets;            // array of linked list heads
    private int count;                         // number of unique IDs stored

    @SuppressWarnings("unchecked")
    public DonorSet() {
        buckets = new Node[CAPACITY];
        count = 0;
    }

    /** Polynomial hash function over donor ID string. */
    private int hash(String key) {
        int hashVal = 0;
        for (int i = 0; i < key.length(); i++) {
            hashVal = (hashVal * 31 + key.charAt(i)) % CAPACITY;
        }
        return Math.abs(hashVal);
    }

    /** Membership check. O(1) average. */
    public boolean contains(String id) {
        int index = hash(id);
        Node<String> current = buckets[index];
        while (current != null) {
            if (current.data.equals(id)) return true;
            current = current.next;
        }
        return false;
    }

    /**
     * Adds ID to the set.
     * Returns false if already present (duplicate).
     * O(1) average insertion.
     */
    public boolean add(String id) {
        if (contains(id)) return false; // duplicate check
        int index = hash(id);
        Node<String> newNode = new Node<>(id);
        newNode.next = buckets[index]; // insert at head of chain
        buckets[index] = newNode;
        count++;
        return true;
    }

    public int size() { return count; }
}
