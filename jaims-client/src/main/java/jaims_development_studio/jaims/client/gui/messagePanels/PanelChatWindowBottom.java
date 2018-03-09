package jaims_development_studio.jaims.client.gui.messagePanels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.recordingFrame.RecordingWindow;

public class PanelChatWindowBottom extends JPanel {

	private PanelChat		panelChat;
	private GUIMain			guiMain;
	private JPanel			panelIcons;
	private JLabel			lblRecord, lblSend;
	private JTextArea		jta;
	private PanelTextField	panelTextField;
	private boolean			shiftPressed		= false;
	private Image			imgRecord, imgSend;
	private Timer			timerSend, timerRecord;
	private boolean			animationRunning	= false, sendIsShowing = false;

	public PanelChatWindowBottom(PanelChat panelChat, GUIMain guiMain) {

		this.panelChat = panelChat;
		this.guiMain = guiMain;
		initGUI();
	}

	private void initGUI() {

		imgRecord = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/mic.png"))
				.getScaledInstance(34, 34, Image.SCALE_SMOOTH);
		imgSend = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/Jaims_Send.png"))
				.getScaledInstance(34, 22, Image.SCALE_SMOOTH);

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(new EmptyBorder(0, 8, 3, 8));
		setBackground(new Color(0, 0, 0, 0));
		setOpaque(false);

		jta = new JTextArea("");
		jta.setFont(new Font(guiMain.getSettings().getOwnFontName(), Font.PLAIN, 12));
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		jta.setBackground(Color.WHITE);
		jta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (jta.getText().equals(""))
					showRecordIcon();
				else {
					if (sendIsShowing == false) {
						showSendIcon();
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (shiftPressed) {
						jta.setText(jta.getText() + "\n");
					} else {
						if (jta.getText().equals("") || jta.getText().equals("[\\s]*") || jta.getText().equals("[\\n]*")
								|| jta.getText().trim().isEmpty()) {
						} else {

							sendSendable(jta.getText().trim());
							panelChat.addNewUserMessage(jta.getText().trim());
							jta.setText("");
							showRecordIcon();
						}
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_SHIFT)
					shiftPressed = false;

			}

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_SHIFT)
					shiftPressed = true;

			}
		});

		panelTextField = new PanelTextField(jta, panelChat);
		add(panelTextField);

		panelIcons = new JPanel();
		panelIcons.setBackground(new Color(0, 0, 0, 0));
		panelIcons.setOpaque(false);
		panelIcons.setPreferredSize(new Dimension(35, 35));
		panelIcons.setMinimumSize(new Dimension(35, 35));
		panelIcons.setLayout(null);
		{
			lblRecord = new JLabel(new ImageIcon(imgRecord));
			lblRecord.setBackground(new Color(0, 0, 0, 0));
			lblRecord.setOpaque(false);
			lblRecord.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lblRecord.setPreferredSize(new Dimension(34, 34));
			lblRecord.setMaximumSize(new Dimension(34, 34));
			lblRecord.setBounds(0, 0, 34, 34);
			lblRecord.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {

					RecordingWindow rw = new RecordingWindow(guiMain, panelChat);

				}
			});
			panelIcons.add(lblRecord);

			lblSend = new JLabel(new ImageIcon(imgSend));
			lblSend.setBackground(new Color(0, 0, 0, 0));
			lblSend.setOpaque(false);
			lblSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lblSend.setPreferredSize(new Dimension(34, 34));
			lblSend.setMaximumSize(new Dimension(34, 34));
			lblSend.setBounds(40, 0, 34, 34);
			lblSend.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {

					if (jta.getText().equals("") || jta.getText().equals("[\\s]*") || jta.getText().equals("[\\n]*")
							|| jta.getText().trim().isEmpty()) {
					} else {

						sendSendable(jta.getText());
						panelChat.addNewUserMessage(jta.getText());
						jta.setText("");
						showRecordIcon();
					}

				}
			});
			panelIcons.add(lblSend);

		}
		panelIcons.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				lblRecord.setBounds(lblRecord.getX(), getHeight() / 2 - 17, 34, 34);
				lblSend.setBounds(lblSend.getX(), getHeight() / 2 - 17, 34, 34);
				panelIcons.repaint();
				revalidate();
			}
		});
		add(Box.createHorizontalGlue());
		add(panelIcons);
		add(Box.createRigidArea(new Dimension(0, 2)));

		guiMain.getJaimsFrame().addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				revalidate();
				repaint();

			}
		});

	}

	private void showSendIcon() {

		if (animationRunning == false) {
			animationRunning = true;
			timerSend = new Timer(5, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					int xRecord = lblRecord.getX() - 2;
					int xSend = lblSend.getX() - 2;

					if (xRecord >= -38)
						lblRecord.setBounds(xRecord, lblRecord.getY(), 34, 34);

					if (xSend >= 2)
						lblSend.setBounds(xSend, lblSend.getY(), 34, 34);

					if (xSend == 0) {
						((Timer) timerSend).stop();
						panelIcons.revalidate();
						panelIcons.repaint();
						revalidate();
						repaint();
						animationRunning = false;
						sendIsShowing = true;
					}
				}
			});
			timerSend.start();
		}
	}

	private void showRecordIcon() {

		if (animationRunning == false) {
			animationRunning = true;
			timerSend = new Timer(5, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					int xRecord = lblRecord.getX() + 2;
					int xSend = lblSend.getX() + 2;

					if (xSend <= 40)
						lblSend.setBounds(xSend, lblSend.getY(), 34, 34);

					if (xRecord <= 38)
						lblRecord.setBounds(xRecord, lblRecord.getY(), 34, 34);

					if (xRecord == 0) {
						((Timer) timerSend).stop();
						panelIcons.revalidate();
						panelIcons.repaint();
						revalidate();
						repaint();
						animationRunning = false;
						sendIsShowing = false;
					}
				}
			});
			timerSend.start();
		}
	}

	private void sendSendable(String message) {

		TextMessage tm = new TextMessage(guiMain.getUserUUID(), panelChat.getContactID(), message);
		SendableMessage sm = new SendableMessage(tm);
		guiMain.sendSendable(sm);
	}

}
