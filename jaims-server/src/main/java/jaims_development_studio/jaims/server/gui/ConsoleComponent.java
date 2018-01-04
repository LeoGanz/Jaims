package jaims_development_studio.jaims.server.gui;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;

import jaims_development_studio.jaims.api.command.ICommandManager;
import jaims_development_studio.jaims.api.logging.GUIConsoleAppender;
import jaims_development_studio.jaims.api.util.History;
import jaims_development_studio.jaims.server.Server;

public class ConsoleComponent extends JComponent {
	
	private static final long		serialVersionUID	= 1L;
	private static Logger			LOG					= LoggerFactory.getLogger(ConsoleComponent.class);
	private final Server			server;
	private final ServerGui			serverGui;
	private final ICommandManager	commandManager;
	private final History<String>	commandHistory;
	private final JTextField		textFieldCommandLine;
	private final JTextArea			textAreaConsole;
	private final JScrollPane		scrollPaneConsole;
	private List<String>			tabCompletions;
	private Iterator<String>		completionsIterator;
	private int						typedLength;
	private int						internalChange;
	
	public ConsoleComponent(Server server, ServerGui serverGui) {
		this.server = server;
		this.serverGui = serverGui;
		commandManager = server.getCommandManager();
		commandHistory = new History<>(64);
		
		setLayout(new BorderLayout());
		addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusGained(FocusEvent e) {
				textFieldCommandLine.requestFocusInWindow();
			}
		});
		
		textAreaConsole = new JTextArea();
		scrollPaneConsole = new JScrollPane(textAreaConsole, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textAreaConsole.setEditable(false);
		textAreaConsole.setFont(ServerGui.SERVER_GUI_FONT);
		// disabled focus listener for textAreaLog needed?
		
		textFieldCommandLine = new JTextField();
		textFieldCommandLine.setFocusTraversalKeysEnabled(false);
		textFieldCommandLine.addActionListener((e) -> {
			String input = textFieldCommandLine.getText().trim();
			if (!input.isEmpty()) {
				server.addPendingCommand(input, server);
				commandHistory.add(input);
			}
			textFieldCommandLine.setText("");
		});
		textFieldCommandLine.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					String up = commandHistory.up(textFieldCommandLine.getText());
					if (up != null)
						textFieldCommandLine.setText(up);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					String down = commandHistory.down();
					if (down != null)
						textFieldCommandLine.setText(down);
				} else if (e.getKeyCode() == KeyEvent.VK_TAB)
					if (!tabCompletions.isEmpty()) {
						//update textAreaLog
						for (String line : tabCompletions) {
							if (line.equals("?"))
								continue;
							appendLine(line + ", ");
						}
						
						deleteLastChars(2);
						appendLine("\n");
						
						//update textFieldCommandLine
						if (completionsIterator.hasNext()) {
							internalChange = 2;
							String text = "";
							if (textFieldCommandLine.getText().contains(" "))
								text = textFieldCommandLine.getText().substring(0, textFieldCommandLine.getText().lastIndexOf(" ") + 1);
							textFieldCommandLine.setText(text + completionsIterator.next());
							textFieldCommandLine.select(typedLength, textFieldCommandLine.getText().length());
						}
					}
			}
			
		});
		textFieldCommandLine.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				processUpdate();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				processUpdate();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				processUpdate();
			}
			
			private void processUpdate() {
				if (internalChange > 0)
					internalChange--;
				else
					updateCompletions();
			}
		});
		
		add(scrollPaneConsole, BorderLayout.CENTER);
		add(textFieldCommandLine, BorderLayout.SOUTH);
		setBorder(new TitledBorder(new EtchedBorder(), "Console"));
		
		Thread thread = new Thread(() -> {
			String msg;
			while ((msg = GUIConsoleAppender.getNextLogEvent()) != null)
				appendLine(msg);
		});
		thread.setName("LogListener");
		thread.setDaemon(true);
		thread.start();
		
		updateCompletions();
	}
	
	private void updateCompletions() {
		tabCompletions = commandManager.getTabCompletions(server, textFieldCommandLine.getText());
		tabCompletions.remove("?");
		completionsIterator = Iterables.cycle(tabCompletions).iterator();
		typedLength = textFieldCommandLine.getText().length();
	}
	
	private void appendLine(String line) {
		
		try {
			serverGui.getLatch().await();
		} catch (@SuppressWarnings("unused") InterruptedException e) {
		} //Prevent logging until after constructor has ended.
		
		if (!SwingUtilities.isEventDispatchThread())
			SwingUtilities.invokeLater(() -> appendLine(line));
		else {
			Document document = textAreaConsole.getDocument();
			JScrollBar scrollBar = scrollPaneConsole.getVerticalScrollBar();
			boolean updateScrollBar = false;
			
			if (scrollPaneConsole.getViewport().getView() == textAreaConsole)
				updateScrollBar = (scrollBar.getValue() + scrollBar.getSize().getHeight() + (ServerGui.SERVER_GUI_FONT.getSize() * 4)) > scrollBar.getMaximum();
				
				try {
					document.insertString(document.getLength(), line, null);
				} catch (BadLocationException e) {
					LOG.warn("Couldn't add line to console view", e);
				}
				
				if (updateScrollBar)
					scrollBar.setValue(Integer.MAX_VALUE);
		}
	}
	
	private void deleteLastChars(int amount) {
		Document doc = textAreaConsole.getDocument();
		if (doc.getLength() > amount)
			try {
				doc.remove(doc.getLength() - amount, amount);
			} catch (BadLocationException e) {
				LOG.warn("Unexpected Error when deleting chars from the text area", e);
			}
	}
	
}
