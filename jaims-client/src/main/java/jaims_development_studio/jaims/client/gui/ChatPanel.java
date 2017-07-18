package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ChatPanel extends JPanel {
	
	PanelChatWindow chatWindow;
	String nickname;
	
	public ChatPanel(MainFrame mf) {
		initGUI(mf);
	}
	
	private void initGUI(MainFrame mf) {
		setMinimumSize(new Dimension(200, 60));
		setMaximumSize(new Dimension(400, 60));
		setPreferredSize(new Dimension(mf.getFrame().getWidth()/3, 60));
		setBorder(new LineBorder(Color.DARK_GRAY));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(Box.createRigidArea(new Dimension(10, 0)));
	}
	
	public void setChatWindow(PanelChatWindow chatWindow) {
		this.chatWindow = chatWindow;
	}
	
	public PanelChatWindow getChatWindow() {
		return chatWindow;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getNickname() {
		return nickname;
	}
}
