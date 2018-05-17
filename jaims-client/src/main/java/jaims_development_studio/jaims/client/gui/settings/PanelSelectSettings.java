package jaims_development_studio.jaims.client.gui.settings;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class PanelSelectSettings extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private GUIMain				guiMain;
	private JPanel				panelLineStart;
	private PanelShowIcons		panelIconsLineStart;
	private JScrollPane			jspIcons;
	private SettingPanelsParent	spp;

	public PanelSelectSettings(GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		setBackground(Color.DARK_GRAY);
		panelLineStart = new JPanel();
		panelLineStart.setLayout(new BorderLayout());
		panelLineStart.setBackground(Color.DARK_GRAY);
		panelLineStart.setBorder(new MatteBorder(0, 0, 0, 2, Color.DARK_GRAY));
		{

			panelIconsLineStart = new PanelShowIcons(this, guiMain);

			jspIcons = new JScrollPane(panelIconsLineStart, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jspIcons.setComponentZOrder(jspIcons.getVerticalScrollBar(), 0);
			jspIcons.setComponentZOrder(jspIcons.getViewport(), 1);
			jspIcons.setBackground(Color.DARK_GRAY);
			jspIcons.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {

					jspIcons.getViewport().repaint();
				}
			});
			panelLineStart.add(panelIconsLineStart, BorderLayout.CENTER);

			JPanel panelBack = new JPanel() {
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
					g2.setStroke(new BasicStroke(2.3F));
					g2.setColor(Color.LIGHT_GRAY);
					g2.drawLine(5, 15, 13, 7);
					g2.drawLine(5, 15, 13, 23);
					g2.drawLine(5, 15, 25, 15);

					g2.dispose();
				}
			};
			panelBack.setPreferredSize(new Dimension(100, 30));
			panelBack.setMinimumSize(panelBack.getPreferredSize());
			panelBack.setMaximumSize(panelBack.getPreferredSize());
			panelBack.setOpaque(false);
			panelBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
			panelBack.setBorder(new MatteBorder(0, 0, 2, 0, Color.GRAY));
			panelBack.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {

					guiMain.updateSettings();
					guiMain.removeSettings();
				}
			});
			panelLineStart.add(panelBack, BorderLayout.PAGE_START);
		}
		add(panelLineStart, BorderLayout.LINE_START);

	}

	public void setCenterPanel(SettingPanelsParent sp) {

		if (spp != null)
			remove(spp);

		add(spp = sp);
		sp.repaint();
		revalidate();
		repaint();

	}

}
