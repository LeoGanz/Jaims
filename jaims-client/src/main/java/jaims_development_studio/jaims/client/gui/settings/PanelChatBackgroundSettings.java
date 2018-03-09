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
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.TextMessage;

public class PanelChatBackgroundSettings extends SettingPanelsParent {

	private GUIMain	guiMain;
	private Image	img;
	private boolean	height	= true, needsRepainting = true;

	public PanelChatBackgroundSettings(GUIMain guiMain) {

		this.guiMain = guiMain;
		img = guiMain.getChatBackground();
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		setBackground(new Color(guiMain.getSettings().getChatBackground()));
		JLabel pBack = new JLabel(new ImageIcon(img));
		pBack.setBackground(new Color(guiMain.getSettings().getChatBackground()));
		pBack.setOpaque(false);
		pBack.setLayout(new BoxLayout(pBack, BoxLayout.PAGE_AXIS));
		pBack.setBorder(new EmptyBorder(5, 30, 5, 30));
		{
			JPanel pTM = new JPanel();
			pTM.setLayout(new BoxLayout(pTM, BoxLayout.LINE_AXIS));
			pTM.setBackground(new Color(0, 0, 0, 0));
			TextMessage tm = new TextMessage("I'm a test message", guiMain, false);
			pTM.add(tm);
			pTM.add(Box.createHorizontalGlue());
			pBack.add(pTM);

			JPanel pTM2 = new JPanel();
			pTM2.setLayout(new BoxLayout(pTM2, BoxLayout.LINE_AXIS));
			pTM2.setBackground(new Color(0, 0, 0, 0));
			TextMessage tm2 = new TextMessage("I'm a test message", guiMain, true);
			pTM2.add(Box.createHorizontalGlue());
			pTM2.add(tm2);
			pBack.add(Box.createRigidArea(new Dimension(0, 5)));
			pBack.add(pTM2);
		}
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				img = scaleMaintainRatio();
				needsRepainting = true;
				pBack.setIcon(new ImageIcon(img));
				pBack.revalidate();
				pBack.repaint();

			}
		});
		add(pBack, BorderLayout.CENTER);

		JPanel pBottom = new JPanel();
		pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.PAGE_AXIS));
		pBottom.setBackground(Color.DARK_GRAY);
		pBottom.setAlignmentX(Component.CENTER_ALIGNMENT);
		pBottom.setBorder(new EmptyBorder(10, 10, 10, 10));
		{
			pBottom.add(Box.createVerticalGlue());

			RoundButton rb = new RoundButton(Color.GRAY, "Select a background image", pBottom, guiMain);
			rb.setAlignmentX(Component.CENTER_ALIGNMENT);
			rb.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

				}
			});
			pBottom.add(rb);

			JRadioButton jrb1 = new JRadioButton("Scale along width");
			jrb1.setAlignmentX(Component.CENTER_ALIGNMENT);
			jrb1.setBackground(new Color(0, 0, 0, 0));
			jrb1.setOpaque(false);
			jrb1.setFont(guiMain.getSettings().getOwnFont());
			jrb1.setMnemonic(KeyEvent.VK_W);
			jrb1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					height = false;
					img = scaleMaintainRatio();
					needsRepainting = true;
					pBack.repaint();

				}
			});
			pBottom.add(Box.createRigidArea(new Dimension(0, 10)));
			pBottom.add(jrb1);

			JRadioButton jrb2 = new JRadioButton("Scale along height");
			jrb2.setAlignmentX(Component.CENTER_ALIGNMENT);
			jrb2.setBackground(new Color(0, 0, 0, 0));
			jrb2.setOpaque(false);
			jrb2.setFont(guiMain.getSettings().getOwnFont());
			jrb2.setMnemonic(KeyEvent.VK_H);
			jrb2.setSelected(true);
			jrb2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					height = true;
					img = scaleMaintainRatio();
					needsRepainting = true;
					pBack.repaint();

				}
			});
			pBottom.add(Box.createRigidArea(new Dimension(0, 5)));
			pBottom.add(jrb2);
			pBottom.add(Box.createVerticalGlue());

			ButtonGroup bg = new ButtonGroup();
			bg.add(jrb1);
			bg.add(jrb2);

			JPanel pBC = new JPanel();
			pBC.setLayout(new BorderLayout());
			pBC.setBackground(Color.DARK_GRAY);
			pBC.setAlignmentX(Component.CENTER_ALIGNMENT);
			pBC.setBorder(new EmptyBorder(5, 10, 5, 5));
			{
				JPanel p = new JPanel() {
					@Override
					public void paintComponent(Graphics g) {

						Graphics2D g2 = (Graphics2D) g;
						g2.setColor(getBackground());
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
					}
				};

				RoundButton rb2 = new RoundButton(Color.GRAY, "Choose a background color", pBC, guiMain);
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
				pBC.add(rb2, BorderLayout.CENTER);

				p.setBackground(new Color(guiMain.getSettings().getChatBackground()));
				p.setMinimumSize(new Dimension(35, 35));
				p.setPreferredSize(new Dimension(35, 35));
				p.setMaximumSize(new Dimension(35, 35));
				p.setCursor(new Cursor(Cursor.HAND_CURSOR));
				p.setToolTipText("Reset to default");
				p.setFocusable(true);
				p.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {

						guiMain.getSettings().setChatBackground(new Color(191, 225, 14).getRGB());
						p.setBackground(new Color(guiMain.getSettings().getChatBackground()));
						p.repaint();
						setBackground(new Color(guiMain.getSettings().getChatBackground()));
						repaint();

						guiMain.updateSettings();
					}
				});
				pBC.add(p, BorderLayout.LINE_END);
			}
			pBottom.add(Box.createRigidArea(new Dimension(0, 10)));
			pBottom.add(pBC);
		}
		add(pBottom, BorderLayout.PAGE_END);
	}

	private Image scaleMaintainRatio() {

		double ratio;
		if (height) {
			ratio = (double) img.getWidth(this) / img.getHeight(this);
			return guiMain.getChatBackground().getScaledInstance((int) (getHeight() * ratio), getHeight(),
					Image.SCALE_SMOOTH);
		} else {
			ratio = (double) img.getHeight(this) / img.getWidth(this);
			return guiMain.getChatBackground().getScaledInstance(getWidth(), (int) (getWidth() * ratio),
					Image.SCALE_SMOOTH);
		}
	}

}
