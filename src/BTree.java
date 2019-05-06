import java.io.IOException;
import java.io.RandomAccessFile;

public class BTree {
    private BTreeNode root;
    private int maxNodeSize;
    private int degree;
    private RandomAccessFile rawBTreeDataFile;
    private long currentCursorPosition;

    public BTree(int degree, BTreeNode parent, RandomAccessFile file){
        this.degree = degree;
        maxNodeSize = 2*degree - 1;
        rawBTreeDataFile = file;

        currentCursorPosition = 0;
        root = new BTreeNode(parent, maxNodeSize, currentCursorPosition);
        diskWrite(root);
    }

    public void insert(BTree tree, TreeObject key){ // tree T, key k
        BTreeNode treeRoot = root;
        if (treeRoot.getNumKeys() >= maxNodeSize){
            BTreeNode parentForSplit = new BTreeNode(root.getParent(), maxNodeSize, currentCursorPosition);
            tree.setRoot(parentForSplit);
            parentForSplit.setLeaf(false);
            parentForSplit.addChildAddress(0, currentCursorPosition);
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
            int insertLocation = node.getNumKeys() + 1;
            BTreeNode currentChild;
            for (int i = node.getNumKeys() - 1; i >= 0; i--){


                currentChild = diskRead(node.getOffset(), node.getChildOffset(i + 1));


                if (currentChild.getNumKeys() >= maxNodeSize){
                    splitChild(node, i + 1, currentChild);
                    if (node.checkForDuplicates(key)){
                        return;
                    }
                    insertLocation = node.getNumKeys() + 1;
                    i = node.getNumKeys() - 1;
                }
                if (key.getSequence() > node.getKey(i).getSequence()){
                    insertLocation = i + 1;
                    break;
                }
            }

            BTreeNode child;
            if (insertLocation == node.getNumKeys() + 1){
                child = node.getChild(0);
                if (child.getNumKeys() >= maxNodeSize){
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
                if (child.getNumKeys() >= maxNodeSize){
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
        BTreeNode otherChild = new BTreeNode(parentNode, maxNodeSize, currentCursorPosition); // z = allocate-node()
        otherChild.setLeaf(childToSplit.isLeaf());

        int start = degree;
        int stop = childToSplit.getNumKeys();
        for (int i = start; i < stop; i++) {
            otherChild.insertKey(childToSplit.removeKey(start));
        }

        if (!childToSplit.isLeaf()) {
            // move children addresses to other child
            int j = 0;
            for (int i = childToSplit.getNumChildren() - 1; i > start - 1; i--) {
                otherChild.addChildAddress(j, childToSplit.removeChildAddress(childToSplit.getNumChildren() - 1));
                j++;
            }
            // update parent pointers in all children that have a new parent
                // requires disk-read


        }

        parentNode.insertKey(childToSplit.removeKey(childToSplit.getNumKeys() - 1));
        parentNode.addChildAddress(childIndex + 1, otherChild.getOffset());

        diskWrite(parentNode);
        diskWrite(childToSplit);
        diskWrite(otherChild);
    }

    public BTreeNode getRoot(){
        return root;
    }

//    public String toStringForDebug(){
//        StringBuilder retVal = new StringBuilder();
//        retVal.append(root.toStringForDebug() + "\n");
//        for (int i = 0; i < root.getChildren().size(); i++){
//            retVal.append(root.getChildren().get(i).toStringForDebug());
//            retVal.append("\n");
//            for (int j = 0; j < root.getChildren().get(i).getChildren().size(); j++){
//                retVal.append(root.getChildren().get(i).getChildren().get(j).toStringForDebug());
//                retVal.append("\n");
//            }
//        }
//        return retVal.toString();
//    }

    public void setRoot(BTreeNode newRoot){
        this.root = newRoot;
    }

    private void diskWrite(BTreeNode node){
        try{
            System.out.println("Cursor Pos: " + currentCursorPosition);
            // META DATA
            rawBTreeDataFile.writeLong(node.getOffset()); // write offset address
            rawBTreeDataFile.writeInt(node.getNumKeys()); // write number of keys in node
            rawBTreeDataFile.writeInt(node.getNumChildren()); // write number of children node has
            rawBTreeDataFile.writeLong(node.getParentAddress()); // write parent offset address
            rawBTreeDataFile.writeBoolean(node.isLeaf()); // write if the node is a leaf

            // NODE DATA
            for (int i = 0; i < maxNodeSize; i++){
                if (i < node.getNumKeys()){ // write key values to their locations on disk
                    rawBTreeDataFile.writeLong(node.getKey(i).getSequence()); // write the binary sequence to file
                    rawBTreeDataFile.writeInt(node.getKey(i).getDuplicates()); // write the number of duplicates to file
                }
                else { // write 0s in empty key locations
                    rawBTreeDataFile.writeLong(0);
                    rawBTreeDataFile.writeInt(0);
                }
            }

            for (int j = 0; j < maxNodeSize + 1; j++){
                if (j < node.getChildrenOffsets().length){

                }
                rawBTreeDataFile.writeInt(0);
            }

            currentCursorPosition = rawBTreeDataFile.getFilePointer();
            System.out.println("Cursor Pos: " + currentCursorPosition);
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private BTreeNode diskRead(long parentAddress, long address, int sequenceLength){
        BTreeNode retVal = new BTreeNode(parentAddress, maxNodeSize, address);
        currentCursorPosition = address;

        try {
            // READ METADATA
            long offSet = rawBTreeDataFile.readLong(); // read Offset
            int numKeys = rawBTreeDataFile.readInt(); // read number of keys in node
            int numChildren = rawBTreeDataFile.readInt(); // read number of children node has
            long parentOffset = rawBTreeDataFile.readLong(); // read the parent offset address
            boolean isLeaf = rawBTreeDataFile.readBoolean(); // read if the node is a leaf

            for (int i = 0; i < maxNodeSize; i++){
                if (i < numKeys){
                    long keySequence = rawBTreeDataFile.readLong(); // get key from file
                    int numDuplicates = rawBTreeDataFile.readInt(); // get number of duplicates from previous inserts
                    TreeObject key = new TreeObject(keySequence, numDuplicates, sequenceLength);
                    retVal.insertKey();
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

        return retVal;
    }
}