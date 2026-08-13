package bloodbank.modules;

import bloodbank.model.Donor;
import bloodbank.structures.DonorHashTable;
import bloodbank.structures.DonorSet;

/**
 * MEMBER 6 MODULE - owns: Hash Table (fast donor lookup by ID, chaining
 * collision resolution), Set ADT (duplicate-donor prevention).
 * Quick Sort lives in bloodbank.sort.QuickSorter.
 */
public class HashSetModule {

    private final DonorHashTable hashTable = new DonorHashTable();
    private final DonorSet donorSet = new DonorSet();

    // ---- Hash Table ----

    public void put(Donor donor) {
        hashTable.put(donor);
    }

    public Donor get(String id) {
        return hashTable.get(id);
    }

    public boolean remove(String id) {
        return hashTable.remove(id);
    }

    public int hashTableSize() {
        return hashTable.size();
    }

    // ---- Set ADT ----

    public boolean isDuplicate(String id) {
        return donorSet.contains(id);
    }

    /** Returns false if the id was already present (i.e. registration should be rejected). */
    public boolean registerId(String id) {
        return donorSet.add(id);
    }

    public int setSize() {
        return donorSet.size();
    }
}
