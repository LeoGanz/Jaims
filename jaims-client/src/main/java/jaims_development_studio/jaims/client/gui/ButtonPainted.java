package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JButton;

public class ButtonPainted extends JButton {

	private String	paintString;
	Image			img;

	public ButtonPainted(String paintString) {

		this.paintString = paintString;
		setPreferredSize(new Dimension(250, 75));
		setMaximumSize(getPreferredSize());
		img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/button.png"));
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(img, 0, 0, this);

		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Sans Serif", Font.BOLD, 16));

		int width = g2d.getFontMetrics(g2d.getFont()).stringWidth(paintString);
		int height = g2d.getFontMetrics(g2d.getFont()).getHeight();

		g2d.drawString(paintString, (getWidth() / 2) - (width / 2), (getHeight() / 2) + (height / 3));
	}

}
