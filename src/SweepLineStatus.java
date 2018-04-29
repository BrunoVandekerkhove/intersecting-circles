import java.util.NoSuchElementException;

/**
 * A balanced search tree implemented as a red-black tree.
 * 	Insertions, deletions, ... take logarithmic time in the worst case.
 * 
 * This particular class is 
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class SweepLineStatus<Key extends Comparable<Key>> {

	/**
	 * Constant denoting a red color.
	 * 
	 * @category General
	 */
    private static final boolean RED   = true;
    
    /**
	 * Constant denoting a black color.
	 * 
	 * @category General
	 */
    private static final boolean BLACK = false;

    /**
     * Initializes a new red-black tree.
     * 
     * @post The new tree is empty.
     * @category General
     */
    public SweepLineStatus() {
    	// Empty status
    }
    
    /**
     * Variable registering the root of this key.
     * @category General
     */
    private Node root;
    
    /**
     * Get the number of nodes in this tree.
     * 
     * @return	The number of nodes in this tree.
     * @category General
     */
    public int size() {
        return size(root);
    }
    
    /**
     * Get the number of nodes in the subtree rooted at the given node.
     * @param 	x
     * 			The node whose subtree's size is desired.
     * @return	The size of the subtree rooted at the given node.
     * 			| return (x == null ? 0 : x.size)
     * @category HelperFunction
     */
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }
    
    /**
     * Check whether or not this tree is empty.
     * 
     * @return	True if and only if this tree is empty.
     * 			| return (root == null)
     * @category General
     */
    public boolean isEmpty() {
        return root == null;
    }
    
    /**
     * Check whether or not this tree contains the given key.
     * 
     * @param 	key 
     * 			The key to look for.
     * @return	True if and only if this tree contains the given key.
     * @throws 	IllegalArgumentException 
     * 			The given key is null.
     * @category General
     */
    public boolean contains(Key key) {
    	if (key == null) throw new IllegalArgumentException("The key is null");
    	Node x = root;
    	while (x != null) {
            int cmp = key.compareTo(x.key);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else 
            	return true;
        }
        return false;
    }
    
    /**
     * Insert the given key into this tree.
     * 
     * @param 	key
     * 			The key to insert.
     * @throws	IllegalArgumentException
     * 			The given key is null.
     * @category General
     */
    public void insert(Key key) {
    	if (key == null) 
        	throw new IllegalArgumentException("The given key is null");
        root = insert(root, key);
        root.color = BLACK;
    }

    /**
     * Insert the given key into the subtree rooted by the given node.
     * 
     * @param 	h
     * 			The root of the subtree into which the given key needs to be inserted.
     * @param 	key
     * 			The key that is to be inserted.
     * @category HelperFunction
     */
    private Node insert(Node h, Key key) { 
    	
        if (h == null) 
        	return new Node(key, RED, 1);

        int cmp = key.compareTo(h.key);
        if (cmp < 0) 
        	h.left = insert(h.left,  key); 
        else if (cmp > 0) 
        	h.right = insert(h.right, key); 

        // Fix any right-leaning links
        if (isRed(h.right) && !isRed(h.left))      
        	h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) 
        	h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     
        	flipColors(h);
        
        h.size = size(h.left) + size(h.right) + 1;

        return h;
        
    }
    
    /**
     * Removes the specified key and its associated value from this tree.    
     *
     * @param  	key
     * 			The key to delete.
     * 			It is assumed that this key is actually in the tree.
     * @throws 	IllegalArgumentException
     * 			The given key is null.
     * @category General
     */
    public void delete(Key key) { 
        if (key == null) 
        	throw new IllegalArgumentException("The given key is null");
        if (!isRed(root.left) && !isRed(root.right)) // If both children of root are black, set root to red
            root.color = RED;
        root = delete(root, key);
        if (!isEmpty()) 
        	root.color = BLACK;
    }

    /**
     * Delete the given key from the subtree rooted at the given node.
     * 
     * @param 	h
     * 			The root of the subtree that contains the given key.
     * @param 	key
     * 			The key that is to be removed.
     * 			It is assumed that this key is actually in the subtree.
     * @return	The new root of the subtree after the given key is removed.
     * @category HelperFunction
     */
    private Node delete(Node h, Key key) { 
        
    	if (key.compareTo(h.key) < 0)  {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = delete(h.left, key);
        }
        else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.right = deleteMin(h.right);
            }
            else h.right = delete(h.right, key);
        }
    	
        return balance(h);
        
    }
    
    /**
     * Remove the smallest key from this tree.
     * 
     * @throws 	NoSuchElementException 
     * 			The tree is empty.
     * @category General
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Underflow");
        if (!isRed(root.left) && !isRed(root.right)) // If both children of root are black, set root to red
            root.color = RED;
        root = deleteMin(root);
        if (!isEmpty()) 
        	root.color = BLACK;
    }

    /**
     * Remove the smallest key from the subtree rooted at the given node.
     * 
     * @param 	h
     * 			The root of the subtree to remove the smallest key from.
     * @return	The new root of the given subtree, after removal of its
     * 			smallest key.
     * @category HelperFunction
     */
    private Node deleteMin(Node h) { 
        if (h.left == null)
            return null;
        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);
        h.left = deleteMin(h.left);
        return balance(h);
    }
    
    /**
     * Returns the smallest key in this tree.
     * 
     * @return 	The smallest key in the tree.
     * @throws 	NoSuchElementException
     * 			The tree is empty.
     * @category General
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("The tree is empty.");
        return min(root).key;
    } 

    /**
     * Returns the smallest key in the subtree rooted at the given node.
     * 
     * @return 	The smallest key in the subtree rooted at the given node.
     * @category HelperFunction
     */
    private Node min(Node x) { 
        if (x.left == null) return x; 
        else return min(x.left); 
    } 

   /**
     * Check whether the given node's link (to its parent) is red.
     *  This is a HelperFunction.
     * 
     * @param 	x
     * 			The node whose parent link's color is requested.
     * @return	True if and only if the given node's link to its parent is red.
     * 			| return (x == null ? false : x.color == RED)
     * @category HelperFunction
     */
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    /**
     * Rotate the given node to the right (so that its left-pointing red link ends up pointing to the right).
     * 	This is a HelperFunction.
     * 
     * @param 	h
     * 			A node pointing to a red-black tree that is to be rotated.
     * @pre		The given node's left link is red.
     * @return	A node that is the root of a red-black tree for the same set of keys as those of the
     * 			given node's, but whose right link is red.
     * @category HelperFunction
     */
    private Node rotateRight(Node h) {
        assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    /**
     * Rotate the given node to the left (so that its right-pointing red link ends up pointing to the left).
     * 	This is a HelperFunction.
     * 
     * @param 	h
     * 			A node pointing to a red-black tree that is to be rotated.
     * @pre		The given node's right link is red.
     * @return	A node that is the root of a red-black tree for the same set of keys as those of the
     * 			given node's, but whose left link is red.
     * @category HelperFunction
     */
    private Node rotateLeft(Node h) {
        assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    /**
     * Flip the colors of the links at the given node.
     *  This is a HelperFunction.
     * 
     * @param 	h
     * 			The node whose links need a color change.
     * @pre		The node's link to its parent must be the opposite of that of its children.
     * @category HelperFunction
     */
    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    /**
     * Assuming that h is red and both h.left and h.left.left are black,
     * 	make h.left or one of its children red.
     * 
     * @param 	h
     * 			The node to move to the left.
     * @category HelperFunction
     */
    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) { 
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    /**
     * Assuming that h is red and both h.right and h.right.left are black,
     * 	make h.right or one of its children red.
     * 
     * @param 	h
     * 			The node to move to the left.
     * @category HelperFunction
     */
    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) { 
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    /**
     * Restore the balance of the subtree rooted at the given node.
     * 
     * @param 	h
     * 			The root of the subtree to balance.
     * @return	The new root of the subtree, after it is balanced.
     * @category HelperFunction
     */
    private Node balance(Node h) {
        if (isRed(h.right))                      h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);
        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }
    
    /**
     * Insert the two given keys and, at the same time, return their neighbors in the tree.
     * 	The given keys should 
     * 
     * @param firstKey
     * @param secondKey
     * @return
     * @category Custom
     */
    public Key[] insertTwoGetNeighbors(Key firstKey, Key secondKey) {
    	
    	return null;
    	
    }
    
    /**
     * 
     * 
     * @param firstKey
     * @param secondKey
     * @return
     * @category Custom
     */
    public Key[] deleteTwoAndGetNeighbors(Key firstKey, Key secondKey) {
    	
    	return null;
    	
    }
    
    /**
     * 
     * 
     * @param firstKey
     * @param secondKey
     * @return
     * @category Custom
     */
    public Key[] swapAndGetNeighbors(Key firstKey, Key secondKey) {
    	
    	return null;
    	
    }

    /**
     * Helper class representing a node in a red-black tree.
     * 
     * @author Bruno Vandekerkhove
     * @version 1.0
     * @category HelperClass
     */
    private class Node {
    	
    	/**
    	 * Instantiate a new node with given key and color.
    	 * 
    	 * @param 	key
    	 * 			The key for the new tree node.
    	 * @param 	color
    	 * 			The color of the new tree node's parent link.
    	 * @param	size
    	 * 			The size of the new tree node's subtree.
    	 */
    	public Node(Key key, boolean color, int size) {
            this.key = key;
            this.color = color;
            this.size = size;
        }
    	
    	// Instance variables
        private Key key;			// Key
        private Node left, right;	// Links to left and right subtrees
        private boolean color;		// Color of parent link
        private int size;			// Size of the subtree of this node
        
    }
    
}