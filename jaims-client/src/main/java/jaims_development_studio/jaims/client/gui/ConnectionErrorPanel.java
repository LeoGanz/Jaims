package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConnectionErrorPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int[] x = {2,18,10};
	int[] y = {2,2,30};
	int[] x2 = {1,18,10};
	int[] y2 = {1,1,30};
	
	
	public ConnectionErrorPanel() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		JLabel lbl = new JLabel("Verbindung zum Server fehlgeschlagen!", JLabel.CENTER);
		lbl.setForeground(Color.RED);
		lbl.setFont(new Font("Sans Serif", Font.BOLD, 13));
		add(Box.createRigidArea(new Dimension(30, 50)));
		add(lbl);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Color.RED);
		g2d.fill(new RoundPolygon(new Polygon(x,y, 3), 5));
		
		g2d.setColor(Color.BLACK);
		g2d.draw(new RoundPolygon(new Polygon(x2,y2,3), 5));
		
		g2d.setColor(Color.RED);
		g2d.fillOval(6, 35, 8, 8);
		
		g2d.setColor(Color.BLACK);
		g2d.drawOval(5, 34, 9,9);
	}

}
