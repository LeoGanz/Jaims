package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicSliderUI;

import jaims_development_studio.jaims.client.chatObjects.ClientProfile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.logic.ClientMain;
import jaims_development_studio.jaims.client.logic.PlayAudio;

public class VoiceMessage extends JPanel {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 1L;
	String						path;
	JLabel						currentTime, maxTime;
	ClientProfile				contactProfile;
	private boolean				own, paused = true;
	Image						img;
	PlayAudio					pa;
	JSlider						slider;
	Thread						thread;
	JPanel						start;
	private ClientMain			cm;

	public VoiceMessage(ClientProfile contactProfile, String pathToFile, boolean own, ClientMain cm) {

		path = pathToFile;
		this.contactProfile = contactProfile;
		this.own = own;
		this.cm = cm;
		initGUI(contactProfile);
	}

	private void initGUI(ClientProfile contactProfile) {

		setOpaque(true);

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setPreferredSize(new Dimension(330, 50));
		setMaximumSize(getPreferredSize());
		if (own)
			setBorder(new RoundBorder(330, 50, cm.getSetting().getColorContactMessageBorder()));
		else
			setBorder(new RoundBorder(330, 50, cm.getSetting().getColorContactMessageBorder()));

		add(Box.createRigidArea(new Dimension(65, 0)));
		long length = getAudioFileLength();

		JPanel p = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				if (own) {
					g2d.setColor(cm.getSetting().getColorOwnMessages());
					g2d.fillRect(0, 0, getWidth(), getHeight());
				} else {
					g2d.setColor(cm.getSetting().getColorContactMessages());
					g2d.fillRect(0, 0, getWidth(), getHeight());
				}

				g2d.setColor(Color.BLACK);
				g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
				g2d.setColor(Color.WHITE);
				g2d.fillRoundRect(2, 2, getWidth() - 3, getHeight() - 3, 20, 20);
			}
		};
		p.setOpaque(false);
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		p.setPreferredSize(new Dimension(176, 42));
		p.setMaximumSize(p.getPreferredSize());
		{
			p.add(Box.createRigidArea(new Dimension(5, 42)));
			start = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {

					g.setColor(Color.WHITE);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setColor(Color.GRAY);
					if (paused) {
						int[] x = {1, 1, 13};
						int[] y = {1, 16, 8};

						g2d.fillPolygon(x, y, 3);
					} else {
						g2d.fillRect(1, 1, 4, 16);
						g2d.fillRect(8, 1, 5, 16);
					}
				}
			};
			start.setOpaque(false);
			start.setPreferredSize(new Dimension(13, 16));
			start.setMaximumSize(start.getPreferredSize());
			start.setCursor(new Cursor(Cursor.HAND_CURSOR));
			start.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent arg0) {

					if (paused) {
						paused = false;
						start.repaint();

						thread = new Thread(pa);
						thread.start();

					} else {
						paused = true;
						start.repaint();
					}
				}
			});
			p.add(start);
			p.add(Box.createRigidArea(new Dimension(5, 0)));

			slider = new JSlider(0, (int) (length / 1000), 0);
			slider.setPreferredSize(new Dimension(150, 40));
			slider.setUI(new CustomSliderUI(slider));
			slider.setOpaque(false);
			p.add(slider);
			p.add(Box.createRigidArea(new Dimension(5, 0)));
		}
		add(p);

		add(Box.createHorizontalGlue());

		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		Date d = new Date(0L);

		currentTime = new JLabel(sdf.format(d) + " / ", JLabel.CENTER);
		add(currentTime);

		d.setTime(length);
		maxTime = new JLabel(sdf.format(d));
		add(maxTime);

		img = scaleMaintainAspectRatio(Toolkit.getDefaultToolkit().createImage(getPicture(contactProfile)));
		pa = new PlayAudio(path, currentTime, slider, p, this);
		thread = new Thread(pa);
	}

	private long getAudioFileLength() {

		AudioFileFormat format;
		try {
			format = AudioSystem.getAudioFileFormat(new File(path));
			float duration = format.getFrameLength() / format.getFormat().getFrameRate();
			return (long) (duration * 1000);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	private byte[] getPicture(ClientProfile up) {

		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(up.getProfilePic());
			ps.setObject(1, up.getUuid());
			rs = ps.executeQuery();
			con.commit();

			rs.next();

			return rs.getBytes(1);
		} catch (SQLException e) {
			BufferedImage bi;
			try {
				bi = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Jaims_User.png"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bi, "jpg", baos);
				baos.flush();
				byte[] imageInByte = baos.toByteArray();
				baos.close();
				return imageInByte;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return null;
	}

	private Image scaleMaintainAspectRatio(Image image) {

		return image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	}

	@Override
	public void paintComponent(Graphics g) {

		if (own) {
			maxTime.setForeground(cm.getSetting().getColorOwnMessageFont());
			currentTime.setForeground(cm.getSetting().getColorOwnMessageFont());
		} else {
			maxTime.setForeground(cm.getSetting().getColorContactMessageFont());
			currentTime.setForeground(cm.getSetting().getColorContactMessageFont());
		}

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (own) {
			g2d.setColor(cm.getSetting().getColorOwnMessages());
			g2d.fillRoundRect(0, 1, getWidth() - 1, getHeight() - 1, 20, 20);
		} else {
			g2d.setColor(cm.getSetting().getColorContactMessages());
			g2d.fillRoundRect(0, 1, getWidth() - 1, getHeight() - 1, 20, 20);
		}

		g2d.setColor(Color.WHITE);
		g2d.fillRoundRect(9, 3, img.getWidth(this) + 1, img.getHeight(this) + 2, 8, 8);
		g2d.drawImage(img, 10, 5, this);
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(9, 3, img.getWidth(this) + 1, img.getHeight(this) + 2, 8, 8);
	}

	public void setPaused(boolean paused) {

		this.paused = paused;
		start.repaint();
	}
	// -------------------------------------------------------------------------
	// -------------- CUSTOM SLIDER UI CLASS -----------------------------------
	// -------------------------------------------------------------------------

	private class CustomSliderUI extends BasicSliderUI {
		private BasicStroke stroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f,
				new float[] {1f, 2f}, 0f);

		public CustomSliderUI(JSlider b) {

			super(b);
		}

		@Override
		public void paint(Graphics g, JComponent c) {

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			super.paint(g, c);
		}

		@Override
		protected Dimension getThumbSize() {

			return new Dimension(12, 16);
		}

		@Override
		public void paintTrack(Graphics g) {

			Graphics2D g2d = (Graphics2D) g;
			Stroke old = g2d.getStroke();
			g2d.setStroke(stroke);
			g2d.setPaint(Color.BLACK);
			if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
				g2d.drawLine(trackRect.x, trackRect.y + trackRect.height / 2, trackRect.x + trackRect.width,
						trackRect.y + trackRect.height / 2);
			} else {
				g2d.drawLine(trackRect.x + trackRect.width / 2, trackRect.y, trackRect.x + trackRect.width / 2,
						trackRect.y + trackRect.height);
			}
			g2d.setStroke(old);
		}

		@Override
		public void paintThumb(Graphics g) {

			Graphics2D g2d = (Graphics2D) g;
			int x1 = thumbRect.x + 2;
			int x2 = thumbRect.x + thumbRect.width - 2;
			int width = thumbRect.width - 4;
			int topY = thumbRect.y + thumbRect.height / 2 - thumbRect.width / 3;
			GeneralPath shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
			shape.moveTo(x1, topY);
			shape.lineTo(x2, topY);
			shape.lineTo((x1 + x2) / 2, topY + width);
			shape.closePath();
			g2d.setPaint(new Color(81, 83, 186));
			g2d.fill(shape);
			Stroke old = g2d.getStroke();
			g2d.setStroke(new BasicStroke(2f));
			g2d.setPaint(new Color(131, 127, 211));
			g2d.draw(shape);
			g2d.setStroke(old);
		}
	}

}
