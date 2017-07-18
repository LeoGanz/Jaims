package jaims_development_studio.jaims.client.gui;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.commons.lang3.StringUtils;

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
	JTextArea jta;
	Date lastMessage;
	
	public PanelChatWindow(PanelProgram panelProgram, DatabaseManagement databasemanagement, Profile profile, Profile userProfile) {
		this.databaseManagement = databasemanagement;
		this.profile = profile;
		this.userProfile = userProfile;
		this.panelProgram = panelProgram;
		setLayout(new BorderLayout());
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
		int insert =  ((panelProgram.getMF().getFrame().getWidth()-200)/2)-((panelProgram.getMF().getFrame().getWidth()-200)/3);
		
		
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.PAGE_AXIS));
		jp.add(Box.createVerticalGlue());
		
		JScrollPane jsp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		jta = new JTextArea();
		//jta.setMinimumSize(new Dimension((panelProgram.getMF().getFrame().getWidth()/3)*2, 25));
		/*jta.setMaximumSize(new Dimension((panelProgram.getMF().getFrame().getWidth()/3)*2, 25));*/
		jta.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				if (getStringHeightTextArea(jta.getText()) <= 100) {
					jta.setPreferredSize(new Dimension((panelProgram.getMF().getFrame().getWidth()/3)*2, getStringHeightTextArea(jta.getText())));
					jp.setPreferredSize(new Dimension((panelProgram.getMF().getFrame().getWidth()/3)*2, getStringHeightTextArea(jta.getText())));
					jsp.setPreferredSize(new Dimension((panelProgram.getMF().getFrame().getWidth()/3)*2, getStringHeightTextArea(jta.getText())));
				}else {
					jta.setPreferredSize(new Dimension((panelProgram.getMF().getFrame().getWidth()/3)*2, 100));
					jp.setPreferredSize(new Dimension((panelProgram.getMF().getFrame().getWidth()/3)*2, 100));
					jsp.setPreferredSize(new Dimension((panelProgram.getMF().getFrame().getWidth()/3)*2, 100));
				}
				
			}
			
		});
		jta.setBorder(new LineBorder(Color.BLACK));
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		jp.add(jta);
		jsp.setViewportView(jp);
		add(jsp, BorderLayout.PAGE_END);
		
		
		JPanel centerpanel = new JPanel();
		centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.PAGE_AXIS));
		MessagePanel bigPanel = null;
		
		centerpanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		FontMetrics fm = new FontMetrics(new Font("Calibri", Font.PLAIN, 15)) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		};
		
		
		
		for(int i = 0; i < profile.getMessageArray().size(); i++) {
			bigPanel = new MessagePanel();
			bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.LINE_AXIS));
			if ((profile.getMessageArray().get(i).getSender().equals(userProfile.getUUID())) == false) {
				if (profile.getMessageArray().get(i).getMessageObject() instanceof String) {
					childLabel = new JLabel();
					childLabel.setFont(new Font("Calibri", Font.PLAIN, 15));
					childLabel.setPreferredSize(new Dimension(50, getStringWidth((String) profile.getMessageArray().get(i).getMessageObject())));
					childLabel.setBorder(new LineBorder(Color.BLACK));
					childLabel.setAlignmentX(Box.LEFT_ALIGNMENT);
					bigPanel.add(Box.createRigidArea(new Dimension(5, 0)));
					bigPanel.add(childLabel);
					bigPanel.add(Box.createHorizontalGlue());
					bigPanel.add(Box.createRigidArea(new Dimension(insert, 0)));
				}else if (profile.getMessageArray().get(i).getMessageObject() instanceof Image){
					//TODO Create method for finding fitting size for Image
				}else {
					//TODO Print Error
				}
			}else {
				bigPanel = new MessagePanel();
				bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.LINE_AXIS));
				bigPanel.add(Box.createRigidArea(new Dimension(insert, 0)));
				bigPanel.add(Box.createHorizontalGlue());
				
				if (profile.getMessageArray().get(i).getMessageObject() instanceof String) {
					childLabel = new JLabel();
					childLabel.setFont(new Font("Calibri", Font.PLAIN, 15));
					System.out.println(childLabel.getFontMetrics(childLabel.getFont()).stringWidth((String) profile.getMessageArray().get(i).getMessageObject()));
					childLabel.setPreferredSize(new Dimension(preferredSize, getStringWidth((String) profile.getMessageArray().get(i).getMessageObject())));
					childLabel.setBorder(new LineBorder(Color.BLACK));
					childLabel.setAlignmentX(Box.RIGHT_ALIGNMENT);
					bigPanel.add(childLabel);
					bigPanel.add(Box.createRigidArea(new Dimension(5, 0)));
				}else if (profile.getMessageArray().get(i).getMessageObject() instanceof Image){
					//TODO Create method for finding fitting size for Image
				}else {
					//TODO Print Error
				}
			}
			
			if (i == profile.getMessageArray().size()-1) {
				lastMessage = profile.getMessageArray().get(i).getTimeDelievered();
			}
			
			centerpanel.add(bigPanel);
			centerpanel.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		JScrollPane sp = new JScrollPane(centerpanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setHorizontalScrollBar(null);
		sp.scrollRectToVisible(centerpanel.getVisibleRect());
		add(sp, BorderLayout.CENTER);
		
		

		
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
			  if ((fm.stringWidth(wholeString) + fm.stringWidth(st + " ")) < (preferredSize-17)) {
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
	  
	  private int countLinesTextArea(String s, FontMetrics fm) {
		 int i = StringUtils.countMatches(s, "\n"); 
		 System.out.println(i);
		 if (i < 1) {
			 return 1;
		 }else {
			 return i+1;
		 }
	  }
	  
	  public int getStringWidth(String s) {
		  FontMetrics fm =	childLabel.getFontMetrics(childLabel.getFont());
		  int lines = countLines(s, fm);
		  int height = fm.getHeight();
		  return (lines*(height+1));
	  }
	  
	  public int getStringHeightTextArea(String s) {
		  FontMetrics fm = jta.getFontMetrics(jta.getFont());
		  int lines = countLinesTextArea(s, fm);
		  int height = fm.getHeight();
		  return (lines*(height+1));
	  }
	  
	  public ArrayList<MessageObject> getList() {
		  return profile.getMessageArray();
	  }
	  
	  public Date getLastMessage() {
		  return lastMessage;
	  }
	
}
