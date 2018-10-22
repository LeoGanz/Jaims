package jaims_development_studio.jaims.client.gui.dfe;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class SendButton extends JButton {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private DFEWindow			window;

	public SendButton(DFEWindow window) {

		this.window = window;

		initGUI();
	}

	private void initGUI() {

		setText("Send files");
		setMinimumSize(new Dimension(120, 30));
		setPreferredSize(getMinimumSize());
		setMaximumSize(getMinimumSize());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setForeground(Color.BLACK);
		setOpaque(false);
		setContentAreaFilled(false);
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent arg0) {

				setEnabled(false);
				revalidate();

				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setMultiSelectionEnabled(true);

				window.sendFiles(jfc.getSelectedFiles());

				setEnabled(true);
				revalidate();
				repaint();

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				setMinimumSize(new Dimension(120, 30));
				setPreferredSize(getMinimumSize());
				setMaximumSize(getMinimumSize());

				revalidate();
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

				setMinimumSize(new Dimension(200, 50));
				setPreferredSize(getMinimumSize());
				setMaximumSize(getMinimumSize());

				revalidate();
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

		g2.setColor(new Color(192, 192, 192));
		g2.fillRoundRect(0, 0, getWidth(), 30, 30, 30);

		super.paintComponent(g);
	}

}
