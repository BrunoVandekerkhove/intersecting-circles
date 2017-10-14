import java.lang.IllegalArgumentException; 

/**
 * A class representing a rectangle in 2-dimensional Euclidian space, 
 * 	having a lower left - and an upper right vertex.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class Rectangle {

	/**
	 * Initializes a new rectangle with given lower left and upper right vertices.
	 * 
	 * @param 	minX
	 *        	The x coordinate of the rectangle's lower left vertex.
	 * @param 	maxX
	 *        	The x coordinate of the rectangle's upper right vertex.
	 * @param 	minY
	 *        	The y coordinate of the rectangle's lower left vertex.
	 * @param 	maxY
	 *        	The y coordinate of the rectangle's upper right vertex.
	 * @post   	The x coordinate of this rectangle's lower left vertex equals the given one.
	 *       	| new.getMinimumX() == minX
	 * @post   	The x coordinate of this rectangle's upper right vertex equals the given one.
	 *       	| new.getMaximumX() == maxX
	 * @post   	The y coordinate of this rectangle's lower left vertex equals the given one.
	 *       	| new.getMinimumY() == minY
	 * @post   	The y coordinate of this rectangle's upper right vertex equals the given one.
	 *       	| new.getMaximumY() == maxY
	 * @throws	IllegalArgumentException
	 * 			The given lower left vertex does not lie below and on the left of the given upper right vertex.
	 */
	public Rectangle(double minX, double maxX, double minY, double maxY) throws IllegalArgumentException {
		if (minX >= maxX || minY >= maxY)
			throw new IllegalArgumentException("Invalid rectangle coordinates : (" + minX + "," + maxX + ","+ minY + ","+ maxY + ")");
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	/**
	 * Returns the x coordinate of the lower left vertex of this rectangle.
	 */
	public double getMinimumX() {
		return this.minX;
	}
	
	/**
	 * A variable registering the x coordinate of the lower left vertex of this rectangle.
	 */
	private final double minX;
	
	/**
	 * Returns the x coordinate of the upper right vertex of this rectangle.
	 */
	public double getMaximumX() {
		return this.maxX;
	}
	
	/**
	 * A variable registering the x coordinate of the upper right vertex of this rectangle.
	 */
	private final double maxX;
	
	/**
	 * Returns the y coordinate of the lower left vertex of this rectangle.
	 */
	public double getMinimumY() {
		return this.minY;
	}
	
	/**
	 * A variable registering the y coordinate of the lower left vertex of this rectangle.
	 */
	private final double minY;
	
	/**
	 * Returns the y coordinate of the upper right vertex of this rectangle.
	 */
	public double getMaximumY() {
		return this.maxY;
	}
	
	/**
	 * A variable registering the y coordinate of the upper right vertex of this rectangle.
	 */
	private final double maxY;
	
	/**
	 * Checks whether the given rectangle overlaps with this rectangle.
	 * 
	 * @param  	other
	 *         	The rectangle to check with.
	 * @return 	True if and only if the given rectangle overlaps this rectangle.
	 *       	| result == (other.maxX >= this.minX
	 *			&& other.minX <= this.maxX
	 *			&& other.maxY >= this.minY
	 *			&& other.minY <= this.maxY)
	 * @note	This method does *not* check for intersection, as one rectangle inside an other
	 * 			 is not an intersection while it is an overlap.
	 */
	public boolean overlaps(Rectangle other) { // Check if any of the minima/maxima conflict, 4 comparisons.
		return (other.maxX >= this.minX
				&& other.minX <= this.maxX
				&& other.maxY >= this.minY
				&& other.minY <= this.maxY);
	}
	
	/**
	 * Returns a string representation of this rectangle.
	 * 
	 * @return 	A string representing this rectangle.
	 * 			| result == this.getMinimumX() + " " + this.getMaximumX() + " " + this.getMinimumY() + " " + this.getMaximumY()
	 */
	public String toString() {
		return this.getMinimumX() + " " + this.getMaximumX() + " " + this.getMinimumY() + " " + this.getMaximumY();
	}

}