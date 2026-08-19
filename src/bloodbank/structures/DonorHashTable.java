package bloodbank.structures;

import bloodbank.model.Donor;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 6's component: Task 2 - Hash Table.
 * - Collision resolution: separate chaining (each bucket is a linked list).
 * - Uses a polynomial hash function on donor ID strings.
 */
public class DonorHashTable {
    private static final int CAPACITY = 101; // prime number reduces clustering
    private Node<Donor>[] buckets;           // array of linked list heads
    private int count;                       // number of donors stored

    @SuppressWarnings("unchecked")
    public DonorHashTable() {
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

    /** Insert or update a donor. O(1) average, O(n) worst case (all keys collide). */
    public void put(Donor donor) {
        int index = hash(donor.getId());
        Node<Donor> current = buckets[index];
        // Check if donor already exists → update
        while (current != null) {
            if (current.data.getId().equals(donor.getId())) {
                current.data = donor;
                return;
            }
            current = current.next;
        }
        // Insert new donor at head of chain
        Node<Donor> newNode = new Node<>(donor);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        count++;
    }

    /** Lookup donor by ID. O(1) average. */
    public Donor get(String id) {
        int index = hash(id);
        Node<Donor> current = buckets[index];
        while (current != null) {
            if (current.data.getId().equals(id)) return current.data;
            current = current.next;
        }
        return null;
    }

    /** Remove donor by ID. O(1) average. */
    public boolean remove(String id) {
        int index = hash(id);
        Node<Donor> current = buckets[index];
        Node<Donor> prev = null;
        while (current != null) {
            if (current.data.getId().equals(id)) {
                if (prev == null) buckets[index] = current.next;
                else prev.next = current.next;
                count--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public int size() { return count; }

    /** Return all donors in the table. */
    public List<Donor> allDonors() {
        List<Donor> result = new ArrayList<>();
        for (Node<Donor> bucket : buckets) {
            Node<Donor> current = bucket;
            while (current != null) {
                result.add(current.data);
                current = current.next;
            }
        }
        return result;
    }
}
