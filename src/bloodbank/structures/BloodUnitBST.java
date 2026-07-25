package bloodbank.structures;

import bloodbank.model.BloodUnit;
import java.util.ArrayList;
import java.util.List;

public class BloodUnitBST {
    private TreeNode root;

    public void insert(BloodUnit unit){
        root = insertRec(root, unit);
    }
    private TreeNode insertRec(TreeNode root, BloodUnit unit){
        if(root == null){
            return  new TreeNode(unit);
        }
        int cmp = unit.compareTo(root.data);
        if(cmp < 0){
            root.left = insertRec(root.left, unit);
        }
        else{
            root.right = insertRec(root.right, unit);
        }
        return root;
    }

    public BloodUnit search(String unitId){
        return searchRec(root, unitId);
    }
    private BloodUnit searchRec(TreeNode root, String unitId){
        if(root == null){
            return null;
        }
        if(root.data.getUnitId().equals(unitId)){
            return root.data;
        }
        BloodUnit left = searchRec(root.left, unitId);
        if(left != null){
            return  left;
        }
        return searchRec(root.right, unitId);
    }

    public void delete(String unitId) {
        BloodUnit target = search(unitId);
        if (target != null) {
            root = deleteRec(root, target);
        }
    }
    private TreeNode deleteRec(TreeNode root, BloodUnit unit){
        if(root == null){
            return null;
        }
        int cmp = unit.compareTo(root.data);
        if(unit.getUnitId().equals(root.data.getUnitId())){
            if(root.left  == null){
                return root.right;
            }
            if(root.right == null){
                return root.left;
            }
            TreeNode successor = findMin(root.right);
            root.data = successor.data;
            root.right = deleteRec(root.right, successor.data);
        } else if (cmp < 0) {
            root.left = deleteRec(root.left, unit);
        }
        else{
            root.right = deleteRec(root.right, unit);
        }
        return root;


    }

    private TreeNode findMin(TreeNode root){
        while (root.left != null){
            root = root.left;
        }
        return root;
    }

    public List<BloodUnit> inorder(){
        List<BloodUnit> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }
    private void inorderRec(TreeNode root, List<BloodUnit> result){
        if(root == null){
            return;
        }
        inorderRec(root.left, result);
        result.add(root.data);
        inorderRec(root.right, result);
    }

    public List<BloodUnit> preorder(){
        List<BloodUnit> result = new ArrayList<>();
        preorderRec(root, result);
        return result;
    }
    private void preorderRec(TreeNode root, List<BloodUnit> result){
        if (root == null){
            return;
        }
        result.add(root.data);
        preorderRec(root.left, result);
        preorderRec(root.right, result);
    }
    public List<BloodUnit> postorder(){
        List<BloodUnit> result = new ArrayList<>();
        postorderRec(root, result);
        return  result;
    }
    private void postorderRec(TreeNode root, List<BloodUnit> result){
        if (root == null) {
            return;
        }
        postorderRec(root.left, result);
        postorderRec(root.right, result);
        result.add(root.data);
    }
    public boolean isEmpty(){
        return root == null;
    }




}
