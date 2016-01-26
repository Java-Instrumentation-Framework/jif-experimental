package edu.mbl.jif.gui.file;

/**
 * $ $ License.
 *
 * Copyright $ L2FProd.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import edu.mbl.jif.gui.test.FrameForTest;
import java.io.File;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.l2fprod.common.swing.JDirectoryChooser;
import com.l2fprod.common.swing.PercentLayout;
import edu.mbl.jif.utils.StaticSwingUtils;
import javax.swing.BoxLayout;
import javax.swing.filechooser.FileSystemView;

/**
 * A simple example showing how to use the JDirectoryChooser.
 */
public class DirectorySelector
		extends JPanel {

	public DirectorySelector() {
		setLayout(new PercentLayout(PercentLayout.VERTICAL, 3));
		final JButton button = new JButton("Select Directory");
		add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//selectDirectory(button, "C:\\ImageJ\\", "Select a Dir", "Dir set to:");
				StaticSwingUtils.dispatchToEDT(new Runnable() {
					public void run() {
				selectDirectory(button, "C:\\MicroManagerData\\", "Select a Dir","Confirm");		
					}
				});
			}
		});
            
	}

	public static String selectDirectory(Component parent, String selectedFile,
			String message, String confirmMsg) {

		FileSystemView fsv = FileSystemView.getFileSystemView();
		//fsv.getFiles(null, true)
		JDirectoryChooser chooser = new JDirectoryChooser(new File("C:\\MicroManagerData\\"));
		if (selectedFile != null) {
			File selFile = new File(selectedFile);
			if (selFile.exists()) {
				
				chooser.ensureFileIsVisible(selFile);
				chooser.setSelectedFile(selFile);
			}
		}
		//chooser.setShowingCreateDirectory(true);
		
		JTextArea accessory = new JTextArea(message);
		accessory.setLineWrap(true);
		accessory.setWrapStyleWord(true);
		accessory.setEditable(false);
		accessory.setOpaque(false);
		accessory.setFont(UIManager.getFont("Tree.font"));
		//chooser.setAccessory(accessory);
		chooser.setMultiSelectionEnabled(false); // <<<<<<<<
		int choice = chooser.showOpenDialog(parent);
		if (choice == JDirectoryChooser.APPROVE_OPTION) {
			String filenames = "";
			File[] selectedFiles = chooser.getSelectedFiles();
			for (int i = 0, c = selectedFiles.length; i < c; i++) {
				filenames += selectedFiles[i];
			}
			String filenamesNormalized = filenames.replace('\\', '/');

			if (confirmMsg != null) {
				JOptionPane.showMessageDialog(parent,
						confirmMsg + filenamesNormalized);
			}
			return filenamesNormalized;
		} else {
			if (confirmMsg != null) {
				JOptionPane.showMessageDialog(parent, "Cancelled");
			}
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		//UIManager.setLookAndFeel(
		//UIManager.getCrossPlatformLookAndFeelClassName());
		// UIManager.getSystemLookAndFeelClassName());

		//        if (args.length > 0) {
//            selectDirectory(null, args[0], "", "");
//        }
//        else {
//            selectDirectory(null,
//                  "C:\\PSjData",
//                  "Select Data directory",
//                  "Data dir. set to:");
//        }
//      System.exit(0);
		new FrameForTest(new DirectorySelector());
            

	}

}
