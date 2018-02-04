package jaims_development_studio.jaims.client.gui.settings;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class PanelSelectSettings extends JPanel {

	private GUIMain guiMain;

	public PanelSelectSettings(GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		setBackground(Color.DARK_GRAY);

	}

}
