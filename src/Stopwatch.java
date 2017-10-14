/**
 * A class representing a stopwatch, to ease the measurement of elapsed time.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class Stopwatch {

	/**
	 * Initialize a stopwatch.
	 * 
	 * @post   	The start time of this stopwatch equals the number of milliseconds that elapsed since 1 January 1970 UTC.
	 *       	| new.start() == System.currentTimeMillis()
	 */
	public Stopwatch() {
		this.start = System.currentTimeMillis(); // Record start timestamp
	}
	
	/**
	 * Returns the starting time of this stopwatch, in milliseconds since 1 January 1970 UTC.
	 */
	public double getStartTime() {
		return start;
	}
	
	/**
	 * A variable registering the time the stopwatch started, in milliseconds since 1 January 1970 UTC.
	 */
	private final long start;
	
	/**
	 * Returns the time that elapsed since this stopwatch started, in seconds.
	 *
	 * @return  The amount of seconds that elapsed since this stopwatch started.
 	 *			| return ==  (System.currentTimeMillis() - this.getStartTime()) / 1000.0
	 */
	public double getElapsedTime() {
		long now = System.currentTimeMillis(); // Get the current timestamp
		return (now - start) / 1000.0; // Return the elapsed seconds since the start time
	}

}