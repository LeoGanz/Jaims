package jaims_development_studio.jaims.client.gui.dfe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class PanelStoreDFEs extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private UUID				uuid;
	private GUIMain				guiMain;

	public PanelStoreDFEs(UUID uuid, GUIMain guiMain) {

		this.uuid = uuid;
		this.guiMain = guiMain;

		initGUI();
	}

	private void initGUI() {

		setMinimumSize(new Dimension(350, 50));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.DARK_GRAY);
		ArrayList<DFEObject> list = guiMain.getDFEObjectsForUser(uuid);
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				if (list.get(i).getDateSent().after(list.get(i - 1).getDateSent())) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
					add(new JLabel(sdf.format(list.get(i).getDateSent()), JLabel.CENTER));
					add(Box.createRigidArea(new Dimension(0, 5)));
				}
			}

			if (list.get(i).getSender().equals(uuid)) {
				PanelDFEObject dfeo = new PanelDFEObject(list.get(i));

				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
				panel.add(Box.createRigidArea(new Dimension(5, 0)));
				panel.add(dfeo);
				panel.add(Box.createHorizontalGlue());
				add(panel);
				add(Box.createRigidArea(new Dimension(0, 5)));
			} else {
				PanelDFEObject dfeo = new PanelDFEObject(list.get(i));

				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
				panel.add(Box.createHorizontalGlue());
				panel.add(dfeo);
				panel.add(Box.createRigidArea(new Dimension(5, 0)));
				add(panel);
				add(Box.createRigidArea(new Dimension(0, 5)));
			}
		}
	}
	
	public void addDFEObject(DFEObject dfeo) {
		if (dfeo.getSender().equals(uuid)) {
			PanelDFEObject pdfeo = new PanelDFEObject(dfeo);

			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			panel.add(Box.createRigidArea(new Dimension(5, 0)));
			panel.add(pdfeo);
			panel.add(Box.createHorizontalGlue());
			add(panel);
			add(Box.createRigidArea(new Dimension(0, 5)));
		} else {
			PanelDFEObject pdfeo = new PanelDFEObject(dfeo);

			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			panel.add(Box.createHorizontalGlue());
			panel.add(pdfeo);
			panel.add(Box.createRigidArea(new Dimension(5, 0)));
			add(panel);
			add(Box.createRigidArea(new Dimension(0, 5)));
		}
		
		repaint();
	}
	
	
	public void setPath(String path, UUID fileID) {
		for (Component c : getComponents()) {
			if (c instanceof PanelDFEObject) {
				if (((PanelDFEObject) c).getFileID().equals(fileID)) 
					((PanelDFEObject) c).setPath(path);
			}
		}
	}

}
