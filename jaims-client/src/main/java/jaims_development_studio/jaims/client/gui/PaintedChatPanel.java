package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PaintedChatPanel extends JPanel {
	
	Image img;
	String s;
	
	public PaintedChatPanel(Image img, String s) {
		this.img = img;
		this.s = s;
		
		setBorder(new LineBorder(Color.BLACK));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setPreferredSize(new Dimension(250, 55));
		setMaximumSize(getPreferredSize());
	}
	
	private void scaleMaintainAspectRatio() {
        float ratio = (float) img.getHeight(this)/ (float)img.getWidth(this);
        img = img.getScaledInstance(50, (int) (ratio*50), Image.SCALE_SMOOTH);
    }
	
	@Override
	public void paintComponent(Graphics g) {
//		g.setColor(getBackground());
//		g.fillRect(0, 0, 2000, 2000);
//		Graphics2D g2d = (Graphics2D) g;
//		scaleMaintainAspectRatio();
//		
//		BufferedImage bim = createRoundedImage();
////		g2d.setColor(Color.WHITE);
////		g2d.fillRoundRect(4, 1, 52, 52, 40, 40);
////		g2d.setColor(Color.black);
////		g2d.drawRoundRect(4, 1, 52, 52, 40, 40);
//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2d.drawImage(img, 0,0, this);
//		g2d.drawRect(0, 0, getWidth(), getHeight());
//		
//		g2d.setFont(new Font("Calibri", Font.BOLD, 15));
//		g2d.drawString(s, 65, 27);
//		
//		
//		g2d.dispose();
//		g.dispose();
	}
	
	private BufferedImage createRoundedImage() {
		BufferedImage bim = new BufferedImage(img.getWidth(this), img.getHeight(this), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) bim.getGraphics();
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);
        //g2.setClip(new RoundRectangle2D.Double(0, 0, img.getWidth(this), img.getHeight(this), 0, 0));
		g2.drawImage(img, 0, 0, null);
		g2.dispose();
		
		return bim;
	}

}
