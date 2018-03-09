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
import java.util.UUID;

import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;

public class PanelChat extends ParentPanel {

	private GUIMain					guiMain;
	private UUID					contactID;
	private PanelChatWindowTop		panelChatWindowTop;
	private PanelChatWindowBottom	panelChatWindowBottom;
	private PanelShowMessages		panelShowMessages;
	private Image					img;

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
		JScrollPane jsp = new JScrollPane(panelShowMessages, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setBackground(new Color(0, 0, 0, 0));
		jsp.setOpaque(false);
		jsp.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
		jsp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {

				jsp.getViewport().repaint();

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
		jaims_development_studio.jaims.api.message.TextMessage m = new TextMessage(guiMain.getUserUUID(), contactID,
				message);
		guiMain.saveTextMessage(m);
		SendableMessage sm = new SendableMessage(m);
		// guiMain.sendSendable(sm);
	}

	private Image scaleMaintainAspectRatio() {

		double ratio = (double) img.getWidth(this) / img.getHeight(this);
		return img.getScaledInstance((int) (getHeight() * ratio), getHeight(), Image.SCALE_SMOOTH);
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

		g2.drawImage(img, getWidth() / 2 - img.getWidth(this) / 2, getHeight() / 2 - img.getHeight(this) / 2, this);
	}
}
