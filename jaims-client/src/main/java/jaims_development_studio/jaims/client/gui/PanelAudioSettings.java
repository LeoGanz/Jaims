package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;

import javax.sound.sampled.Mixer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.audio.SelectAudioDevices;

public class PanelAudioSettings extends CenterPanelSettings {

	private SelectAudioDevices	selectAudioDevices;
	private PanelSelectMixer	psm;
	private String[]			arr;
	private JPanel				panelInputDevices, panelMixerInfosInput;
	private JScrollPane			jspSelectInputDevices;
	private boolean				inputPanelIsShowing	= false;

	public PanelAudioSettings() {

		initGUI();
	}

	private void initGUI() {

		selectAudioDevices = new SelectAudioDevices();

		setBorder(new EmptyBorder(20, 40, 20, 40));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		panelInputDevices = new JPanel();
		panelInputDevices.setLayout(new BoxLayout(panelInputDevices, BoxLayout.PAGE_AXIS));
		panelInputDevices.setBorder(new EmptyBorder(10, 10, 10, 10));
		{
			panelMixerInfosInput = new JPanel();
			panelMixerInfosInput.setLayout(new BoxLayout(panelMixerInfosInput, BoxLayout.PAGE_AXIS));
			panelMixerInfosInput.setPreferredSize(new Dimension(400, selectAudioDevices.getInputDevices().size() * 40));
			panelMixerInfosInput.setMaximumSize(new Dimension(400, 160));
			{
				for (Mixer.Info mi : selectAudioDevices.getInputDevices()) {
					panelMixerInfosInput.add(new PanelMixerInfo(mi));
				}

			}

			jspSelectInputDevices = new JScrollPane(panelMixerInfosInput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jspSelectInputDevices.getVerticalScrollBar().setUI(new MyScrollBarUI());
			jspSelectInputDevices.setMaximumSize(new Dimension(405, 160));
			jspSelectInputDevices.setVisible(false);

			psm = new PanelSelectMixer((PanelMixerInfo) panelMixerInfosInput.getComponent(0), this, "INPUT");
			panelInputDevices.add(psm);

			panelInputDevices.add(jspSelectInputDevices);
			panelInputDevices.add(Box.createVerticalGlue());
		}

		add(panelInputDevices);
		add(Box.createVerticalGlue());
	}

	public void showSelectionPanel(String device) {

		switch (device) {
		case "INPUT":
			if (!inputPanelIsShowing) {
				jspSelectInputDevices.setVisible(true);
				jspSelectInputDevices.revalidate();
				revalidate();
				repaint();
				inputPanelIsShowing = true;
			} else {
				jspSelectInputDevices.setVisible(false);
				repaint();
				inputPanelIsShowing = false;
			}
		case "OUTPUT":

		}
	}

}
