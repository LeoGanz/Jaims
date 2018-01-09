package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.sound.sampled.Mixer;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelMixerInfo extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private Mixer.Info			mixerInfo;
	private boolean				selected			= false;
	private Color				transparent			= new Color(0, 0, 0, 0);
	private Font				font				= new Font("Arial", Font.BOLD, 16);

	public PanelMixerInfo(Mixer.Info mixerInfo) {

		this.mixerInfo = mixerInfo;
		setPreferredSize(new Dimension(390, 40));
		setMaximumSize(getPreferredSize());
		setBorder(new LineBorder(Color.GRAY));
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {

				selected = false;
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {

				selected = true;
				repaint();
			}
		});
	}

	public String getMixerName() {

		return mixerInfo.getName();
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (selected) {
			g2.setColor(transparent);
			g2.fillRect(4, 4, getWidth() - 8, getHeight() - 8);
			g2.setColor(Color.BLUE);
			g2.setFont(font);
			g2.drawString(mixerInfo.getName(), 15,
					(getHeight() / 2) + (g2.getFontMetrics(g2.getFont()).getHeight() / 3));
			g2.drawRect(4, 4, getWidth() - 8, getHeight() - 8);
		} else {
			g2.setColor(transparent);
			g2.fillRect(4, 4, getWidth() - 8, getHeight() - 8);
			g2.setColor(Color.BLACK);
			g2.setFont(font);
			g2.drawString(mixerInfo.getName(), 15,
					(getHeight() / 2) + (g2.getFontMetrics(g2.getFont()).getHeight() / 3));
			g2.setColor(Color.GRAY);
			g2.drawRect(4, 4, getWidth() - 8, getHeight() - 8);
		}
		g2.dispose();
	}

}
