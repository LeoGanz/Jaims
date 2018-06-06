package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.Color;
import java.util.UUID;

import javax.swing.JPanel;

/**
 * This class serves as a parent to all <code>JPanels</code> that have to be
 * added to the centre of the frame's content pane. It was created to simplify
 * the removing of the currently shown <code>ParentPanel</code> than keeping
 * track of whatever <code>JPanel</code> was last added. Because it has no
 * further uses it only holds a variable of the type <code>UUID</code> which is
 * the same as a UUID from one User in order to associate the
 * <code>ParentPanel</code> to a certain user. Setter and getter methods were
 * created to set the <code>ParentPanel's</code> UUID and retrieve it again.
 * 
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class ParentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private UUID				panelUUID;

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
