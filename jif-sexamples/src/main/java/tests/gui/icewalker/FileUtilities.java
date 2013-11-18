package tests.gui.icewalker;

import tests.gui.icewalker.windows.WindowsFileSystem;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;
import javax.swing.filechooser.FileSystemView;

public final class FileUtilities extends Object {

	static JFileChooser chooser;
	static char[] illegal = {'|', '\\', '/', '?', ',', '*', '%', '$', '#',
						  '\"', '\'', '^', ':', ';', '=', '�', '@', '~', '<', '>', '(', ')'};
	//static Vector<String[]> imageExt = Arrays.asList( String[] { String {"gif", "jpg", "png", "bmp"} ,"Images"} );

	static final int IMAGES  = 1, FILES = 2, DIRECTORIES = 3;
	static int chooserType = FILES;
	static FileBrowserField sharedFileBrowser = new FileBrowserField();

	static JLabel progressLabel = null;
	static NumberFormat formatter = NumberFormat.getNumberInstance();
	static FileSystemView sysView = FileSystemView.getFileSystemView();
    static WindowsFileSystem fileSystem = new WindowsFileSystem();

	public static int countDirectoryFiles(File srcDir) {
		File[] files = srcDir.listFiles();
		
		// Empty directions return null arrays
		// We return 0 for calculation and item count purposes
		if(files == null) {
			return 0;
		}
		
		int size = files.length;

		for(int i = 0; i < files.length; i++) {
			if(files[i].isDirectory()) {
				size += countDirectoryFiles(files[i]);
			}
		}

		return size;
	}
	
	public static int countDirectoryFiles(File srcDir, final JProgressBar bar) {
		File[] files = srcDir.listFiles();
		int size = files.length;
		
		bar.setMaximum( bar.getMaximum() + size );

		for(int i = 0; i < files.length; i++) {
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					bar.setValue( bar.getValue() + 1 );
				}
			});
			
			if(files[i].isDirectory()) {
				size += countDirectoryFiles(files[i]);
			}
		}

		return size;
	}

	public static long getDirSize(File srcDir) {
		File[] files = srcDir.listFiles();

		long dirSize = 0;

		for(int i = 0; i < files.length; i++) {
			if(files[i].isDirectory()) {
				dirSize += getDirSize(files[i]);
			} else {
				dirSize += files[i].length();
			}
		}

		return dirSize;
	}

	public static double getDirSizeInKB(File srcDir) {
		return round((double)getDirSize(srcDir) / 1024, 2);
	}

	public static double getDirSizeInMB(File srcDir) {
		return round(getDirSizeInKB(srcDir) / 1024, 2);
	}

	public static double getDirSizeInGB(File srcDir) {
		return round( getDirSizeInMB(srcDir) / 1024, 2 );
	}
	
	public static String convertFileLengthToString(long fileSize) {
		if(fileSize < 1024) {
			return  formatter.format(fileSize) + " bytes";
		} else if(fileSize > 1000 && fileSize < 1000000) {
			return  formatter.format(round((fileSize / 1024),2)) + " KB";
		} else if(fileSize > 1000000 && fileSize < 1000000000) {
			return  formatter.format(round((fileSize / Math.pow(1024,2) ), 2)) + " MB";
		}
		
		return  formatter.format(round(Math.pow(1024,3), 2)) + " GB";
	}
	
	public static String getFileSizeAsString(File srcFile) {
		long fileSize = srcFile.length();
		
		return convertFileLengthToString( fileSize );
	}
	
	public static String getFileSizeAsString(File srcFile, String byteSize) {
		if(byteSize.equalsIgnoreCase("kb")) {
			return formatter.format(getFileSizeInKB(srcFile)) + " KB";
	
		} else if(byteSize.equalsIgnoreCase("mb")) {
			return formatter.format(getFileSizeInMB(srcFile)) + " MB";
		
		} else if(byteSize.equalsIgnoreCase("gb")) {
			return formatter.format(getFileSizeInGB(srcFile)) + " GB";
		}
		
		return formatter.format(srcFile.length()) + " bytes";
	}
	
	public static double getFileSizeInKB(File srcFile) {
		return round( (double)srcFile.length() / 1024, 2);
	}
	
	public static double getFileSizeInMB(File srcFile) {
		return round( getFileSizeInKB(srcFile) / 1024, 2 );
	}
	
	public static double getFileSizeInGB(File srcFile) {
		return round( getFileSizeInMB(srcFile) / 1024, 2 );
	}

	public static double round(double val, int places) {
		long factor = (long)Math.pow(10,places);

		val = val * factor;

		long tmp = Math.round(val);

		return (double)tmp / factor;
	}

	public static float round(float val, int places) {
		return (float)round((double)val, places);
	}
	
	public static void openFileLocation(File file) {
		try {
			//Desktop.getDesktop().browse(file.toURI());
			Runtime runtime = Runtime.getRuntime();
				runtime.exec("explorer.exe /select,\"" + file.getAbsolutePath() );
		} catch(IOException ioe) {
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could Not Open File Location: " + file.getAbsolutePath());
		}
	}

	public static void deleteDirectory(File dir) {
		if(dir == null) {
			return;
		}

		deleteFiles(dir.listFiles());
		dir.delete();
	}

	public static void deleteFiles(File[] files) {
		int i = 0;

		while(i < files.length) {

			if(files[i].isDirectory()) {
				deleteFiles( files[i].listFiles() );

				//currentFileName.setText("Removing: " + files[i].getAbsolutePath());
				files[i].delete();

				/*SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						progress.setValue(++uninstall_progress);
					}
				});	*/
			}

			/*currentFileName.setText("Removing: " + files[i].getAbsolutePath());*/
			System.out.println("Deleted: " + files[i].getAbsolutePath() );
			if(!files[i].delete()) {
				files[i].deleteOnExit();
			}

			/*SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					progress.setValue(++uninstall_progress);
				}
			});*/

			i++;
		}
	}

	public static void mkpath(File file) throws IOException {
		ArrayList<File> need = new ArrayList<File>();
		File check = file.getParentFile();
		int max = 0;
		for(; !check.exists(); check = check.getParentFile()) {
			max++;
			need.add(check);
		}

		for(int i = 0; i < max; i++) {
			check = (File)need.get(i);
			check.mkdir();
			need.set(i, null);
		}

		check = null;
		need = null;
	}

	public static boolean validateNameForFile(String name) {
		boolean validated = true;

		for(int i = 0; i < name.length(); i++) {
			for(int j = 0; j < illegal.length; j++) {
				if(name.charAt(i) == illegal[j]) {
					validated = false;
					break;
				}
			}
		}

		return validated;
	}

	/*
	 * Checks supplied file for existing extension and appends supplied extension
	 * if none exists. The boolean parameter forces the method to replace any existing
	 * extension with the supplied extension.
	 */
	public static File appendExtension(File file, String ext, boolean replace) {
		if(file == null) {
			return file;
		}

		File dir = file.getParentFile();
		String name = file.getName();
		String cExt = null;

		if(name.indexOf(".") != -1) {
			cExt = name.substring( name.lastIndexOf(".") );
		}

		if(ext != null && ext.indexOf(".") == -1) {
			ext = "." + ext;
		}

		if(cExt == null) {
			name = name + ext;
		} else if(cExt != null && replace) {
			name = name.substring(0, name.lastIndexOf(".") ) + ext;
		}

		File newFile = new File(dir, name);

		System.out.println( newFile.getAbsolutePath() );

		return newFile;
	}

	public static File appendExtension(File file, String ext) {
		return appendExtension(file, ext, true);
	}
	
	/**
	 * Returns the associated System Icon for the provided File
	 */
	public static Icon getFileIcon(File file) {
		return getFileIcon( getFileExtension(file) );
	}
	
	/**
	 * Returns the associated System Icon for the provided file extension
	 */
	
	public static Icon getFileIcon(String ext) {
        if(ext.contains("htm")) {
            return new ImageIcon("resources/images/htm_16.gif");
        }

        File f = new File(fileSystem.getTempFolder(), "f." + ext);

        if(ext.equalsIgnoreCase("folder")) {
            f = new File(fileSystem.getTempFolder(), "mmptmp");
            f.mkdir();
        } else {
            try {
                f.createNewFile();
            } catch(IOException ioe) {}
        }

        return sysView.getSystemIcon(f);
    }
	
	/**
	 * Returns the extension of the supplied file including the starting '.' 
	 *
	 * @return the extension of the supplied file
	 **/
	public static String getFileExtension(File file) {
		return getFileExtension(file, true);
	}
	
	/**
	 * Returns the extension of the supplied file; If includeDot is set to TRUE 
	 * the starting '.' is also included in the returned String.
	 **/
	public static String getFileExtension(File file, boolean includeDot) {
		if(file.isDirectory()) {
			return "";
		}
		
		int startIndex = file.getName().lastIndexOf(".");
		
		if(!includeDot) {
			startIndex += 1;
		}
		
		return file.getName().substring( startIndex, file.getName().length() );
	}

	/*
	 * Return a valid win32 file name. Should only be used on "filename" not entire
	 * file path due to the inclusion of the "/" character.
	 *
	 * The method eliminates all invalid file name characters as well as directory
	 * creating characters from the supplied filename
	 **/
	public static String getValidFileName(String fileName) {
		if(validateNameForFile(fileName)) {
			return fileName;
		}

		String name = fileName;

		for(int i = 0; i < name.length(); i++) {
			for(int j = 0; j < illegal.length; j++) {
				if(name.charAt(i) == illegal[j]) {
					name = name.substring(0,i) + name.substring(i+1, name.length());
				}
			}
		}

		return name;
	}

	public static String getValidURIFileName(String fileName) {
		fileName = getValidFileName(fileName);

		String[] parts = fileName.split(" ");
		StringBuffer fileBuf = new StringBuffer();
		for(int i = 0; i < parts.length; i++) {
			fileBuf.append(parts[i]);
		}

		return fileBuf.toString();
	}

	public static boolean canReplace(File file) {
		if(file == null || !file.exists()) {
			return true;
		}

		int val = JOptionPane.showConfirmDialog(null, "A file with the name: " + file.getName() +
		 " already exists.\nContinue and Replace Existing File?", "File Exists!!!",
		 JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if(val == JOptionPane.NO_OPTION) {
			return false;
		}

		return true;
	}
	
	public static JFileChooser getSharedFileChooserInstance(int type) {
		return createChooser(type);
	}
	
	public static JFileChooser getSharedFileChooserInstance() {
		if(chooser == null) {
			return createChooser(FILES);
		}
		
		return chooser;
	}

	public static JFileChooser createChooser(int type) {
		createChooser(null, null, null);

		if(chooserType == IMAGES && type != IMAGES) {
			chooser.setAccessory(null);
			chooser.resetChoosableFileFilters();
		}

		if(type == IMAGES && chooserType != IMAGES) {
			// TODO chooser.setAccessory( new FilePreviewer(chooser) );
			// TODO chooser.addChoosableFileFilter( new ExampleFileFilter( new String[] {"gif", "jpg", "png", "bmp"}, "Images" ) );
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setApproveButtonText("Select");
		} else if(type == DIRECTORIES && chooserType != DIRECTORIES) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}

		chooserType = type;

		return chooser;
	}

	public static JFileChooser createChooser(FileView fileView, Vector<String[]> extensions, String dialogTitle) {
		if(chooser == null) {
			chooser = new JFileChooser() {
				public Icon getIcon(File file) {
					return FileSystemView.getFileSystemView().getSystemIcon(file);
				}
			};

			chooser.setDialogTitle( dialogTitle == null ? "" : dialogTitle );

			if(fileView != null) {
				chooser.setFileView(fileView);
			}

			if(extensions != null) {
				for(int i = 0; i < extensions.size(); i++) {
					String[] ext = extensions.elementAt(i);
					// TODO chooser.addChoosableFileFilter( new ExampleFileFilter(ext[0], ext[1]) );
				}
			}
		}

		return chooser;
	}

	public static String browseForDirectory() {
		return browseForDirectory( new WindowsFileSystem().getCurrentUserDesktop() );
	}

	public static String browseForDirectory(String currentDir) {
		String dir = currentDir;

		createChooser(DIRECTORIES);
		chooser.setCurrentDirectory(new File(dir));

		int val = chooser.showDialog(null,"Select");
		if(val == JFileChooser.APPROVE_OPTION) {
			dir = chooser.getSelectedFile().getAbsolutePath();
		}

		return dir;
	}

	public static File browseForImage() {
		createChooser(IMAGES);

		File image = null;

		int val = chooser.showDialog(null,"Select Image");

		if(val == JFileChooser.APPROVE_OPTION) {
			image = chooser.getSelectedFile();
		}

		return image;
	}

	public static File browseForFile(String currentDir, String dialogTitle,
					Vector<String[]> extensions, FileView fileView) {

		File file = null;

		createChooser(fileView,extensions,dialogTitle);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int val = chooser.showDialog(null,"Select");

		if(val == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
		}

		return file;
	}

	public static String browseForFile() {
		return browseForFile(new WindowsFileSystem().getCurrentUserDesktop(), "Select File", null, null).getAbsolutePath();
	}


	public static FileBrowserField getFileBrowserField() {
		return new FileBrowserField();
	}

	public static File copyFile(File file, File dir) {
		return copyFile(file.getAbsolutePath(), dir.getAbsolutePath(), null);
	}

	public static File copyFile(File file, File dir, String renameTo) {
		return copyFile(file.getAbsolutePath(), dir.getAbsolutePath(), renameTo);
	}

	public static File copyFile(String fileName, String dirLocation) {
		return copyFile(fileName, dirLocation, null);
	}

	public static File copyFile(String fileName, String dirLocation, String renameTo) {
		if(progressLabel != null) {
			progressLabel.setText("Copying....");
		}

		File dir = new File(dirLocation);
		
		try {
			mkpath(dir);
		} catch(Exception e) {}
		
		if(!dir.exists()) {
			dir.mkdir();
		}

		File fileToCopy = new File(fileName);

		if(fileToCopy.isDirectory()) {
			copyDirectory(fileToCopy, dirLocation);
			return new File(dirLocation);
		}

		File fileToWrite = new File(dirLocation, fileToCopy.getName() );

		if(renameTo != null) {
			fileToWrite = new File(dirLocation, renameTo );
		}

		try {
			BufferedInputStream reader =  new BufferedInputStream(new FileInputStream(fileToCopy), 4096);
			BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(fileToWrite), 4096);

			int charRead = -1;
			while( (charRead = reader.read()) != -1) {
				writer.write(charRead);
			}

			reader.close();
			writer.flush();
			writer.close();

			reader = null;
			writer = null;
		} catch(IOException ioe) {
			System.out.println(ioe);
			ioe.printStackTrace();

			return (File)null;
		}

		return (File)fileToWrite;

	}

	public static boolean copyDirectory(String fileName, String dirLocation) {
		return copyDirectory( new File(fileName), new File(dirLocation) );
	}

	public static boolean copyDirectory(File dir, File targetDir) {
		return copyDirectory(dir, targetDir.getAbsolutePath() );
	}

	public static boolean copyDirectory(File dir, String dirLocation) {
		if(progressLabel != null) {
			progressLabel.setText("Copying....");
		}

		File[] files = dir.listFiles();

		for(int i = 0; i < files.length; i++) {
			String parentString = files[i].getParent();
			String parent = parentString.substring(parentString.lastIndexOf("\\"), parentString.length() );

			System.out.println("Copying: " + files[i].getAbsolutePath() +
					", To: " + dirLocation + parent );

			File newDirectory = new File(dirLocation + parent);
			if(!newDirectory.exists()) {
				newDirectory.mkdir();
			}

			if(files[i].isDirectory()) {
				copyDirectory(files[i], newDirectory.getAbsolutePath() );
				continue;
			}

			if(!files[i].getName().equalsIgnoreCase("Thumbs.db")) {
				copyFile( files[i].getAbsolutePath(), newDirectory.getAbsolutePath() );
			}
		}

		return true;

		//JOptionPane.showMessageDialog(this, "File Copy Completed Successfully","Success!!!!",
		//		JOptionPane.INFORMATION_MESSAGE);


		//progressLabel.setText("File Copy Complete");
	}
	
	public static String getRelativePath(File file) {
		return getRelativePath( file.getAbsolutePath() );
	}
	
	public static String getRelativePath(String filename) {
        // null begets null - something like the commutative property
        if (null == filename)
            return null;

        // Don't translate a string that already looks like a URL
        if (isCommonURL(filename))
            return filename;

        String base = null;
        try {
            File userdir = new File(System.getProperty("user.dir"));
           
            // Note: use CanonicalPath, since this ensures casing
            //  will be identical between the two files
            base = userdir.getCanonicalPath();
        } catch (Exception e) {        	
            // If we can't detect this, we can't determine 
            //  relativeness, so just return the name
            return filename;
        }
        
        File f = new File(filename);
        String tmp = null;
        
        try {
            tmp = f.getCanonicalPath();
        } catch (IOException ioe) {
            tmp = f.getAbsolutePath();
        }

        // If it's not relative to the base, just return as-is
        //  (note: this may not be the answer you expect)
        if (!tmp.startsWith(base))
            return tmp;

        // Strip off the base
        tmp = tmp.substring(base.length());
        
        // Also strip off any beginning file separator, since we 
        //  don't want it to be mistaken for an absolute path
        if (tmp.startsWith(File.separator))
            return tmp.substring(1);
        else
            return tmp;
    }


    /**
     * Worker method to detect common absolute URLs.  
     * 
     * @param s String path\filename or URL (or any, really)
     * @return true if s starts with a common URI scheme (namely 
     * the ones found in the examples of RFC2396); false otherwise
     */
    protected static boolean isCommonURL(String s) {
        if (null == s)
            return false;
            
        if (s.startsWith("file:")
            || s.startsWith("http:")
            || s.startsWith("ftp:")
            || s.startsWith("gopher:")
            || s.startsWith("mailto:")
            || s.startsWith("news:")
            || s.startsWith("telnet:")
        )
            return true;
        else
            return false;
    }           

	public static FileBrowserField getFileBrowserField(String initFile) {
		return new FileBrowserField(initFile);
	}

	public static FileBrowserField getSharedFileBrowserField() {
		return sharedFileBrowser;
	}

	public static class FileBrowserField extends JPanel {

		private JTextField field;
		private JButton browse;
		private boolean selectDirectories = false;

		public FileBrowserField() {
			super( new BorderLayout() );
			createUI();
		}

		public FileBrowserField(String initFile) {
			this();
			field.setText(initFile);
		}

		public void createUI() {
			field = new JTextField(25);
			Insets insets = field.getInsets();
			field.setBorder( BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right) );

			browse = new JButton("...");
			browse.setMargin( new Insets(0,1,0,1) );
			browse.setDefaultCapable(false);
			browse.setFocusPainted(false);
			browse.setFocusable(false);
			browse.setPreferredSize( new Dimension( browse.getPreferredSize().width, 0 ) );
			browse.setToolTipText("Browse...");

			browse.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String path = null;
					
					if(selectDirectories) {
						path = browseForDirectory();
					} else {
						path = browseForFile();
					}
					 

					if(path != null) {
						field.setText(path);
					}
				}
			});

			setBorder( BorderFactory.createLoweredBevelBorder() );
			add(field);
			add(browse, BorderLayout.EAST);
		}

		public String getAbsolutePath() {
			return field.getText();
		}

		public File getSelectedFile() {
			return new File( getAbsolutePath() );
		}

		public void setSelectedFile(File file) {
			field.setText( file.getAbsolutePath() );
		}
		
		public void setSelectDirectories(boolean b) {
			selectDirectories = b;
		}
 	}
 	
 	
}