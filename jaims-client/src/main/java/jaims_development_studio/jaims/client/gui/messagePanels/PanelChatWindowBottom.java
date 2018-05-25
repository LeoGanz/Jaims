package jaims_development_studio.jaims.client.gui.messagePanels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.recordingFrame.RecordingWindow;

/**
 * This class represents a <code>JPanel</code> that is added to the
 * <code>BorderLayout.PAGE_END</code> of a <code>PanelChat</code> object and
 * gives the user the means to enter a text message, send it or record a voice
 * message.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 * 
 * @see PanelChat
 *
 */
public class PanelChatWindowBottom extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private PanelChat			panelChat;
	private GUIMain				guiMain;
	private JPanel				panelIcons;
	private JLabel				lblRecord, lblSend;
	private JTextArea			jta;
	private PanelTextField		panelTextField;
	private boolean				shiftPressed		= false;
	private Image				imgRecord, imgSend;
	private Timer				timerSend, timerRecord;
	private boolean				animationRunning	= false, sendIsShowing = false;

	/**
	 * Constructor of this class. Calls the method {@link #initGUI()} to start
	 * building the GUI.
	 * 
	 * @param panelChat
	 *            a reference to the panelChat instance this panel is added to
	 * @param guiMain
	 *            a reference to the GUIMain instance
	 */
	public PanelChatWindowBottom(PanelChat panelChat, GUIMain guiMain) {

		this.panelChat = panelChat;
		this.guiMain = guiMain;
		initGUI();
	}

	/**
	 * This method is responsible for building this panel's GUI by calling different
	 * methods: <br>
	 * <ul>
	 * <li>{@link #loadImages()}</li>
	 * <li>{@link #initParent()}</li>
	 * <li>{@link #initTextArea()}</li>
	 * <li>{@link #initTextField()}</li>
	 * <li>{@link #initPanelIcons()}</li>
	 * </ul>
	 * 
	 * It also adds a component listener to the frame that repaints the background
	 * on a resize event.
	 * 
	 */
	private void initGUI() {

		loadImages();
		initParent();
		initTextArea();
		initTextField();
		initPanelIcons();

		guiMain.getJaimsFrame().addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				revalidate();
				repaint();

			}
		});

	}

	/**
	 * Loads the images needed for the record and send Label.
	 * 
	 * @see {@link Toolkit#getImage(String)}
	 */
	private void loadImages() {

		imgRecord = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/mic.png"))
				.getScaledInstance(34, 34, Image.SCALE_SMOOTH);
		imgSend = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/Jaims_Send.png"))
				.getScaledInstance(34, 22, Image.SCALE_SMOOTH);
	}

	/**
	 * Initialises the parent <code>JPanel</code>
	 */
	private void initParent() {

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(new EmptyBorder(0, 8, 3, 8));
		setBackground(new Color(0, 0, 0, 0));
		setOpaque(false);
	}

	/**
	 * Creates a <code>JTextArea</code> that allows the user to enter his text
	 * message. There are also some things that have to be mentioned in order to
	 * know how the text input works: <br>
	 * <ul>
	 * <li>Holding 'shift' while pressing 'enter' adds a new line</li>
	 * <li>Just pressing 'enter' sends the message</li>
	 * <li>Upon typing the first letter the label showing the images switches to the
	 * send button</li>
	 * <li>A voice message can only be sent when there is now text in the text area
	 * -> Upon deleting the whole text the label showing the images switches back to
	 * the recording image</li>
	 * </ul>
	 * 
	 * @see #showRecordIcon()
	 * @see #showSendIcon()
	 * @see #sendSendable(String)
	 */
	private void initTextArea() {

		jta = new JTextArea("");
		jta.setFont(new Font(guiMain.getSettings().getOwnFontName(), Font.PLAIN, 12));
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		jta.setBackground(Color.WHITE);
		jta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (jta.getText().equals(""))
					showRecordIcon();
				else {
					if (sendIsShowing == false) {
						showSendIcon();
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (shiftPressed) {
						jta.setText(jta.getText() + "\n");
					} else {
						if (jta.getText().equals("") || jta.getText().equals("[\\s]*") || jta.getText().equals("[\\n]*")
								|| jta.getText().trim().isEmpty()) {
						} else {

							sendSendable(jta.getText().trim());
							jta.setText("");
							showRecordIcon();

							if (panelChat.getSimpleContact().chatExists() == false) {
								panelChat.getSimpleContact().setChatExists(true);
								updateDatabase();
							}
						}
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_SHIFT)
					shiftPressed = false;

			}

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_SHIFT)
					shiftPressed = true;

			}
		});
	}

	/**
	 * Creates a <code>PanelTextField</code>.
	 * 
	 * @see PanelTextField
	 */
	private void initTextField() {

		panelTextField = new PanelTextField(jta, panelChat);
		add(panelTextField);
	}

	/**
	 * Creates a <code>JPanel</code> that holds the <code>JLabels</code> that show
	 * the record and send images.
	 * 
	 * @see #initLabelRecord()
	 * @see #initLabelSend()
	 */
	private void initPanelIcons() {

		panelIcons = new JPanel();
		panelIcons.setBackground(new Color(0, 0, 0, 0));
		panelIcons.setOpaque(false);
		panelIcons.setPreferredSize(new Dimension(35, 35));
		panelIcons.setMinimumSize(new Dimension(35, 35));
		panelIcons.setLayout(null);
		panelIcons.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				lblRecord.setBounds(lblRecord.getX(), getHeight() / 2 - 17, 34, 34);
				lblSend.setBounds(lblSend.getX(), getHeight() / 2 - 17, 34, 34);
				panelIcons.repaint();
				revalidate();
			}
		});

		initLabelRecord();
		initLabelSend();

		add(Box.createHorizontalGlue());
		add(panelIcons);
		add(Box.createRigidArea(new Dimension(0, 2)));
	}

	/**
	 * Creates a <code>JLabel</code> that shows the record image and creates a new
	 * {@link RecordingWindow} upon clicking the label.
	 */
	private void initLabelRecord() {

		lblRecord = new JLabel(new ImageIcon(imgRecord));
		lblRecord.setBackground(new Color(0, 0, 0, 0));
		lblRecord.setOpaque(false);
		lblRecord.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblRecord.setPreferredSize(new Dimension(34, 34));
		lblRecord.setMaximumSize(new Dimension(34, 34));
		lblRecord.setBounds(0, 0, 34, 34);
		lblRecord.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				new RecordingWindow(guiMain, panelChat);

			}
		});
		panelIcons.add(lblRecord);
	}

	/**
	 * Creates a new <code>JLabel</code> that show the send image and sends the text
	 * message upon clicking the label by calling {@link #sendSendable(String)}
	 * 
	 * @see #showRecordIcon()
	 */
	private void initLabelSend() {

		lblSend = new JLabel(new ImageIcon(imgSend));
		lblSend.setBackground(new Color(0, 0, 0, 0));
		lblSend.setOpaque(false);
		lblSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblSend.setPreferredSize(new Dimension(34, 34));
		lblSend.setMaximumSize(new Dimension(34, 34));
		lblSend.setBounds(40, 0, 34, 34);
		lblSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				if (jta.getText().equals("") || jta.getText().equals("[\\s]*") || jta.getText().equals("[\\n]*")
						|| jta.getText().trim().isEmpty()) {
				} else {

					sendSendable(jta.getText());
					jta.setText("");
					showRecordIcon();
				}

			}
		});
		panelIcons.add(lblSend);
	}

	/**
	 * Starts an animation that slides from the recordLabel to the sendLabel.
	 */
	private void showSendIcon() {

		if (animationRunning == false) {
			animationRunning = true;
			timerSend = new Timer(5, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					int xRecord = lblRecord.getX() - 2;
					int xSend = lblSend.getX() - 2;

					if (xRecord >= -38)
						lblRecord.setBounds(xRecord, lblRecord.getY(), 34, 34);

					if (xSend >= 2)
						lblSend.setBounds(xSend, lblSend.getY(), 34, 34);

					if (xSend == 0) {
						((Timer) timerSend).stop();
						panelIcons.revalidate();
						panelIcons.repaint();
						revalidate();
						repaint();
						animationRunning = false;
						sendIsShowing = true;
					}
				}
			});
			timerSend.start();
		}
	}

	/**
	 * Starts an animation that slides from the sendLabel to the recordLabel.
	 */
	private void showRecordIcon() {

		if (animationRunning == false) {
			animationRunning = true;
			timerRecord = new Timer(5, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					int xRecord = lblRecord.getX() + 2;
					int xSend = lblSend.getX() + 2;

					if (xSend <= 40)
						lblSend.setBounds(xSend, lblSend.getY(), 34, 34);

					if (xRecord <= 38)
						lblRecord.setBounds(xRecord, lblRecord.getY(), 34, 34);

					if (xRecord == 0) {
						((Timer) e.getSource()).stop();
						panelIcons.revalidate();
						panelIcons.repaint();
						revalidate();
						repaint();
						animationRunning = false;
						sendIsShowing = false;
					}
				}
			});
			timerRecord.start();
		}
	}

	private void updateDatabase() {

		guiMain.updateHasChat(true, panelChat.getSimpleContact().getContactID());
	}

	/**
	 * Builds a <code>TextMessage</code> with a given string, sends it and adds it
	 * to the chat.
	 * 
	 * @param message
	 *            the string to be sent
	 * 
	 * @see TextMessage
	 * @see GUIMain#sendSendable(jaims_development_studio.jaims.api.sendables.Sendable)
	 * @see PanelChat#addNewUserMessage(String, TextMessage)
	 */
	private void sendSendable(String message) {

		TextMessage tm = new TextMessage(guiMain.getUserUUID(), panelChat.getContactID(), message);
		tm.setUuid(UUID.randomUUID());
		tm.setTimestampSent();
		SendableMessage sm = new SendableMessage(tm);
		guiMain.sendSendable(sm);
		panelChat.addNewUserMessage(message, tm);
		guiMain.setUserOnTop(panelChat.getPanelUUID());
	}

}
