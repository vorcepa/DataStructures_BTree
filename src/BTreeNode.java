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

    public BTreeNode getChild(int index){
        return children.get(index);
    }

    public int insertKey(TreeObject key){
        int insertLocation = -1;
        TreeObject currentKey;
        for (int i = 0; i < nodeKeys.size(); i++){
            currentKey = nodeKeys.get(i);
            if (key.getSequence() == currentKey.getSequence()){
                currentKey.incrementDuplicates();
                return -1;
            }
            if (key.getSequence() < nodeKeys.get(i).getSequence()){
                insertLocation = i;
                break;
            }
        }
        if (insertLocation == -1){
            nodeKeys.add(key);
        }
        else {
            nodeKeys.add(insertLocation, key);
            insertLocation = nodeKeys.size();
        }

        return insertLocation;
    }

    public TreeObject getKey(int index){
        if (nodeKeys.size() == 0){
            throw new NoSuchElementException();
        }
        return nodeKeys.get(index);
    }

    public int indexOfGreaterKeyInNode(TreeObject other){
        int retVal = -1;
        for (int i = 0; i < nodeKeys.size(); i++){
            if (nodeKeys.get(i).getSequence() > other.getSequence()){
                retVal = i;
                break;
            }
        }

        return retVal;
    }

    public TreeObject removeKey(int index){
        return nodeKeys.remove(index);
    }

    public boolean isLeaf(){
        return isLeaf;
    }

//    public String toString(){
//        StringBuilder retVal = new StringBuilder();
//        retVal.append("[");
//        for (int i = 0; i < nodeKeys.size(); i++){
//            retVal.append(nodeKeys.get(i).getSequence());
//            retVal.append(", ");
//        }
//        retVal.deleteCharAt(retVal.lastIndexOf(","));
//        retVal.append("]");
//
//        return retVal.toString();
//
//    }

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
        for (int i = 0; i < nodeKeys.size(); i++){
            retVal.append(nodeKeys.get(i).toString());
            retVal.append(", ");
        }

        if (retVal.toString().length() > 1) {
            retVal.replace(retVal.lastIndexOf(", "), retVal.lastIndexOf(", ") + 2, "");
        }
        retVal.append("]\n");
//
//        StringBuilder retValConcat = new StringBuilder();
//        retValConcat.append("[");
//
//        for (int i = 0; i < nodeKeys.size(); i++){
//            retValConcat.append(nodeKeys.get(i).binaryToString());
//            retValConcat.append(", ");
//        }
//        if (retValConcat.toString().length() > 1){
//            retValConcat.replace(retValConcat.lastIndexOf(", "), retValConcat.lastIndexOf(", ") + 2, "");
//        }
//        retValConcat.append("]");
//        retVal.append(retValConcat.toString());

        return retVal.toString();
    }

    public boolean checkForDuplicates(TreeObject key){
        boolean retVal = false;
        TreeObject currentKey;
        for (int i = 0; i < nodeKeys.size(); i++){
            currentKey = nodeKeys.get(i);
            if (key.getSequence() == currentKey.getSequence()){
                retVal = true;
                currentKey.incrementDuplicates();
            }
        }
        return retVal;
    }
}