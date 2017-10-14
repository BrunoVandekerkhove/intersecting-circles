/**
 * A class to test algorithms with.
 * 	It can analyze algorithms by measuring their speed, their speed as the input size doubles, ...
 * 	and it can plot the results.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class AlgorithmTests {

	/**
	 * Unit tests.
	 * 
	 * @param 	args
	 * 			This parameter is currently not in use. It could be used to allow for an analysis from the command line.
	 */
	public static void main(String[] args) {
		
		// Algorithm algorithm = Algorithm.initializeAlgorithm(Algorithm.AlgorithmType.typeForOrdinal(1));
		// Rectangle[] rectangles = Rectangle.randomIntegerRectangles(1000000, new Rectangle(0, 10000, 0, 10000), 10);
		// Stopwatch stopwatch = new Stopwatch();
		// System.out.println(algorithm.getIntersections(rectangles).size() + " intersections");
		// System.out.println(stopwatch.getElapsedTime() + " seconds");
		// testSweepLineQuadratic();
		
		// testSweepLineLinearithmic();
		plotSweepLineLinearithmic();
		
	}
	
	/**
	 * Test several cases with the brute force algorithm.
	 */
	public static void testBruteForce() {
		
		Algorithm algorithm = Algorithm.initializeAlgorithm(Algorithm.AlgorithmType.BRUTE_FORCE);
		Circle[] circles = new Circle[2];
		
		// I used this as a playground for testing the brute force algorithm for all kinds of cases.
		
		circles[0] = new Circle(new Point(0,2), 2);
		circles[1] = new Circle(new Point(1,3), 3);
		printIntersections(algorithm.getIntersections(circles));
				
		circles[0] = new Circle(new Point(0,1), 1);
		circles[1] = new Circle(new Point(1,3), 3);
		System.out.println("Overlapping corners :");
		printIntersections(algorithm.getIntersections(circles));
		
	}
	
	/**
	 * Plot the speed of the brute force algorithm for increasing input size.
	 * 
	 * @param 	logarithmic
	 * 			True if a log-log plot is desired.
	 */
	public static void plotBruteForce(boolean logarithmic) {
		
		Algorithm algorithm = Algorithm.initializeAlgorithm(Algorithm.AlgorithmType.BRUTE_FORCE);
		
		// Determine the average speeds for increasing input sizes
		Stack<Point>speeds = new Stack<Point>();
		for (int N=0 ; N<12000 ; N+=20) { // Incrementing input size N
			for (int i=0 ; i<1 ; i++) {
				Circle[] circles = Circle.randomIntegerCircles(N, new Rectangle(0, 100, 0, 100), 10);
				Stopwatch stopwatch = new Stopwatch();
				algorithm.getIntersections(circles);
				if (logarithmic) speeds.push(new Point(Math.log(N/20) * 60, Math.log(stopwatch.getElapsedTime() * 1250)*60)); // For the log-log plot
				else speeds.push(new Point((N/20), stopwatch.getElapsedTime() * 1250)); // Average out
			}

		}
		
		testSweepLineLinearithmic();
		plotAlgorithmSpeeds(speeds);
		
	}
	
	/**
	 * Test several cases with the quadratic sweep line algorithm.
	 */
	public static void testSweepLineQuadratic() {
		
		Algorithm algorithm = Algorithm.initializeAlgorithm(Algorithm.AlgorithmType.SWEEP_LINE);		
		Circle[] circles = Circle.randomIntegerCircles(100, new Rectangle(0, 20, 0, 1000), 10);
		Stopwatch stopwatch = new Stopwatch();
		algorithm.getIntersections(circles);
		System.out.println(stopwatch.getElapsedTime() + " seconds");
		
	}
	
	/**
	 * Plot the speed of the quadratic sweep line algorithm for increasing input size.
	 * 
	 * @param 	logarithmic
	 * 			True if an accompanying log-log plot is desired.
	 */
	public static void plotSweepLineQuadratic(boolean logarithmic) {
		
		Algorithm algorithm = Algorithm.initializeAlgorithm(Algorithm.AlgorithmType.SWEEP_LINE);
		
		// Determine the average speeds for increasing input sizes
		Stack<Point>speeds = new Stack<Point>();
		for (int N=0 ; N<6000 ; N+=20) { // Incrementing input size N
			for (int i=0 ; i<1 ; i++) {
				Circle[] circles = Circle.randomIntegerCircles(N, new Rectangle(0, 1000, 0, 10), 10);
				Stopwatch stopwatch = new Stopwatch();
				algorithm.getIntersections(circles);
				if (logarithmic) speeds.push(new Point(Math.log(N/10) * 60, Math.log(stopwatch.getElapsedTime() * 1250)*60)); // For the log-log plot
				else speeds.push(new Point((N/10), stopwatch.getElapsedTime() * 1800)); // Average out
			}

		}
		
		plotAlgorithmSpeeds(speeds);
		
	}
	
	/**
	 * Test several cases with the linearithmic sweep line algorithm.
	 */
	public static void testSweepLineLinearithmic() {
		
		Algorithm algorithm = Algorithm.initializeAlgorithm(Algorithm.AlgorithmType.SWEEP_LINE_LINEARITHMIC);
		
		// Doubling time trial
		double lastSpeed = 1.0, speed;
		for (int N=20 ; N<9000000 ; N*=2) { // Incrementing input size N
			Circle[] circles = Circle.randomIntegerCircles(N, new Rectangle(0, 1000, 0, 1000), 10);
			Stopwatch stopwatch = new Stopwatch();
			algorithm.getIntersections(circles);
			speed = stopwatch.getElapsedTime();
			System.out.println("\\hline");
			System.out.println(N + " & " + speed + " & " + speed/lastSpeed + "\\\\");
			lastSpeed = speed;
		}
		
	}
	
	/**
	 * Plot the speed of the linearithmic sweep line algorithm for increasing intersections in input.
	 */
	public static void plotSweepLineLinearithmic() {
		
		Algorithm algorithm = Algorithm.initializeAlgorithm(Algorithm.AlgorithmType.SWEEP_LINE_LINEARITHMIC);
		
		// Determine the average speeds for increasing input sizes
		Stack<Point>speeds = new Stack<Point>();
		for (int E=1 ; E<=800 ; E++) {
			Circle[] circles = Circle.randomIntegerCircles(10000, new Rectangle(0, 1500, 0, 1500), E);
			Stopwatch stopwatch = new Stopwatch();
			Stack<Object> intersections = algorithm.getIntersections(circles);
			speeds.push(new Point(intersections.size()/2500, stopwatch.getElapsedTime() * 7000));
		}
		
		plotAlgorithmSpeeds(speeds);
		
	}
	
	/**
	 * Visualize the given algorithm speeds in a scatter plot.
	 * 
	 * @param 	speeds
	 * 			A stack of points with input size as x coordinate and algorithm speed as y coordinate.
	 */
	public static void plotAlgorithmSpeeds(Stack<Point> speeds) {
				
		// Visualize
		//	The plot size could be matched automatically to the point coordinates (N/speed values),
		//	but this is not exactly crucial for this assignment.
		ScatterPlot plot = new ScatterPlot("Snijdende Rechthoeken");
		plot.setSize(600, 600); // Set the frame's size (alternatively, use pack() function)
		plot.points = speeds;
		plot.setVisible(true);
		
		// Save plot as an image
		try {
			plot.saveTo("/Users/Bubble/Desktop/Plot.jpeg");
		}
		catch (Exception exception) {
			// Handle exceptions
		}
		
	}
	
	/**
	 * Print out the given intersections to standard output.
	 * 
	 * @param 	intersections
	 * 			The intersections to print out.
	 */
	public static void printIntersections(Stack<Object> intersections) {
		for (Object intersection : intersections) {
			System.out.println(intersection);
		}
	}

}