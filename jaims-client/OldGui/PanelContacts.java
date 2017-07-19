package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.logic.DatabaseManagement;

public class PanelContacts extends JPanel {
	
	ArrayList<ChatPanel> list = new ArrayList<>();
	
	public PanelContacts(DatabaseManagement  dm, MainFrame mf) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new LineBorder(Color.DARK_GRAY));
		initGUI(dm, mf);
	}
	
	private void initGUI(DatabaseManagement dm, MainFrame mf) {
		for (int i = 0; i < dm.getProfileList().size(); i++) {
			ChatPanel chatPanel = new ChatPanel(mf);
			
			chatPanel.setNickname(dm.getProfileList().get(i).getNickname());
			
			Image profileImage = null;
			try {
				profileImage = ImageIO.read(new ByteArrayInputStream(dm.getProfileList().get(i).getByteImage()));
				profileImage = profileImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageIcon ii = new ImageIcon(profileImage);
			JLabel lbl = new JLabel();
			lbl.setIcon(ii);
			
			chatPanel.add(lbl);
			chatPanel.add(Box.createRigidArea(new Dimension(15, 0)));
			
			JLabel username = new JLabel(dm.getProfileList().get(i).getNickname(), JLabel.LEFT);
			username.setFont(new Font("Calibri", Font.BOLD, 15));
			chatPanel.add(username);
			
			list.add(chatPanel);
		}
		
		Comparator<ChatPanel> comp = new Comparator<ChatPanel>() {

			@Override
			public int compare(ChatPanel p1, ChatPanel p2) {
				if (p1.getNickname().compareTo(p2.getNickname()) > 0) {
					return 1;
				}else if(p1.getNickname().compareTo(p2.getNickname()) < 0) {
					return -1;
				}else {
					return 0;
				}
			}
		};
		Collections.sort(list, comp);
		
		for (ChatPanel panel: list) {
			add(panel);
		}
	}

}
