package jaims_development_studio.jaims.client.gui.messagePanels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class PanelTextField extends JPanel {
	private JTextArea	jta;
	private JScrollPane	jsp	= null;
	private JTextField	jtf;

	public PanelTextField(JTextArea jta) {

		this.jta = jta;
		initGUI();
	}

	public PanelTextField(JTextField jtf) {

		this.jtf = jtf;
		initGUITextField();
	}

	private void initGUI() {

		JPanel p = this;
		jta.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (jta.getSize().getHeight() > (jta.getFontMetrics(jta.getFont()).getHeight() * 5 + 10)) {
					if (jsp == null) {
						jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
								JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
						jsp.setPreferredSize(new Dimension(p.getWidth() - 50,
								jta.getFontMetrics(jta.getFont()).getHeight() * 5 + 10));
						jta.setMaximumSize(jta.getPreferredSize());

						removeAll();
						add(Box.createRigidArea(new Dimension(25, 0)));
						add(jsp);
						add(Box.createRigidArea(new Dimension(25, 0)));
						repaint();
					}
				} else {
					if (jsp != null) {
						removeAll();
						add(Box.createRigidArea(new Dimension(25, 0)));
						add(jta);
						add(Box.createRigidArea(new Dimension(25, 0)));
						repaint();

					}
				}
			}
		});
		setBorder(new EmptyBorder(6, 0, 6, 0));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setOpaque(false);
		add(Box.createRigidArea(new Dimension(25, 0)));
		add(jta);
		add(Box.createRigidArea(new Dimension(25, 0)));
	}

	private void initGUITextField() {

		// setBorder(new EmptyBorder(6, 0, 6, 0));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setMinimumSize(new Dimension(400, 36));
		setPreferredSize(new Dimension(400, 36));
		setMaximumSize(new Dimension(400, 36));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setOpaque(false);
		add(Box.createRigidArea(new Dimension(25, 0)));
		add(jtf);
		add(Box.createRigidArea(new Dimension(25, 0)));
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fillRoundRect(5, 1, getWidth() - 10, getHeight() - 2, 35, 35);
		g2.setColor(Color.DARK_GRAY);
		g2.setStroke(new BasicStroke(2.5F));
		g2.drawRoundRect(5, 1, getWidth() - 10, getHeight() - 2, 35, 35);

	}
}
