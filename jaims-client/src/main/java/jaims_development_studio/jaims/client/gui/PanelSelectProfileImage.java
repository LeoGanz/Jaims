package jaims_development_studio.jaims.client.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.swing.JPanel;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelSelectProfileImage extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private File				selectedImage;
	private Image				profilePicture;
	private Point				startDrag, endDrag;
	private Shape				shape				= null;
	private ClientMain			cm;

	public PanelSelectProfileImage(File selectedImage, ClientMain cm) {

		this.cm = cm;
		this.selectedImage = selectedImage;
		initGUI();

	}

	private void initGUI() {

		profilePicture = Toolkit.getDefaultToolkit().getImage(selectedImage.getAbsolutePath());
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				startDrag = new Point(e.getX(), e.getY());
				endDrag = startDrag;
				repaint();
			}

			public void mouseReleased(MouseEvent e) {

				if (endDrag != null && startDrag != null) {
					try {
						shape = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
						cm.repaintPanelSelectProfileImage();
						startDrag = null;
						endDrag = null;
						repaint();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {

				endDrag = new Point(e.getX(), e.getY());
				repaint();
			}
		});
	}

	private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {

		return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(profilePicture, 0, 0, null);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setStroke(new BasicStroke(2));
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

		if (shape != null) {
			g2.setPaint(Color.BLACK);
			g2.draw(shape);
			g2.setPaint(Color.YELLOW);
			g2.fill(shape);
		}

		if (startDrag != null && endDrag != null) {
			g2.setPaint(Color.LIGHT_GRAY);
			Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
			g2.draw(r);
		}

	}
}
