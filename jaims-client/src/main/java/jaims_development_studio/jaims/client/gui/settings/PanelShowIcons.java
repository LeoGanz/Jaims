package jaims_development_studio.jaims.client.gui.settings;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class PanelShowIcons extends JPanel {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 1L;
	private PanelAudioSettings			pas;
	private PanelMessageSettings		pms;
	private PanelChatBackgroundSettings	pcbs;

	public PanelShowIcons(PanelSelectSettings pss, GUIMain guiMain) {

		initGUI(pss, guiMain);
	}

	private void initGUI(PanelSelectSettings pss, GUIMain guiMain) {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.GRAY);
		add(Box.createVerticalGlue());

		JLabel lblAudio = new JLabel(new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/calendar.png"))
						.getScaledInstance(70, 70, Image.SCALE_SMOOTH)),
				JLabel.CENTER);
		lblAudio.setBackground(new Color(0, 0, 0, 0));
		lblAudio.setOpaque(false);
		lblAudio.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblAudio.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblAudio.setToolTipText("Audio settings");
		lblAudio.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (pas == null)
					pas = new PanelAudioSettings(guiMain);

				pss.setCenterPanel(pas);

			}
		});
		add(lblAudio);

		JLabel lblMessages = new JLabel(new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/calendar.png"))
						.getScaledInstance(70, 70, Image.SCALE_SMOOTH)),
				JLabel.CENTER);
		lblMessages.setBackground(new Color(0, 0, 0, 0));
		lblMessages.setOpaque(false);
		lblMessages.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMessages.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblMessages.setToolTipText("ClientInternMessage settings");
		lblMessages.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (pms == null)
					pms = new PanelMessageSettings(guiMain);

				pss.setCenterPanel(pms);

			}
		});
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(lblMessages);

		JLabel lblChatBackground = new JLabel(new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/calendar.png"))
						.getScaledInstance(70, 70, Image.SCALE_SMOOTH)),
				JLabel.CENTER);
		lblChatBackground.setBackground(new Color(0, 0, 0, 0));
		lblChatBackground.setOpaque(false);
		lblChatBackground.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblChatBackground.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblChatBackground.setToolTipText("Chat background settings");
		lblChatBackground.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (pcbs == null)
					pcbs = new PanelChatBackgroundSettings(guiMain);

				pss.setCenterPanel(pcbs);

			}
		});
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(lblChatBackground);

		add(Box.createVerticalGlue());

	}

}
