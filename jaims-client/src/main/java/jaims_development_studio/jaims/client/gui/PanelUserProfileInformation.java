package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import jaims_development_studio.jaims.client.chatObjects.ClientProfile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelUserProfileInformation extends JPanel {

	private ClientMain			cm;
	private ClientProfile		userProfile;
	private JPanel				panelUserInfo, panelStatus, panelDescription, panelStats, panelProfilePicture;
	private JTextArea			jtaStatus, jtaDescription;
	private PanelChatMessages	pcm;
	private JScrollPane			jsp;
	private JTable				tableStats;

	public PanelUserProfileInformation(ClientMain cm, ClientProfile userProfile, PanelChatMessages pcm) {

		this.pcm = pcm;
		this.cm = cm;
		this.userProfile = userProfile;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		setBackground(Color.white);
		setBorder(new EmptyBorder(0, 0, 0, 8));
		panelUserInfo = new JPanel();
		panelUserInfo.setLayout(new BoxLayout(panelUserInfo, BoxLayout.PAGE_AXIS));
		{
			panelProfilePicture = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {

					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					int width = (cm.getJaimsFrame().getWidth() - 260) / 2;
					int startingPoint = width - (width / 2);

					setPreferredSize(new Dimension(width * 2, width + 3));

					g2d.setColor(Color.BLACK);
					g2d.drawRoundRect(startingPoint, 0, width + 1, width + 2, 15, 15);
					g2d.setClip(new RoundRectangle2D.Double(startingPoint + 1, 1, width, width, 15, 15));

					g2d.drawImage(getPicture(userProfile).getScaledInstance(width, width, Image.SCALE_SMOOTH),
							startingPoint + 1, 1, this);

					g2d.dispose();
				}
			};
			panelUserInfo.add(panelProfilePicture);
			panelUserInfo.add(Box.createRigidArea(new Dimension(0, 5)));

			panelStatus = new JPanel();
			panelStatus
					.setBorder(new CompoundBorder(new TitledBorder("Status"), new MatteBorder(1, 0, 1, 0, Color.GRAY)));
			panelStatus.setLayout(new BoxLayout(panelStatus, BoxLayout.LINE_AXIS));
			{
				panelStatus.add(Box.createRigidArea(new Dimension(10, 50)));

				jtaStatus = new JTextArea(userProfile.getStatus());
				jtaStatus.setBorder(null);
				jtaStatus.setOpaque(false);
				jtaStatus.setLineWrap(true);
				jtaStatus.setWrapStyleWord(true);
				jtaStatus.setEditable(false);
				jtaStatus.setCursor(new Cursor(Cursor.TEXT_CURSOR));

				panelStatus.add(jtaStatus);
				panelStatus.add(Box.createHorizontalGlue());
				panelStatus.add(Box.createRigidArea(new Dimension(0, 50)));
			}
			panelUserInfo.add(panelStatus);
			panelUserInfo.add(Box.createVerticalGlue());

			panelDescription = new JPanel();
			panelDescription.setBorder(new CompoundBorder(new TitledBorder("Über " + userProfile.getNickname()),
					new MatteBorder(0, 0, 1, 0, Color.GRAY)));
			panelDescription.setLayout(new BoxLayout(panelDescription, BoxLayout.LINE_AXIS));
			{
				panelDescription.add(Box.createRigidArea(new Dimension(10, 50)));

				jtaDescription = new JTextArea(userProfile.getDescription());
				jtaDescription.setBorder(null);
				jtaDescription.setOpaque(false);
				jtaDescription.setLineWrap(true);
				jtaDescription.setWrapStyleWord(true);
				jtaDescription.setEditable(false);
				jtaDescription.setCursor(new Cursor(Cursor.TEXT_CURSOR));

				panelDescription.add(jtaDescription);
				panelDescription.add(Box.createHorizontalGlue());
			}
			panelUserInfo.add(panelDescription);
			Box.createVerticalGlue();

			panelStats = new JPanel();
			panelStats.setBorder(
					new CompoundBorder(new TitledBorder("Chat-Statistiken"), new MatteBorder(0, 0, 1, 0, Color.GRAY)));
			panelStats.setLayout(new BoxLayout(panelStats, BoxLayout.PAGE_AXIS));
			{
				panelStats.add(Box.createRigidArea(new Dimension(0, 5)));
				String[] arr = {"", ""};
				Object[][] data = {{"Insgesamt versandte Nachrichten: ", "<html><b>" + pcm.getAllMessages()},
						{"Davon Textnachrichten: ", "<html><b>" + pcm.getTextMessages()},
						{"Davon Sprachnachrichten: ", "<html><b>" + pcm.getVoiceMessages()},
						{"Selbst versandte Textnachrichten: ", "<html><b>" + pcm.getOwnTextMessages()},
						{"Von " + userProfile.getNickname() + " versandte Textnachrichten: ",
								"<html><b>" + pcm.getContactTextMessages()},
						{"Wörter benutzt von " + ClientMain.userProfile.getNickname() + ": ",
								"<html><b>" + pcm.getOwnWords()},
						{"Wörter benutzt von " + userProfile.getNickname() + ": ", "<html><b>" + pcm.getContactWords()},
						{"Insgesamt benutze Wörter: ", "<html><b>" + pcm.getAllWords()},
						{"Selbst versandte Sprachnachrichten: ", "<html><b>" + pcm.getOwnVoiceMessages()},
						{"Von " + userProfile.getNickname() + " versandte Sprachnachrichten: ",
								"<html><b>" + pcm.getContactVoiceMessages()}};
				tableStats = new JTable(data, arr);
				tableStats.setSelectionModel(new NullSelectionModel());
				tableStats.setFont(new Font("SansSerif", Font.PLAIN, 13));
				tableStats.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				tableStats.getColumnModel().getColumn(1).setMaxWidth(100);
				tableStats.setBorder(new LineBorder(Color.BLACK));
				panelStats.add(tableStats);
				panelStats.add(Box.createVerticalGlue());

			}
			panelUserInfo.add(panelStats);
			panelUserInfo.add(Box.createVerticalGlue());

			jsp = new JScrollPane(panelUserInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.getVerticalScrollBar().addComponentListener(new ComponentAdapter() {

				@Override
				public void componentShown(ComponentEvent e) {

					panelUserInfo.setBorder(new EmptyBorder(0, 0, 0, jsp.getVerticalScrollBar().getWidth()));
					panelUserInfo.repaint();
				}
			});

			add(jsp, BorderLayout.CENTER);
		}
	}

	private Image getPicture(ClientProfile up) {

		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(up.getProfilePic());
			ps.setObject(1, up.getUuid());
			rs = ps.executeQuery();
			con.commit();

			rs.next();

			return ImageIO.read(new ByteArrayInputStream(rs.getBytes(1)));
		} catch (SQLException e) {
			BufferedImage bi;
			try {
				bi = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/JAIMS_Penguin.png"));
				return bi;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
