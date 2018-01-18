package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.logic.RecordAudio;
import jaims_development_studio.jaims.client.settings.Settings;

public class RecordingFrame extends JWindow {

	boolean					paused			= false, recording = false;
	TargetDataLine			line;
	byte[]					message;
	int						NumBytesRead;
	ByteArrayOutputStream	out				= new ByteArrayOutputStream();
	JLabel					time;
	JPanel					centerpanel;
	long					pauseStarted	= 0, pauseEnded = 0, timeStarted;
	Thread					thread			= null;
	RecordAudio				ra				= null;

	public RecordingFrame(PanelChat activePanelChat, ChatObject co) {

		super();
		initGUI(activePanelChat, co);
	}

	private void initGUI(PanelChat activePanelChat, ChatObject co) {

		setSize(new Dimension(400, 200));
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		centerpanel = new JPanel();
		centerpanel.setLayout(new BorderLayout(5, 5));
		centerpanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(5, 5, 5, 5)));

		JWindow window = this;

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		{
			JPanel stopRecording = new JPanel() {

				@Override
				public void paintComponent(Graphics g) {

					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fillOval(0, 0, 40, 40);

					g2d.setColor(Color.BLACK);
					g2d.fillRect(9, 9, 22, 22);
				}

			};
			stopRecording.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {

					if (line != null && recording == true) {
						ra.stopRecording();
						try {
							thread.join();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					recording = false;
					paused = false;
					window.dispose();
				}
			});
			stopRecording.setPreferredSize(new Dimension(40, 40));
			stopRecording.setMaximumSize(stopRecording.getPreferredSize());
			stopRecording.setOpaque(false);
			stopRecording.setCursor(new Cursor(Cursor.HAND_CURSOR));
			panel.add(Box.createHorizontalGlue());
			panel.add(Box.createHorizontalGlue());
			panel.add(stopRecording);

			JPanel pauseRecording = new JPanel() {

				@Override
				public void paintComponent(Graphics g) {

					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fillOval(0, 0, 40, 40);

					g2d.setColor(Color.black);
					if (paused) {
						int[] x = {9, 32, 9};
						int[] y = {9, 20, 31};
						g2d.fillPolygon(x, y, 3);
					} else {
						g2d.fillRect(9, 9, 8, 22);
						g2d.fillRect(23, 9, 8, 22);
					}
				}
			};
			pauseRecording.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {

					if (paused) {
						paused = false;
						if (recording == false && line != null) {
							recording = true;
							pauseEnded = System.currentTimeMillis();
							ra.restartRecording();
							record();

						}
					} else {
						paused = true;
						if (recording && line != null) {
							ra.pauseRecording();
							pauseStarted = System.currentTimeMillis();
							recording = false;
						}

					}
				}
			});
			pauseRecording.setPreferredSize(new Dimension(40, 40));
			pauseRecording.setMaximumSize(pauseRecording.getPreferredSize());
			pauseRecording.setOpaque(true);
			pauseRecording.setCursor(new Cursor(Cursor.HAND_CURSOR));
			panel.add(Box.createRigidArea(new Dimension(30, 0)));
			panel.add(pauseRecording);
			panel.add(Box.createHorizontalGlue());
			panel.add(Box.createHorizontalGlue());

			PanelSend ps = new PanelSend();
			ps.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {

					if (ra != null) {
						ra.sendRecording();
					}
					recording = false;
					paused = false;
					if (thread != null) {
						try {
							thread.join();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					activePanelChat.getPCM().addVoiceMessageFromUser(ra.getPath(),
							activePanelChat.getPCM().getUserProfile().getUuid(), co);
					window.dispose();
				}
			});
			ps.setCursor(new Cursor(Cursor.HAND_CURSOR));
			panel.add(ps);

		}
		centerpanel.add(panel, BorderLayout.PAGE_END);

		time = new JLabel("", JLabel.CENTER);
		time.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		centerpanel.add(time, BorderLayout.CENTER);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));

		JPanel recordPanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(Color.RED);
				g2d.fillOval(5, 5, 51, 50);
				g2d.setColor(Color.BLACK);
				g2d.drawOval(0, 0, 60, 60);
			}
		};
		recordPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				if (recording == false) {
					try {
						line = (TargetDataLine) AudioSystem
								.getLine(new DataLine.Info(TargetDataLine.class, Settings.getInputFormat()));
						line.addLineListener(new LineListener() {

							@Override
							public void update(LineEvent arg0) {

								if (arg0.getType() == LineEvent.Type.OPEN && thread == null) {
									thread = new Thread(ra = new RecordAudio(line));
									recording = true;
									timeStarted = System.currentTimeMillis();
									thread.start();
									record();
								}

							}
						});
						line.flush();
						line.open(Settings.getInputFormat());

					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					line.stop();
					line.flush();
					recording = false;
				}
			}
		});

		recordPanel.setPreferredSize(new Dimension(62, 62));
		recordPanel.setMaximumSize(recordPanel.getPreferredSize());
		recordPanel.setOpaque(true);
		recordPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		p.add(Box.createHorizontalGlue());
		p.add(recordPanel);
		p.add(Box.createHorizontalGlue());

		centerpanel.add(p, BorderLayout.PAGE_START);
		add(centerpanel, BorderLayout.CENTER);
	}

	private void record() {

		SimpleDateFormat df = new SimpleDateFormat("mm:ss");
		Date dt = new Date();
		while (recording) {
			long currentTime = System.currentTimeMillis();
			long diff = ((currentTime - (pauseEnded - pauseStarted)) - timeStarted);

			dt.setTime(diff);
			time.setText(df.format(dt));
			time.repaint();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
