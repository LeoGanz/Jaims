package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jaims_development_studio.jaims.client.gui.GUIMain;

/**
 * This class represents a JPanel which is added to the top of the frame's
 * content pane. It holds the minimise, maximise and close button.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 * 
 * @see #FrameTop(GUIMain)
 *
 */
public class FrameTop extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private boolean				maximized			= false, hoveringOnClose = false;
	private JPanel				pMinimize, pMaximize, pClose;
	private GUIMain				guiMain;
	private Point				lastPoint;

	/**
	 * Constructor of this class. Takes a reference to the <code>GUIMain</code>
	 * class and calls all methods needed to build the <code>JPanel</code> which
	 * later on will be added to the frame's content pane's top position.
	 * 
	 * @param guiMain reference to the GUIMain class
	 * 
	 * @see #initPanelMinimise()
	 * @see #pMinimiseAddListener()
	 * @see #initPanelMaximise()
	 * @see #pMaximizeAddListener()
	 * @see #initPanelClose()
	 * @see #pCloseAddListener()
	 * @see GUIMain
	 */
	public FrameTop(GUIMain guiMain) {

		this.guiMain = guiMain;

		initPanelMinimise();
		pMinimiseAddListener();
		initPanelMaximise();
		pMaximizeAddListener();
		initPanelClose();
		pCloseAddListener();
		addMovementListener();

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(Box.createRigidArea(new Dimension(0, 40)));
		add(Box.createHorizontalGlue());
		add(pMinimize);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(pMaximize);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(pClose);
		add(Box.createHorizontalGlue());

	}

	/**
	 * Creates the custom painted JPanel which shows a minimise button and
	 * minimises, as the name suggests, the frame when clicked. This method
	 * initialises the JPanel with all important attributes while the listener for
	 * mouse events is added in {@link #pMinimiseAddListener()}.
	 */
	private void initPanelMinimise() {

		pMinimize = new JPanel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2.setColor(Color.LIGHT_GRAY);
				g2.fillOval(0, 0, getWidth(), getHeight());
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(1.8F));
				g2.drawLine(8, 17, getWidth() - 9, 17);

				g2.dispose();
			}
		};
		pMinimize.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pMinimize.setMinimumSize(new Dimension(25, 25));
		pMinimize.setPreferredSize(pMinimize.getMinimumSize());
		pMinimize.setMaximumSize(pMinimize.getMinimumSize());

	}

	/**
	 * This method adds a mouse listener to the <code>JPanel</code> created in
	 * {@link #initPanelMinimise()} which fires events when the mouse is pressed,
	 * entered and exited. Upon entering the mouse, the <code>JPanel</code> will
	 * increase its size and redraw itself in order to show some animation. Upon
	 * exiting the mouse the <code>JPanel</code> will return to its original size.
	 * When clicking on the </code>JPanel</code> the <code>JFrame</code> will be
	 * minimised.
	 * 
	 * @see JPanel
	 * @see JFrame
	 * @see MouseAdapter
	 */
	private void pMinimiseAddListener() {

		pMinimize.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				guiMain.getJaimsFrame().setExtendedState(JFrame.ICONIFIED);
			}

			@Override
			public void mouseExited(MouseEvent e) {

				pMinimize.setMinimumSize(new Dimension(25, 25));
				pMinimize.setPreferredSize(pMinimize.getMinimumSize());
				pMinimize.setMaximumSize(pMinimize.getMinimumSize());
				pMinimize.revalidate();
				repaint();

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				pMinimize.setMinimumSize(new Dimension(30, 30));
				pMinimize.setPreferredSize(pMinimize.getMinimumSize());
				pMinimize.setMaximumSize(pMinimize.getMinimumSize());
				pMinimize.revalidate();
				repaint();
			}
		});
	}

	/**
	 * Creates the custom painted <code>JPanel</code> which shows the maximise
	 * button and will maximise the <code>JFrame</code> when clicked. This method
	 * only initialises the JPanel with its custom painting and all necessary
	 * attributes while the mouse listener is added in
	 * {@link #pMaximizeAddListener()}.
	 */
	private void initPanelMaximise() {

		pMaximize = new JPanel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				if (maximized == false) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2.setColor(Color.LIGHT_GRAY);
					g2.fillOval(0, 0, getWidth(), getHeight());
					g2.setColor(Color.BLACK);
					g2.drawRect(5, 6, getWidth() - 11, getHeight() - 12);

					g2.dispose();
				} else {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2.setColor(Color.LIGHT_GRAY);
					g2.fillOval(0, 0, getWidth(), getHeight());
					g2.setColor(Color.BLACK);
					g2.drawRect(5, 10, 9, 9);

					g2.dispose();
				}
			}
		};
		pMaximize.setMinimumSize(new Dimension(25, 25));
		pMaximize.setPreferredSize(pMaximize.getMinimumSize());
		pMaximize.setMaximumSize(pMaximize.getMinimumSize());
		pMaximize.setCursor(new Cursor(Cursor.HAND_CURSOR));

	}

	/**
	 * This method adds a mouse listener, more specifically a mouse adapter because
	 * it doesn't need every method offered by the mouse listener, to the
	 * <code>JPanel</code> created in {@link #initPanelMaximise()} which fires
	 * events upon pressing, entering or exiting the mouse. Upon entering the mouse,
	 * the <code>JPanel</code> will increase its size and redraw itself in order to
	 * show some animation. Upon exiting the mouse, the <code>JPanel</code> will
	 * return to its original size. When clicking on the <code>JPanel</code> the
	 * <code>JFrame</code> will me maximised.
	 * 
	 * @see JPanel
	 * @see JFrame
	 * @see MouseListener
	 * @see MouseAdapter
	 */
	private void pMaximizeAddListener() {

		pMaximize.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (maximized) {
					maximized = false;
					guiMain.getJaimsFrame().setExtendedState(JFrame.NORMAL);
				} else {
					maximized = true;
					guiMain.getJaimsFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
				repaint();

			}

			@Override
			public void mouseExited(MouseEvent e) {

				pMaximize.setMinimumSize(new Dimension(25, 25));
				pMaximize.setPreferredSize(pMaximize.getMinimumSize());
				pMaximize.setMaximumSize(pMaximize.getMinimumSize());
				pMaximize.revalidate();
				repaint();

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				pMaximize.setMinimumSize(new Dimension(30, 30));
				pMaximize.setPreferredSize(pMaximize.getMinimumSize());
				pMaximize.setMaximumSize(pMaximize.getMinimumSize());
				pMaximize.revalidate();
				repaint();

			}
		});
	}

	/**
	 * Creates the custom painted <code>JPanel</code> that shows the close button
	 * and closes the <code>JFrame</code> when clicked. This method only initialises
	 * the JPanel with the custom painting code and the necessary attributes while
	 * the mouse listener is added in {@link #pCloseAddListener()}.
	 */
	private void initPanelClose() {

		pClose = new JPanel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				if (hoveringOnClose == false) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2.setColor(Color.LIGHT_GRAY);
					g2.fillOval(0, 0, getWidth(), getHeight());
					g2.setColor(Color.BLACK);
					g2.drawLine(6, 5, getWidth() - 7, getHeight() - 6);
					g2.drawLine(6, getHeight() - 6, getWidth() - 7, 5);

					g2.dispose();
				} else {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2.setColor(new Color(128, 0, 0));
					g2.fillOval(0, 0, getWidth(), getHeight());
					g2.setColor(Color.WHITE);
					g2.setStroke(new BasicStroke(1.2F));
					g2.drawLine(6, 5, getWidth() - 7, getHeight() - 6);
					g2.drawLine(6, getHeight() - 6, getWidth() - 7, 5);

					g2.dispose();
				}
			}
		};
	}

	/**
	 * This method adds a mouse listener, more specifically a mouse adapter because
	 * it doesn't need every method offered by the mouse listener, to the
	 * <code>JPanel</code> created in {@link #initPanelClose()} which fires events
	 * upon pressing, entering or exiting the mouse. Upon entering the mouse, the
	 * <code>JPanel</code> will increase its size and redraw itself in order to show
	 * some animation. Upon exiting the mouse, the <code>JPanel</code> will return
	 * to its original size. When clicking on the <code>JPanel</code> the
	 * <code>JFrame</code> will me closed and the program stopped.
	 */
	private void pCloseAddListener() {

		pClose.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				System.exit(0);

			}

			@Override
			public void mouseExited(MouseEvent e) {

				hoveringOnClose = false;
				pClose.setMinimumSize(new Dimension(25, 25));
				pClose.setPreferredSize(pClose.getMinimumSize());
				pClose.setMaximumSize(pClose.getMinimumSize());
				pClose.revalidate();
				repaint();

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				hoveringOnClose = true;
				pClose.setMinimumSize(new Dimension(30, 30));
				pClose.setPreferredSize(pClose.getMinimumSize());
				pClose.setMaximumSize(pClose.getMinimumSize());
				pClose.revalidate();
				repaint();

			}
		});
		pClose.setMinimumSize(new Dimension(25, 25));
		pClose.setPreferredSize(pClose.getMinimumSize());
		pClose.setMaximumSize(pClose.getMinimumSize());
		pClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	private void addMovementListener() {

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				lastPoint = e.getLocationOnScreen();

			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {

				if (guiMain.getJaimsFrame().getExtendedState() == JFrame.MAXIMIZED_BOTH) {
					guiMain.getJaimsFrame().setExtendedState(JFrame.NORMAL);
					maximized = false;
					repaint();
				}

				int xDifference = (int) e.getLocationOnScreen().getX() - (int) lastPoint.getX();
				int yDifference = (int) (e.getLocationOnScreen().getY() - lastPoint.getY());

				guiMain.getJaimsFrame().setLocation((int) guiMain.getJaimsFrame().getLocation().getX() + xDifference,
						(int) guiMain.getJaimsFrame().getLocation().getY() + yDifference);

				lastPoint = e.getLocationOnScreen();

				if (e.getLocationOnScreen().getY() < 1) {
					guiMain.getJaimsFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
					maximized = true;
					repaint();
				}

			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.setColor(Color.LIGHT_GRAY);
		g2.setStroke(new BasicStroke(0.8F));
		g2.drawLine(45, getHeight() - 3, getWidth() - 45, getHeight() - 3);
	}

}
