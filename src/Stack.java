import java.util.Iterator;

/**
 * A generic class representing an iterable stack of items.
 * 	The stack is implemented as a linked list.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class Stack<Item> implements Iterable<Item> {
	
	/**
	 * A nested class defining nodes in a linked list of items.
	 */
	public class Node {
		Item item;
		Node next;
	}
	
	/**
	 * A variable registering the node first added to this stack, if any.
	 */
	private Node first;
	
	/**
	 * A variable registering the number of nodes in this stack.
	 */
	private int size;
	
	/**
	 * Returns the number of items in this stack.
	 */
	public int size() {
		return this.size;
	}
	
	/**
	 * Checks whether this stack is empty or not.
	 */
	public boolean isEmpty() {
		return first == null;
	}
	
	/**
	 * Push the given item on the top of this stack.
	 * 
	 * @param 	item
	 * 			The item to append to this stack. Should not be null.
	 * @post	The size of the new stack increases by one.
	 * 			new.size() = this.size() + 1
	 */
	public void push(Item item) {
		if (item == null)
			return;
		Node oldFirst = first;
		first = new Node();
		first.item = item;
		first.next = oldFirst;
		size++;
	}
	
	/**
	 * Pop the item from the top of this stack.
	 * 
	 * @return	The item last added to this stack.
	 */
	public Item pop() {
		Item item = first.item;
		first = first.next;
		size--;
		return item;
	}
	
	/**
	 * Checks whether this stack of items contains the given item.
	 * 
	 * @param 	item
	 * 			The item to look for. Should not be null.
	 * @return	True if and only if this stack contains the given item.
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
	 * Returns a new iterator for iterating over the nodes in this stack.
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
		private Node current = first;
		
		/**
		 * Checks whether the iterator has more elements.
		 */
		public boolean hasNext() {
			return current != null;
		}
		
		/**
		 * Removes from the underlying collection the last element returned by this iterator.
		 */
		public void remove() {
			// Optional operation, not implemented.
		}
		
		/**
		 * Returns the next element in the iterator.
		 */
		public Item next() {
			Item item = current.item;
			current = current.next;
			return item;
		}
		
	}
	
}
