import java.util.LinkedList;
import java.util.NoSuchElementException;

public class BTreeNode{
    private TreeObject[] nodeKeys;
    private LinkedList<BTreeNode> children;
    private BTreeNode parent;
    private boolean isLeaf;

    private int numKeys;

    public BTreeNode(int maxSize, BTreeNode parent){
        nodeKeys = new TreeObject[maxSize];
        this.parent = parent;
        children = new LinkedList<BTreeNode>();
        numKeys = 0;
        isLeaf = true;
    }

    public int getSize(){
        return numKeys;
    }

    public void insertKey(TreeObject key, int index){
        if (index < 0 || index > numKeys){
            throw new IndexOutOfBoundsException();
        }

        for (int i = nodeKeys.length - 1; i > index; i--){
            if (nodeKeys[i] != null){
                nodeKeys[i] = nodeKeys[i - 1];
            }
        }

        nodeKeys[index] = key;
        numKeys++;
    }

    public TreeObject getKey(int index){
        if (numKeys == 0){
            throw new NoSuchElementException();
        }

        if (index < 0 || index >= nodeKeys.length){
            throw new IndexOutOfBoundsException();
        }

        if (index >= numKeys){
            throw new NoSuchElementException();
        }

        return nodeKeys[index];
    }

    public TreeObject removeKey(int index){
        if (numKeys == 0){
            throw new NoSuchElementException();
        }

        if (index < 0 || index >= numKeys){
            throw new IndexOutOfBoundsException();
        }

        TreeObject retVal = nodeKeys[index];
        for (int i = index; i < nodeKeys.length - 1; i++){
            nodeKeys[i] = nodeKeys[i + 1];
        }

        nodeKeys[nodeKeys.length - 1] = null;
        numKeys--;

        return retVal;
    }

    public String toString(){
        StringBuilder retVal = new StringBuilder();
        retVal.append("[");
        for (int i = 0; i < numKeys; i++){
            retVal.append(nodeKeys[i].getSequence());
            retVal.append(", ");
        }
        retVal.deleteCharAt(retVal.lastIndexOf(","));
        retVal.append("]");

        return retVal.toString();

    }


}