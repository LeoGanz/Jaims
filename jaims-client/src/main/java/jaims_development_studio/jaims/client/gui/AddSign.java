package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class AddSign extends JPanel {

	public AddSign(ClientMain cm) {

		setPreferredSize(new Dimension(38, 38));
		setMaximumSize(getPreferredSize());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				cm.addPanelAddUser();

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
		g2d.fillRect(10, 17, 18, 4);
		g2d.fillRect(17, 10, 4, 18);

		g2d.dispose();
	}
}
