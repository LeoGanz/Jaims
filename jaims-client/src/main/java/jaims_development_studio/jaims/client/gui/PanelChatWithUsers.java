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
import java.util.ArrayList;
import java.util.Collections;
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

public class PanelChatWithUsers extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6014240532566432438L;
	private static final Logger LOG = LoggerFactory.getLogger(PanelChatWithUsers.class);
	ProfileImage lbl;
	PanelContactsAndChats jtp;
	
	List<ContactPanel> panels;

	public PanelChatWithUsers(List<ContactPanel> panels) {
		this.panels = Collections.synchronizedList(new ArrayList<ContactPanel>());
		for (ContactPanel cp : panels)
			this.panels.add(cp);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public void initGUI() {

		Comparator<ContactPanel> comp = new Comparator<ContactPanel>() {

			@Override
			public int compare(ContactPanel o1, ContactPanel o2) {
				if (o1.getChatObject().getList().get(o1.getChatObject().getList().size()-1).getTimestampRecieved().compareTo(o2.getChatObject().getList().get(o2.getChatObject().getList().size()-1).getTimestampRecieved()) > 0) {
					return 1;
				}else if (o1.getChatObject().getList().get(o1.getChatObject().getList().size()-1).getTimestampRecieved().compareTo(o2.getChatObject().getList().get(o2.getChatObject().getList().size()-1).getTimestampRecieved()) < 0 ) {
					return -1;
				}else {
					return 0;
				}
			}
			
		};
		panels.sort(comp);

		for (ContactPanel panel: panels) {
			JPanel jp = new JPanel();
			jp.setBorder(new LineBorder(Color.BLACK));
			jp.setCursor(new Cursor(Cursor.HAND_CURSOR));
			{
				jp.setLayout(new BoxLayout(jp, BoxLayout.LINE_AXIS));
				jp.add(Box.createRigidArea(new Dimension(10, 0)));
				try {
					if (panel.getChatObject().getProfileContact().getProfilePicture() != null) {
						//lbl = new ProfileImage(ImageIO.read(new ByteArrayInputStream(panel.getChatObject().getProfileContact().getProfilePicture())));
						
						Image image = ImageIO.read(new ByteArrayInputStream(panel.getChatObject().getProfileContact().getProfilePicture()));
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
						
						jp.add(lbl);
					}else {
						Image image = ImageIO.read(getClass().getClassLoader().getResource("images/JAIMS_Penguin.png"));
						image = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
						jp.add(new JLabel(new ImageIcon(image)));
					}
				}catch (IOException ioe) {
					LOG.error("Failed to create image");
				}
				jp.add(Box.createRigidArea(new Dimension(10, 0)));
				JLabel lbl = new JLabel(panel.getChatObject().getProfileContact().getNickname());
				lbl.setFont(new Font("Calibri", Font.PLAIN, 15));
				jp.add(lbl);
				jp.add(Box.createHorizontalGlue());
				jp.addMouseListener(new MouseAdapter() {
					
					@Override
					public void mousePressed(MouseEvent e) {
						panel.setPanel(panel);			
					}
				});
			}
			add(jp);
			add(Box.createRigidArea(new Dimension(0, 5)));
		}
	}
	
	@Override
	public void run() {
		initGUI();
		
	}
	
	public List<ContactPanel> getList() {
		return panels;
	}

}


