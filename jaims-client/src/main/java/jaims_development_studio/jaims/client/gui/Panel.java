package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class Panel extends JPanel{
	
	Image img;
	String username;
	
	public Panel(Image img, String username) {
		this.img = scaleMaintainAspectRatio(img);
		this.username = username;
		setPreferredSize(new Dimension(250, 55));
		setMaximumSize(getPreferredSize());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(img, 10, 27 - (img.getHeight(this) / 2), this);
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Calibri", Font.BOLD, 15));
		g2d.drawString(username, 75, 27);
		//g2d.setStroke(new BasicStroke(4.0F));
		g2d.drawRect(0, 0, getWidth(), getHeight());
		
		g2d.drawRoundRect(9, 2, 51, 51, 15, 15);
		
		g2d.dispose();
		
	}
	
	private Image scaleMaintainAspectRatio(Image image) {
        float ratio = (float) image.getHeight(this)/ (float)image.getWidth(this);
        Image returnImg = image.getScaledInstance( 50, (int) (50*ratio), Image.SCALE_SMOOTH);
        return returnImg;
    }
	
}
