package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.SendableTextMessage;
import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.chatObjects.ClientProfile;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelChat extends ContainerPanel implements Runnable {

	private static final Logger	LOG				= LoggerFactory.getLogger(PanelChat.class);
	JPanel						panelPageEnd;
	public static JScrollPane	jsp, jsp2;
	JTextField					jtf;
	JTextArea					jta;
	PanelChatMessages			pcm;
	PanelChatWindowTop			pcwt;
	ClientProfile				userProfile;
	ChatObject					co;
	ContactPanel				cp;
	int							jtaWidth		= 0, countLinesOld = 1;
	boolean						jspActive		= false;
	boolean						shiftPressed	= false;
	private Image				recordImage;

	public PanelChat(ClientProfile userProfile, PanelChatMessages pcm, ChatObject co, ContactPanel cp) {

		this.userProfile = userProfile;
		this.pcm = pcm;
		this.co = co;
		this.cp = cp;
	}

	private void initGUI() {

		setLayout(new BorderLayout(0, 2));
		setBorder(new LineBorder(Color.BLACK));

		panelPageEnd = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(110, 110, 110, 205));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panelPageEnd.setLayout(new BorderLayout(6, 0));
		{
			jta = new JTextArea("");
			jta.setFont(new Font(cp.getClientMain().getSetting().getOwnFontName(), Font.PLAIN, 12));
			jta.setBorder(new LineBorder(Color.GRAY));
			jta.setLineWrap(true);
			jta.setWrapStyleWord(true);
			jta.setBackground(Color.WHITE);
			jta.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {

					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						if (shiftPressed) {
							jta.setText(jta.getText() + "\n");
						} else {
							if (jta.getText().equals("") || jta.getText().equals("[\\s]*")
									|| jta.getText().equals("[\\n]*") || jta.getText().trim().isEmpty()) {
							} else {
								pcm.addMessageFromUser(jta.getText(), userProfile.getUuid(), co);
								jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum() + 50);
								cp.getClientMain().getPCC().checkChatPanels(cp);

								sendSendable(jta.getText());
								jta.setText("");
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

			PanelTextField ptf = new PanelTextField(jta, this);
			panelPageEnd.add(ptf, BorderLayout.CENTER);

			pcm.getFrame().addComponentListener(new ComponentAdapter() {

				@Override
				public void componentResized(ComponentEvent arg0) {

					// TODO Auto-generated method stub
					jta.revalidate();
					jta.repaint();
				}
			});

			recordImage = Toolkit.getDefaultToolkit()
					.getImage(getClass().getClassLoader().getResource("images/mic.png"));
			recordImage = recordImage.getScaledInstance(44, 45, Image.SCALE_SMOOTH);

			JPanel end = new JPanel();
			end.setOpaque(false);
			end.setLayout(new BoxLayout(end, BoxLayout.LINE_AXIS));
			{
				JLabel record = new JLabel() {
					@Override
					public void paintComponent(Graphics g) {

						super.paintComponent(g);
						g.setColor(new Color(0, 0, 0, 0));
						g.fillRect(0, 0, getWidth(), getHeight());
					}
				};
				record.setIcon(new ImageIcon(recordImage));
				record.setCursor(new Cursor(Cursor.HAND_CURSOR));
				record.setPreferredSize(new Dimension(44, 45));
				record.setMaximumSize(record.getPreferredSize());
				record.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseReleased(MouseEvent e) {

						pcm.getContactPanel().getClientMain().showRecordFrame(co);

					}
				});
				end.add(record);
				end.add(Box.createRigidArea(new Dimension(2, 45)));

				JLabel send = new JLabel() {
					@Override
					public void paintComponent(Graphics g) {

						super.paintComponent(g);
						g.setColor(new Color(0, 0, 0, 0));
						g.fillRect(0, 0, getWidth(), getHeight());
					}
				};
				send.setIcon(new ImageIcon(Toolkit.getDefaultToolkit()
						.getImage(getClass().getClassLoader().getResource("images/Jaims_Send.png"))
						.getScaledInstance(30, 20, Image.SCALE_SMOOTH)));
				send.setCursor(new Cursor(Cursor.HAND_CURSOR));
				send.setPreferredSize(new Dimension(30, 20));
				send.setMaximumSize(send.getPreferredSize());
				send.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseReleased(MouseEvent e) {

						if (jta.getText().equals("")) {
						} else {
							pcm.addMessageFromUser(jta.getText(), userProfile.getUuid(), co);
							jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum() + 50);
							cp.getClientMain().getPCC().checkChatPanels(cp);

							sendSendable(jta.getText());
							jta.setText("");
						}

					}
				});
				end.add(send);
				end.add(Box.createRigidArea(new Dimension(2, 45)));
			}
			panelPageEnd.add(end, BorderLayout.LINE_END);
		}
		add(panelPageEnd, BorderLayout.PAGE_END);

		jsp = new JScrollPane(pcm, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setOpaque(false);
		jsp.getVerticalScrollBar().addAdjustmentListener(e -> jsp.getViewport().repaint());
		jsp.getVerticalScrollBar().setUnitIncrement(16);
		jsp.getVerticalScrollBar().setBackground(getBackground());
		jsp.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				jsp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 50));
				jsp.getVerticalScrollBar().setMaximumSize(jsp.getPreferredSize());

			}
		});
		jsp.setSize(new Dimension(490, 400));
		jsp.setLocation(0, 60);
		jsp.setBorder(null);
		add(jsp, BorderLayout.CENTER);

		pcwt = new PanelChatWindowTop(userProfile);
		pcwt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				cp.getClientMain().addPanelUserProfileInformation(pcm, userProfile);

			}
		});
		add(pcwt, BorderLayout.PAGE_START);
	}

	@Override
	public void run() {

		initGUI();

	}

	public void sendSendable(String message) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				SendableTextMessage stm = new SendableTextMessage(ClientMain.userProfile.getUuid(),
						userProfile.getUuid(), message);
				cp.getClientMain().getServerConnection().sendSendable(stm);

			}
		};
		thread.start();

	}

	private void repaintPanel() {

		pcm.repaintFrame();
	}

	public PanelChatMessages getPCM() {

		return pcm;
	}

	public JScrollPane getSP() {

		return jsp;
	}

	public ContactPanel getContactPanel() {

		return cp;
	}

	public JPanel getPanelPageEnd() {

		return panelPageEnd;
	}

}
