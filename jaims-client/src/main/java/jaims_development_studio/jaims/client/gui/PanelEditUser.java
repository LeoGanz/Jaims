package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;

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
		panelSelectProfileImage.setBorderPainted(true);
		panelSelectProfileImage.setBorder(new LineBorder(Color.black, 2));
		panelSelectProfileImage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				cm.addPanelSelectProfileImage();

			}
		});
		panelSelectProfileImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(panelSelectProfileImage);

		add(Box.createRigidArea(new Dimension(0, 15)));

		panelChangeAccountDetails = new ButtonPainted(changeAccountDetails);
		panelChangeAccountDetails.setPreferredSize(new Dimension(250, 75));
		panelChangeAccountDetails.setMaximumSize(panelChangeAccountDetails.getPreferredSize());
		panelChangeAccountDetails.setBorderPainted(true);
		panelChangeAccountDetails.setBorder(new LineBorder(Color.black, 2));
		panelChangeAccountDetails.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// TODO Auto-generated method stub

			}
		});
		panelChangeAccountDetails.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(panelChangeAccountDetails);

		add(Box.createVerticalGlue());
	}

}
