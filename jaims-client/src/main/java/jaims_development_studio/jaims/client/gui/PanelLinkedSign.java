package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class PanelLinkedSign extends JPanel{
	
	boolean linked = true;
	
	public PanelLinkedSign() {
		setPreferredSize(new Dimension(120, 50));
		setMaximumSize(getPreferredSize());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (linked)
					linked = false;
				else
					linked = true;
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2d = (Graphics2D) g;
		if (linked) {
//			g2d.setStroke(new BasicStroke(2F));
//			g2d.setColor(Color.GRAY);
//			g2d.drawRoundRect(3, 3, 80, 40, 15, 15);
			
			g2d.setColor(Color.BLACK);
			//g2d.setStroke(new BasicStroke(1F));
			g2d.drawRoundRect(2, 2, 80, 40, 15, 15);
			
		}
	}

}
