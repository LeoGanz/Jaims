package jaims_development_studio.jaims.client.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelEditUser extends ContainerPanel {

	/**
	 *
	 */
	private static final long	serialVersionUID		= 1L;
	private ButtonPainted		panelSelectProfileImage, panelChangeAccountDetails;
	private ClientMain			cm;
	private String				selectProfilePicture	= "Profilbild ändern",
			changeAccountDetails = "Account bearbeiten";

	public PanelEditUser(ClientMain cm) {

		this.cm = cm;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		initGUI();
	}

	private void initGUI() {

		add(Box.createVerticalGlue());
		panelSelectProfileImage = new ButtonPainted(selectProfilePicture);
		panelSelectProfileImage.setPreferredSize(new Dimension(250, 75));
		panelSelectProfileImage.setMaximumSize(panelSelectProfileImage.getPreferredSize());
		panelSelectProfileImage.setBorderPainted(false);
		panelSelectProfileImage.addActionListener(e -> cm.addPanelSelectProfileImage());
		panelSelectProfileImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(panelSelectProfileImage);

		add(Box.createRigidArea(new Dimension(0, 15)));

		panelChangeAccountDetails = new ButtonPainted(changeAccountDetails);
		panelChangeAccountDetails.setPreferredSize(new Dimension(250, 75));
		panelChangeAccountDetails.setMaximumSize(panelChangeAccountDetails.getPreferredSize());
		panelChangeAccountDetails.setBorderPainted(false);
		panelChangeAccountDetails.addActionListener(e -> {

			// TODO Auto-generated method stub

		});
		panelChangeAccountDetails.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(panelChangeAccountDetails);

		add(Box.createVerticalGlue());
	}

}
