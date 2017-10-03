package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
import jaims_development_studio.jaims.client.database.ReadFromDatabase;

public class PanelContacts extends JPanel implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PanelContacts.class);
	
	PanelContactsAndChats pcc;

	public PanelContacts(PanelContactsAndChats pcc) {
		this.pcc = pcc;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		//setPreferredSize(new Dimension(200, 10));
		add(Box.createRigidArea(new Dimension(0, 8)));
	}
	
	private void initGUI() {
		List<ChatObjects> list = new ArrayList<>(ReadFromDatabase.chatObjectsList);
		
		
		Comparator<ChatObjects> comp = new Comparator<ChatObjects>() {

			@Override
			public int compare(ChatObjects o1, ChatObjects o2) {
				if (o1.getProfileContact().getNickname().compareTo(o2.getProfileContact().getNickname()) > 0) {
					return 1;
				}else if (o1.getProfileContact().getNickname().compareTo(o2.getProfileContact().getNickname()) < 0 ) {
					return -1;
				}else {
					return 0;
				}
			}
			
		};
		list.sort(comp);
		
		for (int i = 0; i < list.size(); i++) {
			JPanel panel = new JPanel();
			/*if (i%2 == 0) {
				panel.setBackground(Color.GRAY);
			}else {
				panel.setBackground(Color.LIGHT_GRAY);
			}*/
			//panel.setPreferredSize(new Dimension(10, 60));
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			panel.add(Box.createRigidArea(new Dimension(10, 0)));
			try {
				if (list.get(i).getProfileContact().getProfilePicture() != null) {
					Image image = ImageIO.read(new ByteArrayInputStream(list.get(i).getProfileContact().getProfilePicture()));
					ImageObserver io = this;
					Image bimage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
					JLabel lbl = new JLabel(new ImageIcon(bimage));
					lbl.addMouseListener(new MouseAdapter() {
					
						@Override
						public void mousePressed(MouseEvent arg0) {
							JFrame frame = new JFrame();
							frame.setSize(image.getWidth(io), image.getHeight(io));
							frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
							frame.add(new JLabel(new ImageIcon(image)));
							frame.setVisible(true);
						}
					});
					lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
					panel.add(lbl);
				}else {
					Image image = ImageIO.read(getClass().getClassLoader().getResource("images/JAIMS_Penguin.png"));
					image = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
					panel.add(new JLabel(new ImageIcon(image)));
				}
			}catch (IOException ioe) {
				LOG.error("Failed to create image");
			}
			panel.add(Box.createRigidArea(new Dimension(10, 0)));
			JLabel lbl = new JLabel(list.get(i).getProfileContact().getNickname());
			lbl.setFont(new Font("Calibri", Font.PLAIN, 15));
			panel.add(lbl);
			panel.add(Box.createHorizontalGlue());
			panel.setBorder(new LineBorder(new Color(191, 191, 191)));
			add(panel);
			add(Box.createRigidArea(new Dimension(0, 5)));
		}
	}
	

	
	@Override
	public void run() {
		initGUI();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
	}

}
