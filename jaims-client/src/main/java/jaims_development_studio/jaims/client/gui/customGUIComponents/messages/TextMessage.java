package jaims_development_studio.jaims.client.gui.customGUIComponents.messages;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class TextMessage extends JPanel {

	private GUIMain				guiMain;
	private JTextArea			jta;
	private boolean				own, multipleLines;
	private int					height, width;
	private ArrayList<String>	lineStrings	= new ArrayList<>();
	private String				longestLine, message;
	private Color				border;

	public TextMessage(String message, GUIMain guiMain, boolean own) {

		this.guiMain = guiMain;
		this.own = own;
		this.message = message;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		// setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(5, 8, 5, 5));

		super.setMinimumSize(new Dimension(50, 30));
		super.setMaximumSize(new Dimension(computeMaximumWidth(), Integer.MAX_VALUE));

		jta = new JTextArea(message);
		jta.setAlignmentY(Component.CENTER_ALIGNMENT);
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		jta.setEditable(false);
		jta.setVisible(true);
		jta.setBackground(new Color(0, 0, 0, 0));
		jta.setOpaque(false);
		jta.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		if (own) {
			jta.setFont(guiMain.getSettings().getOwnFont());
			jta.setForeground(new Color(guiMain.getSettings().getColorOwnMessageFont()));
		} else {
			jta.setFont(guiMain.getSettings().getContactFont());
			jta.setForeground(new Color(guiMain.getSettings().getColorContactMessageFont()));
		}

		int preferredWidth = jta.getFontMetrics(jta.getFont()).stringWidth(message);
		if (preferredWidth < computeMaximumWidth()) {
			super.setPreferredSize(new Dimension(preferredWidth + 40, 35));
			jta.setPreferredSize(new Dimension(preferredWidth + 10, 30));
		} else {
			super.setPreferredSize(new Dimension(computeMaximumWidth(), Integer.MAX_VALUE));
			jta.setPreferredSize(new Dimension(computeMaximumWidth(), Integer.MAX_VALUE));
		}
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(Box.createHorizontalGlue());
		add(jta);
		add(Box.createHorizontalGlue());
		add(Box.createRigidArea(new Dimension(5, 0)));

		guiMain.getJaimsFrame().addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				int preferredWidth = jta.getFontMetrics(jta.getFont()).stringWidth(message);
				if (preferredWidth < computeMaximumWidth()) {
					TextMessage.super.setPreferredSize(new Dimension(preferredWidth + 40, 35));
					jta.setPreferredSize(new Dimension(preferredWidth + 10, 30));
				} else {
					TextMessage.super.setPreferredSize(new Dimension(computeMaximumWidth(), Integer.MAX_VALUE));
					jta.setPreferredSize(new Dimension(computeMaximumWidth(), Integer.MAX_VALUE));
				}

			}

		});
	}

	private int computeMaximumWidth() {

		double ratio = -0.000000000031829 * Math.pow(guiMain.getJaimsFrame().getWidth(), 3)
				+ 0.000000404015684 * Math.pow(guiMain.getJaimsFrame().getWidth(), 2)
				- 0.000992064197966 * guiMain.getJaimsFrame().getWidth() + 1.210427932031404;

		return (int) ((guiMain.getJaimsFrame().getWidth() - 260) * ratio);
	}

	public void updateMessage() {

		if (own) {
			jta.setFont(guiMain.getSettings().getOwnFont());
			jta.setForeground(new Color(guiMain.getSettings().getColorOwnMessageFont()));
		} else {
			jta.setFont(guiMain.getSettings().getContactFont());
			jta.setForeground(new Color(guiMain.getSettings().getColorContactMessageFont()));
		}
		int preferredWidth = jta.getFontMetrics(jta.getFont()).stringWidth(message);
		if (preferredWidth < computeMaximumWidth()) {
			TextMessage.super.setPreferredSize(new Dimension(preferredWidth + 40, 35));
			jta.setPreferredSize(new Dimension(preferredWidth + 10, 30));
		} else {
			TextMessage.super.setPreferredSize(new Dimension(computeMaximumWidth(), Integer.MAX_VALUE));
			jta.setPreferredSize(new Dimension(computeMaximumWidth(), Integer.MAX_VALUE));
		}

		jta.revalidate();
		jta.repaint();
		repaint();
	}

	@Override
	public Dimension getMinimumSize() {

		return super.getMinimumSize();
	}

	@Override
	public Dimension getPreferredSize() {

		return super.getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {

		return super.getMaximumSize();
	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (own) {
			g2d.setColor(new Color(guiMain.getSettings().getColorOwnMessages()));
			g2d.fillRoundRect(0, 0, (int) getPreferredSize().getWidth() - 1, (int) getPreferredSize().getHeight() - 1,
					20, 20);
			g2d.setColor(new Color(guiMain.getSettings().getColorOwnMessageBorder()));
			g2d.setStroke(new BasicStroke(2.0F));
			g2d.drawRoundRect(0, 0, (int) super.getPreferredSize().getWidth() - 2,
					(int) super.getPreferredSize().getHeight() - 2, 20, 20);
			g2d.dispose();
		} else {
			g2d.setColor(new Color(guiMain.getSettings().getColorContactMessages()));
			g2d.fillRoundRect(0, 0, (int) getPreferredSize().getWidth() - 1, (int) getPreferredSize().getHeight() - 1,
					20, 20);
			g2d.setColor(new Color(guiMain.getSettings().getColorContactMessageBorder()));
			g2d.setStroke(new BasicStroke(2.0F));
			g2d.drawRoundRect(0, 0, (int) super.getPreferredSize().getWidth() - 2,
					(int) super.getPreferredSize().getHeight() - 2, 20, 20);
			g2d.dispose();
		}

	}

}
