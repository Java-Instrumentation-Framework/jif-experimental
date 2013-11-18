package edu.mbl.jif.camera;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;

import edu.mbl.jif.camera.CamUtils;

public class FileUtil {
  public static final String slash = "\\";

  //////////////////////////////////////////////////////////////////////////
  // DIRECTORY OPERATIONS
  //////////////////////////////////////////////////////////////////////////
  //
  public static String openFile(Component c, String curDir) {
    JFileChooser fc = new JFileChooser(curDir);
    int returnVal = fc.showOpenDialog(c);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      return file.getName();
    }
    else {
      return null; // Cancelled
    }
  }

////////////////////////////////////////////////////////////////////////
// directoryChooser
//
  public static String directoryChooser(String msg, String path) {
    String dirSelected = null;
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(path);
    java.io.File file = null;
    chooser.setFileHidingEnabled(true);
    chooser.setMultiSelectionEnabled(false);
    chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
    chooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
    int state = chooser.showDialog(null, msg);
    if (state == javax.swing.JFileChooser.APPROVE_OPTION) {
      file = chooser.getSelectedFile();
      dirSelected = file.getAbsolutePath();
      System.out.println(dirSelected);
    }
    return dirSelected;
  }

//////////////////////////////////////////////////////////////////////
// Find directory
//
  public static boolean findDir(String directoryName) {
    String[] files; // Array of file names in the directory.

    // Directory name entered by the user
    directoryName = "F:\\";
    // File object referring to the directory
    File directory = new File(directoryName);
    if (directory.isDirectory() == false) {
      if (directory.exists() == false) {
        System.out.println("There is no such directory!");
      }
      else {
        System.out.println("That file is not a directory.");
      }
    }
    else {
      files = directory.list();
      System.out.println("Files in directory \"" + directory + "\":");
      for (int i = 0; i < files.length; i++) {
        System.out.println("   " + files[i]);
      }
    }
    return true;
  }

////////////////////////////////////////////////////////////////////////
// checkDirOK
//
  public static boolean checkDirOK(String path) {
    File dir = new File(path);
    if (dir.exists() == false) {
      //jif.utils.PSjUtils.event("Referenced path does not exist: " + dir);
      return false;
    }
    if (dir.isDirectory() == false) {
      //jif.utils.PSjUtils.event("Referenced path is not a directory: " + dir);
      return false;
    }
    return true;
  }

/////////////////////////////////////////////////////////////////////
// fileSystemRoots
//
  public static File[] fileSystemRoots() {
    // Listing the File System Roots
    // UNIX single root, "/". Windows, each drive is a root
    File[] roots = File.listRoots();
    for (int i = 0; i < roots.length; i++) {
      // process(roots[i]);
    }
    return roots;
  }

///////////////////////////////////////////////////////////////////////
// Traversing a Directory (recursively)
  public static void traverse(File f) {
    // process(f);
    if (f.isDirectory()) {
      String[] children = f.list();
      for (int i = 0; i < children.length; i++) {
        traverse(new File(f, children[i]));
      }
    }
  }

///////////////////////////////////////////////////////////////////////
// Create a Directory
  public static boolean directoryCreate(String path) {
    return (new File(path)).mkdir();
  }

///////////////////////////////////////////////////////////////////////
// Delete a Directory - Deletes all files and subdirectories under dir.
// Returns true if all deletions were successful.
// If a deletion fails, the method stops attempting to delete and returns false.
  public static boolean directoryDelete(String path) {
    File dir = new File(path);
    return directoryDelete(dir);
  }

  public static boolean directoryDelete(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = directoryDelete(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }
    return dir.delete(); // The directory is now empty so delete it
  }

///////////////////////////////////////////////////////////////////////
// getFilesSorted - get the files in dir and sort them alphabetically
//
  public static String[] getFilesSorted(String dir, String filterSet) {
    File path = new File(dir);
    String[] list;
    if (!path.exists()) {
      return null;
    }
    if (filterSet == null) {
      list = path.list();
    }
    else {
      list = path.list(filter(filterSet));
    }

    //    System.out.println("In getFilesSorted:");
    //    for (int i = 0; i < list.length; i++) {
    //        System.out.println(list[i]);
    //    }
    if (list != null) {
      if (list.length > 0) {
        Arrays.sort(list, new AlphabeticComparator());
      }
    }
    return list;
  }

  public static FilenameFilter filter(final String afn) {
    // Creation of anonymous inner class:
    return new FilenameFilter() {
      String fn = afn;

      public boolean accept(File dir, String n) {
        // Strip path information:
        String f = new File(n).getName();
        return f.indexOf(fn) != -1;
      }
    }; // End of anonymous inner class
  }

  public static boolean matchWildcard(String mask, String target) {
    // ported almost verbatim from rpb's ages old Turbo Pascal 1.0 routine
    //
    // Compare two strings which may contain the DOS wildcard characters * and ?
    // and return a boolean result indicating their "equivalence".
    //
    // Most usage will involve a filename mask (e.g. *.java) being
    // compared with some filename (e.g. WCMatcher.java).  However,
    // either mask or target or both may contain DOS wildcard characters
    // and this routine "should" provide an arguably correct result
    //
    // this method is case insensitive
    int p1 = 0; // used as character index into mask
    int p2 = 0; // used as character index into target
    boolean matched = true; // Assume true to begin.
    if ( (mask.length() == 0) && (target.length() == 0)) {
      matched = true;
    }
    else {
      if (mask.length() == 0) {
        if (target.charAt(0) == '*') {
          matched = true;
        }
        else {
          matched = false;
        }
      }
      else {
        if (target.length() == 0) {
          if (mask.charAt(0) == '*') {
            matched = true;
          }
          else {
            matched = false;
          }
        }
      }
    }
    while ( (matched) && (p1 < mask.length()) && (p2 < target.length())) {
      if ( (mask.charAt(p1) == '?') || (target.charAt(p2) == '?')) {
        p1++;
        p2++;
      }
      else {
        if (mask.charAt(p1) == '*') {
          p1++;
          if (p1 < mask.length()) {
            while ( (p2 < target.length())
                   && (!matchWildcard(mask.substring(p1, mask.length()),
                                      target.substring(p2, target.length())))) {
              p2++;
            }
            if (p2 >= target.length()) {
              matched = false;
            }
            else {
              p1 = mask.length();
              p2 = target.length();
            }
          }
          else {
            p2 = target.length();
          }
        }
        else {
          if (target.charAt(p2) == '*') {
            p2++;
            if (p2 < target.length()) {
              while ( (p1 < mask.length())
                     && (!matchWildcard(mask.substring(p1,
                  mask.length()),
                                        target.substring(p2, target.length())))) {
                p1++;
              }
              if (p1 >= mask.length()) {
                matched = false;
              }
              else {
                p1 = mask.length();
                p2 = target.length();
              }
            }
            else {
              p1 = mask.length();
            }
          }
          else {
            if (mask.toLowerCase()
                .charAt(p1) == target.toLowerCase()
                .charAt(p2)) {
              p1++;
              p2++;
            }
            else {
              matched = false;
            }
          }
        }
      }
    }
    if (p1 >= mask.length()) {
      while ( (p2 < target.length()) && (target.charAt(p2) == '*')) {
        p2++;
      }
      if (p2 < target.length()) {
        matched = false;
      }
    }
    if (p2 >= target.length()) {
      while ( (p1 < mask.length()) && (mask.charAt(p1) == '*')) {
        p1++;
      }
      if (p1 < mask.length()) {
        matched = false;
      }
    }
    return matched;
  }

///////////////////////////////////////////////////////////////////////////
// FILE OPERATIONS
///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////
// Delete a File
//
  public static boolean fileDelete(String path) {
    File f = new File(path);

    //System.out.println("Path in delfile: " + path + " : " + f);
    return (new File(path)).delete();
  }

/////////////////////////////////////////////////////////////////////////
// getJustFilename - get the filename off the end of a path
//
  public static String getJustFilename(String path) {
    String f = null;
    int i = path.lastIndexOf('\\');
    if ( (i > 0) && (i < (path.length() - 1))) {
      f = path.substring(i + 1);
    }
    else {
      f = path;
    }
    return f;
  }

  public static String getJustFilenameNoExt(String path) {
    String f = null;
    int i = path.lastIndexOf('\\');
    if ( (i > 0) && (i < (path.length() - 1))) {
      f = path.substring(i + 1);
    }
    else {
      f = path;
    }
    i = f.lastIndexOf('.');
    if ( (i > 0) && (i < (f.length() - 1))) {
      f = f.substring(0, i);
    }
    return f;
  }

/////////////////////////////////////////////////////////////////////////
// getJustFilename - get the filename off the end of a path
//
  public static String getJustPath(String path) {
    String p = null;
    int i = path.lastIndexOf('\\');
    if ( (i > 0) && (i < (path.length() - 1))) {
      p = path.substring(0, i);
    }
    return p;
  }

/////////////////////////////////////////////////////////////////////////
// getExtension - get the extension of a file
//
  public static String getExtension(File f) {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf('.');
    if ( (i > 0) && (i < (s.length() - 1))) {
      ext = s.substring(i + 1)
          .toLowerCase();
    }
    return ext;
  }

/////////////////////////////////////////////////////////////////////////
// removeExtension - remove the extension from a filename
//
  public static String removeExtension(String f) {
    String ext = null;
    if (f == null) {
      return null;
    }
    int i = f.lastIndexOf('.');
    if (i > 1) {
      ext = f.substring(0, i);
    }
    return ext;
  }

//////////////////////////////////////////////////////////////////////////
// Series path/file utilities
//   public static boolean isFromSeries(String filename) {
//      // Check to see if this is connected to a T or Z series directory
//      System.out.println("isFromSeries.filename= " + filename);
//      if (!filename.startsWith("ST") && !filename.startsWith("TZ")) {
//         return false;
//      } else {
//         String seriesID = FileUtil.removeExtension(filename);
//         seriesID = seriesID.substring(3);
//         System.out.println("seriesID: " + seriesID);
//         if (FileUtil.checkDirOK(DataAccess.pathSeries() + seriesID)) {
//            return true;
//         } else {
//            return false;
//         }
//      }
//   }

//   public static String getSeriesID(String filename) {
//      // Check to see if this is connected to a T or Z series directory
//      if (filename.lastIndexOf("\\") != -1)
//         filename = filename.substring(filename.lastIndexOf("\\"));
//      System.out.println("isFromSeries.filename = " + filename);
//      if (!filename.startsWith("ST") && !filename.startsWith("TZ")) {
//         return null;
//      } else {
//         String seriesID = FileUtil.removeExtension(filename);
//         seriesID = seriesID.substring(3);
//         System.out.println("seriesID: " + seriesID);
//         if (FileUtil.checkDirOK(DataAccess.pathSeries() + seriesID)) {
//            return seriesID;
//         } else {
//            return null;
//         }
//      }
//   }

////////////////////////////////////////////////////////////////////////
// fileCopy
//
  public static void fileCopy(String source_name, String dest_name) throws
      IOException {
    File source_file = new File(source_name);
    File destination_file = new File(dest_name);
    FileInputStream source = null;
    FileOutputStream destination = null;
    byte[] buffer;
    int bytes_read;
    try {
      if (!source_file.exists() || !source_file.isFile()) {
        throw new FileCopyException("FileCopy:no such source file:"
                                    + source_name);
      }
      if (!source_file.canRead()) {
        throw new FileCopyException("FileCopy:sourcefile"
                                    + "is unreadable:" + source_name);
      }
      if (destination_file.exists()) {
        if (destination_file.isFile()) {
          DataInputStream in = new DataInputStream(System.in);
          String response;
          if (!destination_file.canWrite()) {
            throw new FileCopyException("FileCopy:destination"
                                        + "file is unwritable:" + dest_name);
          }
        }
        else {
          throw new FileCopyException("FileCopy:destination"
                                      + "is not a file:" + dest_name);
        }
      }
      else {
        File parentdir = parent(destination_file);
        if (!parentdir.exists()) {
          throw new FileCopyException("FileCopy: destination"
                                      + "directory doesn`t exist:" + dest_name);
        }
        if (!parentdir.canWrite()) {
          throw new FileCopyException("FileCopy: destination"
                                      + "directory is unwritable:" + dest_name);
        }
      }
      source = new FileInputStream(source_file);
      destination = new FileOutputStream(destination_file);
      buffer = new byte[1024];
      while (true) {
        bytes_read = source.read(buffer);
        if (bytes_read == -1) {
          break;
        }
        destination.write(buffer, 0, bytes_read);
      }
    }
    finally {
      if (source != null) {
        try {
          source.close();
        }
        catch (IOException e) {}
      }
      ;
      if (destination != null) {
        try {
          destination.close();
        }
        catch (IOException e) {}
      }
      ;
    }
  }

  private static File parent(File f) {
    String dirname = f.getParent();
    if (dirname == null) {
      if (f.isAbsolute()) {
        return new File(File.separator);
      }
      else {
        return new File(System.getProperty("user.dir"));
      }
    }
    return new File(dirname);
  }

// Determining If a Filename Path Is a File or a Directory
  public static boolean isFile(String fileName) {
    File dir = new File(fileName);
    boolean isDir = dir.isDirectory();
    if (isDir) {
      return true;
    }
    else {
      return false;
    }
  }

  public static boolean doesExist(String fileName) {
    // Determining If a File or Directory Exists
    boolean exists = (new File(fileName)).exists();
    if (exists) {
      return true;
    }
    else {
      return false;
    }
  }

  public static boolean creatingFile(String fileName) {
    boolean success = false;
    try {
      File file = new File("filename");

      // Create file if it does not exist
      success = file.createNewFile();
    }
    catch (IOException e) {
      return false;
    }
    if (success) {
      // File did not exist and was created
      return true;
    }
    else {
      return false; // File already exists
    }
  }

  boolean createTemporaryFile(String fileName) {
    try {
      // Create temp file.
      File temp = File.createTempFile("pattern", ".suffix");

      // Delete temp file when program exits.
      temp.deleteOnExit();
      // Write to temp file
      BufferedWriter out = new BufferedWriter(new FileWriter(temp));
      out.write("aString");
      out.close();
    }
    catch (IOException e) {
      return false;
    }
    return true;
  }

  ///////////////////////////////////////////////////////////////////////
  // saveFileDialog
  //
  public static String saveFileDialog(Component parent, String title,
                                      String dirDefault, String fileDefault) {
    String saveToFile =
        chooseFileSave(title, parent, dirDefault, fileDefault);
    if (saveToFile != null) {
      System.out.println("saveToFile: " + saveToFile);
      if (FileUtil.doesExist(saveToFile)) {
        if (true) {
//            CamUtils.boxConfirm("Saving File",
//                        "Overwrite existing file?\n" + saveToFile)) {
          return saveToFile;
        }
      }
      else {
        //jif.utils.PSjUtils.boxNotify("Save File", saveToFile + " saved.");
        return saveToFile;
      }
      return null;
    }
    return null;
  }

////////////////////////////////////////////////////////////////////
// chooseFileSave
//
  public static String chooseFileSave(String title, Component parent,
                                      String dirName, String filename) {
    //Create a file chooser
    File file = null;
    String filePath = null;
    final JFileChooser fc = new JFileChooser();
    if (dirName != null) {
      try {
        File f = new File(new File(dirName).getCanonicalPath());
        fc.setCurrentDirectory(f);
      }
      catch (IOException e) {}
    }
    if (filename != null) {
      try {
        File f = new File(new File(filename).getCanonicalPath());
        fc.setSelectedFile(f);
      }
      catch (IOException e) {}
    }
    fc.setDialogTitle(title);
    int returnVal = fc.showSaveDialog(parent); // <== Save Dialog
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      filePath = fc.getSelectedFile()
          .getAbsolutePath();
      return filePath;
    }
    else {
      return null;
    }
  }

////////////////////////////////////////////////////////////////////////
// GetDiscSpace
//
  public static long getDiscSpace() {
    Runtime rt = Runtime.getRuntime();
    String strDiskSpace = null;
    Process p = null;
    String strOS = System.getProperty("os.name");
    String availStr = null;
    long avail = 0;
    try {
      if (strOS.equals("Windows 2000")) {
        p = rt.exec("cmd.exe /c dir c:\\");
      }
      else {
        // Windows 95 , 98
        p = rt.exec("command.com /c dir c:\\");
      }
      InputStream inStream = p.getInputStream();
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(inStream));
      String strTemp;
      while ( (strTemp = reader.readLine()) != null) {
        strDiskSpace = strTemp;
      }

      //      System.out.println(strDiskSpace.trim());
      StringTokenizer sToken =
          new StringTokenizer(strDiskSpace.trim(), " ");
      sToken.nextToken();
      sToken.nextToken();
      availStr = sToken.nextToken();
      String cleanStr = removeCommas(availStr);

      //      System.out.println(cleanStr);
      avail = Long.parseLong(cleanStr);
      p.destroy();
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
    if (availStr == null) {
      return -1;
    }
    else {
      //System.out.println(avail);
      return avail;
    }
  }

  static String removeCommas(String input) {
    String strA = "";
    StringCharacterIterator iter = new StringCharacterIterator(input);
    for (char current_char = iter.first();
         current_char != CharacterIterator.DONE;
         current_char = iter.next()) {
      if (!Character.isLetterOrDigit(current_char)) {
        continue;
      }
      strA += current_char;
    }
    return strA;
  }

   static void saveTxtFile(String file, String xml, boolean b) {
      throw new UnsupportedOperationException("Not yet implemented");
   }

}

////////////////////////////////////////////////
//
class AlphabeticComparator
    implements Comparator {
  public int compare(Object o1, Object o2) {
    String s1 = (String) o1;
    String s2 = (String) o2;
    return s1.toLowerCase()
        .compareTo(s2.toLowerCase());
  }
}

////////////////////////////////////////////////
//
class FileCopyException
    extends IOException {
  public FileCopyException(String msg) {
    super(msg);
  }
}
