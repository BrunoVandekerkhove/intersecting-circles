import java.util.Iterator;

/**
 * A sweep line algorithm for finding intersections in a collection of circles.
 * 
 * The default running time is quadratic in the worst case, when all circles overlap horizontally. 
 * 	It is linear in the best case, if no circle overlaps horizontally with any other circle.
 * 
 * The sweep line algorithm can be set to guarantee linearithmic time complexity, in which case the
 * 	status of the sweep line (active circles) is represented by a red-black binary search tree.
 *  The running time of the linearithmic algorithm is of the order O((N + S)*log_2(N)), S being the 
 *  number of intersections.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class SweepLineAlgorithm extends Algorithm {

	/**
	 * Initializes a new sweep line algorithm to determine the intersections in a given array of circles.
	 * 
	 * @param 	linearithmic
	 * 			The type of this new algorithm. True if this algorithm is to use data structures
	 * 				as to guarantee linearithmic time complexity.
	 * @post	This new algorithm guarantees linearithmic time complexity if the given flag is true.
	 * 			| new.isLinearithmic() = linearithmic
	 */
	public SweepLineAlgorithm(boolean linearithmic) {
		this.linearithmic = linearithmic;
	}
	
	/**
	 * Returns whether this sweep line algorithm guarantees a linearithmic time complexity.
	 */
	public boolean isLinearithmic() {
		return this.linearithmic;
	}
	
	/**
	 * Variable registering whether this sweep line algorithm uses particular 
	 * 	data structures to guarantee linearithmic time complexity.
	 */
	private boolean linearithmic;
	
	/**
	 * Returns an iterable collection of the intersections between the circles in the given collection.
	 * 
	 * @param	circles
	 * 			A collection of circles to determine intersections of.
	 * @return	The intersections between the circles in the given collection.
	 */
	public Stack<Object> getIntersections(Circle[] circles) {
		if (this.isLinearithmic())
			return getIntersectionsLinearithmic(circles);
		else
			return getIntersectionsQuadratic(circles);
	}
	
	/**
	 * Returns an iterable collection of the intersections between the circles in the given collection.
	 * 
	 * @param	circles
	 * 			A collection of circles to determine intersections of.
	 * @return	The intersections between the circles in the given collection.
	 * @note	This algorithm finds intersections in O(N^2) in the worst case.
	 */
	public Stack<Object> getIntersectionsQuadratic(Circle[] circles)  throws IllegalArgumentException{	
	
		Stack<Object> intersections = new Stack<Object>(); // The intersections (stacks are quick for additions and iteration)
		int N = circles.length; // The number of rectangles in the input array
		if (N < 2)
			return intersections; // No intersections possible
		
		// Throw an exception if there are too many circles in the input array. This could be solved by keeping activations
		//	and deactivations of circles in separate arrays and using one step of mergesort, though it seems like half of 
		//	the Integer class' max value is still a wildly large number of circles.
		if (N > Integer.MAX_VALUE / 2)
			throw new IllegalArgumentException("Too many rectangles in the input array.");
				
		// Determine status transitions in the upcoming sweep and sort them by X coordinate.
		Transition[] transitions = new Transition[N+N];
		for (int i=0 ; i<N ; i++) {
			transitions[i+i] = new Transition(circles[i], true); // Activation
			transitions[i+i+1] = new Transition(circles[i], false); // Deactivation
		}
		QuickSort.quicksort(transitions);
		
		// Sweep through plane, activating/deactivating rectangles, check for intersections at every deletion.
		// Checking at deletion means one can mix iteration through the list with the deletion,
		//	not that that matters that much.
		DoublyLinkedList<Rectangle> activeRectangles = new DoublyLinkedList<Rectangle>();
		Iterator<Rectangle> iterator = null;
		Rectangle currentRectangle = null, activeRectangle;
		int i=0;
		do {
			if (transitions[i].isActivation())
				activeRectangles.add(transitions[i].getRectangle());
			else {
				currentRectangle = transitions[i].getRectangle();
				iterator = activeRectangles.iterator();
				while(iterator.hasNext()) {
					activeRectangle = iterator.next();
					if (activeRectangle == currentRectangle) iterator.remove(); // Don't check rectangle with itself
					else currentRectangle.addIntersections(activeRectangle, intersections);
				}
			}
		} while (++i < N+N);
		
		return intersections;
		
	}
	
	/**
	 * Returns an iterable collection of the intersections between the rectangles in the given collection.
	 * 
	 * @param	rectangles
	 * 			A collection of rectangles to determine intersections of.
	 * @return	The intersections between the rectangles in the given collection.
	 * @note	This is a linearithmic sweep line algorithm, making use of a red-black search tree.
	 */
	public Stack<Object> getIntersectionsLinearithmic(Circle[] circles) {
		
		Stack<Object> intersections = new Stack<Object>(); // The intersections (stacks are quick for additions and iteration)
		int N = rectangles.length; // The number of rectangles in the input array
		if (N < 2)
			return intersections; // No intersections possible
		
		// Throw an exception if there are too many rectangles in the input array. This could be solved by keeping activations
		//	and deactivations of rectangles in separate arrays and using one step of mergesort, though it seems like half of 
		//	the Integer class' max value is still a wildly large number of rectangles.
		if (N > Integer.MAX_VALUE / 2)
			throw new IllegalArgumentException("Too many rectangles in the input array.");
				
		// Determine status transitions in the upcoming sweep and sort them by X coordinate.
		Transition[] transitions = new Transition[N+N];
		for (int i=0 ; i<N ; i++) {
			transitions[i+i] = new Transition(circles[i], true); // Activation
			transitions[i+i+1] = new Transition(circles[i], false); // Deactivation
		}
		QuickSort.quicksort(transitions);
		
		// Sweep through plane, activating/deactivating rectangles, check for intersections at every deletion.
		IntervalSearchTree<Rectangle> searchTree = new IntervalSearchTree<Rectangle>();
		Rectangle currentRectangle = null;
		Interval interval;
		int i=0;
		do {
			currentRectangle = transitions[i].rectangle;
			interval = new Interval(currentRectangle.getMinimumY(), currentRectangle.getMaximumY());
			if (transitions[i].isActivation())
				searchTree.insert(interval, currentRectangle);
			else {
				// If one is to avoid overwriting duplicate intervals then the rectangle
				//	should be given at removal too, to check whether the interval being removed is the 
				//	interval associated with the rectangle being deactivated
				// But how does one balance red-black trees if duplicates are allowed (no time left for me to
				//	ponder about this)
				searchTree.remove(interval, currentRectangle); 
				for (Rectangle activeRectangle : searchTree.getValuesForIntervalsIntersectingWith(interval)) {
					currentRectangle.addIntersections(activeRectangle, intersections);
				}
			}
		} while (++i < N+N);
		
		return intersections;
		
	}
	
	/**
	 * A transition represents a sweep line status transition, i.e. the activation or deactivation of a circle.
	 * 
	 * @author Bruno Vandekerkhove
	 * @version 1.0
	 */
	private class Transition implements Comparable<Transition> {
		
		/**
		 * Create a new transition with given index and flag.
		 * 
		 * @param 	circle
		 * 			The circle this transition refers to.
		 * @param 	activation
		 * 			True if this transition signals the activation of its circle, false otherwise.
		 * @post	The new transition's circle equals the given one.
		 * 			| new.getCircle() = circle
		 * @post	The new transition's circle activation flag is true if and only if the given flag is true.
		 * 			| new.isActivation() = activation
		 */
		public Transition(Circle circle, boolean activation) {
			this.circle = circle;
			this.activation = activation;
		}
		
		/**
		 * Returns the circle this transition refers to.
		 */
		public Circle getCircle() {
			return this.circle;
		}
		
		/**
		 * Returns the X coordinate of this transition.
		 */
		public double getX() {
			if (this.isActivation())
				return this.getCircle().getMinimumX();
			return this.getCircle().getMaximumX();
		}
		
		/**
		 * A variable registering the circle this transition refers to.
		 */
		final Circle circle;
		
		/**
		 * Returns true if and only if this transition signals the activation of its circle.
		 */
		public boolean isActivation() {
			return this.activation;
		}
		
		/**
		 * A variable registering whether this transition refers to the activation or deactivation of the
		 *  circle it refers to.
		 */
		final boolean activation;
		
		/**
		 * Compare this transition to the given one.
		 * 
		 * @return 	A positive integer if the edge this transition refers to lies to the right of that of the given transition, 
		 * 			zero if the edges overlap and this transition is a deactivation, 
		 * 			a negative integer otherwise.
		 * 			| result = 
		 */
		public int compareTo(Transition other) {
			int comparison = Double.compare(this.getX(), other.getX());
			if (comparison == 0) // Make sure any activations precede overlapping deactivations, to avoid missing out on intersections.
				comparison = (this.isActivation() ? (other.isActivation() ? 0 : -1) : (other.isActivation() ? 1 : 0));
			return comparison;
		}
		
	}
	
}