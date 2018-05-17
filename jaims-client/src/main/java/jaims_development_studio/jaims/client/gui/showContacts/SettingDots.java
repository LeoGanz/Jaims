package jaims_development_studio.jaims.client.gui.showContacts;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.PopUpMenu;

public class SettingDots extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private boolean				drawAnimation		= false;
	private int					x					= 19, y = 19, width = 0, height = 0;
	private Timer				timer;
	private PopUpMenu			pum;

	public SettingDots(GUIMain guimain) {

		setPreferredSize(new Dimension(38, 38));
		setMaximumSize(getPreferredSize());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setFocusable(true);

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				x = 1;
				y = 1;
				width = width + 6;
				height = height + 6;
				repaint();
				drawAnimation = false;

			}

			@Override
			public void mousePressed(MouseEvent e) {

				x = 4;
				y = 4;
				width = width - 6;
				height = height - 6;
				repaint();

				if (pum == null)
					pum = new PopUpMenu(guimain, SettingDots.this);

			}

			@Override
			public void mouseExited(MouseEvent e) {

				if (timer.isRunning())
					timer.stop();

				timer = new Timer(8, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						if (x < 19)
							x += 1;

						if (y < 19)
							y += 1;

						if (width > 0)
							width -= 2;

						if (height > 0)
							height -= 2;

						repaint();

						if (x == 19) {
							((Timer) e.getSource()).stop();
							drawAnimation = false;
						}

					}
				});
				timer.start();

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				if (timer != null)
					if (timer.isRunning())
						timer.stop();

				drawAnimation = true;
				timer = new Timer(8, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						if (x > 1)
							x -= 1;

						if (y > 1)
							y -= 1;

						if (width < 34)
							width += 2;

						if (height < 34)
							height += 2;

						repaint();

						if (x == 1) {
							((Timer) e.getSource()).stop();
						}

					}
				});
				timer.start();

			}
		});

	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(170, 170, 170));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(200, 200, 200));
		g2d.drawOval(1, 1, 34, 34);

		if (drawAnimation) {
			g2d.setColor(new Color(200, 200, 200));
			g2d.fillOval(x, y, width, height);
		}

		g2d.setColor(Color.BLACK);
		g2d.fillOval(5, 15, 8, 8);
		g2d.fillOval(15, 15, 8, 8);
		g2d.fillOval(25, 15, 8, 8);

		g2d.dispose();
	}

	public void setDrawAnimation(boolean animation) {

		drawAnimation = animation;
		pum = null;
	}

}
