package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelSettings extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private PanelSettingIcons	panelIconsLineStart;
	private JPanel				panelLineStart;
	private JScrollPane			jspIcons;
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
		}
		add(panelLineStart, BorderLayout.LINE_START);
	}

	public void addCenterPanel(CenterPanelSettings cp) {

		if (cps != null)
			remove(cps);

		add(cp, BorderLayout.CENTER);
		revalidate();
		repaint();

		cps = cp;
	}

}
