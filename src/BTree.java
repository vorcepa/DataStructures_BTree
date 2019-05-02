public class BTree{
    private BTreeNode root;
    private int maxNodeSize;

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

            insertNonFull(parentForSplit, key);
        }
        else{
            insertNonFull(treeRoot, key);
        }
    }

    private void insertNonFull(BTreeNode node, TreeObject key){
        if (node.isLeaf()){
            node.insertKey(key);
        }
        else{
            int currentKeyIndex = node.getSize() - 1;
            BTreeNode currentChild = node.getChild(currentKeyIndex);
            while(currentKeyIndex >= 0 && key.getSequence() < node.getKey(currentKeyIndex).getSequence()){
                currentChild = node.getChild(currentKeyIndex);
                if (currentChild.getSize() >= maxNodeSize){
                    splitChild(node, currentKeyIndex, currentChild);
                    if (key.getSequence() > node.getKey(currentKeyIndex).getSequence()){
                        currentKeyIndex++;
                    }
                }
                currentKeyIndex--;
            }
            insertNonFull(currentChild, key);
        }

    }

    private void splitChild(BTreeNode parentNode, int childIndex, BTreeNode childToSplit) {
        BTreeNode otherChild = new BTreeNode(maxNodeSize, parentNode); // z = allocate-node()
        otherChild.setLeaf(childToSplit.isLeaf());

        int degree = (maxNodeSize + 1) / 2;
        for (int i = degree; i < childToSplit.getSize(); i++) {
            otherChild.insertKey(childToSplit.removeKey(degree + 1));
        }

        if (!childToSplit.isLeaf()) {
            for (int i = childToSplit.getChildren().size() - 1; i > degree - 1; i--) {
                otherChild.addChild(childToSplit.getChildren().removeLast(), otherChild.getChildren().size());
            }
        }

        parentNode.insertKey(childToSplit.getKey(degree));
        parentNode.addChild(otherChild, childIndex + 1);

        // TO DO: DISK-WRITE PARENT, CHILDTOSPLIT, OTHERCHILD
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