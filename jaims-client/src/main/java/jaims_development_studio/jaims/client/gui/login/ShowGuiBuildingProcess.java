package jaims_development_studio.jaims.client.gui.login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class ShowGuiBuildingProcess extends JPanel {

	private GUIMain			guiMain;
	private Image			img;
	private JProgressBar	jpb;

	public ShowGuiBuildingProcess(GUIMain guiMain) {

		this.guiMain = guiMain;
		img = Toolkit.getDefaultToolkit()
				.getImage(getClass().getClassLoader().getResource("images/LoginBackground.jpeg"))
				.getScaledInstance(900, 600, Image.SCALE_FAST);

	}

	public void initGUI() {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(Box.createVerticalGlue());

		jpb = new JProgressBar(0, 100);
		jpb.setBorder(new LineBorder(Color.black, 2));
		jpb.setPreferredSize(new Dimension(500, 40));
		jpb.setMaximumSize(jpb.getPreferredSize());
		jpb.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(jpb);

		add(Box.createVerticalGlue());

	}

	public void setProgress(int progress) {

		jpb.setValue(progress);
		jpb.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setClip(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40.5, 40.5));
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.drawImage(img, getWidth() / 2 - img.getWidth(this) / 2, getHeight() / 2 - img.getHeight(this) / 2, this);
	}

}
