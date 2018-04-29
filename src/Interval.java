/**
 * A class representing a floating-point interval, having a minimum and a maximum.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class Interval implements Comparable<Interval> {

	/**
	 * Create a new interval with given minimum and given maximum.
	 * 
	 * @param 	minimum
	 * 			The minimum value of this interval.
	 * @param 	maximum
	 * 			The maximum value of this interval.
	 * @post	The minimum value of this interval equals the given minimum.
	 * 			| new.getMinimum() = minimum
	 * @post	The maximum value of this interval equals the given maximum.
	 * 			| new.getMaximum() = maximum
	 */
	public Interval(double minimum, double maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}
	
	/**
	 * Returns the minimum value of this interval.
	 */
	public double getMinimum() {
		return this.minimum;
	}
	
	/**
	 * Variable registering the minimum value of this interval.
	 */
	private double minimum;
	
	/**
	 * Returns the maximum value of this interval.
	 */
	public double getMaximum() {
		return this.maximum;
	}
	
	/**
	 * Variable registering the maximum value of this interval.
	 */
	private double maximum;
	
	/**
	 * Checks whether this interval intersects the given interval.
	 * 
	 * @param 	other
	 * 			The interval to check for intersection with.
	 * @return	True if and only if this interval intersects the given one.
	 */
	public boolean intersects(Interval other) {
		return minimum <= other.getMaximum() && maximum >= other.getMinimum();
	}
	
	/**
	 * Compare this interval with the given one.
	 * 
	 * @return	True if and only if this interval's minimum value is smaller than that of the given interval.
	 * 			| result == Double.compare(this.getMinimum(), other.getMinimum())
	 */
	public int compareTo(Interval other) {
		return Double.compare(this.getMinimum(), other.getMinimum());
	}
	
}
