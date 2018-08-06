package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class EventManager extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private int					xStart				= 0, yStart = 0, width = 0, height = 40, numberOfNewEvents = 0;
	private String				string				= "";

	public EventManager(int numberOfNewEvents) {

		this.numberOfNewEvents = numberOfNewEvents;
		initGUI();
	}

	private void initGUI() {

		setMinimumSize(new Dimension(250, 40));
		setMaximumSize(new Dimension(250, 40));

		if (numberOfNewEvents > 0) {
			string = "" + numberOfNewEvents;
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			setToolTipText(numberOfNewEvents + " neue Events");
			width = 40;
		}

	}

	public void addNewEvent() {

		if (numberOfNewEvents == 0) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			setToolTipText(numberOfNewEvents + " neue Events");
			width = 40;
			repaint();
		}

		numberOfNewEvents++;

		new Timer(20, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (width < 230)
					width += 10;

				if (width == 230) {
					string = numberOfNewEvents + " neue Events";
					repaint();

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					string = numberOfNewEvents + "";
					repaint();

					new Timer(20, new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {

							if (width > 0)
								width -= 10;

							if (width == 0)
								((Timer) e.getSource()).stop();

						}
					}).start();

					((Timer) e.getSource()).stop();
				}

			}
		}).start();
	}

	public void removeEvent() {

		if (numberOfNewEvents == 1) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			width = 0;
			setToolTipText("");
		}

		numberOfNewEvents--;
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());

		if (numberOfNewEvents > 0) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(255, 123, 0));
			g2.fillRoundRect(xStart, yStart, width, height, 40, 40);

			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Sans Serif", Font.BOLD, 16));
			g2.drawString(string, (float) width / 2, (float) height / 2);

			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(1.2F));
			g2.drawRoundRect(xStart, yStart, width, height, 40, 40);
		}
	}
}
