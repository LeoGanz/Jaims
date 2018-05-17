package jaims_development_studio.jaims.client.gui.customGUIComponents.messages;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.RoundBorder;
import jaims_development_studio.jaims.client.logic.PlayAudio;

/**
 * This class is a graphical representation of a voice message. It displays the
 * sender's profile picture, a start/stop button, a slider indicating the clip's
 * current position, which means how much of the audio has already been played,
 * and a label displaying the clip's current position in a text format. On
 * further notice one has to know that a voice message has a fixed size which
 * fits into the frame's minimum size and isn't resized accordingly with the
 * frame.
 * 
 * @author Bu88le
 *
 * @since v0.1.0
 * 
 * @see #VoiceMessage(UUID, String, boolean, GUIMain)
 */
@SuppressWarnings("deprecation")
public class VoiceMessage extends JPanel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static final Logger	LOG					= LoggerFactory.getLogger(VoiceMessage.class);

	private String				path;
	private JLabel				currentTime, maxTime;
	private boolean				own, paused = true;
	private Image				img;
	private JSlider				slider;
	private Thread				thread;
	private JPanel				start, backgroundPanel;
	private PlayAudio			pa;
	private GUIMain				guiMain;

	/**
	 * The constructor of this class; responsible for initialising the fields and
	 * afterwards calling the {@link #initGUI(UUID)} method.
	 * 
	 * @param uuid
	 *            The sender's UUID
	 * @param pathToFile
	 *            the path to the location on the disk where the audio file is saved
	 * @param own
	 *            boolean that indicates whether the voice message is from the user
	 *            or from a contact
	 * @param guiMain
	 *            a reference to the GUIMain class
	 * 
	 * @see UUID
	 * @see GUIMain
	 */
	public VoiceMessage(UUID uuid, String pathToFile, boolean own, GUIMain guiMain) {

		path = pathToFile;
		this.own = own;
		this.guiMain = guiMain;

		try {
			initGUI(uuid);
		} catch (NullPointerException npe) {
			LOG.error("UUID was null", npe);
		}

	}

	/**
	 * This method is responsible for building everything needed to display the
	 * voice message properly. It also initialises the parent's panel's main fields
	 * like the layout, the preferredSize, the MaximumSize and the border.
	 * </p>
	 * Calls following methods:
	 * <ul>
	 * <li>{@link #buildBackgroundPanel()}</li>
	 * <li>{@link #buildStartStopPanel()}</li>
	 * <li>{@link #buildTimeSlider()}</li>
	 * <li>{@link #addTimeLabel()}</li>
	 * </ul>
	 * 
	 * @param uuid
	 *            The voice message's sender
	 */
	private void initGUI(UUID uuid) {

		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setPreferredSize(new Dimension(350, 50));
		setMaximumSize(getPreferredSize());

		// Sets the border of the voice message based on the sender
		if (own)
			setBorder(new RoundBorder(330, 50, new Color(guiMain.getSettings().getColorContactMessageBorder())));
		else
			setBorder(new RoundBorder(330, 50, new Color(guiMain.getSettings().getColorContactMessageBorder())));

		buildBackgroundPanel();
		buildStartStopPanel();
		buildTimeSlider();

		add(Box.createRigidArea(new Dimension(65, 0)));
		add(backgroundPanel);
		add(Box.createRigidArea(new Dimension(8, 0)));
		add(Box.createHorizontalGlue());

		addTimeLabel();

		img = scaleMaintainAspectRatio(guiMain.getProfileImage(uuid));
		pa = new PlayAudio(path, currentTime, slider, backgroundPanel);

	}

	/**
	 * Builds the background panel which holds all other GUI relevant objects like
	 * the slider. The background panel overrides its <code>paintComponent</code>
	 * method in order to be able to draw its corners rounded. It then sets the
	 * panel's layout, preferred and maximum size.
	 */
	private void buildBackgroundPanel() {

		backgroundPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				if (own) {
					g2d.setColor(new Color(guiMain.getSettings().getColorOwnMessages()));
					g2d.fillRect(0, 0, getWidth(), getHeight());
				} else {
					g2d.setColor(new Color(guiMain.getSettings().getColorContactMessages()));
					g2d.fillRect(0, 0, getWidth(), getHeight());
				}

				g2d.setColor(Color.BLACK);
				g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
				g2d.setColor(Color.WHITE);
				g2d.fillRoundRect(2, 2, getWidth() - 3, getHeight() - 3, 20, 20);
			}
		};
		backgroundPanel.setOpaque(false);
		backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.LINE_AXIS));
		backgroundPanel.setPreferredSize(new Dimension(176, 42));
		backgroundPanel.setMaximumSize(backgroundPanel.getPreferredSize());
	}

	/**
	 * This methods builds a JPanel which displays a start button that changes to a
	 * stop button once it is clicked. It is used for controlling the audio's
	 * playback status. It also sets the panel's default settings which include the
	 * preferred and maximum size, the cursor and adds a mouse Listener.
	 * 
	 * @see Graphics2D
	 * @see BoxLayout
	 * @see Dimension
	 * @see Cursor
	 * @see MouseAdapter
	 */
	private void buildStartStopPanel() {

		backgroundPanel.add(Box.createRigidArea(new Dimension(5, 42)));

		start = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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

				/*
				 * If the button has been clicked before, it's displaying the play symbol which
				 * means when clicking it again it will stop playing back. If it hasn't been
				 * clicked before the boolean 'paused' is 'true' which means that on a clicking
				 * event the playback will be started. In it's default state, which it displays
				 * upon opening the chart for the first time after starting the program,
				 * 'paused' is set to true which means that the play button, a triangle, is
				 * shown.
				 */
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
		backgroundPanel.add(start);
		backgroundPanel.add(Box.createRigidArea(new Dimension(5, 0)));
	}

	/**
	 * This method is responsible for creating a <code>JSlider</code> which
	 * indicates the current playback position of the audio while it is played. It
	 * initialises the JSlider with the minimum value of 0, a maximum value of the
	 * audio file's length in seconds, a preferred size of width = 150 and height =
	 * 40 and a custom Component UI.
	 * 
	 * @see JSlider
	 * @see CustomSliderUI
	 * 
	 */
	private void buildTimeSlider() {

		long length = getAudioFileLength();

		slider = new JSlider(0, (int) (length / 1000), 0);
		slider.setPreferredSize(new Dimension(150, 40));
		slider.setUI(new CustomSliderUI(slider));
		slider.setOpaque(false);
		backgroundPanel.add(slider);
		backgroundPanel.add(Box.createRigidArea(new Dimension(5, 0)));
	}

	/**
	 * This method creates a JLabel which displays the current playback time and the
	 * length of the whole audio file in the format 'current time/ length'. </br>
	 * For example: '0:20/3:48' indicates that 20 seconds of the 3:48 minutes long
	 * audio have been played
	 * </p>
	 * The label is then added next to the JSlider.
	 * 
	 * @see JLabel
	 * @see SimpleDateFormat
	 * @see Date
	 */
	private void addTimeLabel() {

		long length = getAudioFileLength();

		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		Date d = new Date(0L);
		currentTime = new JLabel(sdf.format(d) + " / ", JLabel.CENTER);
		add(currentTime);

		d.setTime(length);
		maxTime = new JLabel(sdf.format(d));
		add(maxTime);

	}

	/**
	 * This method calculates the audio file's length by dividing the number of the
	 * file's sample frame's by the file's frame rate. The frame rate specifies the
	 * number of frames per second, e.g. a sample rate of 44100 Hz = 44.1 kHz
	 * contains 44100 samples of any size per elapsed second. </br>
	 * Example case:
	 * <ul>
	 * <li>The audio file contains 3,840,000 samples</li>
	 * <li>The audio file has a sample rate of 48000 Hz = 48000 samples per second
	 * </li>
	 * <li>Dividing the samples by the sample rate: 3,840,000/48000 = 80</li>
	 * <li>Audio file has a length of 80 s = 1 minute, 20 seconds</li>
	 * </ul>
	 * </br>
	 * If the audio file or the audio file's format isn't supported then an error is
	 * thrown and the method returns 0.
	 * 
	 * @return the audio file's length in milliseconds
	 */
	private long getAudioFileLength() {

		AudioFileFormat format;
		try {
			format = AudioSystem.getAudioFileFormat(new File(path));
			float duration = format.getFrameLength() / format.getFormat().getFrameRate();
			return (long) (duration * 1000);
		} catch (UnsupportedAudioFileException | IOException e) {
			LOG.error("Unsupported file", e);
			return 0;
		}

	}

	/**
	 * This method resizes a given image to the size of width=40px and height=40px.
	 * 
	 * @param image
	 *            The image to be scaled
	 * @return The scaled image
	 */
	private Image scaleMaintainAspectRatio(Image image) {

		return image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	}

	@Override
	public void paintComponent(Graphics g) {

		if (own) {
			maxTime.setForeground(new Color(guiMain.getSettings().getColorOwnMessageFont()));
			currentTime.setForeground(new Color(guiMain.getSettings().getColorOwnMessageFont()));
		} else {
			maxTime.setForeground(new Color(guiMain.getSettings().getColorContactMessageFont()));
			currentTime.setForeground(new Color(guiMain.getSettings().getColorContactMessageFont()));
		}

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (own) {
			g2d.setColor(new Color(guiMain.getSettings().getColorOwnMessages()));
			g2d.fillRoundRect(0, 1, getWidth() - 1, getHeight() - 1, 20, 20);
		} else {
			g2d.setColor(new Color(guiMain.getSettings().getColorContactMessages()));
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

	/**
	 * This class paints a custom Thumb and Track for the JSlider. Inspiration was
	 * taken from the Internet from an unknown user's suggestion.
	 * 
	 * @author Bu88le
	 *
	 */
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
