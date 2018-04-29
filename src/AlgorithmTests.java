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
		
		// testIntersections();

		Stopwatch stopwatch = new Stopwatch();
		testBruteForce();
		System.out.println(stopwatch.getElapsedTime() + " seconds");
		// plotBruteForce(true);
		
		// testSweepLineQuadratic();
		// plotSweepLineQuadratic(true);
		
		// testSweepLineLinearithmic();
		// plotSweepLineLinearithmic();
		
		// testRedBlackTree();
		
	}
	
	/**
	 * Test the intersection calculation code.
	 */
	public static void testIntersections() {
			
		Stack<Point> intersections = new Stack<Point>();
		
		Circle circle1 = new Circle(new Point(2,2), 2);
		Circle circle2 = new Circle(new Point(6,2), 2);
		circle1.addIntersections(circle2, intersections);
		
		printIntersections(intersections);
		
	}
	
	/**
	 * Test several cases with the brute force algorithm.
	 */
	public static void testBruteForce() {
		
		Algorithm algorithm = Algorithm.initializeAlgorithm(Algorithm.AlgorithmType.BRUTE_FORCE);
		Circle[] circles = Circle.randomIntegerCircles(4500, new Rectangle(0, 20000, 0, 20000), 400);
		System.out.println("Intersection # = " + algorithm.getIntersections(circles).size());

		// I used this as a playground for testing the brute force algorithm for all kinds of cases.
//		printIntersections(algorithm.getIntersections(circles));
//
//		int maxX = 600, maxY = 600;
//		int maxR = 40;
//		Rectangle bounds = new Rectangle(0,maxX,0,maxY);
//		Circle[] circles2 = Circle.randomIntegerCircles(100, bounds, maxR);
//		AlgorithmTests.visualizeIntersections(circles2, algorithm.getIntersections(circles2), maxX, maxY);
		
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
		
		int maxX = 600, maxY = 600;
		int maxR = 40;
		Rectangle bounds = new Rectangle(0,maxX,0,maxY);
		Circle[] circles = Circle.randomIntegerCircles(400, bounds, maxR);
		
		Stack<Point> intersections = algorithm.getIntersections(circles);
		AlgorithmTests.visualizeIntersections(circles, intersections, maxX, maxY);
		
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
			Stack<Point> intersections = algorithm.getIntersections(circles);
			speeds.push(new Point(intersections.size()/2500, stopwatch.getElapsedTime() * 7000));
		}
		
		plotAlgorithmSpeeds(speeds);
		
	}
	
	/**
	 * Test the Red Black tree implementation.
	 */
	public static void testRedBlackTree() {
		
		SweepLineStatus<String> tree = new SweepLineStatus<String>();
		
		for (int i=0; i<20; i++) {
            tree.insert(new Integer((int)(1000*Math.random())).toString());
        }
		
		System.out.println("N = " + tree.size() + "\n------");
        
        while (!tree.isEmpty()) {
        	System.out.println(tree.min());
        	tree.deleteMin();
        }
        
        tree.insert("Key");
        tree.insert("Another key");
        
        if (tree.contains("Key"))
        	System.out.println("Ok.");
        System.out.println(tree.min());
        
	}
	
	/**
	 * Visualize the given algorithm speeds in a scatter plot.
	 * 
	 * @param 	speeds
	 * 			A stack of points with input size as x coordinate and algorithm speed as y coordinate.
	 */
	public static void plotAlgorithmSpeeds(Stack<Point> speeds) {
				
		//	The plot size could be matched automatically to the point coordinates (N/speed values),
		//	but this is not exactly crucial for this assignment.
		ScatterPlot plot = new ScatterPlot("Snijdende Cirkels");
		plot.setSize(600, 600); // Set the frame's size (alternatively, use pack() function)
		plot.points = speeds;
		plot.setVisible(true);
		
		// Save plot as an image
		try {
			plot.saveTo("Plot.jpeg");
		}
		catch (Exception exception) {
			// Handle exceptions
		}
		
	}
	
	/**
	 * Visualize the given list of circles and stack of intersections.
	 *  The part of the coordinate system that is displayed lies between (0,0) and (maxX,maxY).
	 * 
	 * @param   circles
	 * 			The circles to draw in the frame. These will be colored in gray.
	 * @param 	intersections
	 * 			The intersections to display in the frame. These will be colored in red.
	 * @param	width
	 * 			The maximum x coordinate displayed in the frame.
	 * @param	height
	 * 			The maximum y coordinate displayed in the frame.
	 */
	public static void visualizeIntersections(Circle[] circles, Stack<Point> intersections, int maxX, int maxY) {
		
		// Visualize
		//	The plot size could be matched automatically to the point coordinates (N/speed values),
		//	but this is not exactly crucial for this assignment.
		IntersectionsFrame frame = new IntersectionsFrame("Snijdende Cirkels");
		frame.setSize(maxX, maxY); // Set the frame's size (alternatively, use pack() function)
		frame.circles = circles;
		frame.points = intersections;
		frame.setVisible(true);
		
		// Save frame as an image
		try {
			frame.saveTo("Plot.jpeg");
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
	public static void printIntersections(Stack<Point> intersections) {
		for (Point intersection : intersections) {
			System.out.println(intersection);
		}
	}

}