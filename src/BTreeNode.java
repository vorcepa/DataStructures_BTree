import java.util.NoSuchElementException;

public class BTreeNode{
    private TreeObject[] nodeKeys;
    private long[] childrenOffsets;
    private long parentOffset;

    // META DATA
    private long offset;
    private int numKeys;
    private int numChildren;
    private boolean isLeaf;


    public BTreeNode(long parentAddress, int maxNodeSize, long offset){
        nodeKeys = new TreeObject[maxNodeSize];
        this.parentOffset = parentAddress;

        // META DATA INSTANTIATION
        isLeaf = true;
        this.offset = offset;
        numKeys = 0;
        parentOffset = 0;
        numChildren = 0;
        childrenOffsets = new long[maxNodeSize + 1];
    }

    public BTreeNode(int maxNodeSize, long offset, int numKeys, int numChildren, long parentOffset,
                     boolean isLeaf, TreeObject[] keys, long[] childrenOffsets){
        this.offset = offset;
        this.numKeys = numKeys;
        this.numChildren = numChildren;
        this.parentOffset = parentOffset;
        this.isLeaf = isLeaf;

        nodeKeys = new TreeObject[maxNodeSize];
        for (int i = 0; i < keys.length; i++){
            nodeKeys[i] = keys[i];
        }

        this.childrenOffsets = new long[maxNodeSize];
        for (int j = 0; j < childrenOffsets.length; j++){
            this.childrenOffsets[j] = childrenOffsets[j];
        }
    }

    public long getChildOffset(int index){
        return childrenOffsets[index];
    }

    public void insertKey(TreeObject key){
        int insertLocation = -1;
        TreeObject currentKey;
        for (int i = 0; i < numKeys; i++){
            currentKey = nodeKeys[i];
            if (key.getSequence() == currentKey.getSequence()){
                currentKey.incrementDuplicates();
                return;
            }
            if (key.getSequence() < nodeKeys[i].getSequence()){
                insertLocation = i;
                break;
            }
        }

        if (insertLocation == -1){
            nodeKeys[numKeys] = key;
        }
        else {
            for (int j = numKeys; j > insertLocation; j--){
                nodeKeys[j] = nodeKeys[j - 1];
            }
            nodeKeys[insertLocation] = key;
        }
        numKeys++;
    }

    public TreeObject getKey(int index){
        if (nodeKeys.length == 0){
            throw new NoSuchElementException();
        }

        return nodeKeys[index];
    }

    public TreeObject removeKey(int index){
        TreeObject retVal = nodeKeys[index];
        for (int i = index; i < numKeys - 2; i++){
            nodeKeys[i] = nodeKeys[i + 1];
        }
        numKeys--;
        return retVal;
    }

    public boolean isLeaf(){
        return isLeaf;
    }

    public long[] getChildrenOffsets(){
        return childrenOffsets;
    }

    public void setParentOffset(long offset){
        this.parentOffset = offset;
    }

    public void addChildAddress(int index, long offset){
        for (int i = numChildren; i > index; i--){
            childrenOffsets[i] = childrenOffsets[i - 1];
        }
        childrenOffsets[index] = offset;
        numChildren++;
    }

    public long removeChildAddress(int index){
        long retVal = childrenOffsets[index];

        for (int i = index; i < numChildren; i++){
            childrenOffsets[i] = childrenOffsets[i + 1];
        }
        numChildren--;

        return retVal;
    }

    public void setLeaf(boolean thisNodeIsALeaf){
        isLeaf = thisNodeIsALeaf;
    }

    public long getParentAddress(){
        return parentOffset;
    }

    public String toStringForDebug(){
        StringBuilder retVal = new StringBuilder();
        retVal.append("[");
        for (int i = 0; i < numKeys; i++){
            retVal.append(nodeKeys[i].toString());
            retVal.append(", ");
        }

        if (retVal.toString().length() > 1) {
            retVal.replace(retVal.lastIndexOf(", "), retVal.lastIndexOf(", ") + 2, "");
        }
        retVal.append("]\n");
        return retVal.toString();
    }

    public boolean checkForDuplicates(TreeObject key){
        boolean retVal = false;
        TreeObject currentKey;
        for (int i = 0; i < numKeys; i++){
            currentKey = nodeKeys[i];
            if (key.getSequence() == currentKey.getSequence()){
                retVal = true;
                currentKey.incrementDuplicates();
            }
        }
        return retVal;
    }

    public long getOffset(){
        return offset;
    }

    public int getNumKeys(){
        return numKeys;
    }

    public int getNumChildren(){
        return numChildren;
    }
}