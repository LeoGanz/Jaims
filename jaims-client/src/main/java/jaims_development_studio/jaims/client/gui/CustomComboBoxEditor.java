package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.sound.sampled.Mixer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class CustomComboBoxEditor extends BasicComboBoxEditor {
	private JLabel		label;
	private JPanel		panel	= new JPanel();
	private Mixer.Info	selectedItem;

	public CustomComboBoxEditor() {

		label = new JLabel() {
			@Override
			public void paintComponent(Graphics g) {

				// g.setColor(Color.white);
				// g.fillRect(2, 2, getWidth() - 2, getHeight() - 2);
				//
				// Graphics2D g2 = (Graphics2D) g;
				// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				// RenderingHints.VALUE_ANTIALIAS_ON);
				// g2.setColor(Color.BLACK);
				// g2.setFont(new Font("Arial", Font.BOLD, 16));
				// g2.drawString(getName(), 15, getHeight() / 2);
				//
				// g2.dispose();
			}
		};

		label.setOpaque(false);
		label.setFont(new Font("Arial", Font.BOLD, 14));
		label.setForeground(Color.BLACK);

		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
		panel.add(label);
		panel.setBackground(Color.GREEN);
	}

	public Component getEditorComponent() {

		return this.panel;
	}

	public Mixer.Info getItem() {

		return (Mixer.Info) this.selectedItem;
	}

	public void setItem(Mixer.Info item) {

		this.selectedItem = item;
		label.setText(item.getName());
	}
}
