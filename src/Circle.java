import java.lang.IllegalArgumentException; 

/**
 * A class representing a circle in 2-dimensional Euclidian space, 
 * 	having a center and a radius.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class Circle {

	/**
	 * Initializes a new circle with given center and radius.
	 * 
	 * @param 	center
	 *        	The center of the circle.
	 * @param 	radius
	 *        	The radius of the circle.
	 * @post   	The center of this circle equals the given one.
	 *       	| new.getCenter() == center
	 * @post   	The radius of this circle equals the given radius.
	 *       	| new.getRadius() == radius
	 * @throws	IllegalArgumentException
	 * 			The given radius is negative.
	 * @throws  IllegalArgumentException
	 * 			The x - and y coordinate of the given center point are at at less than radius
	 * 			 away from either Double.MAX_VALUE or -Double.MAX_VALUE.
	 * 			| Double.MAX_VALUE - Math.abs(center.getX()) < radius
	 * 			| || Double.MAX_VALUE - Math.abs(center.getY()) < radius
	 */
	public Circle(Point center, double radius) throws IllegalArgumentException {
		if (radius < 0
			|| Double.MAX_VALUE - Math.abs(center.getX()) < radius
			|| Double.MAX_VALUE - Math.abs(center.getY()) < radius)
			throw new IllegalArgumentException("Negative radius given");
		this.center = center;
		this.radius = radius;
	}

	/**
	 * Returns the center of this circle.
	 */
	public Point getCenter() {
		return this.center;
	}
	
	/**
	 * A variable registering the center point of this circle.
	 */
	private final Point center;
	
	/**
	 * Returns the radius of this rectangle.
	 */
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * A variable registering the radius of this circle.
	 */
	private final double radius;
	
	/**
	 * Returns the minimum x coordinate of this circle.
	 * 
	 * @return  The leftmost x coordinate of this circle.
	 * 			| this.getCenter().getX() - this.getRadius()
	 */
	public double getMinimumX() {
		return this.getCenter().getX() - this.getRadius();
	}
	
	/**
	 * Returns the maximum x coordinate of this circle.
	 * 
	 * @return  The rightmost x coordinate of this circle.
	 * 			| this.getCenter().getX() + this.getRadius()
	 */
	public double getMaximumX() {
		return this.getCenter().getX() + this.getRadius();
	}
	
	/**
	 * Checks whether the given circle and this circle overlap.
	 * 
	 * @param  	other
	 *         	The circle to check with.
	 * @return 	True if and only if the distance between this - and the other circle's center point is smaller than
	 * 			or equal to the sum of their radiuses.
	 *       	| result == (this.getCenter().distanceTo(other.getCenter()) <= (this.getRadius() + other.getRadius()))
	 * @note	This method does *not* check for intersections, as one circle inside an other
	 * 			 is not an intersection while it is an overlap.
	 */
	public boolean overlaps(Circle other) {
		return (this.getCenter().distanceTo(other.getCenter()) <= (this.getRadius() + other.getRadius()));
	}
	
	/**
	 * Checks whether the given circle intersects this circle.
	 * 
	 * @param  	other
	 *         	The circle to check with.
	 * @return 	True if and only if the distance between this - and the other circle's center point is smaller
	 * 			or equal to the sum - and greater or equal to the difference between their radiuses.
	 *       	| result == ((this.getCenter().distanceTo(other.getCenter())) <= (this.getRadius() + other.getRadius()))
	 *			|	&& ((this.getCenter().distanceTo(other.getCenter())) >= Math.abs(this.getRadius() - other.getRadius()))
	 */
	public boolean intersects(Circle other) {
		double distance = (this.getCenter().distanceTo(other.getCenter()));
		return (distance <= (this.getRadius() + other.getRadius()))
				&& (distance >= Math.abs(this.getRadius() - other.getRadius()));
	}
	
	/**
	 * Add the intersections of this circle with the given one to the given stack. 
	 * 
	 * @param 	other
	 * 			The circle intersecting this circle.
	 * @post	The given stack contains the intersections of the given circle with this circle.
	 */
	public void addIntersections(Circle other, Stack<Point> intersections) {
		
		Point c1 = this.getCenter(), c2 = other.getCenter(); // Center points
		double r1 = this.getRadius(), r2 = other.getRadius(); // Radii
		double distance = (c1.distanceTo(c2)); // Distance between center points
		
		if (distance <= (r1 + r2) && distance >= Math.abs(r1 - r2)) {
			
			if (distance == 0)
				return; // The circles are equal
			
			double d1 = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(distance, 2)) / (2 * distance);
			double z = Math.sqrt(Math.pow(r1, 2) - Math.pow(d1, 2));
			
			double xp = c1.getX() + d1 * (c2.getX() - c1.getX()) / distance;
			double yp = c1.getY() + d1 * (c2.getY() - c1.getY()) / distance;
				
			intersections.push(new Point(	xp + z * (c2.getY() - c1.getY()) / distance,
											yp - z * (c2.getX() - c1.getX()) / distance));
			
			if (distance != r1 + r2 && distance != Math.abs(r1 - r2)) // The circles intersect in only one point
				intersections.push(new Point(	xp - z * (c2.getY() - c1.getY()) / distance,
												yp + z * (c2.getX() - c1.getX()) / distance));
			
		}
		
	}
		
	/**
	 * Create an array of N circles whose center points have x coordinates, y coordinates and radiuses greater than
	 *  or equal to 0.0 and smaller than 1.0.
	 * 
	 * @param 	N
	 * 			The amount of circles to create.
	 * @return	An array of N circles with center point having x - and y coordinates, and radiuses greater than
	 * 				or equal to 0.0 and smaller than 1.0.
	 */
	public static Circle[] randomCircles(int N) {
		Circle[] circles = new Circle[N];
		for (int i=0 ; i<N ; i++) {
			double x = Math.random(), y = Math.random(), r = Math.random();
			circles[i] = new Circle(new Point(x,y), r);
		}
		return circles;
	}
	
	/**
	 * Create an array of N circles contained by the given rectangle and whose radiuses are smaller
	 *  than the given radius.
	 * 
	 * @param 	N
	 * 			The amount of circles to create.
	 * @param	bounds
	 * 			The rectangle in which all resulting circles should lie. Could be a window frame's coordinates.
	 * @param	maxRadius
	 * 			The maximum radius of the newly created circles.
	 * @return	An array of N circles contained by the given rectangle, with radiuses smaller than the given one.
	 * @throws	IllegalArgumentException
	 * 			The given radius is negative.
	 */
	public static Circle[] randomCircles(int N, Rectangle bounds, double maxRadius) {
		if (maxRadius < 0)
			throw new IllegalArgumentException("The given radius is negative.");
		Circle[] circles = new Circle[N];
		double x, y, r;
		double minX = bounds.getMinimumX(), maxX = bounds.getMaximumX(), minY = bounds.getMinimumY(), maxY = bounds.getMaximumY();
		double dx = maxX - minX, dy = maxY - minY;
		for (int i=0 ; i<N ; i++) {
			x = minX + dx * Math.random();
			y = minY + dy * Math.random(); 
			double[] radiuses = {Math.random() * maxRadius, y - minY, maxY - y, x - minX, maxX - x};
			r = Circle.minimum(radiuses);
			circles[i] = new Circle(new Point(x,y), r);
		}
		return circles;
	}
	
	/**
	 * Create an array of N circles contained by the given rectangle and whose radiuses are smaller
	 *  than the given radius. These coordinates and the radiuses are made sure to be integers.
	 * 
	 * @param 	N
	 * 			The amount of circles to create.
	 * @param	bounds
	 * 			The rectangle in which all resulting circles should lie. Could be a window frame's coordinates.
	 * @param	maxRadius
	 * 			The maximum radius of the newly created circles.
	 * @return	An array of N circles contained by the given rectangle, with radiuses smaller than the given one.
	 * @throws	IllegalArgumentException
	 * 			The given radius is negative.
	 */
	public static Circle[] randomIntegerCircles(int N, Rectangle bounds, double maxRadius) {
		if (maxRadius < 0)
			throw new IllegalArgumentException("The given radius is negative.");
		Circle[] circles = new Circle[N];
		double x, y, r;
		double minX = bounds.getMinimumX(), maxX = bounds.getMaximumX(), minY = bounds.getMinimumY(), maxY = bounds.getMaximumY();
		double dx = maxX - minX, dy = maxY - minY;
		for (int i=0 ; i<N ; i++) {
			x = (int)(minX + dx * Math.random());
			y = (int)(minY + dy * Math.random()); 
			double[] radiuses = {Math.random() * maxRadius, y - minY, maxY - y, x - minX, maxX - x};
			r = (int)Circle.minimum(radiuses);
			circles[i] = new Circle(new Point(x,y), r);
		}
		return circles;
	}
	
	/**
	 * Returns the smallest double in the given array of doubles.
	 * 	This is a helper method.
	 * 
	 * @param 	array
	 * 			An array of doubles.
	 * @return	The minimum value in the given array, or zero if the array is empty.
	 */
	private static double minimum(double[] array) {
		if (array.length == 0)
			return 0.0;
		double minimum = array[0];
		for (int i=1 ; i<array.length ; i++)
			minimum = Math.min(minimum, array[i]);
		return minimum;
	}
	
	/**
	 * Returns a string representation of this circle.
	 * 
	 * @return 	A string representing this circle.
	 * 			| result == this.getMinimumX() + " " + this.getMaximumX() + " " + this.getMinimumY() + " " + this.getMaximumY()
	 */
	public String toString() {
		return this.getCenter().getX() + " " + this.getCenter().getY() + " " + this.getRadius();
	}
	
}