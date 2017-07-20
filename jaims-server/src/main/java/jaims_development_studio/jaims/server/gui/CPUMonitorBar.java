package jaims_development_studio.jaims.server.gui;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPUMonitorBar extends MonitorBar {
	
	private static final Logger	LOG					= LoggerFactory.getLogger(CPUMonitorBar.class);
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void paintComponent(Graphics g) {

		if (!enabled) {
			info = "CPU usage: -";
			super.paintComponent(g);
			return;
		}
		
		double cpuLoad = Double.NaN;
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name;
		AttributeList list = null;
		try {
			name = ObjectName.getInstance("java.lang:type=OperatingSystem");
			list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });
		} catch (MalformedObjectNameException | InstanceNotFoundException | ReflectionException e) {
			LOG.warn("Couldn't calculate cpu usage", e);
		}
		
		if ((list != null) && !list.isEmpty()) {
			
			Attribute att = (Attribute) list.get(0);
			Double value = (Double) att.getValue();
			
			// may take a couple of seconds before it gets real values
			if (value != -1.0)
				// sets a percentage value with 1 decimal point precision
				cpuLoad = ((int) (value * 1000) / 10.0);
		}

		info = "CPU usage: " + cpuLoad + "%";

		Insets insets = getInsets();
		
		int width = getWidth() - insets.left - insets.right;
		int height = getHeight() - insets.top - insets.bottom;

		int x0 = insets.left;
		int x1 = insets.left + (int) ((width * ((cpuLoad != Double.NaN) ? cpuLoad : 0)) / 100);
		int x2 = (width + insets.right) - x1;
		
		int y0 = insets.top;
		int y1 = insets.top + height;

		Graphics2D g2d = (Graphics2D) g;
		Paint oldPaint = g2d.getPaint();
		if (cpuLoad < 90)
			g2d.setPaint(new GradientPaint(x0, y0, COLOR_GRADIENT_LEFT, x1, y0, COLOR_GRADIENT_RIGHT));
		else
			g2d.setPaint(new GradientPaint(x0, y0, COLOR_GRADIENT_LEFT_WARNING, x1, y0, COLOR_GRADIENT_RIGHT_WARNING));
		g2d.fillRect(x0, y0, x1, y1);

		g2d.setPaint(oldPaint);

		g2d.setColor(COLOR_BAR_BACKGROUND);
		g2d.fillRect(x1, y0, x2, y1); //getWidth() - x1 - insets.left
		
		super.paintComponent(g);
		
	}
	
}
