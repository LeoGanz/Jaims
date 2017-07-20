package jaims_development_studio.jaims.server.gui;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;

public class MemoryMonitorBar extends MonitorBar {
	
	private static final long	serialVersionUID	= 1L;
	
	@Override
	protected void paintComponent(Graphics g) {
		
		if (!enabled) {
			info = "Memory usage: -";
			super.paintComponent(g);
			return;
		}

		long freeMemory = Runtime.getRuntime().freeMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long maxMemory = Runtime.getRuntime().maxMemory();

		long memoryUsed = totalMemory - freeMemory;
		
		info = "Memory usage: " + (memoryUsed / (1024 * 1024)) + " of " + (maxMemory / (1024 * 1024)) + " MB used";

		Insets insets = getInsets();

		int width = getWidth() - insets.left - insets.right;
		int height = getHeight() - insets.top - insets.bottom;

		int x0 = insets.left;
		int x1 = insets.left + (int) ((width * memoryUsed) / maxMemory);
		int x2 = insets.left + (int) ((width * totalMemory) / maxMemory);
		int x3 = insets.left + width;
		
		int y0 = insets.top;
		int y1 = insets.top + height;

		Graphics2D g2d = (Graphics2D) g;
		Paint oldPaint = g2d.getPaint();
		if (((double) memoryUsed / maxMemory) < 0.75)
			g2d.setPaint(new GradientPaint(x0, y0, COLOR_GRADIENT_LEFT, x1, y0, COLOR_GRADIENT_RIGHT));
		else
			g2d.setPaint(new GradientPaint(x0, y0, COLOR_GRADIENT_LEFT_WARNING, x1, y0, COLOR_GRADIENT_RIGHT_WARNING));
		g2d.fillRect(x0, y0, x1, y1);
		
		g2d.setPaint(oldPaint);
		
		g2d.setColor(COLOR_MIDDLE);
		g2d.fillRect(x1, y0, x2, y1);
		
		g2d.setColor(COLOR_BAR_BACKGROUND);
		g2d.fillRect(x2, y0, x3, y1);
		
		super.paintComponent(g);
		
	}
	
}
