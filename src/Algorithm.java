/**
 * An abstract class representing an algorithm for finding intersections in a collection of circles.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public abstract class Algorithm {
	
	/**
	 * A variable registering the intersections of the circles in the input of this algorithm.
	 */
	protected Stack<Point> intersections;
	
	/**
	 * An enumeration with types of algorithms implemented throughout.
	 */
	public static enum AlgorithmType { // Needs to be static to be able to access it in other classes.
		
		BRUTE_FORCE, // Brute force algorithm, O(N^2)
		SWEEP_LINE, // Sweep-line algorithm, worst case O(N^2)
		SWEEP_LINE_LINEARITHMIC; // Sweep-line algorithm with time complexity O((N + S)log_2(N))
		
		/**
		 * Returns the type of algorithm matching the given ordinal.
		 */
		public static AlgorithmType typeForOrdinal(int ordinal) {
			if (ordinal < 0 || ordinal > 2)
				return null;
			else
				return AlgorithmType.values()[ordinal];
		}
		
	}
	
	/**
	 * Returns an iterable collection of the intersections of the circles in the given array.
	 * 
	 * @param	circles
	 * 			An array of circles to determine intersections of.
	 * @return	The intersections of the circles in the given array.
	 */
	public abstract Stack<Point> getIntersections(Circle[] circles) ;
	
	/**
	 * Create and initialize a new algorithm of given type.
	 * 
	 * @param 	type
	 * 			The type of algorithm to set up.
	 * @return	An initialized algorithm of given type, or null if the given type is not valid.
	 */
	public static Algorithm initializeAlgorithm(AlgorithmType type) {

		if (type == null)
			return null; // null type given
		
		switch (type) {
			case BRUTE_FORCE:
				return new BruteForceAlgorithm();
			case SWEEP_LINE:
				return new SweepLineAlgorithm(false);
			case SWEEP_LINE_LINEARITHMIC:
				return new SweepLineAlgorithm(true);
			default:
				return null; // No valid type given
		}
		
	}
	
}