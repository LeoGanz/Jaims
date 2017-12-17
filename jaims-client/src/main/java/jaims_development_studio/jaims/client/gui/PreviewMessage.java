package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.settings.Settings;

public class PreviewMessage extends JPanel{

	String message;
	boolean own;
	PreviewFrame pf;
	
	public PreviewMessage(String message, boolean own, PreviewFrame pf) {
		this.message = message;
		this.own = own;
		this.pf = pf;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (own) {
			Graphics2D g2d = (Graphics2D) g.create();
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setFont(new Font(Settings.ownFontName, Settings.ownFontStyle, Settings.ownFontSize));
	        
	       	g2d.setColor(Settings.colorOwnMessages);
	        g2d.fillRoundRect(2, 3, g2d.getFontMetrics(g2d.getFont()).stringWidth(message)+10, g2d.getFontMetrics(g2d.getFont()).getHeight()+3, Settings.arcMessages, Settings.arcMessages);
	        g2d.setColor(Settings.colorOwnMessageBorder);
	        g2d.drawRoundRect(1, 2, g2d.getFontMetrics(g2d.getFont()).stringWidth(message)+11, g2d.getFontMetrics(g2d.getFont()).getHeight()+4, Settings.arcMessages, Settings.arcMessages);
	        g2d.setColor(Settings.colorOwnMessageFont);	        
	        g2d.setStroke(new BasicStroke(1.0F));
	        g2d.drawString(message, 7,19);
	        
	        setS(g2d);
	        g2d.dispose();
		}else {
			Graphics2D g2d = (Graphics2D) g.create();
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setFont(new Font(Settings.contactFontName, Settings.contactFontStyle, Settings.contactFontSize));
	        
	       	g2d.setColor(Settings.colorContactMessages);
	       	g2d.fillRoundRect(2, 3, g2d.getFontMetrics(g2d.getFont()).stringWidth(message)+10, g2d.getFontMetrics(g2d.getFont()).getHeight()+3, Settings.arcMessages, Settings.arcMessages);
	        g2d.setColor(Settings.colorContactMessageBorder);
	        g2d.drawRoundRect(1, 2, g2d.getFontMetrics(g2d.getFont()).stringWidth(message)+11, g2d.getFontMetrics(g2d.getFont()).getHeight()+4, Settings.arcMessages, Settings.arcMessages);
	        g2d.setColor(Settings.colorContactMessageFont);
	        g2d.setStroke(new BasicStroke(1.0F));
	        g2d.drawString(message, 7,19);
	        
	        setS(g2d);
	        g2d.dispose();
		}
		 	
	}
	
	private void setS(Graphics2D g2d) {
		setPreferredSize(new Dimension(g2d.getFontMetrics(g2d.getFont()).stringWidth(message)+15, g2d.getFontMetrics(g2d.getFont()).getHeight()+8));
		setMaximumSize(getPreferredSize());
		pf.reCon();
	}
	
}
