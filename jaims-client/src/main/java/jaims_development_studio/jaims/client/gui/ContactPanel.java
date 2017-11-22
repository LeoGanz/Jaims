package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
import jaims_development_studio.jaims.client.chatObjects.Profile;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class ContactPanel extends JPanel{
	
	private static final Logger LOG = LoggerFactory.getLogger(ContactPanel.class);
	ChatObjects co;
	PanelChatMessages pcm;
	PanelChat pc;
	JaimsFrame jf;
	ClientMain cm;
	
	public ContactPanel(ChatObjects co, JaimsFrame jf, ClientMain cm) {
		this.co = co;
		this.jf = jf;
		this.cm = cm;
		initGUI(co.getProfileContact(), jf, cm);
	}
	
	private void initGUI(Profile pf, JaimsFrame jf, ClientMain cm) {
		
//		try {
//			PaintedChatPanel pcp = new PaintedChatPanel(ImageIO.read(new ByteArrayInputStream(pf.getProfilePicture())), pf.getNickname());
//			add(pcp);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
//		setCursor(new Cursor(Cursor.HAND_CURSOR));
//		add(Box.createRigidArea(new Dimension(10, 0)));
//		try {
//			if (pf.getProfilePicture() != null) {
//				Image image = ImageIO.read(new ByteArrayInputStream(pf.getProfilePicture()));
//				ImageObserver io = this;
//				Image bimage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
//				JLabel lbl = new JLabel(new ImageIcon(bimage));
//				lbl.addMouseListener(new MouseAdapter() {
//				
//					@Override
//					public void mousePressed(MouseEvent arg0) {
//						JFrame frame = new JFrame();
//						frame.setSize(image.getWidth(io), image.getHeight(io));
//						frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//						frame.add(new JLabel(new ImageIcon(image)));
//						frame.setVisible(true);
//					}
//				});
//				lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
//				add(lbl);
//			}else {
//				Image image = ImageIO.read(getClass().getClassLoader().getResource("images/JAIMS_Penguin.png"));
//				image = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
//				add(new JLabel(new ImageIcon(image)));
//			}
//		}catch (IOException ioe) {
//			LOG.error("Failed to create image");
//		}
//		add(Box.createRigidArea(new Dimension(10, 0)));
//		JLabel lbl = new JLabel(pf.getNickname());
//		lbl.setFont(new Font("Calibri", Font.PLAIN, 15));
//		add(lbl);
//		add(Box.createHorizontalGlue());
//		setBorder(new LineBorder(Color.BLACK));
		
		ContactPanel cp = this;
		
		
		pcm = new PanelChatMessages(jf, pf, co);
		Thread thread = new Thread(pcm);
		thread.start();
		pc = new PanelChat(pf, pcm);
		Thread thread2 = new Thread(pc);
		thread2.start();
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				cm.setAcvtiveContactPanel(cp);
				cm.setMessagePanel(pc);
			}
		});
	}
	
	public void setPanel(ContactPanel cp) {
		
		cm.setAcvtiveContactPanel(cp);
		cm.setMessagePanel(pc);
		
		System.out.println(pc.getSize().getWidth());
	}
	
	public ChatObjects getChatObject() {
		return co;
	}
	
}
