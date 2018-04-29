/**
 * An interval search tree implemented as a red-black search tree.
 * 
 * This implementation is not finished at all, there was too little time left.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class IntervalRBSearchTree<Value> {
	
	/**
	 * Nested class representing a node in an interval search tree, having an interval, a value, a left node, a right node,
	 * 	the maximum interval maximum in the subtree and an integer denoting the size of the node's subtree.
	 * 
	 * @author Bruno Vandekerkhove
	 * @version 1.0
	 */
	private class Node {
		
		/**
		 * Create a new node with given interval, associated value and subtree size.
		 * 
		 * @param 	interval
		 * 			The interval this node is associated with.
		 * @param 	value
		 * 			The value this node refers to.
		 * @param 	size
		 * 			The size of this node's subtree.
		 * @post	The new node's interval equals the given key.
		 * 			| new.interval = interval
		 * @post	The new node's value equals the given value.
		 * 			| new.value = value
		 * @post	The new node's subtree size equals the given size.
		 * 			| new.size = size
		 */
		public Node(Interval interval, Value value, int size, boolean color) {
            this.interval = interval;
            this.value = value;
            this.size = 1;
            this.maximum = interval.getMaximum();
            this.color = color;
        }
		
		/**
		 * Variable registering the interval of this node.
		 */
        private Interval interval;
        
        /**
         * Variable registering the value this node's key refers to.
         */
        private Value value;
        
        /**
         * Variable registering the left node of this node.
         */
        private Node left;
        
        /**
         * Variable registering the right node of this node.
         */
        private Node right;
        
        /**
         * Variable registering the maximum interval maximum in this node's subtree (includes this node).
         */
        private double maximum;
        
        /**
         * Variable registering the size of this node's subtree.
         */
        private int size;
        
        /**
         * The color for this node (true for red, false for black).
         */
        private boolean color;

    }
	
	/**
	 * Checks whether the given node is red (helper function).
	 * 
	 * @param 	node
	 * 			The node to check the color of.
	 * @return	True if and only if the given node is red.
	 * 			| result == (node == null ? false : node.color)
	 */
	private boolean isRed(Node node) {
		if (node == null)
			return false;
		return node.color;
	}
	
	/**
	 * Variable registering the root node of this true. This one equals null if the tree is empty.
	 */
	private Node root;
	
	/**
	 * Returns the size (number of nodes) of this tree.
	 */
	public int size() {
		return size(root);
	}
	
	/**
	 * Returns the size (number of nodes) of the given node's subtree (includes the given node).
	 * 
	 * @param 	node
	 * 			The node whose subtree's size is requested.
	 * @return 	The size of the subtree of the given node, including the given node.
	 * 			| result == (node == null ? 0 : node.size)
	 */
	private int size(Node node) {
		if (node == null)
			return 0;
		return node.size;
	}
	
	/**
	 * Checks whether this tree is empty.
	 * 
	 * @return	True if and only if there are no nodes in this tree.
	 * 			| result == (root == null)
	 */
	public boolean isEmpty() {
		return root == null;
	}
	
	/**
	 * Checks whether this interval search tree contains the given interval.
	 * 
	 * @param 	interval
	 * 			The interval to look out for.
	 * @return	True if and only if this tree contains the given interval.
	 * 			| result == get(interval) != null
	 */
	public boolean contains(Interval interval, Value value) {
        return get(interval) != null;
    }
	
	/**
	 * Inserts the given interval and associated value in this tree.
	 * 
	 * @param 	interval
	 * 			The interval to insert.
	 * @param 	value
	 * 			The value the given interval is associated with.
	 * @post	This search tree contains a node whose interval and value equals the
	 * 			given interval and value.
	 * 			| this.contains(interval, value)
	 */
	public void insert(Interval interval, Value value) {
		root = insert(root, interval, value);
		root.color = false;
	}
	
	/**
	 * Inserts the given node with associated interval and value into this tree.
	 * 
	 * @param 	node
	 * 			The node to insert.
	 * @param 	interval
	 * 			The interval to associate with the node.
	 * @param 	value
	 * 			The value to associate with the node.
	 * @return	The node that has been inserted, null if no insertion was completed.
	 * @post	This search tree contains a node whose interval and value equals the
	 * 			given interval and value.
	 * 			| this.contains(interval, value)
	 */
	private Node insert(Node node, Interval interval, Value value) {
		
		if (node == null)
			return new Node(interval, value, 1, true);
		
		int comparison = interval.compareTo(node.interval);
		if (comparison < 0)
			node.left = insert(node.left, interval, value);
		else if (comparison > 0)
			node.right = insert(node.right, interval, value);
		else
			node.value = value;
		
		if (isRed(node.right) && !isRed(node.left))
			node = rotateLeft(node);
		if (isRed(node.left) && isRed(node.left.left))
			node = rotateRight(node);
		if (isRed(node.left) && isRed(node.right))
			flipColors(node);
		
		node.size = size(node.left) + size(node.right) + 1;
		return node;
		
	}
	
	/**
	 * Remove the minimum from this search tree.
	 * 
	 * @post	This tree does not contain its former minimum.
	 * 			| !this.contains(old.min())
	 */
	public void remove() {
		if (!isRed(root.left) && !isRed(root.right))
			root.color = true;
		root = removeMin(root);
		if (root != null)
			root.color = false;
	}
	
	/**
	 * Remove the minimum from this search tree, searching in the given node's subtree.
	 * 
	 * @param 	node
	 * 			The node whose subtree that is to be searched through.
	 * @return	The removed node, if any.
	 */
	private Node removeMin(Node node) {
		if (node.left == null)
			return null;
		if (!isRed(node.left) && !isRed(node.left.left))
			node = moveRedLeft(node);
		node.left = removeMin(node.left);
		return balance(node);
	}
	
	/**
	 * Remove the given interval from the tree.
	 * 
	 * @param 	interval
	 * 			The interval to remove.
	 * @post	This tree does not contain the given interval.
	 * 			| !this.contains(interval)
	 */
	public void remove(Interval interval) {
		if (!isRed(root.left) && !isRed(root.right))
			root.color = true;
		root = remove(root, interval);
		if (root != null)
			root.color = false;
	}
	
	/**
	 * Remove the node associated with the given interval from this search tree, 
	 * 	searching in the given node's subtree.
	 * 
	 * @param 	node
	 * 			The node whose subtree that is to be searched through.
	 * @param	interval
	 * 			The interval to delete.
	 * @return	The deleted node, if any.
	 */
	private Node remove(Node node, Interval interval) {
		
		if (interval.compareTo(node.interval) < 0) {
			if (!isRed(node.left) && !isRed(node.left.left))
				node = moveRedLeft(node);
			node.left = remove(node.left, interval);
		}
		else {
			if (isRed(node.left))
				node = rotateRight(node);
			if (interval.compareTo(node.interval) == 0 && node.right == null)
				return null;
			if (!isRed(node.right) && !isRed(node.right.left))
				node = moveRedRight(node);
			if (interval.compareTo(node.interval) == 0) {
				node.value = get(node.right, min(node.right).interval);
				node.interval = min(node.right).interval;
				node.right = removeMin(node.right);
			}
			else
				node.right = remove(node.right, interval);
		}
		
		return balance(node);
		
	}
	
	/**
	 * Get the value associated with the given interval from this tree.
	 * 
	 * @param 	interval
	 * 			The interval whose value is requested.
	 * @return	The value associated with the given interval in this tree,
	 * 			if the tree contains the given interval.
	 */
	public Value get(Interval interval) {
        return get(root, interval);
    }

	/**
	 * Get the value associated with the given interval from this tree,
	 * 	by searching through the given node's subtree.
	 * 
	 * @param 	node
	 * 			The node whose subtree we're searching through.
	 * @param 	interval
	 * 			The interval whose value is requested.
	 * @return	The value associated with the given interval in this tree,
	 * 			if the tree contains the given interval.
	 */
    private Value get(Node node, Interval interval) {
        while (node != null) {
            int comparison = interval.compareTo(node.interval);
            if (comparison < 0) 
            	node = node.left;
            else if (comparison > 0) 
            	node = node.right;
            else 
            	return node.value;
        }
        return null;
    }
    
    /**
     * Get the minimum interval in this search tree (the interval with the smallest minimum).
     */
    public Interval min() {
        return min(root).interval;
    } 

    /**
     * Get the minimum interval in the subtree at the given node.
     * 
     * @param	node
     * 			The subtree to search through.
     * @return	The minimum interval in the subtree of the given node.
     * 			| result == (node.left == null ? node : min(node.left))
     */
    private Node min(Node node) { 
        if (node.left == null) 
        	return node; 
        else
        	return min(node.left); 
    } 
    
    /**
     * Get all values of intervals intersecting with the given interval.
     * 
     * @param 	interval
     * 			The interval to check intersections with.
     * @return	A stack of values containing all values of intervals in the search tree
     * 			intersecting with the given interval.
     */
    public Stack<Value> getValuesForIntervalsIntersectingWith(Interval interval) {
    	Stack<Value> values = new Stack<Value>();
    	addValuesForIntervalsIntersectingWith(root, interval, values);
    	return values;
    }
    
    /**
     * Add all values of intervals intersecting with the given interval to the given stack.
     * 
     * @param	node
     * 			The node at which to start searching.
     * @param 	interval
     * 			The interval to check intersections with.
     * @param	values
     * 			The stack to add values to.
     * @return	True if a value was found in the given node's subtree, false otherwise.
     */
    private boolean addValuesForIntervalsIntersectingWith(Node node, Interval interval, Stack<Value> values) {
    	
    	boolean found = false;
    	
    	if (node == null)
    		return false;
    	if (interval.intersects(node.interval)) {
    		values.push(node.value);
    		found = true;
    	}
    	
    	if (node.left != null && node.left.maximum >= interval.getMinimum())
    		found = addValuesForIntervalsIntersectingWith(node.left, interval, values);
    	if (found || node.left == null || node.left.maximum < interval.getMinimum())
    		found = addValuesForIntervalsIntersectingWith(node.right, interval, values);
    	
    	return found;
    	
    }
	
	/*
	 * 
	 * 	HELPER FUNCTIONS
	 * 
	 */
	
	/**
	 * Rotates the right-pointing red link of the given node to the left.
	 * 
	 * @param 	node
	 * 			The node whose red link is to be rotated.
	 * @return	The new parent node after rotation.
	 * 			| result == node.right
	 */
	private Node rotateLeft(Node node) {
		Node right = node.right;
		node.right = right.left;
		right.left = node;
		right.color = node.color;
		node.color = true;
		right.size = node.size;
		node.size = size(node.left) + size(node.right) + 1;
		return right;
	}
	
	/**
	 * Rotates the left-pointing red link of the given node to the right.
	 * 
	 * @param 	node
	 * 			The node whose red link is to be rotated.
	 * @return	The new parent node after rotation.
	 * 			| result == node.left
	 */
	private Node rotateRight(Node node) {
		Node left = node.left;
		node.left = left.right;
		left.right = node;
		left.color = node.color;
		node.color = true;
		left.size = node.size;
		node.size = size(node.left) + size(node.right) + 1;
		return left;
	}

	/**
	 * Flip the colors of the given node and its red children.
	 * 
	 * @param 	node
	 * 			The node whose color is to be flipped.
	 * @post	The given node's color is red.
	 * 			| isRed(node)
	 * @post	The given node's children are black.
	 * 			| !isRed(node.left) && !isRed(node.right)
	 */
	private void flipColors(Node node) {
		node.color = true;
		node.left.color = false;
		node.right.color = false;
	}
	
	/**
	 * Make the left child or one of the left child's children of the given node red,
	 * 	assuming that the given node is red and both its left node as well as the left node of
	 * 	its left node is black.
	 * 
	 * @param 	node
	 * 			The node to work with.
	 * @return	The new parent node.
	 */
	private Node moveRedLeft(Node node) {
		flipColors(node);
		if (isRed(node.right.left)) {
			node.right = rotateRight(node.right);
			node = rotateLeft(node);
		}
		return node;
	}
	
	/**
	 * Make the right child or one of the right child's children of the given node red,
	 * 	assuming that the given node is red and both its right node as well as the right node of
	 * 	its right node is black.
	 * 
	 * @param node
	 * @return
	 */
	private Node moveRedRight(Node node) {
		flipColors(node);
		if (!isRed(node.left.left))
			node = rotateRight(node);
		return node;
	}
	
	/**
	 * Restore the balance in the given node's subtree.
	 * 
	 * @param	node
	 * 			The node whose subtree is to be balanced.
	 * @post	The given node's subtree is properly balanced.
	 */
	private Node balance(Node node) {
        if (isRed(node.right))
        	node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left))
        	node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right))
        	flipColors(node);
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }
	
}