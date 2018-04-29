/**
 * A class representing a half-circle (with end-points having the same y-coordinate).
 * 	This is a wrapper class, referring to a circle and having a flag denoting whether or not 
 *  the half-circle is the upper or lower part of this circle.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class HalfCircle implements Comparable<HalfCircle> {

	/**
	 * Register a new half-circle as the given circle's top or bottom half.
	 * 
	 * @param 	circle
	 * 			The circle this half-circle is part of.
	 * @param 	isTop
	 * 			True if and only if the new half-circle is the top part of the given circle,
	 * 			false if it is the bottom half.
	 * @post	This half-circle's circle equals the given one.
	 * 			| this.circle == circle
	 * @post	This half-circle isTop flag equals the given value.
	 * 			| this.isTop = isTop
	 */
	public HalfCircle(Circle circle, boolean isTop) {
		if (circle == null) throw new IllegalArgumentException("Negative radius given");
		this.circle = circle;
		this.isTop = isTop;
	}
	
	/**
	 * Variable registering circle this half-circle is part of.
	 */
	public final Circle circle;
	
	/**
	 * Flag denoting whether or not this half-circle is the top of bottom half of its associated circle.
	 */
	public final boolean isTop;
	
	/**
	 * Get the y-coordinate of this half-circle corresponding to the given x-coordinate.
	 * 
	 * @param 	x
	 * 			The x coordinate whose corresponding y coordinate is desired.
	 * @return	The y-coordinate of the intersection of a vertical line in x with this
	 * 				half-circle.
	 * 			| return center.getY() + 
	 * 			| 	(this.isTop ? 1 : -1) * Math.pow(this.circle.getRadius(), 2) - Math.abs(this.circle.getCenter().getX() - x)
	 */
	public double getYForX(double x) {
		Point center = this.circle.getCenter();
		double dy = Math.pow(this.circle.getRadius(), 2) - Math.abs(center.getX() - x);
		return center.getY() + (isTop ? dy : -dy);
	}
	
	public Point getIntersection(Circle other) {
		return null;
	}
	
}
