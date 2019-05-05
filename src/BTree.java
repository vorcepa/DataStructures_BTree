public class BTree{
    private BTreeNode root;
    private int maxNodeSize;
    private int degree;

    public BTree(int degree, BTreeNode parent){
        this.degree = degree;
        maxNodeSize = 2*degree - 1;

        BTreeNode treeRoot = new BTreeNode(parent);
        root = treeRoot;
    }

    public void insert(BTree tree, TreeObject key){ // tree T, key k
        BTreeNode treeRoot = root;
        if (treeRoot.getSize() >= maxNodeSize){
            BTreeNode parentForSplit = new BTreeNode(root.getParent());
            tree.setRoot(parentForSplit);
            parentForSplit.setLeaf(false);
            parentForSplit.addChild(treeRoot, 0);
            treeRoot.setParent(parentForSplit);
            splitChild(parentForSplit, 0, treeRoot);
            if (parentForSplit.checkForDuplicates(key)){
                return;
            }

            insertNonFull(parentForSplit, key);

        }
        else{
            insertNonFull(treeRoot, key);
        }
    }

    private void insertNonFull(BTreeNode node, TreeObject key){
        if (node.checkForDuplicates(key)){
            return;
        }
        if (node.isLeaf()){
            node.insertKey(key);
        }
        else{
            int insertLocation = node.getSize() + 1;
            BTreeNode currentChild;
            for (int i = node.getSize() - 1; i >= 0; i--){
                currentChild = node.getChild(i + 1);
                if (currentChild.getSize() >= maxNodeSize){
                    splitChild(node, i + 1, currentChild);
                    if (node.checkForDuplicates(key)){
                        return;
                    }
                    insertLocation = node.getSize() + 1;
                    i = node.getSize() - 1;
                }
                if (key.getSequence() > node.getKey(i).getSequence()){
                    insertLocation = i + 1;
                    break;
                }
            }

            BTreeNode child;
            if (insertLocation == node.getSize() + 1){
                child = node.getChild(0);
                if (child.getSize() >= maxNodeSize){
                    splitChild(node, 0, child);
                    if (node.checkForDuplicates(key)){
                        return;
                    }
                    insertNonFull(node, key);
                }
                else {
                    insertNonFull(child, key);
                }
            }
            else{
                child = node.getChild(insertLocation);
                if (child.getSize() >= maxNodeSize){
                    splitChild(node, insertLocation, child);
                    if (node.checkForDuplicates(key)){
                        return;
                    }
                }
                insertNonFull(child, key);
            }
        }

    }

    private void splitChild(BTreeNode parentNode, int childIndex, BTreeNode childToSplit) {
        BTreeNode otherChild = new BTreeNode(parentNode); // z = allocate-node()
        otherChild.setLeaf(childToSplit.isLeaf());

        int start = degree;
        int stop = childToSplit.getSize();
        for (int i = start; i < stop; i++) {
            otherChild.insertKey(childToSplit.removeKey(start));
        }

        if (!childToSplit.isLeaf()) {
            for (int i = childToSplit.getChildren().size() - 1; i > start - 1; i--) {
                otherChild.addChild(childToSplit.getChildren().removeLast(), 0);
            }
        }

        parentNode.insertKey(childToSplit.removeKey(childToSplit.getSize() - 1));
        parentNode.addChild(otherChild, childIndex + 1);

        // TO DO: DISK-WRITE PARENT, CHILDTOSPLIT, OTHERCHILD
    }

    public BTreeNode getRoot(){
        return root;
    }

    public String toStringForDebug(){
        StringBuilder retVal = new StringBuilder();
        retVal.append(root.toStringForDebug() + "\n");
        for (int i = 0; i < root.getChildren().size(); i++){
            retVal.append(root.getChildren().get(i).toStringForDebug());
            retVal.append("\n");
            for (int j = 0; j < root.getChildren().get(i).getChildren().size(); j++){
                retVal.append(root.getChildren().get(i).getChildren().get(j).toStringForDebug());
                retVal.append("\n");
            }
        }
        return retVal.toString();
    }

    public void setRoot(BTreeNode newRoot){
        this.root = newRoot;
    }
}