package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PreviewFrame extends JFrame{

	boolean exists = false;
	public PreviewMessage p1, p2;
	JPanel contentPane;
	JPanel panelP1, panelP2;
	
	public PreviewFrame(ClientMain cm) {
		if (exists == false) {
			exists = true;			
			initGUI(cm);
		}
	}
	
	private void initGUI(ClientMain cm) {
			cm.getJaimsFrame().setMaximumSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 550, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
			setSize(500,cm.getJaimsFrame().getHeight());
			setUndecorated(true);
			setLocation(cm.getJaimsFrame().getX()+cm.getJaimsFrame().getWidth(), cm.getJaimsFrame().getY());
			getContentPane().setLayout(new BorderLayout());
			
			
			contentPane = new JPanel();
			contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
			contentPane.setBorder(new LineBorder(Color.BLACK));
			contentPane.add(Box.createRigidArea(new Dimension(0, 3)));
			{
				panelP1 = new JPanel();
				panelP1.setLayout(new BoxLayout(panelP1, BoxLayout.LINE_AXIS));
				p1 = new PreviewMessage("Ich bin eine User-Nachricht", true, this);
				panelP1.add(Box.createHorizontalGlue());				
				panelP1.add(p1);
				panelP1.add(Box.createRigidArea(new Dimension(3, 0)));
				contentPane.add(panelP1);
				contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
				
				panelP2 = new JPanel();
				panelP2.setLayout(new BoxLayout(panelP2, BoxLayout.LINE_AXIS));
				p2 = new PreviewMessage("Ich bin eine Antwort-Nachricht", false, this);
				panelP2.add(Box.createRigidArea(new Dimension(3, 0)));
				panelP2.add(p2);
				panelP2.add(Box.createHorizontalGlue());
				contentPane.add(panelP2);
				contentPane.add(Box.createVerticalGlue());
			}
			getContentPane().add(contentPane, BorderLayout.CENTER);

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
	
	public PreviewMessage getP1() {
		return p1;
	}
	
	public PreviewMessage getP2() {
		return p2;
	}
	
	public void reCon() {
		panelP1.revalidate();
		panelP2.revalidate();
		contentPane.revalidate();
		contentPane.repaint();
	}
	
}
