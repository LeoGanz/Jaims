package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PreviewFrame extends JFrame{

	boolean exists = false;
	
	public PreviewFrame(ClientMain cm) {
		super();
		initGUI(cm);
	}
	
	private void initGUI(ClientMain cm) {
		if (exists == false) {
			exists = true;
			cm.getJaimsFrame().setMaximumSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 550, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
			setSize(500,cm.getJaimsFrame().getHeight());
			setUndecorated(true);
			setLocation(cm.getJaimsFrame().getX()+cm.getJaimsFrame().getWidth(), cm.getJaimsFrame().getY());
			getContentPane().add(new PreviewMessage("Ich bin eine Test Nachricht"));

			setVisible(true);
			
			cm.getJaimsFrame().addComponentListener(new ComponentAdapter() {
				
				@Override
				public void componentMoved(ComponentEvent e) {
					setLocation(cm.getJaimsFrame().getX()+cm.getJaimsFrame().getWidth(), cm.getJaimsFrame().getY());
					
				}
				
			});
			
			cm.getJaimsFrame().addWindowListener(new WindowAdapter() {
				
				@Override
				public void windowClosed(WindowEvent e) {
					exists = false;
					
				}

			});
		}
	}
	
}
