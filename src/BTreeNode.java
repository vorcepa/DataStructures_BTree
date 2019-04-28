import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class BTreeNode{
    private ArrayList<TreeObject> nodeKeys;
    private int maxSize;
    private LinkedList<BTreeNode> children;
    private BTreeNode parent;
    private boolean isLeaf;

    public BTreeNode(int maxSize, BTreeNode parent){
        nodeKeys = new ArrayList<TreeObject>();
        this.parent = parent;
        this.maxSize = maxSize;
        children = new LinkedList<BTreeNode>();
        isLeaf = true;
    }

    public int getSize(){
        return nodeKeys.size();
    }

    public int getMaxSize(){
        return maxSize;
    }

    public void insertKey(TreeObject key){
        if (nodeKeys.size() >= maxSize){
            throw new IndexOutOfBoundsException("Node is full; node should have been split beforehand");
        }
        if (nodeKeys.size() == 0){
            nodeKeys.add(key);
        }
        else{
            int currentKey = 0;
            while (currentKey < nodeKeys.size()){
                if (key.getSequence() < nodeKeys.get(currentKey).getSequence()){
                    break;
                }
                currentKey++;
            }
            if (isLeaf){
                nodeKeys.add(currentKey, key);
            }
            else{
                children.get(currentKey).insertKey(key);
            }
        }

    }

    public TreeObject getKey(int index){
        if (nodeKeys.size() == 0){
            throw new NoSuchElementException();
        }

        if (index < 0 || index >= nodeKeys.size()){
            throw new IndexOutOfBoundsException();
        }

        return nodeKeys.get(index);
    }

    public TreeObject removeKey(int index){
        if (nodeKeys.size() < 1){
            throw new NoSuchElementException();
        }

        if (index < 0 || index >= nodeKeys.size()){
            throw new IndexOutOfBoundsException();
        }

        return nodeKeys.remove(index);
    }

    public boolean isLeaf(){
        return isLeaf;
    }

    public String toString(){
        StringBuilder retVal = new StringBuilder();
        retVal.append("[");
        for (int i = 0; i < nodeKeys.size(); i++){
            retVal.append(nodeKeys.get(i).getSequence());
            retVal.append(", ");
        }
        retVal.deleteCharAt(retVal.lastIndexOf(","));
        retVal.append("]");

        return retVal.toString();

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
}