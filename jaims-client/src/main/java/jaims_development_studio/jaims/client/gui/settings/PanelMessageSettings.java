package jaims_development_studio.jaims.client.gui.settings;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.TextMessage;

public class PanelMessageSettings extends SettingPanelsParent {

	private GUIMain		guiMain;
	private JPanel		panelTop, pTM, pTM2;
	private TextMessage	tm, tm2;

	public PanelMessageSettings(GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());

		panelTop = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(guiMain.getSettings().getChatBackground()));
				g.fillRect(0, 0, getWidth(), getHeight());

			}
		};
		panelTop.setMinimumSize(new Dimension(150, 120));
		panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.PAGE_AXIS));
		panelTop.setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 0, Color.WHITE), new EmptyBorder(5, 30, 5, 30)));
		{
			pTM = new JPanel();
			pTM.setLayout(new BoxLayout(pTM, BoxLayout.LINE_AXIS));
			pTM.setBackground(new Color(0, 0, 0, 0));
			tm = new TextMessage("I'm a test message", guiMain, false);

			pTM.add(tm);
			pTM.add(Box.createHorizontalGlue());
			panelTop.add(pTM);

			pTM2 = new JPanel();
			pTM2.setLayout(new BoxLayout(pTM2, BoxLayout.LINE_AXIS));
			pTM2.setBackground(new Color(0, 0, 0, 0));
			tm2 = new TextMessage("I'm a test message", guiMain, true);
			pTM2.add(Box.createHorizontalGlue());
			pTM2.add(tm2);
			panelTop.add(Box.createRigidArea(new Dimension(0, 5)));
			panelTop.add(pTM2);

		}
		JScrollPane jsp = new JScrollPane(panelTop, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setBackground(Color.WHITE);
		jsp.getVerticalScrollBar().setBackground(Color.WHITE);
		jsp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 50));
		jsp.getVerticalScrollBar().setMaximumSize(new Dimension(8, 50));
		add(jsp, BorderLayout.PAGE_START);

		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.PAGE_AXIS));
		panelCenter.setBackground(Color.DARK_GRAY);
		{
			JLabel lbl = new JLabel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Arial", Font.BOLD, 17));
					g2.setStroke(new BasicStroke(2.3F));

					int xStart = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth("Message settings") / 2;
					int yFontStart = getHeight() / 2 + g2.getFontMetrics(g2.getFont()).getHeight() / 2;
					int xEnd = getWidth() / 2 + (getWidth() / 2 - xStart);
					int stringHeight = g2.getFontMetrics(g2.getFont()).getHeight();

					g2.setColor(Color.WHITE);
					g2.drawString("Message settings", xStart, yFontStart);

					g2.setColor(new Color(55, 102, 255));
					g2.setStroke(new BasicStroke(3));
					g2.drawLine(xStart - 20, yFontStart + 5, xStart - 20, yFontStart - stringHeight);
					g2.drawLine(xStart - 19, yFontStart + 5, xStart - 13, yFontStart + 9);
					g2.drawLine(xStart - 19, yFontStart - stringHeight, xStart - 13, yFontStart - stringHeight - 4);
					g2.drawLine(xEnd + 18, yFontStart + 5, xEnd + 18, yFontStart - stringHeight);
					g2.drawLine(xEnd + 17, yFontStart + 5, xEnd + 11, yFontStart + 9);
					g2.drawLine(xEnd + 17, yFontStart - stringHeight, xEnd + 11, yFontStart - stringHeight - 4);

					g2.dispose();
				}
			};
			lbl.setPreferredSize(new Dimension(500, 70));
			lbl.setMaximumSize(lbl.getPreferredSize());
			lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
			lbl.setOpaque(false);
			lbl.setBackground(new Color(0, 0, 0, 0));
			panelCenter.add(lbl);
			panelCenter.add(Box.createRigidArea(new Dimension(0, 12)));

			JLabel lblOwnMessageB = new JLabel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Arial", Font.BOLD, 14));
					g2.setColor(Color.WHITE);
					g2.drawString("Hintergrundfarbe - Eigene Nachrichten:", 2,
							g2.getFontMetrics(g2.getFont()).getHeight() + 4);
					g2.setColor(new Color(55, 102, 255));
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
							g2.getFontMetrics(g2.getFont()).stringWidth("Hintergrundfarbe - Eigene Nachrichten:"),
							g2.getFontMetrics(g2.getFont()).getHeight() + 7);

					g2.dispose();
				}
			};
			lblOwnMessageB.setPreferredSize(new Dimension(680, 40));
			lblOwnMessageB.setMaximumSize(lblOwnMessageB.getPreferredSize());
			lblOwnMessageB.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblOwnMessageB.setOpaque(false);
			lblOwnMessageB.setBackground(new Color(0, 0, 0, 0));
			panelCenter.add(lblOwnMessageB);
			panelCenter.add(Box.createRigidArea(new Dimension(0, 5)));

			JPanel pBC = new JPanel();
			pBC.setLayout(new BoxLayout(pBC, BoxLayout.LINE_AXIS));
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
				pBC.add(Box.createHorizontalGlue());

				RoundButton rb = new RoundButton(Color.GRAY, "Choose a background color", pBC, guiMain);
				rb.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						Color c = JColorChooser.showDialog(panelCenter, "Choose a message's background color",
								new Color(191, 225, 14));
						if (c != null)
							guiMain.getSettings().setColorOwnMessages(c.getRGB());
						p.setBackground(new Color(guiMain.getSettings().getColorOwnMessages()));
						p.repaint();
						tm2.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pBC.add(rb);

				p.setBackground(new Color(guiMain.getSettings().getColorOwnMessages()));
				p.setMinimumSize(new Dimension(35, 35));
				p.setPreferredSize(new Dimension(35, 35));
				p.setMaximumSize(new Dimension(35, 35));
				p.setCursor(new Cursor(Cursor.HAND_CURSOR));
				p.setToolTipText("Reset to default");
				p.setFocusable(true);
				p.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {

						guiMain.getSettings().setColorOwnMessages(new Color(191, 225, 14).getRGB());
						p.setBackground(new Color(guiMain.getSettings().getColorOwnMessages()));
						p.repaint();
						tm2.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pBC.add(Box.createHorizontalGlue());
				pBC.add(p);
				pBC.add(Box.createRigidArea(new Dimension(1, 50)));
			}
			panelCenter.add(pBC);

			JLabel lblOwnMessageFontB = new JLabel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Arial", Font.BOLD, 14));
					g2.setColor(Color.WHITE);
					g2.drawString("Schriftfarbe - Eigene Nachrichten:", 2,
							g2.getFontMetrics(g2.getFont()).getHeight() + 4);
					g2.setColor(new Color(55, 102, 255));
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
							g2.getFontMetrics(g2.getFont()).stringWidth("Schriftfarbe - Eigene Nachrichten:"),
							g2.getFontMetrics(g2.getFont()).getHeight() + 7);

					g2.dispose();
				}
			};
			lblOwnMessageFontB.setPreferredSize(new Dimension(680, 40));
			lblOwnMessageFontB.setMaximumSize(lblOwnMessageFontB.getPreferredSize());
			lblOwnMessageFontB.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblOwnMessageFontB.setOpaque(false);
			lblOwnMessageFontB.setBackground(new Color(0, 0, 0, 0));
			panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
			panelCenter.add(lblOwnMessageFontB);

			JPanel pOMFC = new JPanel();
			pOMFC.setLayout(new BoxLayout(pOMFC, BoxLayout.LINE_AXIS));
			pOMFC.setBackground(Color.DARK_GRAY);
			pOMFC.setAlignmentX(Component.CENTER_ALIGNMENT);
			pOMFC.setBorder(new EmptyBorder(5, 10, 5, 5));
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
				pOMFC.add(Box.createHorizontalGlue());

				RoundButton rb = new RoundButton(Color.GRAY, "Choose a message's font color", pOMFC, guiMain);
				rb.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						Color c = JColorChooser.showDialog(panelCenter, "Choose a message font color", Color.BLACK);
						if (c != null)
							guiMain.getSettings().setColorOwnMessageFont(c.getRGB());
						p.setBackground(new Color(guiMain.getSettings().getColorOwnMessageFont()));
						p.repaint();
						tm2.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pOMFC.add(rb);

				p.setBackground(new Color(guiMain.getSettings().getColorOwnMessageFont()));
				p.setMinimumSize(new Dimension(35, 35));
				p.setPreferredSize(new Dimension(35, 35));
				p.setMaximumSize(new Dimension(35, 35));
				p.setCursor(new Cursor(Cursor.HAND_CURSOR));
				p.setToolTipText("Reset to default");
				p.setFocusable(true);
				p.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {

						guiMain.getSettings().setColorOwnMessageFont(Color.BLACK.getRGB());
						p.setBackground(new Color(guiMain.getSettings().getColorOwnMessageFont()));
						p.repaint();
						tm2.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pOMFC.add(Box.createHorizontalGlue());
				pOMFC.add(p);
				pOMFC.add(Box.createRigidArea(new Dimension(1, 50)));

			}
			panelCenter.add(Box.createRigidArea(new Dimension(0, 3)));
			panelCenter.add(pOMFC);

			JLabel lblOwnMessageBorder = new JLabel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Arial", Font.BOLD, 14));
					g2.setColor(Color.WHITE);
					g2.drawString("Randfarbe - Eigene Nachrichten:", 2,
							g2.getFontMetrics(g2.getFont()).getHeight() + 4);
					g2.setColor(new Color(55, 102, 255));
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
							g2.getFontMetrics(g2.getFont()).stringWidth("Randfarbe - Eigene Nachrichten:"),
							g2.getFontMetrics(g2.getFont()).getHeight() + 7);

					g2.dispose();
				}
			};
			lblOwnMessageBorder.setPreferredSize(new Dimension(680, 40));
			lblOwnMessageBorder.setMaximumSize(lblOwnMessageBorder.getPreferredSize());
			lblOwnMessageBorder.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblOwnMessageBorder.setOpaque(false);
			lblOwnMessageBorder.setBackground(new Color(0, 0, 0, 0));
			panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
			panelCenter.add(lblOwnMessageBorder);

			JPanel pOMB = new JPanel();
			pOMB.setLayout(new BoxLayout(pOMB, BoxLayout.LINE_AXIS));
			pOMB.setBackground(Color.DARK_GRAY);
			pOMB.setAlignmentX(Component.CENTER_ALIGNMENT);
			pOMB.setBorder(new EmptyBorder(5, 10, 5, 5));
			{
				JPanel panel = new JPanel() {
					@Override
					public void paintComponent(Graphics g) {

						Graphics2D g2 = (Graphics2D) g;
						g2.setColor(getBackground());
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
					}
				};
				pOMB.add(Box.createHorizontalGlue());

				RoundButton roundb = new RoundButton(Color.GRAY, "Choose a message's border color", pOMB, guiMain);
				roundb.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						Color c = JColorChooser.showDialog(panelCenter, "Choose a message's border color", Color.BLACK);
						if (c != null)
							guiMain.getSettings().setColorOwnMessageBorder(c.getRGB());
						panel.setBackground(new Color(guiMain.getSettings().getColorOwnMessageBorder()));
						panel.repaint();
						tm2.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pOMB.add(roundb);

				panel.setBackground(new Color(guiMain.getSettings().getColorOwnMessageBorder()));
				panel.setMinimumSize(new Dimension(35, 35));
				panel.setPreferredSize(new Dimension(35, 35));
				panel.setMaximumSize(new Dimension(35, 35));
				panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				panel.setToolTipText("Reset to default");
				panel.setFocusable(true);
				panel.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {

						guiMain.getSettings().setColorOwnMessageBorder(Color.GRAY.getRGB());
						panel.setBackground(new Color(guiMain.getSettings().getColorOwnMessageBorder()));
						panel.repaint();
						tm2.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pOMB.add(Box.createHorizontalGlue());
				pOMB.add(panel);
				pOMB.add(Box.createRigidArea(new Dimension(1, 50)));
			}
			panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
			panelCenter.add(pOMB);

			JLabel lblOwnMessageFont = new JLabel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Arial", Font.BOLD, 14));
					g2.setColor(Color.WHITE);
					g2.drawString("Eigene Nachrichten - Schriftart und -größe:", 2,
							g2.getFontMetrics(g2.getFont()).getHeight() + 4);
					g2.setColor(new Color(55, 102, 255));
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
							g2.getFontMetrics(g2.getFont()).stringWidth("Eigene Nachrichten - Schriftart und -größe:"),
							g2.getFontMetrics(g2.getFont()).getHeight() + 7);

					g2.dispose();
				}
			};
			lblOwnMessageFont.setPreferredSize(new Dimension(680, 40));
			lblOwnMessageFont.setMaximumSize(lblOwnMessageFont.getPreferredSize());
			lblOwnMessageFont.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblOwnMessageFont.setOpaque(false);
			lblOwnMessageFont.setBackground(new Color(0, 0, 0, 0));
			panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
			panelCenter.add(lblOwnMessageFont);

			JPanel pOMF = new JPanel();
			pOMF.setLayout(new BoxLayout(pOMF, BoxLayout.LINE_AXIS));
			pOMF.setBackground(Color.DARK_GRAY);
			pOMF.setAlignmentX(Component.CENTER_ALIGNMENT);
			pOMF.setBorder(new EmptyBorder(5, 10, 5, 5));
			{
				pOMF.add(Box.createHorizontalGlue());

				JComboBox<String> jccOwnFontNames = new JComboBox<>(
						GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
				jccOwnFontNames.setOpaque(false);
				jccOwnFontNames.setBackground(new Color(0, 0, 0, 0));
				jccOwnFontNames.setMaximumRowCount(15);
				jccOwnFontNames.setEditable(false);
				jccOwnFontNames.setSelectedItem(guiMain.getSettings().getOwnFontName());
				jccOwnFontNames.setPreferredSize(new Dimension(250, 30));
				jccOwnFontNames.setMaximumSize(jccOwnFontNames.getPreferredSize());
				jccOwnFontNames.setAlignmentX(Component.CENTER_ALIGNMENT);
				jccOwnFontNames.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {

						if (e.getStateChange() == ItemEvent.SELECTED) {
							guiMain.getSettings().setOwnFontName((String) jccOwnFontNames.getSelectedItem());
							tm2.updateMessage();
							tm2.repaint();
							panelTop.revalidate();
							panelTop.repaint();

							guiMain.updateSettings();
						}

					}
				});
				pOMF.add(jccOwnFontNames);

				String[] arr = {"Plain", "<html><b>Bold</b></html>", "<html><i>Italic</i></html>"};
				JComboBox<String> jccOwnFontStyle = new JComboBox<>(arr);
				jccOwnFontStyle.setOpaque(false);
				jccOwnFontStyle.setBackground(new Color(0, 0, 0, 0));
				jccOwnFontStyle.setEditable(false);
				jccOwnFontStyle.setMaximumRowCount(4);
				jccOwnFontStyle.setPreferredSize(new Dimension(100, 30));
				jccOwnFontStyle.setMaximumSize(jccOwnFontStyle.getPreferredSize());
				jccOwnFontStyle.setAlignmentX(Component.CENTER_ALIGNMENT);
				jccOwnFontStyle.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {

						if (e.getStateChange() == ItemEvent.SELECTED) {
							if (jccOwnFontStyle.getSelectedIndex() == 0)
								guiMain.getSettings().setOwnFontStyle(Font.PLAIN);
							else if (jccOwnFontStyle.getSelectedIndex() == 1)
								guiMain.getSettings().setOwnFontStyle(Font.BOLD);
							else if (jccOwnFontStyle.getSelectedIndex() == 2)
								guiMain.getSettings().setOwnFontStyle(Font.ITALIC);
							tm2.updateMessage();
							tm2.repaint();
							pTM2.revalidate();
							pTM2.repaint();
							revalidate();
							repaint();

							guiMain.updateSettings();
						}

					}
				});
				pOMF.add(Box.createRigidArea(new Dimension(5, 0)));
				pOMF.add(jccOwnFontStyle);

				Integer[] i = {8, 9, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28};
				JComboBox<Integer> jccOwnFontSize = new JComboBox<>(i);
				jccOwnFontSize.setOpaque(false);
				jccOwnFontSize.setBackground(new Color(0, 0, 0, 0));
				jccOwnFontSize.setEditable(false);
				jccOwnFontSize.setMaximumRowCount(4);
				jccOwnFontSize.setSelectedItem(guiMain.getSettings().getOwnFontSize());
				jccOwnFontSize.setPreferredSize(new Dimension(50, 30));
				jccOwnFontSize.setMaximumSize(jccOwnFontNames.getPreferredSize());
				jccOwnFontSize.setAlignmentX(Component.CENTER_ALIGNMENT);
				jccOwnFontSize.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {

						if (e.getStateChange() == ItemEvent.SELECTED) {
							guiMain.getSettings().setOwnFontSize((int) jccOwnFontSize.getSelectedItem());
							tm2.updateMessage();
							tm2.repaint();
							pTM2.revalidate();
							pTM2.repaint();
							revalidate();
							repaint();

							guiMain.updateSettings();
						}

					}
				});
				pOMF.add(Box.createRigidArea(new Dimension(5, 0)));
				pOMF.add(jccOwnFontSize);
				pOMF.add(Box.createHorizontalGlue());
			}
			panelCenter.add(Box.createRigidArea(new Dimension(0, 3)));
			panelCenter.add(pOMF);

			// -------------------------------------------------------------
			// ----------------------CONTACT_MESSAGES-----------------------
			// -------------------------------------------------------------

			JPanel panelSeperator = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {

					g.setColor(new Color(0, 0, 0, 0));
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(Color.BLUE);
					g2.fillOval(10, 25, getWidth() - 20, 2);
					g2.fillOval(10, 41, getWidth() - 20, 2);
					g2.setColor(Color.WHITE);
					g2.fillOval(60, 33, getWidth() - 120, 2);
				}
			};
			panelSeperator.setPreferredSize(new Dimension(500, 70));
			panelSeperator.setMaximumSize(lbl.getPreferredSize());
			panelSeperator.setAlignmentX(Component.CENTER_ALIGNMENT);
			panelSeperator.setOpaque(false);
			panelSeperator.setBackground(new Color(0, 0, 0, 0));
			panelCenter.add(Box.createRigidArea(new Dimension(0, 12)));
			panelCenter.add(panelSeperator);

			JLabel lblContactMessageB = new JLabel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Arial", Font.BOLD, 14));
					g2.setColor(Color.WHITE);
					g2.drawString("Hintergrundfarbe - Eingehende Nachrichten:", 2,
							g2.getFontMetrics(g2.getFont()).getHeight() + 4);
					g2.setColor(new Color(55, 102, 255));
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
							g2.getFontMetrics(g2.getFont()).stringWidth("Hintergrundfarbe - Eingehende Nachrichten:"),
							g2.getFontMetrics(g2.getFont()).getHeight() + 7);

					g2.dispose();
				}
			};
			lblContactMessageB.setPreferredSize(new Dimension(680, 40));
			lblContactMessageB.setMaximumSize(lblContactMessageB.getPreferredSize());
			lblContactMessageB.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblContactMessageB.setOpaque(false);
			lblContactMessageB.setBackground(new Color(0, 0, 0, 0));
			panelCenter.add(lblContactMessageB);
			panelCenter.add(Box.createRigidArea(new Dimension(0, 5)));

			JPanel pCBC = new JPanel();
			pCBC.setLayout(new BoxLayout(pCBC, BoxLayout.LINE_AXIS));
			pCBC.setBackground(Color.DARK_GRAY);
			pCBC.setAlignmentX(Component.CENTER_ALIGNMENT);
			pCBC.setBorder(new EmptyBorder(5, 10, 5, 5));
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
				pCBC.add(Box.createHorizontalGlue());

				RoundButton rb = new RoundButton(Color.GRAY, "Choose a background color", pCBC, guiMain);
				rb.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						Color c = JColorChooser.showDialog(panelCenter, "Choose a message's background color",
								new Color(191, 225, 14));
						if (c != null)
							guiMain.getSettings().setColorContactMessages(c.getRGB());
						p.setBackground(new Color(guiMain.getSettings().getColorContactMessages()));
						p.repaint();
						tm.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pCBC.add(rb);

				p.setBackground(new Color(guiMain.getSettings().getColorContactMessages()));
				p.setMinimumSize(new Dimension(35, 35));
				p.setPreferredSize(new Dimension(35, 35));
				p.setMaximumSize(new Dimension(35, 35));
				p.setCursor(new Cursor(Cursor.HAND_CURSOR));
				p.setToolTipText("Reset to default");
				p.setFocusable(true);
				p.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {

						guiMain.getSettings().setColorContactMessages(new Color(255, 250, 240).getRGB());
						p.setBackground(new Color(guiMain.getSettings().getColorContactMessages()));
						p.repaint();
						tm.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pCBC.add(Box.createHorizontalGlue());
				pCBC.add(p);
				pCBC.add(Box.createRigidArea(new Dimension(1, 50)));
			}
			panelCenter.add(pCBC);

			JLabel lblContactMessageFontB = new JLabel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Arial", Font.BOLD, 14));
					g2.setColor(Color.WHITE);
					g2.drawString("Schriftfarbe - Eingehende Nachrichten:", 2,
							g2.getFontMetrics(g2.getFont()).getHeight() + 4);
					g2.setColor(new Color(55, 102, 255));
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
							g2.getFontMetrics(g2.getFont()).stringWidth("Schriftfarbe - Eingehende Nachrichten:"),
							g2.getFontMetrics(g2.getFont()).getHeight() + 7);

					g2.dispose();
				}
			};
			lblContactMessageFontB.setPreferredSize(new Dimension(680, 40));
			lblContactMessageFontB.setMaximumSize(lblContactMessageFontB.getPreferredSize());
			lblContactMessageFontB.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblContactMessageFontB.setOpaque(false);
			lblContactMessageFontB.setBackground(new Color(0, 0, 0, 0));
			panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
			panelCenter.add(lblContactMessageFontB);

			JPanel pCMFC = new JPanel();
			pCMFC.setLayout(new BoxLayout(pCMFC, BoxLayout.LINE_AXIS));
			pCMFC.setBackground(Color.DARK_GRAY);
			pCMFC.setAlignmentX(Component.CENTER_ALIGNMENT);
			pCMFC.setBorder(new EmptyBorder(5, 10, 5, 5));
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
				pCMFC.add(Box.createHorizontalGlue());

				RoundButton rb = new RoundButton(Color.GRAY, "Choose a message's font color", pCMFC, guiMain);
				rb.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						Color c = JColorChooser.showDialog(panelCenter, "Choose a message font color", Color.BLACK);
						if (c != null)
							guiMain.getSettings().setColorContactMessageFont(c.getRGB());
						p.setBackground(new Color(guiMain.getSettings().getColorContactMessageFont()));
						p.repaint();
						tm.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pCMFC.add(rb);

				p.setBackground(new Color(guiMain.getSettings().getColorContactMessageFont()));
				p.setMinimumSize(new Dimension(35, 35));
				p.setPreferredSize(new Dimension(35, 35));
				p.setMaximumSize(new Dimension(35, 35));
				p.setCursor(new Cursor(Cursor.HAND_CURSOR));
				p.setToolTipText("Reset to default");
				p.setFocusable(true);
				p.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {

						guiMain.getSettings().setColorOwnMessageFont(Color.BLACK.getRGB());
						p.setBackground(new Color(guiMain.getSettings().getColorContactMessageFont()));
						p.repaint();
						tm.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pCMFC.add(Box.createHorizontalGlue());
				pCMFC.add(p);
				pCMFC.add(Box.createRigidArea(new Dimension(1, 50)));

			}
			panelCenter.add(Box.createRigidArea(new Dimension(0, 3)));
			panelCenter.add(pCMFC);

			JLabel lblContactMessageBorder = new JLabel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Arial", Font.BOLD, 14));
					g2.setColor(Color.WHITE);
					g2.drawString("Randfarbe - Eingehende Nachrichten:", 2,
							g2.getFontMetrics(g2.getFont()).getHeight() + 4);
					g2.setColor(new Color(55, 102, 255));
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
							g2.getFontMetrics(g2.getFont()).stringWidth("Randfarbe - Eingehende Nachrichten:"),
							g2.getFontMetrics(g2.getFont()).getHeight() + 7);

					g2.dispose();
				}
			};
			lblContactMessageBorder.setPreferredSize(new Dimension(680, 40));
			lblContactMessageBorder.setMaximumSize(lblContactMessageBorder.getPreferredSize());
			lblContactMessageBorder.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblContactMessageBorder.setOpaque(false);
			lblContactMessageBorder.setBackground(new Color(0, 0, 0, 0));
			panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
			panelCenter.add(lblContactMessageBorder);

			JPanel pCMB = new JPanel();
			pCMB.setLayout(new BoxLayout(pCMB, BoxLayout.LINE_AXIS));
			pCMB.setBackground(Color.DARK_GRAY);
			pCMB.setAlignmentX(Component.CENTER_ALIGNMENT);
			pCMB.setBorder(new EmptyBorder(5, 10, 5, 5));
			{
				JPanel panel = new JPanel() {
					@Override
					public void paintComponent(Graphics g) {

						Graphics2D g2 = (Graphics2D) g;
						g2.setColor(getBackground());
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
					}
				};
				pCMB.add(Box.createHorizontalGlue());

				RoundButton roundb = new RoundButton(Color.GRAY, "Choose a message's border color", pCMB, guiMain);
				roundb.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						Color c = JColorChooser.showDialog(panelCenter, "Choose a message's border color", Color.GRAY);
						if (c != null)
							guiMain.getSettings().setColorContactMessageBorder(c.getRGB());
						panel.setBackground(new Color(guiMain.getSettings().getColorContactMessageBorder()));
						panel.repaint();
						tm.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pCMB.add(roundb);

				panel.setBackground(new Color(guiMain.getSettings().getColorContactMessageBorder()));
				panel.setMinimumSize(new Dimension(35, 35));
				panel.setPreferredSize(new Dimension(35, 35));
				panel.setMaximumSize(new Dimension(35, 35));
				panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				panel.setToolTipText("Reset to default");
				panel.setFocusable(true);
				panel.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {

						guiMain.getSettings().setColorContactMessageBorder(Color.GRAY.getRGB());
						panel.setBackground(new Color(guiMain.getSettings().getColorContactMessageBorder()));
						panel.repaint();
						tm2.repaint();
						repaint();

						guiMain.updateSettings();
					}
				});
				pCMB.add(Box.createHorizontalGlue());
				pCMB.add(panel);
				pCMB.add(Box.createRigidArea(new Dimension(1, 50)));
			}
			panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
			panelCenter.add(pCMB);

			JLabel lblContactMessageFont = new JLabel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Arial", Font.BOLD, 14));
					g2.setColor(Color.WHITE);
					g2.drawString("Eingehende Nachrichten - Schriftart und -größe:", 2,
							g2.getFontMetrics(g2.getFont()).getHeight() + 4);
					g2.setColor(new Color(55, 102, 255));
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
							g2.getFontMetrics(g2.getFont())
									.stringWidth("Eingehende Nachrichten - Schriftart und -größe:"),
							g2.getFontMetrics(g2.getFont()).getHeight() + 7);

					g2.dispose();
				}
			};
			lblContactMessageFont.setPreferredSize(new Dimension(680, 40));
			lblContactMessageFont.setMaximumSize(lblContactMessageFont.getPreferredSize());
			lblContactMessageFont.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblContactMessageFont.setOpaque(false);
			lblContactMessageFont.setBackground(new Color(0, 0, 0, 0));
			panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
			panelCenter.add(lblContactMessageFont);

			JPanel pCMF = new JPanel();
			pCMF.setLayout(new BoxLayout(pCMF, BoxLayout.LINE_AXIS));
			pCMF.setBackground(Color.DARK_GRAY);
			pCMF.setAlignmentX(Component.CENTER_ALIGNMENT);
			pCMF.setBorder(new EmptyBorder(5, 10, 5, 5));
			{
				pCMF.add(Box.createHorizontalGlue());

				JComboBox<String> jccContactFontNames = new JComboBox<>(
						GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
				jccContactFontNames.setOpaque(false);
				jccContactFontNames.setBackground(new Color(0, 0, 0, 0));
				jccContactFontNames.setMaximumRowCount(15);
				jccContactFontNames.setEditable(false);
				jccContactFontNames.setSelectedItem(guiMain.getSettings().getContactFontName());
				jccContactFontNames.setPreferredSize(new Dimension(250, 30));
				jccContactFontNames.setMaximumSize(jccContactFontNames.getPreferredSize());
				jccContactFontNames.setAlignmentX(Component.CENTER_ALIGNMENT);
				jccContactFontNames.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {

						if (e.getStateChange() == ItemEvent.SELECTED) {
							guiMain.getSettings().setContactFontName((String) jccContactFontNames.getSelectedItem());
							tm.updateMessage();
							tm.repaint();
							panelTop.revalidate();
							panelTop.repaint();

							guiMain.updateSettings();
						}

					}
				});
				pCMF.add(jccContactFontNames);

				String[] arr = {"Plain", "<html><b>Bold</b></html>", "<html><i>Italic</i></html>"};
				JComboBox<String> jccContactFontStyle = new JComboBox<>(arr);
				jccContactFontStyle.setOpaque(false);
				jccContactFontStyle.setBackground(new Color(0, 0, 0, 0));
				jccContactFontStyle.setEditable(false);
				jccContactFontStyle.setMaximumRowCount(4);
				jccContactFontStyle.setPreferredSize(new Dimension(100, 30));
				jccContactFontStyle.setMaximumSize(jccContactFontStyle.getPreferredSize());
				jccContactFontStyle.setAlignmentX(Component.CENTER_ALIGNMENT);
				jccContactFontStyle.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {

						if (e.getStateChange() == ItemEvent.SELECTED) {
							if (jccContactFontStyle.getSelectedIndex() == 0)
								guiMain.getSettings().setContactFontStyle(Font.PLAIN);
							else if (jccContactFontStyle.getSelectedIndex() == 1)
								guiMain.getSettings().setContactFontStyle(Font.BOLD);
							else if (jccContactFontStyle.getSelectedIndex() == 2)
								guiMain.getSettings().setContactFontStyle(Font.ITALIC);
							tm.updateMessage();
							tm.repaint();
							pTM.revalidate();
							pTM.repaint();
							revalidate();
							repaint();

							guiMain.updateSettings();
						}

					}
				});
				pCMF.add(Box.createRigidArea(new Dimension(5, 0)));
				pCMF.add(jccContactFontStyle);

				Integer[] i = {8, 9, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28};
				JComboBox<Integer> jccContactFontSize = new JComboBox<>(i);
				jccContactFontSize.setOpaque(false);
				jccContactFontSize.setBackground(new Color(0, 0, 0, 0));
				jccContactFontSize.setEditable(false);
				jccContactFontSize.setMaximumRowCount(4);
				jccContactFontSize.setSelectedItem(guiMain.getSettings().getOwnFontSize());
				jccContactFontSize.setPreferredSize(new Dimension(50, 30));
				jccContactFontSize.setMaximumSize(jccContactFontSize.getPreferredSize());
				jccContactFontSize.setAlignmentX(Component.CENTER_ALIGNMENT);
				jccContactFontSize.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {

						if (e.getStateChange() == ItemEvent.SELECTED) {
							guiMain.getSettings().setContactFontSize((int) jccContactFontSize.getSelectedItem());
							tm.updateMessage();
							tm.repaint();
							pTM.revalidate();
							pTM.repaint();
							revalidate();
							repaint();

							guiMain.updateSettings();
						}

					}
				});
				pCMF.add(Box.createRigidArea(new Dimension(5, 0)));
				pCMF.add(jccContactFontSize);
				pCMF.add(Box.createHorizontalGlue());
			}
			panelCenter.add(Box.createRigidArea(new Dimension(0, 3)));
			panelCenter.add(pCMF);
		}

		JScrollPane jScrollPane = new JScrollPane(panelCenter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		add(jScrollPane, BorderLayout.CENTER);

	}

}
