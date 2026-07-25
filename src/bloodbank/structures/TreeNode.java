package bloodbank.structures;
import  bloodbank.model.BloodUnit;
public class TreeNode {
    public BloodUnit data;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(BloodUnit data){
        this.data = data;
    }
}
