public class BTree{
    private BTreeNode root;
    int maxNodeSize;

    public BTree(int degree){
        maxNodeSize = 2*degree - 1;
        root = new BTreeNode(maxNodeSize, null);
    }

    public void keyInsert(TreeObject key, BTreeNode subTreeNodeRoot){
        if (subTreeNodeRoot.getSize() == maxNodeSize){
            subTreeNodeRoot = nodeSplit(subTreeNodeRoot);
        }
        for (int i = 0; i < subTreeNodeRoot.getSize(); i++){

        }
    }

    private BTreeNode nodeSplit(BTreeNode nodeToSplit){
        int centerKeyIndex = (nodeToSplit.getSize() / 2) + 1;
        TreeObject centerKey = nodeToSplit.getKey(centerKeyIndex);

    }
}