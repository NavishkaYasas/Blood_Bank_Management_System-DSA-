package bloodbank.structures;

import bloodbank.model.BloodUnit;
//this show a single node in AVL tree

public class AVLNode {
    //store blood unit data in this node
    public BloodUnit data;
    //left child node
    public AVLNode left;
    //right child node
    public AVLNode right;
    //store the height of the node in AVL tree
    public int height;

    public AVLNode(BloodUnit data) {
        // assigning of data to a given unit
        this.data = data;
        // a new node with height of 1
        this.height = 1;
    }
}