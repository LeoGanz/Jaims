package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.Color;
import java.util.UUID;

import javax.swing.JPanel;

public class ParentPanel extends JPanel {

	private UUID panelUUID;

	public ParentPanel() {

		setBackground(Color.DARK_GRAY);
	}

	public void setPanelUUID(UUID uuid) {

		panelUUID = uuid;
	}

	public UUID getPanelUUID() {

		return panelUUID;
	}

}
