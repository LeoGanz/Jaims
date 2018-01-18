package jaims_development_studio.jaims.client.gui;

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

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelSettings extends ContainerPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private PanelSettingIcons	panelIconsLineStart;
	private JPanel				panelLineStart;
	private JScrollPane			jspIcons, jspCenterPanel;
	private CenterPanelSettings	cps;

	public PanelSettings(ClientMain cm) {

		initGUI(cm);
	}

	private void initGUI(ClientMain cm) {

		setLayout(new BorderLayout());
		panelLineStart = new JPanel();
		panelLineStart.setLayout(new BorderLayout());
		panelLineStart.setBorder(new MatteBorder(0, 0, 0, 2, Color.GRAY));
		{

			panelIconsLineStart = new PanelSettingIcons(this);
			panelIconsLineStart.setMaximumSize(new Dimension(100, 60));
			panelIconsLineStart.setPreferredSize(panelIconsLineStart.getMaximumSize());

			jspIcons = new JScrollPane(panelIconsLineStart, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jspIcons.getVerticalScrollBar().setUI(new MyScrollBarUI());
			jspIcons.setComponentZOrder(jspIcons.getVerticalScrollBar(), 0);
			jspIcons.setComponentZOrder(jspIcons.getViewport(), 1);
			jspIcons.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {

					jspIcons.getViewport().repaint();
				}
			});
			panelLineStart.add(panelIconsLineStart, BorderLayout.CENTER);

			JPanel panelBack = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {

					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setStroke(new BasicStroke(2.3F));
					g2.setColor(Color.DARK_GRAY);
					g2.drawLine(5, 15, 13, 7);
					g2.drawLine(5, 15, 13, 23);
					g2.drawLine(5, 15, 25, 15);

					g2.dispose();
				}
			};
			panelBack.setPreferredSize(new Dimension(100, 30));
			panelBack.setMinimumSize(panelBack.getPreferredSize());
			panelBack.setOpaque(false);
			panelBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
			panelBack.setBorder(new MatteBorder(0, 0, 2, 0, Color.GRAY));
			panelBack.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {

					cm.removeSettingPanel();

				}
			});
			panelLineStart.add(panelBack, BorderLayout.PAGE_START);
		}
		add(panelLineStart, BorderLayout.LINE_START);

		jspCenterPanel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspCenterPanel.setComponentZOrder(jspCenterPanel.getVerticalScrollBar(), 0);
		jspCenterPanel.setComponentZOrder(jspCenterPanel.getViewport(), 1);
		jspCenterPanel.setBackground(getBackground());
		jspCenterPanel.setBorder(null);
		jspCenterPanel.getVerticalScrollBar().setBackground(getBackground());
		jspCenterPanel.getVerticalScrollBar().setUnitIncrement(10);
		jspCenterPanel.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {

				jspCenterPanel.getViewport().repaint();

			}
		});
		add(jspCenterPanel, BorderLayout.CENTER);
	}

	public void addCenterPanel(CenterPanelSettings cp) {

		if (cps != null)
			jspCenterPanel.remove(cps);

		jspCenterPanel.setViewportView(cp);
		jspCenterPanel.revalidate();
		revalidate();
		repaint();

		cps = cp;
	}

}
