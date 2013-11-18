package tests.gui.icewalker;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import javax.swing.*;

/* Static Utilities class for creating Zip/Jar Archives. 
 *
 * Users of this class should call only the public static methods with arguments. The 
 * other methods are only for testing purposes.
 *
 * The class has the ability to accept an optional Progress Bar and a JLabel 
 * for use during the archive creation and extraction processes for a better 
 * user experience during these process. The progress bar will be updated with 
 * the current extraction status and the label with the current file name
 *
 **/

public final class JarUtilities extends Object {
	
	static int i = 0;
	static javax.swing.Timer timer = null;
	static BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
	static boolean pauseExtraction = false;
	
	protected static void main(String[] arg) {
		showMenu();
	}
	
	protected static void showMenu() {
		
		System.out.println("\nJar Utilities Tester\n------------------------------------");
		System.out.println("Enter \'1\' to create a JAR file");
		System.out.println("Enter \'2\' to extract a JAR file");
		System.out.println("Enter \'3\' to exit");
		
		System.out.print("\nEnter Choice: ");
		
		try {
			String num = reader.readLine();
			int val = Integer.parseInt(num);
			
			switch(val) {
				case 1: 
					createJar(); 					
					break;
				case 2: 
					extractJar(); 					
					break;
				case 3: 
					System.out.println("\nThanks for using Jar Utilities");
					System.exit(0);
				default: 
					System.out.println("-----------------------------------------");
			}			
			
		} catch(NumberFormatException e) {
			System.out.println("Invalid Number\n");
		} catch(IOException ioe) {
			System.out.println("Invalid Procedure\n");
		}
	}
	
	protected static void extractJar() {
		System.out.println("Preparing To Extract Jar....\n");
		
		// TODO ExampleFileFilter jarFilter = new ExampleFileFilter("jar", "JAR File (Java Archieve)");
		File jarFile = null, tarFolder = null;
		JFileChooser chooser = new JFileChooser();
			// TODO chooser.addChoosableFileFilter(jarFilter);
			chooser.setDialogTitle("Select Jar File");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
		
		int val = chooser.showDialog(null, "Select");
		
		if(val == JFileChooser.APPROVE_OPTION) {
			jarFile = chooser.getSelectedFile();
		}
		
		if(jarFile == null) {
			System.out.println("Exiting. No JAR File selected");
			return;
		}
		
		// TODO chooser.removeChoosableFileFilter(jarFilter);
		chooser.setDialogTitle("Select Target Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);
		
		val = chooser.showDialog(null, "Select");
		
		if(val == JFileChooser.APPROVE_OPTION) {
			tarFolder = chooser.getSelectedFile();
		}
		
		if(tarFolder == null) {
			System.out.println("No Directory Selected");
			return;
		}
		
		JarUtilities.extractJar(jarFile, tarFolder);
		
		showMenu();
	}
	
	protected static void createJar() {
		System.out.println("Preparing To Create...");
		
		JFileChooser chooser = new JFileChooser();
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		int val = chooser.showDialog(null, "Select");
		
		if(val == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();			
			
			File target = new File("dbs/test.jar");
			Manifest mf = null;
			try {
				mf = new Manifest( new FileInputStream( new File("manifest.mf") ) );
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
			
			
			if(files.length == 1 && files[0].isDirectory()) {
				JarUtilities.createJar(target, files[0], mf, true, JarEntry.DEFLATED);
			} else {
				JarUtilities.createJar(target, files, mf, JarEntry.DEFLATED);
			}
		}
		
		showMenu();
	}
	
	
	static FileInputStream istream;
	static FileOutputStream stream;
	static JarInputStream input; 
	static JarOutputStream output;
	private static JProgressBar progress;
	private static int updateAmount = 1;
	private static Thread jarrer = null;
	public static JLabel currentFileName;
	public static String label_prefix = "Current File: ";
	public static int DEFLATED = JarEntry.DEFLATED;
	public static int STORED = JarEntry.STORED;
	
	public static File jarTarget, extractTarget;
	
	protected JarUtilities() {}
	
	/*
	 * Static Utility method to create a Zip/Jar Archive by supplying the target file name, the source
	 * directory, an optional manifest for Jar archives.
	 *
	 * The boolean parameters determine whether to
	 * 1. Include the specifies directory in the archive or only its contents
	 * 2. Compress the archive or just store the files without compression
	 * */
	public static void createJar(File target, File dir, Manifest mf, 
			boolean includeDir, int storeType) {
		
		jarTarget = target;
		
		try {
			stream = new FileOutputStream(target);
			
			if(mf != null) {
				output = new JarOutputStream(stream, mf);
			} else {
				output = new JarOutputStream(stream);
			}
			
			output.setMethod( storeType );
			
			jarDirectory(null, dir, output, storeType, includeDir);
			
			output.flush();
			output.finish();
			output.close();

		} catch(IOException ioe) {
			System.out.println(ioe);
			ioe.printStackTrace();
			
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}
	
	/*
	 * Static Utility method to create a Zip/Jar Archive by supplying the target file name 
	 * and the source directory
	 *
	 * The boolean parameters determine whether to
	 *  1. Include the specifies directory in the archive or only its contents
	 *  2. Compress the archive or just store the files without compression
	 * */
	
	public static void createJar(File target, File dir, boolean includeDir, int storeType) {
		createJar(target, dir, null, includeDir, storeType);
	}
	
	/*
	 * Static Utility method to create a Zip/Jar Archive by supplying the target file name 
	 * and the source files as an array
	 *
	 * The boolean parameter determine whether or not to Compress the archive or 
	 * just store the files without compression
	 * */
	 
	public static void createJar(File target, File[] files, int storeType) {
		createJar(target, files, null, storeType);
	}
	
	
	/*
	 * Static Utility method to create a Zip/Jar Archive by supplying the target file name, 
	 * the source files as an Vector<File> and an optional manifest for Jar archives.
	 *
	 * The boolean parameter determine whether or not to Compress the archive or 
	 * just store the files without compression
	 * */
	public static void createJar(File target, Vector<File> files, Manifest mf, int storeType) {
		createJar(target, files.toArray(new File[files.size()]), mf, storeType);
	}
	
	/*
	 * Static Utility method to create a Zip/Jar Archive by supplying the target file name 
	 * and the source files as an array and an optional manifest for Jar archives.
	 *
	 * The boolean parameter determine whether or not to Compress the archive or 
	 * just store the files without compression
	 * */
	
	public static void createJar(File target, final File[] files, Manifest mf, final int storeType) {
		jarTarget = target;
		
		try {
			stream = new FileOutputStream(target);
			
			if(mf != null) {
				output = new JarOutputStream(stream, mf);
			} else {
				output = new JarOutputStream(stream);
			}
			
			output.setMethod( storeType );
			
			i = 0;

			for(i = 0; i < files.length; i++) {				
				if(files[i].isDirectory()) {
					jarDirectory(null, files[i], output, storeType, true);
				} else {
					jarFile(files[i], output, storeType);
				}
				
				for(int x = 0; x < 25; x++) {}
				
				System.gc();				
			}			
			
			try {
				updateLabelText(" ");
				updateProgress();
				output.flush();
				output.finish();
				output.close();							
			} catch(IOException ioe) {
				ioe.printStackTrace();
				timer = null;
			}
		} catch(IOException ioe) {
			System.out.println(ioe);
			ioe.printStackTrace();
			timer = null;
		} catch(Exception e) {
			e.printStackTrace();
			timer = null;
		}
		
		/* blocks until timer has finished working */
		//while(timer != null) {}
	}
	
	private static void jarDirectory(String parentDirName, File dir, JarOutputStream output,
				int storeType, boolean includeDir) {
		
		if(!dir.isDirectory()) {
			System.out.println(dir.getName() + " is not a directory");
			return;
		}
		
				
		String dirName = dir.getName();
		
		if(parentDirName == null) {
			parentDirName = dirName;
		} else {
			parentDirName = parentDirName + "/" + dirName;
		}
		
		File[] files = dir.listFiles();
		
		for(int i = 0; i < files.length; i++) {
			if(files[i].isDirectory()) {
				jarDirectory(parentDirName, files[i], output, storeType, true);
			}
			
			if(includeDir) {
				jarFile(parentDirName + "/" + files[i].getName(), files[i], output, storeType );
			} else {
				jarFile(null, files[i], output, storeType);
			}
			
			System.gc();
		}
	}
	
	private static void jarFile(File file, JarOutputStream output, int storeType) {
		jarFile(null, file, output, storeType);			
	}
	
	private static void jarFile(String entryName, File file, JarOutputStream output, int storeType) {
		if(entryName == null) {
			entryName = file.getName();
		}
		
		try {
			System.out.println("Adding: " + file.getAbsolutePath() );
			updateLabelText(file.getName());
			updateProgress();
			
			byte[] bytes = new byte[ new Long(file.length()).intValue() ];
			BufferedInputStream input = new  BufferedInputStream(new FileInputStream(file), 8192);
				//input.read(bytes);
				//input.close();
				
			CRC32 crc = new CRC32();
				//crc.update(bytes);
				
			JarEntry jarfile = new JarEntry(entryName);									
				jarfile.setMethod( storeType );
				//jarfile.setSize( bytes.length );
				jarfile.setSize( file.length() );
				//if(storeType != STORED)
				//jarfile.setCompressedSize( file.length() );
				jarfile.setCrc( crc.getValue() );
				jarfile.setTime( file.lastModified() );
				
			output.putNextEntry( jarfile );
			
			while(input.available() > 0) {
				int b = input.read();
				output.write(b);
				crc.update(b);
			}
			
			//output.write(bytes);
			output.flush();
			output.closeEntry();
			
			input.close();
			
			jarfile = null;
			input = null;
			bytes = null;
			crc = null;
		} catch(IOException ioe) {
			System.out.println(ioe);
			ioe.printStackTrace();
			
			if(timer != null) {
				timer = null;
			}
		}
	}
	
	
	// Extraction Utilities
	public static void pauseExtraction(boolean p) {
		pauseExtraction = p;
	}
	
	public static boolean extractionIsPaused() {
		return pauseExtraction;
	}
		
	public static void extractJar(File jarFile, File targetDir) {
		String destDir = targetDir.getAbsolutePath();
	
		JarFile jar;
		try {
			jar = new JarFile(jarFile);
		} catch(IOException ioe) {
			JOptionPane.showMessageDialog(null, "Could not unpack: " + jarFile.getName(),
			"File Not Found", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Enumeration enu = jar.entries();
	
		while (enu.hasMoreElements()) {
			while(extractionIsPaused()) {
				try {
					Thread.sleep(3000);
				} catch(InterruptedException ie) {}
			}
						
			try {
				JarEntry file = (JarEntry) enu.nextElement();
				File f = new File(destDir + File.separator + file.getName());
				
				System.out.println("Extracting: " + file.getName() + ", To: " + f.getAbsolutePath() );
				updateLabelText(file.getName());
				updateProgress();
				
				mkpath(f);
				
				if (file.isDirectory()) { // if its a directory, create it
					f.mkdir();
					continue;
				}
				
				InputStream is = jar.getInputStream(file); // get the input stream
				BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f));
				
				while (is.available() > 0) {  // write contents of 'is' to 'fos'
					fos.write(is.read());
				}
				
				fos.close();
				is.close();
				
				fos = null;
				is = null;
				
				int x = 0;
				while(x < 50) {
					x++;
				}
			} catch(IOException ioe) {
				ioe.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		jar = null;
		enu = null;
	
	}	
	
	public static int getEntryCount(File jarFile) {
		int entries = 0;
		
		try {
			JarFile jar = new JarFile(jarFile);
			Enumeration enu = jar.entries();
			
			while(enu.hasMoreElements()) {
				enu.nextElement();
				entries++;
			}
			enu = null;
			jar = null;	
		} catch(IOException ioe) {
			
		}
		
		return entries;
	}
	
	public static Vector<String> getEntriesAsStrings(File jarFile) {
		Vector<String> v = new Vector<String>();
		
		try {
			JarFile jar = new JarFile(jarFile);
			Enumeration<JarEntry> enu = jar.entries();
			
			while(enu.hasMoreElements()) {
				JarEntry entry = enu.nextElement();
				v.addElement( entry.getName() );
			}
			enu = null;
			jar = null;	
		} catch(IOException ioe) {
			
		}
		
		return v;
	}
	
	private static void mkpath(File file) throws IOException {
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
	
	public static void destroyPackage() {
		try {
			if(output == null) {
				return;
			}
			
			if(timer != null) {
				timer.stop();
				timer = null;
			}
			
			output.flush();
			output.close();
			
			stream.flush();
			stream.close();
			
			output = null;
			stream = null;
			
			jarTarget.delete();
			jarTarget = null;
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void stopExtraction() {
		
	}
	
	public static void setProgressBar(JProgressBar bar) {
		progress = bar;
	}
	
	public static void setProgressUpdateAmount(int amount) {
		if(updateAmount <= 0) {
			throw new IllegalArgumentException("Invalid Update Amount Specified");
		}
		
		updateAmount = amount;
	}
	
	public static void updateProgress() {
		if(progress == null) {
			return;
		}
		
		if(progress.isIndeterminate()) {
			progress.setIndeterminate(false);
		}
		
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				try {
					progress.setValue( progress.getValue() + updateAmount );
				} catch(NullPointerException e) {}
			}
		});
	}
	
	public static void setCurrentFileNameLabel(JLabel label) {
		currentFileName = label;
	}
	
	public static void updateLabelText(final String text) {
		if(currentFileName == null) {
			return;
		}
		
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				try {
					currentFileName.setText(label_prefix + text);
				} catch(NullPointerException e) {}
			}
		});
	}
	
	public static void setLabelPrefix(String prefix) {
		if(prefix == null) {
			label_prefix = "Current File: ";
		} else {
			label_prefix = prefix;
		}
	}
}