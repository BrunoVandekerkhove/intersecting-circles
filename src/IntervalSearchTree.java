/**
 * A class representing a binary search tree with intervals, to ease and speed up the finding of overlapping intervals.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class IntervalSearchTree<Value> {
    	
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
		public Node(Interval interval, Value value) {
            this.interval = interval;
            this.value = value;
            this.size = 1;
            this.maximum = interval.getMaximum();
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

    }
	
    /**
     * Variable registering the root of this search tree.
     */
    private Node root;
    
    /**
     * Returns whether or not this search tree is empty.
     * 
     * @return	True if this search tree is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of intervals in this search tree.
     * 
     * @return	The number of nodes in this search tree.
     */
    public int size() {
        return size(root);
    }

    /**
     * Returns the number of intervals in the subtree of the given node.
     * 
     * @param	node
     * 			The node whose subtree's size is requested.
     * @return	The size of the subtree of the given node, zero if the given node is null.
     * 			| result == (node == null ? 0 : node.size)
     */
    private int size(Node node) {
        if (node == null) 
        	return 0;
        else 
        	return node.size;
    }

    /**
     * Check whether this interval tree contains the given interval with associated value.
     * 
     * @param 	interval
     * 			The interval to search for.
     * @param	value
     * 			The value the interval has to be associated with.
     * @return	True if and only if this interval tree contains the given interval.
     * 			| result == get(interval) != null
     */
    public boolean contains(Interval interval, Value value) {
        return contains(root, interval, value);
    }

    /**
     * Check whether the given node's subtree contains the given interval with associated value.
     * 
     * @param	node
     * 			The node whose subtree to search in.
     * @param 	interval
     * 			The interval to search for.
     * @param	value
     * 			The value the interval has to be associated with.
     * @return	True if and only if this interval tree contains the given interval.
     * 			| result == get(interval) != null
     */
    public boolean contains(Node node, Interval interval, Value value) {
        if (node == null) 
        	return false; // Interval is not in tree
        int comparison = interval.compareTo(node.interval);
        if (comparison < 0) 
        	return contains(node.left, interval, value); // Search left
        else if (comparison > 0) 
        	return contains(node.right, interval, value); // Search right
        else
        	return true;
    }

    /**
     * Insert the given interval and associated value in this search tree.
     * 
     * @param 	interval
     * 			The interval to insert.
     * @param 	value
     * 			The value associated with the interval that is to be inserted.
     * @post	This search tree contains the given interval.
     * 			| this.contains(interval)
     */
    public void insert(Interval interval, Value value) {
    	root = insert(root, interval, value);
    }

    /**
     * Insert a node with given interval and value in the right spot in the given node's subtree.
     * 
     * @param 	node
     * 			The node in whose subtree a node is to be inserted.
     * @param 	interval
     * 			The interval the new node is to be associated with.
     * @param 	value
     * 			The value the new node is to be associated with.
     * @return	The newly inserted node, with correct maximum and subtree size.
     */
    private Node insert(Node node, Interval interval, Value value) {
    	if (node == null) 
    		return new Node(interval, value);
        int compare = interval.compareTo(node.interval);
        if (compare < 0)
        	node.left = insert(node.left,  interval, value);
        else if (compare > 0) 
        	node.right = insert(node.right, interval, value);
        node.size = 1 + size(node.left) + size(node.right);
        update(node);
        return node;
    }
    
    /**
     * Updates the maximum and the subtree size of the given node.
     * 
     * @param 	node
     * 			The node whose maximum and subtree size is to be updated.
     * @post	The given node has a correct maximum and a correct subtree size.
     * 			| node.maximum >= node.left.maximum
     * 			| && node.maximum >= node.right.maximum
     * 			| && node.size == node.left.size + node.right.size + 1
     */
    private void update(Node node) {
        if (node == null) 
        	return;
        node.size = size(node.left) + size(node.right) + 1;
        if (node.left != null && node.left.maximum > node.interval.getMaximum())
        	node.maximum = node.left.maximum;
        if (node.right != null && node.right.maximum > node.interval.getMaximum())
        	node.maximum = node.right.maximum;
    }
    
    /**
     * Remove the given interval with given value from the search tree.
     * 	The reason for including the value as a parameter is that intervals can overlap,
     * 	then the value is necessary to make a distinction.
     * 
     * @param 	interval
     * 			The interval to remove.
     * @param	value
     * 			The value of the interval to be removed.
     * @return	This search tree does not contain the given interval.
     * 			| !this.contains(interval)
     */
    public void remove(Interval interval, Value value) {
        root = remove(root, interval, value);
    }

    /**
     * Remove the given interval with given value or, if the given node does not match the interval/value pair,
     * 	return the subtree that might contain the pair.
     * 
     * @param 	node
     * 			The node whose subtree could contain the given interval with given value.
     * @param 	interval
     * 			The interval to remove.
     * @param 	value
     * 			The value of the interval to remove.
     * @return	A subnode of the given node whose subtree might contain the given interval/value pair.
     */
    private Node remove(Node node, Interval interval, Value value) {
        if (node == null) 
        	return null;
        int comparison = interval.compareTo(node.interval);
        if (comparison < 0)
        	node.left = remove(node.left, interval, value);
        else if (comparison > 0) 
        	node.right = remove(node.right, interval, value);
        else
        	node = joinLeftRight(node.left, node.right);
        update(node);
        return node;
    }
    
    /**
     * Join the given children of a node to make a subtree.
     * 
     * @param 	first
     * 			The first child to form a subtree with.
     * @param 	second
     * 			The second child to form a subtree with.
     * @return	A node, the parent of a subtree containing the two
     * 			given nodes and their children.
     */
    private Node joinLeftRight(Node first, Node second) { 
        if (first == null) 
        	return second; // No first node, second node is parent
        if (second == null) 
        	return first; // No second node, first node is parent
        if (Math.random() * (size(first) + size(second)) < size(first))  {
        	first.right = joinLeftRight(first.right, second);
            update(first);
            return first;
        }
        else {
        	second.left = joinLeftRight(first, second.left);
            update(second);
            return second;
        }
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
    	
    	if (node == null)
    		return false;
    	
    	boolean found = false, foundleft = false, foundright = false;

    	if (interval.intersects(node.interval)) {
    		values.push(node.value);
    		found = true;
    	}
    	
    	if (node.left != null && node.left.maximum >= interval.getMinimum())
    		foundleft = addValuesForIntervalsIntersectingWith(node.left, interval, values); // Look in left subtree
    	if (foundleft || node.left == null || node.left.maximum < interval.getMinimum()) // If not found in left subtree, ignore right subtree
    		foundright = addValuesForIntervalsIntersectingWith(node.right, interval, values); // Look in right subtree
    	
    	return found || foundleft || foundright;
    	
    }
    
}