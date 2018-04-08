package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JWindow;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.showContacts.SettingDots;

public class PopUpMenu extends JWindow {

	private GUIMain		guiMain;
	private SettingDots	sd;
	private Image		img, imgLogout;
	private boolean		mouseOnLogout	= false;

	public PopUpMenu(GUIMain guiMain, SettingDots sd) {

		super();

		this.guiMain = guiMain;
		this.sd = sd;

		initGUI();
	}

	private void initGUI() {

		img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/calendar.png"))
				.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		imgLogout = Toolkit.getDefaultToolkit()
				.getImage(getClass().getClassLoader().getResource("images/logout_button.png"))
				.getScaledInstance(40, 40, Image.SCALE_SMOOTH);

		setFocusable(true);
		setSize(42, 87);
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
		panelCalendar.setToolTipText("Settings");
		panelCalendar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				dispose();
				sd.setDrawAnimation(false);
				guiMain.showSettings();

			}
		});
		pBackground.add(panelCalendar);

		JPanel panelLogout = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());

				g.drawImage(imgLogout, 1, 1, this);
			}
		};
		panelLogout.setMinimumSize(new Dimension(42, 42));
		panelLogout.setPreferredSize(panelLogout.getMinimumSize());
		panelLogout.setMaximumSize(panelLogout.getMinimumSize());
		panelLogout.setOpaque(false);
		panelLogout.setFocusable(true);
		panelLogout.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				dispose();
				guiMain.doLogout();

			}
		});
		pBackground.add(Box.createRigidArea(new Dimension(0, 5)));
		pBackground.add(panelLogout);

		pBackground.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (mouseOnLogout) {
					dispose();
					guiMain.doLogout();
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {

				if (mouseOnLogout) {
					if ((e.getX() >= panelLogout.getX() && e.getY() >= panelLogout.getY()
							&& e.getX() <= panelLogout.getX() + panelLogout.getWidth()
							&& e.getY() <= panelLogout.getY() + panelLogout.getHeight()) == false) {
						mouseOnLogout = false;
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				if (e.getX() >= panelLogout.getX() && e.getY() >= panelLogout.getY()
						&& e.getX() <= panelLogout.getX() + panelLogout.getWidth()
						&& e.getY() <= panelLogout.getY() + panelLogout.getHeight()) {
					mouseOnLogout = true;
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

			}
		});

		add(pBackground);

		guiMain.getJaimsFrame().addComponentListener(new ComponentAdapter() {

			@Override
			public void componentMoved(ComponentEvent e) {

				if (isShowing()) {
					setLocation((int) sd.getLocationOnScreen().getX() - 2, (int) sd.getLocationOnScreen().getY() + 40);
				}

			}

		});

		setVisible(true);

	}

}
