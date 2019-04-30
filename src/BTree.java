public class BTree{
    private BTreeNode root;
    int maxNodeSize;

    public BTree(int degree, BTreeNode parent){
        maxNodeSize = 2*degree - 1;
        BTreeNode treeRoot = new BTreeNode(maxNodeSize, parent);
        root = treeRoot;
    }

    public void insert(BTree tree, TreeObject key){ // tree T, key k
        BTreeNode treeRoot = root;
        if (treeRoot.getSize() >= maxNodeSize){
            BTreeNode parentForSplit = new BTreeNode(maxNodeSize, root.getParent());
            tree.setRoot(parentForSplit);
            parentForSplit.setLeaf(false);
            parentForSplit.addChild(treeRoot, 0);
            treeRoot.setParent(parentForSplit);
            splitChild(parentForSplit, 0, treeRoot);
        }
    }

    private void insertNonFull(){}

    private void splitChild(BTreeNode parentNode, int childIndex, BTreeNode childToSplit){
        BTreeNode otherChild = new BTreeNode(maxNodeSize, parentNode); // z = allocate-node()
        otherChild.setLeaf(childToSplit.isLeaf());

        int degree = (maxNodeSize + 1) / 2;
        for (int i = degree; i < childToSplit.getSize(); i++){
            otherChild.insertKey(childToSplit.removeKey(degree + 1));
        }

        for (int i = degree + 1; i < childToSplit.getChildren().size(); i++){
            otherChild.addChild(childToSplit.getChildren().remove(degree + 1), i - degree + 1);
        }
    }

    public BTreeNode getRoot(){
        return root;
    }

    public String toStringForDebug(){
        return root.toStringForDebug();
    }

    public void setRoot(BTreeNode newRoot){
        this.root = newRoot;
    }
}