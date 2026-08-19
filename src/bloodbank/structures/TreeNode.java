package bloodbank.structures;

import bloodbank.model.BloodUnit;

/**
 * Node used by BloodUnitBST.
 * - Stores a BloodUnit record.
 * - Has left and right child references.
 * - Forms the building blocks of the Binary Search Tree.
 */
public class TreeNode {
    public BloodUnit data;   // The blood unit stored in this node
    public TreeNode left;    // Reference to left child
    public TreeNode right;   // Reference to right child

    // Constructor: create a new node with given BloodUnit
    public TreeNode(BloodUnit data) {
        this.data = data;
    }
}
