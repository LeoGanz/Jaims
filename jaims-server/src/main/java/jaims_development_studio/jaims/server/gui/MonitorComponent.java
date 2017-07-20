package jaims_development_studio.jaims.server.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MonitorComponent extends JComponent implements IMonitorColors {

	private static final long serialVersionUID = 1L;
	
	private final MonitorBar	monitorBar;
	private final JLabel		info;
	
	public MonitorComponent(MonitorBar monitorBar) {

		this.monitorBar = monitorBar;
		
		setBorder(new EmptyBorder(5, 0, 5, 0));
		setLayout(new BorderLayout());
		
		int labelHeight = 20;
		info = new JLabel();
		info.setPreferredSize(new Dimension(getWidth(), labelHeight));
		info.setForeground(COLOR_FONT);
		add(info, BorderLayout.NORTH);
		
		add(monitorBar, BorderLayout.CENTER);

	}

	@Override
	protected void paintComponent(Graphics g) {
		info.setText(monitorBar.getInfo());
		super.paintComponent(g);
	}

	@Override
	public void setEnabled(boolean enabled) {
		monitorBar.setEnabled(enabled);
		super.setEnabled(enabled);
		SwingUtilities.invokeLater(() -> info.repaint());
	}
	
}
