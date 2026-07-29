package bloodbank.structures;

import bloodbank.model.BloodUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 3's component: Task 5 - Trees.
 * Binary Search Tree keyed by expiry date (via BloodUnit.compareTo), so an
 * in-order traversal naturally lists units from soonest to latest expiry -
 * directly supporting FIFO issuing of the oldest stock first.
 * Search by expiry ordering is O(log n) average, O(n) worst case (skewed tree).
 */
public class BloodUnitBST {
    private TreeNode root;

    public void insert(BloodUnit unit) {
        root = insertRec(root, unit);
    }

    private TreeNode insertRec(TreeNode node, BloodUnit unit) {
        if (node == null) return new TreeNode(unit);
        int cmp = unit.compareTo(node.data);
        if (cmp < 0) node.left = insertRec(node.left, unit);
        else node.right = insertRec(node.right, unit);
        return node;
    }

    /** Search by unit ID (walks the whole tree since the tree is keyed by expiry date, not ID). */
    public BloodUnit search(String unitId) {
        return searchRec(root, unitId);
    }

    private BloodUnit searchRec(TreeNode node, String unitId) {
        if (node == null) return null;
        if (node.data.getUnitId().equals(unitId)) return node.data;
        BloodUnit left = searchRec(node.left, unitId);
        if (left != null) return left;
        return searchRec(node.right, unitId);
    }

    public void delete(String unitId) {
        BloodUnit target = search(unitId);
        if (target != null) root = deleteRec(root, target);
    }

    private TreeNode deleteRec(TreeNode node, BloodUnit unit) {
        if (node == null) return null;
        int cmp = unit.compareTo(node.data);
        if (unit.getUnitId().equals(node.data.getUnitId())) {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            TreeNode successor = findMin(node.right);
            node.data = successor.data;
            node.right = deleteRec(node.right, successor.data);
        } else if (cmp < 0) {
            node.left = deleteRec(node.left, unit);
        } else {
            node.right = deleteRec(node.right, unit);
        }
        return node;
    }

    private TreeNode findMin(TreeNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /** In-order traversal: soonest-expiring units first. */
    public List<BloodUnit> inorder() {
        List<BloodUnit> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }

    private void inorderRec(TreeNode node, List<BloodUnit> result) {
        if (node == null) return;
        inorderRec(node.left, result);
        result.add(node.data);
        inorderRec(node.right, result);
    }

    public List<BloodUnit> preorder() {
        List<BloodUnit> result = new ArrayList<>();
        preorderRec(root, result);
        return result;
    }

    private void preorderRec(TreeNode node, List<BloodUnit> result) {
        if (node == null) return;
        result.add(node.data);
        preorderRec(node.left, result);
        preorderRec(node.right, result);
    }

    public List<BloodUnit> postorder() {
        List<BloodUnit> result = new ArrayList<>();
        postorderRec(root, result);
        return result;
    }

    private void postorderRec(TreeNode node, List<BloodUnit> result) {
        if (node == null) return;
        postorderRec(node.left, result);
        postorderRec(node.right, result);
        result.add(node.data);
    }

    public boolean isEmpty() { return root == null; }
}
