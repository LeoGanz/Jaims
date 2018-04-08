package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class FrameTop extends JPanel {

	private boolean maximized = false, hoveringOnClose = false;

	public FrameTop(GUIMain guiMain) {

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(Box.createRigidArea(new Dimension(0, 40)));

		JPanel pMinimize = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2.setColor(Color.LIGHT_GRAY);
				g2.fillOval(0, 0, getWidth(), getHeight());
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(1.8F));
				g2.drawLine(8, 17, getWidth() - 9, 17);

				g2.dispose();
			}
		};
		pMinimize.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				guiMain.getJaimsFrame().setExtendedState(JFrame.ICONIFIED);
			}

			@Override
			public void mouseExited(MouseEvent e) {

				pMinimize.setMinimumSize(new Dimension(25, 25));
				pMinimize.setPreferredSize(pMinimize.getMinimumSize());
				pMinimize.setMaximumSize(pMinimize.getMinimumSize());
				pMinimize.revalidate();
				repaint();

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				pMinimize.setMinimumSize(new Dimension(30, 30));
				pMinimize.setPreferredSize(pMinimize.getMinimumSize());
				pMinimize.setMaximumSize(pMinimize.getMinimumSize());
				pMinimize.revalidate();
				repaint();
			}
		});
		pMinimize.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pMinimize.setMinimumSize(new Dimension(25, 25));
		pMinimize.setPreferredSize(pMinimize.getMinimumSize());
		pMinimize.setMaximumSize(pMinimize.getMinimumSize());

		JPanel pMaximize = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				if (maximized == false) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2.setColor(Color.LIGHT_GRAY);
					g2.fillOval(0, 0, getWidth(), getHeight());
					g2.setColor(Color.BLACK);
					g2.drawRect(5, 6, getWidth() - 11, getHeight() - 12);

					g2.dispose();
				} else {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2.setColor(Color.LIGHT_GRAY);
					g2.fillOval(0, 0, getWidth(), getHeight());
					g2.setColor(Color.BLACK);
					g2.drawRect(5, 10, 9, 9);

					g2.dispose();
				}
			}
		};
		pMaximize.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (maximized) {
					maximized = false;
					guiMain.getJaimsFrame().setExtendedState(JFrame.NORMAL);
				} else {
					maximized = true;
					guiMain.getJaimsFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
				repaint();

			}

			@Override
			public void mouseExited(MouseEvent e) {

				pMaximize.setMinimumSize(new Dimension(25, 25));
				pMaximize.setPreferredSize(pMaximize.getMinimumSize());
				pMaximize.setMaximumSize(pMaximize.getMinimumSize());
				pMaximize.revalidate();
				repaint();

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				pMaximize.setMinimumSize(new Dimension(30, 30));
				pMaximize.setPreferredSize(pMaximize.getMinimumSize());
				pMaximize.setMaximumSize(pMaximize.getMinimumSize());
				pMaximize.revalidate();
				repaint();

			}
		});
		pMaximize.setMinimumSize(new Dimension(25, 25));
		pMaximize.setPreferredSize(pMaximize.getMinimumSize());
		pMaximize.setMaximumSize(pMaximize.getMinimumSize());
		pMaximize.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JPanel pClose = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				if (hoveringOnClose == false) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2.setColor(Color.LIGHT_GRAY);
					g2.fillOval(0, 0, getWidth(), getHeight());
					g2.setColor(Color.BLACK);
					g2.drawLine(6, 5, getWidth() - 7, getHeight() - 6);
					g2.drawLine(6, getHeight() - 6, getWidth() - 7, 5);

					g2.dispose();
				} else {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2.setColor(new Color(128, 0, 0));
					g2.fillOval(0, 0, getWidth(), getHeight());
					g2.setColor(Color.WHITE);
					g2.setStroke(new BasicStroke(1.2F));
					g2.drawLine(6, 5, getWidth() - 7, getHeight() - 6);
					g2.drawLine(6, getHeight() - 6, getWidth() - 7, 5);

					g2.dispose();
				}
			}
		};
		pClose.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				System.exit(0);

			}

			@Override
			public void mouseExited(MouseEvent e) {

				hoveringOnClose = false;
				pClose.setMinimumSize(new Dimension(25, 25));
				pClose.setPreferredSize(pClose.getMinimumSize());
				pClose.setMaximumSize(pClose.getMinimumSize());
				pClose.revalidate();
				repaint();

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				hoveringOnClose = true;
				pClose.setMinimumSize(new Dimension(30, 30));
				pClose.setPreferredSize(pClose.getMinimumSize());
				pClose.setMaximumSize(pClose.getMinimumSize());
				pClose.revalidate();
				repaint();

			}
		});
		pClose.setMinimumSize(new Dimension(25, 25));
		pClose.setPreferredSize(pClose.getMinimumSize());
		pClose.setMaximumSize(pClose.getMinimumSize());
		pClose.setCursor(new Cursor(Cursor.HAND_CURSOR));

		add(Box.createHorizontalGlue());
		add(pMinimize);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(pMaximize);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(pClose);
		add(Box.createHorizontalGlue());

	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.setColor(Color.LIGHT_GRAY);
		g2.setStroke(new BasicStroke(0.8F));
		g2.drawLine(45, getHeight() - 3, getWidth() - 45, getHeight() - 3);
	}

}
