package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicSliderUI;

import jaims_development_studio.jaims.client.chatObjects.Profile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.settings.Settings;

public class VoiceMessage extends JPanel{

	String path;
	JLabel currentTime;
	Profile contactProfile;
	boolean own;
	
	public VoiceMessage(Profile contactProfile, String pathToFile, boolean own) {
		path = pathToFile;
		this.contactProfile = contactProfile;
		this.own = own;
		initGUI(contactProfile);
	}
	
	private void initGUI(Profile contactProfile) {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setPreferredSize(new Dimension(250, 50));
		setMaximumSize(getPreferredSize());
		if (own)
			setBorder(new RoundBorder(250, 50, Settings.colorOwnMessageBorder));
		else
			setBorder(new RoundBorder(250, 50, Settings.colorContactMessageBorder));
		
		
		add(Box.createRigidArea(new Dimension(55, 0)));
		long length = getAudioFileLength();
		
		JSlider slider = new JSlider(0, (int) (length/1000), 0);
		slider.setPreferredSize(new Dimension(150, 40));
		slider.setBorder(new RoundBorder((int) slider.getPreferredSize().getWidth(), (int) slider.getPreferredSize().getHeight(), Color.BLACK));
		slider.setUI(new CustomSliderUI(slider));
		if (own)
			slider.setBackground(Settings.colorOwnMessages);
		else
			slider.setBackground(Settings.colorContactMessages);
		add(slider);
		
		add(Box.createHorizontalGlue());
		
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		Date d = new Date(0L);
		
		currentTime = new JLabel(sdf.format(d) + " / ", JLabel.CENTER);
		add(currentTime);
		
		d.setTime(length);
		JLabel maxTime = new JLabel(sdf.format(d));
		add(maxTime);
		
	}
	
	private long getAudioFileLength() {
		AudioFileFormat format;
		try {
			format = AudioSystem.getAudioFileFormat(new File(path));
			float duration = format.getFrameLength() / format.getFormat().getFrameRate();
			return (long) (duration*1000);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	
	private byte[] getPicture(Profile up) {
		ResultSet rs;
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(up.getProfilePicture());
			ps.setObject(1, up.getUuid());
			rs = ps.executeQuery();
			con.commit();
			
			rs.next();
			
			return rs.getBytes(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Image scaleMaintainAspectRatio(Image image) {
        float ratio = (float) image.getHeight(this)/ (float)image.getWidth(this);
        Image returnImg = image.getScaledInstance( 40, 40, Image.SCALE_SMOOTH);
        return returnImg;
    }
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(own) {
			g2d.setColor(Settings.colorOwnMessages);
			g2d.fillRoundRect(0, 1, getWidth()-1, getHeight()-1, 20, 20);
		}else {
			g2d.setColor(Settings.colorContactMessages);
			g2d.fillRoundRect(0, 1, getWidth()-1, getHeight()-1, 20, 20);
		}
		
		g2d.drawImage(scaleMaintainAspectRatio(Toolkit.getDefaultToolkit().createImage(getPicture(contactProfile))), 5, 5, this);
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(4, 4, 41, 41, 8, 8);
	}
	
	
	//-------------------------------------------------------------------------
	//-------------- CUSTOM SLIDER UI CLASS -----------------------------------
	//-------------------------------------------------------------------------
	
	private class CustomSliderUI extends BasicSliderUI {
		private BasicStroke stroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, 
	            BasicStroke.JOIN_ROUND, 0f, new float[]{1f, 2f}, 0f);

	    public CustomSliderUI(JSlider b) {
	        super(b);
	    }

	    @Override
	    public void paint(Graphics g, JComponent c) {
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	                RenderingHints.VALUE_ANTIALIAS_ON);
	        super.paint(g, c);
	    }

	    @Override
	    protected Dimension getThumbSize() {
	        return new Dimension(12, 16);
	    }

	    @Override
	    public void paintTrack(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        Stroke old = g2d.getStroke();
	        g2d.setStroke(stroke);
	        g2d.setPaint(Color.BLACK);
	        if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
	            g2d.drawLine(trackRect.x, trackRect.y + trackRect.height / 2, 
	                    trackRect.x + trackRect.width, trackRect.y + trackRect.height / 2);
	        } else {
	            g2d.drawLine(trackRect.x + trackRect.width / 2, trackRect.y, 
	                    trackRect.x + trackRect.width / 2, trackRect.y + trackRect.height);
	        }
	        g2d.setStroke(old);
	    }

	    @Override
	    public void paintThumb(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        int x1 = thumbRect.x + 2;
	        int x2 = thumbRect.x + thumbRect.width - 2;
	        int width = thumbRect.width - 4;
	        int topY = thumbRect.y + thumbRect.height / 2 - thumbRect.width / 3;
	        GeneralPath shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
	        shape.moveTo(x1, topY);
	        shape.lineTo(x2, topY);
	        shape.lineTo((x1 + x2) / 2, topY + width);
	        shape.closePath();
	        g2d.setPaint(new Color(81, 83, 186));
	        g2d.fill(shape);
	        Stroke old = g2d.getStroke();
	        g2d.setStroke(new BasicStroke(2f));
	        g2d.setPaint(new Color(131, 127, 211));
	        g2d.draw(shape);
	        g2d.setStroke(old);
	    }
	}
	
}
