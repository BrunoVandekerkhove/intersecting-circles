import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A class representing an application to detect intersections in collections of circles.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class IntersectingCircles {

	/**
	 * Launch the intersecting circles application with given parameters.
	 * 
	 * @param   args
	 * 			The input arguments (input file, output file).
	 */
	public static void main(String[] args) {
		
		// Comment out the line below if creating a sample input file is desired
		// createInputFile(1, Circle.randomIntegerCircles(1000, new Rectangle(0,1000,0,1000), 10), new File("input.txt"));
		
		if (args.length < 1)
			System.out.println("Ongeldige invoer. Raadpleeg de ReadMe voor verdere informatie.");
		else {
			
			String outputPath = "output.txt";
			if (args.length > 1 && args[1] != null)
				outputPath = args[1];
			
			/*
			boolean flag = false;
			if ((args.length > 1 && args[1].equalsIgnoreCase("--lines")) || (args.length > 2 && args[2].equalsIgnoreCase("--lines")))
				flag = true;
				*/
			
			// Run an algorithm matching the provided input file and write its output to the given output file.
			File input = new File(args[0]);
			File output = new File(outputPath);
			
			if (input.getName().equals(output.getName()))
				System.out.println("Ongeldige invoer. Raadpleeg de ReadMe voor verdere informatie.");
			else
				runAlgorithm(input, output);
			
		}				
		
	}
	
	/**
	 * Fetch the content of the given input file, use it to set up the appropriate algorithm,
	 * 	and look for intersecting circles.
	 *  
	 * @param	input
	 * 			The input file, having the algorithm's ordinal on the first line and the number of
	 * 				circles (= N) on the second line. 
	 *  		After the second line each of these circles' center point coordinates and radius should be listed 
	 *  			line by line as (x, y, r) where x is the x- and y the y-coordinate of the center point
	 *  			and r the radius of the respective circle.
	 * @param	output
	 * 			The text file to output circle intersections to. The last line will have the time it took
	 * 				to find those intersections, unless there was an error (in which case the whole output file 
	 * 				merely describes the error).	
	 */
	private static void runAlgorithm(File input, File output) {
		
		try {
			
			// Initialize file reading/writing
			Scanner scanner = new Scanner(input);
			FileWriter writer = new FileWriter(output);
		    PrintWriter printer = new PrintWriter(writer);
			
		    // Read input file
			int ordinal = scanner.nextInt(); // Fetch the algorithm ordinal
			int N = scanner.nextInt(); // Fetch the number of rectangles
			Algorithm algorithm = Algorithm.initializeAlgorithm(Algorithm.AlgorithmType.typeForOrdinal((ordinal-1) % 3));
			if (algorithm == null)
				printer.println("Dit algoritme is niet geïmplementeerd."); // Invalid ordinal
			else {
				
				// Read circle information
				Circle[] circles = new Circle[N]; // Setup rectangle collection (array)
				for (int i=0 ; i<N ; i++) { // Loop through rectangle coordinates
					double x = scanner.nextDouble(), y = scanner.nextDouble();
					double r = scanner.nextDouble();
					circles[i] = new Circle(new Point(x,y), r);
				}
				
				// Start a stopwatch and run the appropriate algorithm
				Stopwatch stopwatch = new Stopwatch();
				Stack<Point> intersections = algorithm.getIntersections(circles);
				double elapsedTime = stopwatch.getElapsedTime();
				for (Object intersection : intersections) {
					printer.println(intersection.toString());
				}
				printer.println(""); // Blanco line
				printer.print(elapsedTime);
				
			}
			
			scanner.close();
			printer.close();
			
		} catch (FileNotFoundException exception) {
			System.out.println("Het invoerbestand bestaat niet.");
		} catch (InputMismatchException exception) {
			System.out.println("Het invoerbestand is niet goed geformateerd. Raadpleeg de ReadMe voor verdere informatie.");
		} catch (NoSuchElementException exception) {
			System.out.println("Het invoerbestand bevat niet voldoende informatie.");
		} catch (IOException e) {
			System.out.println("Het uitvoerbestand kon niet opgemaakt worden.");
		}
		
	}	
	
	/**
	 * Fill the given text file with the given circles, formatted to match this assignment's input format.
	 * 	This function can be used for testing purposes.
	 * 
	 * @param	algorithmOrdinal
	 * 			The ordinal of the algorithm to be used when processing the input file.
	 * @param	circles
	 * 			An array of circles to fill the given file with.
	 * @param 	file
	 * 			The file to fill with circles. Its contents will be formatted appropriately.
	 */
	@SuppressWarnings("unused")
	private static void createInputFile(int algorithmOrdinal, Circle[] circles, File file) {
		
		try {
			
			// Initialize file reading/writing
			FileWriter writer = new FileWriter(file);
		    PrintWriter printer = new PrintWriter(writer);
		    printer.println(algorithmOrdinal);
		    printer.println(circles.length);
		    for (int i=0 ; i<circles.length ; i++) {
		    	System.out.println(circles[i].toString());
			    printer.println(circles[i].toString());
		    }
			printer.close();
			
		} catch (Exception exception) {
			System.out.println("Het invoerbestand kon niet opgesteld worden.");
		}
		
	}

}