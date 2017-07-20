package jaims_development_studio.jaims.server.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.border.BevelBorder;

public class MonitorBar extends JComponent implements IMonitorColors {
	
	private static final long	serialVersionUID	= 1L;
	protected String			info;
	protected boolean			enabled				= true;

	public String getInfo() {
		return info;
	}

	public MonitorBar() {
		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		if (!enabled)
			setBorder(new BevelBorder(BevelBorder.LOWERED));
		else
			setBorder(new BevelBorder(BevelBorder.RAISED));
		
		super.setEnabled(enabled);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (!enabled) {
			Graphics2D g2d = (Graphics2D) g;

			Insets insets = getInsets();
			g2d.setColor(COLOR_BAR_BACKGROUND);
			g2d.fillRect(insets.left, insets.top, getWidth() - insets.right, getHeight() - insets.bottom);
		}
		super.paintComponent(g);
	}

}
