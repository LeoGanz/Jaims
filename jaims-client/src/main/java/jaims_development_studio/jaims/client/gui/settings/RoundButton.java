package jaims_development_studio.jaims.client.gui.settings;

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

import javax.swing.JButton;
import javax.swing.JPanel;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class RoundButton extends JButton {

	private Color	backgroundColor;
	private String	text;
	private Font	font;

	public RoundButton(Color bc, String text, JPanel parentPanel, GUIMain guiMain) {

		backgroundColor = bc;
		this.text = text;
		initGUI(parentPanel, guiMain);
	}

	private void initGUI(JPanel parentPanel, GUIMain guiMain) {

		setPreferredSize(new Dimension(230, 36));
		setMaximumSize(getPreferredSize());
		setOpaque(true);
		setBorderPainted(false);
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				if (isEnabled()) {
					setPreferredSize(new Dimension(230, 50));
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
					setPreferredSize(new Dimension(220, 45));
					setMaximumSize(getPreferredSize());
					repaint();
					parentPanel.revalidate();
					parentPanel.repaint();
					guiMain.getJaimsFrame().getContentPane().repaint();
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {

				setPreferredSize(new Dimension(230, 36));
				setMaximumSize(getPreferredSize());
				repaint();
				parentPanel.revalidate();
				parentPanel.repaint();
				guiMain.getJaimsFrame().getContentPane().repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {

				if (isEnabled()) {
					setPreferredSize(new Dimension(230, 50));
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

		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(font);
		g2.setColor(backgroundColor);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);

		int x = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth(text) / 2;
		int y = getHeight() / 2 + g2.getFontMetrics(g2.getFont()).getHeight() / 4;

		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(2));
		g2.drawString(text, x, y);

		if (this.isEnabled() == false) {
			g2.setColor(backgroundColor);
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
		}
	}

}
