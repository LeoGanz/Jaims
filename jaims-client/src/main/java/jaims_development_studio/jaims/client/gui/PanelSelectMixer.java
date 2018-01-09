package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

public class PanelSelectMixer extends JPanel {

	private PanelMixerInfo		pmi;
	private PanelAudioSettings	pas;
	private String				device;
	private boolean				mouseCursorChanged	= false;

	public PanelSelectMixer(PanelMixerInfo pmi, PanelAudioSettings pas, String device) {

		this.pmi = pmi;
		this.pas = pas;
		this.device = device;
		setPreferredSize(new Dimension(405, 40));
		setMaximumSize(getPreferredSize());
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				if (e.getX() >= 376 && e.getX() <= 396 && e.getY() >= 3 && e.getY() <= 36) {
					pas.showSelectionPanel(device);
				}

			}

		});
		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {

				if (e.getX() >= 376 && e.getX() <= 396 && e.getY() >= 3 && e.getY() <= 36) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));
					mouseCursorChanged = true;
				} else if (!(e.getX() >= 376 && e.getX() <= 396 && e.getY() >= 3 && e.getY() <= 36)) {
					if (mouseCursorChanged) {
						mouseCursorChanged = false;
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}

			}
		});
	}

	public void setPanelMixerInfo(PanelMixerInfo pmi) {

		this.pmi = pmi;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Arial", Font.PLAIN, 15));
		g2.drawString(pmi.getMixerName(), 15, (getHeight() / 2) + (g2.getFontMetrics(g2.getFont()).getHeight() / 3));

		g2.setStroke(new BasicStroke(2F));
		g2.setColor(Color.GRAY);
		g2.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
		g2.drawLine(getWidth() - 26, 1, getWidth() - 26, getHeight() - 2);

		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1.5F));
		g2.drawLine(getWidth() - 19, 18, getWidth() - 14, 23);
		g2.drawLine(getWidth() - 14, 23, getWidth() - 9, 18);

		g2.dispose();
	}

}
