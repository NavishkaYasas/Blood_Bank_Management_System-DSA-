package bloodbank.modules;

import bloodbank.io.DonorFileManager;
import bloodbank.model.Donor;
import bloodbank.search.LinearSearch;
import bloodbank.structures.DonorLinkedList;

import java.util.List;

/**
 * MEMBER 1 MODULE - owns: Arrays (blood group categories, defined in
 * BloodBankSystem.BLOOD_GROUPS), Linked List (donor records), Linear Search.
 *
 * This class is the single point of contact for anything involving the
 * DonorLinkedList. Other modules (e.g. Member 6's hash table/set) are kept
 * in sync by BloodBankSystem, which calls both modules together whenever a
 * donor is added or removed.
 */
public class DonorModule {

    private final DonorLinkedList donorList = new DonorLinkedList();
    private final DonorFileManager fileManager = new DonorFileManager();
    private final String filePath;

    public DonorModule(String filePath) {
        this.filePath = filePath;
    }

    /** Loads donors.txt into the linked list at startup. Returns the loaded list for other modules to sync with. */
    public List<Donor> loadAll() {
        List<Donor> loaded = fileManager.loadFromFile(filePath);
        for (Donor d : loaded) donorList.insert(d);
        return loaded;
    }

    public void insert(Donor donor) {
        donorList.insert(donor);
        persist();
    }

    public boolean delete(String id) {
        boolean removed = donorList.delete(id);
        if (removed) persist();
        return removed;
    }

    public Donor searchById(String id) {
        return donorList.search(id);
    }

    /** Task 3 deliverable: Linear Search, used here to search by donor name. */
    public Donor searchByName(String name) {
        return LinearSearch.searchByName(donorList.traverse(), name);
    }

    public List<Donor> traverse() {
        return donorList.traverse();
    }

    public int size() {
        return donorList.size();
    }

    private void persist() {
        fileManager.saveToFile(filePath, donorList.traverse());
    }
}
