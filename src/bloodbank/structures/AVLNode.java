package bloodbank.structures;

import bloodbank.model.BloodUnit;

/**
 * Represents a single node in the AVL Tree.
 * - Stores a BloodUnit object.
 * - Tracks left and right child references.
 * - Maintains height for balancing operations.
 */
public class AVLNode {
    public BloodUnit data;   // The blood unit stored in this node
    public AVLNode left;     // Reference to left child
    public AVLNode right;    // Reference to right child
    public int height;       // Height of the node (used for balance factor)

    // Constructor: create a new node with given data
    public AVLNode(BloodUnit data) {
        this.data = data;
        this.height = 1; // new node starts with height 1
    }
}
