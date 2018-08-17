package jaims_development_studio.jaims.client.gui.dfe;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;


public class ConnectButton extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DFEWindow window;
	private boolean connected = false;
	
	public ConnectButton(DFEWindow window) {
		this.window = window;
		initGUI();
	}
	
	private void initGUI() {
		setText("Connect");
		setMinimumSize(new Dimension(120, 30));
		setPreferredSize(getMinimumSize());
		setMaximumSize(getMinimumSize());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setForeground(Color.WHITE);
		setFont(new Font("SansSerif", Font.BOLD, 16));
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
				if (connected == false) {
					setText("Connectiong...");
					
					while(window.initialiseServer() == false) {	}
					
					connected = true;
					setText("Close");
				}else {
					setText("Closing");
					
					//TODO: Close connection
				}
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
				setMinimumSize(new Dimension(120, 30));
				setPreferredSize(getMinimumSize());
				setMaximumSize(getMinimumSize());
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
				setMinimumSize(new Dimension(200, 50));
				setPreferredSize(getMinimumSize());
				setMaximumSize(getMinimumSize());
				
				repaint();
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setClip(new RoundRectangle2D.Double(0,0, getWidth(), getHeight(), getHeight(), getHeight()));
		
		if (connected == false) 
			g2.setColor(new Color(76,196,23));
		else
			g2.setColor(new Color(247,13,26));
		
		g2.fillRect(0, 0, getWidth(), getHeight());
		
	}

}
