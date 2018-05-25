package jaims_development_studio.jaims.client.gui.messagePanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.VoiceMessage;
import jaims_development_studio.jaims.client.gui.dfe.PanelStoreDFEs;
import jaims_development_studio.jaims.client.logic.SimpleContact;

/**
 * This class represents a JPanel that shows a chat with a user, meaning it
 * holds the Panel that shows the messages, a panel that holds a text field for
 * typing new message and a panel that is added to the top and mainly shows the
 * user's profile picture and name.
 * 
 * @see PanelChatWindowTop
 * @see PanelShowMessages
 * @see PanelChatWindowBottom
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class PanelChat extends ParentPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static final Logger	LOG					= LoggerFactory.getLogger(PanelChat.class);

	private GUIMain				guiMain;
	private UUID				contactID;
	private PanelChatWindowTop	panelChatWindowTop;
	private PanelShowMessages	panelShowMessages;
	private Image				img;
	private JScrollPane			jsp, jspDFE;

	/**
	 * Constructor of this class. Initialises the fields and starts building the GUI
	 * by calling {@link #initGUI()}.
	 * 
	 * @param guiMain
	 *            an instance of the GUIMain class
	 * @param contactID
	 *            the contact's UUID with whom this PanelChat is associated
	 * @param panelChatWindowTop
	 *            an instance of a ChatWindowTop class
	 * @param panelShowMessages
	 *            an instance of a PanelShowMessages class
	 */
	public PanelChat(GUIMain guiMain, UUID contactID, PanelChatWindowTop panelChatWindowTop,
			PanelShowMessages panelShowMessages) {

		this.guiMain = guiMain;
		this.contactID = contactID;
		this.panelChatWindowTop = panelChatWindowTop;
		this.panelShowMessages = panelShowMessages;

		super.setPanelUUID(contactID);

		initGUI();
	}

	/**
	 * This method is responsible for building the GUI of the PanelChat. This
	 * happens by initialising the background image, adding both the panels for the
	 * top and the bottom and the <code>PanelShowMessages</code> on a
	 * <code>JScrollPane</code> which scrolls only vertically. It also adds a
	 * ComponentListener to the <code>ParentPanel</code> that resizes the background
	 * image on a resize event.
	 * 
	 * @see PanelShowMessages
	 * @see JScrollPane
	 * @see ParentPanel
	 */
	private void initGUI() {

		img = guiMain.getChatBackground();
		setLayout(new BorderLayout());

		add(panelChatWindowTop, BorderLayout.PAGE_START);
		add(new PanelChatWindowBottom(this, guiMain), BorderLayout.PAGE_END);
		jsp = new JScrollPane(panelShowMessages, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setBackground(new Color(0, 0, 0, 0));
		jsp.getViewport().setBackground(new Color(0, 0, 0, 0));
		jsp.setOpaque(false);
		jsp.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
		jsp.getVerticalScrollBar().setUnitIncrement(10);
		jsp.getVerticalScrollBar()
				.setValue(jsp.getVerticalScrollBar().getMaximum() - jsp.getVerticalScrollBar().getVisibleAmount());
		jsp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {

				// panelShowMessages.repaint();
				jsp.getViewport().repaint();
				jsp.repaint();

			}
		});
		add(jsp, BorderLayout.CENTER);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				img = scaleMaintainAspectRatio();
				updateUI();
				revalidate();
				repaint();

			}
		});
		Toolkit.getDefaultToolkit().setDynamicLayout(false);

	}

	/**
	 * Gets a text message the user has sent, adds it to the
	 * <code>PanelShowMessages</code> and saves it to the database.
	 * 
	 * @see PanelShowMessages#addNewUserMessage(String)
	 * @see GUIMain#saveTextMessage(TextMessage)
	 * 
	 * @param message
	 *            The message to be added in form of a string
	 * @param m
	 *            the TextMessage to be saved
	 * 
	 * @see TextMessage
	 */
	public void addNewUserMessage(String message, TextMessage m) {

		panelShowMessages.addNewUserMessage(message);
		panelShowMessages.revalidate();
		jsp.getVerticalScrollBar()
				.setValue(jsp.getVerticalScrollBar().getMaximum() - jsp.getVerticalScrollBar().getVisibleAmount());
		repaint();
		guiMain.saveTextMessage(m);
	}

	/**
	 * This methods adds a new voice message to the <code>PanelShowMessage</code> by
	 * initialising a <code>VoiceMessage</code>, adding it and finally saving it.
	 * 
	 * @see VoiceMessage
	 * @see PanelShowMessages#addNewUserVoiceMessage(VoiceMessage)
	 * @see GUIMain#saveVoiceMessage(jaims_development_studio.jaims.api.message.VoiceMessage,
	 *      String)
	 * 
	 * @param pathToFile
	 */
	public void addNewUserVoiceMessage(String pathToFile) {

		VoiceMessage vm = new VoiceMessage(UUID.randomUUID(), pathToFile, true, guiMain);
		panelShowMessages.addNewUserVoiceMessage(vm);
		panelShowMessages.revalidate();
		jsp.getVerticalScrollBar()
				.setValue(jsp.getVerticalScrollBar().getMaximum() - jsp.getVerticalScrollBar().getVisibleAmount());
		repaint();

		try {
			jaims_development_studio.jaims.api.message.VoiceMessage v = new jaims_development_studio.jaims.api.message.VoiceMessage(
					guiMain.getUserUUID(), contactID, IOUtils.toByteArray(new FileInputStream(new File(pathToFile))));
			guiMain.saveVoiceMessage(v, pathToFile);
		} catch (IOException e) {
			LOG.error("Failed to load audio file!", e);
		}
	}

	/**
	 * This method sets the value of the <code>JScrollBar</code> of the
	 * <code>JScrollPane</code> that holds the <code>PanelShowMessages</code> to
	 * maximum.
	 */
	public void setScrollBarValueToMax() {

		jsp.revalidate();
		jsp.repaint();
		jsp.getVerticalScrollBar()
				.setValue(jsp.getVerticalScrollBar().getMaximum() - jsp.getVerticalScrollBar().getVisibleAmount());
		jsp.revalidate();
		jsp.repaint();

		repaint();
	}

	/**
	 * Scales the background image according to the resize hint set in the settings.
	 * 
	 * @return the resized image
	 * 
	 * @see GUIMain#getSettings()
	 * @see Settings
	 * @see Settings#getImageResizeHint()
	 */
	private Image scaleMaintainAspectRatio() {

		if (guiMain.getSettings().getImageResizeHint() == Settings.RESIZE_ALONG_WIDTH) {
			double ratio = (double) img.getHeight(this) / img.getWidth(this);
			return img.getScaledInstance(getWidth(), (int) (ratio * getWidth()), Image.SCALE_SMOOTH);
		} else if (guiMain.getSettings().getImageResizeHint() == Settings.RESIZE_ALONG_HEIGHT) {
			double ratio = (double) img.getWidth(this) / img.getHeight(this);
			return img.getScaledInstance((int) (ratio * getHeight()), getHeight(), Image.SCALE_SMOOTH);
		} else {
			if (getWidth() > getHeight()) {
				double ratio = (double) img.getHeight(this) / img.getWidth(this);
				return img.getScaledInstance(getWidth(), (int) (ratio * getWidth()), Image.SCALE_SMOOTH);

			} else {
				double ratio = (double) img.getWidth(this) / img.getHeight(this);
				return img.getScaledInstance((int) (ratio * getHeight()), getHeight(), Image.SCALE_SMOOTH);
			}
		}
	}

	public void addDFEPanel() {

		PanelStoreDFEs psDFE = new PanelStoreDFEs(contactID, guiMain);
		jspDFE = new JScrollPane(psDFE, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(jspDFE, BorderLayout.LINE_END);
		revalidate();
		repaint();
	}

	/**
	 * 
	 * @return the UUID of the contact this PanelChat is associated with
	 */
	public UUID getContactID() {

		return contactID;
	}

	public SimpleContact getSimpleContact() {

		return guiMain.getSimpleContact(contactID);
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(guiMain.getSettings().getChatBackground()));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.drawImage(img, getWidth() / 2 - img.getWidth(this) / 2, getHeight() / 2 - img.getHeight(this) / 2, null);
	}

}
