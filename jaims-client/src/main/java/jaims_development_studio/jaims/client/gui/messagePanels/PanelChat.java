package jaims_development_studio.jaims.client.gui.messagePanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.UUID;

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

		initGUI();
	}

	private void initGUI() {

		img = guiMain.getChatBackground();
		setLayout(new BorderLayout());

		add(panelChatWindowTop, BorderLayout.PAGE_START);
		add(panelShowMessages, BorderLayout.CENTER);
		add(panelChatWindowBottom = new PanelChatWindowBottom(this, guiMain), BorderLayout.PAGE_END);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				img = scaleMaintainAspectRatio();

			}
		});
	}

	private Image scaleMaintainAspectRatio() {

		double ratio = (double) img.getWidth(this) / img.getHeight(this);
		return img.getScaledInstance((int) (getHeight() * ratio), getHeight(), Image.SCALE_SMOOTH);
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setClip(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40.5, 40.5));
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.drawImage(img, getWidth() / 2 - img.getWidth(this) / 2, getHeight() / 2 - img.getHeight(this) / 2, this);
	}
}
