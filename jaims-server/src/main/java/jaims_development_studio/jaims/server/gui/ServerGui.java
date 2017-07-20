package jaims_development_studio.jaims.server.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.server.Server;
import jaims_development_studio.jaims.server.logging.GUIConsoleAppender;
import jaims_development_studio.jaims.server.util.History;

public class ServerGui extends JComponent {
	
	private static Logger			LOG					= LoggerFactory.getLogger(ServerGui.class);
	private static final long		serialVersionUID	= 1L;
	private static final Font		SERVER_GUI_FONT		= new Font("Monospaced", 0, 12);
	private final CountDownLatch	latch				= new CountDownLatch(1);
	private final Server			server;
	private final History<String>	inputHistory		= new History<>(64);
	
	/**
	 * Creates the server GUI and sets it visible for the user.
	 */
	public static void createServerGui(Server server) {
		
		LOG.info("Creating server gui...");
		
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
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
			add(getLogComponent(), BorderLayout.CENTER);
			add(getStatsComponent(), BorderLayout.WEST);
		} catch (Exception e) {
			LOG.error("Couldn't build server GUI", e);
		}
		
	}
	
	/**
	 * Generates new LogComponent and returns it.
	 */
	private JComponent getLogComponent() {
		
		JTextArea textAreaLog = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textAreaLog, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textAreaLog.setEditable(false);
		textAreaLog.setFont(SERVER_GUI_FONT);
		// disabled focus listener for textAreaLog needed?
		
		JTextField textFieldCommandLine = new JTextField();
		textFieldCommandLine.addActionListener((e) -> {
			String input = textFieldCommandLine.getText().trim();
			if (!input.isEmpty()) {
				server.addPendingCommand(input, server);
				inputHistory.add(input);
			}
			textFieldCommandLine.setText("");
		});
		textFieldCommandLine.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					String up = inputHistory.up(textFieldCommandLine.getText());
					if (up != null)
						textFieldCommandLine.setText(up);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					String down = inputHistory.down();
					if (down != null)
						textFieldCommandLine.setText(down);
				}
			}
			
		});
		
		JPanel logPanel = new JPanel(new BorderLayout());
		logPanel.add(scrollPane, BorderLayout.CENTER);
		logPanel.add(textFieldCommandLine, BorderLayout.SOUTH);
		logPanel.setBorder(new TitledBorder(new EtchedBorder(), "Log and broadcasts"));
		
		Thread thread = new Thread(() -> {
			String msg;
			while ((msg = GUIConsoleAppender.getNextLogEvent()) != null)
				appendLine(textAreaLog, scrollPane, msg);
		});
		thread.setName("LogListener");
		thread.setDaemon(true);
		thread.start();
		
		LOG.debug("Log component created");
		
		return logPanel;
	}
	
	/**
	 * Generates new StatsComponent and returns it.
	 */
	private JComponent getStatsComponent() {
		
		JPanel statsPanel = new JPanel(new BorderLayout());
		statsPanel.add(new StatsComponent(server), BorderLayout.NORTH);
		statsPanel.add(getUserListComponent(), BorderLayout.CENTER);
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
	
	private void appendLine(JTextArea textArea, JScrollPane scrollPane, String line) {
		
		try {
			latch.await();
		} catch (@SuppressWarnings("unused") InterruptedException e) {
		} //Prevent logging until after constructor has ended.
		
		if (!SwingUtilities.isEventDispatchThread())
			SwingUtilities.invokeLater(() -> appendLine(textArea, scrollPane, line));
		else {
			Document document = textArea.getDocument();
			JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
			boolean updateScrollBar = false;
			
			if (scrollPane.getViewport().getView() == textArea)
				updateScrollBar = (scrollBar.getValue() + scrollBar.getSize().getHeight() + (SERVER_GUI_FONT.getSize() * 4)) > scrollBar.getMaximum();
				
				try {
					document.insertString(document.getLength(), line, null);
				} catch (BadLocationException e) {
					LOG.warn("Couldn't add line to logging view", e);
				}
				
				if (updateScrollBar)
					scrollBar.setValue(Integer.MAX_VALUE);
		}
	}
}
