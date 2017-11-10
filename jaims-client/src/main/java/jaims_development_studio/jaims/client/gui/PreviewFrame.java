package jaims_development_studio.jaims.client.gui;

import javax.swing.JFrame;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PreviewFrame extends JFrame{

	public PreviewFrame(ClientMain cm) {
		super();
		initGUI(cm);
	}
	
	private void initGUI(ClientMain cm) {
		setSize(200,200);
		setUndecorated(true);
		setLocation(cm.getJaimsFrame().getX()+cm.getJaimsFrame().getWidth(), cm.getJaimsFrame().getY());
		setVisible(true);
	}
}
