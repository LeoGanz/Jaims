package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PreviewFrame extends JFrame{

	public PreviewFrame(ClientMain cm) {
		super();
		initGUI(cm);
	}
	
	private void initGUI(ClientMain cm) {
		cm.getJaimsFrame().setMaximumSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 450, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
		setSize(400,200);
		setUndecorated(true);
		setLocation(cm.getJaimsFrame().getX()+cm.getJaimsFrame().getWidth(), cm.getJaimsFrame().getY());

		setVisible(true);
		
		cm.getJaimsFrame().addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentMoved(ComponentEvent e) {
				setLocation(cm.getJaimsFrame().getX()+cm.getJaimsFrame().getWidth(), cm.getJaimsFrame().getY());
				
			}
		});
	}
	
}
