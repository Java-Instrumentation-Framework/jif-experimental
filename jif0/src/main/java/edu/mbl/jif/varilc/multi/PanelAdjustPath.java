package edu.mbl.jif.varilc.multi;

import edu.mbl.jif.varilc.multi.PathVLC;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

/**
 * <p>Title: </p>
 *
 * <p>Description: Panel for adjusting and calibrating a VLC path </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PanelAdjustPath
		extends JPanel {

	PathVLC path;

	public PanelAdjustPath() {
		this(new PathVLC());
	}

	public PanelAdjustPath(PathVLC path) {
		this.path = path;
		try {
			jbInit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
		ButtonGroup group = new ButtonGroup();
		int settings = path.numSettings;
		//int retarders = path.getNumRetarders();
		for (int s = 0; s < settings; s++) {
			PanelSettingAdjust psa = new PanelSettingAdjust(path, s, group);
			group.add(psa.getSelectButton());
			this.add(psa);
		}
	}

}
