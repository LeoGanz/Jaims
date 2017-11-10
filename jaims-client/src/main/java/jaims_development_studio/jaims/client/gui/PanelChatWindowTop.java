package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.chatObjects.Profile;

public class PanelChatWindowTop extends JPanel {
	
	public PanelChatWindowTop(Profile userProfile) {
		initGUI(userProfile);
	
	}
	
	private void initGUI(Profile userProfile) {
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setLayout(new BorderLayout(10,0));
		setBorder(new LineBorder(Color.black, 2));
		
		ImageIcon ii = null;
		try {
			Image i = ImageIO.read(new ByteArrayInputStream(userProfile.getProfilePicture()));
			i = i.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
			ii = new ImageIcon(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JLabel lbl = new JLabel(ii);
		add(lbl, BorderLayout.LINE_START);
		
		JLabel lbl2 = new JLabel(userProfile.getNickname(), JLabel.LEFT);
		lbl2.setFont(new Font("Calibri", Font.BOLD, 15));
		add(lbl2, BorderLayout.CENTER);
		
		add(Box.createRigidArea(new Dimension(0,40)), BorderLayout.LINE_END);
	}

}
