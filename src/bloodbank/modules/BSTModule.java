package bloodbank.modules;

import bloodbank.io.BloodUnitFileManager;
import bloodbank.model.BloodUnit;
import bloodbank.structures.BloodUnitBST;

import java.util.List;

/**
 * MEMBER 3 MODULE - owns: Binary Search Tree, keyed by expiry date.
 * Selection Sort lives in bloodbank.sort.SelectionSorter.
 */
public class BSTModule {

    private final BloodUnitBST tree = new BloodUnitBST();
    private final BloodUnitFileManager fileManager = new BloodUnitFileManager();
    private final String filePath;

    public BSTModule(String filePath) {
        this.filePath = filePath;
    }

    public List<BloodUnit> loadAll() {
        List<BloodUnit> loaded = fileManager.loadFromFile(filePath);
        for (BloodUnit u : loaded) tree.insert(u);
        return loaded;
    }

    public void insert(BloodUnit unit) {
        tree.insert(unit);
        persist();
    }

    public void delete(String unitId) {
        tree.delete(unitId);
        persist();
    }

    public BloodUnit search(String unitId) {
        return tree.search(unitId);
    }

    public List<BloodUnit> inorder() { return tree.inorder(); }
    public List<BloodUnit> preorder() { return tree.preorder(); }
    public List<BloodUnit> postorder() { return tree.postorder(); }

    /** Persist without re-inserting (used after a unit's status field is mutated in place, e.g. Issued). */
    public void persist() {
        fileManager.saveToFile(filePath, tree.inorder());
    }
}
