package jaims_development_studio.jaims.client.logic;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.Profile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.gui.AddSign;
import jaims_development_studio.jaims.client.gui.ContactPanel;
import jaims_development_studio.jaims.client.gui.JaimsFrame;
import jaims_development_studio.jaims.client.gui.LoginPanel;
import jaims_development_studio.jaims.client.gui.PanelAccount;
import jaims_development_studio.jaims.client.gui.PanelChat;
import jaims_development_studio.jaims.client.gui.PanelContactsAndChats;
import jaims_development_studio.jaims.client.gui.PanelSettings;
import jaims_development_studio.jaims.client.gui.RecordingFrame;
import jaims_development_studio.jaims.client.gui.SettingDots;
import jaims_development_studio.jaims.client.networking.ServerConnection;

public class ClientMain {

	private static final Logger LOG = LoggerFactory.getLogger(ClientMain.class);
	public static boolean confirmationRecieved = false;
	Point p = new Point(10, 5);
	JPanel panel = new JPanel();
	Rectangle r = new Rectangle(150, 150, 500, 500);
	ContactPanel activeContactPanel;
	PanelChat activePanelChat;
	PanelSettings ps;
	
	Thread threadDatabaseManagement, threadPCC;
	DatabaseConnection dc;
	PanelContactsAndChats pcc;
	JaimsFrame jf;
	
	public static Profile userProfile;
	
	public static void main(String[] args) {
		new ClientMain();
	}
	
	public ClientMain() {
		initProgram();
	}
	
	private void initProgram() {
		jf = new JaimsFrame();
		
		dc = new DatabaseConnection();
		dc.initConnection();
		dc.writeToDatabase();
		
		Thread thread = new Thread(new ServerConnection());
		thread.start();
		
		try {
			Thread.sleep(1800);
		} catch (InterruptedException e) {
			LOG.error("Sleep interrupted");
		}
		
		jf.initGUI();
		
		LoginPanel lp = new LoginPanel(jf, this);
		jf.getContentPane().add(lp, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		
	}
	
	public void startCreatingChatWindow(String username) {
		
		userProfile = new Profile(UUID.randomUUID(), "Bu88le", "Test", "Test", null, new Date(System.currentTimeMillis()));
		
		Thread thread = dc.readFromDatabase(username);
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PanelAccount pa = null;
		try {
			pa = new PanelAccount(ImageIO.read(getClass().getResourceAsStream("/images/JAIMS_Penguin.png")), username);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JPanel p = new JPanel();
		p.setBorder(new LineBorder(Color.GRAY));
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		p.add(pa);
		p.add(Box.createHorizontalGlue());
		p.add(new AddSign(this));
		p.add(Box.createRigidArea(new Dimension(3, 0)));
		p.add(new SettingDots(this));
		
		threadPCC = new Thread(pcc = new PanelContactsAndChats(jf, this));
		threadPCC.start();
		
		try {
			threadPCC.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jf.getContentPane().removeAll();
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(p, BorderLayout.PAGE_START);
		panel.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.CENTER);
		panel.add(pcc, BorderLayout.PAGE_END);
		jf.getContentPane().add(panel, BorderLayout.LINE_START);
		jf.getContentPane().setBackground(Color.WHITE);
		panel.revalidate();
		panel.repaint();
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();
		jf.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
	}
	
	public void setMessagePanel(PanelChat pc) {
		if (ps != null) {
			jf.getContentPane().remove(ps);
			ps.getPF().dispose();
			jf.revalidate();
		}
		if (activePanelChat != null) {
			jf.getContentPane().remove(activePanelChat);
			jf.getContentPane().revalidate();
		}
		jf.getContentPane().add(pc, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();
		
		activePanelChat = pc;
	}
	
	public void setSettingPanel(PanelSettings ps) {
		if (activePanelChat != null) {
			jf.getContentPane().remove(activePanelChat);
			jf.revalidate();
		}
		jf.getContentPane().add(ps, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();
		
		this.ps = ps;
	}
	
	public void showRecordFrame() {	
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
				g2.setColor(Color.LIGHT_GRAY);
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		
		RecordingFrame rf = new RecordingFrame(activePanelChat);
		rf.setLocationRelativeTo(activePanelChat);
		rf.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		jf.getContentPane().setEnabled(false);
		jf.getContentPane().add(panel, BorderLayout.CENTER);
		jf.getContentPane().repaint();
		rf.setVisible(true);
		
		
	}
	
	public void repaintPanelLeft() {
		panel.repaint();
		jf.getContentPane().repaint();
	}
	
	public void setAcvtiveContactPanel(ContactPanel cp) {
		activeContactPanel = cp;
	}
	
	public JaimsFrame getJaimsFrame() {
		return jf;
	}
}