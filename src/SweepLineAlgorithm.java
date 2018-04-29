import java.util.Iterator;

/**
 * A sweep line algorithm for finding intersections in a collection of circles.
 * 
 * The default running time is quadratic in the worst case, and linearithmic in an average case.
 * 
 * The sweep line algorithm can be set to guarantee linearithmic time complexity, in which case the
 * 	status of the sweep line (active circles) is represented by a red-black binary search tree and custom
 * 	logic is applied.
 * The running time of the linearithmic algorithm is of the order O((N + S)*log_2(N)), S being the 
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
	public Stack<Point> getIntersections(Circle[] circles) {
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
	public Stack<Point> getIntersectionsQuadratic(Circle[] circles)  throws IllegalArgumentException{	
	
		Stack<Point> intersections = new Stack<Point>(); // The intersections (stacks are quick for additions and iteration)
		int N = circles.length; // The number of circles in the input array
		if (N < 2)
			return intersections; // No intersections possible
		
		// Throw an exception if there are too many circles in the input array. This could be solved by keeping activations
		//	and deactivations of circles in separate arrays and using one step of mergesort, though it seems like half of 
		//	the Integer class' max value is a wildly large number of circles.
		if (N > Integer.MAX_VALUE / 2)
			throw new IllegalArgumentException("Te veel cirkels in de invoer.");
				
		// Determine status transitions in the upcoming sweep and sort them by X coordinate,
		//	 prioritising activations (left extremes) of circles.
		Transition[] transitions = new Transition[2*N];
		for (int i=0 ; i<N ; i++) {
			transitions[2*i] = new Transition(circles[i], true); // Activation
			transitions[2*i+1] = new Transition(circles[i], false); // Deactivation
		}
		QuickSort.quicksort(transitions);
		
		// Sweep through plane, activating/deactivating circles, check for intersections at every deletion.
		// Checking at deletion means one can combine iteration through the list with deactivation.
		DoublyLinkedList<Circle> activeCircles = new DoublyLinkedList<Circle>();
		Iterator<Circle> iterator = null;
		Circle currentCircle = null, activeCircle = null;
		int i=0;
		do {
			
			if (transitions[i].activation)
				activeCircles.add(transitions[i].circle);
			else {
				currentCircle = transitions[i].circle;
				iterator = activeCircles.iterator();
				while(iterator.hasNext()) {
					activeCircle = iterator.next();
					if (activeCircle == currentCircle) iterator.remove(); // Don't check circle with itself
					else
						currentCircle.addIntersections(activeCircle, intersections);
				}
			}
			
		} while (++i < 2*N);
		
		return intersections;
		
	}
	
	/**
	 * Returns an iterable collection of the intersections between the circles in the given collection.
	 * 
	 * @param	circles
	 * 			A collection of circles to determine intersections of.
	 * @return	The intersections between the circles in the given collection.
	 * @note	This is a linearithmic sweep line algorithm, making use of a red-black balanced search tree
	 * 			a binary heap.
	 */
	public Stack<Point> getIntersectionsLinearithmic(Circle[] circles) {

		Stack<Point> intersections = new Stack<Point>(); // The intersections (stacks are quick for additions and iteration)
		int N = circles.length; // The number of circles in the input array
		if (N < 2)
			return intersections; // No intersections possible

		// Throw an exception if there are too many circles in the input array. This could be solved by keeping activations
		//	and deactivations of circles in separate arrays and using one step of mergesort, though it seems like half of 
		//	the Integer class' max value is still a wildly large number of circles.
		if (N > Integer.MAX_VALUE / 2)
			throw new IllegalArgumentException("Te veel cirkels in de invoer.");
				
		// Determine status transitions in the upcoming sweep and add them to a minimum priority queue.
		MinPQ<Transition> transitions = new MinPQ<Transition>(2*N);
		for (int i=0 ; i<N ; i++) {
			transitions.insert(new Transition(circles[i], true));
			transitions.insert(new Transition(circles[i], false));
		}
		
		// Sweep through the plane, updating the sweep status whenever appropriate.
		// TODO http://www.eecs.tufts.edu/~atong01/compgeo_final/
		/*
		SweepLineStatus<HalfCircle> status = new SweepLineStatus<HalfCircle>();
		IntervalSearchTree<Circle> searchTree = new IntervalSearchTree<Circle>();
		Circle currentCircle = null;
		Transition transition = null;
		Interval interval;
		int i=0;
		do {
			transition = transitions.delMin();
			currentCircle = transition.circle;
			interval = new Interval(currentCircle.getMinimumY(), currentCircle.getMaximumY());
			if (transitions[i].isActivation())
				searchTree.insert(interval, currentCircle);
			else {
				// How does one balance red-black trees if duplicates are allowed (no time left for me to
				//	ponder about this)
				searchTree.remove(interval, currentCircle);
				for (Circle activeCircle : searchTree.getValuesForIntervalsIntersectingWith(interval)) {
					currentCircle.addIntersections(activeCircle, intersections);
				}
			}
		} while (++i < N+N);
		*/
		
		return intersections;

	}
	
	/**
	 * An enumeration with types of transitions for the sweep line.
	 */
	public static enum TransitionType { // Needs to be static to be able to access it in other classes.
		
		ACTIVATION, // Activation of a (half-)circle
		DEACTIVATION, // Activation of a (half-)circle
		INTERSECTION; // Intersection of (half-)circles
		
		/**
		 * Returns the type of transition matching the given ordinal.
		 */
		public static TransitionType typeForOrdinal(int ordinal) {
			if (ordinal < 0 || ordinal > 2)
				return null;
			else
				return TransitionType.values()[ordinal];
		}
		
	}
	
	/**
	 * A transition represents a sweep line status transition, i.e. the activation or deactivation of a circle.
	 * 
	 * @author Bruno Vandekerkhove
	 * @version 1.0
	 */
	private class Transition implements Comparable<Transition> {
		
		/**
		 * Create a new transition with given circle and flag.
		 * 
		 * @param 	circle
		 * 			The circle this transition refers to.
		 * @param 	activation
		 * 			True if this transition signals the activation of its circle, false otherwise.
		 * @post	The new transition's circle equals the given one.
		 * 			| new.circle = circle
		 * @post	The new transition's circle activation flag is true if and only if the given flag is true.
		 * 			| new.activation = activation
		 */
		public Transition(Circle circle, boolean activation) {
			this.circle = circle;
			this.activation = activation;
		}
		
		/**
		 * Returns the X coordinate of this transition.
		 */
		public double getX() {
			if (this.activation)
				return this.circle.getMinimumX();
			return this.circle.getMaximumX();
		}
		
		/**
		 * A variable registering the circle this transition refers to.
		 */
		public final Circle circle;
		
		/**
		 * A variable registering whether this transition refers to the activation or deactivation of the
		 *  circle it refers to.
		 */
		public final boolean activation;
		
		/**
		 * Compare this transition to the given one.
		 * 
		 * @return 	A positive integer if the edge this transition refers to lies to the right of that of the given transition, 
		 * 			zero if the edges overlap and this transition is a deactivation, a negative integer otherwise.
		 */
		public int compareTo(Transition other) {
			int comparison = Double.compare(this.getX(), other.getX());
			if (comparison == 0) // Make sure any activations precede overlapping deactivations, to avoid missing out on intersections.
				comparison = (this.activation ? (other.activation ? 0 : -1) : (other.activation ? 1 : 0));
			return comparison;
		}
		
	}
	
	/**
	 * An intersection transition represents a sweep line status transition denoting an intersection point in particular.
	 * 
	 * @author Bruno Vandekerkhove
	 * @version 1.0
	 */
	private class TransitionIntersection extends Transition {
		
		/**
		 * Create a new intersection transition with given index and flag.
		 * 
		 * @param 	firstCircle
		 * 			The circle this transition refers to.
		 * @param 	upperArc
		 * 			True if the intersection is with the transition's circle upper arc.
		 * @param 	intersectedCircle
		 * 			The half-circle this transition's associated arc intersects with.
		 * @post	The new transition's circle equals the given one.
		 * 			| new.circle = circle
		 * @post	The new transition's circle activation flag is true if and only if the given flag is true.
		 * 			| new.isActivation() = activation
		 */
		public TransitionIntersection(Circle firstCircle, boolean upperArc, HalfCircle intersectedCircle) {
			super(firstCircle, upperArc); // Note that this transition's 'activation' flag has different meaning in this class.
			this.intersectedCircle = intersectedCircle;
		}
		
		/**
		 * Variable registering the circle that is intersected by this transition's associated arc.
		 */
		public final HalfCircle intersectedCircle;
		
		/**
		 * Compare this transition to the given one.
		 * 
		 * @return 	A positive integer if the edge this transition refers to lies to the right of that of the given transition, 
		 * 			zero if the edges overlap and this transition is a deactivation, a negative integer otherwise.
		 */
		public int compareTo(Transition other) {
			int comparison = Double.compare(this.getX(), other.getX());
			if (comparison == 0) // Make sure any activations precede overlapping deactivations, to avoid missing out on intersections.
				comparison = (this.activation ? (other.activation ? 0 : -1) : (other.activation ? 1 : 0));
			return comparison;
		}
		
	}
	
}