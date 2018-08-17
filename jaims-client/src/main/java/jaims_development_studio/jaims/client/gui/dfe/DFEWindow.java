package jaims_development_studio.jaims.client.gui.dfe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jaims_development_studio.jaims.client.directFileExchange.DFEManager;
import jaims_development_studio.jaims.client.directFileExchange.sendables.SDFEFileInfo;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;

public class DFEWindow extends ParentPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GUIMain guiMain;
	private UUID userID;
	private String nickname;
	private DFEManager dfeManager;
	private ConnectButton connectButton;
	private PanelStoreDFEs centerPanel;
	
	public DFEWindow(GUIMain guiMain, UUID userID, String nickname) {
		this.guiMain = guiMain;
		this.userID = userID;
		this.nickname = nickname;
		
		initGUI();
	}
	
	private void initGUI() {
		
		setLayout(new BorderLayout());
		
		add(buildTopPanel(), BorderLayout.PAGE_START);
		add(buildCenterPanel(), BorderLayout.CENTER);
		add(buildBottomPanel(), BorderLayout.PAGE_END);
		
	}
	
	private JPanel buildTopPanel() {
		
		JPanel pTop = new JPanel();
		pTop.setBackground(new Color(240, 240, 240));
		pTop.setLayout(new BoxLayout(pTop, BoxLayout.LINE_AXIS));
		{
			pTop.add(Box.createHorizontalGlue());
			pTop.add(Box.createRigidArea(new Dimension(0, 60)));
			
			JLabel titel = new JLabel("Direct file exchange - " + nickname);
			titel.setFont(new Font("SansSerig", Font.BOLD, 18));
			titel.setForeground(Color.black);
			titel.setOpaque(false);
			pTop.add(titel);
			pTop.add(Box.createHorizontalGlue());
			
			
			connectButton = new ConnectButton(this);
			pTop.add(connectButton);
			
		}
		
		return pTop;
	}
	
	private JPanel buildCenterPanel() {
		centerPanel = new PanelStoreDFEs(userID, guiMain);

		return centerPanel;
	}
	
	private JPanel buildBottomPanel() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		bottomPanel.setBackground(Color.DARK_GRAY);
		{
			bottomPanel.add(Box.createHorizontalGlue());
			
			SendButton sendButton = new SendButton(this);
			bottomPanel.add(sendButton);
			
			bottomPanel.add(Box.createRigidArea(new Dimension(0, 60)));
			bottomPanel.add(Box.createHorizontalGlue());
		}
		
		return bottomPanel;
	}
	
	public boolean initialiseServer() {
		dfeManager = new DFEManager(guiMain.getClientMain(),this);
		return dfeManager.initiateServer();
	}
	
	public DFEManager getDFEManager() {
		return dfeManager;
	}
	
	public void sendFiles(File[] arr) {
		dfeManager.sendFiles(arr);
	}
	
	public void addNewObject(SDFEFileInfo info) {
		DFEObject dfeo = new DFEObject(info.getFileID(), info.getSender(), info.getRecipient(), info.getFileName(), "", info.getExtension(), info.getFileType(), info.getDateSent());
		centerPanel.addDFEObject(dfeo);
	}
	
	public UUID getRecipientID() {
		return userID;
	}

}
