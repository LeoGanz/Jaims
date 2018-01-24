package jaims_development_studio.jaims.client.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel {

	Image			img;
	String			username;
	private boolean	clicked	= false;
	private Timer	timer	= null;

	public Panel(Image img, String username) {

		this.img = scaleMaintainAspectRatio(img);
		this.username = username;
		setPreferredSize(new Dimension(250, 55));
		setMaximumSize(getPreferredSize());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (clicked) {
					clicked = false;
					if (timer.isRunning())
						timer.stop();

				} else {
					clicked = true;
					drawCircle();
				}

			}
		});
	}

	private void drawCircle() {

		Graphics g = getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setClip(0, 0, getWidth(), getHeight());

		timer = new Timer(20, new ActionListener() {
			int	initialWidth	= getWidth() + 60, width = 10;
			int	initialHeight	= getHeight() + 15, height = 2;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (width < initialWidth) {
					g2d.setColor(Color.GRAY);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
					g2d.fillOval(initialWidth / 2 - width / 2, initialHeight / 2 - height / 2, width, height);
					width += 20;
					height += 4;
				} else {
					timer.stop();
				}

			}
		});

		if (clicked) {
			g2d.setColor(Color.GRAY);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}

		JFrame f = new JFrame();
		JLayeredPane lp = new JLayeredPane();
		lp.setLayout(new BorderLayout());

		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(Color.BLACK);
				g2d.setFont(new Font("Calibri", Font.BOLD, 15));
				g2d.drawString(username, 75, 30);
				g2d.setClip(new RoundRectangle2D.Double(10, 3, img.getWidth(this), img.getHeight(this), 15, 15));
				g2d.drawImage(img, 10, 3, this);
				g2d.setClip(0, 0, getWidth(), getHeight());
				g2d.setStroke(new BasicStroke(1.8F));
				g2d.drawRoundRect(10, 3, 50, 50, 15, 15);

				g2d.dispose();

			}
		};
		// panel.setOpaque(false);
		panel.setMaximumSize(new Dimension(350, 50));// Size is needed here, as there is no layout in lp

		JPanel glass = new JPanel();
		glass.setOpaque(true); // Set to true to see it
		glass.setBackground(Color.GREEN);
		glass.setMaximumSize(new Dimension(350, 160));
		glass.setLocation(0, 0);

		lp.add(panel, BorderLayout.CENTER, Integer.valueOf(2));
		lp.add(glass, BorderLayout.CENTER, Integer.valueOf(1));
		f.getContentPane().add(lp);
		// f.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Calibri", Font.BOLD, 15));
		g2d.drawString(username, 75, 30);
		g2d.setClip(new RoundRectangle2D.Double(10, 3, img.getWidth(this), img.getHeight(this), 15, 15));
		g2d.drawImage(img, 10, 3, this);
		g2d.setClip(0, 0, getWidth(), getHeight());
		g2d.setStroke(new BasicStroke(1.8F));
		g2d.drawRoundRect(10, 3, 50, 50, 15, 15);

		g2d.dispose();

	}

	private Image scaleMaintainAspectRatio(Image image) {

		image.getHeight(this);
		image.getWidth(this);
		Image returnImg = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		return returnImg;
	}

}
