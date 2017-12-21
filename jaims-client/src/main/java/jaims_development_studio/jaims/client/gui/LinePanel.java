package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class LinePanel extends JPanel{
	
	public LinePanel() {
		setPreferredSize(new Dimension(250, 14));
		setMaximumSize(getPreferredSize());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillOval(10, 7, 230, 4);
		
		g2d.dispose();
	}

}
