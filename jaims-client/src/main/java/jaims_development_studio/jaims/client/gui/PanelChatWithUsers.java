package jaims_development_studio.jaims.client.gui;

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
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
import jaims_development_studio.jaims.client.database.ReadFromDatabase;

public class PanelChatWithUsers extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6014240532566432438L;
	private static final Logger LOG = LoggerFactory.getLogger(PanelChatWithUsers.class);
	
	public PanelChatWithUsers() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	private void initGUI() {
		
		List<ChatObjects> list = ReadFromDatabase.chatObjectsList2;
		Comparator<ChatObjects> comp = new Comparator<ChatObjects>() {

			@Override
			public int compare(ChatObjects o1, ChatObjects o2) {
				if (o1.getList().get(o1.getList().size()-1).getTimestampRecieved().compareTo(o2.getList().get(o2.getList().size()-1).getTimestampRecieved()) > 0) {
					return 1;
				}else if (o1.getList().get(o1.getList().size()-1).getTimestampRecieved().compareTo(o2.getList().get(o2.getList().size()-1).getTimestampRecieved()) < 0 ) {
					return -1;
				}else {
					return 0;
				}
			}
			
		};
		list.sort(comp);
		
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getList() != null) {
				JPanel panel = new JPanel();
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
	}
	
	@Override
	public void run() {
		initGUI();
		
	}

}
