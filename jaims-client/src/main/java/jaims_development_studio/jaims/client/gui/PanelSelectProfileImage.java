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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.swing.JPanel;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelSelectProfileImage extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private File				selectedImage;
	private Image				profilePicture, scaledPicture;
	private Point				startDrag, endDrag;
	private Shape				shape				= null;
	private ClientMain			cm;
	private int					startX, startY;
	private boolean				controlPressed;

	public PanelSelectProfileImage(File selectedImage, ClientMain cm) {

		this.cm = cm;
		this.selectedImage = selectedImage;
		initGUI();

	}

	private void initGUI() {

		profilePicture = Toolkit.getDefaultToolkit().getImage(selectedImage.getAbsolutePath());
		scaledPicture = profilePicture;
		setFocusable(true);
		requestFocus();
		ImageObserver io = this;
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				startDrag = new Point(e.getX(), e.getY());
				endDrag = startDrag;
				startX = e.getX();
				startY = e.getY();
				repaint();
			}

			public void mouseReleased(MouseEvent e) {

				if (endDrag != null && startDrag != null) {
					try {
						shape = makeRectangle(startDrag.x, startDrag.y, (int) endDrag.getX(), (int) endDrag.getY());
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

				int width, height;
				if (e.getX() > startX)
					width = e.getX() - startX;
				else
					width = startX - e.getX();
				System.out.println(width);

				if (e.getY() > startY)
					height = e.getY() - startY;
				else
					height = startY - e.getY();

				if (width > height)
					endDrag = new Point(startX + width, startY + width);
				else if (height > width)
					endDrag = new Point(startX + height, startY + height);
				else
					endDrag = new Point(startX + width, startY + height);
				repaint();
			}
		});
		addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {

				if (controlPressed) {
					if (e.getWheelRotation() <= 0) {

						if (scaledPicture.getWidth(io) <= profilePicture.getWidth(io) * 2) {
							scaledPicture = scaleMaintainRatio(e.getWheelRotation() - 5, true);

						}
						repaint();

					} else {
						if (scaledPicture.getWidth(io) >= profilePicture.getWidth(io) / 10) {
							scaledPicture = scaleMaintainRatio(e.getWheelRotation() + 5, false);

						}
						repaint();
					}
				}

			}
		});
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_CONTROL)
					controlPressed = false;

			}

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_CONTROL)
					controlPressed = true;

				if (e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
					if (controlPressed) {
						if (scaledPicture.getWidth(io) >= profilePicture.getWidth(io) / 10) {
							scaledPicture = scaleMaintainRatio(4, true);
							repaint();
						}
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_ADD) {
					if (controlPressed) {
						if (scaledPicture.getWidth(io) <= profilePicture.getWidth(io) * 2) {
							scaledPicture = scaleMaintainRatio(-4, false);
							repaint();
						}
					}
				}

			}
		});
	}

	private Image scaleMaintainRatio(int wheelRotationNumber, boolean scaleUp) {

		int width = profilePicture.getWidth(this);
		int height = profilePicture.getHeight(this);

		if (width > height) {
			double ratio = (double) width / height;
			if (scaleUp)
				return profilePicture.getScaledInstance(
						scaledPicture.getWidth(this) - (int) (16 * ratio * wheelRotationNumber),
						scaledPicture.getHeight(this) - 16 * wheelRotationNumber, Image.SCALE_SMOOTH);
			else
				return profilePicture.getScaledInstance(
						scaledPicture.getWidth(this) - (int) (16 * ratio * wheelRotationNumber),
						scaledPicture.getHeight(this) - 16 * wheelRotationNumber, Image.SCALE_FAST);
		} else {
			double ratio = height / width;

			if (scaleUp)
				return profilePicture.getScaledInstance(
						scaledPicture.getWidth(this) - (int) (16 * ratio * wheelRotationNumber),
						scaledPicture.getHeight(this) - 16 * wheelRotationNumber, Image.SCALE_SMOOTH);
			else
				return profilePicture.getScaledInstance(
						scaledPicture.getWidth(this) - (int) (16 * ratio * wheelRotationNumber),
						scaledPicture.getHeight(this) - 16 * wheelRotationNumber, Image.SCALE_FAST);
		}

	}

	private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {

		return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setClip(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		g2.drawImage(scaledPicture, getWidth() / 2 - scaledPicture.getWidth(this) / 2,
				getHeight() / 2 - scaledPicture.getHeight(this) / 2, null);
		g2.setStroke(new BasicStroke(2));
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));

		if (shape != null) {
			g2.setPaint(Color.BLACK);
			g2.draw(shape);
			g2.setPaint(new Color(88, 166, 255));
			g2.fill(shape);
		}

		if (startDrag != null && endDrag != null) {
			g2.setPaint(Color.LIGHT_GRAY);
			Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
			g2.draw(r);
		}

	}
}
