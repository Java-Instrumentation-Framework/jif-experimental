package edu.mbl.jif.utils.file;

import java.util.*;
import java.io.*;


public class ExtensionFilenameFilter implements FilenameFilter  {

  Vector extensions = new Vector();

  public ExtensionFilenameFilter(String extension) {

    if (extension.indexOf('.') != -1) {
      extension = extension.substring(extension.lastIndexOf('.')+1);
    }
    extensions.addElement(extension);

  }

  public void addExtension(String extension) {

    if (extension.indexOf('.') != -1) {
      extension = extension.substring(extension.lastIndexOf('.')+1);
    }
    extensions.addElement(extension);

  }

  public boolean accept(File directory, String filename) {

    String extension = filename.substring(filename.lastIndexOf('.')+1);
    if (extensions.contains(extension)) {
      return true;
    }
    return false;
  }

}
