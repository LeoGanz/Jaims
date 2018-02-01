package jaims_development_studio.jaims.client.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
<<<<<<< HEAD
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelAddUser extends JPanel {

	private ClientMain	cm;
	private JTextField	jta;
	private JPanel		panelShowUser, panelUsers;
	private JScrollPane	jsp;

	public PanelAddUser(ClientMain cm) {

		this.cm = cm;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());

		JPanel panelTop = new JPanel();
		panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.LINE_AXIS));
		panelTop.setBorder(new EmptyBorder(5, 10, 5, 10));
		panelTop.setBorder(new MatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
		{
			JPanel back = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {

					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.CYAN);
					g2.setStroke(new BasicStroke(2));
					g2.drawOval(2, 2, 30, 30);
				}
			};
			back.setPreferredSize(new Dimension(34, 34));
			back.setMaximumSize(back.getPreferredSize());
			back.setCursor(new Cursor(Cursor.HAND_CURSOR));
			back.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {

					cm.removePanelAddUser();
				}

			});
			panelTop.add(back);
			panelTop.add(Box.createRigidArea(new Dimension(5, 0)));

			JPanel panelEntries = new JPanel();
			panelEntries.setLayout(new BoxLayout(panelEntries, BoxLayout.PAGE_AXIS));
			// panelEntries.setAlignmentX(Component.CENTER_ALIGNMENT);
			{
				JLabel lblAddUser = new JLabel("Suchen Sie nach einem neuen Kontakt:", JLabel.LEFT);
				lblAddUser.setAlignmentX(Component.LEFT_ALIGNMENT);
				lblAddUser.setFont(new Font("Arial", Font.PLAIN, 13));
				panelEntries.add(lblAddUser);
				panelEntries.add(Box.createRigidArea(new Dimension(0, 2)));

				jta = new JTextField();
				jta.setBorder(new LineBorder(Color.GRAY));
				jta.setFont(new Font("Arial", Font.BOLD, 16));
				jta.setAlignmentX(Component.CENTER_ALIGNMENT);
				jta.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(KeyEvent e) {

						sendEnquiry(jta.getText());

					}

				});
				panelEntries.add(jta);
				panelEntries.add(Box.createRigidArea(new Dimension(0, 2)));
			}
			panelTop.add(Box.createRigidArea(new Dimension(0, 3)));
			panelTop.add(panelEntries);
			panelTop.add(Box.createRigidArea(new Dimension(10, 48)));

			JPanel magnifyingGlass = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {

					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.CYAN);
					g2.setStroke(new BasicStroke(2));
					g2.drawOval(2, 2, 30, 30);
				}
			};
			magnifyingGlass.setPreferredSize(new Dimension(34, 34));
			magnifyingGlass.setMaximumSize(magnifyingGlass.getPreferredSize());
			magnifyingGlass.setCursor(new Cursor(Cursor.HAND_CURSOR));
			magnifyingGlass.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {

					sendEnquiry(jta.getText());
					Timer t = new Timer(5, new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {

							panelShowUser.setLocation(panelShowUser.getX() + 10, panelShowUser.getY());

							if (panelShowUser.getX() >= cm.getJaimsFrame().getWidth())
								((Timer) e.getSource()).stop();

						}
					});
					t.start();
				}

			});
			panelTop.add(magnifyingGlass);
			panelTop.add(Box.createRigidArea(new Dimension(2, 0)));
		}
		add(panelTop, BorderLayout.PAGE_START);

		panelShowUser = new JPanel();
		panelShowUser.setLayout(new BoxLayout(panelShowUser, BoxLayout.LINE_AXIS));
		panelShowUser.setBorder(new EmptyBorder(5, 10, 5, 10));
		{
			panelShowUser.add(Box.createHorizontalGlue());

			panelUsers = new JPanel();
			panelUsers.setLayout(new BoxLayout(panelUsers, BoxLayout.PAGE_AXIS));
			// panelUsers.setBorder(new MatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
			panelUsers.setBackground(Color.WHITE);
			// panelUsers.setPreferredSize(new Dimension(250, cm.getJaimsFrame().getHeight()
			// - 100));
			// panelUsers.setMaximumSize(panelUsers.getPreferredSize());

			jsp = new JScrollPane(panelUsers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.setPreferredSize(new Dimension(270, cm.getJaimsFrame().getHeight() - 100));
			jsp.setMaximumSize(jsp.getPreferredSize());
			jsp.setBackground(Color.WHITE);
			jsp.setBorder(new MatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
			jsp.getVerticalScrollBar().setBackground(Color.WHITE);
			jsp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {

					jsp.getVerticalScrollBar().repaint();
					jsp.getViewport().repaint();

				}
			});
			panelShowUser.add(jsp);
			panelShowUser.add(Box.createHorizontalGlue());

			cm.getJaimsFrame().addComponentListener(new ComponentAdapter() {

				@Override
				public void componentResized(ComponentEvent e) {

					jsp.setPreferredSize(new Dimension(270, cm.getJaimsFrame().getHeight() - 100));
					jsp.setMaximumSize(jsp.getPreferredSize());

					jsp.revalidate();
					panelShowUser.revalidate();
					panelShowUser.repaint();
=======
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import jaims_development_studio.jaims.client.gui.messagePanels.PanelTextField;

public class PanelAddUser extends JPanel {
	private GUIMain		guiMain;
	private JTextField	jta;
	private JPanel		panelShowUser, panelUsers;
	private JScrollPane	jsp;

	public PanelAddUser(GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		setBackground(Color.DARK_GRAY);

		JPanel panelTop = new JPanel();
		panelTop.setBackground(Color.DARK_GRAY);
		panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.LINE_AXIS));
		panelTop.setBorder(new CompoundBorder(new EmptyBorder(3, 0, 0, 0), new MatteBorder(0, 0, 2, 0, Color.WHITE)));
		{
			JPanel back = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {

					g.setColor(new Color(0, 0, 0, 0));
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.CYAN);
					g2.setStroke(new BasicStroke(2));
					g2.drawOval(2, 2, 30, 30);
				}
			};
			back.setPreferredSize(new Dimension(34, 34));
			back.setMaximumSize(back.getPreferredSize());
			back.setCursor(new Cursor(Cursor.HAND_CURSOR));
			back.setAlignmentX(Component.LEFT_ALIGNMENT);
			back.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {

					guiMain.removePanelAddUser();
				}

			});
			panelTop.add(Box.createRigidArea(new Dimension(5, 0)));
			panelTop.add(back);
			panelTop.add(Box.createHorizontalGlue());

			JPanel panelEntries = new JPanel();
			panelEntries.setBackground(Color.DARK_GRAY);
			panelEntries.setLayout(new BoxLayout(panelEntries, BoxLayout.PAGE_AXIS));
			panelEntries.setAlignmentX(Component.CENTER_ALIGNMENT);
			{
				JLabel lblAddUser = new JLabel("Suchen Sie nach einem neuen Kontakt:", JLabel.CENTER);
				lblAddUser.setAlignmentX(Component.CENTER_ALIGNMENT);
				lblAddUser.setFont(new Font("Arial", Font.PLAIN, 13));
				lblAddUser.setForeground(Color.WHITE);
				lblAddUser.setOpaque(false);
				panelEntries.add(lblAddUser);
				panelEntries.add(Box.createRigidArea(new Dimension(0, 2)));

				jta = new JTextField();
				jta.setFont(new Font("Arial", Font.BOLD, 16));
				jta.setHorizontalAlignment(JTextField.CENTER);
				jta.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(KeyEvent e) {

						sendEnquiry(jta.getText());

					}

				});
				jta.setMinimumSize(new Dimension(330, 30));
				jta.setPreferredSize(new Dimension(330, 30));
				jta.setMaximumSize(new Dimension(330, 30));
				jta.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
				panelEntries.add(new PanelTextField(jta));
				panelEntries.add(Box.createRigidArea(new Dimension(0, 2)));
				panelTop.add(panelEntries);
				panelTop.add(Box.createHorizontalGlue());

				JPanel magnifyingGlass = new JPanel() {
					@Override
					public void paintComponent(Graphics g) {

						g.setColor(new Color(0, 0, 0, 0));
						g.fillRect(0, 0, getWidth(), getHeight());

						Graphics2D g2 = (Graphics2D) g;
						g2.setColor(Color.CYAN);
						g2.setStroke(new BasicStroke(2));
						g2.drawOval(2, 2, 30, 30);
					}
				};
				magnifyingGlass.setPreferredSize(new Dimension(34, 34));
				magnifyingGlass.setMaximumSize(magnifyingGlass.getPreferredSize());
				magnifyingGlass.setCursor(new Cursor(Cursor.HAND_CURSOR));
				magnifyingGlass.setAlignmentX(Component.RIGHT_ALIGNMENT);
				magnifyingGlass.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseReleased(MouseEvent e) {

						sendEnquiry(jta.getText());
					}

				});
				panelTop.add(magnifyingGlass);
				panelTop.add(Box.createRigidArea(new Dimension(5, 0)));
			}
		}
		add(panelTop, BorderLayout.PAGE_START);

		panelShowUser = new JPanel();
		panelShowUser.setLayout(new BoxLayout(panelShowUser, BoxLayout.LINE_AXIS));
		panelShowUser.setBackground(Color.DARK_GRAY);
		panelShowUser.setBorder(new EmptyBorder(5, 0, 5, 0));
		{
			panelUsers = new JPanel();
			panelUsers.setLayout(new BoxLayout(panelUsers, BoxLayout.PAGE_AXIS));
			panelUsers.setBackground(Color.DARK_GRAY);

			jsp = new JScrollPane(panelUsers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.setBackground(Color.DARK_GRAY);
			jsp.getVerticalScrollBar().setBackground(Color.WHITE);
			jsp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {

					jsp.getVerticalScrollBar().repaint();
					jsp.getViewport().repaint();

				}
			});
			panelShowUser.add(jsp);

			guiMain.getJaimsFrame().addComponentListener(new ComponentAdapter() {

				@Override
				public void componentResized(ComponentEvent e) {
>>>>>>> refs/remotes/origin/Bu88le

				}
			});
		}
		add(panelShowUser, BorderLayout.CENTER);
	}

	private void sendEnquiry(String s) {

		System.out.println(s);
	}
}
