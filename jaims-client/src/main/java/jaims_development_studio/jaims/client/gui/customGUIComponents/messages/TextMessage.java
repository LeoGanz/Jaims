package jaims_development_studio.jaims.client.gui.customGUIComponents.messages;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.RoundBorder;

public class TextMessage extends JPanel {

	private GUIMain				guiMain;
	private JTextArea			jta;
	private boolean				own, multipleLines;
	private int					height, width;
	private ArrayList<String>	lineStrings	= new ArrayList<>();
	private String				longestLine;

	public TextMessage(String message, GUIMain guiMain, boolean own) {

		this.guiMain = guiMain;
		this.own = own;
		initGUI(message);
	}

	private void initGUI(String message) {

		setLayout(new BorderLayout(0, 5));

		jta = new JTextArea();
		jta.setOpaque(false);

		if (own) {
			jta.setFont(new Font(guiMain.getSettings().getOwnFontName(), guiMain.getSettings().getOwnFontStyle(),
					guiMain.getSettings().getOwnFontSize()));
			jta.setForeground(guiMain.getSettings().getColorOwnMessageFont());
			setBorder(new RoundBorder(width, height, guiMain.getSettings().getColorOwnMessageBorder()));
		} else {
			jta.setFont(new Font(guiMain.getSettings().getContactFontName(),
					guiMain.getSettings().getContactFontStyle(), guiMain.getSettings().getContactFontSize()));
			jta.setForeground(guiMain.getSettings().getColorContactMessageFont());
			setBorder(new RoundBorder(width, height, guiMain.getSettings().getColorContactMessageBorder()));
		}

		height = getStringHeight(message, jta) + 10;
		width = getStringWidth(message, jta) + 15;

		jta.setPreferredSize(new Dimension(width, height));
		jta.setMaximumSize(getPreferredSize());
		jta.setEditable(false);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		// setBorder(new LineBorder(Color.BLACK));
		if (multipleLines) {
			addComponentListener(new ComponentAdapter() {

				@Override
				public void componentShown(ComponentEvent e) {

					if (own)
						jta.setFont(new Font(guiMain.getSettings().getOwnFontName(),
								guiMain.getSettings().getOwnFontStyle(), guiMain.getSettings().getOwnFontSize()));
					else
						jta.setFont(new Font(guiMain.getSettings().getContactFontName(),
								guiMain.getSettings().getContactFontStyle(),
								guiMain.getSettings().getContactFontSize()));

					int height = getStringHeight(message, jta) + 10;
					int width = getStringWidth(message, jta) + 10;

					setPreferredSize(new Dimension(width, height));
					setMaximumSize(getPreferredSize());
					revalidate();
					repaint();
				}

				@Override
				public void componentResized(ComponentEvent e) {

					int height = getStringHeight(message, jta) + 10;
					int width = getStringWidth(message, jta) + 10;

					if (own) {
						jta.setFont(new Font(guiMain.getSettings().getOwnFontName(),
								guiMain.getSettings().getOwnFontStyle(), guiMain.getSettings().getOwnFontSize()));
						jta.setForeground(guiMain.getSettings().getColorOwnMessageFont());
						setBorder(new RoundBorder(width, height, guiMain.getSettings().getColorOwnMessageBorder()));
					} else {
						jta.setFont(new Font(guiMain.getSettings().getContactFontName(),
								guiMain.getSettings().getContactFontStyle(),
								guiMain.getSettings().getContactFontSize()));
						jta.setForeground(guiMain.getSettings().getColorContactMessageFont());
						setBorder(new RoundBorder(width, height, guiMain.getSettings().getColorContactMessageBorder()));
					}

					setPreferredSize(new Dimension(width, height));
					setMaximumSize(getPreferredSize());
					jta.repaint();
					revalidate();
					repaint();
				}
			});
		}

		setPreferredSize(new Dimension(width, (int) lineStrings.size() * 19 + 15));
		setMaximumSize(getPreferredSize());

		add(jta);
	}

	private int countLines(String s, FontMetrics fm, JTextArea jta) {

		lineStrings.clear();
		longestLine = "";

		int width = (int) guiMain.getJaimsFrame().getSize().getWidth() - guiMain.getPanelWidth() - 16;
		double ratio = -0.000000000031829 * Math.pow(width, 3) + 0.000000404015684 * Math.pow(width, 2)
				- 0.000992064197966 * width + 1.210427932031404;
		int preferredWidth = (int) (width * ratio);

		String wholeString = "";
		String[] split = s.split(" ");

		String text = "";
		int countLines = 1;

		for (String st : split) {
			if ((fm.stringWidth(wholeString) + fm.stringWidth(st + " ")) < preferredWidth) {
				wholeString += st;
				wholeString += " ";
			} else {
				if (fm.stringWidth(wholeString) > fm.stringWidth(longestLine))
					longestLine = wholeString;

				lineStrings.add(wholeString);
				text += wholeString;
				text += "\n";
				wholeString = "";
				countLines++;
				wholeString += st;
				wholeString += " ";
			}
		}

		if (text.equals("")) {
			lineStrings.add(wholeString);
			text = wholeString;
			longestLine = wholeString;
			jta.setText(text);
			return 1;
		} else {
			text += wholeString;
			jta.setText(text);
			multipleLines = true;
			return countLines;
		}

	}

	public int getStringHeight(String s, JTextArea lbl) {

		FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
		int lines = countLines(s, fm, lbl);
		int height = fm.getHeight();
		if (lines == 1) {
			return ((lines * height) + 5);
		} else {
			return (lines * (height + 1));
		}

	}

	public int getStringWidth(String s, JTextArea lbl) {

		int width = (int) guiMain.getJaimsFrame().getSize().getWidth() - guiMain.getPanelWidth() - 16;
		double preferredWidth = -0.000000000031829 * Math.pow(width, 3) + 0.000000404015684 * Math.pow(width, 2)
				- 0.000992064197966 * width + 1.210427932031404;

		FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
		int i = fm.stringWidth(s) + 5;
		if (i > (width * preferredWidth)) {
			return fm.stringWidth(longestLine);
		} else {
			return i;
		}
	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (own) {
			g2d.setColor(guiMain.getSettings().getColorOwnMessages());
			g2d.fillRoundRect(0, 0, (int) getPreferredSize().getWidth() - 1, (int) getPreferredSize().getHeight() - 1,
					20, 20);
			g2d.dispose();
		} else {
			g2d.setColor(guiMain.getSettings().getColorContactMessages());
			g2d.fillRoundRect(0, 0, (int) getPreferredSize().getWidth() - 1, (int) getPreferredSize().getHeight() - 1,
					20, 20);

			g2d.dispose();
		}
	}

}
