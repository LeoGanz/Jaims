package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelSettings extends JScrollPane{
	
	JPanel panel,panelLanguage;
	JComboBox<String> jcb;
	JLabel lblLanguage;
	String[] languages = {"Deutsch", "English"};
	
	public PanelSettings(ClientMain cm) {
		initGUI(cm);
	}
	
	private void initGUI(ClientMain cm) {
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				panelLanguage.setPreferredSize(panelLanguage.getPreferredSize());
				panelLanguage.setMaximumSize(panelLanguage.getPreferredSize());
				panelLanguage.revalidate();
				panelLanguage.repaint();
				
				revalidate();
				repaint();
				cm.getJaimsFrame().getContentPane().repaint();
			}
		});
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		
		panelLanguage = new JPanel();
		panelLanguage.setLayout(new BoxLayout(panelLanguage, BoxLayout.PAGE_AXIS));
		panelLanguage.setPreferredSize(new Dimension(cm.getJaimsFrame().getWidth()-280, 60));
		panelLanguage.setMaximumSize(panelLanguage.getPreferredSize());
		panelLanguage.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Sprache"));
		{
			panelLanguage.add(Box.createVerticalGlue());
			
			lblLanguage = new JLabel("Bitte wählen Sie Ihre Sprache aus:", JLabel.LEFT);
			lblLanguage.setAlignmentX(LEFT_ALIGNMENT);
			panelLanguage.add(lblLanguage);
			panelLanguage.add(Box.createRigidArea(new Dimension(0, 3)));
			
			jcb = new JComboBox<>(languages);
			jcb.setSelectedIndex(0);
			jcb.setMaximumSize(new Dimension(250, 30));
			jcb.setAlignmentX(LEFT_ALIGNMENT);
			panelLanguage.add(jcb);
			
			panelLanguage.add(Box.createVerticalGlue());
		}
		panel.add(panelLanguage);
		
		setViewportView(panel);
		
	}

}
