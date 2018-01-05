package jaims_development_studio.jaims.server.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.server.Server;

public class ServerGui extends JComponent {
	
	private static Logger			LOG					= LoggerFactory.getLogger(ServerGui.class);
	private static final long		serialVersionUID	= 1L;
	public static final Font		SERVER_GUI_FONT		= new Font("Monospaced", 0, 12);
	private final CountDownLatch	latch				= new CountDownLatch(1);
	private final Server			server;
	private JComponent				consoleComponent;
	
	/**
	 * Creates the server GUI and sets it visible for the user.
	 */
	public static void createServerGui(Server server) {
		if (SwingUtilities.isEventDispatchThread())
			initServerGui(server);
		else
			SwingUtilities.invokeLater(() -> initServerGui(server));
	}
	
	public static void initServerGui(Server server) {
		
		LOG.info("Creating server gui...");
		
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			LOG.warn("Could not update LookAndFeel", e);
		}
		
		ServerGui serverGui = new ServerGui(server);
		JFrame frame = new JFrame("Server");
		frame.add(serverGui);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				server.initiateShutdown();
				
				while (!server.isServerStopped())
					try {
						Thread.sleep(100);
					} catch (@SuppressWarnings("unused") InterruptedException ex) {
						LOG.warn("Unexpected exception while waiting for server to stop");
					}
				
				System.exit(0);
			}
		});
		
		while (serverGui.consoleComponent == null)
			try {
				Thread.sleep(50);
			} catch (@SuppressWarnings("unused") InterruptedException ex) {
			}
		serverGui.consoleComponent.requestFocusInWindow();

		LOG.info("Server gui initialized");
		serverGui.latch.countDown(); //now other threads can start using the gui since the initialization is finished
	}
	
	/**
	 * Create the ServerGui.
	 */
	private ServerGui(Server server) {
		
		this.server = server;

		setPreferredSize(new Dimension(854, 480));
		setLayout(new BorderLayout());
		
		try {
			consoleComponent = getConsoleComponent();
			add(consoleComponent, BorderLayout.CENTER);
			add(getStatsComponent(), BorderLayout.WEST);
		} catch (Exception e) {
			LOG.error("Couldn't build server GUI", e);
		}
	}

	/**
	 * Generates new ConsoleComponent and returns it.
	 */
	private ConsoleComponent getConsoleComponent() {
		LOG.debug("Initializing ConsoleComponent");
		return new ConsoleComponent(server, this);
	}
	
	/**
	 * Generates new StatsComponent and returns it.
	 */
	private JComponent getStatsComponent() {
		
		JPanel statsPanel = new JPanel(new BorderLayout());
		statsPanel.add(new StatsComponent(server), BorderLayout.NORTH);
		SwingUtilities.invokeLater(() -> statsPanel.add(getUserListComponent(), BorderLayout.CENTER));
		statsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
		
		return statsPanel;
	}
	
	/**
	 * Generates new UserListComponent and returns it.
	 */
	private JComponent getUserListComponent() {
		
		JList<String> userList = new UserListComponent(server);
		JScrollPane scrollPane = new JScrollPane(userList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Online users"));
		
		return scrollPane;
	}
	
	public CountDownLatch getLatch() {
		return latch;
	}
	
}
