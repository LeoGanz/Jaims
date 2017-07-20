package jaims_development_studio.jaims.server.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.server.ITickable;
import jaims_development_studio.jaims.server.Server;

public class StatsComponent extends JComponent implements ITickable {

	private static final long				serialVersionUID	= 1L;
	private volatile boolean				stopped;
	private final Server					server;
	private final List<MonitorComponent>	monitorComponents	= new ArrayList<>();
	
	public StatsComponent(Server server) {
		
		this.server = server;
		
		Dimension size = new Dimension(456, 256);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		setBorder(new EmptyBorder(new Insets(0, 10, 0, 10)));

		setLayout(new GridLayout(4, 1));

		MonitorComponent memoryMonitorComponent = new MonitorComponent(new MemoryMonitorBar());
		monitorComponents.add(memoryMonitorComponent);
		add(memoryMonitorComponent);
		
		MonitorComponent cpuMonitorComponent = new MonitorComponent(new CPUMonitorBar());
		monitorComponents.add(cpuMonitorComponent);
		add(cpuMonitorComponent);
		
		JRadioButton onOffSwitch = new JRadioButton("Suspend statistics updating");
		onOffSwitch.setToolTipText("Suspending statistics updating improves performance");
		onOffSwitch.addActionListener((e) -> toggleStopped());
		add(onOffSwitch, 2);
		
		server.subscribeToTicker(this);

	}
	
	public void toggleStopped() {
		stopped = stopped ? false : true;
		if (stopped)
			server.unsubscribeFromTicker(this);
		else
			server.subscribeToTicker(this);
		for (MonitorComponent c : monitorComponents)
			c.setEnabled(!stopped);
	}

	@Override
	public void tick() {
		repaint();
	}
	
}
