import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;


public class BTree {
    private BTreeNode root;
    private int maxNodeSize;
    private int degree;
    private final int sequenceLength;
    private RandomAccessFile rawBTreeDataFile;
    private long currentCursorPosition;
    private Cache treeCache;

    public BTree(int degree, int sequenceLength, RandomAccessFile file, boolean canWrite, int cacheSize){
        this.degree = degree;
        maxNodeSize = 2*degree - 1;
        rawBTreeDataFile = file;
        this.sequenceLength = sequenceLength;

        currentCursorPosition = 0;
        root = new BTreeNode(0, maxNodeSize, currentCursorPosition);

        if (canWrite) {
            diskWrite(root);
        }

        treeCache = new Cache(cacheSize);
    }

    public void insert(BTree tree, TreeObject key){ // tree T, key k
        try {
            BTreeNode treeRoot = root;
            if (treeRoot.getNumKeys() >= maxNodeSize) {
                currentCursorPosition = rawBTreeDataFile.length();
                BTreeNode parentForSplit = new BTreeNode(root.getParentAddress(), maxNodeSize, 0);
                tree.setRoot(parentForSplit);
                parentForSplit.setLeaf(false);
                treeRoot.setOffset(currentCursorPosition);
                parentForSplit.addChildAddress(0, treeRoot.getOffset());
                treeRoot.setParentOffset(parentForSplit.getOffset());

                splitChild(parentForSplit, 0, treeRoot);
                insertNonFull(parentForSplit, key);
            } else {
                insertNonFull(treeRoot, key);
            }
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void insertNonFull(BTreeNode node, TreeObject key){
        if (node.checkForDuplicates(key)){
            node.getKey(key).incrementDuplicates();
            diskWrite(node);
        }
        else {
            if (node.isLeaf()) {
                node.insertKey(key);
                diskWrite(node);
            } else {
                int insertLocation = node.getNumKeys() + 1;
                for (int i = node.getNumKeys() - 1; i >= 0; i--) {
                    if (key.getSequence() > node.getKey(i).getSequence()) {
                        insertLocation = i + 1;
                        break;
                    }
                }

                long childOffset;
                BTreeNode nextChild;
                if (insertLocation == node.getNumKeys() + 1) {
                    childOffset = node.getChildOffset(0);
                    nextChild = diskRead(childOffset);
                    if (nextChild.getNumKeys() >= maxNodeSize) {
                        splitChild(node, 0, nextChild);
                        insertNonFull(node, key);
                    } else {
                        insertNonFull(nextChild, key);
                    }
                } else {
                    childOffset = node.getChildOffset(insertLocation);
                    nextChild = diskRead(childOffset);
                    if (nextChild.getNumKeys() >= maxNodeSize) {
                        splitChild(node, insertLocation, nextChild);
                        insertNonFull(node, key);
                    } else {
                        insertNonFull(nextChild, key);
                    }
                }
            }
        }

    }

    private void splitChild(BTreeNode parentNode, int childIndex, BTreeNode childToSplit) {
        try {
            childToSplit.setParentOffset(parentNode.getOffset());
            currentCursorPosition = rawBTreeDataFile.length();
            BTreeNode otherChild = new BTreeNode(parentNode.getOffset(), maxNodeSize, currentCursorPosition); // z = allocate-node()
            otherChild.setLeaf(childToSplit.isLeaf());

            int start = degree;
            int stop = childToSplit.getNumKeys();
            for (int i = start; i < stop; i++) {
                otherChild.insertKey(childToSplit.removeKey(start));
            }

            if (!childToSplit.isLeaf()) {
                // move children addresses to other child
                int j = 0;
                for (int i = childToSplit.getNumChildren() - 1; i >= childToSplit.getNumKeys(); i--) {
                    otherChild.addChildAddress(j, childToSplit.removeChildAddress(start));
                    j++;
                }

            }

            parentNode.insertKey(childToSplit.removeKey(childToSplit.getNumKeys() - 1));

            diskWrite(childToSplit);
            otherChild.setOffset(rawBTreeDataFile.length());
            parentNode.addChildAddress(childIndex + 1, otherChild.getOffset());


            diskWrite(parentNode);
            if (!childToSplit.isLeaf()){
                // update parent pointers in all children that have a new parent
                // requires disk-read

                BTreeNode otherChildCurrentChild;
                BTreeNode splitChildCurrentChild;
                for (int k = 0; k < childToSplit.getNumChildren(); k++) {
                    otherChildCurrentChild = diskRead(otherChild.getChildOffset(k));
                    otherChildCurrentChild.setParentOffset(otherChild.getOffset());

                    splitChildCurrentChild = diskRead(childToSplit.getChildOffset(k));
                    splitChildCurrentChild.setParentOffset(childToSplit.getOffset());

                    diskWrite(otherChildCurrentChild);
                    diskWrite(splitChildCurrentChild);
                }
            }
            diskWrite(otherChild);
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void setRoot(BTreeNode newRoot){
        this.root = newRoot;
    }

    @SuppressWarnings("unchecked")
    private void diskWrite(BTreeNode node){
        try{

            treeCache.addObject(node);
            currentCursorPosition = node.getOffset();
            rawBTreeDataFile.seek(currentCursorPosition);

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
                if (j < node.getNumChildren()){
                    rawBTreeDataFile.writeLong(node.getChildOffset(j));
                }
                else {
                    rawBTreeDataFile.writeLong(0);
                }
            }

            rawBTreeDataFile.seek(rawBTreeDataFile.length());
            currentCursorPosition = rawBTreeDataFile.getFilePointer();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public BTreeNode diskRead(long address){
        BTreeNode retVal;
        retVal = treeCache.getObject()

        currentCursorPosition = address;

        try {
            rawBTreeDataFile.seek(currentCursorPosition);

            // READ METADATA
            rawBTreeDataFile.readLong(); // read Offset
            int numKeys = rawBTreeDataFile.readInt(); // read number of keys in node
            int numChildren = rawBTreeDataFile.readInt(); // read number of children node has
            long parentOffset = rawBTreeDataFile.readLong(); // read the parent offset address
            boolean isLeaf = rawBTreeDataFile.readBoolean(); // read if the node is a leaf

            // read node keys
            TreeObject[] keys = new TreeObject[numKeys];
            for (int i = 0; i < maxNodeSize; i++){
                if (i < numKeys){
                    long keySequence = rawBTreeDataFile.readLong(); // get key from file
                    int numDuplicates = rawBTreeDataFile.readInt(); // get number of duplicates from previous inserts
                    TreeObject key = new TreeObject(keySequence, numDuplicates, sequenceLength);
                    keys[i] = key;
                }
                else{
                    rawBTreeDataFile.readLong(); // advance cursor
                    rawBTreeDataFile.readInt(); // advance cursor
                }
            }

            // read children offsets
            long[] childOffsets = new long[numChildren];
            for (int j = 0; j < maxNodeSize + 1; j++){
                if (j < numChildren) {
                    childOffsets[j] = rawBTreeDataFile.readLong();
                }
                else{
                    rawBTreeDataFile.readLong(); // advance cursor
                }
            }

            rawBTreeDataFile.seek(rawBTreeDataFile.length());
            currentCursorPosition = rawBTreeDataFile.getFilePointer();

            retVal = new BTreeNode(maxNodeSize, address, numKeys, numChildren, parentOffset,
                    isLeaf, keys, childOffsets);

            return retVal;
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    public void writeDump(FileWriter fw, BTreeNode node) throws IOException{
        if (node.isLeaf()){
            for (int i = 0; i < node.getNumKeys(); i++){
                fw.write(node.getKey(i).toString() + ": " + node.getKey(i).getDuplicates() + "\n");
            }
        }
        else{
            int j = 0;
            BTreeNode child = diskRead(node.getChildOffset(j));
            writeDump(fw, child);
            for (; j < node.getNumKeys(); j++){
                fw.write(node.getKey(j).toString() + ": " + node.getKey(j).getDuplicates() + "\n");
                child = diskRead(node.getChildOffset(j + 1));
                writeDump(fw, child);
            }
        }
    }

    public BTreeNode getRoot(){
        diskWrite(root);
        return root;
    }

}