package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelAccount extends JPanel implements Runnable{
	
	String username;
	ClientMain cm;
	
	public PanelAccount(String name, ClientMain cm) {
		username = name;
		this.cm = cm;
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
		
		add(new SettingDots(cm));
		
		//setPreferredSize(new Dimension(250, 60));
	}

	@Override
	public void run() {
		initGUI();		
	}

}
