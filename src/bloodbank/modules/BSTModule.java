package bloodbank.modules;

import bloodbank.io.BloodUnitFileManager;
import bloodbank.model.BloodUnit;
import bloodbank.structures.BloodUnitBST;

import java.util.List;

/**
 * MEMBER 3 MODULE
 * - Owns: Binary Search Tree (BST), keyed by expiry date.
 * - Demonstrates how blood units can be stored and traversed in a BST.
 * - Selection Sort lives separately in bloodbank.sort.SelectionSorter.
 */
public class BSTModule {

    // Internal BST structure that stores BloodUnit objects
    private final BloodUnitBST tree = new BloodUnitBST();
    // File manager for persistence (load/save blood units)
    private final BloodUnitFileManager fileManager = new BloodUnitFileManager();
    private final String filePath; // path to blood units file

    // Constructor: initialize with file path
    public BSTModule(String filePath) {
        this.filePath = filePath;
    }

    // Load all blood units from file and insert into BST
    public List<BloodUnit> loadAll() {
        List<BloodUnit> loaded = fileManager.loadFromFile(filePath);
        for (BloodUnit u : loaded) tree.insert(u);
        return loaded;
    }

    // Insert a new blood unit into BST and persist changes
    public void insert(BloodUnit unit) {
        tree.insert(unit);
        persist();
    }

    // Delete a blood unit by ID and persist changes
    public void delete(String unitId) {
        tree.delete(unitId);
        persist();
    }

    // Search for a blood unit by ID
    public BloodUnit search(String unitId) {
        return tree.search(unitId);
    }

    // Traversals: return lists of blood units in different orders
    public List<BloodUnit> inorder() { return tree.inorder(); }   // sorted by expiry
    public List<BloodUnit> preorder() { return tree.preorder(); } // root → left → right
    public List<BloodUnit> postorder() { return tree.postorder(); } // left → right → root

    /**
     * Persist current BST state to file.
     * Saves inorder traversal (sorted by expiry date).
     * Used after status changes (e.g. Issued) without reinsertion.
     */
    public void persist() {
        fileManager.saveToFile(filePath, tree.inorder());
    }
}
