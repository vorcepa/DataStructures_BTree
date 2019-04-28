public class BTree{
    private BTreeNode root;
    int maxNodeSize;

    public BTree(int degree, BTreeNode parent){
        maxNodeSize = 2*degree - 1;
        BTreeNode treeRoot = new BTreeNode(maxNodeSize, parent);
        root = treeRoot;
    }

    public void insert(BTree tree, TreeObject key){ // tree T, key k
        BTreeNode subTreeRoot = root; // r = root[T]
        if (subTreeRoot.getSize() >= maxNodeSize){ // if n[r] >= 2t - 1
            BTreeNode parentForSplit = new BTreeNode(maxNodeSize, null); // s = allocate-node()
            tree.root = parentForSplit; // root[T] = s
            parentForSplit.setLeaf(false); // leaf[s] = false
            parentForSplit.addChild(subTreeRoot, 0); // child is the old root node -- THIS MIGHT BE INCORRECT
            splitChild(parentForSplit, 0); // B-Tree-Split-Child (s, left, right) r is split
        }
        else{
            insertNonFull();
        }
    }

    private void insertNonFull(){}

    private void splitChild(BTreeNode nodeToSplit, int centerKeyIndex){
        BTreeNode rightChild = new BTreeNode(maxNodeSize, nodeToSplit);

    }

//    private BTreeNode nodeSplit(BTreeNode nodeToSplit){
//        int centerKeyIndex = (nodeToSplit.getSize() / 2) + 1;
//
//        BTreeNode leftChild = new BTreeNode(maxNodeSize, nodeToSplit);
//        for (int i = 0; i < centerKeyIndex; i++){
//            leftChild.insertKey(nodeToSplit.getKey(i), i);
//        }
//
//        BTreeNode rightChild = new BTreeNode(maxNodeSize, nodeToSplit);
//        for (int i = centerKeyIndex + 1; i < maxNodeSize; i++){
//            rightChild.insertKey(nodeToSplit.getKey(i), i);
//        }
//
//        for (int i = 0; i < nodeToSplit.getSize(); i++){
//            if (i != centerKeyIndex){
//                nodeToSplit.removeKey(i);
//            }
//        }
//
//        return nodeToSplit;
//    }

    public BTreeNode getRoot(){
        return root;
    }
}