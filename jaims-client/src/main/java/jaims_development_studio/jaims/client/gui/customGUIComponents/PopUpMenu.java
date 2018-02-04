package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JWindow;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.showContacts.SettingDots;

public class PopUpMenu extends JWindow {

	private GUIMain		guiMain;
	private SettingDots	sd;
	private Image		img;

	public PopUpMenu(GUIMain guiMain, SettingDots sd) {

		super();

		this.guiMain = guiMain;
		this.sd = sd;

		initGUI();
	}

	private void initGUI() {

		img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/calendar.png"))
				.getScaledInstance(40, 40, Image.SCALE_SMOOTH);

		setFocusable(true);
		setSize(42, 42);
		setBackground(new Color(0, 0, 0, 0));
		setLocation((int) sd.getLocationOnScreen().getX() - 2, (int) sd.getLocationOnScreen().getY() + 40);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		JPanel pBackground = new JPanel();
		pBackground.setBackground(new Color(0, 0, 0, 0));
		pBackground.setOpaque(false);
		pBackground.setLayout(new BoxLayout(pBackground, BoxLayout.PAGE_AXIS));

		JPanel panelCalendar = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());

				g.drawImage(img, 1, 1, this);
			}
		};
		panelCalendar.setMinimumSize(new Dimension(42, 42));
		panelCalendar.setPreferredSize(panelCalendar.getMinimumSize());
		panelCalendar.setMaximumSize(panelCalendar.getMinimumSize());
		panelCalendar.setOpaque(false);
		pBackground.add(panelCalendar);

		add(pBackground);

		guiMain.getJaimsFrame().addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (PopUpMenu.this.contains(e.getPoint()) == false)
					dispose();

			}
		});

		setVisible(true);
	}

}
