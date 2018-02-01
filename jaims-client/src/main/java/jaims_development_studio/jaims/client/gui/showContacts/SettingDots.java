package jaims_development_studio.jaims.client.gui.showContacts;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.NullSelectionModel;

public class SettingDots extends JPanel {

	private JPopupMenu	jpm;
	private JMenuItem	item;

	public SettingDots(GUIMain guimain) {

		setPreferredSize(new Dimension(38, 38));
		setMaximumSize(getPreferredSize());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		jpm = new JPopupMenu();
		jpm.setBackground(new Color(0, 0, 0, 0));
		jpm.add(item = new JMenuItem(""), new Icon(
				Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/calendar.png"))));
		jpm.setLightWeightPopupEnabled(false);
		jpm.setSelectionModel(new NullSelectionModel());
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("Pressed");

			}
		});
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				System.out.println(SettingDots.this.getX());
				jpm.show(SettingDots.this, 0, 38);
			}

		});
	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(200, 200, 200));
		g2d.fillOval(2, 2, 34, 34);

		g2d.setColor(Color.BLACK);
		// g2d.drawOval(1, 1, 35, 35);
		g2d.fillOval(5, 15, 8, 8);
		g2d.fillOval(15, 15, 8, 8);
		g2d.fillOval(25, 15, 8, 8);

		g2d.dispose();
	}

	private class Icon extends JPanel {

		private Image img;

		public Icon(Image img) {

			this.img = img.getScaledInstance(34, 34, Image.SCALE_SMOOTH);
			setPreferredSize(new Dimension(34, 34));
			setMaximumSize(new Dimension(34, 34));
		}

		@Override
		public void paintComponent(Graphics g) {

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g2d.setClip(new RoundRectangle2D.Double(0, 0, 34, 34, 34, 34));
			g2d.drawImage(img, 0, 0, this);

			g2d.dispose();
		}
	}

}
