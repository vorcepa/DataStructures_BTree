
public class BTreeNode {
	
	private TreeObject[] keys;
	private int[] childPointers;
	public int maxObjects;
	private int numObjects;
	int offset;
	boolean isLeaf;
	boolean isRoot;
	
	/**
	 * BTreeNode constructor
	 * @param offset - The byte offset of the root/first node starts.
	 * @param maxObjects - Maximum number of objects a node can hold.
	 */
	public BTreeNode(int degree) {
		this.offset = 0;
		this.keys = new TreeObject[2*degree-1]; //replace with tree object type.
		this.numObjects = 0;
		this.isLeaf = true;
		this.childPointers = new int[2*degree];
	}

	public int getNumObjects()
	{
		return numObjects;
	}
	
	public TreeObject getKey(int index)
	{
		return keys[index];
	}
	
	public void setKeys(int index, TreeObject t)
	{
		keys[index] = t; 
	}
	
	public int getMaxObjects()
	{
		return maxObjects;
	}

	public int getNumChildPointers()
	{
		return childPointers.length;
	}
	
	public boolean getIsLeaf()
	{
		return isLeaf;
	}
	
	public int getChildPointer(int index)
	{
		return childPointers[index];
	}
	
	public void setChildPointer(int index, int value)
	{
		childPointers[index] = value;
	}
	

}