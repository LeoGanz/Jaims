package jaims_development_studio.jaims.client.gui.recordingFrame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.IOUtils;

import jaims_development_studio.jaims.api.message.VoiceMessage;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.audio.AudioWaveformCreator;
import jaims_development_studio.jaims.client.gui.messagePanels.PanelChat;

public class AfterRecordingWindow extends JWindow {

	private GUIMain			guiMain;
	private PanelChat		panelChat;
	private File			recordFile;
	private boolean			paused		= true, waveFormSet = false;
	private JSlider			slider;
	private BufferedImage	bim;
	private JPanel			waveForm;
	private Clip			audioClip;

	private int				waveFormX	= -1, waveFormSetX = -1, currentPosition = 0;

	public AfterRecordingWindow(GUIMain guiMain, PanelChat panelChat, File recordFile) {

		this.guiMain = guiMain;
		this.panelChat = panelChat;
		this.recordFile = recordFile;

	}

	public void initGUI() {

		setBackground(new Color(0, 0, 0, 0));
		setSize(new Dimension(400, 160));
		setLayout(new BorderLayout());
		setLocationRelativeTo(panelChat);
		setAlwaysOnTop(true);

		JPanel center = new JPanel() {

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.WHITE);
				g2.fillRoundRect(0, 0, 400, 160, 60, 60);
			}

		};
		center.setBackground(Color.WHITE);
		center.setLayout(new BorderLayout(0, 10));
		center.setPreferredSize(new Dimension(400, 160));
		center.setMaximumSize(center.getPreferredSize());
		center.setMinimumSize(center.getPreferredSize());
		center.setBorder(new EmptyBorder(5, 0, 5, 0));

		JPanel c = new JPanel();
		c.setBackground(Color.WHITE);
		c.setLayout(new BoxLayout(c, BoxLayout.LINE_AXIS));
		JPanel startButton = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.GRAY);
				g2.fillOval(0, 0, 30, 31);

				if (paused) {
					int[] xPoints = {8, 8, 24};
					int[] yPoints = {7, 24, 15};

					g2.setColor(Color.WHITE);
					g2.fillPolygon(xPoints, yPoints, 3);
				} else {
					g2.setColor(Color.WHITE);
					g2.fillRect(7, 7, 5, 16);
					g2.fillRect(17, 7, 5, 16);
				}
			}
		};
		startButton.setPreferredSize(new Dimension(30, 31));
		startButton.setMaximumSize(new Dimension(30, 31));
		startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		startButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (paused) {
					play();
					paused = false;
					startButton.repaint();
				} else {
					pausePlayback();
					paused = true;
					startButton.repaint();
				}

			}
		});
		c.add(Box.createHorizontalGlue());
		c.add(startButton);
		c.add(Box.createRigidArea(new Dimension(10, 0)));

		try {
			AudioWaveformCreator awc = new AudioWaveformCreator(recordFile, "waveFile2");
			bim = awc.createAudioInputStream();
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		waveForm = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2.drawImage(bim.getSubimage(0, 15, bim.getWidth(), bim.getHeight() - 15), 0,
						(getHeight() / 2) - ((bim.getHeight() - 15) / 2), null);

				if (waveFormSet) {
					g2.setColor(Color.BLUE);
					g2.setStroke(new BasicStroke(1.5F));
					g2.drawLine(waveFormSetX, 0, waveFormSetX, getHeight());
				}

				if (waveFormX > 0) {
					g2.setColor(Color.CYAN);
					g2.setStroke(new BasicStroke(1.3F));
					g2.drawLine(waveFormX, 0, waveFormX, getHeight());
				}

				if (paused == false) {
					g2.setColor(Color.PINK);
					g2.setStroke(new BasicStroke(1.3F));
					g2.drawLine(currentPosition, 0, currentPosition, getHeight());
				}

			}
		};
		waveForm.setMinimumSize(new Dimension(bim.getWidth(), bim.getHeight() - 30));
		waveForm.setPreferredSize(waveForm.getMinimumSize());
		waveForm.setMaximumSize(waveForm.getMinimumSize());
		waveForm.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {

				waveFormX = -1;
				waveForm.repaint();

			}

			@Override
			public void mousePressed(MouseEvent e) {

				if (e.getX() < bim.getWidth()) {
					waveFormSet = true;
					waveFormSetX = e.getX();
					waveForm.repaint();
				}
			}
		});
		waveForm.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {

				if (e.getX() <= bim.getWidth()) {
					waveFormX = e.getX();
					waveForm.repaint();
				}

			}
		});

		JFrame f = new JFrame();
		f.add(new JLabel(new ImageIcon(bim.getScaledInstance(200, 180, Image.SCALE_SMOOTH))));
		f.setVisible(true);

		JScrollPane jsp = new JScrollPane(waveForm, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setMinimumSize(new Dimension(200, 90));
		jsp.setPreferredSize(new Dimension(200, 90));
		jsp.setMaximumSize(new Dimension(200, 90));
		c.add(jsp);
		c.add(Box.createHorizontalGlue());

		center.add(c);

		JLabel lbl = new JLabel(recordFile.getName(), JLabel.CENTER);
		lbl.setBackground(Color.WHITE);
		lbl.setForeground(Color.BLACK);
		lbl.setFont(new Font("Sans Serif", Font.BOLD, 16));
		center.add(lbl, BorderLayout.PAGE_START);

		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
		bottom.setOpaque(false);
		bottom.setBackground(new Color(0, 0, 0, 0));

		Image img = Toolkit.getDefaultToolkit()
				.getImage(getClass().getClassLoader().getResource("images/Jaims_Send.png"))
				.getScaledInstance(30, 25, Image.SCALE_SMOOTH);

		JLabel send = new JLabel(new ImageIcon(img));
		send.setOpaque(false);
		send.setCursor(new Cursor(Cursor.HAND_CURSOR));
		send.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				panelChat.addNewUserVoiceMessage(recordFile.getAbsolutePath());
				sendVoiceMessage();
				dispose();

			}
		});

		Image trash = Toolkit.getDefaultToolkit()
				.getImage(getClass().getClassLoader().getResource("images/trash_bin.png"))
				.getScaledInstance(18, 30, Image.SCALE_SMOOTH);
		JLabel trashBin = new JLabel(new ImageIcon(trash));
		trashBin.setOpaque(false);
		trashBin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		trashBin.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				recordFile.delete();
				dispose();

			}
		});

		bottom.add(Box.createHorizontalGlue());
		bottom.add(trashBin);
		bottom.add(Box.createRigidArea(new Dimension(25, 0)));
		bottom.add(send);
		bottom.add(Box.createHorizontalGlue());
		center.add(bottom, BorderLayout.PAGE_END);

		add(center, BorderLayout.CENTER);
		setVisible(true);

	}

	private long getAudioFileLength() {

		AudioFileFormat format;
		float duration = 0;
		try {
			format = AudioSystem.getAudioFileFormat(recordFile);
			duration = format.getFrameLength() / format.getFormat().getFrameRate();
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (long) (duration * 1000);

	}

	private void play() {

		try {
			Mixer info = guiMain.getListAudioDevices().getSystemOutputMixer();
			audioClip = AudioSystem.getClip(info.getMixerInfo());
			AudioInputStream ais = AudioSystem.getAudioInputStream(recordFile);
			audioClip.open(ais);
			audioClip.start();

			Timer t = new Timer(100, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					currentPosition += 1;
					waveForm.repaint();

					if (currentPosition == bim.getWidth())
						((Timer) e.getSource()).stop();

				}
			});
			t.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void pausePlayback() {

	}

	private void sendVoiceMessage() {

		try {
			VoiceMessage vm = new VoiceMessage(guiMain.getUserUUID(), panelChat.getContactID(),
					IOUtils.toByteArray(new FileInputStream(recordFile)));
			SendableMessage sm = new SendableMessage(vm);
			guiMain.sendSendable(sm);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
