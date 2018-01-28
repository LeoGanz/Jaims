package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class PanelSend extends JPanel {

	Image img;

	public PanelSend() {

		setPreferredSize(new Dimension(30, 20));
		setMaximumSize(getPreferredSize());
		setMinimumSize(getPreferredSize());

		img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/Jaims_Send.png"));
		img = img.getScaledInstance(30, 20, Image.SCALE_SMOOTH);
	}

	@Override
	public void paintComponent(Graphics g) {

		// super.paintComponent(g);
		// g.setColor(new Color(0, 0, 0, 0));
		// g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.drawImage(img, 0, 0, this);

		g2d.dispose();
	}

}
