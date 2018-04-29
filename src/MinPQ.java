import java.util.NoSuchElementException;

/**
 *  A class representing a minimum priority queue implemented as a binary heap.
 *   Inserting keys and deleting the minimum takes logarithmic time.
 *
 *	@author Bruno Vandekerkhove
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @version 1.0
 */
public class MinPQ<Key> {
	
    /**
     * Initializes an empty priority queue with the given initial capacity.
     *
     * @param  	initCapacity 
     * 			The initial capacity of this priority queue.
     * @throws	IllegalArgumentException
	 * 			The given radius is negative.
     */
    @SuppressWarnings("unchecked")
	public MinPQ(int capacity) {
    	if (capacity < 0) throw new IllegalArgumentException("Negative capacity given");
        pq = (Key[])new Object[capacity + 1];
        N = 0;
    }

    /**
     * Initializes an empty priority queue.
     */
    public MinPQ() {
        this(1);
    }
    
    /**
	 * The array of keys stored in this priority queue.
	 */
    private Key[] pq;
    
    /**
     * The number of items in the priority queue.
     */
    private int N;

    /**
     * Initializes a priority queue from the given array of keys.
     *
     * @param  	keys 
     * 			An array of keys to be put in the queue.
     */
    @SuppressWarnings("unchecked")
    public MinPQ(Key[] keys) {
        N = keys.length;
        pq = (Key[])new Object[keys.length + 1];
        for (int i=0 ; i < N; i++)
            pq[i+1] = keys[i];
        for (int k=N/2 ; k >= 1 ; k--)
            sink(k);
    }

    /**
     * Check whether or not this queue is empty.
     *
     * @return 	True if and only if this queue is empty.
     * 			| return (N == 0)
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Returns the number of keys in this priority queue.
     *
     * @return 	The number of keys in this priority queue.
     */
    public int size() {
        return N;
    }

    /**
     * Adds a new key to this priority queue.
     * 	This method does nothing if the queue is already filled.
     *
     * @param 	key 	
     * 			The key to add to this priority queue.
     */
    public void insert(Key x) {
        if (N == pq.length - 1)
        	return;
        pq[++N] = x;
        swim(N);
    }

    /**
     * Removes and returns a smallest key on this priority queue.
     *
     * @return 	A smallest key on this priority queue
     * @throws 	NoSuchElementException 
     * 			This priority queue is empty.
     * 			| this.isEmpty()
     */
    public Key delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        exch(1, N);
        Key min = pq[N--];
        sink(1);
        pq[N+1] = null; // Garbage collection
        return min;
    }


    /**
     * Perform a swim operation on the key at the given index.
     * 	This is a helper function.
     * 
     * @param 	k
     * 			The index of the key to apply a swim operation on.
     */
    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    /**
     * Perform a sink operation on the key at the given index.
     * 	This is a helper function.
     * 
     * @param 	k
     * 			The index of the key to apply a swim operation on.
     */
    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    /**
     * Check whether the key at the first index is greater than the key at the second index.
     * 	This is a helper function.
     * 
     * @param 	i
     * 			The index of the first key.
     * @param 	j
     * 			The index of the second key.
     * @return	True if and only if the key at the first index is greater than that at the second index.
     * 			| return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0
     */
    @SuppressWarnings("unchecked")
	private boolean greater(int i, int j) {
        return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
    }

    /**
     * Exchange the key at the first index with the key at the second index.
     * 
     * @param 	i
     * 			The index of the first key.
     * @param 	j
     * 			The index of the second key.
     */
    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

}