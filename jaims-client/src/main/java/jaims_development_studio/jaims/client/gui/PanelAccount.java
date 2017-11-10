package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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

public class PanelAccount extends JPanel implements Runnable{
	
	String username;
	
	public PanelAccount(String name) {
		username = name;
	}
	
	public void initGUI() {
		
		Image image = Toolkit.getDefaultToolkit().getImage("G:/Bilder/Camera/20140806_083211.jpg");
		image = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        
		setBorder(new LineBorder(Color.black));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(Box.createRigidArea(new Dimension(10, 0)));
		
		JLabel lbl = new JLabel(new ImageIcon(image));
		lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(lbl);
		
		add(Box.createRigidArea(new Dimension(10, 0)));
		
		JLabel lbl2 = new JLabel(username);
		lbl2.setFont(new Font("Calibri", Font.PLAIN, 15));
		add(lbl2);
		
		add(Box.createHorizontalGlue());
		
		add(new SettingDots());
		
		//setPreferredSize(new Dimension(250, 60));
	}

	@Override
	public void run() {
		initGUI();		
	}

}
