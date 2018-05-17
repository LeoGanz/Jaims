package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.Border;

/**
 * This class implements a rounded border by extending the <code>Border</code>
 * class and painting the border as a <code>RoundRectangle</code> with a given
 * width and height.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 * 
 * @see Border
 * @see RoundRectangle2D
 *
 */
public class RoundBorder implements Border {
	private int	width, height;
	Color		c;

	public RoundBorder(int width, int height, Color c) {

		this.width = width;
		this.height = height;
		this.c = c;
	}

	public int getWidth() {

		return width;
	}

	public int getHeight() {

		return height;
	}

	public Color getColor() {

		return c;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.BLACK);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(1.2F));
		g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, 20, 20));

		g2d.dispose();
	}

	@Override
	public Insets getBorderInsets(Component c) {

		return new Insets(5, 5, 5, 5);
	}

	@Override
	public boolean isBorderOpaque() {

		return false;
	}
}
