package jaims_development_studio.jaims.client.gui.settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.TextMessage;

public class PanelChatBackgroundSettings extends SettingPanelsParent {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private GUIMain				guiMain;
	private Image				img;
	private JPanel				pShowMessages;

	public PanelChatBackgroundSettings(GUIMain guiMain) {

		this.guiMain = guiMain;
		img = guiMain.getChatBackground();
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		setBackground(new Color(guiMain.getSettings().getChatBackground()));
		JPanel pBack = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(guiMain.getSettings().getChatBackground()));
				g.fillRect(0, 0, getWidth(), getHeight());

				g.drawImage(img, getWidth() / 2 - img.getWidth(this) / 2, getHeight() / 2 - img.getHeight(this) / 2,
						null);
			}
		};
		pBack.setOpaque(false);
		pBack.setLayout(new BoxLayout(pBack, BoxLayout.PAGE_AXIS));
		pBack.setBorder(new EmptyBorder(5, 30, 5, 30));
		{
			pShowMessages = new JPanel();
			pShowMessages.setBackground(new Color(0, 0, 0, 0));
			pShowMessages.setOpaque(false);
			pShowMessages.setLayout(new BoxLayout(pShowMessages, BoxLayout.PAGE_AXIS));
			{
				pShowMessages.add(Box.createRigidArea(new Dimension(0, 5)));

				TextMessage tm = new TextMessage("I'm a test message", guiMain, false);
				JPanel ptm = new JPanel();
				ptm.setLayout(new BoxLayout(ptm, BoxLayout.LINE_AXIS));
				ptm.setBackground(new Color(0, 0, 0, 0));
				ptm.setOpaque(false);
				ptm.add(tm);
				ptm.add(Box.createHorizontalGlue());
				pShowMessages.add(ptm);
				pShowMessages.add(Box.createRigidArea(new Dimension(0, 5)));

				TextMessage tm2 = new TextMessage("I'm a test message", guiMain, true);
				JPanel ptm2 = new JPanel();
				ptm2.setLayout(new BoxLayout(ptm2, BoxLayout.LINE_AXIS));
				ptm2.setBackground(new Color(0, 0, 0, 0));
				ptm2.setOpaque(false);
				ptm2.add(Box.createHorizontalGlue());
				ptm2.add(tm2);
				pShowMessages.add(ptm2);
				pShowMessages.add(Box.createVerticalGlue());
			}

			JScrollPane jsp = new JScrollPane(pShowMessages, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.getViewport().setBackground(new Color(0, 0, 0, 0));
			jsp.setBackground(new Color(0, 0, 0, 0));
			jsp.setBorder(null);
			pBack.add(jsp);

		}
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				img = scaleMaintainRatio();
				pBack.repaint();
				pShowMessages.revalidate();
				pShowMessages.repaint();

			}
		});
		add(pBack, BorderLayout.CENTER);

		JPanel pBottom = new JPanel();
		pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.PAGE_AXIS));
		pBottom.setBackground(Color.DARK_GRAY);
		pBottom.setBorder(new EmptyBorder(10, 10, 10, 10));
		{
			pBottom.add(Box.createVerticalGlue());

			JPanel rbPanel = new JPanel();
			rbPanel.setBackground(Color.DARK_GRAY);
			rbPanel.setLayout(new BoxLayout(rbPanel, BoxLayout.PAGE_AXIS));
			rbPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
			{
				JRadioButton jrb1 = new JRadioButton("Scale along width");
				jrb1.setAlignmentX(Component.LEFT_ALIGNMENT);
				jrb1.setBackground(new Color(0, 0, 0, 0));
				jrb1.setOpaque(false);
				jrb1.setForeground(Color.WHITE);
				jrb1.setFont(guiMain.getSettings().getOwnFont());
				jrb1.setMnemonic(KeyEvent.VK_W);
				jrb1.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						guiMain.getSettings().setImageResizeHint(Settings.RESIZE_ALONG_WIDTH);
						img = scaleMaintainRatio();
						pBack.repaint();

					}
				});
				rbPanel.add(Box.createRigidArea(new Dimension(0, 10)));
				rbPanel.add(jrb1);

				JRadioButton jrb2 = new JRadioButton("Scale along height");
				jrb2.setAlignmentX(Component.LEFT_ALIGNMENT);
				jrb2.setBackground(new Color(0, 0, 0, 0));
				jrb2.setOpaque(false);
				jrb2.setForeground(Color.WHITE);
				jrb2.setFont(guiMain.getSettings().getOwnFont());
				jrb2.setMnemonic(KeyEvent.VK_H);
				jrb2.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						guiMain.getSettings().setImageResizeHint(Settings.RESIZE_ALONG_HEIGHT);
						img = scaleMaintainRatio();
						pBack.repaint();

					}
				});
				rbPanel.add(Box.createRigidArea(new Dimension(0, 5)));
				rbPanel.add(jrb2);

				JRadioButton jrb3 = new JRadioButton("Fill space");
				jrb3.setAlignmentX(Component.LEFT_ALIGNMENT);
				jrb3.setBackground(new Color(0, 0, 0, 0));
				jrb3.setOpaque(false);
				jrb3.setForeground(Color.WHITE);
				jrb3.setFont(guiMain.getSettings().getOwnFont());
				jrb3.setMnemonic(KeyEvent.VK_F);
				jrb3.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						guiMain.getSettings().setImageResizeHint(Settings.RESIZE_FILL_SCREEN);
						img = scaleMaintainRatio();
						pBack.repaint();

					}
				});
				rbPanel.add(Box.createRigidArea(new Dimension(0, 5)));
				rbPanel.add(jrb3);

				if (guiMain.getSettings().getImageResizeHint() == Settings.RESIZE_ALONG_HEIGHT) {
					jrb2.setSelected(true);
				} else if (guiMain.getSettings().getImageResizeHint() == Settings.RESIZE_ALONG_WIDTH) {
					jrb1.setSelected(true);
				} else {
					jrb3.setSelected(true);
				}
				ButtonGroup bg = new ButtonGroup();
				bg.add(jrb1);
				bg.add(jrb2);
				bg.add(jrb3);
			}
			pBottom.add(Box.createRigidArea(new Dimension(0, 5)));
			pBottom.add(rbPanel);
			pBottom.add(Box.createVerticalGlue());

			JPanel btPanel = new JPanel();
			btPanel.setBackground(Color.DARK_GRAY);
			btPanel.setLayout(new BoxLayout(btPanel, BoxLayout.LINE_AXIS));
			{
				btPanel.add(Box.createHorizontalGlue());
				btPanel.add(Box.createRigidArea(new Dimension(1, 55)));

				RoundButton rb = new RoundButton(Color.GRAY, "Select a background image", btPanel, guiMain);
				rb.setAlignmentX(Component.CENTER_ALIGNMENT);
				rb.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

					}
				});
				btPanel.add(rb);
				btPanel.add(Box.createRigidArea(new Dimension(1, 55)));
				btPanel.add(Box.createHorizontalGlue());
			}
			pBottom.add(Box.createRigidArea(new Dimension(0, 10)));
			pBottom.add(btPanel);

			JPanel pBC = new JPanel();
			pBC.setLayout(new BoxLayout(pBC, BoxLayout.LINE_AXIS));
			pBC.setBackground(Color.DARK_GRAY);
			pBC.setAlignmentX(Component.CENTER_ALIGNMENT);
			pBC.setBorder(new EmptyBorder(5, 10, 5, 5));
			{
				JPanel p = new JPanel() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void paintComponent(Graphics g) {

						Graphics2D g2 = (Graphics2D) g;
						g2.setColor(getBackground());
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 35, 35);
						g2.setColor(Color.WHITE);
						g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 35, 35);
					}
				};

				RoundButton rb2 = new RoundButton(Color.GRAY, "Choose a background color", pBC, guiMain);
				rb2.setAlignmentX(Component.CENTER_ALIGNMENT);
				rb2.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						Color c = JColorChooser.showDialog(pBack, "Choose a message's background color",
								new Color(191, 225, 14));
						if (c != null)
							guiMain.getSettings().setChatBackground(c.getRGB());
						p.setBackground(new Color(guiMain.getSettings().getChatBackground()));
						p.repaint();
						setBackground(new Color(guiMain.getSettings().getChatBackground()));
						repaint();

						guiMain.updateSettings();
					}
				});
				pBC.add(Box.createHorizontalGlue());
				pBC.add(Box.createRigidArea(new Dimension(36, 55)));
				pBC.add(rb2);
				pBC.add(Box.createHorizontalGlue());

				p.setBackground(new Color(guiMain.getSettings().getChatBackground()));
				p.setMinimumSize(new Dimension(36, 36));
				p.setPreferredSize(new Dimension(36, 36));
				p.setMaximumSize(new Dimension(36, 36));
				p.setCursor(new Cursor(Cursor.HAND_CURSOR));
				p.setToolTipText("Reset to default");
				p.setFocusable(true);
				p.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {

						guiMain.getSettings().setChatBackground(Color.DARK_GRAY.getRGB());
						p.setBackground(new Color(guiMain.getSettings().getChatBackground()));
						p.repaint();
						setBackground(new Color(guiMain.getSettings().getChatBackground()));
						repaint();

						guiMain.updateSettings();
					}
				});
				pBC.add(p);
			}

			pBottom.add(Box.createRigidArea(new Dimension(0, 10)));
			pBottom.add(pBC);
			pBottom.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		JScrollPane jsp = new JScrollPane(pBottom, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.getViewport().setBackground(Color.DARK_GRAY);
		jsp.setBorder(new LineBorder(Color.DARK_GRAY));
		add(jsp, BorderLayout.PAGE_END);
	}

	private Image scaleMaintainRatio() {

		if (guiMain.getSettings().getImageResizeHint() == Settings.RESIZE_ALONG_WIDTH) {
			double ratio = (double) img.getHeight(this) / img.getWidth(this);
			return img.getScaledInstance(getWidth(), (int) (ratio * getWidth()), Image.SCALE_SMOOTH);
		} else if (guiMain.getSettings().getImageResizeHint() == Settings.RESIZE_ALONG_HEIGHT) {
			double ratio = (double) img.getWidth(this) / img.getHeight(this);
			return img.getScaledInstance((int) (ratio * getHeight()), getHeight(), Image.SCALE_SMOOTH);
		} else {
			if (getWidth() > getHeight()) {
				double ratio = (double) img.getWidth(this) / img.getHeight(this);
				return img.getScaledInstance((int) (ratio * getHeight()), getHeight(), Image.SCALE_SMOOTH);
			} else {
				double ratio = (double) img.getHeight(this) / img.getWidth(this);
				return img.getScaledInstance(getWidth(), (int) (ratio * getWidth()), Image.SCALE_SMOOTH);
			}
		}
	}

}
