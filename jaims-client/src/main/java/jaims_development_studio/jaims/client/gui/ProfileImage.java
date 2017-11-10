package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ProfileImage extends JPanel {
	
	Image img;
	boolean painted = false;
	int durchlauf = 0;
	
	public ProfileImage(Image img) {
		setPreferredSize(new Dimension(55, 55));
		setMaximumSize(getPreferredSize());
		this.img = img;
		addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				createRoundedImage();
				revalidate();
				repaint();
			}
		});
	}
	
	private void scaleMaintainAspectRatio() {
        float ratio = (float) img.getHeight(this)/ (float)img.getWidth(this);
        img = img.getScaledInstance( 53, 53, Image.SCALE_SMOOTH);
    }
	
	private void createRoundedImage() {
		scaleMaintainAspectRatio();
		
		BufferedImage bim = new BufferedImage(img.getWidth(this), img.getHeight(this), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) bim.getGraphics();
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);
        g2.setClip(new RoundRectangle2D.Double(1, (int) (27-(img.getHeight(this)/2)), img.getWidth(this), img.getHeight(this), 5, 5));
		g2.drawImage(img, -5, -30, null);
		g2.dispose();
		
		Graphics2D g2d = (Graphics2D) getGraphics();
		g2d.setColor(Color.black);
		g2d.drawRoundRect(0, 0, 50, 50, 30, 30);
		
	}	
	
	public void addImage() {
		//scaleImage();
	}

}
