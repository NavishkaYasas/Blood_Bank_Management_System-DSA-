package bloodbank.modules;

import bloodbank.model.Donor;
import bloodbank.structures.DonorHashTable;
import bloodbank.structures.DonorSet;

/**
 * MEMBER 6 MODULE
 * - Owns: Hash Table (fast donor lookup by ID, chaining collision resolution).
 * - Owns: Set ADT (duplicate-donor prevention).
 * - Quick Sort lives separately in bloodbank.sort.QuickSorter.
 */
public class HashSetModule {

    // Internal hash table for fast donor lookup by ID
    private final DonorHashTable hashTable = new DonorHashTable();
    // Internal set for preventing duplicate donor IDs
    private final DonorSet donorSet = new DonorSet();

    // ---- Hash Table ----

    // Insert donor into hash table
    public void put(Donor donor) {
        hashTable.put(donor);
    }

    // Retrieve donor by ID
    public Donor get(String id) {
        return hashTable.get(id);
    }

    // Remove donor by ID
    public boolean remove(String id) {
        return hashTable.remove(id);
    }

    // Get number of donors stored in hash table
    public int hashTableSize() {
        return hashTable.size();
    }

    // ---- Set ADT ----

    // Check if donor ID already exists (duplicate check)
    public boolean isDuplicate(String id) {
        return donorSet.contains(id);
    }

    /**
     * Register a new donor ID in the set.
     * Returns false if the ID was already present (duplicate).
     */
    public boolean registerId(String id) {
        return donorSet.add(id);
    }

    // Get number of unique donor IDs in the set
    public int setSize() {
        return donorSet.size();
    }
}
