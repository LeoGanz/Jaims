package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import jaims_development_studio.jaims.client.audio.SelectAudioDevices;

public class CustomComboBoxRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomComboBoxRenderer(SelectAudioDevices sad) {

	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		String name = (String) value.toString();
		setText(name);

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(Color.BLACK);
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		// Set the icon and text. If icon was null, say so.
		setFont(list.getFont());

		return this;
	}

}
