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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;

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
				.getScaledInstance(34, 34, Image.SCALE_SMOOTH);

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

							// sendSendable(jta.getText());
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

		panelTextField = new PanelTextField(jta);
		add(panelTextField);

		panelIcons = new JPanel();
		panelIcons.setBackground(new Color(0, 0, 0, 0));
		panelIcons.setOpaque(false);
		panelIcons.setPreferredSize(new Dimension(35, 35));
		panelIcons.setLayout(null);
		{
			lblRecord = new JLabel(new ImageIcon(imgRecord));
			lblRecord.setBackground(new Color(0, 0, 0, 0));
			lblRecord.setOpaque(false);
			lblRecord.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lblRecord.setPreferredSize(new Dimension(34, 34));
			lblRecord.setMaximumSize(new Dimension(34, 34));
			lblRecord.setBounds(0, 0, 34, 34);
			panelIcons.add(lblRecord);

			lblSend = new JLabel(new ImageIcon(imgSend));
			lblSend.setBackground(new Color(0, 0, 0, 0));
			lblSend.setOpaque(false);
			lblSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lblSend.setPreferredSize(new Dimension(34, 34));
			lblSend.setMaximumSize(new Dimension(34, 34));
			lblSend.setBounds(40, 0, 34, 34);

		}
		panelIcons.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				lblRecord.setBounds(lblRecord.getX(), getHeight() / 2 - 17, 34, 34);
				lblSend.setBounds(lblSend.getX(), getHeight() / 2 - 17, 34, 34);
				panelIcons.repaint();
			}
		});
		add(Box.createHorizontalGlue());
		add(panelIcons);

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
						panelIcons.repaint();
						animationRunning = false;
						sendIsShowing = true;
					}
				}
			});
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

					if (xSend <= -2)
						lblSend.setBounds(xSend, lblSend.getY(), 34, 34);

					if (xRecord <= 38)
						lblRecord.setBounds(xRecord, lblRecord.getY(), 34, 34);

					if (xRecord == 0) {
						((Timer) timerSend).stop();
						panelIcons.revalidate();
						panelIcons.repaint();
						animationRunning = false;
						sendIsShowing = false;
					}
				}
			});
		}
	}

}
