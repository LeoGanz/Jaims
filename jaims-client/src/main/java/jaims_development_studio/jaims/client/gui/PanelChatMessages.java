package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.chatObjects.ClientProfile;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelChatMessages extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static final Logger	LOG					= LoggerFactory.getLogger(PanelChatMessages.class);

	JaimsFrame					jf;
	ClientProfile				userProfile;
	ChatObject					co;
	JLabel						lbl;
	JTextArea					jta;
	ContactPanel				cp;
	private int					textMessages, voiceMessages, allMessages, ownTextMessages, contactTextMessages,
			ownVoiceMessages, contactVoiceMessages, ownWords, contactWords, allWords;

	public PanelChatMessages(JaimsFrame jf, ClientProfile userProfile, ChatObject co, ContactPanel cp) {

		this.jf = jf;
		this.userProfile = userProfile;
		this.co = co;
		this.cp = cp;

		setName(userProfile.getNickname());
		setOpaque(false);
		setBorder(new EmptyBorder(0, 3, 0, 3));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	private void initGUI() {

		for (int i = 0; i < co.getList().size(); i++) {
			if (co.getList().get(i).getSender() != ClientMain.userProfile.getUuid() && co.getList() != null) {
				// Case Sender = Contact

				JPanel p = new JPanel();
				p.setOpaque(false);
				p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
				if (co.getList().get(i).getMessageObject() instanceof String) {
					if (co.getList().get(i).getVoiceMessage() == false) {
						TextAreaMessage tam = new TextAreaMessage((String) co.getList().get(i).getMessageObject(), jf,
								this, false);

						p.add(tam);
						p.add(Box.createHorizontalGlue());
						textMessages++;
						allMessages++;
						contactTextMessages++;
						int c = ((String) co.getList().get(i).getMessageObject()).split(" ").length;
						contactWords += c;
						allWords += c;
					} else if (co.getList().get(i).getVoiceMessage() == true) {
						VoiceMessage vm = new VoiceMessage(userProfile, (String) co.getList().get(i).getMessageObject(),
								false);

						p.add(vm);
						p.add(Box.createHorizontalGlue());

						voiceMessages++;
						allMessages++;
						contactVoiceMessages++;
					}

				} else if (co.getList().get(i).getMessageObject() instanceof Image) {

				} else {

				}

				add(p);
				add(Box.createRigidArea(new Dimension(0, 10)));
			} else {
				// Case Sender = User
				if (co.getList() != null) {
					JPanel p = new JPanel();
					p.setOpaque(false);
					p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
					if (co.getList().get(i).getMessageObject() instanceof String) {
						if (co.getList().get(i).getVoiceMessage() == false) {
							TextAreaMessage tam = new TextAreaMessage((String) co.getList().get(i).getMessageObject(),
									jf, this, true);

							p.add(Box.createHorizontalGlue());
							p.add(tam);

							textMessages++;
							allMessages++;
							ownTextMessages++;
							int c = ((String) co.getList().get(i).getMessageObject()).split(" ").length;
							ownWords += c;
							allWords += c;
						} else if (co.getList().get(i).getVoiceMessage() == true) {
							VoiceMessage vm = new VoiceMessage(ClientMain.userProfile,
									(String) co.getList().get(i).getMessageObject(), true);

							p.add(Box.createHorizontalGlue());
							p.add(vm);

							voiceMessages++;
							allMessages++;
							ownVoiceMessages++;
						}

					} else if (co.getList().get(i).getMessageObject() instanceof Image) {

					} else {

					}

					add(p);
					add(Box.createRigidArea(new Dimension(0, 10)));
				}
			}
		}

		repaint();
	}

	@Override
	public void run() {

		if (messageListExists(co))
			initGUI();
	}

	private int countLines(String s, FontMetrics fm, JTextArea lbl) {

		int width = (int) jf.getSize().getWidth() - 260 - 16;
		double ratio = -0.000000000031829 * Math.pow(width, 3) + 0.000000404015684 * Math.pow(width, 2)
				- 0.000992064197966 * width + 1.210427932031404;
		int preferredWidth = (int) (width * ratio);

		String wholeString = "";
		String[] split = s.split(" ");

		String text = "";
		int countLines = 1;

		for (String st : split) {
			if ((fm.stringWidth(wholeString) + fm.stringWidth(st + " ")) < preferredWidth) {
				wholeString += st;
				wholeString += " ";
			} else {
				text += wholeString;
				text += "\n";
				wholeString = "";
				countLines++;
				wholeString += st;
				wholeString += " ";
			}
		}

		if (text.equals("")) {
			text = wholeString;
			lbl.setText(text);
			return 1;
		} else {
			text += wholeString;
			lbl.setText(text);
			return countLines;
		}

	}

	public int getStringHeight(String s, JTextArea lbl) {

		FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
		int lines = countLines(s, fm, lbl);
		int height = fm.getHeight();
		if (lines == 1) {
			return ((lines * height) + 5);
		} else {
			return (lines * (height + 1));
		}

	}

	public int getStringWidth(String s, JTextArea lbl) {

		int width = (int) jf.getSize().getWidth() - 260 - 16;
		double preferredWidth = -0.000000000031829 * Math.pow(width, 3) + 0.000000404015684 * Math.pow(width, 2)
				- 0.000992064197966 * width + 1.210427932031404;

		FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
		int i = fm.stringWidth(s) + 5;
		if (i > (width * preferredWidth)) {
			return (int) (width * preferredWidth);
		} else {
			return i;
		}
	}

	public void addMessageFromUser(String s, UUID profileUUID, ChatObject co) {

		PanelChatMessages pcm = this;
		Thread thread = new Thread() {
			@Override
			public void run() {

				JPanel p = new JPanel();
				p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
				TextAreaMessage tam = new TextAreaMessage(s.trim(), jf, pcm, true);

				p.add(Box.createHorizontalGlue());
				p.add(tam);
				add(p);
				add(Box.createRigidArea(new Dimension(0, 10)));
				revalidate();
				repaint();

				PanelChat.jsp.getVerticalScrollBar().setValue(PanelChat.jsp.getVerticalScrollBar().getMaximum());
				Message m = new Message(ClientMain.userProfile.getUuid(), profileUUID, s, null,
						new Date(System.currentTimeMillis()), false);
				cp.getClientMain().getWTD().writeMessage(m, profileUUID, co, getMessageList(co));
				cp.getClientMain().getPCC().checkChatPanels(cp);
			}
		};
		thread.start();

	}

	public void addMessageFromContact(SendableMessage sm) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				JPanel p = new JPanel();
				p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));

			}
		};
		thread.start();

	}

	public void addVoiceMessageFromUser(String path, UUID profileUUID, ChatObject co) {

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));

		VoiceMessage vm = new VoiceMessage(userProfile, path, true);
		p.add(Box.createHorizontalGlue());
		p.add(vm);
		add(p);
		add(Box.createRigidArea(new Dimension(0, 10)));
		revalidate();
		repaint();

		PanelChat.jsp.getVerticalScrollBar().setValue(PanelChat.jsp.getVerticalScrollBar().getMaximum());
		Message m = new Message(ClientMain.userProfile.getUuid(), profileUUID, path, null,
				new Date(System.currentTimeMillis()), true);
		cp.getClientMain().getWTD().writeMessage(m, profileUUID, co, getMessageList(co));
		cp.getClientMain().getPCC().checkChatPanels(cp);
	}

	private boolean messageListExists(ChatObject co) {

		return co.getList() != null;

	}

	private ArrayList<Message> getMessageList(ChatObject co) {

		ResultSet rs;
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		ArrayList<Message> listCo = null;
		try {
			ps = con.prepareStatement(
					"SELECT MESSAGE_ARRAY FROM " + ClientMain.userProfile.getNickname().toUpperCase() + " WHERE ID=?;");
			ps.setObject(1, co.getProfileContact().getUuid());
			rs = ps.executeQuery();
			con.commit();

			rs.next();

			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(rs.getBytes(1)));
			listCo = (ArrayList<Message>) ois.readObject();

			return listCo;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public int getFrameWidth() {

		return jf.getWidth();
	}

	public int getFrameHeight() {

		return jf.getHeight();
	}

	public void repaintFrame() {

		jf.getContentPane().repaint();
	}

	public ContactPanel getContactPanel() {

		return cp;
	}

	public JaimsFrame getFrame() {

		return jf;
	}

	public ClientProfile getUserProfile() {

		return userProfile;
	}

	public int getTextMessages() {

		return textMessages;
	}

	public int getVoiceMessages() {

		return voiceMessages;
	}

	public int getAllMessages() {

		return allMessages;
	}

	public int getOwnTextMessages() {

		return ownTextMessages;
	}

	public int getContactTextMessages() {

		return contactTextMessages;
	}

	public int getOwnVoiceMessages() {

		return ownVoiceMessages;
	}

	public int getContactVoiceMessages() {

		return contactVoiceMessages;
	}

	public int getOwnWords() {

		return ownWords;
	}

	public int getContactWords() {

		return contactWords;
	}

	public int getAllWords() {

		return allWords;
	}
}
