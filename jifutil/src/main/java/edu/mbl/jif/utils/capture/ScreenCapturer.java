package edu.mbl.jif.utils.capture;

// Capture.java
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.imageio.stream.*;
import javax.swing.*;

/**
 * This class defines the application GUI and starts the application.
 */
public class ScreenCapturer extends JFrame {

	/**
	 * Width and height of screen. A single screen is assumed.
	 */
	Dimension dimScreenSize;
	/**
	 * Component for presenting captured image.
	 */
	ImageArea ia = new ImageArea();
	/**
	 * Screen width and height as a Rectangle. This is a convenience for Robot's createScreenCapture() method.
	 */
	Rectangle rectScreenSize;
	/**
	 * Robot is needed to capture screen contents.
	 */
	static Robot robot;
	/**
	 * To support the display of images that can't be fully displayed without scrolling, the ImageArea component is placed
	 * into a JScrollPane.
	 */
	JScrollPane jsp;

	/**
	 * Construct a Capture GUI.
	 *
	 * @param title text appearing in the title bar of Capture's main window
	 */
	public ScreenCapturer(String title) {
		// Place title in the title bar of Capture's main window.
		super(title);

		// Exit the application if user selects Close from system menu.
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Save screen dimensions for initially positioning main window and
		// performing screen captures.
		dimScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Copy screen dimensions to Rectangle for use with Robot's
		// createScreenCapture() method.
		rectScreenSize = new Rectangle(dimScreenSize);

		// Construct a save file chooser. Initialize the starting directory to
		// the current directory, do not allow the user to select the "all files"
		// filter, and restrict the files that can be selected to those ending
		// with .jpg or .jpeg extensions.

		final JFileChooser fcSave = new JFileChooser();
		fcSave.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fcSave.setAcceptAllFileFilterUsed(false);
		fcSave.setFileFilter(new ImageFileFilter());

		// Create the application's menus.
		ActionListener al;
		JMenuBar mb = new JMenuBar();
		JMenu menu = new JMenu("File");
		//
		JMenuItem mi = new JMenuItem("Save As...");
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,	InputEvent.ALT_MASK));
		al = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// Disallow image saving if there is no image to save.
				if (ia.getImage() == null) {
					showError("No captured image.");
					return;
				}
				// Present the "save" file chooser without any file selected.
				// If the user cancels this file chooser, exit this method.
				fcSave.setSelectedFile(null);
				if (fcSave.showSaveDialog(ScreenCapturer.this)
						!= JFileChooser.APPROVE_OPTION) {
					return;
				}
				// Obtain the selected file. Validate its extension, which
				// must be .jpg or .jpeg. If extension not present, append
				// .jpg extension.
				File file = fcSave.getSelectedFile();
				String path = file.getAbsolutePath().toLowerCase();
				if (!path.endsWith(".jpg") && !path.endsWith(".jpeg")) {
					file = new File(path += ".jpg");
				}
				// If the file exists, inform the user, who might not want
				// to accidentally overwrite an existing file. Exit method
				// if the user specifies that it is not okay to overwrite
				// the file.
				if (file.exists()) {
					int choice = JOptionPane.showConfirmDialog(null,
							"Overwrite file?",
							"Capture",
							JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.NO_OPTION) {
						return;
					}
				}
				saveImageFileToDisk(file);
			}
		};
		mi.addActionListener(al);
		menu.add(mi);
		menu.addSeparator();
		//
		mi = new JMenuItem("Sent...");
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,	InputEvent.ALT_MASK));
		al = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// Disallow image saving if there is no image to save.
				if (ia.getImage() == null) {
					showError("No captured image.");
					return;
				}
				// Present the "save" file chooser without any file selected.
				// If the user cancels this file chooser, exit this method.
				fcSave.setSelectedFile(null);
				if (fcSave.showSaveDialog(ScreenCapturer.this)
						!= JFileChooser.APPROVE_OPTION) {
					return;
				}
				// Obtain the selected file. Validate its extension, which
				// must be .jpg or .jpeg. If extension not present, append
				// .jpg extension.
				File file = fcSave.getSelectedFile();
				String path = file.getAbsolutePath().toLowerCase();
				if (!path.endsWith(".jpg") && !path.endsWith(".jpeg")) {
					file = new File(path += ".jpg");
				}
				// If the file exists, inform the user, who might not want
				// to accidentally overwrite an existing file. Exit method
				// if the user specifies that it is not okay to overwrite
				// the file.
				if (file.exists()) {
					int choice = JOptionPane.showConfirmDialog(null,
							"Overwrite file?",
							"Capture",
							JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.NO_OPTION) {
						return;
					}
				}
				saveImageFileToDisk(file);
			}
		};
		mi.addActionListener(al);
		menu.add(mi);

		menu.addSeparator();
		//
		mi = new JMenuItem("Exit");
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.ALT_MASK));
		mi.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});
		menu.add(mi);
		mb.add(menu);
		//
		menu = new JMenu("Capture");
		//
		mi = new JMenuItem("Capture");
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.ALT_MASK));
		al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Hide Capture's main window so that it does not appear in
				// the screen capture.
				setVisible(false);
				// Perform the screen capture.
				BufferedImage biScreen;
				biScreen = robot.createScreenCapture(rectScreenSize);
				// Show Capture's main window for continued user interaction.
				setVisible(true);
				// Update ImageArea component with the new image, and adjust
				// the scrollbars.
				ia.setImage(biScreen);
				jsp.getHorizontalScrollBar().setValue(0);
				jsp.getVerticalScrollBar().setValue(0);
			}

		};

		mi.addActionListener(al);
		menu.add(mi);
		mb.add(menu);
		//
		mi = new JMenuItem("Crop");
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,	InputEvent.ALT_MASK));
		al = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// Crop ImageArea component and adjust the scrollbars if
				// cropping succeeds.

				if (ia.crop()) {
					jsp.getHorizontalScrollBar().setValue(0);
					jsp.getVerticalScrollBar().setValue(0);
				} else {
					showError("Out of bounds.");
				}
			}

		};

		mi.addActionListener(al);
		menu.add(mi);
		mb.add(menu);
		// Install these menus.
		setJMenuBar(mb);
		// Install a scollable ImageArea component.
		getContentPane().add(jsp = new JScrollPane(ia));
		// Size main window to half the screen's size, and center window.
		setSize(dimScreenSize.width / 2, dimScreenSize.height / 2);
		setLocation((dimScreenSize.width - dimScreenSize.width / 2) / 2,
				(dimScreenSize.height - dimScreenSize.height / 2) / 2);
		// Display the GUI and start the event-handling thread.
		setVisible(true);
	}

	
	void saveImageFileToDisk(File file) {
				// If the file does not exist or the user gives permission,
				// save image to file.
				ImageWriter writer = null;
				ImageOutputStream ios = null;
				try {
					// Obtain a writer based on the jpeg format.
					Iterator iter;
					iter = ImageIO.getImageWritersByFormatName("jpeg");
					// Validate existence of writer.
					if (!iter.hasNext()) {
						showError("Unable to save image to jpeg file type.");
						return;
					}
					// Extract writer.
					writer = (ImageWriter) iter.next();
					// Configure writer output destination.
					ios = ImageIO.createImageOutputStream(file);
					writer.setOutput(ios);
					// Set JPEG compression quality to 95%.
					ImageWriteParam iwp = writer.getDefaultWriteParam();
					iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
					iwp.setCompressionQuality(0.95f);
					// Write the image.
					writer.write(null,
							new IIOImage((BufferedImage) ia.getImage(), null, null),
							iwp);
				} catch (IOException e2) {
					showError(e2.getMessage());
				} finally {
					try {
						// Cleanup.

						if (ios != null) {
							ios.flush();
							ios.close();
						}

						if (writer != null) {
							writer.dispose();
						}
					} catch (IOException e2) {
					}
				}
			}
	
	/**
	 * Present an error message via a dialog box.
	 *
	 * @param message the message to be presented
	 */
	public static void showError(String message) {
		JOptionPane.showMessageDialog(null, message, "Capture",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Application entry point.
	 *
	 * @param args array of command-line arguments
	 */
	public static void main(String[] args) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			showError(e.getMessage());
			System.exit(0);
		} catch (SecurityException e) {
			showError("Permission required to use Robot.");
			System.exit(0);
		}

		// Construct the GUI and begin the event-handling thread.
		new ScreenCapturer("Capture");
	}

}
