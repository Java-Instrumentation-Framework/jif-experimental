
package edu.mbl.jif.gui.file;

import java.awt.Color;
import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileView;


public class JavaFileView extends FileView { 

	Icon javaIcon = (Icon) new DiamondIcon(Color.blue);
	Icon classIcon = (Icon) new DiamondIcon(Color.green);
	Icon htmlIcon = (Icon) new DiamondIcon(Color.red);
	Icon jarIcon = (Icon) new DiamondIcon(Color.pink);
	

	public String getName(File file) {
		String filename = file.getName();
		if (filename.endsWith(".java")) {
			String name = filename + " : " + file.length();
			return name;
		}
		return null;
	}

	public String getTypeDescription(File file) {
		String typeDescription = null;
		String filename = file.getName().toLowerCase();

		if (filename.endsWith(".java")) {
			typeDescription = "Java Source";
		} else if (filename.endsWith(".class")) {
			typeDescription = "Java Class File";
		} else if (filename.endsWith(".jar")) {
			typeDescription = "Java Archive";
		} else if (filename.endsWith(".html") || filename.endsWith(".htm")) {
			typeDescription = "Applet Loader";
		}
		return typeDescription;
	}

	public Icon getIcon(File file) {
		if (file.isDirectory()) {
			return null;
		}
		Icon icon = null;
		String filename = file.getName().toLowerCase();
		if (filename.endsWith(".java")) {
			icon = javaIcon;
		} else if (filename.endsWith(".class")) {
			icon = classIcon;
		} else if (filename.endsWith(".jar")) {
			icon = jarIcon;
		} else if (filename.endsWith(".html") || filename.endsWith(".htm")) {
			icon = htmlIcon;
		}
		return icon;
	}

}