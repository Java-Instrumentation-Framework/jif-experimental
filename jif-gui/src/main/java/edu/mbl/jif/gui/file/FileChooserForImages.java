/*
 * FileChooserForImages.java
 *
 * Created on December 14, 2006, 4:22 PM
 */
package edu.mbl.jif.gui.file;
/*
 * Definitive Guide to Swing for Java 2, Second Edition By John Zukowski ISBN: 1-893115-78-X Publisher: APress
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

public class FileChooserForImages {

	public static void main(String args[]) {
		JFrame frame = new JFrame("JFileChooser Filter Popup");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();

		final JLabel directoryLabel = new JLabel();
		contentPane.add(directoryLabel, BorderLayout.NORTH);

		final JLabel filenameLabel = new JLabel();
		contentPane.add(filenameLabel, BorderLayout.SOUTH);

		final JButton button = new JButton("Open FileChooser");
		ActionListener actionListener = new ActionListener() {

			public void actionPerformed(ActionEvent actionEvent) {
				// ??
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Throwable t) {
				}
				Component parent = (Component) actionEvent.getSource();
				JFileChooser fileChooser = new JFileChooser(".");
				JComponent preview = new ImagePreviewAccessory(fileChooser);
				fileChooser.setAccessory(preview);
				FileFilter filter1 = new ExtensionFileFilter(null, new String[]{"TIF", "TIFF"});
				//  fileChooser.setFileFilter(filter1);
				fileChooser.addChoosableFileFilter(filter1);  // Note: Choosable
				FileFilter filter2 = new ExtensionFileFilter("gif", new String[]{"gif"});
				fileChooser.addChoosableFileFilter(filter2);
				// example of icon for file type
				fileChooser.setFileView((FileView)new JavaFileView());
				// fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
				int status = fileChooser.showOpenDialog(parent);
				if (status == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					directoryLabel.setText(selectedFile.getParent());
					filenameLabel.setText(selectedFile.getName());
				} else if (status == JFileChooser.CANCEL_OPTION) {
					directoryLabel.setText(" ");
					filenameLabel.setText(" ");
				}
			}

		};
		button.addActionListener(actionListener);
		contentPane.add(button, BorderLayout.CENTER);

		frame.setSize(300, 200);
		frame.setVisible(true);
	}

}

	


