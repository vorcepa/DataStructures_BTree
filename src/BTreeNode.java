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
        if (index < 0 || index >= nodeKeys.length){
            throw new IndexOutOfBoundsException();
        }

        for (int i = nodeKeys.length; i > index; i--){
            if (nodeKeys[i] != null){
                nodeKeys[i] = nodeKeys[i - 1];
            }
        }

        nodeKeys[index] = key;
        numKeys++;
    }

    public TreeObject getKey(int index){
        if (index < 0 || index >= nodeKeys.length){
            throw new IndexOutOfBoundsException();
        }

        if (index >= numKeys){
            throw new NoSuchElementException();
        }

        return nodeKeys[index];
    }


}