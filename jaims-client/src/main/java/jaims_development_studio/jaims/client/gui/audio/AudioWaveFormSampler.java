package jaims_development_studio.jaims.client.gui.audio;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates a wave form graph for a given audio file. This happens by
 * first deciding based on the sample size in bits of the audio how to treat the
 * file. It then converts the audio file to values representing the sample size
 * in bits. For 8 bit audio no conversion happens and the file is read into a
 * byte array. For 16 bit audio two bytes of the file are taken and converted
 * into a short; this happens until there are no more bytes left to read. For 24
 * and 32 bit audio three and four bytes respectively are converted into an
 * integer. After the conversion is done it splits the array into multiple
 * smaller arrays, one for each channel. Finally it iterates through those
 * channel arrays with a given step and draws a line between two points,
 * consisting of the iteration value as the x coordinate and the value of the
 * channel array at the iteration value's position as the y coordinate.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class AudioWaveFormSampler {

	private static final Logger	LOG				= LoggerFactory.getLogger(AudioWaveFormSampler.class);

	final int					IMAGE_HEIGHT	= 100;
	final int					Y_CENTERING		= IMAGE_HEIGHT / 2;
	final int					STRETCH_FACTOR	= IMAGE_HEIGHT - Y_CENTERING - 35;
	private float				SAMPLE_FREQUENCY;
	private float				FRAME_SIZE;
	private double				pixelspersecond;

	public BufferedImage createWaveFile(File f) {

		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
			final float SAMPLE_SIZE_IN_BITS = audioInputStream.getFormat().getSampleSizeInBits();
			SAMPLE_FREQUENCY = audioInputStream.getFormat().getSampleRate();
			FRAME_SIZE = audioInputStream.getFormat().getFrameSize();

			BufferedImage bim;
			if (SAMPLE_SIZE_IN_BITS == 8.0)
				bim = createWith8Bit(f);
			else if (SAMPLE_SIZE_IN_BITS == 16.0)
				bim = createWith16Bit(f);
			else if (SAMPLE_SIZE_IN_BITS == 24.0)
				bim = createWith24Bit(f);
			else if (SAMPLE_SIZE_IN_BITS == 32.0)
				bim = createWith32Bit(f);
			else
				bim = createWith16Bit(f);

			return bim;

		} catch (FileNotFoundException e) {
			LOG.error("Audio file could not be found", e);
		} catch (IOException e) {
			LOG.error("Error while trrying to read audio file!", e);
		} catch (UnsupportedAudioFileException e) {
			LOG.error("Audio file is not supported!", e);
		}

		BufferedImage bim = new BufferedImage(200, 400, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bim.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, bim.getWidth(), bim.getHeight());
		return bim;
	}

	private BufferedImage createWith8Bit(File f) throws IOException {

		byte[] fileInBytes = IOUtils.toByteArray(new FileInputStream(f));
		byte[] bytesTo8Bit = Arrays.copyOfRange(fileInBytes, 45, fileInBytes.length - 1);

		byte greatestValue = 0;
		for (byte i : bytesTo8Bit) {
			if (i > greatestValue) {
				greatestValue = i;
			}
		}

		double[] left = new double[bytesTo8Bit.length / 2];
		double[] right = new double[left.length];

		int arrayPosition = 0;
		for (int i = 0; i < bytesTo8Bit.length - 2; i += 2) {
			left[arrayPosition] = (double) bytesTo8Bit[i] / greatestValue;
			right[arrayPosition] = (double) bytesTo8Bit[i + 1] / greatestValue;

			if (arrayPosition < left.length) {
				arrayPosition++;
			}
		}

		int imageWidth;
		if ((left.length / 1000) < 200)
			imageWidth = 200;
		else if ((left.length / 1000) > 44100)
			imageWidth = 44100;
		else
			imageWidth = left.length / 1000;

		double xStep = imageWidth / (double) left.length;
		final int INCREASE_STEP = (left.length / imageWidth) * 2;

		BufferedImage bim = new BufferedImage(imageWidth, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) bim.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, bim.getWidth(), bim.getHeight());
		g2.setColor(Color.CYAN);
		g2.setStroke(new BasicStroke(2.0F));
		g2.drawLine(1, 0, 1, IMAGE_HEIGHT);
		g2.drawLine(0, Y_CENTERING, imageWidth, Y_CENTERING);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(0.4F));

		for (int i = 0; i < left.length; i += INCREASE_STEP) {
			if (i > 0) {
				g2.draw(new Line2D.Double((i - INCREASE_STEP) * xStep,
						Y_CENTERING + (left[i - INCREASE_STEP] * STRETCH_FACTOR), i * xStep,
						Y_CENTERING + (left[i] * STRETCH_FACTOR)));
			}
		}

		return bim;
	}

	private BufferedImage createWith16Bit(File f) throws FileNotFoundException, IOException {

		byte[] fileInBytes = IOUtils.toByteArray(new FileInputStream(f));
		fileInBytes = Arrays.copyOfRange(fileInBytes, 45, fileInBytes.length - 1);
		float floatsPerSecondPerChannel = (SAMPLE_FREQUENCY * FRAME_SIZE) / 4;

		short[] bytesTo16Bit = new short[(fileInBytes.length - 44) / 2];

		DataInputStream d = new DataInputStream(new ByteArrayInputStream(fileInBytes));
		for (int i = 0; i < bytesTo16Bit.length; i++) {
			bytesTo16Bit[i] = d.readShort();
		}

		short greatestValue = 0;
		for (short i : bytesTo16Bit) {
			if (i > greatestValue) {
				greatestValue = i;
			}
		}

		double[] left = new double[bytesTo16Bit.length / 2];
		double[] right = new double[left.length];

		int arrayPosition = 0;
		for (int i = 0; i < bytesTo16Bit.length - 2; i += 2) {
			left[arrayPosition] = (double) bytesTo16Bit[i] / greatestValue;
			right[arrayPosition] = (double) bytesTo16Bit[i + 1] / greatestValue;

			if (arrayPosition < left.length) {
				arrayPosition++;
			}
		}

		int imageWidth;
		if ((left.length / 1000) < 200)
			imageWidth = 200;
		else if ((left.length / 1000) > 44100)
			imageWidth = 44100;
		else
			imageWidth = left.length / 1000;

		double xStep = imageWidth / (double) left.length;
		final int INCREASE_STEP = (left.length / imageWidth) * 2;
		pixelspersecond = floatsPerSecondPerChannel * xStep;

		BufferedImage bim = new BufferedImage(imageWidth, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) bim.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, bim.getWidth(), bim.getHeight());
		g2.setColor(Color.CYAN);
		g2.setStroke(new BasicStroke(2.0F));
		g2.drawLine(1, 0, 1, IMAGE_HEIGHT);
		g2.drawLine(0, Y_CENTERING, imageWidth, Y_CENTERING);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(0.4F));

		for (int i = 0; i < left.length; i += INCREASE_STEP) {
			if (i > 0) {
				g2.draw(new Line2D.Double((i - INCREASE_STEP) * xStep,
						Y_CENTERING + (left[i - INCREASE_STEP] * STRETCH_FACTOR), i * xStep,
						Y_CENTERING + (left[i] * STRETCH_FACTOR)));
			}
		}

		return bim;
	}

	private BufferedImage createWith24Bit(File f) throws FileNotFoundException, IOException {

		byte[] fileInBytes = IOUtils.toByteArray(new FileInputStream(f));
		fileInBytes = Arrays.copyOfRange(fileInBytes, 45, fileInBytes.length - 1);
		int[] bytesTo24Bit = new int[(fileInBytes.length - 44) / 3];

		for (int i = 0; i < bytesTo24Bit.length; i += 3) {
			bytesTo24Bit[i] = bytesToSInt(fileInBytes[i], fileInBytes[i + 1], fileInBytes[i + 2]);
		}

		int greatestValue = 0;
		for (int i : bytesTo24Bit) {
			if (i > greatestValue) {
				greatestValue = i;
			}
		}

		double[] left = new double[bytesTo24Bit.length / 2];
		double[] right = new double[left.length];

		int arrayPosition = 0;
		for (int i = 0; i < bytesTo24Bit.length - 2; i += 2) {
			left[arrayPosition] = (double) bytesTo24Bit[i] / greatestValue;
			right[arrayPosition] = (double) bytesTo24Bit[i + 1] / greatestValue;

			if (arrayPosition < left.length) {
				arrayPosition++;
			}
		}

		int imageWidth;
		if ((left.length / 1000) < 200)
			imageWidth = 200;
		else if ((left.length / 1000) > 44100)
			imageWidth = 44100;
		else
			imageWidth = left.length / 1000;

		double xStep = imageWidth / (double) left.length;
		final int INCREASE_STEP = (left.length / imageWidth) * 2;

		BufferedImage bim = new BufferedImage(imageWidth, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) bim.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, bim.getWidth(), bim.getHeight());
		g2.setColor(Color.CYAN);
		g2.setStroke(new BasicStroke(2.0F));
		g2.drawLine(1, 0, 1, IMAGE_HEIGHT);
		g2.drawLine(0, Y_CENTERING, imageWidth, Y_CENTERING);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(0.4F));

		for (int i = 0; i < left.length; i += INCREASE_STEP) {
			if (i > 0) {
				g2.draw(new Line2D.Double((i - INCREASE_STEP) * xStep,
						Y_CENTERING + (left[i - INCREASE_STEP] * STRETCH_FACTOR), i * xStep,
						Y_CENTERING + (left[i] * STRETCH_FACTOR)));
			}
		}

		return bim;

	}

	private BufferedImage createWith32Bit(File f) throws FileNotFoundException, IOException {

		// Reading file into byte array
		byte[] fileInBytes = IOUtils.toByteArray(new FileInputStream(f));
		// Remove header bytes of wav file in positions 0 to 44
		fileInBytes = Arrays.copyOfRange(fileInBytes, 45, fileInBytes.length - 1);
		// Create int array to hold 32bit integer values
		int[] bytesTo32Bit = new int[(fileInBytes.length - 44) / 4];

		// Use DataInputStream to convert for bytes into 32bit integer
		DataInputStream d = new DataInputStream(new ByteArrayInputStream(fileInBytes));
		for (int i = 0; i < bytesTo32Bit.length; i++) {
			bytesTo32Bit[i] = d.readInt();
		}

		// Get the greatest value in the integer array
		int greatestValue = 0;
		for (int i : bytesTo32Bit) {
			if (i > greatestValue) {
				greatestValue = i;
			}
		}

		// create arrays with double precision for channels left and right
		double[] left = new double[bytesTo32Bit.length / 2];
		double[] right = new double[left.length];

		// sort integer values into left and right arrays and at the same time divide
		// value at current position by the greatest value to get a range between 0 and
		// 1
		int arrayPosition = 0;
		for (int i = 0; i < bytesTo32Bit.length - 2; i += 2) {
			left[arrayPosition] = (double) bytesTo32Bit[i] / greatestValue;
			right[arrayPosition] = (double) bytesTo32Bit[i + 1] / greatestValue;

			if (arrayPosition < left.length) {
				arrayPosition++;
			}
		}

		// Calculate the images width
		int imageWidth;
		if ((left.length / 1000) < 200)
			imageWidth = 200;
		else if ((left.length / 1000) > 44100)
			imageWidth = 44100;
		else
			imageWidth = left.length / 1000;

		double xStep = imageWidth / (double) left.length;
		final int INCREASE_STEP = (left.length / imageWidth) * 2;

		// Create buffered image
		BufferedImage bim = new BufferedImage(imageWidth, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) bim.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, bim.getWidth(), bim.getHeight());
		g2.setColor(Color.CYAN);
		g2.setStroke(new BasicStroke(2.0F));
		g2.drawLine(1, 0, 1, IMAGE_HEIGHT);
		g2.drawLine(0, Y_CENTERING, imageWidth, Y_CENTERING);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(0.4F));

		// draw lines
		for (int i = 0; i < left.length; i += INCREASE_STEP) {
			if (i > 0) {
				g2.draw(new Line2D.Double((i - INCREASE_STEP) * xStep,
						Y_CENTERING + (left[i - INCREASE_STEP] * STRETCH_FACTOR), i * xStep,
						Y_CENTERING + (left[i] * STRETCH_FACTOR)));
			}
		}

		return bim;

	}

	private int bytesToSInt(byte one, byte two, byte three) {

		return (three) << 16 | (two & 0xFF) << 8 | (one & 0xFF);
	}

	public double getPixelsPerSecond() {

		return pixelspersecond;
	}

}
