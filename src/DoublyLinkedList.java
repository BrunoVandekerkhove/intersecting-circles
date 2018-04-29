import java.util.Iterator;

/**
 * A generic class representing a doubly linked list of items.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class DoublyLinkedList<Item> implements Iterable<Item> {

	/**
	 * A nested class defining nodes in a doubly linked list of items.
	 */
	public class Node {
		Item item;
		Node previous;
		Node next;
	}
	
	/**
	 * Create a new doubly linked list.
	 */
	public DoublyLinkedList() {
        this.pre  = new Node();
        this.post = new Node();
        pre.next = post;
        post.previous = pre;
    }
	
	/**
	 * Returns the size of this doubly linked list.
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * A variable registering the size of this doubly linked list.
	 */
	private int size;
	
	/**
	 * A variable registering a node at the very start of this doubly linked list.
	 */
    private Node pre;
    
    /**
     * A variable registering a node at the very end of this doubly linked list.
     */
    private Node post;

    /**
     * Add the given item to this list.
     * 
     * @param 	item
     * 			The item to add to this list.
     * @post	This doubly linked list contains the given item.
     * 			| new.contains(item)
     */
    public void add(Item item) {
        Node last = post.previous;
        Node node = new Node();
        node.item = item;
        node.next = post;
        node.previous = last;
        post.previous = node;
        last.next = node;
        size++;
    }
    
    /**
	 * Checks whether this doubly linked list contains the given item.
	 * 
	 * @param 	item
	 * 			The item to look for. Should not be null.
	 * @return	True if and only if this doubly linked list contains the given item.
	 */
	public boolean contains(Item item) {
		if (item == null)
			return false;
		for (Item other : this)
			if (other.equals(item))
				return true;
		return false;
	}
	
	/**
	 * Returns a new iterator for iterating over the items in this doubly linked list.
	 */
	public Iterator<Item> iterator() {
		return new ListIterator();
	}
	
	/**
	 * A class implementing the iterator interface.
	 */
	private class ListIterator implements Iterator<Item> {
		
		/**
		 * Variable registering the current element in the iterator.
		 */
		private Node current = pre.next;
		
		/**
		 * Variable registering the last accessed node in this iterator.
		 */
		private Node lastAccessed  = null;
		
		/**
		 * Variable registering the index of the current node.
		 */
		private int index = 0;
		
		/**
		 * Checks whether the iterator has more elements.
		 */
		public boolean hasNext() {
			return index < size;
		}
		
		/**
		 * Returns the next element in the iterator, or null if there is none.
		 */
		public Item next() {
			if (!hasNext())
				return null;
            lastAccessed = current;
            Item item = current.item;
            current = current.next; 
            index++;
            return item;
		}

        /**
         * Remove the element that was last accessed by next() from the underlying list.
         */
        public void remove() { 
            if (lastAccessed == null) throw new IllegalStateException();
            Node x = lastAccessed.previous;
            Node y = lastAccessed.next;
            x.next = y;
            y.previous = x;
            size--;
            if (current == lastAccessed) current = y;
            else index--;
            lastAccessed = null;
        }
		
	}
	
}