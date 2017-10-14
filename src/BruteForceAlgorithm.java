/**
 * A brute force algorithm for finding intersections in a collection of circles.
 * 	The running time is quadratic as it compares each circle to every other circle,
 * 	for a total of N * (N-1) = O(N^2) compares.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class BruteForceAlgorithm extends Algorithm {
	
	/**
	 * Returns an iterable collection having the intersections of the circles in the given array.
	 * 
	 * @param	circles
	 * 			An array of circles to determine intersections of.
	 * @return	The intersections of the circles in the given array.
	 * @note	This algorithm finds intersections in O(N^2)
	 */
	public Stack<Object> getIntersections(Circle[] circles) {
				
		// Initialize intersections list (list of points and circles)
		intersections = new Stack<Object>(); // A static array of adequate length would likely reduce cost, but would be less neat
		
		// Loop through all circles, comparing them with every other circle
		Circle first, second;
		for (int i=0 ; i<circles.length ; i++) { // Outer loop, O(N)
			first = circles[i];
			for (int j=i+1 ; j<circles.length ; j++) { // Inner loop, O(N*(N-1)) = O(N^2)
				second = circles[j];
				first.addIntersections(second, intersections); // Method does nothing if there are no intersections
			}
		}
		
		// Return result
		return intersections;
		
	}	
	
}