package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import jaims_development_studio.jaims.client.logic.ClientMain;
import jaims_development_studio.jaims.client.settings.Settings;

public class PanelSettings extends JScrollPane{
	
	JPanel panel,panelLanguage, panelOwnMessages, panelOwnFont, panelContactMessages, panelContactFont, panelLinkDesigns;
	JComboBox<String> jcb, jcbNames, jcbStyle, jcbSize, jcbContactNames, jcbContactStyle, jcbContactSize;
	JLabel lblLanguage, lblOwnColor, lblOwnColorBorder, lblOwnFont, lblContactColor, lblContactColorBorder, lblContactFont;
	JButton btColorChooser, btColorChooserBorder, btOwnFontColor, btContactColor, btContactColorBorder, btContactColorFont;
	JColorChooser jcc;
	JCheckBox linkDesigns;
	String[] languages = {"Deutsch", "English"};
	String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	String[] fontStyle = {"Plain" , "<html><b>Bold", "<html><i>Italic"};
	String[] fontSize = {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
	PreviewFrame pf;
	
	boolean link = true;
	
	public PanelSettings(ClientMain cm, PreviewFrame pf) {
		this.pf = pf;
		initGUI(cm);
	}
	
	private void initGUI(ClientMain cm) {
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		
		panelLanguage = new JPanel();
		panelLanguage.setLayout(new BoxLayout(panelLanguage, BoxLayout.PAGE_AXIS));
		//panelLanguage.setPreferredSize(new Dimension(cm.getJaimsFrame().getWidth()-280, 60));
		//panelLanguage.setMaximumSize(panelLanguage.getPreferredSize());
		panelLanguage.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(Color.BLACK), "Sprache"), new EmptyBorder(0, 4, 0, 4)));
		((TitledBorder)((CompoundBorder) panelLanguage.getBorder()).getOutsideBorder()).setTitleFont(new Font("Serif", Font.BOLD, 16));
		{
			panelLanguage.add(Box.createHorizontalGlue());
			
			lblLanguage = new JLabel("Bitte wählen Sie Ihre Sprache aus:", JLabel.LEFT);
			lblLanguage.setAlignmentX(LEFT_ALIGNMENT);
			panelLanguage.add(lblLanguage);
			panelLanguage.add(Box.createRigidArea(new Dimension(0, 3)));
			
			jcb = new JComboBox<>(languages);
			jcb.setSelectedIndex(0);
			jcb.setMaximumSize(new Dimension(250, 30));
			jcb.setAlignmentX(LEFT_ALIGNMENT);
			panelLanguage.add(jcb);

		}
		panel.add(panelLanguage);
		panel.add(Box.createRigidArea(new Dimension(0, 6)));
		
		panelOwnMessages = new JPanel();
		panelOwnMessages.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(Color.black), "Nachrichtenformat: Eigene Nachrichten"), new EmptyBorder(0, 4, 0, 4)));
		((TitledBorder)((CompoundBorder) panelOwnMessages.getBorder()).getOutsideBorder()).setTitleFont(new Font("Serif", Font.BOLD, 16));
		panelOwnMessages.setLayout(new BoxLayout(panelOwnMessages, BoxLayout.PAGE_AXIS));
		{
			
			lblOwnColor = new JLabel("Wählen Sie eine Hintergrundfarbe aus:", JLabel.LEFT);
			lblOwnColor.setAlignmentX(LEFT_ALIGNMENT);
			panelOwnMessages.add(lblOwnColor);
			panelOwnMessages.add(Box.createRigidArea(new Dimension(0, 2)));
			
			btColorChooser = new JButton("Farbe wählen");
			btColorChooser.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btColorChooser.setBorderPainted(false);
			btColorChooser.setForeground(Color.BLUE);
			btColorChooser.setFont(new Font("Serif", Font.ITALIC, 13));
			btColorChooser.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					jcc = new JColorChooser();
					jcc.setVisible(true);
					Settings.colorOwnMessages =JColorChooser.showDialog(cm.getJaimsFrame(), "Hintergrundfarbe wählen", new Color(191,255,14));
					pf.getP1().repaint();
					
					if(link)
						Settings.colorContactMessages = Settings.colorOwnMessages;
				}
			});
			panelOwnMessages.add(btColorChooser);
			panelOwnMessages.add(Box.createRigidArea(new Dimension(0, 10)));
			
			lblOwnColorBorder = new JLabel("Wählen Sie eine Randfarbe:", JLabel.LEFT);
			lblOwnColorBorder.setAlignmentX(LEFT_ALIGNMENT);
			panelOwnMessages.add(lblOwnColorBorder);
			panelOwnMessages.add(Box.createRigidArea(new Dimension(0, 2)));
			
			btColorChooserBorder = new JButton("Farbe wählen");
			btColorChooserBorder.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btColorChooserBorder.setBorderPainted(false);
			btColorChooserBorder.setForeground(Color.BLUE);
			btColorChooserBorder.setFont(new Font("Serif", Font.ITALIC, 13));
			btColorChooserBorder.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					jcc = new JColorChooser();
					jcc.setVisible(true);
					Settings.colorOwnMessageBorder = JColorChooser.showDialog(cm.getJaimsFrame(), "Randfarbe wählen", Color.BLACK);
					pf.getP1().repaint();
					
					if(link)
						Settings.colorContactMessageBorder = Settings.colorOwnMessageBorder;
				}
			});
			panelOwnMessages.add(btColorChooserBorder);			
			panelOwnMessages.add(Box.createRigidArea(new Dimension(0, 10)));
			
			lblOwnFont = new JLabel("Wählen Sie die Schriftart, Schriftgröße und Schriftfarbe");
			lblOwnFont.setAlignmentX(LEFT_ALIGNMENT);
			panelOwnMessages.add(lblOwnFont);
			
			panelOwnFont = new JPanel();
			panelOwnFont.setLayout(new BoxLayout(panelOwnFont, BoxLayout.LINE_AXIS));
			panelOwnFont.setAlignmentX(LEFT_ALIGNMENT);
			{
				jcbNames = new JComboBox<>(fontNames);
				jcbStyle = new JComboBox<>(fontStyle);
				jcbSize = new JComboBox<>(fontSize);
				
				
				
				jcbNames.setSelectedItem("Serif");
				jcbNames.setMaximumSize(new Dimension(80, 25));
				jcbNames.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.ownFontName = fontNames[jcbNames.getSelectedIndex()];
						pf.getP1().repaint();
						
						if(link)
							jcbContactNames.setSelectedIndex(jcbNames.getSelectedIndex());
					}
				});
				panelOwnFont.add(jcbNames);
				
				jcbStyle.setSelectedIndex(0);
				jcbStyle.setMaximumSize(new Dimension(80, 25));
				jcbStyle.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.ownFontStyle = jcbStyle.getSelectedIndex();
						pf.getP1().repaint();
						
						if(link)
							jcbContactStyle.setSelectedIndex(jcbStyle.getSelectedIndex());
					}
				});
				panelOwnFont.add(Box.createRigidArea(new Dimension(8, 0)));
				panelOwnFont.add(jcbStyle);
				
				jcbSize.setMaximumSize(new Dimension(80, 25));
				jcbSize.setSelectedIndex(5);
				jcbSize.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.ownFontSize = Integer.parseInt(fontSize[jcbSize.getSelectedIndex()]);
						pf.getP1().repaint();
						
						if (link)
							jcbContactSize.setSelectedIndex(jcbSize.getSelectedIndex());
					}
				});
				panelOwnFont.add(Box.createRigidArea(new Dimension(8, 0)));
				panelOwnFont.add(jcbSize);
				panelOwnFont.add(Box.createHorizontalGlue());
			}
			panelOwnMessages.add(Box.createRigidArea(new Dimension(0, 2)));
			panelOwnMessages.add(panelOwnFont);
			
			btOwnFontColor = new JButton("Farbe wählen");
			btOwnFontColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btOwnFontColor.setBorderPainted(false);
			btOwnFontColor.setForeground(Color.BLUE);
			btOwnFontColor.setFont(new Font("Serif", Font.ITALIC, 13));
			btOwnFontColor.setAlignmentX(LEFT_ALIGNMENT);
			btOwnFontColor.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					jcc = new JColorChooser();
					jcc.setVisible(true);
					Settings.colorOwnMessageFont = JColorChooser.showDialog(cm.getJaimsFrame(), "Schriftfarbe wählen", Color.BLACK);
					pf.getP1().repaint();
					
					if (link)
						Settings.colorContactMessageFont = Settings.colorOwnMessageFont;
					
				}
			});
			panelOwnMessages.add(Box.createRigidArea(new Dimension(0, 2)));
			panelOwnMessages.add(btOwnFontColor);
			panelOwnMessages.add(Box.createRigidArea(new Dimension(0, 6)));
			
			panelLinkDesigns = new JPanel();
			panelLinkDesigns.setLayout(new BoxLayout(panelLinkDesigns, BoxLayout.LINE_AXIS));
			panelLinkDesigns.setAlignmentX(LEFT_ALIGNMENT);
			{
				linkDesigns = new JCheckBox("Design-Einstellungen auch auf eingehende Nachrichten anwenden");
				linkDesigns.setSelected(true);
				linkDesigns.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (link == true) {
							link = false;
							linkDesigns.setSelected(false);
							linkDesigns.repaint();
						}else {
							link = true;
							Settings.colorContactMessages = Settings.colorOwnMessages;
							Settings.colorContactMessageBorder = Settings.colorOwnMessageBorder;
							jcbContactNames.setSelectedIndex(jcbNames.getSelectedIndex());
							jcbContactStyle.setSelectedIndex(jcbStyle.getSelectedIndex());
							jcbContactSize.setSelectedIndex(jcbSize.getSelectedIndex());
							Settings.colorContactMessageFont = Settings.colorOwnMessageFont;
							
							pf.getP2().repaint();
							linkDesigns.setSelected(true);
							linkDesigns.repaint();
						}
					}
				});
				panelLinkDesigns.add(Box.createHorizontalGlue());
				panelLinkDesigns.add(linkDesigns);
				panelLinkDesigns.add(Box.createRigidArea(new Dimension(0, 3)));
				
			}
			panelOwnMessages.add(panelLinkDesigns);
		}
		//panelOwnMessages.setPreferredSize(new Dimension(cm.getJaimsFrame().getWidth()-280, 230));
		//panelOwnMessages.setMaximumSize(panelOwnMessages.getPreferredSize());
		panel.add(panelOwnMessages);
		panel.add(Box.createRigidArea(new Dimension(0, 6)));
		
		
		panelContactMessages = new JPanel();
		panelContactMessages.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(Color.black), "Nachrichtenformat: Eingehende Nachrichten"), new EmptyBorder(0, 4, 0, 4)));
		((TitledBorder)((CompoundBorder) panelContactMessages.getBorder()).getOutsideBorder()).setTitleFont(new Font("Serif", Font.BOLD, 16));
		panelContactMessages.setLayout(new BoxLayout(panelContactMessages, BoxLayout.PAGE_AXIS));
		{
			lblContactColor = new JLabel("Wählen Sie eine Hintergrundfarbe aus:", JLabel.LEFT);
			lblContactColor.setAlignmentX(LEFT_ALIGNMENT);
			panelContactMessages.add(lblContactColor);
			panelContactMessages.add(Box.createRigidArea(new Dimension(0, 2)));
			
			btContactColor = new JButton("Farbe wählen");
			btContactColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btContactColor.setBorderPainted(false);
			btContactColor.setForeground(Color.BLUE);
			btContactColor.setFont(new Font("Serif", Font.ITALIC, 13));
			btContactColor.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					jcc = new JColorChooser();
					jcc.setVisible(true);
					Settings.colorContactMessages =JColorChooser.showDialog(cm.getJaimsFrame(), "Hintergrundfarbe wählen", new Color(191,255,14));
					pf.getP2().repaint();
					
					if (link) {
						link =  false;
						linkDesigns.setSelected(false);
					}
				}
			});
			panelContactMessages.add(btContactColor);
			panelContactMessages.add(Box.createRigidArea(new Dimension(0, 10)));
			
			lblContactColorBorder = new JLabel("Wählen Sie eine Randfarbe:", JLabel.LEFT);
			lblContactColorBorder.setAlignmentX(LEFT_ALIGNMENT);
			panelContactMessages.add(lblContactColorBorder);
			panelContactMessages.add(Box.createRigidArea(new Dimension(0, 2)));
			
			btContactColorBorder = new JButton("Farbe wählen");
			btContactColorBorder.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btContactColorBorder.setBorderPainted(false);
			btContactColorBorder.setForeground(Color.BLUE);
			btContactColorBorder.setFont(new Font("Serif", Font.ITALIC, 13));
			btContactColorBorder.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					jcc = new JColorChooser();
					jcc.setVisible(true);
					Settings.colorContactMessageBorder = JColorChooser.showDialog(cm.getJaimsFrame(), "Randfarbe wählen", Color.BLACK);
					pf.getP2().repaint();
					
					if (link) {
						link =  false;
						linkDesigns.setSelected(false);
					}
				}
			});
			panelContactMessages.add(btContactColorBorder);			
			panelContactMessages.add(Box.createRigidArea(new Dimension(0, 10)));
			
			panelContactFont = new JPanel();
			panelContactFont.setLayout(new BoxLayout(panelContactFont, BoxLayout.LINE_AXIS));
			panelContactFont.setAlignmentX(LEFT_ALIGNMENT);
			{
				jcbContactNames = new JComboBox<>(fontNames);
				jcbContactStyle = new JComboBox<>(fontStyle);
				jcbContactSize = new JComboBox<>(fontSize);
				
				
				
				jcbContactNames.setSelectedItem("Serif");
				jcbContactNames.setMaximumSize(new Dimension(80, 25));
				jcbContactNames.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.contactFontName = fontNames[jcbNames.getSelectedIndex()];
						pf.getP2().repaint();
						
						if (link) {
							link =  false;
							linkDesigns.setSelected(false);
						}
					}
				});
				panelContactFont.add(jcbContactNames);
				
				jcbContactStyle.setSelectedIndex(0);
				jcbContactStyle.setMaximumSize(new Dimension(80, 25));
				jcbContactStyle.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.contactFontStyle = jcbContactStyle.getSelectedIndex();
						pf.getP2().repaint();
						
						if (link) {
							link =  false;
							linkDesigns.setSelected(false);
						}
					}
				});
				panelContactFont.add(Box.createRigidArea(new Dimension(8, 0)));
				panelContactFont.add(jcbContactStyle);
				
				jcbContactSize.setMaximumSize(new Dimension(80, 25));
				jcbContactSize.setSelectedIndex(5);
				jcbContactSize.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.contactFontSize = Integer.parseInt(fontSize[jcbContactSize.getSelectedIndex()]);
						pf.getP2().repaint();
						
						if (link) {
							link =  false;
							linkDesigns.setSelected(false);
						}
					}
				});
				panelContactFont.add(Box.createRigidArea(new Dimension(8, 0)));
				panelContactFont.add(jcbContactSize);
				panelContactFont.add(Box.createHorizontalGlue());
			}
			panelContactMessages.add(Box.createRigidArea(new Dimension(0, 2)));
			panelContactMessages.add(panelContactFont);
			
			btContactColorFont = new JButton("Farbe wählen");
			btContactColorFont.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btContactColorFont.setBorderPainted(false);
			btContactColorFont.setForeground(Color.BLUE);
			btContactColorFont.setFont(new Font("Serif", Font.ITALIC, 13));
			btContactColorFont.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					jcc = new JColorChooser();
					jcc.setVisible(true);
					Settings.colorContactMessageFont = JColorChooser.showDialog(cm.getJaimsFrame(), "Schriftfarbe wählen", Color.BLACK);
					pf.getP2().repaint();
					
					if (link) {
						link =  false;
						linkDesigns.setSelected(false);
					}
				}
			});
			panelContactMessages.add(Box.createRigidArea(new Dimension(0, 2)));
			panelContactMessages.add(btContactColorFont);
			panelContactMessages.add(Box.createHorizontalGlue());
		}
		panel.add(panelContactMessages);
		
		setViewportView(panel);
		
	}
	
	public PreviewFrame getPF() {
		return pf;
	}

}
