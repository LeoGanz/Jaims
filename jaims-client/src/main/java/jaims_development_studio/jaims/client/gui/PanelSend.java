package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class PanelSend extends JPanel{
	
	public PanelSend() {
		setPreferredSize(new Dimension(32, 33));
		setMaximumSize(getPreferredSize());
		setMinimumSize(getPreferredSize());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.BLUE);
		g2d.fillOval(0, 0, 32, 33);
		
		g2d.setColor(Color.WHITE);
		int[] x = {6,6,31};
		int[] y = {6,26,16};
		g2d.fillPolygon(x,y,3);
		
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(0.6F));
		g2d.drawLine(8, 16, 29, 16);
		g2d.drawLine(8, 15, 29, 15);
		
		g2d.dispose();
	}

}
