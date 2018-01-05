package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.chatObjects.ClientProfile;

public class PanelChat extends JPanel implements Runnable {

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

		panelPageEnd = new JPanel();
		panelPageEnd.setLayout(new BorderLayout(6, 0));
		{
			jta = new JTextArea("");
			jta.setFont(new Font("Sans Serif", Font.PLAIN, 14));
			jta.setBorder(new LineBorder(Color.GRAY));
			jta.setLineWrap(true);
			jta.setWrapStyleWord(true);
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
								jta.setText("");
								jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum() + 50);
								cp.getClientMain().getPCC().checkChatPanels(cp);
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

			jta.addComponentListener(new ComponentAdapter() {

				@Override
				public void componentResized(ComponentEvent e) {

					if ((jta.getSize().getHeight() > (pcm.getFrameHeight() / 5)) && jspActive == false) {
						int position = jta.getCaretPosition();
						jsp2 = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
								JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
						jsp2.setBorder(new LineBorder(Color.GRAY));
						jsp2.setPreferredSize(
								new Dimension((int) jta.getSize().getWidth(), (pcm.getFrameHeight() / 5)));
						panelPageEnd.remove(jta);
						panelPageEnd.add(jsp2, BorderLayout.CENTER);

						panelPageEnd.revalidate();
						repaintPanel();
						jsp2.getVerticalScrollBar().setValue(jsp2.getVerticalScrollBar().getMaximum());
						jspActive = true;

						jta.setCaretPosition(position);
						jta.revalidate();
						jta.requestFocus();
					} else if (jta.getSize().getHeight() <= (pcm.getFrameHeight() / 5) && jspActive == true) {
						int position = jta.getCaretPosition();
						panelPageEnd.remove(jsp2);
						panelPageEnd.add(jta, BorderLayout.CENTER);
						panelPageEnd.repaint();
						jspActive = false;

						jta.setCaretPosition(position);
						jta.revalidate();
						jta.requestFocus();
					}
				}
			});
			panelPageEnd.add(jta, BorderLayout.CENTER);

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
			end.setLayout(new BoxLayout(end, BoxLayout.LINE_AXIS));
			{
				JLabel record = new JLabel();
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

				PanelSend ps = new PanelSend();
				ps.setCursor(new Cursor(Cursor.HAND_CURSOR));
				ps.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {

						if (jta.getText().equals("")) {
						} else {
							pcm.addMessageFromUser(jta.getText().replaceAll("\n", " "), userProfile.getUuid(), co);
							jta.setText("");
							jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum() + 50);
							cp.getClientMain().getPCC().checkChatPanels(cp);
						}

					}
				});
				end.add(ps);
				end.add(Box.createRigidArea(new Dimension(2, 0)));
			}
			panelPageEnd.add(end, BorderLayout.LINE_END);
		}
		add(panelPageEnd, BorderLayout.PAGE_END);

		jsp = new JScrollPane(pcm, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setOpaque(false);
		jsp.getVerticalScrollBar().addAdjustmentListener(e -> jsp.getViewport().repaint());
		add(jsp, BorderLayout.CENTER);
		jsp.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {

				jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
			}
		});

		pcwt = new PanelChatWindowTop(userProfile);
		pcwt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				cp.getClientMain().addPanelUserProfileInformation(pcm, userProfile);

			}
		});
		add(pcwt, BorderLayout.PAGE_START);

	}

	@Override
	public void run() {

		initGUI();

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

}
