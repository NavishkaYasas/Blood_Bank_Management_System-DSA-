package bloodbank.modules;

import bloodbank.model.BloodUnit;
import bloodbank.structures.BloodUnitAVL;

import java.util.List;

/**
 * MEMBER 4 MODULE - owns: AVL Tree, the self-balancing counterpart to
 * Member 3's BST, also keyed by expiry date. Kept in sync with the BST by
 * BloodBankSystem so both structures can be demonstrated side by side.
 * Insertion Sort lives in bloodbank.sort.InsertionSorter.
 */
public class AVLModule {

    private final BloodUnitAVL tree = new BloodUnitAVL();

    public void insert(BloodUnit unit) {
        tree.insert(unit);
    }

    public void delete(String unitId) {
        tree.delete(unitId);
    }

    public BloodUnit search(String unitId) {
        return tree.search(unitId);
    }

    public List<BloodUnit> inorder() { return tree.inorder(); }
    public List<BloodUnit> preorder() { return tree.preorder(); }
    public List<BloodUnit> postorder() { return tree.postorder(); }

    public int getTreeHeight() {
        return tree.getTreeHeight();
    }
}
