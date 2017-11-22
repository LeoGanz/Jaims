package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class TextAreaMessage extends JPanel {
	
	JaimsFrame jf;
	String longestLine = "";
	boolean multipleLines = false;
	ArrayList<String> lineStrings = new ArrayList<>();
	JTextArea jta;
	
	public TextAreaMessage(String message, JaimsFrame jf, JPanel panel) {
		super();
		this.jf = jf;
		setLayout(new BorderLayout());
		initGUI(message, panel);
	}
	
	private void initGUI(String message, JPanel panel) {
		//setBackground(new Color(191, 225, 148));
		jta = new JTextArea();
		jta.setOpaque(false);
		//jta.setMargin(new Insets(5, 5, 5, 5));
		
		jta.setFont(new Font("Calibri", Font.PLAIN, 14));
		
		int height = getStringHeight(message, jta)+10;
		int width = getStringWidth(message, jta) + 15;
		
		jta.setPreferredSize(new Dimension(width, height));
		jta.setMaximumSize(getPreferredSize());
		jta.setEditable(false);
		setCursor(new Cursor(Cursor.TEXT_CURSOR));
		setBorder(new RoundBorder(width, height, Color.GRAY));
		//setBorder(new LineBorder(Color.BLACK));
		if (multipleLines) {
			panel.addComponentListener(new ComponentAdapter() {
				
				@Override
				public void componentShown(ComponentEvent e) {
					int height = getStringHeight(message, jta)+5;
					int width = getStringWidth(message, jta)+10;
					
					setPreferredSize(new Dimension(width, height));
					setMaximumSize(getPreferredSize());
					revalidate();
					repaint();
					panel.repaint();
				}
				
				@Override
				public void componentResized(ComponentEvent e) {
					int height = getStringHeight(message, jta)+5;
					int width = getStringWidth(message, jta)+10;
					
					setPreferredSize(new Dimension(width, height));
					setMaximumSize(getPreferredSize());
					revalidate();
					repaint();
					panel.repaint();
				}
			});
		}
		
		setPreferredSize(new Dimension(width, (int) lineStrings.size()*19+15));
		setMaximumSize(getPreferredSize());
		
		add(jta, BorderLayout.CENTER);
	}
	
	private int countLines(String s, FontMetrics fm, JTextArea jta) {
		lineStrings.clear();
		longestLine = ""; 
				
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
			  if (fm.stringWidth(wholeString) > fm.stringWidth(longestLine))
				  longestLine = wholeString;
			  
			  lineStrings.add(wholeString);
			  text += wholeString;
			  text += "\n";
			  wholeString = "";
			  countLines++;
			  wholeString += st;
			  wholeString += " ";
		  }
		 }
		 
		 if (text.equals("")) {
			 lineStrings.add(wholeString);
		  text = wholeString;
		  longestLine = wholeString;
		  jta.setText(text);
		  return 1;
		 }else {
		  text += wholeString;
		  jta.setText(text);
		  multipleLines = true;
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
			return fm.stringWidth(longestLine); 
		 }else {
			 return i;
		 }
	 }
	 
	 @Override
	    public void paintComponent(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g.create();
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        
	       	g2d.setColor(new Color(191, 225, 148));
	        g2d.fillRoundRect(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight(), 20, 20);
	        g2d.setColor(Color.black);
	        g2d.setFont(getFont());
	        g2d.setStroke(new BasicStroke(1.0F));
	        
	        int inset;
	        if (lineStrings.size() == 1) {
	        	inset = (int) ((getHeight() - (lineStrings.size() * (g2d.getFontMetrics(g2d.getFont()).getHeight()-3))));
	        }else {
	        	inset = (int) ((getHeight() - (lineStrings.size() * (g2d.getFontMetrics(g2d.getFont()).getHeight()-1)))/2);
	        }
	        
	        int x = 8;
	        int y = (int) getLocation().getY()+inset;
//	        for (int i = 0; i < lineStrings.size(); i++) {
//	        	
//	        	g2d.drawString(lineStrings.get(i), x, y);
//	        	y += g2d.getFontMetrics(g2d.getFont()).getHeight()+1;
//	        }
	        g2d.dispose();
	    }

}
