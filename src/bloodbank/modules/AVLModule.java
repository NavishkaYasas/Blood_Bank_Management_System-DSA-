package bloodbank.modules;

import bloodbank.model.BloodUnit;
import bloodbank.structures.BloodUnitAVL;

import java.util.List;

/**
 * MEMBER 4 MODULE
 * - Owns: AVL Tree (self-balancing binary search tree).
 * - This is the counterpart to Member 3's plain BST.
 * - Both are kept in sync by BloodBankSystem so they can be demonstrated side by side.
 * - Insertion Sort lives separately in bloodbank.sort.InsertionSorter.
 */
public class AVLModule {

    // Internal AVL tree structure that stores BloodUnit objects
    private final BloodUnitAVL tree = new BloodUnitAVL();

    // Insert a new blood unit into the AVL tree (auto-balances after insertion)
    public void insert(BloodUnit unit) {
        tree.insert(unit);
    }

    // Delete a blood unit from the AVL tree by its ID (auto-balances after deletion)
    public void delete(String unitId) {
        tree.delete(unitId);
    }

    // Search for a blood unit in the AVL tree by its ID
    public BloodUnit search(String unitId) {
        return tree.search(unitId);
    }

    // Traversals: return lists of blood units in different orders
    public List<BloodUnit> inorder() { return tree.inorder(); }   // sorted order by expiry
    public List<BloodUnit> preorder() { return tree.preorder(); } // root → left → right
    public List<BloodUnit> postorder() { return tree.postorder(); } // left → right → root

    // Get the height of the AVL tree (shows balance efficiency)
    public int getTreeHeight() {
        return tree.getTreeHeight();
    }
}
