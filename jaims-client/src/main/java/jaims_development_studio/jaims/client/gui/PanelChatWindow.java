package jaims_development_studio.jaims.client.gui;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.logic.DatabaseManagement;
import jaims_development_studio.jaims.client.logic.MessageObject;
import jaims_development_studio.jaims.client.logic.Profile;

public class PanelChatWindow extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	DatabaseManagement databaseManagement;
	Profile profile, userProfile;
	PanelProgram panelProgram;
	int preferredSize;
	JPanel parentPanel;
	JLabel childLabel;
	
	public PanelChatWindow(PanelProgram panelProgram, DatabaseManagement databasemanagement, Profile profile, Profile userProfile) {
		this.databaseManagement = databasemanagement;
		this.profile = profile;
		this.userProfile = userProfile;
		this.panelProgram = panelProgram;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new LineBorder(Color.BLACK));
		setComparator();
	}
	
	private void setComparator() {
		Comparator<MessageObject> comp = new Comparator<MessageObject>() {

			@Override
			public int compare(MessageObject o1, MessageObject o2) {
				if (o1.getTimeDelievered().compareTo(o2.getTimeDelievered()) > 0) {
					return 1;
				}else if(o1.getTimeDelievered().compareTo(o2.getTimeDelievered()) < 0) {
					return -1;
				}else {
					return 0;
				}
			}
		};
		Collections.sort(profile.getMessageArray(), comp);
		
		initGUI();
	}
	
	private void initGUI() {
		
		preferredSize = ((panelProgram.getMF().getFrame().getWidth()-200)/2)+((panelProgram.getMF().getFrame().getWidth()-200)/3);
		
		for(int i = 0; i < profile.getMessageArray().size(); i++) {
			if (!(profile.getMessageArray().get(i).getSender() == userProfile.getUUID())) {
				parentPanel = new JPanel();
				parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.LINE_AXIS));
				if (profile.getMessageArray().get(i).getMessageObject() instanceof String) {
					childLabel = new JLabel();
					childLabel.setFont(new Font("Calibri", Font.PLAIN, 15));
					childLabel.setMaximumSize(new Dimension(preferredSize, getStringWidth((String) profile.getMessageArray().get(i).getMessageObject())));
					childLabel.setBorder(new LineBorder(Color.BLACK));
					//parentPanel.setMaximumSize(new Dimension(preferredSize, getStringWidth((String) profile.getMessageArray().get(i).getMessageObject())));
					parentPanel.setAlignmentX(Box.LEFT_ALIGNMENT);
					parentPanel.add(childLabel);
					parentPanel.add(Box.createRigidArea(new Dimension(15, 0)));
					parentPanel.add(Box.createHorizontalGlue());
				}else if (profile.getMessageArray().get(i).getMessageObject() instanceof Image){
					//TODO Create method for finding fitting size for Image
				}else {
					//TODO Print Error
				}
			}else {
				parentPanel = new JPanel();
				parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.LINE_AXIS));				
				if (profile.getMessageArray().get(i).getMessageObject() instanceof String) {
					childLabel = new JLabel();
					childLabel.setFont(new Font("Calibri", Font.PLAIN, 15));
					childLabel.setMaximumSize(new Dimension(preferredSize, getStringWidth((String) profile.getMessageArray().get(i).getMessageObject())));
					childLabel.setBorder(new LineBorder(Color.BLACK));
					//parentPanel.setMaximumSize(new Dimension(preferredSize, getStringWidth((String) profile.getMessageArray().get(i).getMessageObject())));
					parentPanel.setAlignmentX(Box.RIGHT_ALIGNMENT);
					parentPanel.add(Box.createRigidArea(new Dimension(15, 0)));
					parentPanel.add(Box.createHorizontalGlue());					
					parentPanel.add(childLabel);
				}else if (profile.getMessageArray().get(i).getMessageObject() instanceof Image){
					//TODO Create method for finding fitting size for Image
				}else {
					//TODO Print Error
				}
			}
			
			add(parentPanel);
			add(Box.createRigidArea(new Dimension(0, 10)));
		}		
	}
	
	public JLabel getLabel() {
		return childLabel;
	}
	
	public void resized() {
		removeAll();
		
		initGUI();
		//msg.setMaximumSize(new Dimension((panelProgram.getMF().getFrame().getWidth()/2)+((4*panelProgram.getMF().getFrame().getWidth())/6), lbl.getHeight()));
		revalidate();
	}
	  
	  private void doResizing() {
		  removeAll();
		  //initGUINew();
		  revalidate();
		  repaint();
	  }
	
	  @Override
	  public void run() {
		 doResizing();
			
	  }
	  
	  private int countLines(String s, FontMetrics fm) {
		  String wholeString = "";
		  String[] split = s.split(" ");
		  String text = "<html>";
		  int countLines = 1;
		  for (String st : split) {
			  if ((fm.stringWidth(wholeString) + fm.stringWidth(st + " ")) < (preferredSize-15)) {
				  wholeString += st;
				  wholeString += " ";	  
			  }else {
				  text += wholeString;
				  text += "</p>";
				  wholeString = "";
				  countLines++;
				  wholeString += st;
				  wholeString += " ";
			  }
		  }
		  
		  System.out.println(countLines);
		  
		  if (text.equals("<html>")) {
			  text = wholeString;
			  childLabel.setText(text);
			  return 1;
		  }else {
			  text += wholeString;
			  childLabel.setText(text);
			  return countLines;
		  }
		  
	  }
	  
	  public int getStringWidth(String s) {
		  FontMetrics fm =	childLabel.getFontMetrics(childLabel.getFont());
		  int lines = countLines(s, fm);
		  int height = fm.getHeight();
		  return (lines*(height+1));
	  }
	  
	
}
