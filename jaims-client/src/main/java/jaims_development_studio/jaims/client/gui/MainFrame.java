package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.apache.commons.io.IOUtils;

import jaims_development_studio.jaims.client.logic.DatabaseManagement;
import jaims_development_studio.jaims.client.logic.Profile;
import jaims_development_studio.jaims.client.networking.ServerConnection;

public class MainFrame {
	
	private static JFrame mainFrame;
	private File errorFile;
	private BufferedWriter bw;
	private DateFormat df;
	private Date today;
	private String reportDate;
	private ServerConnection con;
	private DatabaseManagement dm;
	private MainFrame mf;
	public Thread threadConnection;
	Profile userProfile;
	PanelProgram pp;
	
	private static Container c;
	private static JPanel centerpanel = null;

	public MainFrame() {
		mf = this;
		con = new ServerConnection(mf);
		threadConnection = new Thread(con);
		threadConnection.start();
		initGUI();
	}
	
	private void initGUI() {
		mainFrame = new JFrame("JAIMS");
		mainFrame.setSize(new Dimension(600, 450));
		mainFrame.setMinimumSize(new Dimension(600, 450));
		//mainFrame.setLocationRelativeTo(null);
		mainFrame.setLocationByPlatform(true);
		mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			try {
				errorFile = new File(getClass().getClassLoader().getResource("errorfile.err").toURI());
				try {
					bw = new BufferedWriter(new FileWriter(errorFile));
					df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					today = Calendar.getInstance().getTime();
					reportDate = df.format(today);
					bw.write("\n" + reportDate + " " + e1.getMessage() + "\n");
					bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		mainFrame.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				c.revalidate();
				c.repaint();
				
			}
		});
		mainFrame.addWindowStateListener(new WindowStateListener() {
			
			@Override
			public void windowStateChanged(WindowEvent evt) {
				if (evt.getNewState() != Frame.NORMAL) {
					c.revalidate();
					c.repaint();
				}
				
			}
		});
		mainFrame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
				
			}
			
		});
		
		c = mainFrame.getContentPane();
		c.setLayout(new BorderLayout());
		
		userProfile = new Profile();
		userProfile.setNickname("Bu88le");
		try {
			userProfile.setByteImage(IOUtils.toByteArray(this.getClass().getClassLoader().getResource("images/JAIMS_PENGUIN.png")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		userProfile.setUUID(UUID.randomUUID());
		
		DatabaseManagement dm = new DatabaseManagement(this, userProfile);
		dm.setExample();
		
		panelLogin();
		mainFrame.setVisible(true);
	}
	
	public void panelLogin() {
		if (centerpanel != null) {
			c.remove(centerpanel);
		}
		c.add(centerpanel = new PanelLogin(mainFrame, mf), BorderLayout.CENTER);
		c.revalidate();
	}
	
	public void panelProgramm() {
		c.remove(centerpanel);
		pp = new PanelProgram(this, userProfile);
		c.add(centerpanel = pp, BorderLayout.CENTER);
		c.revalidate();
		//addListeners();
	}
	
	
	public ServerConnection getConnection() {
		return con;
	}
	
	public void setDatabaseManagement(DatabaseManagement dm) {
		this.dm = dm;
	}
	
	public JPanel getCenterpanel() {
		return centerpanel;
	}
	
	public JFrame getFrame() {
		return mainFrame;
	}
	
	public Container getContainer() {
		return c;
	}
	
	private void addListeners() {
		mainFrame.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				//panelChatWithUsers.resized();
				pp.doResizing();
				
				c.revalidate();
				c.repaint();
			}
		});
		mainFrame.addWindowStateListener(new WindowStateListener() {
			
			@Override
			public void windowStateChanged(WindowEvent e) {
				//panelChatWithUsers.resized();
				pp.doResizing();
				
				c.revalidate();
				c.repaint();
				
			}
		});
	}
}
