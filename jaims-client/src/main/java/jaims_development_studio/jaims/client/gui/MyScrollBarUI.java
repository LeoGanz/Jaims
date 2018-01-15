package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MyScrollBarUI extends BasicScrollBarUI {

	private final Dimension	d			= new Dimension();
	private final Color		transparent	= new Color(0, 0, 0, 0);

	@Override
	protected JButton createDecreaseButton(int orientation) {

		return new JButton() {
			@Override
			public Dimension getPreferredSize() {

				return d;
			}
		};
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {

		return new JButton() {
			@Override
			public Dimension getPreferredSize() {

				return d;
			}
		};
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle r) {

		g.setColor(c.getBackground());
		g.fillRect(r.x, r.y, r.width, r.height);
	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle r) {

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color color = null;
		JScrollBar sb = (JScrollBar) c;
		if (!sb.isEnabled() || r.width > r.height) {
			return;
		} else if (isDragging) {
			color = new Color(96, 96, 96, 200);
		} else if (isThumbRollover()) {
			color = new Color(160, 160, 160, 200);
		} else {
			color = new Color(160, 160, 160, 120);
		}
		g2.setPaint(color);
		g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
		g2.setColor(color);
		g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
		g2.dispose();
	}

	@Override
	protected void setThumbBounds(int x, int y, int width, int height) {

		super.setThumbBounds(x + width - 8, y, 8, height);
		scrollbar.repaint();
	}

	public static ComponentUI createUI(JComponent c) {

		return new MyScrollBarUI();
	}

}
