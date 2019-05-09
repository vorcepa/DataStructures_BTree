import java.util.LinkedList;
import java.util.ListIterator;

/**
 * This Cache class implements a LinkedList to simulate a computer's cache.
 * The cache can store any generic object, return an object when called,
 * and will continuously enforce a limit set by the class that instantiated a cache object.
 *
 * @param <T> The LinkedList is instanstiated and can accept generic types
 * @author Phillip Vorce
 * @version Spring 2019
 */
public class Cache{
    private LinkedList<BTreeNode> cacheStorage;
    private int cacheSize;

    /**
     * Constructor for the cache class
     * @param cacheSize sets the maximum size the cache can be before it starts automatically
     *                  removing the oldest and least-used item as more items are added.
     */
    public Cache(int cacheSize){
        cacheStorage = new LinkedList<BTreeNode>();
        this.cacheSize = cacheSize;
    }

    /**
     * Returns the item that was sent when called, if it is already in the cache
     * @param item any generic item to be checked to see if its in the cache
     * @return the item that was sent, if it was found in the cache.  Null otherwise
     */
    public BTreeNode getObject(long offset){
        ListIterator iter = cacheStorage.listIterator();
        BTreeNode currentNode;
        while (iter.hasNext()){
            currentNode = (BTreeNode) iter.next();
            if (offset == currentNode.getOffset()){
                return currentNode;
            }
        }

        return null;
    }

    /**
     * Adds the item sent to the cache, to the start of the list.  If the cache is full,
     * the oldest item is removed from the end of the list.
     * @param item any generic item to be added to the cache
     */
    public void addObject(BTreeNode node){
        cacheStorage.addFirst(node);
        if (cacheStorage.size() > cacheSize){
            cacheStorage.removeLast();
        }
    }

    /**
     * Removes the sent item from the cache
     * @param item the item to be removed from the cache
     */
    public void removeObject(BTreeNode node){
        cacheStorage.remove(node);
    }

    /**
     * Completely empties the cache of any items.  Size is preserved.
     */
    public void clearCache(){
        cacheStorage.clear();
    }
}
