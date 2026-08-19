package bloodbank.modules;

import bloodbank.io.DonorFileManager;
import bloodbank.model.Donor;
import bloodbank.search.LinearSearch;
import bloodbank.structures.DonorLinkedList;

import java.util.List;

/**
 * MEMBER 1 MODULE
 * - Owns: Arrays (blood group categories, defined in BloodBankSystem.BLOOD_GROUPS),
 *   Linked List (donor records), Linear Search.
 *
 * This class is the single point of contact for anything involving the DonorLinkedList.
 * Other modules (e.g. Member 6's hash table/set) are kept in sync by BloodBankSystem,
 * which calls both modules together whenever a donor is added or removed.
 */
public class DonorModule {

    // Internal linked list structure to store donor records
    private final DonorLinkedList donorList = new DonorLinkedList();
    // File manager for persistence (load/save donors)
    private final DonorFileManager fileManager = new DonorFileManager();
    private final String filePath; // path to donors.txt

    // Constructor: initialize with file path
    public DonorModule(String filePath) {
        this.filePath = filePath;
    }

    /** Loads donors.txt into the linked list at startup. Returns the loaded list for other modules to sync with. */
    public List<Donor> loadAll() {
        List<Donor> loaded = fileManager.loadFromFile(filePath);
        for (Donor d : loaded) donorList.insert(d); // insert each donor into linked list
        return loaded;
    }

    // Insert a new donor into linked list and persist changes
    public void insert(Donor donor) {
        donorList.insert(donor);
        persist();
    }

    // Delete a donor by ID. Persist only if deletion succeeded.
    public boolean delete(String id) {
        boolean removed = donorList.delete(id);
        if (removed) persist();
        return removed;
    }

    // Search donor by ID (linked list search)
    public Donor searchById(String id) {
        return donorList.search(id);
    }

    /** Task 3 deliverable: Linear Search, used here to search by donor name. */
    public Donor searchByName(String name) {
        return LinearSearch.searchByName(donorList.traverse(), name);
    }

    // Traverse linked list and return all donors
    public List<Donor> traverse() {
        return donorList.traverse();
    }

    // Get number of donors in linked list
    public int size() {
        return donorList.size();
    }

    // Persist current donor list to file
    private void persist() {
        fileManager.saveToFile(filePath, donorList.traverse());
    }
}
