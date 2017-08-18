package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		for (int i = 0; i < ReadFromDatabase.chatObjectsList.size(); i++) {
			if (ReadFromDatabase.chatObjectsList.get(i).getList() != null) {
				JPanel panel = new JPanel();
				panel.setBorder(new LineBorder(Color.DARK_GRAY));
				panel.setPreferredSize(new Dimension(10, 60));
				panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
				panel.add(Box.createRigidArea(new Dimension(10, 0)));
				try {
					if (ReadFromDatabase.chatObjectsList.get(i).getProfileContact().getProfilePicture() != null) {
						Image image = ImageIO.read(new ByteArrayInputStream(ReadFromDatabase.chatObjectsList.get(i).getProfileContact().getProfilePicture()));
						image = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
						panel.add(new JLabel(new ImageIcon(image)));
					}else {
						Image image = ImageIO.read(getClass().getClassLoader().getResource("images/JAIMS_Penguin.png"));
						image = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
						panel.add(new JLabel(new ImageIcon(image)));
					}
				}catch (IOException ioe) {
					LOG.error("Failed to create image");
				}
				panel.add(Box.createRigidArea(new Dimension(10, 0)));
				panel.add(new JLabel(ReadFromDatabase.chatObjectsList.get(i).getProfileContact().getNickname()));
				panel.add(Box.createHorizontalGlue());

				add(panel);
			}
		}
	}
	
	@Override
	public void run() {
		initGUI();
		
	}

}
