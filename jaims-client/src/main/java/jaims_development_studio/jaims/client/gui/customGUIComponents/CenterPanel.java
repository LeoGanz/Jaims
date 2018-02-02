package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class CenterPanel extends JPanel {

	public CenterPanel() {

		setLayout(new BorderLayout());
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
