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

import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.VoiceMessage;

public class PanelChat extends ParentPanel {

	private GUIMain					guiMain;
	private UUID					contactID;
	private PanelChatWindowTop		panelChatWindowTop;
	private PanelChatWindowBottom	panelChatWindowBottom;
	private PanelShowMessages		panelShowMessages;
	private Image					img;
	private JScrollPane				jsp;

	public PanelChat(GUIMain guiMain, UUID contactID, PanelChatWindowTop panelChatWindowTop,
			PanelShowMessages panelShowMessages) {

		this.guiMain = guiMain;
		this.contactID = contactID;
		this.panelChatWindowTop = panelChatWindowTop;
		this.panelShowMessages = panelShowMessages;

		super.setPanelUUID(contactID);

		initGUI();
	}

	private void initGUI() {

		img = guiMain.getChatBackground();
		setLayout(new BorderLayout());

		add(panelChatWindowTop, BorderLayout.PAGE_START);
		add(panelChatWindowBottom = new PanelChatWindowBottom(this, guiMain), BorderLayout.PAGE_END);
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

	public void addNewUserMessage(String message) {

		panelShowMessages.addNewUserMessage(message);
		panelShowMessages.revalidate();
		jsp.getVerticalScrollBar()
				.setValue(jsp.getVerticalScrollBar().getMaximum() - jsp.getVerticalScrollBar().getVisibleAmount());
		repaint();
		jaims_development_studio.jaims.api.message.TextMessage m = new TextMessage(guiMain.getUserUUID(), contactID,
				message);
		guiMain.saveTextMessage(m);
	}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addNewVoiceMessage() {

	}

	public void setScrollBarValueToMax() {

		jsp.revalidate();
		jsp.repaint();
		jsp.getVerticalScrollBar()
				.setValue(jsp.getVerticalScrollBar().getMaximum() - jsp.getVerticalScrollBar().getVisibleAmount());
		jsp.revalidate();
		jsp.repaint();

		repaint();
	}

	private Image scaleMaintainAspectRatio() {

		if (guiMain.getSettings().getImageResizeHint() == Settings.RESIZE_ALONG_WIDTH) {
			double ratio = (double) img.getHeight(this) / img.getWidth(this);
			return img.getScaledInstance(getWidth(), (int) (ratio * getWidth()), Image.SCALE_SMOOTH);
		} else if (guiMain.getSettings().getImageResizeHint() == Settings.RESIZE_ALONG_HEIGHT) {
			double ratio = (double) img.getWidth(this) / img.getHeight(this);
			return img.getScaledInstance((int) (ratio * getHeight()), getHeight(), Image.SCALE_SMOOTH);
		} else {
			if (getWidth() > getHeight()) {
				double ratio = (double) img.getWidth(this) / img.getHeight(this);
				return img.getScaledInstance((int) (ratio * getHeight()), getHeight(), Image.SCALE_SMOOTH);
			} else {
				double ratio = (double) img.getHeight(this) / img.getWidth(this);
				return img.getScaledInstance(getWidth(), (int) (ratio * getWidth()), Image.SCALE_SMOOTH);
			}
		}
	}

	public UUID getContactID() {

		return contactID;
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
