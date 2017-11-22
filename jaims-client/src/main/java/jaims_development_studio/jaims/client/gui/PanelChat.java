package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.chatObjects.Profile;

public class PanelChat extends JPanel implements Runnable{
	
	JPanel panelPageEnd;
	JScrollPane jsp, jsp2;
	JTextField jtf;
	JTextArea jta;
	PanelChatMessages pcm;
	PanelChatWindowTop pcwt;
	Profile userProfile;
	int jtaWidth = 0, countLinesOld = 1;
	boolean jspActive = false;
	
	public PanelChat(Profile userProfile, PanelChatMessages pcm) {
		this.userProfile = userProfile;
		this.pcm = pcm;
	}
	
	private void initGUI() {
		setLayout(new BorderLayout(0,2));
		
		panelPageEnd = new JPanel();
		panelPageEnd.setLayout(new BorderLayout(6,0));
		{			
			jta = new JTextArea();
			jta.setFont(new Font("Calibri", Font.PLAIN, 14));
			jta.setBorder(new LineBorder(Color.GRAY));
			jta.setLineWrap(true);
			jta.setWrapStyleWord(true);
			jta.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {					
					
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						pcm.addMessageFromUser(jta.getText().replaceAll("\n", " "));
						jta.setText("");
						jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
					}
				}
			});
			jta.addComponentListener(new ComponentAdapter() {
				
				@Override
				public void componentResized(ComponentEvent e) {
					// TODO Auto-generated method stub
					if (jta.getSize().getHeight() > (pcm.getFrameHeight()/5) && jspActive == false) {
						int position = jta.getCaretPosition();
						jsp2 = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
						jsp2.setBorder(new LineBorder(Color.GRAY));
						jsp2.setPreferredSize(new Dimension((int) jta.getSize().getWidth(), (pcm.getFrameHeight()/5))); 
						panelPageEnd.remove(jta);
						panelPageEnd.add(jsp2, BorderLayout.CENTER);
						panelPageEnd.revalidate();
						repaintPanel();						
						jsp2.getVerticalScrollBar().setValue(jsp2.getVerticalScrollBar().getMaximum());
						jspActive = true;
						
						jta.setCaretPosition(position);
						jta.revalidate();
						jta.requestFocus();
					}else if (jta.getSize().getHeight() <= (pcm.getFrameHeight()/5) && jspActive == true){
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
			
			
			PanelSend ps = new PanelSend();
			ps.setCursor(new Cursor(Cursor.HAND_CURSOR));
			ps.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent arg0) {
					pcm.addMessageFromUser(jta.getText().replaceAll("\n" , " "));
					jta.setText("");
					jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum()+50);
				}
			});
			panelPageEnd.add(ps, BorderLayout.LINE_END);
		}
		add(panelPageEnd, BorderLayout.PAGE_END);
		
		jsp = new JScrollPane(pcm, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setOpaque(false);
		jsp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				pcm.repaint();
				jsp.getViewport().repaint();
				
			}
		});
		add(jsp, BorderLayout.CENTER);
		
		pcwt = new PanelChatWindowTop(userProfile);
		add(pcwt, BorderLayout.PAGE_START);
		
	}

	@Override
	public void run() {
		initGUI();
		
	}
	
	private void repaintPanel() {
		pcm.repaintFrame();
	}

}
