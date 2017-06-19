package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.logic.Profile;

public class PanelProfile extends JPanel implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5373258965538357082L;
	
	Profile userProfile;
	JPanel panelProfileImage, panelAddFriend, panelSettings;
	JLabel labelProfileImage, labelUsername, labelAddFriendImage, labelSettingsImage;
	Image profileImage, addFriendImage, settingsImage;
	ImageIcon profileImageIcon, addFriendImageIcon, settingsImageIcon;
	MainFrame mf;
	
	private PanelProgram panelProgram;
	
	public PanelProfile(Profile userProfile, MainFrame mf, PanelProgram panelProgram) {
		this.userProfile = userProfile;
		this.panelProgram = panelProgram;
		this.mf = mf;
		initGUI(userProfile, mf);
	}
	
	private void initGUI(Profile userProfile, MainFrame mf) {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setMinimumSize(new Dimension(200, 60));
		setMaximumSize(new Dimension(400, 60));
		setPreferredSize(new Dimension(mf.getFrame().getWidth()/3, 60));
		setBorder(new LineBorder(Color.BLACK));
		
		add(Box.createRigidArea(new Dimension(10, 0)));
		
		labelProfileImage = new JLabel();
		try {
			profileImage = ImageIO.read(new ByteArrayInputStream(userProfile.getByteImage()));
			profileImage = profileImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				profileImage = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/JAIMS_Penguin.png"));
				profileImage = profileImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		profileImageIcon = new ImageIcon(profileImage);
		labelProfileImage.setIcon(profileImageIcon);
		add(labelProfileImage);
		
		add(Box.createRigidArea(new Dimension(15,0)));
		
		labelUsername = new JLabel(userProfile.getNickname(), JLabel.LEFT);
		labelUsername.setFont(new Font("Calibri", Font.BOLD, 15));
		add(labelUsername);
		
		add(Box.createHorizontalGlue());
		
		try{
			addFriendImage = ImageIO.read(this.getClass().getClassLoader().getResource("images/add.png"));
			addFriendImage = addFriendImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		}catch (IOException e) {
			//TODO
			e.printStackTrace();
		}
		addFriendImageIcon = new ImageIcon(addFriendImage);
		labelAddFriendImage = new JLabel();
		labelAddFriendImage.setIcon(addFriendImageIcon);
		labelAddFriendImage.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		add(labelAddFriendImage);
		
		add(Box.createRigidArea(new Dimension(10, 0)));
		
		try {
			settingsImage = ImageIO.read(this.getClass().getClassLoader().getResource("images/dots.png"));
			settingsImage = settingsImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		}catch(IOException e) {
			//TODO
			e.printStackTrace();
		}
		settingsImageIcon = new ImageIcon(settingsImage);
		labelSettingsImage = new JLabel();
		labelSettingsImage.setIcon(settingsImageIcon);
		labelSettingsImage.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		add(labelSettingsImage);
		
		
		
	}
	
	public void resized() {
		removeAll();
		
		initGUI(userProfile, mf);
		revalidate();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(null, 0, 0, null);
		
	}
	
	private void doResizing() {
		removeAll();
		initGUI(userProfile, mf);
		revalidate();
		repaint();
	}

	@Override
	public void run() {
		doResizing();
		
	}

}
