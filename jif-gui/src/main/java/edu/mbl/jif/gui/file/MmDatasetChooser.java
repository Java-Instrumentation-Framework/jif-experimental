/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.gui.file;

import java.awt.Component;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author GBH
 */
public class MmDatasetChooser {
	
	
	public void chooseFromProject(String project, Component parent) {
		
	}
	public void chooseFromSession(String session, Component parent) {
		
	}
	
	public void choose(Component parent) {
		JFileChooser fileChooser = new JFileChooser(".");
				JComponent preview = new ImagePreviewAccessory(fileChooser);
				fileChooser.setAccessory(preview);
				FileFilter filter1 = new ExtensionFileFilter(null, new String[]{"JPG", "JPEG"});
				//  fileChooser.setFileFilter(filter1);
				fileChooser.addChoosableFileFilter(filter1);  // Note: Choosable
				FileFilter filter2 = new ExtensionFileFilter("gif", new String[]{"gif"});
				fileChooser.addChoosableFileFilter(filter2);
				// example of icon for file type
				// fileChooser.setFileView((FileView)new JavaFileView());
				// fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
				int status = fileChooser.showOpenDialog(parent);
				if (status == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
//					directoryLabel.setText(selectedFile.getParent());
//					filenameLabel.setText(selectedFile.getName());
				} else if (status == JFileChooser.CANCEL_OPTION) {
//					directoryLabel.setText(" ");
//					filenameLabel.setText(" ");
				}
	}

	
}
