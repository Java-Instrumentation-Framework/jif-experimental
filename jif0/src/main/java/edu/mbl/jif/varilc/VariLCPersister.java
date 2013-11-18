/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.varilc;

import edu.mbl.jif.varilc.camacq.VariLCController;
import edu.mbl.jif.data.DataModel;

import edu.mbl.jif.utils.DialogBox;
import edu.mbl.jif.utils.FileUtil;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.io.xml.ObjectStoreXML;
/**
 *
 * @author GBH
 */
public class VariLCPersister implements VLCPersistence { 
	
	InstrumentController instrumentCtrl;
	private VariLCModel vlcModel;
	private VLCController vlcCtrl;

	public VariLCPersister(InstrumentController instrumentCtrl, VariLCModel vlcModel, VLCController vlcCtrl) {
		this.instrumentCtrl = instrumentCtrl;
		this.vlcModel = vlcModel;
		this.vlcCtrl = vlcCtrl;
	}
	
	// <editor-fold defaultstate="collapsed" desc=" Save/Load Settings ">
	@Override
	public void saveVLCSettings() {
		String filename = chooseFileSave();
		if (filename != null) {
			filename = FileUtil.getJustPath(filename) + "\\" + FileUtil.getJustFilenameNoExt(filename) + ".xml";
			if (new File(filename).exists()) {
				if (!DialogBox.boxConfirm("File Exists", "Over-write file?")) {
					return;
				}
			}
			try {
				VariLCSettings variLCSettings = new VariLCSettings();
				variLCSettings.getCurrent(vlcModel);
				//String path = Files.path(dataDir, "vlcSetting.xml");
				ObjectStoreXML.write(variLCSettings, filename);
			} catch (Throwable ex) {
				Logger.getLogger(VariLCController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public String chooseFileSave() {
		JFileChooser fc = new JFileChooser();
		fc.setName("Save VariLC Settings");
		FileFilter filter = new FileNameExtensionFilter("XML", "xml");
		// Default to Image data dir.
		DataModel data = (DataModel) instrumentCtrl.getModel("data");
		String dataDir = data.getImageDirectory();
		fc.setCurrentDirectory(new File(dataDir));
		fc.setFileFilter(filter);
		int option = fc.showSaveDialog(null);
		if (JFileChooser.APPROVE_OPTION == option) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	@Override
	public void loadVLCSettings() {
		String filename = chooseFileLoad();
		if (filename != null) {
			try {
				VariLCSettings variLCSettings;
				variLCSettings = (VariLCSettings) ObjectStoreXML.read(filename);
				variLCSettings.setCurrent(vlcModel);
				vlcCtrl.transmitAllElements();
			} catch (Throwable ex) {
				Logger.getLogger(VariLCController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public String chooseFileLoad() {
		JFileChooser fc = new JFileChooser();
		fc.setName("Load VariLC Settings");
		FileFilter filter = new FileNameExtensionFilter("XML", "xml");
		// Default to Image data dir.
		DataModel data = (DataModel) instrumentCtrl.getModel("data");
		String dataDir = data.getImageDirectory();
		fc.setCurrentDirectory(new File(dataDir));
		fc.setFileFilter(filter);
		int option = fc.showOpenDialog(null);
		if (JFileChooser.APPROVE_OPTION == option) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
}
