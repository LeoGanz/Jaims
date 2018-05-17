package jaims_development_studio.jaims.client.gui.messagePanels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.chatObjects.ClientInternMessage;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.TextMessage;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.VoiceMessage;

public class PanelShowMessages extends JPanel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;
	private GUIMain					guiMain;
	private UUID					contactUUID;
	private ArrayList<TextMessage>	allTextMessages		= new ArrayList<>();
	private ArrayList<VoiceMessage>	allVoiceMessages	= new ArrayList<>();
	private ManageMessagePanels		mmp;
	private File					directory;
	private String					fileTime;

	public PanelShowMessages(GUIMain guiMain, ArrayList<ClientInternMessage> mList, UUID contactID,
			ManageMessagePanels mmp) {

		this.guiMain = guiMain;
		this.mmp = mmp;
		contactUUID = contactID;
		initGUI(mList, contactID);
	}

	private void initGUI(ArrayList<ClientInternMessage> mList, UUID contactID) {

		setOpaque(false);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(0, 5, 0, 5));
		add(Box.createRigidArea(new Dimension(0, 5)));

		for (ClientInternMessage m : mList) {
			mmp.getContactChatInformation(contactID).addTotalNumberMessages();
			if (m.getSender().equals(guiMain.getUserUUID())) {
				mmp.getContactChatInformation(contactID).addNumberOwnMessages();
				if (m.getMessageType().equals("TEXT")) {
					TextMessage tm = new TextMessage((String) m.getMessage(), guiMain, true);
					allTextMessages.add(tm);
					JPanel ptm = new JPanel();
					ptm.setLayout(new BoxLayout(ptm, BoxLayout.LINE_AXIS));
					ptm.setBackground(new Color(0, 0, 0, 0));
					ptm.setOpaque(false);
					ptm.add(Box.createHorizontalGlue());
					ptm.add(tm);
					add(ptm);
					add(Box.createRigidArea(new Dimension(0, 5)));

					mmp.getContactChatInformation(contactID).addNumberOwnTextMessages();
					mmp.getContactChatInformation(contactID).setNumberWordsUsed(m.getMessage().split(" ").length);
					mmp.getContactChatInformation(contactID).setNumberOwnWordsUsed(m.getMessage().split(" ").length);
				} else if (m.getMessageType().equals("IMAGE")) {

				} else if (m.getMessageType().equals("VOICE")) {
					VoiceMessage vm = new VoiceMessage(m.getSender(), m.getMessage(), true, guiMain);
					vm.setAlignmentX(Component.RIGHT_ALIGNMENT);
					JPanel pvm = new JPanel();
					pvm.setLayout(new BoxLayout(pvm, BoxLayout.LINE_AXIS));
					pvm.setBackground(new Color(0, 0, 0, 0));
					pvm.setOpaque(false);
					pvm.add(Box.createHorizontalGlue());
					pvm.add(vm);
					add(pvm);
					add(Box.createRigidArea(new Dimension(0, 5)));

					mmp.getContactChatInformation(contactID).addNumberTotalVoiceMessages();
					mmp.getContactChatInformation(contactID).addNumberOwnVoiceMessages();
				} else if (m.getMessageType().equals("FILE")) {

				} else if (m.getMessageType().equals("LOCATION")) {

				} else if (m.getMessageType().equals("OTHER")) {

				} else {

				}
			} else {
				mmp.getContactChatInformation(contactID).addNumberContactMessages();
				if (m.getMessageType().equals("TEXT")) {
					TextMessage tm = new TextMessage(m.getMessage(), guiMain, false);
					allTextMessages.add(tm);

					JPanel ptm = new JPanel();
					ptm.setLayout(new BoxLayout(ptm, BoxLayout.LINE_AXIS));
					ptm.setBackground(new Color(0, 0, 0, 0));
					ptm.setOpaque(false);
					ptm.add(tm);
					ptm.add(Box.createHorizontalGlue());
					add(ptm);
					add(Box.createRigidArea(new Dimension(0, 5)));

					mmp.getContactChatInformation(contactID).addNumberContactTextMessages();
					mmp.getContactChatInformation(contactID).setNumberWordsUsed(m.getMessage().split(" ").length);
					mmp.getContactChatInformation(contactID)
							.setNumberContactWordsUsed(m.getMessage().split(" ").length);
				} else if (m.getMessageType().equals("IMAGE")) {

				} else if (m.getMessageType().equals("VOICE")) {
					VoiceMessage vm = new VoiceMessage(m.getSender(), m.getMessage(), true, guiMain);
					vm.setAlignmentX(Component.LEFT_ALIGNMENT);
					add(vm);
					add(Box.createRigidArea(new Dimension(0, 5)));

					mmp.getContactChatInformation(contactID).addNumberTotalVoiceMessages();
					mmp.getContactChatInformation(contactID).addNumberContactVoiceMessages();
				} else if (m.getMessageType().equals("FILE")) {

				} else if (m.getMessageType().equals("LOCATION")) {

				} else if (m.getMessageType().equals("OTHER")) {

				} else {

				}

			}
		}
	}

	public void addNewUserMessage(String message) {

		mmp.getContactChatInformation(contactUUID).addTotalNumberMessages();
		mmp.getContactChatInformation(contactUUID).addNumberOwnMessages();

		TextMessage tm = new TextMessage(message, guiMain, true);
		JPanel ptm = new JPanel();
		ptm.setLayout(new BoxLayout(ptm, BoxLayout.LINE_AXIS));
		ptm.setBackground(new Color(0, 0, 0, 0));
		ptm.add(Box.createHorizontalGlue());
		ptm.setOpaque(false);
		ptm.add(tm);
		add(ptm);
		add(Box.createRigidArea(new Dimension(0, 5)));

		revalidate();
		repaint();
		guiMain.repaintParentPanel();
		allTextMessages.add(tm);
		tm.setMaximumSize();

		mmp.getContactChatInformation(contactUUID).addNumberOwnTextMessages();
		mmp.getContactChatInformation(contactUUID).setNumberWordsUsed(message.split(" ").length);
		mmp.getContactChatInformation(contactUUID).setNumberOwnWordsUsed(message.split(" ").length);
	}

	public void addNewTextMessage(jaims_development_studio.jaims.api.message.TextMessage m) {

		mmp.getContactChatInformation(m.getSender()).addTotalNumberMessages();
		mmp.getContactChatInformation(m.getSender()).addNumberOwnMessages();

		TextMessage tm = new TextMessage(m.getMessage(), guiMain, false);
		JPanel ptm = new JPanel();
		ptm.setLayout(new BoxLayout(ptm, BoxLayout.LINE_AXIS));
		ptm.setBackground(new Color(0, 0, 0, 0));
		ptm.setOpaque(false);
		ptm.add(tm);
		ptm.add(Box.createHorizontalGlue());
		add(ptm);
		add(Box.createRigidArea(new Dimension(0, 5)));

		tm.setMaximumSize();
		revalidate();
		repaint();

		allTextMessages.add(tm);

		mmp.getContactChatInformation(m.getSender()).addNumberOwnTextMessages();
		mmp.getContactChatInformation(m.getSender()).setNumberWordsUsed(m.getMessage().split(" ").length);
		mmp.getContactChatInformation(m.getSender()).setNumberOwnWordsUsed(m.getMessage().split(" ").length);

	}

	public void addNewUserVoiceMessage(VoiceMessage vm) {

		mmp.getContactChatInformation(contactUUID).addTotalNumberMessages();
		mmp.getContactChatInformation(contactUUID).addNumberOwnMessages();

		JPanel pvm = new JPanel();
		pvm.setLayout(new BoxLayout(pvm, BoxLayout.LINE_AXIS));
		pvm.setBackground(new Color(0, 0, 0, 0));
		pvm.setOpaque(false);
		pvm.add(Box.createHorizontalGlue());
		pvm.add(vm);
		add(pvm);
		add(Box.createRigidArea(new Dimension(0, 5)));

		revalidate();
		repaint();
		guiMain.repaintParentPanel();
		allVoiceMessages.add(vm);

		mmp.getContactChatInformation(contactUUID).addNumberOwnVoiceMessages();
	}

	public void addNewVoiceMessage(jaims_development_studio.jaims.api.message.VoiceMessage m) {

		int fileNumber = getFileNumber();
		DecimalFormat dm = new DecimalFormat("0000");
		File f = new File(guiMain.getUserPath() + "audios/" + "JAIAUD-" + fileTime + "-" + dm.format(fileNumber) + "."
				+ guiMain.getSettings().getInputFileFormat().getExtension());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			fos.write(m.getVoiceMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		guiMain.saveVoiceMessage(m, f.getAbsolutePath());

		mmp.getContactChatInformation(m.getSender()).addTotalNumberMessages();
		mmp.getContactChatInformation(m.getSender()).addNumberOwnMessages();

		VoiceMessage vm = new VoiceMessage(m.getSender(), f.getAbsolutePath(), false, guiMain);
		JPanel pvm = new JPanel();
		pvm.setLayout(new BoxLayout(pvm, BoxLayout.LINE_AXIS));
		pvm.setBackground(new Color(0, 0, 0, 0));
		pvm.setOpaque(false);
		pvm.add(vm);
		pvm.add(Box.createHorizontalGlue());
		add(pvm);
		add(Box.createRigidArea(new Dimension(0, 5)));

		revalidate();
		repaint();
		guiMain.repaintParentPanel();
		allVoiceMessages.add(vm);

		mmp.getContactChatInformation(m.getSender()).addNumberContactVoiceMessages();

	}

	private int getFileNumber() {

		int existingAudioFiles = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		fileTime = sdf.format(new Date(System.currentTimeMillis()));

		directory = new File(guiMain.getUserPath() + "audios/");
		directory.mkdirs();
		String[] filenames = directory.list();
		if (filenames.length >= 0) {
			for (String s : filenames) {
				if (s.contains(fileTime))
					existingAudioFiles++;
			}
		}

		return ++existingAudioFiles;
	}

	public void updateMessages() {

		Thread tTextMessages = new Thread() {
			@Override
			public void run() {

				for (TextMessage tm : allTextMessages)
					tm.updateMessage();
			}
		};
		tTextMessages.start();
	}

	public UUID getContactID() {

		return contactUUID;
	}

}
