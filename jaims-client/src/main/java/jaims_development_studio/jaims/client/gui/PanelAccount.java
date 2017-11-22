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
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.hibernate.loader.plan.exec.process.spi.ScrollableResultSetProcessor;

public class PanelAccount extends JPanel{
	
	String username;
	Image img;
	
	public PanelAccount(Image img, String name) {
		this.img = scaleMaintainAspectRatio(img);
		username = name;
		setPreferredSize(new Dimension(200, 60));
		setMaximumSize(getPreferredSize());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	public void initGUI() {
		
//		Image image = null;
//		try {
//			image = ImageIO.read(getClass().getResourceAsStream("/images/JAIMS_Penguin.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		image = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
//        
//		setBorder(new LineBorder(Color.black));
//        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
//		add(Box.createRigidArea(new Dimension(10, 0)));
//		
//		JLabel lbl = new JLabel(new ImageIcon(image));
//		lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
//		add(lbl);
//		
//		add(Box.createRigidArea(new Dimension(10, 0)));
//		
//		JLabel lbl2 = new JLabel(username);
//		lbl2.setFont(new Font("Calibri", Font.PLAIN, 15));
//		add(lbl2);
//		
//		add(Box.createHorizontalGlue());
//		
//		
		
		//setPreferredSize(new Dimension(250, 60));
	}
	
	private Image scaleMaintainAspectRatio(Image image) {
        float ratio = (float) image.getHeight(this)/ (float)image.getWidth(this);
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
