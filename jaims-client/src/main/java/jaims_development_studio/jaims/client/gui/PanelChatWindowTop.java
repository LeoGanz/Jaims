package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.chatObjects.ClientProfile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;

public class PanelChatWindowTop extends JPanel {

	private Image profileImage;

	public PanelChatWindowTop(ClientProfile userProfile) {

		initGUI(userProfile);

	}

	private void initGUI(ClientProfile userProfile) {

		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(new LineBorder(Color.black, 1));

		profileImage = null;
		try {
			profileImage = ImageIO.read(new ByteArrayInputStream(getPicture(userProfile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		profileImage = scaleMaintainAspectRatio(profileImage);

		JPanel p = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.setClip(new RoundRectangle2D.Double(4, 4, 32, 32, 15, 15));
				g2d.drawImage(profileImage, 4, 4, 32, 32, this);
				g2d.setColor(Color.BLACK);
				g2d.setClip(0, 0, getWidth(), getHeight());
				g2d.setStroke(new BasicStroke(1.5F));
				g2d.drawRoundRect(3, 3, 32, 32, 15, 15);

				g2d.dispose();
			}

		};
		p.setPreferredSize(new Dimension(39, 39));
		add(p);
		// add(Box.createRigidArea(new Dimension(0, 15)));

		JLabel lblUsername = new JLabel(userProfile.getNickname(), JLabel.LEFT);
		lblUsername.setFont(new Font("Calibri", Font.BOLD, 15));
		lblUsername.setPreferredSize(
				new Dimension(lblUsername.getFontMetrics(lblUsername.getFont()).stringWidth(userProfile.getNickname()),
						lblUsername.getFontMetrics(lblUsername.getFont()).getHeight()));
		lblUsername.setMaximumSize(lblUsername.getPreferredSize());
		add(lblUsername);
		add(Box.createHorizontalGlue());
	}

	private Image scaleMaintainAspectRatio(Image image) {

		image.getHeight(this);
		image.getWidth(this);
		Image returnImg = image.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		return returnImg;
	}

	private byte[] getPicture(ClientProfile up) {

		ResultSet rs;
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(up.getProfilePic());
			ps.setObject(1, up.getUuid());
			rs = ps.executeQuery();
			con.commit();

			rs.next();
			byte[] arr = rs.getBytes(1);

			return arr;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
