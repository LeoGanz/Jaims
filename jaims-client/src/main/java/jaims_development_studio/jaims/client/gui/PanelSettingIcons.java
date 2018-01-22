package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
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
import javax.swing.border.MatteBorder;

import jaims_development_studio.jaims.client.audio.SelectAudioDevices;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelSettingIcons extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private PanelSettings		ps;
	private ClientMain			cm;
	Image						img;
	private PanelAudioSettings	panelAudioSettings;
	private SelectAudioDevices	sad;

	public PanelSettingIcons(PanelSettings ps, ClientMain cm, SelectAudioDevices sad) {

		this.ps = ps;
		this.cm = cm;
		this.sad = sad;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setBorder(new MatteBorder(2, 0, 0, 0, Color.gray));
		{
			add(Box.createVerticalGlue());

			img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/calendar.png"));
			img = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);

			JPanel pCalendar = new JPanel();
			pCalendar.setLayout(new BoxLayout(pCalendar, BoxLayout.LINE_AXIS));
			pCalendar.setOpaque(false);
			pCalendar.setBackground(new Color(0, 0, 0, 0));
			{
				pCalendar.add(Box.createHorizontalGlue());

				JLabel lblCalendar = new JLabel();
				lblCalendar.setIcon(new ImageIcon(img));
				lblCalendar.setPreferredSize(new Dimension(61, 61));
				lblCalendar.setMaximumSize(lblCalendar.getPreferredSize());
				lblCalendar.setToolTipText("Calendar settings");
				lblCalendar.setCursor(new Cursor(Cursor.HAND_CURSOR));
				lblCalendar.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseReleased(MouseEvent e) {

						if (panelAudioSettings == null)
							panelAudioSettings = new PanelAudioSettings(cm, sad);
						ps.addCenterPanel(panelAudioSettings);

					}
				});
				pCalendar.add(lblCalendar);

				pCalendar.add(Box.createHorizontalGlue());
			}

			add(pCalendar);

			add(Box.createVerticalGlue());
		}

	}
}
