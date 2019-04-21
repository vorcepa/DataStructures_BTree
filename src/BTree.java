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


        BTreeNode leftChild = new BTreeNode(maxNodeSize, nodeToSplit);
        for (int i = 0; i < centerKeyIndex; i++){
            leftChild.insertKey(nodeToSplit.getKey(i), i);
        }

        BTreeNode rightChild = new BTreeNode(maxNodeSize, nodeToSplit);
        for (int i = centerKeyIndex + 1; i < maxNodeSize; i++){
            rightChild.insertKey(nodeToSplit.getKey(i), i);
        }

        for (int i = 0; i < nodeToSplit.getSize(); i++){
            if (i != centerKeyIndex){
                nodeToSplit.removeKey(i);
            }
        }

        return nodeToSplit;
    }
}