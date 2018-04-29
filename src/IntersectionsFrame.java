import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A frame that can display circles and their intersections.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
@SuppressWarnings("serial")
public class IntersectionsFrame extends JFrame {

	/**
	 * A public variable registering the color of this frame's intersection points.
	 */
	public Color color = Color.blue;
	
	/**
	 * A public variable registering this frame's list of intersections.
	 */
	public Stack<Point> points;
	
	/**
	 * A public variable registering this frame's array of circles.
	 */
	public Circle[] circles;

	/**
	 * Initialize a new frame with given title.
	 * 
	 * @param 	title
	 * 			The title for this new frame.
	 * @post	The title for this new frame is equal to the given title.
	 * 			| new.getTitle() = title
	 */
	public IntersectionsFrame(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the app when the window is closed
	}
	
	/**
	 * Set the size of this frame.
	 * 
	 * @param	size
	 * 			The new size for this frame.
	 */
	public void setSize(int width, int height) {
		super.setSize(width, height);
		this.setLocationRelativeTo(null); // Center the frame
		this.setResizable(false);
		this.getContentPane().setBackground(Color.white);
	}

	/**
	 * Paints the container.
	 * 
	 * @param	g
	 * 			The specified graphics window.
	 */
	public void paint(Graphics g){

		super.paint(g);

		if (points == null)
			return;
				
		g.setColor(Color.BLACK); // Black circles
		
		for (Circle circle : circles) { // Draw circles
			// System.out.println(point);
			int x = (int)circle.getCenter().getX(), y = this.getSize().height - (int)circle.getCenter().getY();
			int r = (int)circle.getRadius();
			g.drawOval(x-r, y-r, 2*r, 2*r);
		}
		
		g.setColor(this.color); // Red points
		
		for (Point point : points) { // Draw intersections
			// System.out.println(point);
			int x = (int)point.getX(), y = this.getSize().height - (int)point.getY();
			g.drawRect(x-1, y-1, 3, 3);
		}
		
		g.dispose();

	}
	
	/**
	 * Save the contents of this plot to an image at the given path.
	 * 
	 * @param	path
	 * 			The path of the image to save this plot to.
	 * @throws	IOException
	 * 			An error occurred during writing.
	 */
	public void saveTo(String path) throws IOException {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        this.paint(graphics2D);
        ImageIO.write(image,"jpeg", new File(path));
	}

}