package jaims_development_studio.jaims.client.gui.customGUIComponents.messages;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;

/**
 * This class visualises a text message. This happens by adding the text to a
 * JTextPane and adding that to a custom painted JPanel. Furthermore custom
 * calculations are done in order to fit the message to the perfect size.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 * 
 * @see TextMessage#TextMessage(String, GUIMain, boolean)
 *
 */
public class TextMessage extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private GUIMain				guiMain;
	private JTextArea			jta;
	private boolean				own;
	private String				message;

	/**
	 * Constructor initialises the fields and calls {@link #initGUI()}.
	 * 
	 * @param message
	 *            the message to be displayed as a string
	 * @param guiMain
	 *            a reference to the guiMain class
	 * @param own
	 *            own message or contact's message
	 * 
	 * @see GUIMain
	 */
	public TextMessage(String message, GUIMain guiMain, boolean own) {

		this.guiMain = guiMain;
		this.own = own;
		this.message = message;
		initGUI();
		addResizeListeners();
	}

	/**
	 * Initialises the GUI by:
	 * <ul>
	 * <li>setting the panel's layout</li>
	 * <li>creating a new JTextArea and adding the message's text onto it</li>
	 * <li>setting the panel's fore- and background colour according to the boolean
	 * </li>
	 * </ul>
	 * 
	 * @see JTextArea
	 * @see BorderLayout
	 * @see EmptyBorder
	 */
	private void initGUI() {

		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(2, 8, 3, 8));

		jta = new JTextArea(message);
		jta.setMargin(new Insets(5, 8, 5, 5));
		jta.setAlignmentY(Component.CENTER_ALIGNMENT);
		jta.setMinimumSize(new Dimension(10, 40));
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		jta.setEditable(false);
		jta.setVisible(true);

		if (own) {
			jta.setFont(guiMain.getSettings().getOwnFont());
			jta.setForeground(new Color(guiMain.getSettings().getColorOwnMessageFont()));
			jta.setBackground(new Color(guiMain.getSettings().getColorOwnMessages()));
		} else {
			jta.setFont(guiMain.getSettings().getContactFont());
			jta.setForeground(new Color(guiMain.getSettings().getColorContactMessageFont()));
			jta.setBackground(new Color(guiMain.getSettings().getColorContactMessages()));
		}
		;
		add(jta);

	}

	/**
	 * Adds listeners to the panel and the frame respectively in order resize the
	 * message accordingly to the frame's size.
	 * 
	 * @see ComponentAdapter
	 * @see ComponentListener
	 */
	private void addResizeListeners() {

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {

				setMaximumSize();

			}
		});

		guiMain.getJaimsFrame().addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				setMaximumSize();

			}

		});
	}

	/**
	 * When called this method calculates the preferred size for the JTextArea and
	 * changes its and the panel's size accordingly.
	 */
	public void setMaximumSize() {

		String text = jta.getText();

		int preferredWidth = computeMaximumWidth();

		int textLength = jta.getFontMetrics(jta.getFont()).stringWidth(text);
		int textHeight = jta.getFontMetrics(jta.getFont()).getHeight();
		// True, if the message's string length is shorter than the program's calculated
		// preferred width
		if (textLength < preferredWidth) {
			if (textLength < jta.getMinimumSize().getWidth())
				textLength = (int) jta.getMinimumSize().getWidth();

			// Curiously need to set both the panel's and the JTextArea's sizes again in
			// order to properly resize both.
			jta.setMaximumSize(new Dimension(textLength + 5, textHeight));
			jta.setPreferredSize(jta.getMaximumSize());
			jta.setMinimumSize(jta.getMaximumSize());

			super.setMaximumSize(new Dimension((int) jta.getMaximumSize().getWidth() + 25,
					(int) jta.getMaximumSize().getHeight() + 15));
			super.setPreferredSize(super.getMaximumSize());
			super.setMinimumSize(super.getMaximumSize());

		} else if (textLength == preferredWidth) {
			jta.setMaximumSize(new Dimension(textLength + 5, textHeight));
			jta.setPreferredSize(jta.getMaximumSize());
			super.setMaximumSize(new Dimension((int) jta.getMaximumSize().getWidth() + 25,
					(int) jta.getMaximumSize().getHeight() + 15));
			super.setPreferredSize(super.getMaximumSize());
			super.setMinimumSize(super.getMaximumSize());
		} else {
			double height = ((double) textLength / ((double) preferredWidth - 20)) * (textHeight + 5) + 1;
			jta.setMaximumSize(new Dimension(preferredWidth, (int) height));
			jta.setPreferredSize(jta.getMaximumSize());
			super.setMaximumSize(new Dimension((int) jta.getMaximumSize().getWidth() + 25,
					(int) jta.getMaximumSize().getHeight() + 15));
			super.setPreferredSize(super.getMaximumSize());
			super.setMinimumSize(super.getMaximumSize());

		}

		jta.revalidate();
		jta.repaint();
		super.revalidate();
		super.repaint();

	}

	/**
	 * Computes the maximum width messages are allowed to take on.
	 * 
	 * @return the message's maximum width
	 */
	private int computeMaximumWidth() {

		double ratio = -0.000000000031829 * Math.pow(guiMain.getJaimsFrame().getWidth(), 3)
				+ 0.000000404015684 * Math.pow(guiMain.getJaimsFrame().getWidth(), 2)
				- 0.000992064197966 * guiMain.getJaimsFrame().getWidth() + 1.210427932031404;

		return (int) ((guiMain.getJaimsFrame().getWidth() - 260) * ratio);
	}

	/**
	 * Updates the message's visualisation by resetting its back-,foreground and
	 * font colour and again setting the message to its preferred size by calling
	 * {@link #setMaximumSize()}.
	 */
	public void updateMessage() {

		if (own) {
			jta.setFont(guiMain.getSettings().getOwnFont());
			jta.setForeground(new Color(guiMain.getSettings().getColorOwnMessageFont()));
			jta.setBackground(new Color(guiMain.getSettings().getColorOwnMessages()));
		} else {
			jta.setFont(guiMain.getSettings().getContactFont());
			jta.setForeground(new Color(guiMain.getSettings().getColorContactMessageFont()));
			jta.setBackground(new Color(guiMain.getSettings().getColorContactMessages()));
		}

		setMaximumSize();

		jta.revalidate();
		jta.repaint();
		repaint();
	}

	@Override
	public Dimension getMinimumSize() {

		return super.getMinimumSize();
	}

	@Override
	public Dimension getPreferredSize() {

		return super.getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {

		return super.getMaximumSize();
	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (own) {
			g2d.setColor(new Color(guiMain.getSettings().getColorOwnMessages()));
			g2d.fillRoundRect(0, 0, (int) getPreferredSize().getWidth() - 1, (int) getPreferredSize().getHeight() - 1,
					20, 20);
			g2d.setColor(new Color(guiMain.getSettings().getColorOwnMessageBorder()));
			g2d.setStroke(new BasicStroke(2.0F));
			g2d.drawRoundRect(0, 0, (int) super.getPreferredSize().getWidth() - 2,
					(int) super.getPreferredSize().getHeight() - 2, 20, 20);
			g2d.dispose();
		} else {
			g2d.setColor(new Color(guiMain.getSettings().getColorContactMessages()));
			g2d.fillRoundRect(0, 0, (int) getPreferredSize().getWidth() - 1, (int) getPreferredSize().getHeight() - 1,
					20, 20);
			g2d.setColor(new Color(guiMain.getSettings().getColorContactMessageBorder()));
			g2d.setStroke(new BasicStroke(2.0F));
			g2d.drawRoundRect(0, 0, (int) super.getPreferredSize().getWidth() - 2,
					(int) super.getPreferredSize().getHeight() - 2, 20, 20);
			g2d.dispose();
		}

		jta.repaint();

	}

}
