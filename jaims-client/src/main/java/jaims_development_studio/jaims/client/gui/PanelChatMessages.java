package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.chatObjects.Profile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelChatMessages extends JPanel implements Runnable{
	
	JaimsFrame jf;
	Profile userProfile;
	ChatObjects co;
	JLabel lbl;
	JTextArea jta;
	ContactPanel cp;
	
	public PanelChatMessages(JaimsFrame jf, Profile userProfile, ChatObjects co, ContactPanel cp) {
		this.jf = jf;
		this.userProfile = userProfile;
		this.co = co;
		this.cp = cp;
		
		setOpaque(false);
		setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new LineBorder(getBackground(), 3)));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	private void initGUI() {
		ResultSet rs;
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		ArrayList<Message> listCo = null;
		try {
			ps = con.prepareStatement(co.getList());
			ps.setObject(1, co.getProfileContact().getUuid());
			rs = ps.executeQuery();
			con.commit();
			rs.next();
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(rs.getBytes(1)));
			listCo = (ArrayList<Message>) ois.readObject();
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
		
		
		for (int i = 0; i < listCo.size(); i++) {
			if (listCo.get(i).getSender() != ClientMain.userProfile.getUUID() && listCo != null) {
				//Case Sender = Contact
				
				JPanel p = new JPanel();
				p.setOpaque(false);
				p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
				if (listCo.get(i).getMessageObject() instanceof String) {
					TextAreaMessage tam = new TextAreaMessage((String) listCo.get(i).getMessageObject(), jf, this, false);
					
					
					p.add(tam);
					p.add(Box.createHorizontalGlue());
					
				}else if (listCo.get(i).getMessageObject() instanceof Image) {
					
				}else {
					
				}
				
				add(p);
				add(Box.createRigidArea(new Dimension(0, 10)));
			}else {
				//Case Sender = User
				if (listCo != null) {
					JPanel p = new JPanel();
					p.setOpaque(false);
					p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));				
					if (listCo.get(i).getMessageObject() instanceof String) {
						TextAreaMessage tam = new TextAreaMessage((String) listCo.get(i).getMessageObject(), jf, this, true); 
						
						p.add(Box.createHorizontalGlue());
						p.add(tam);
						
					}else if (listCo.get(i).getMessageObject() instanceof Image) {
						
					}else {
						
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
		if(messageListExists(co))
			initGUI();
	}
	
	 private int countLines(String s, FontMetrics fm, JTextArea lbl) {
		 
		 int width = (int) jf.getSize().getWidth()-260-16;
		 double ratio = -0.000000000031829 * Math.pow(width, 3) + 0.000000404015684* Math.pow(width, 2) - 0.000992064197966 * width + 1.210427932031404;
		 int preferredWidth = (int) (width * ratio);
		 
		  String wholeString = "";
		  String[] split = s.split(" ");
		  
		  String text = "";
		  int countLines = 1;
		  
		  for (String st : split) {
			  if ((fm.stringWidth(wholeString) + fm.stringWidth(st + " ")) < preferredWidth) {
				  wholeString += st;
				  wholeString += " ";	  
			  }else {
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
		  }else {
			  text += wholeString;
			  lbl.setText(text);
			  return countLines;
		  }
		  
	 }
	 
	 public int getStringHeight(String s, JTextArea lbl) {
		  FontMetrics fm =	lbl.getFontMetrics(lbl.getFont());
		  int lines = countLines(s, fm, lbl);
		  int height = fm.getHeight();
		  if (lines == 1) {
			  return ((lines*height) + 5);
		  }else {
			  return (lines*(height+1) );
		  }
		  
	 }
	 
	 public int getStringWidth(String s, JTextArea lbl) {
		 int width = (int) jf.getSize().getWidth()-260-16;
		 double preferredWidth = -0.000000000031829 * Math.pow(width, 3) + 0.000000404015684* Math.pow(width, 2) - 0.000992064197966 * width + 1.210427932031404;
		 
		 FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
		 int i = fm.stringWidth(s)+5;
		 if (i > (width * preferredWidth)) {
			return (int) (width*preferredWidth); 
		 }else {
			 return i;
		 }
	 }
	 
	 public void addMessageFromUser(String s) {
		 	JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
			TextAreaMessage tam = new TextAreaMessage(s.trim(), jf, this, true);
			
			p.add(Box.createHorizontalGlue());
			p.add(tam);			
			add(p);
			add(Box.createRigidArea(new Dimension(0, 10)));
			revalidate();
			repaint();
	 }
	 
	 public void addVoiceMessageFromUser(String path) {
		 JPanel p = new JPanel();
		 p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		 
		 VoiceMessage vm = new VoiceMessage(userProfile, path, true);
		 p.add(Box.createHorizontalGlue());
		 p.add(vm);
		 add(p);
		 add(Box.createRigidArea(new Dimension(0, 10)));
		 revalidate();
		 repaint();
		 
	 }
	 
	 private boolean messageListExists(ChatObjects co) {
			ResultSet rs;
			Connection con = DatabaseConnection.getConnection();
			PreparedStatement ps;
			try {
				ps = con.prepareStatement(co.getList());
				ps.setObject(1, co.getProfileContact().getUuid());
				rs = ps.executeQuery();
				con.commit();
				
				rs.next();
				
				if (rs.getBytes(1) != null)
					return true;
				else
					return false;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			
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
	

}
