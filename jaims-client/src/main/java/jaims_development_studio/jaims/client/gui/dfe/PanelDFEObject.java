package jaims_development_studio.jaims.client.gui.dfe;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jaims_development_studio.jaims.client.logic.DFEObject;
import jaims_development_studio.jaims.client.logic.EFileType;

public class PanelDFEObject extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private DFEObject			dfeo;

	public PanelDFEObject(DFEObject dfeo) {

		this.dfeo = dfeo;
		initGUI();
	}

	private void initGUI() {

		setMinimumSize(new Dimension(200, 50));
		setPreferredSize(getMinimumSize());
		setMaximumSize(getMinimumSize());
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				File f = new File(dfeo.getPath());

				if (!Desktop.isDesktopSupported()) {
					System.out.println("Desktop is not supported");
					return;
				}

				Desktop desktop = Desktop.getDesktop();
				if (f.exists())
					try {
						desktop.open(f);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});

		if (dfeo.getFileType().equals(EFileType.FILETYPE_AUDIO)) {
			initGUIForAudio();
		} else if (dfeo.getFileType().equals(EFileType.FILETYPE_EXCEL)) {

		} else if (dfeo.getFileType().equals(EFileType.FILETYPE_OPT)) {

		} else if (dfeo.getFileType().equals(EFileType.FILETYPE_PDF)) {

		} else if (dfeo.getFileType().equals(EFileType.FILETYPE_TEXT)) {

		} else if (dfeo.getFileType().equals(EFileType.FILETYPE_VIDEO)) {

		} else if (dfeo.getFileType().equals(EFileType.FILETYPE_WORD)) {

		} else if (dfeo.getFileType().equals(EFileType.FILETYPE_ZIP)) {

		} else if (dfeo.getFileType().equals(EFileType.FILETYPE_PPT)) {

		}
	}

	private void initGUIForAudio() {

		try {
			File f = new File(getClass().getClassLoader()
					.getResource("images/fileIcons/" + dfeo.getExtension() + ".png").toURI());
			if (f.exists()) {
				Image i = Toolkit.getDefaultToolkit().getImage(f.toURI().toURL());
				JLabel lblIcon = new JLabel(new ImageIcon(i));
				lblIcon.setOpaque(false);
				lblIcon.setMinimumSize(new Dimension(48, 48));
				lblIcon.setPreferredSize(lblIcon.getMinimumSize());
				lblIcon.setMaximumSize(lblIcon.getMinimumSize());
				add(Box.createRigidArea(new Dimension(10, 0)));
				add(lblIcon);

				JLabel lblName = new JLabel(dfeo.getFilename(), JLabel.LEFT);
				lblName.setOpaque(false);
				lblName.setFont(new Font("Arial", Font.PLAIN, 16));
				add(Box.createRigidArea(new Dimension(10, 0)));
				add(lblName);
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.GRAY);

		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
	}

}
