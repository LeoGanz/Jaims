package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class PanelAccount extends JPanel{
	
	private String username;
	private Image img;
	
	public PanelAccount(Image img, String name) {
		this.img = scaleMaintainAspectRatio(img);
		username = name;
		setPreferredSize(new Dimension(200, 60));
		setMaximumSize(getPreferredSize());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	private Image scaleMaintainAspectRatio(Image image) {
        Image returnImg = image.getScaledInstance( 55, 55, Image.SCALE_SMOOTH);
        return returnImg;
    }
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(img, 10, 3, this);
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Calibri", Font.BOLD, 15));
		g2d.drawString(username, 75, 30);
		g2d.drawRoundRect(9, 1, 56, 56, 15, 15);
		g2d.dispose();
		
	}
}
