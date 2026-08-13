package bloodbank.structures;

import bloodbank.model.BloodUnit;

public class AVLNode {
    public BloodUnit data;
    public AVLNode left;
    public AVLNode right;
    public int height;

    public AVLNode(BloodUnit data) {
        this.data = data;
        this.height = 1;
    }
}