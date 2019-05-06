import java.util.LinkedList;
import java.util.NoSuchElementException;

public class BTreeNode{
    private TreeObject[] nodeKeys;
    private LinkedList<BTreeNode> children;
    private BTreeNode parent;

    // META DATA
    private final long offset;
    private int numKeys;
    private int numChildren;
    private int parentOffset;
    private boolean isLeaf;


    public BTreeNode(BTreeNode parent, int maxNodeSize, long offset){
        nodeKeys = new TreeObject[maxNodeSize];
        this.parent = parent;
        children = new LinkedList<BTreeNode>();

        // META DATA INSTANTIATION
        isLeaf = true;
        this.offset = offset;
        numKeys = 0;
        numChildren = 0;
        parentOffset = 0;
    }

    public BTreeNode getChild(int index){
        return children.get(index);
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

    public LinkedList<BTreeNode> getChildren(){
        return children;
    }

    public void setParent(BTreeNode parent){
        this.parent = parent;
    }

    public void addChild(BTreeNode child, int index){
        children.add(index, child);
        if (children.size() > 0){
            isLeaf = false;
        }
    }

    public void setLeaf(boolean thisNodeIsALeaf){
        isLeaf = thisNodeIsALeaf;
    }

    public BTreeNode getParent(){
        return parent;
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

    public int getParentOffset(){
        return parentOffset;
    }
}