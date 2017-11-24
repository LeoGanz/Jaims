package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class PreviewMessage extends JPanel{

	String message;
	
	public PreviewMessage(String message) {
		this.message = message;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		 Graphics2D g2d = (Graphics2D) g.create();
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        
	       	g2d.setColor(new Color(191, 225, 148));
	        g2d.fillRoundRect(1, 1, 250,getFontMetrics(getFont()).getHeight()+10, 20, 20);
	        g2d.setColor(Color.black);
	        g2d.drawRoundRect(0, 0, 251, getFontMetrics(getFont()).getHeight()+12, 20, 20);
	        g2d.setFont(getFont());
	        g2d.setStroke(new BasicStroke(1.0F));
	        
	        g2d.drawString(message, 10,10);
	        g2d.dispose();
	}
	
}
