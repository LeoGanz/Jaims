package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.logic.MessageObject;

public class MessagePanel extends JPanel {
	
	MessageObject mo;
	
	
	public MessagePanel() {
		
	};

	
	public void setMessageObject (MessageObject mo) {
		this.mo = mo;
	}
	
	public MessageObject getMessageObject() {
		return mo;
	}

}
