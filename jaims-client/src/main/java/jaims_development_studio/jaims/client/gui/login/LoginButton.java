package jaims_development_studio.jaims.client.gui.login;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import jaims_development_studio.jaims.client.gui.GUIMain;

/**
 * This class represents a <code>JButton</code> which is used as the login
 * button on the startup frame. On entering the mouse to the button it increases
 * its size in order to show some animation. Furthermore when there is no
 * connection to the server the button gets deactivated and activated again when
 * the connections is built.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class LoginButton extends JButton {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private GUIMain				guiMain;
	private String				text;
	private JPanel				parentPanel;

	public LoginButton(GUIMain guiMain, String text, JPanel parentPanel) {

		this.guiMain = guiMain;
		this.text = text;
		this.parentPanel = parentPanel;
		initGUI();
	}

	/**
	 * This method initialises the button with its attributes and adds a mouse
	 * listener which increases and decreases the buttons size respectively on
	 * entering and exiting the mouse.
	 * 
	 * @see MouseListener
	 */
	private void initGUI() {

		setPreferredSize(new Dimension(125, 36));
		setMaximumSize(getPreferredSize());
		setOpaque(true);
		setBorderPainted(false);
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		if (guiMain.isServerConnected() == false) {
			setEnabled(false);
			setToolTipText("Could not connect to the server!");
		}
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				if (isEnabled()) {
					setPreferredSize(new Dimension(185, 50));
					setMaximumSize(getPreferredSize());
					repaint();
					parentPanel.revalidate();
					parentPanel.repaint();
					guiMain.getJaimsFrame().getContentPane().repaint();
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {

				if (isEnabled()) {
					setPreferredSize(new Dimension(180, 45));
					setMaximumSize(getPreferredSize());
					repaint();
					parentPanel.revalidate();
					parentPanel.repaint();
					guiMain.getJaimsFrame().getContentPane().repaint();
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {

				setPreferredSize(new Dimension(155, 36));
				setMaximumSize(getPreferredSize());
				repaint();
				parentPanel.revalidate();
				parentPanel.repaint();
				guiMain.getJaimsFrame().getContentPane().repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {

				if (isEnabled()) {
					setPreferredSize(new Dimension(185, 50));
					setMaximumSize(getPreferredSize());
					repaint();
					parentPanel.revalidate();
					parentPanel.repaint();
					guiMain.getJaimsFrame().getContentPane().repaint();
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
		g2.setFont(new Font("Calibri", Font.BOLD, 16));
		g2.setColor(new Color(225, 12, 83));
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);

		int x = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth(text) / 2;
		int y = getHeight() / 2 + g2.getFontMetrics(g2.getFont()).getHeight() / 4;

		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(2));
		g2.drawString(text, x, y);

		if (this.isEnabled() == false) {
			g2.setColor(new Color(82, 82, 82, 180));
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
		}
	}

}
