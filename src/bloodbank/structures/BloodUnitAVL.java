package bloodbank.structures;

import bloodbank.model.BloodUnit;
import java.util.ArrayList;
import java.util.List;

/**
 implementing AVL tree to store blood unit objects
 the tree is organized according to the expiry date of blood units
 time complexity -
 insert : o(log n)
 delete : o(log n)
 balance :o(log n)
 */
public class BloodUnitAVL {
    //store root node of AVL tree
    private AVLNode root;
    // gives height of a given node
    // if node = null , the height is considered as 0
    private int height(AVLNode node) { return node == null ? 0 : node.height; }
//calculating balance factor of a node
    // balance factor = height of left subtree - height of right subtree
    private int getBalance(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }
//updating height of nodes by considering children
    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }
// right rotate to balance the tree
    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode t2 = x.right;
        x.right = y;
        y.left = t2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }
//rotate left to balance the tree
    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode t2 = y.left;
        y.left = x;
        x.right = t2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }
// method to insert new blood unit into AVL tree
    public void insert(BloodUnit unit) {
        root = insertRec(root, unit);
    }
// recursive method used to insert a blood unit
    private AVLNode insertRec(AVLNode node, BloodUnit unit) {
        // if current position is empty return and create a new AVL node
        if (node == null) return new AVLNode(unit);
        //comparing of new blood unit with current node
        int cmp = unit.compareTo(node.data);
        if (cmp < 0) node.left = insertRec(node.left, unit);
        else node.right = insertRec(node.right, unit);

        updateHeight(node);
        //calculate balance factor
        int balance = getBalance(node);
//left-left case :-
        //tree is heavy in left side , so new value is inserted in left subtree
        if (balance > 1 && unit.compareTo(node.left.data) < 0) return rotateRight(node);
 // right - right case :-
 //tree is heavy in right side , so insert new value in right subtree
        if (balance < -1 && unit.compareTo(node.right.data) > 0) return rotateLeft(node);
        //left - right case :-
        //1 st left rotation on left child
        // then right rotation on current node
        if (balance > 1 && unit.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        //right - left case :-
        // 1st right rotation on right child
        // then left rotation on current node
        if (balance < -1 && unit.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        //return current node if no rotation is needed .

        return node;
    }
// searching a blood unit by using its unit ID
    public BloodUnit search(String unitId) {
        return searchRec(root, unitId);
    }
// recursive search method
    private BloodUnit searchRec(AVLNode node, String unitId) {
        if (node == null) return null;
        //check current node contain requested ID or not ,
        if (node.data.getUnitId().equals(unitId)) return node.data;
        //searching left subtree
        BloodUnit left = searchRec(node.left, unitId);
        //if that unit found in left subtree , then return it,
        if (left != null) return left;
        //or else search right subtree
        return searchRec(node.right, unitId);
    }
// delete a blood unit from AVL tree by using unit ID if needed
    public void delete(String unitId) {
        //searching that
        BloodUnit target = search(unitId);
        //if that required blood unit exists remove that
        if (target != null) root = deleteRec(root, target);
    }
// recursive method use to dlt the blood unit
    private AVLNode deleteRec(AVLNode node, BloodUnit unit) {
        if (node == null) return null;
        //compare target unit with current node
        int cmp = unit.compareTo(node.data);
        //checking whether the current node contain target unit or not,
        if (unit.getUnitId().equals(node.data.getUnitId())) {
            //if there is no left child , replace current node with its right child
            if (node.left == null) return node.right;
            //if there is no right child replace the current node with its left child
            if (node.right == null) return node.left;
            //if the node has 2 children,find smallest node in right subtree
            AVLNode successor = findMin(node.right);
            node.data = successor.data;
            //delete successor node from right subtree
            node.right = deleteRec(node.right, successor.data);
        } else if (cmp < 0) {
            //if the finding one is smaller search in left subtree
            node.left = deleteRec(node.left, unit);
        } else {
            //if the finding one is larger search in right subtree
            node.right = deleteRec(node.right, unit);
        }

        updateHeight(node);
        //calculate the balance factor
        int balance = getBalance(node);
//left - left case after deletion
        if (balance > 1 && getBalance(node.left) >= 0) return rotateRight(node);
        //left - right case after deletion
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        //right-right case after deletion
        if (balance < -1 && getBalance(node.right) <= 0) return rotateLeft(node);
        //right-left case after deletion
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }
//find the node with smallest value in a subtree
    private AVLNode findMin(AVLNode node) {
        //moving left until the found the smallest node
        while (node.left != null) node = node.left;
        return node;
    }
//performs an inorder traversal for a AVL tree
    public List<BloodUnit> inorder() {
        List<BloodUnit> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }
//recursive inrorder traversal
    private void inorderRec(AVLNode node, List<BloodUnit> result) {
        if (node == null) return;
        inorderRec(node.left, result);
        result.add(node.data);
        inorderRec(node.right, result);
    }
// recursive preorder traversal
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
// postorder traversal from AVL tree
    public List<BloodUnit> postorder() {
        List<BloodUnit> result = new ArrayList<>();
        //start postorder traversal from root
        postorderRec(root, result);
        return result;
    }
// recursive postorder traversal
    private void postorderRec(AVLNode node, List<BloodUnit> result) {
        //stop when there is no node
        if (node == null) return;
        postorderRec(node.left, result);
        postorderRec(node.right, result);
        result.add(node.data);
    }
// gives the height of the full AVL tree
    public int getTreeHeight() { return height(root); }
    //check the AVL tree is empty or not
    public boolean isEmpty() { return root == null; }
}