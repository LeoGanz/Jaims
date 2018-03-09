package jaims_development_studio.jaims.client.gui.recordingFrame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.messagePanels.PanelChat;
import jaims_development_studio.jaims.client.logic.RecordAudio;

public class RecordingWindow extends JWindow {

	private GUIMain		guiMain;
	private int			translucency	= 255;
	private boolean		appearing		= false, mouseOnClose = false, clicked = false, paintTime = false,
			mouseOnSend = false, paintClick = false;;
	private StopButton	sb;
	private PauseButton	pb;
	private String		time			= "";
	private JPanel		centerpanel;
	private long		startTime, startPause = 0, endPause = 0;
	private Timer		timer;
	private RecordAudio	ra;
	private Image		sendImg;

	public RecordingWindow(GUIMain guiMain, PanelChat panelChat) {

		this.guiMain = guiMain;
		ra = new RecordAudio(guiMain);

		initGUI(panelChat);
	}

	private void initGUI(PanelChat panelChat) {

		sendImg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/Jaims_Send.png"))
				.getScaledInstance(30, 20, Image.SCALE_SMOOTH);

		setBackground(new Color(0, 0, 0, 0));
		setSize(new Dimension(0, 0));
		setLayout(new BorderLayout());
		setLocationRelativeTo(panelChat);
		setAlwaysOnTop(true);

		JPanel rb = new JPanel();
		rb.setLayout(new BoxLayout(rb, BoxLayout.LINE_AXIS));

		JPanel recordingButton = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(255, 0, 0, translucency));
				g2.fillOval(getWidth() / 2 - (getWidth() - 2) / 2, getHeight() / 2 - (getWidth() - 2) / 2,
						(getWidth() - 2), (getWidth() - 2));
				g2.dispose();
			}
		};
		recordingButton.setPreferredSize(new Dimension(52, 52));
		recordingButton.setMaximumSize(new Dimension(52, 52));
		recordingButton.setFocusable(true);
		recordingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		recordingButton.setIgnoreRepaint(true);
		recordingButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				recordingButton.setPreferredSize(new Dimension(46, 46));
				recordingButton.setMaximumSize(new Dimension(46, 46));
				rb.revalidate();
				centerpanel.revalidate();
				revalidate();
				repaint();

				Thread t = new Thread(ra);
				t.start();

				timer = new Timer(25, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						if (appearing) {
							if (translucency > 80)
								translucency -= 5;
							else {
								appearing = false;
							}
						} else {
							if (translucency < 255)
								translucency += 5;
							else
								appearing = true;
						}

						recordingButton.repaint();

					}
				});
				timer.start();

				Thread thread = new Thread() {
					@Override
					public void run() {

						startTime = System.currentTimeMillis();
						paintTime = true;

						SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
						while (timer.isRunning()) {
							time = sdf.format(
									new Date((System.currentTimeMillis() - (endPause - startPause)) - startTime));
							centerpanel.repaint(0, 0, centerpanel.getWidth(), 30);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						paintTime = false;
					}
				};
				thread.start();

			}

			@Override
			public void mouseReleased(MouseEvent e) {

				recordingButton.setPreferredSize(new Dimension(52, 52));
				recordingButton.setMaximumSize(new Dimension(52, 52));
				rb.revalidate();
				centerpanel.revalidate();
				revalidate();
				repaint();
			}
		});
		rb.add(Box.createHorizontalGlue());
		rb.add(recordingButton);
		rb.add(Box.createHorizontalGlue());

		centerpanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.WHITE);
				g2.fillRoundRect(0, 0, 400, 160, 60, 60);

				if (paintClick) {
					g2.setColor(Color.GRAY);
					g2.fillRoundRect(getWidth() - 45, getHeight() - 30, 40, 28, 60, 60);
				}
				g2.drawImage(sendImg, getWidth() - 40, getHeight() - 25, this);

				if (paintTime) {
					g2.setColor(Color.BLACK);
					g2.setFont(new Font("Sans Serif", Font.BOLD, 14));
					g2.drawString(time, getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth(time) / 2, 20);
				}

				if (mouseOnClose) {
					g2.setColor(Color.RED);
					g2.setStroke(new BasicStroke(1.8F));
					g2.drawLine(360, 15, 375, 30);
					g2.drawLine(360, 30, 375, 15);
				} else {
					g2.setColor(Color.GRAY);
					g2.drawLine(360, 15, 375, 30);
					g2.drawLine(360, 30, 375, 15);
				}
				g2.dispose();

				recordingButton.repaint();
				sb.repaint();
				pb.repaint();
			}
		};
		centerpanel.setOpaque(false);
		centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.PAGE_AXIS));
		centerpanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (mouseOnClose) {
					dispose();
				}

				if (mouseOnSend) {
					paintClick = true;
					centerpanel.repaint();
				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {

				if (mouseOnSend) {
					paintClick = false;
					centerpanel.repaint();
				}
			}
		});
		centerpanel.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {

				if (e.getX() >= 360 && e.getX() <= 375 && e.getY() >= 15 && e.getY() <= 30) {
					mouseOnClose = true;
					centerpanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
					centerpanel.repaint(355, 10, 30, 30);
				} else {
					if (mouseOnClose) {
						mouseOnClose = false;
						centerpanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
						centerpanel.repaint(355, 10, 30, 30);
					}
				}

				if (e.getX() >= 360 && e.getX() <= 390 && e.getY() >= 130 && e.getY() <= 160) {
					mouseOnSend = true;
					centerpanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					if (mouseOnSend) {
						mouseOnSend = false;
						centerpanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}

			}
		});

		centerpanel.add(Box.createVerticalGlue());
		centerpanel.add(rb);
		centerpanel.add(Box.createVerticalGlue());

		JPanel buttons = new JPanel();
		buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.setBackground(Color.white);
		buttons.setOpaque(false);
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		sb = new StopButton();
		sb.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				ra.stopRecording();

				time = "";
				paintTime = false;
				if (timer != null)
					if (timer.isRunning())
						timer.stop();
				translucency = 255;
				recordingButton.repaint();
				centerpanel.repaint();
			}
		});
		pb = new PauseButton();
		buttons.add(Box.createHorizontalGlue());
		buttons.add(sb);
		buttons.add(Box.createRigidArea(new Dimension(5, 0)));
		buttons.add(pb);
		buttons.add(Box.createHorizontalGlue());
		centerpanel.add(buttons);

		add(centerpanel, BorderLayout.CENTER);
		setVisible(true);
		Timer t = new Timer(5, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (getWidth() < 400) {
					setSize(new Dimension(getWidth() + 20, getHeight() + 8));
					setLocationRelativeTo(panelChat);
					repaint();
				} else {
					((Timer) e.getSource()).stop();
				}

			}
		});
		t.start();
	}

}
