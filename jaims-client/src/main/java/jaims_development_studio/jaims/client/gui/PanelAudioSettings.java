package jaims_development_studio.jaims.client.gui;

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

import jaims_development_studio.jaims.client.audio.SelectAudioDevices;
import jaims_development_studio.jaims.client.settings.Settings;

public class PanelAudioSettings extends CenterPanelSettings {

	private static final Logger	LOG					= LoggerFactory.getLogger(PanelAudioSettings.class);

	private SelectAudioDevices	selectAudioDevices;
	private PanelSelectMixer	psm;
	private String[]			arr;
	private JPanel				panelInputDevice, panelMixerInfosInput, panelInputVolume, panelInputFormat,
			panelOutputVolume, panelOutputDevice, panelSupported;
	private JScrollPane			jspSelectInputDevices;
	private boolean				inputPanelIsShowing	= false;
	private String[]			inputMixerNames, outputMixerNames;
	private JComboBox<String>	jccInputDevices, jccOutputDevices;
	private String				input				= "Aufnahme-Einstellungen", output = "Ausgabe-Einstellungen";
	private String[]			encoding			= {"ALAW", "PCM_FLOAT", "PCM_SIGNED", "PCM_UNSIGNED", "ULAW"},
			sampleRate = {"8,000 Hz", "11,025 Hz", "16,000 Hz", "22,050 Hz", "44,100 Hz", "48,000 Hz"},
			sampleSize = {"8 bit", "16 bit"}, channels = {"1", "2", "5", "7"}, endian = {"Little Endian", "Big Endian"};
	private boolean				lineSupported;

	public PanelAudioSettings() {

		initGUI();
	}

	private void initGUI() {

		selectAudioDevices = new SelectAudioDevices();

		setBorder(new EmptyBorder(20, 40, 20, 40));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JLabel lbl = new JLabel() {
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

				g2.setColor(Color.BLACK);
				g2.drawString(input, xStart, yFontStart);

				g2.setColor(Color.BLUE);
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
			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.drawString("Aufnahmegerät", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(Color.BLUE);
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
		panelInputDevice.setBorder(new EmptyBorder(5, 10, 5, 10));
		{

			JLabel lblSelectMixerIn = new JLabel("Wählen Sie ein Aufnahmegerät aus: ", JLabel.LEFT);
			lblSelectMixerIn.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblSelectMixerIn.setPreferredSize(new Dimension(500, 25));
			lblSelectMixerIn.setMaximumSize(lblSelectMixerIn.getPreferredSize());
			lblSelectMixerIn.setOpaque(false);
			lblSelectMixerIn.setBackground(new Color(0, 0, 0, 0));
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
			jccInputDevices.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					if (e.getStateChange() == ItemEvent.SELECTED) {
						Settings.inputMixerInfo = selectAudioDevices.getInputDevices()
								.get(jccInputDevices.getSelectedIndex());

						updateInputLine();
					}

				}
			});
			panelInputDevice.add(jccInputDevices);

		}

		add(panelInputDevice);
		add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel lblSelectInputVolume = new JLabel() {
			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.drawString("Aufnahmepegel", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(Color.BLUE);
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
		panelInputVolume.setBorder(new EmptyBorder(5, 10, 5, 10));
		{
			JLabel lblSelectGain = new JLabel("Wählen Sie die Eingangspegelverstärkung / -reduzierung aus:",
					JLabel.LEFT);
			lblSelectGain.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblSelectGain.setPreferredSize(new Dimension(500, 25));
			lblSelectGain.setMaximumSize(lblSelectGain.getPreferredSize());
			lblSelectGain.setOpaque(false);
			lblSelectGain.setBackground(new Color(0, 0, 0, 0));
			panelInputVolume.add(lblSelectGain);
			panelInputVolume.add(Box.createRigidArea(new Dimension(0, 2)));

			JSlider sliderGain = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
			sliderGain.setMajorTickSpacing(50);
			sliderGain.setMinorTickSpacing(10);
			sliderGain.setPaintTicks(true);
			sliderGain.setPreferredSize(new Dimension(500, 40));
			sliderGain.setMaximumSize(sliderGain.getPreferredSize());
			sliderGain.setAlignmentX(Component.CENTER_ALIGNMENT);
			sliderGain.setOpaque(false);
			sliderGain.setBackground(new Color(0, 0, 0, 0));
			sliderGain.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {

					JSlider source = (JSlider) e.getSource();
					if (!source.getValueIsAdjusting()) {
						Settings.inputGain = source.getValue() / 100;
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

		JLabel lblInputFormat = new JLabel() {
			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.drawString("Aufnahmeformat", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(Color.BLUE);
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
		panelInputFormat.setBorder(new EmptyBorder(5, 10, 5, 10));
		{
			JPanel panelFirstFormats = new JPanel();
			panelFirstFormats.setAlignmentX(Component.CENTER_ALIGNMENT);
			panelFirstFormats.setLayout(new BoxLayout(panelFirstFormats, BoxLayout.LINE_AXIS));
			{
				panelFirstFormats.add(Box.createHorizontalGlue());

				JPanel panelEncoding = new JPanel();
				panelEncoding.setLayout(new BoxLayout(panelEncoding, BoxLayout.PAGE_AXIS));
				{
					JLabel lblSelectEncoding = new JLabel("Wählen Sie eine Kodierung:", JLabel.LEFT);
					lblSelectEncoding.setPreferredSize(new Dimension(160, 25));
					lblSelectEncoding.setMaximumSize(lblSelectEncoding.getPreferredSize());
					lblSelectEncoding.setAlignmentX(Component.CENTER_ALIGNMENT);
					lblSelectEncoding.setOpaque(false);
					lblSelectEncoding.setBackground(new Color(0, 0, 0, 0));
					panelEncoding.add(lblSelectEncoding);
					panelEncoding.add(Box.createRigidArea(new Dimension(0, 2)));

					JComboBox<String> jccSelectEncoding = new JComboBox<String>(encoding);
					jccSelectEncoding.setOpaque(false);
					jccSelectEncoding.setPreferredSize(new Dimension(160, 25));
					jccSelectEncoding.setMaximumSize(jccSelectEncoding.getPreferredSize());
					jccSelectEncoding.setSelectedIndex(2);
					jccSelectEncoding.setEditable(false);
					jccSelectEncoding.setAlignmentX(Component.CENTER_ALIGNMENT);
					jccSelectEncoding.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							if (e.getStateChange() == ItemEvent.SELECTED) {
								@SuppressWarnings("unchecked")
								int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
								if (i == 0)
									Settings.inputEncoding = AudioFormat.Encoding.ALAW;
								else if (i == 1)
									Settings.inputEncoding = AudioFormat.Encoding.PCM_FLOAT;
								else if (i == 2)
									Settings.inputEncoding = AudioFormat.Encoding.PCM_SIGNED;
								else if (i == 3)
									Settings.inputEncoding = AudioFormat.Encoding.PCM_UNSIGNED;
								else if (i == 4)
									Settings.inputEncoding = AudioFormat.Encoding.ULAW;

								updateInputLine();
							}

						}
					});
					panelEncoding.add(jccSelectEncoding);

				}
				panelFirstFormats.add(panelEncoding);
				panelFirstFormats.add(Box.createHorizontalGlue());

				JPanel panelSampleRate = new JPanel();
				panelSampleRate.setLayout(new BoxLayout(panelSampleRate, BoxLayout.PAGE_AXIS));
				{
					JLabel lblSampleRate = new JLabel("Wahlen Sie eine Abtastrate:", JLabel.LEFT);
					lblSampleRate.setPreferredSize(new Dimension(160, 25));
					lblSampleRate.setMaximumSize(lblSampleRate.getPreferredSize());
					lblSampleRate.setAlignmentX(Component.CENTER_ALIGNMENT);
					lblSampleRate.setOpaque(false);
					lblSampleRate.setBackground(new Color(0, 0, 0, 0));
					panelSampleRate.add(lblSampleRate);
					panelSampleRate.add(Box.createRigidArea(new Dimension(0, 2)));

					JComboBox<String> jccSampleRate = new JComboBox<>(sampleRate);
					jccSampleRate.setOpaque(false);
					jccSampleRate.setPreferredSize(new Dimension(160, 25));
					jccSampleRate.setMaximumSize(jccSampleRate.getPreferredSize());
					jccSampleRate.setMaximumRowCount(4);
					jccSampleRate.setBackground(new Color(0, 0, 0, 0));
					jccSampleRate.setSelectedIndex(4);
					jccSampleRate.setEditable(false);
					jccSampleRate.setAlignmentX(Component.CENTER_ALIGNMENT);
					jccSampleRate.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							if (e.getStateChange() == ItemEvent.SELECTED) {
								@SuppressWarnings("unchecked")
								int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
								if (i == 0)
									Settings.inputSampleRate = 8000;
								else if (i == 1)
									Settings.inputSampleRate = 11025;
								else if (i == 2)
									Settings.inputSampleRate = 16000;
								else if (i == 3)
									Settings.inputSampleRate = 22050;
								else if (i == 4)
									Settings.inputSampleRate = 44100;
								else if (i == 5)
									Settings.inputSampleRate = 48000;

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
				panelSampleSize.setLayout(new BoxLayout(panelSampleSize, BoxLayout.PAGE_AXIS));
				{
					JLabel lblSampleSize = new JLabel("Wahlen Sie eine Abtasttiefe:", JLabel.LEFT);
					lblSampleSize.setPreferredSize(new Dimension(160, 25));
					lblSampleSize.setMaximumSize(lblSampleSize.getPreferredSize());
					lblSampleSize.setAlignmentX(Component.CENTER_ALIGNMENT);
					lblSampleSize.setOpaque(false);
					lblSampleSize.setBackground(new Color(0, 0, 0, 0));
					panelSampleSize.add(lblSampleSize);
					panelSampleSize.add(Box.createRigidArea(new Dimension(0, 2)));

					JComboBox<String> jccSampleSize = new JComboBox<>(sampleSize);
					jccSampleSize.setOpaque(false);
					jccSampleSize.setPreferredSize(new Dimension(160, 25));
					jccSampleSize.setMaximumSize(jccSampleSize.getPreferredSize());
					jccSampleSize.setBackground(new Color(0, 0, 0, 0));
					jccSampleSize.setSelectedIndex(1);
					jccSampleSize.setEditable(false);
					jccSampleSize.setAlignmentX(Component.CENTER_ALIGNMENT);
					jccSampleSize.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							if (e.getStateChange() == ItemEvent.SELECTED) {
								@SuppressWarnings("unchecked")
								int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
								if (i == 0)
									Settings.inputSampleSize = 8;
								else if (i == 1)
									Settings.inputSampleSize = 16;

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
			panelSecondFormats.setAlignmentX(Component.CENTER_ALIGNMENT);
			{
				panelSecondFormats.add(Box.createHorizontalGlue());

				JPanel panelChannels = new JPanel();
				panelChannels.setLayout(new BoxLayout(panelChannels, BoxLayout.PAGE_AXIS));
				{
					JLabel selectChannels = new JLabel("Wählen Sie die Anzahl der Kanäle:", JLabel.LEFT);
					selectChannels.setPreferredSize(new Dimension(180, 25));
					selectChannels.setMaximumSize(selectChannels.getPreferredSize());
					selectChannels.setAlignmentX(Component.CENTER_ALIGNMENT);
					selectChannels.setOpaque(false);
					selectChannels.setBackground(new Color(0, 0, 0, 0));
					panelChannels.add(selectChannels);
					panelChannels.add(Box.createRigidArea(new Dimension(0, 2)));

					JComboBox<String> jccChannels = new JComboBox<>(channels);
					jccChannels.setOpaque(false);
					jccChannels.setPreferredSize(new Dimension(180, 25));
					jccChannels.setMaximumSize(jccChannels.getPreferredSize());
					jccChannels.setBackground(new Color(0, 0, 0, 0));
					jccChannels.setSelectedIndex(1);
					jccChannels.setEditable(false);
					jccChannels.setAlignmentX(Component.CENTER_ALIGNMENT);
					jccChannels.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							if (e.getStateChange() == ItemEvent.SELECTED) {
								@SuppressWarnings("unchecked")
								int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
								if (i == 0)
									Settings.inputChannels = 1;
								else if (i == 1)
									Settings.inputChannels = 2;
								else if (i == 2)
									Settings.inputChannels = 5;
								else if (i == 3)
									Settings.inputChannels = 7;

								updateInputLine();
							}

						}
					});
					panelChannels.add(jccChannels);
				}
				panelSecondFormats.add(panelChannels);
				panelSecondFormats.add(Box.createRigidArea(new Dimension(20, 0)));

				JPanel panelEndian = new JPanel();
				panelEndian.setLayout(new BoxLayout(panelEndian, BoxLayout.PAGE_AXIS));
				{
					JLabel selectEndian = new JLabel("Wählen sie die Byte-Reihenfolge", JLabel.LEFT);
					selectEndian.setPreferredSize(new Dimension(180, 25));
					selectEndian.setMaximumSize(selectEndian.getPreferredSize());
					selectEndian.setAlignmentX(Component.CENTER_ALIGNMENT);
					selectEndian.setOpaque(false);
					selectEndian.setBackground(new Color(0, 0, 0, 0));
					panelEndian.add(selectEndian);
					panelEndian.add(Box.createHorizontalGlue());

					JComboBox<String> jccEndian = new JComboBox<>(endian);
					jccEndian.setOpaque(false);
					jccEndian.setPreferredSize(new Dimension(180, 25));
					jccEndian.setMaximumSize(jccEndian.getPreferredSize());
					jccEndian.setBackground(new Color(0, 0, 0, 0));
					jccEndian.setSelectedIndex(0);
					jccEndian.setEditable(false);
					jccEndian.setAlignmentX(Component.CENTER_ALIGNMENT);
					jccEndian.addItemListener(new ItemListener() {

						@Override
						public void itemStateChanged(ItemEvent e) {

							if (e.getStateChange() == ItemEvent.SELECTED) {
								@SuppressWarnings("unchecked")
								int i = ((JComboBox<String>) e.getSource()).getSelectedIndex();
								if (i == 0)
									Settings.inputBigEndian = false;
								else if (i == 1)
									Settings.inputBigEndian = true;

								updateInputLine();

							}
						}
					});
					panelEndian.add(jccEndian);
				}
				panelSecondFormats.add(panelEndian);
				panelEndian.add(Box.createHorizontalGlue());
			}

			panelInputFormat.add(Box.createRigidArea(new Dimension(0, 5)));
			panelInputFormat.add(panelSecondFormats);
		}
		add(panelInputFormat);
		add(Box.createRigidArea(new Dimension(0, 10)));

		panelSupported = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(getBackground());
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
					g2.setColor(Color.BLACK);
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
					g2.setColor(Color.BLACK);
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
		// panelSupported.setOpaque(false);
		// panelSupported.setBackground(new Color(0, 0, 0, 0));
		add(panelSupported);
		add(Box.createRigidArea(new Dimension(0, 20)));

		JLabel lblOutput = new JLabel() {
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

				g2.setColor(Color.BLACK);
				g2.drawString(output, xStart, yFontStart);

				g2.setColor(Color.BLUE);
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
			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.drawString("Ausgabegerät", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(Color.BLUE);
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
		panelOutputDevice.setBorder(new EmptyBorder(5, 10, 5, 10));
		{
			JLabel lblSelectOutput = new JLabel("Wählen Sie ein Wiedergabegerät aus", JLabel.LEFT);
			lblSelectOutput.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblSelectOutput.setPreferredSize(new Dimension(500, 25));
			lblSelectOutput.setMaximumSize(lblSelectOutput.getPreferredSize());
			lblSelectOutput.setOpaque(false);
			lblSelectOutput.setBackground(new Color(0, 0, 0, 0));
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
			jccOutputDevices.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					if (e.getStateChange() == ItemEvent.SELECTED) {
						Settings.outputMixerInfo = selectAudioDevices.getOutputDevices()
								.get(jccOutputDevices.getSelectedIndex());
					}

				}
			});

			panelOutputDevice.add(jccOutputDevices);
		}
		add(panelOutputDevice);
		add(Box.createRigidArea(new Dimension(0, 5)));

		JLabel lblOutputVolume = new JLabel() {
			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Arial", Font.BOLD, 14));
				g2.drawString("Wiedergabelautstärke", 2, g2.getFontMetrics(g2.getFont()).getHeight() + 4);
				g2.setColor(Color.BLUE);
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
		panelOutputVolume.setBorder(new EmptyBorder(5, 10, 5, 10));
		{
			JLabel lblSelectGain = new JLabel("Wählen Sie die Wiedergabelautstärke:", JLabel.LEFT);
			lblSelectGain.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblSelectGain.setPreferredSize(new Dimension(500, 25));
			lblSelectGain.setMaximumSize(lblSelectGain.getPreferredSize());
			lblSelectGain.setOpaque(false);
			lblSelectGain.setBackground(new Color(0, 0, 0, 0));
			panelOutputVolume.add(lblSelectGain);
			panelOutputVolume.add(Box.createRigidArea(new Dimension(0, 2)));

			JSlider sliderGain = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
			sliderGain.setMajorTickSpacing(50);
			sliderGain.setMinorTickSpacing(10);
			sliderGain.setPaintTicks(true);
			sliderGain.setPreferredSize(new Dimension(500, 40));
			sliderGain.setMaximumSize(sliderGain.getPreferredSize());
			sliderGain.setAlignmentX(Component.CENTER_ALIGNMENT);
			sliderGain.setOpaque(false);
			sliderGain.setBackground(new Color(0, 0, 0, 0));
			sliderGain.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {

					JSlider source = (JSlider) e.getSource();
					if (!source.getValueIsAdjusting()) {
						Settings.outputVolume = source.getValue() / 100;
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

		LOG.info("Encoding: " + Settings.inputEncoding);
		LOG.info("Sample Rate: " + Settings.inputSampleRate);
		LOG.info("Sample size: " + Settings.inputSampleSize);
		LOG.info("Channels: " + Settings.inputChannels);
		LOG.info("Big Endian: " + Settings.inputBigEndian);

		AudioFormat af = new AudioFormat(Settings.inputEncoding, Settings.inputSampleRate, Settings.inputSampleSize,
				Settings.inputChannels, Settings.frameSize, Settings.inputSampleRate, Settings.inputBigEndian);
		Mixer m = AudioSystem.getMixer(Settings.inputMixerInfo);
		LOG.info("Is Line supported: " + m.isLineSupported(new DataLine.Info(TargetDataLine.class, af)));
		if (m.isLineSupported(new DataLine.Info(TargetDataLine.class, af)))
			lineSupported = true;
		else
			lineSupported = false;

		panelSupported.repaint();
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
