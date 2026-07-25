package bloodbank.structures;

import bloodbank.model.Donor;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 6's component: Task 2 - Hash Table.
 * Collision resolution: separate chaining, where each bucket is itself a
 * small custom linked list (built with the shared generic Node<T>).
 */
public class DonorHashTable {
    private static final int CAPACITY = 101; // prime number to reduce clustering
    private Node<Donor>[] buckets;
    private int count;

    @SuppressWarnings("unchecked")
    public DonorHashTable() {
        buckets = new Node[CAPACITY];
        count = 0;
    }

    /** Simple polynomial hash function over the donor ID string. */
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
        while (current != null) {
            if (current.data.getId().equals(donor.getId())) {
                current.data = donor;
                return;
            }
            current = current.next;
        }
        Node<Donor> newNode = new Node<>(donor);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        count++;
    }

    /** O(1) average lookup by donor ID. */
    public Donor get(String id) {
        int index = hash(id);
        Node<Donor> current = buckets[index];
        while (current != null) {
            if (current.data.getId().equals(id)) return current.data;
            current = current.next;
        }
        return null;
    }

    /** Remove a donor by ID. O(1) average. */
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