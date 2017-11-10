package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class SettingDots extends JPanel {
	
	public SettingDots() {
		setPreferredSize(new Dimension(38, 38));
		setMaximumSize(getPreferredSize());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(200, 200, 200));
		g2d.fillOval(2, 2, 34, 34);
		
		g2d.setColor(Color.BLACK);
		//g2d.drawOval(1, 1, 35, 35);
		g2d.fillOval(5, 15, 8, 8);
		g2d.fillOval(15,15,8, 8);
		g2d.fillOval(25,15,8, 8);
		
		g2d.dispose();
	}

}