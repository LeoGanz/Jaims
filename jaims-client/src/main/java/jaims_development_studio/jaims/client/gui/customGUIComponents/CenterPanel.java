package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * This class was made to serve as a parent to every panel that has to be added
 * to the jframe's content pane's centre. This was done in order to be able to
 * remove these panels more simply than by keeping track of which panel is
 * currently shown.
 * 
 * @author Bu88le
 *
 */
public class CenterPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CenterPanel() {

		setLayout(new BorderLayout());
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
