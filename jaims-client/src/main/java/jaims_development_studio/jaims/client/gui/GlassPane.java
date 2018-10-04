package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GlassPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(80, 80, 80, 180));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
