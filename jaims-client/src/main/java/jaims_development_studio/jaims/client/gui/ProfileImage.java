package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProfileImage extends JPanel {
	
	Image img;
	boolean painted = false;
	int durchlauf = 0;
	
	public ProfileImage(Image img) {
		setPreferredSize(new Dimension(55, 55));
		setMaximumSize(getPreferredSize());
		setBorder(new RoundBorder(50, 50, Color.BLACK));
		//add(new JLabel(new ImageIcon(img)));
		this.img = img;
	}
	
	private void scaleImage() {
		System.out.println("Jup");
		float ratio = (float) img.getWidth(this)/ (float) img.getHeight(this);
		float width = ratio * 50;
		
		
		Image reImg = img.getScaledInstance((int) width, 50, Image.SCALE_SMOOTH);
		

      
	}
	
	public void addImage() {
		//scaleImage();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (painted == false) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			img = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			
			Graphics2D g2d = (Graphics2D) super.getGraphics();
			RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	        qualityHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
	        g2d.setRenderingHints(qualityHints);
	        
	        g2d.setClip(new RoundRectangle2D.Double(0, 0, 49, 49, 30, 30));
	        g2d.drawImage(img,0,0, this);
	        g2d.dispose();
	        
	        painted = true;
		}
	}

}
