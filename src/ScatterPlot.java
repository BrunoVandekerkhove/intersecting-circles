import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A frame that can plot a list of points.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ScatterPlot extends JFrame {

	/**
	 * A public variable registering the color of this plot's points.
	 */
	public Color color = Color.blue;
	
	/**
	 * A public variable registering this plot's list of points.
	 */
	public Stack<Point> points;

	/**
	 * Initialize a new frame with given title.
	 * 
	 * @param 	title
	 * 			The title for this new frame.
	 * @post	The title for this new frame is equal to the given title.
	 * 			| new.getTitle() = title
	 */
	public ScatterPlot(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the app when the window is closed
	}
	
	/**
	 * Set the size of this plot.
	 * 
	 * @param	size
	 * 			The new size for this plot.
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
		
		g.setColor(this.color);
		for (Point point : points) {
			// System.out.println(point);
			int x = (int)point.getX(), y = this.getSize().height - (int)point.getY();
			g.drawRect(x, y, 1, 1);
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