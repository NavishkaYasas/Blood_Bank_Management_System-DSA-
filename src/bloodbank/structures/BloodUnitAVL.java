package bloodbank.structures;

import bloodbank.model.BloodUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 4's component: Task 5 - Trees (self-balancing variant).
 * AVL tree keyed by expiry date, same ordering purpose as Member 3's BST
 * but self-balances after every insert/delete so operations stay O(log n)
 * even if units are inserted in already-sorted (worst-case-for-BST) order.
 */
public class BloodUnitAVL {
    private AVLNode root;

    private int height(AVLNode node) { return node == null ? 0 : node.height; }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode t2 = x.right;
        x.right = y;
        y.left = t2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode t2 = y.left;
        y.left = x;
        x.right = t2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    public void insert(BloodUnit unit) {
        root = insertRec(root, unit);
    }

    private AVLNode insertRec(AVLNode node, BloodUnit unit) {
        if (node == null) return new AVLNode(unit);
        int cmp = unit.compareTo(node.data);
        if (cmp < 0) node.left = insertRec(node.left, unit);
        else node.right = insertRec(node.right, unit);

        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1 && unit.compareTo(node.left.data) < 0) return rotateRight(node);
        if (balance < -1 && unit.compareTo(node.right.data) > 0) return rotateLeft(node);
        if (balance > 1 && unit.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && unit.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    public BloodUnit search(String unitId) {
        return searchRec(root, unitId);
    }

    private BloodUnit searchRec(AVLNode node, String unitId) {
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

    private AVLNode deleteRec(AVLNode node, BloodUnit unit) {
        if (node == null) return null;
        int cmp = unit.compareTo(node.data);
        if (unit.getUnitId().equals(node.data.getUnitId())) {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            AVLNode successor = findMin(node.right);
            node.data = successor.data;
            node.right = deleteRec(node.right, successor.data);
        } else if (cmp < 0) {
            node.left = deleteRec(node.left, unit);
        } else {
            node.right = deleteRec(node.right, unit);
        }

        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) return rotateRight(node);
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) return rotateLeft(node);
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private AVLNode findMin(AVLNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public List<BloodUnit> inorder() {
        List<BloodUnit> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }

    private void inorderRec(AVLNode node, List<BloodUnit> result) {
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

    private void preorderRec(AVLNode node, List<BloodUnit> result) {
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

    private void postorderRec(AVLNode node, List<BloodUnit> result) {
        if (node == null) return;
        postorderRec(node.left, result);
        postorderRec(node.right, result);
        result.add(node.data);
    }

    public int getTreeHeight() { return height(root); }
    public boolean isEmpty() { return root == null; }
}