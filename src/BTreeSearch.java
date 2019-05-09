import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class BTreeSearch {
    public static void main(String[] args){
        try {
            RandomAccessFile file = new RandomAccessFile("testFile.txt", "r");
            BTree tree = new BTree(2, 4, file, false, 0);
            BTreeNode currentNode = tree.diskRead(0);
            TreeObject currentKey = new TreeObject("caca");

            if (currentNode.contains(currentKey)){
                System.out.println(currentKey.toString() + ": Found");
            }
            else {
                while (!currentNode.isLeaf()) {
                    currentNode = findNextChild(tree, currentNode, currentKey);
                    if (currentNode.contains(currentKey)){
                        System.out.println(currentKey.toString() + ": Found");
                        System.out.println(currentNode.toStringForDebug());
                        System.out.println("Parent Address: " + currentNode.getParentAddress());
                        break;
                    }
                }
                if (!currentNode.contains(currentKey)){
                    System.out.println(currentKey.toString() + ": Not found");
                }
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static BTreeNode findNextChild(BTree tree, BTreeNode node, TreeObject key){
        BTreeNode retVal = null;
        for (int i = 0; i < node.getNumKeys(); i++){
            if (key.getSequence() < node.getKey(i).getSequence()){
                long childOffset = node.getChildOffset(i);
                retVal = tree.diskRead(childOffset);
                break;
            }
        }

        if (retVal == null){
            long childOffset = node.getChildOffset(node.getNumChildren() - 1);
            retVal = tree.diskRead(childOffset);
        }

        return retVal;
    }
}
