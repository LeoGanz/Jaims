package jaims_development_studio.jaims.client.gui.settings;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.settings.EEndianness;
import jaims_development_studio.jaims.api.settings.EFileFormatType;
import jaims_development_studio.jaims.api.settings.EInputEncodingType;
import jaims_development_studio.jaims.client.audio.ListAudioDevices;
import jaims_development_studio.jaims.client.gui.GUIMain;

public class PanelAudioSettings extends SettingPanelsParent {
	private static final Logger	LOG		= LoggerFactory.getLogger(PanelAudioSettings.class);

	private GUIMain				guiMain;
	private String				input	= "Aufnahme-Einstellungen", output = "Ausgabe-Einstellungen";
	private JPanel				panelInputDevice, panelInputVolume, panelInputFormat, panelSupported, panelOutputDevice,
			panelOutputVolume;
	private String[]			inputMixerNames, outputMixerNames,
			sampleRate = {"8000 Hz", "11025 Hz", "16000 Hz", "22050 Hz", "44100 Hz", "48000 Hz"},
			sampleSize = {"8 bit", "16 bit"}, channels = {"1", "2", "5", "7"};
	private ListAudioDevices	selectAudioDevices;
	private JComboBox<String>	jccInputDevices, jccOutputDevices;
	private boolean				lineSupported;

	public PanelAudioSettings(GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI();
	}

	private void initGUI() {

		selectAudioDevices = new ListAudioDevices();

		setBorder(new EmptyBorder(20, 40, 20, 40));
		setBackground(Color.DARK_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JLabel lbl = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 17));
				g2.setStroke(new BasicStroke(2.3F));

				int xStart = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth(input) / 2;
				int yFontStart = getHeight() / 2 + g2.getFontMetrics(g2.getFont()).getHeight() / 2;
				int xEnd = getWidth() / 2 + g2.getFontMetrics(g2.getFont()).stringWidth(input) / 2;
				int stringHeight = g2.getFontMetrics(g2.getFont()).getHeight();

				g2.setColor(Color.WHITE);
				g2.drawString(input, xStart, yFontStart);

				g2.setColor(new Color(55, 102, 255));
				g2.setStroke(new BasicStroke(3));
				g2.drawLine(xStart - 20, yFontStart + 5, xStart - 20, yFontStart - stringHeight);
				g2.drawLine(xStart - 19, yFontStart + 5, xStart - 13, yFontStart + 9);
				g2.drawLine(xStart - 19, yFontStart - stringHeight, xStart - 13, yFontStart - stringHeight - 4);
				g2.drawLine(xEnd + 20, yFontStart + 5, xEnd + 20, yFontStart - stringHeight);
				g2.drawLine(xEnd + 19, yFontStart + 5, xEnd + 13, yFontStart + 9);
				g2.drawLine(xEnd + 19, yFontStart - stringHeight, xEnd + 13, yFontStart - stringHeight - 4);

				g2.dispose();
			}
		};
		lbl.setPreferredSize(new Dimension(500, 70));
		lbl.setMaximumSize(lbl.getPreferredSize());
		lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl.setOpaque(false);
		lbl.setBackground(new Color(0, 0, 0, 0));
		add(lbl);
		add(Box.createRigidArea(new Dimension(0, 5)));

		JLabel lblInputMixer = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.setColor(Color.WHITE);
				g2.drawString("Aufnahmegerät", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(new Color(55, 102, 255));
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
						g2.getFontMetrics(g2.getFont()).stringWidth("Aufnahmegerät"),
						g2.getFontMetrics(g2.getFont()).getHeight() + 7);

				g2.dispose();
			}
		};
		lblInputMixer.setPreferredSize(new Dimension(600, 40));
		lblInputMixer.setMaximumSize(lblInputMixer.getPreferredSize());
		lblInputMixer.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblInputMixer.setOpaque(false);
		lblInputMixer.setBackground(new Color(0, 0, 0, 0));
		add(lblInputMixer);

		panelInputDevice = new JPanel();
		panelInputDevice.setLayout(new BoxLayout(panelInputDevice, BoxLayout.PAGE_AXIS));
		panelInputDevice.setBackground(Color.DARK_GRAY);
		panelInputDevice.setBorder(new EmptyBorder(5, 10, 5, 10));
		{

			JLabel lblSelectMixerIn = new JLabel("Wählen Sie ein Aufnahmegerät aus: ", JLabel.LEFT);
			lblSelectMixerIn.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblSelectMixerIn.setPreferredSize(new Dimension(500, 25));
			lblSelectMixerIn.setMaximumSize(lblSelectMixerIn.getPreferredSize());
			lblSelectMixerIn.setOpaque(false);
			lblSelectMixerIn.setBackground(new Color(0, 0, 0, 0));
			lblSelectMixerIn.setForeground(Color.WHITE);
			panelInputDevice.add(lblSelectMixerIn);
			panelInputDevice.add(Box.createRigidArea(new Dimension(0, 2)));

			inputMixerNames = new String[selectAudioDevices.getInputDevices().size()];
			for (int i = 0; i < selectAudioDevices.getInputDevices().size(); i++) {
				inputMixerNames[i] = selectAudioDevices.getInputDevices().get(i).getName();

			}

			jccInputDevices = new JComboBox<>(inputMixerNames);
			jccInputDevices.setOpaque(false);
			jccInputDevices.setMaximumRowCount(4);
			jccInputDevices.setPreferredSize(new Dimension(500, 30));
			jccInputDevices.setMaximumSize(jccInputDevices.getPreferredSize());
			jccInputDevices.setAlignmentX(Component.CENTER_ALIGNMENT);
			if (guiMain.getSettings().getInputMixerInfoName() != null) {
				for (int i = 0; i < inputMixerNames.length; i++) {
					if (inputMixerNames[i].equals(guiMain.getSettings().getInputMixerInfoName())) {
						jccInputDevices.setSelectedIndex(i);
						break;
					} else
						jccInputDevices.setSelectedIndex(0);
				}
			} else {
				jccInputDevices.setSelectedIndex(0);
				guiMain.getSettings().setInputMixerInfo(inputMixerNames[0]);
			}
			jccInputDevices.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					if (e.getStateChange() == ItemEvent.SELECTED) {
						guiMain.getSettings().setInputMixerInfo(
								selectAudioDevices.getInputDevices().get(jccInputDevices.getSelectedIndex()).getName());

						updateInputLine();
					}

				}
			});
			panelInputDevice.add(jccInputDevices);

		}

		add(panelInputDevice);
		add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel lblSelectInputVolume = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.setColor(Color.WHITE);
				g2.drawString("Aufnahmepegel", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(new Color(55, 102, 255));
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
						g2.getFontMetrics(g2.getFont()).stringWidth("Aufnahmepegel"),
						g2.getFontMetrics(g2.getFont()).getHeight() + 7);

				g2.dispose();
			}
		};
		lblSelectInputVolume.setPreferredSize(new Dimension(600, 40));
		lblSelectInputVolume.setMaximumSize(lblSelectInputVolume.getPreferredSize());
		lblSelectInputVolume.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblSelectInputVolume.setOpaque(false);
		lblSelectInputVolume.setBackground(new Color(0, 0, 0, 0));
		add(lblSelectInputVolume);

		panelInputVolume = new JPanel();
		panelInputVolume.setLayout(new BoxLayout(panelInputVolume, BoxLayout.PAGE_AXIS));
		panelInputVolume.setBackground(Color.DARK_GRAY);
		panelInputVolume.setBorder(new EmptyBorder(5, 10, 5, 10));
		{
			JLabel lblSelectGain = new JLabel("Wählen Sie die Eingangspegelverstärkung / -reduzierung aus:",
					JLabel.LEFT);
			lblSelectGain.setForeground(Color.WHITE);
			lblSelectGain.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblSelectGain.setPreferredSize(new Dimension(500, 25));
			lblSelectGain.setMaximumSize(lblSelectGain.getPreferredSize());
			lblSelectGain.setOpaque(false);
			lblSelectGain.setBackground(new Color(0, 0, 0, 0));
			panelInputVolume.add(lblSelectGain);
			panelInputVolume.add(Box.createRigidArea(new Dimension(0, 2)));

			JSlider sliderGain = new JSlider(JSlider.HORIZONTAL, 0, 200,
					(int) (guiMain.getSettings().getInputGain() * 100));
			sliderGain.setMajorTickSpacing(50);
			sliderGain.setMinorTickSpacing(10);
			sliderGain.setPaintTicks(true);
			sliderGain.setPreferredSize(new Dimension(500, 40));
			sliderGain.setMaximumSize(sliderGain.getPreferredSize());
			sliderGain.setAlignmentX(Component.CENTER_ALIGNMENT);
			sliderGain.setOpaque(false);
			sliderGain.setBackground(new Color(0, 0, 0, 0));
			sliderGain.setForeground(Color.WHITE);
			sliderGain.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {

					JSlider source = (JSlider) e.getSource();
					if (!source.getValueIsAdjusting()) {
						guiMain.getSettings().setInputGain((float) source.getValue() / 100);
					}
				}
			});

			Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			labelTable.put(new Integer(0), new JLabel("0 %"));
			labelTable.put(new Integer(50), new JLabel("50 %"));
			labelTable.put(new Integer(100), new JLabel("100 %"));
			labelTable.put(new Integer(150), new JLabel("150 %"));
			labelTable.put(new Integer(200), new JLabel("200 %"));
			sliderGain.setLabelTable(labelTable);
			sliderGain.setPaintLabels(true);
			panelInputVolume.add(sliderGain);
		}
		add(panelInputVolume);
		add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel lblSelectFileFormat = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.setColor(Color.WHITE);
				g2.drawString("Dateiformat", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(new Color(55, 102, 255));
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
						g2.getFontMetrics(g2.getFont()).stringWidth("Dateiformat"),
						g2.getFontMetrics(g2.getFont()).getHeight() + 7);

				g2.dispose();
			}
		};
		lblSelectFileFormat.setPreferredSize(new Dimension(600, 40));
		lblSelectFileFormat.setMaximumSize(lblSelectFileFormat.getPreferredSize());
		lblSelectFileFormat.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblSelectFileFormat.setOpaque(false);
		lblSelectFileFormat.setBackground(new Color(0, 0, 0, 0));
		add(lblSelectFileFormat);

		JPanel panelSelectFileFormat = new JPanel();
		panelSelectFileFormat.setBorder(new EmptyBorder(5, 10, 5, 10));
		panelSelectFileFormat.setBackground(Color.DARK_GRAY);
		panelSelectFileFormat.setLayout(new BoxLayout(panelSelectFileFormat, BoxLayout.PAGE_AXIS));
		{
			JLabel lblSelectFFormat = new JLabel("Wählen Sie das gewünschte Dateiformat:", JLabel.LEFT);
			lblSelectFFormat.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblSelectFFormat.setPreferredSize(new Dimension(500, 25));
			lblSelectFFormat.setMaximumSize(lblSelectFFormat.getPreferredSize());
			lblSelectFFormat.setOpaque(false);
			lblSelectFFormat.setBackground(new Color(0, 0, 0, 0));
			lblSelectFFormat.setForeground(Color.WHITE);
			panelSelectFileFormat.add(lblSelectFFormat);
			panelSelectFileFormat.add(Box.createRigidArea(new Dimension(0, 2)));

			JComboBox<EFileFormatType> jccFileFormat = new JComboBox<>(EFileFormatType.values());
			jccFileFormat.setOpaque(false);
			jccFileFormat.setPreferredSize(new Dimension(500, 30));
			jccFileFormat.setMaximumSize(jccFileFormat.getPreferredSize());
			jccFileFormat.setEditable(false);
			jccFileFormat.setAlignmentX(Component.CENTER_ALIGNMENT);
			for (int i = 0; i < EFileFormatType.values().length; i++) {

				if (EFileFormatType.values()[i].equals(guiMain.getSettings().getInputFileFormatString())) {
					jccFileFormat.setSelectedIndex(i);
					break;
				} else
					jccFileFormat.setSelectedIndex(0);
			}
			jccFileFormat.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					if (e.getStateChange() == ItemEvent.SELECTED) {
						@SuppressWarnings("unchecked")
						int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
						if (i == 0)
							guiMain.getSettings().setInputFileFormat(EFileFormatType.FORMAT_WAVE);
						else if (i == 1)
							guiMain.getSettings().setInputFileFormat(EFileFormatType.FORMAT_AU);
						else if (i == 2)
							guiMain.getSettings().setInputFileFormat(EFileFormatType.FORMAT_AIFF);
						else if (i == 3)
							guiMain.getSettings().setInputFileFormat(EFileFormatType.FORMAT_AIFC);
						else if (i == 4)
							guiMain.getSettings().setInputFileFormat(EFileFormatType.FORMAT_SND);

						updateInputLine();
					}

				}
			});

			panelSelectFileFormat.add(jccFileFormat);

		}
		add(panelSelectFileFormat);
		add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel lblInputFormat = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.setColor(Color.WHITE);
				g2.drawString("Aufnahmeformat", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(new Color(55, 102, 255));
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
						g2.getFontMetrics(g2.getFont()).stringWidth("Aufnahmeformat"),
						g2.getFontMetrics(g2.getFont()).getHeight() + 7);

				g2.dispose();
			}
		};
		lblInputFormat.setPreferredSize(new Dimension(600, 40));
		lblInputFormat.setMaximumSize(lblInputFormat.getPreferredSize());
		lblInputFormat.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblInputFormat.setOpaque(false);
		lblInputFormat.setBackground(new Color(0, 0, 0, 0));
		add(lblInputFormat);

		panelInputFormat = new JPanel();
		panelInputFormat.setLayout(new BoxLayout(panelInputFormat, BoxLayout.PAGE_AXIS));
		panelInputFormat.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelInputFormat.setBackground(Color.DARK_GRAY);
		panelInputFormat.setBorder(new EmptyBorder(5, 10, 5, 10));
		{
			JPanel panelFirstFormats = new JPanel();
			panelFirstFormats.setAlignmentX(Component.CENTER_ALIGNMENT);
			panelFirstFormats.setBackground(Color.DARK_GRAY);
			panelFirstFormats.setLayout(new BoxLayout(panelFirstFormats, BoxLayout.LINE_AXIS));
			{
				panelFirstFormats.add(Box.createHorizontalGlue());

				JPanel panelEncoding = new JPanel();
				panelEncoding.setBackground(Color.DARK_GRAY);
				panelEncoding.setLayout(new BoxLayout(panelEncoding, BoxLayout.PAGE_AXIS));
				{
					JLabel lblSelectEncoding = new JLabel("Wählen Sie eine Kodierung:", JLabel.LEFT);
					lblSelectEncoding.setPreferredSize(new Dimension(160, 25));
					lblSelectEncoding.setMaximumSize(lblSelectEncoding.getPreferredSize());
					lblSelectEncoding.setAlignmentX(Component.CENTER_ALIGNMENT);
					lblSelectEncoding.setOpaque(false);
					lblSelectEncoding.setBackground(new Color(0, 0, 0, 0));
					lblSelectEncoding.setForeground(Color.WHITE);
					panelEncoding.add(lblSelectEncoding);
					panelEncoding.add(Box.createRigidArea(new Dimension(0, 2)));

					JComboBox<EInputEncodingType> jccSelectEncoding = new JComboBox<>(EInputEncodingType.values());
					jccSelectEncoding.setOpaque(false);
					jccSelectEncoding.setPreferredSize(new Dimension(160, 25));
					jccSelectEncoding.setMaximumSize(jccSelectEncoding.getPreferredSize());
					jccSelectEncoding.setEditable(false);
					jccSelectEncoding.setAlignmentX(Component.CENTER_ALIGNMENT);
					for (int i = 0; i < EInputEncodingType.values().length; i++) {
						if (EInputEncodingType.values()[i].equals(guiMain.getSettings().getInputEncoding())) {
							jccSelectEncoding.setSelectedIndex(i);
							break;
						} else
							jccSelectEncoding.setSelectedIndex(2);
					}
					jccSelectEncoding.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							if (e.getStateChange() == ItemEvent.SELECTED) {
								@SuppressWarnings("unchecked")
								int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
								if (i == 0)
									guiMain.getSettings().setInputEncoding(EInputEncodingType.ENCODING_ALAW);
								else if (i == 1)
									guiMain.getSettings().setInputEncoding(EInputEncodingType.ENCODING_PCM_FLOAT);
								else if (i == 2)
									guiMain.getSettings().setInputEncoding(EInputEncodingType.ENCODING_PCM_SIGNED);
								else if (i == 3)
									guiMain.getSettings().setInputEncoding(EInputEncodingType.ENCODING_PCM_UNSIGNED);
								else if (i == 4)
									guiMain.getSettings().setInputEncoding(EInputEncodingType.ENCODING_ULAW);

								updateInputLine();
							}

						}
					});
					panelEncoding.add(jccSelectEncoding);

				}
				panelFirstFormats.add(panelEncoding);
				panelFirstFormats.add(Box.createHorizontalGlue());

				JPanel panelSampleRate = new JPanel();
				panelSampleRate.setBackground(Color.DARK_GRAY);
				panelSampleRate.setLayout(new BoxLayout(panelSampleRate, BoxLayout.PAGE_AXIS));
				{
					JLabel lblSampleRate = new JLabel("Wahlen Sie eine Abtastrate:", JLabel.LEFT);
					lblSampleRate.setPreferredSize(new Dimension(160, 25));
					lblSampleRate.setMaximumSize(lblSampleRate.getPreferredSize());
					lblSampleRate.setAlignmentX(Component.CENTER_ALIGNMENT);
					lblSampleRate.setOpaque(false);
					lblSampleRate.setBackground(new Color(0, 0, 0, 0));
					lblSampleRate.setForeground(Color.WHITE);
					panelSampleRate.add(lblSampleRate);
					panelSampleRate.add(Box.createRigidArea(new Dimension(0, 2)));

					JComboBox<String> jccSampleRate = new JComboBox<>(sampleRate);
					jccSampleRate.setOpaque(false);
					jccSampleRate.setPreferredSize(new Dimension(160, 25));
					jccSampleRate.setMaximumSize(jccSampleRate.getPreferredSize());
					jccSampleRate.setMaximumRowCount(4);
					jccSampleRate.setBackground(new Color(0, 0, 0, 0));
					jccSampleRate.setEditable(false);
					jccSampleRate.setAlignmentX(Component.CENTER_ALIGNMENT);
					for (int i = 0; i < sampleRate.length; i++) {
						String s = guiMain.getSettings().getInputSampleRate() + "";
						if (sampleRate[i].equals(s.substring(0, s.length() - 2) + " Hz")) {
							jccSampleRate.setSelectedIndex(i);
							break;
						} else
							jccSampleRate.setSelectedIndex(2);
					}
					jccSampleRate.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							if (e.getStateChange() == ItemEvent.SELECTED) {
								@SuppressWarnings("unchecked")
								int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
								if (i == 0)
									guiMain.getSettings().setInputSampleRate(8000);
								else if (i == 1)
									guiMain.getSettings().setInputSampleRate(11025);
								else if (i == 2)
									guiMain.getSettings().setInputSampleRate(16000);
								else if (i == 3)
									guiMain.getSettings().setInputSampleRate(22050);
								else if (i == 4)
									guiMain.getSettings().setInputSampleRate(44100);
								else if (i == 5)
									guiMain.getSettings().setInputSampleRate(48000);

								updateInputLine();
							}

						}
					});
					panelSampleRate.add(jccSampleRate);
					if (jccSampleRate.getUI().getAccessibleChild(jccSampleRate, 0) instanceof JPopupMenu) {
						((JScrollPane) ((JPopupMenu) jccSampleRate.getUI().getAccessibleChild(jccSampleRate, 0))
								.getComponent(0)).getVerticalScrollBar().setBackground(Color.WHITE);
					}

				}
				panelFirstFormats.add(panelSampleRate);
				panelFirstFormats.add(Box.createHorizontalGlue());

				JPanel panelSampleSize = new JPanel();
				panelSampleSize.setBackground(Color.DARK_GRAY);
				panelSampleSize.setLayout(new BoxLayout(panelSampleSize, BoxLayout.PAGE_AXIS));
				{
					JLabel lblSampleSize = new JLabel("Wahlen Sie eine Abtasttiefe:", JLabel.LEFT);
					lblSampleSize.setPreferredSize(new Dimension(160, 25));
					lblSampleSize.setMaximumSize(lblSampleSize.getPreferredSize());
					lblSampleSize.setAlignmentX(Component.CENTER_ALIGNMENT);
					lblSampleSize.setOpaque(false);
					lblSampleSize.setBackground(new Color(0, 0, 0, 0));
					lblSampleSize.setForeground(Color.WHITE);
					panelSampleSize.add(lblSampleSize);
					panelSampleSize.add(Box.createRigidArea(new Dimension(0, 2)));

					JComboBox<String> jccSampleSize = new JComboBox<>(sampleSize);
					jccSampleSize.setOpaque(false);
					jccSampleSize.setPreferredSize(new Dimension(160, 25));
					jccSampleSize.setMaximumSize(jccSampleSize.getPreferredSize());
					jccSampleSize.setBackground(new Color(0, 0, 0, 0));
					jccSampleSize.setEditable(false);
					jccSampleSize.setAlignmentX(Component.CENTER_ALIGNMENT);
					for (int i = 0; i < sampleSize.length; i++) {
						if (sampleSize[i].equals(guiMain.getSettings().getInputSampleSize() + " bit")) {
							jccSampleSize.setSelectedIndex(i);
							break;
						} else
							jccSampleSize.setSelectedIndex(1);
					}
					jccSampleSize.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							if (e.getStateChange() == ItemEvent.SELECTED) {
								@SuppressWarnings("unchecked")
								int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
								if (i == 0)
									guiMain.getSettings().setInputSampleSize(8);
								else if (i == 1)
									guiMain.getSettings().setInputSampleSize(16);

								updateInputLine();

							}

						}
					});
					panelSampleSize.add(jccSampleSize);
				}
				panelFirstFormats.add(panelSampleSize);
				panelFirstFormats.add(Box.createHorizontalGlue());
			}
			panelInputFormat.add(panelFirstFormats);

			JPanel panelSecondFormats = new JPanel();
			panelSecondFormats.setLayout(new BoxLayout(panelSecondFormats, BoxLayout.LINE_AXIS));
			panelSecondFormats.setBackground(Color.DARK_GRAY);
			panelSecondFormats.setAlignmentX(Component.CENTER_ALIGNMENT);
			{
				panelSecondFormats.add(Box.createHorizontalGlue());

				JPanel panelChannels = new JPanel();
				panelChannels.setBackground(Color.DARK_GRAY);
				panelChannels.setLayout(new BoxLayout(panelChannels, BoxLayout.PAGE_AXIS));
				{
					JLabel selectChannels = new JLabel("Wählen Sie die Anzahl der Kanäle:", JLabel.LEFT);
					selectChannels.setPreferredSize(new Dimension(180, 25));
					selectChannels.setMaximumSize(selectChannels.getPreferredSize());
					selectChannels.setAlignmentX(Component.CENTER_ALIGNMENT);
					selectChannels.setOpaque(false);
					selectChannels.setBackground(new Color(0, 0, 0, 0));
					selectChannels.setForeground(Color.WHITE);
					panelChannels.add(selectChannels);
					panelChannels.add(Box.createRigidArea(new Dimension(0, 2)));

					JComboBox<String> jccChannels = new JComboBox<>(channels);
					jccChannels.setOpaque(false);
					jccChannels.setPreferredSize(new Dimension(180, 25));
					jccChannels.setMaximumSize(jccChannels.getPreferredSize());
					jccChannels.setBackground(new Color(0, 0, 0, 0));
					jccChannels.setEditable(false);
					jccChannels.setAlignmentX(Component.CENTER_ALIGNMENT);
					for (int i = 0; i < channels.length; i++) {
						if (channels[i].equals(guiMain.getSettings().getInputChannels() + "")) {
							jccChannels.setSelectedIndex(i);
							break;
						} else
							jccChannels.setSelectedIndex(1);
					}
					jccChannels.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							if (e.getStateChange() == ItemEvent.SELECTED) {
								@SuppressWarnings("unchecked")
								int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
								if (i == 0)
									guiMain.getSettings().setInputChannels(1);
								else if (i == 1)
									guiMain.getSettings().setInputChannels(2);
								else if (i == 2)
									guiMain.getSettings().setInputChannels(5);
								else if (i == 3)
									guiMain.getSettings().setInputChannels(7);

								updateInputLine();
							}

						}
					});
					panelChannels.add(jccChannels);
				}
				panelSecondFormats.add(panelChannels);
				panelSecondFormats.add(Box.createRigidArea(new Dimension(20, 0)));

				JPanel panelEndian = new JPanel();
				panelEndian.setBackground(Color.DARK_GRAY);
				panelEndian.setLayout(new BoxLayout(panelEndian, BoxLayout.PAGE_AXIS));
				{
					JLabel selectEndian = new JLabel("Wählen sie die Byte-Reihenfolge", JLabel.LEFT);
					selectEndian.setPreferredSize(new Dimension(180, 25));
					selectEndian.setMaximumSize(selectEndian.getPreferredSize());
					selectEndian.setAlignmentX(Component.CENTER_ALIGNMENT);
					selectEndian.setOpaque(false);
					selectEndian.setBackground(new Color(0, 0, 0, 0));
					selectEndian.setForeground(Color.WHITE);
					panelEndian.add(selectEndian);
					panelEndian.add(Box.createHorizontalGlue());

					JComboBox<EEndianness> jccEndian = new JComboBox<>(EEndianness.values());
					jccEndian.setOpaque(false);
					jccEndian.setPreferredSize(new Dimension(180, 25));
					jccEndian.setMaximumSize(jccEndian.getPreferredSize());
					jccEndian.setBackground(new Color(0, 0, 0, 0));
					jccEndian.setEditable(false);
					jccEndian.setAlignmentX(Component.CENTER_ALIGNMENT);
					if (guiMain.getSettings().isInputBigEndian())
						jccEndian.setSelectedIndex(1);
					else
						jccEndian.setSelectedIndex(0);
					jccEndian.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							if (e.getStateChange() == ItemEvent.SELECTED) {
								@SuppressWarnings("unchecked")
								int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
								if (i == 0)
									guiMain.getSettings().setInputBigEndian(EEndianness.LITTLE_ENDIAN);
								else if (i == 1)
									guiMain.getSettings().setInputBigEndian(EEndianness.BIG_ENDIAN);

								updateInputLine();

							}
						}
					});
					panelEndian.add(jccEndian);
				}
				panelSecondFormats.add(panelEndian);
				panelSecondFormats.add(Box.createHorizontalGlue());
			}

			panelInputFormat.add(Box.createRigidArea(new Dimension(0, 5)));
			panelInputFormat.add(panelSecondFormats);
		}
		add(panelInputFormat);
		add(Box.createRigidArea(new Dimension(0, 10)));

		panelSupported = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				if (lineSupported) {
					int xStart = getWidth() / 2 - g2.getFontMetrics(g2.getFont())
							.stringWidth("Aufnahmeformat wird vom gewählten Aufnahmegerät unterstützt") / 2;
					int yFontStart = getHeight() / 2 + g2.getFontMetrics(g2.getFont()).getHeight() / 2;
					int stringHeight = g2.getFontMetrics(g2.getFont()).getHeight();

					g2.setStroke(new BasicStroke(1.8F));
					g2.setColor(Color.WHITE);
					g2.drawString("Aufnahmeformat wird vom gewählten Aufnahmegerät unterstützt", xStart, yFontStart);

					g2.setColor(Color.GREEN);
					g2.setStroke(new BasicStroke(3));
					g2.drawLine(xStart - 30, yFontStart - 5, xStart - 22, (int) (yFontStart + stringHeight / 2));
					g2.drawLine(xStart - 22, (int) (yFontStart + stringHeight / 2), xStart - 6,
							(int) (yFontStart - 2 * stringHeight));
				} else {
					int xStart = getWidth() / 2 - g2.getFontMetrics(g2.getFont())
							.stringWidth("Aufnahmeformat wird vom gewählten Aufnahmegerät nicht unterstützt") / 2;
					int yFontStart = getHeight() / 2 + g2.getFontMetrics(g2.getFont()).getHeight() / 2;
					int stringHeight = g2.getFontMetrics(g2.getFont()).getHeight();

					g2.setStroke(new BasicStroke(1.8F));
					g2.setColor(Color.WHITE);
					g2.drawString("Aufnahmeformat wird vom gewählten Aufnahmegerät nicht unterstützt", xStart,
							yFontStart);

					g2.setColor(Color.RED);
					g2.setStroke(new BasicStroke(3));
					g2.drawLine(xStart - 30, (int) (yFontStart - 2 * stringHeight), xStart - 6,
							(int) (yFontStart + stringHeight / 2));
					g2.drawLine(xStart - 30, (int) (yFontStart + stringHeight / 2), xStart - 6,
							(int) (yFontStart - 2 * stringHeight));
				}
			}
		};
		panelSupported.setPreferredSize(new Dimension(600, 50));
		panelSupported.setMaximumSize(panelSupported.getPreferredSize());
		panelSupported.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(panelSupported);
		add(Box.createRigidArea(new Dimension(0, 20)));

		JLabel lblOutput = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 17));
				g2.setStroke(new BasicStroke(2.3F));

				int xStart = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth(input) / 2;
				int yFontStart = getHeight() / 2 + g2.getFontMetrics(g2.getFont()).getHeight() / 2;
				int xEnd = getWidth() / 2 + g2.getFontMetrics(g2.getFont()).stringWidth(input) / 2;
				int stringHeight = g2.getFontMetrics(g2.getFont()).getHeight();

				g2.setColor(Color.WHITE);
				g2.drawString(output, xStart, yFontStart);

				g2.setColor(new Color(55, 102, 255));
				g2.setStroke(new BasicStroke(3));
				g2.drawLine(xStart - 20, yFontStart + 5, xStart - 20, yFontStart - stringHeight);
				g2.drawLine(xStart - 19, yFontStart + 5, xStart - 13, yFontStart + 9);
				g2.drawLine(xStart - 19, yFontStart - stringHeight, xStart - 13, yFontStart - stringHeight - 4);
				g2.drawLine(xEnd + 20, yFontStart + 5, xEnd + 20, yFontStart - stringHeight);
				g2.drawLine(xEnd + 19, yFontStart + 5, xEnd + 13, yFontStart + 9);
				g2.drawLine(xEnd + 19, yFontStart - stringHeight, xEnd + 13, yFontStart - stringHeight - 4);

				g2.dispose();
			}
		};
		lblOutput.setPreferredSize(new Dimension(500, 70));
		lblOutput.setMaximumSize(lblOutput.getPreferredSize());
		lblOutput.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblOutput.setOpaque(false);
		lblOutput.setBackground(new Color(0, 0, 0, 0));
		add(lblOutput);
		add(Box.createRigidArea(new Dimension(0, 5)));

		JLabel lblOutputMixer = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.setColor(Color.WHITE);
				g2.drawString("Ausgabegerät", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(new Color(55, 102, 255));
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
						g2.getFontMetrics(g2.getFont()).stringWidth("Ausgabegerät"),
						g2.getFontMetrics(g2.getFont()).getHeight() + 7);

				g2.dispose();
			}
		};
		lblOutputMixer.setPreferredSize(new Dimension(600, 40));
		lblOutputMixer.setMaximumSize(lblOutputMixer.getPreferredSize());
		lblOutputMixer.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblOutputMixer.setOpaque(false);
		lblOutputMixer.setBackground(new Color(0, 0, 0, 0));
		add(lblOutputMixer);

		panelOutputDevice = new JPanel();
		panelOutputDevice.setLayout(new BoxLayout(panelOutputDevice, BoxLayout.PAGE_AXIS));
		panelOutputDevice.setBackground(Color.DARK_GRAY);
		panelOutputDevice.setBorder(new EmptyBorder(5, 10, 5, 10));
		{
			JLabel lblSelectOutput = new JLabel("Wählen Sie ein Wiedergabegerät aus", JLabel.LEFT);
			lblSelectOutput.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblSelectOutput.setPreferredSize(new Dimension(500, 25));
			lblSelectOutput.setMaximumSize(lblSelectOutput.getPreferredSize());
			lblSelectOutput.setOpaque(false);
			lblSelectOutput.setBackground(new Color(0, 0, 0, 0));
			lblSelectOutput.setForeground(Color.WHITE);
			panelOutputDevice.add(lblSelectOutput);
			panelOutputDevice.add(Box.createRigidArea(new Dimension(0, 2)));

			outputMixerNames = new String[selectAudioDevices.getOutputDevices().size()];
			for (int i = 0; i < selectAudioDevices.getOutputDevices().size(); i++) {
				outputMixerNames[i] = selectAudioDevices.getOutputDevices().get(i).getName();

			}

			jccOutputDevices = new JComboBox<>(outputMixerNames);
			jccOutputDevices.setOpaque(false);
			jccOutputDevices.setMaximumRowCount(4);
			jccOutputDevices.setPreferredSize(new Dimension(500, 40));
			jccOutputDevices.setMaximumSize(jccOutputDevices.getPreferredSize());
			jccOutputDevices.setBackground(new Color(0, 0, 0, 0));
			if (guiMain.getSettings().getOutputMixerInfoName() != null
					&& !guiMain.getSettings().getOutputMixerInfoName().equals("")) {
				for (int i = 0; i < outputMixerNames.length; i++) {
					if (outputMixerNames[i].equals(guiMain.getSettings().getOutputMixerInfoName())) {
						jccOutputDevices.setSelectedIndex(i);
						break;
					} else
						jccOutputDevices.setSelectedIndex(0);
				}
			} else {
				jccOutputDevices.setSelectedIndex(0);
				guiMain.getSettings().setOutputMixerInfo(outputMixerNames[0]);
			}
			jccOutputDevices.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					if (e.getStateChange() == ItemEvent.SELECTED) {
						guiMain.getSettings().setOutputMixerInfo(selectAudioDevices.getOutputDevices()
								.get(jccOutputDevices.getSelectedIndex()).getName());
					}

				}
			});
			panelOutputDevice.add(jccOutputDevices);
		}
		add(panelOutputDevice);
		add(Box.createRigidArea(new Dimension(0, 5)));

		JLabel lblOutputVolume = new JLabel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.setColor(Color.WHITE);
				g2.drawString("Wiedergabelautstärke", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(new Color(55, 102, 255));
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(2, g2.getFontMetrics(g2.getFont()).getHeight() + 7,
						g2.getFontMetrics(g2.getFont()).stringWidth("Wiedergabelautstärke"),
						g2.getFontMetrics(g2.getFont()).getHeight() + 7);

				g2.dispose();
			}
		};
		lblOutputVolume.setPreferredSize(new Dimension(600, 40));
		lblOutputVolume.setMaximumSize(lblOutputVolume.getPreferredSize());
		lblOutputVolume.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblOutputVolume.setOpaque(false);
		lblOutputVolume.setBackground(new Color(0, 0, 0, 0));
		add(lblOutputVolume);

		panelOutputVolume = new JPanel();
		panelOutputVolume.setLayout(new BoxLayout(panelOutputVolume, BoxLayout.PAGE_AXIS));
		panelOutputVolume.setBackground(Color.DARK_GRAY);
		panelOutputVolume.setBorder(new EmptyBorder(5, 10, 5, 10));
		{
			JLabel lblSelectGain = new JLabel("Wählen Sie die Wiedergabelautstärke:", JLabel.LEFT);
			lblSelectGain.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblSelectGain.setPreferredSize(new Dimension(500, 25));
			lblSelectGain.setMaximumSize(lblSelectGain.getPreferredSize());
			lblSelectGain.setOpaque(false);
			lblSelectGain.setBackground(new Color(0, 0, 0, 0));
			lblSelectGain.setForeground(Color.WHITE);
			panelOutputVolume.add(lblSelectGain);
			panelOutputVolume.add(Box.createRigidArea(new Dimension(0, 2)));

			JSlider sliderGain = new JSlider(JSlider.HORIZONTAL, 0, 200,
					(int) (guiMain.getSettings().getOutputVolume() * 100));
			sliderGain.setMajorTickSpacing(50);
			sliderGain.setMinorTickSpacing(10);
			sliderGain.setPaintTicks(true);
			sliderGain.setPreferredSize(new Dimension(500, 40));
			sliderGain.setMaximumSize(sliderGain.getPreferredSize());
			sliderGain.setAlignmentX(Component.CENTER_ALIGNMENT);
			sliderGain.setOpaque(false);
			sliderGain.setBackground(new Color(0, 0, 0, 0));
			sliderGain.setForeground(Color.WHITE);
			sliderGain.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {

					JSlider source = (JSlider) e.getSource();
					if (!source.getValueIsAdjusting()) {
						guiMain.getSettings().setOutputVolume((float) source.getValue() / 100);
					}
				}
			});

			Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			labelTable.put(new Integer(0), new JLabel("0 %"));
			labelTable.put(new Integer(50), new JLabel("50 %"));
			labelTable.put(new Integer(100), new JLabel("100 %"));
			labelTable.put(new Integer(150), new JLabel("150 %"));
			labelTable.put(new Integer(200), new JLabel("200 %"));
			sliderGain.setLabelTable(labelTable);
			sliderGain.setPaintLabels(true);
			panelOutputVolume.add(sliderGain);
		}
		add(panelOutputVolume);
		add(Box.createRigidArea(new Dimension(0, 10)));

		add(Box.createVerticalGlue());

		updateInputLine();
	}

	private void updateInputLine() {

		AudioFormat af = guiMain.getSettings().getAudioFormat();
		Mixer m = null;
		if (guiMain.getSettings().getInputMixerInfoName() != null) {
			for (Mixer.Info mi : AudioSystem.getMixerInfo()) {
				if (mi.getName().equals(guiMain.getSettings().getInputMixerInfoName()))
					m = AudioSystem.getMixer(mi);
			}
			if (m == null) {
				m = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
			}
		} else {
			m = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
		}
		LOG.info("Is Line supported: " + m.isLineSupported(new DataLine.Info(TargetDataLine.class, af)));
		if (m.isLineSupported(new DataLine.Info(TargetDataLine.class, af)))
			lineSupported = true;
		else
			lineSupported = false;

		panelSupported.repaint();
	}
}
