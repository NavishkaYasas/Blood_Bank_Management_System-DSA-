package bloodbank.structures;

/**
 * Member 6's component: Task 2 - Set ADT.
 * Custom hash-based Set (does not use java.util.HashSet) that stores donor
 * IDs only, used purely to check for and prevent duplicate registrations.
 * Collision resolution: separate chaining, same technique as DonorHashTable.
 */
public class DonorSet {
    private static final int CAPACITY = 101;
    private Node<String>[] buckets;
    private int count;

    @SuppressWarnings("unchecked")
    public DonorSet() {
        buckets = new Node[CAPACITY];
        count = 0;
    }

    private int hash(String key) {
        int hashVal = 0;
        for (int i = 0; i < key.length(); i++) {
            hashVal = (hashVal * 31 + key.charAt(i)) % CAPACITY;
        }
        return Math.abs(hashVal);
    }

    /** O(1) average membership check. */
    public boolean contains(String id) {
        int index = hash(id);
        Node<String> current = buckets[index];
        while (current != null) {
            if (current.data.equals(id)) return true;
            current = current.next;
        }
        return false;
    }

    /** Adds id to the set. Returns false if it was already present (duplicate). O(1) average. */
    public boolean add(String id) {
        if (contains(id)) return false;
        int index = hash(id);
        Node<String> newNode = new Node<>(id);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        count++;
        return true;
    }

    public int size() { return count; }
}