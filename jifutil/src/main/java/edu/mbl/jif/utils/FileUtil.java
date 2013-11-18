package edu.mbl.jif.utils;

//import edu.mbl.jif.imaging.ImagingUtils;
import edu.mbl.jif.utils.string.StringUtility;
import edu.mbl.jif.utils.string.ListOfString;
//import edu.mbl.jif.utils.prefs.Prefs;
//import java.beans.PropertyVetoException;
import java.io.*;
import java.text.*;
import java.util.*;
// jdk  >= 1.4
import java.util.regex.Pattern;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

//import edu.mbl.jif.gui.*;
//import foxtrot.*;

public class FileUtil {

    public static final String slash = "\\";
    
      /**
   * Gets current directory path
   *
   * @return the current directory path
   */
  public static String getcwd() {
    File here = new File(".");
    try {
      return here.getCanonicalPath();
    } catch (Exception e) {};
    return here.getAbsolutePath();
//    return System.getProperty("user.dir");
  }

    // FOR TEST =======================================================
    //File fileSystems[] = File.listRoots();
    //  iterate through the array and get the name of the file system
    //for (int i=0;i<fileSystems.length ;i++ ){
    //  System.out.println("Got File System *"+
    //      fileSystems[i].getPath() + "*");
    //}

    /////////////////////////////////////////////////////////////
    // FileChoosers...
    //
    //-----------------------------------------------------------
    // File Chooser
    //
    public static java.io.File fileChooser(String msg, String path)
      {
        //String dirSelected = null;
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(path);
        java.io.File file = null;

        //chooser.setFileHidingEnabled(true);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
        chooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
        int state = 0;
        //        if(PSj.deskTopFrame!=null)
//         state = chooser.showDialog(PSj.deskTopFrame, msg);
//        else 
        state = chooser.showDialog(null, msg);
        if (state == javax.swing.JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            //dirSelected = file.getAbsolutePath();
            System.out.println(file);
        }
        return file;
      }

    //-----------------------------------------------------------
    // fileOpenerTree
    //
    public static void fileOpenerTreeDialog(String msg, String path)
      {
//        JPanel panelFileChooser = new JPanel();
//        panelFileChooser.setLayout(new BorderLayout());
//        JPanel choices = new JPanel(new FlowLayout());
//        final DirectoryChooserPanel dlg = new DirectoryChooserPanel(path);
//        panelFileChooser.add(dlg, BorderLayout.CENTER);
//        JButton openButton = new JButton("Open");
//        choices.add(openButton);
//        panelFileChooser.add(choices, BorderLayout.SOUTH);
//        final InternalFrameModal frame =
//                new InternalFrameModal("Open File",
//                panelFileChooser, 20, 50);
//        //
//        openButton.addActionListener(new ActionListener()
//                {
//
//                    public void actionPerformed(ActionEvent e) {
//                        if (dlg.getSelectedFile() != null) {
//                            String ext =
//                                    FileUtil.getFileExtension(dlg.getSelectedFile(),
//                                    false);
//
//                            // System.out.println("dlg.getSelectedFile(): " + dlg.getSelectedFile());
//                            if (dlg.getSelectedFile().isFile() && (ext.equalsIgnoreCase("tif") || ext.equalsIgnoreCase("tiff"))) {
//                                // *********************************openSelectedFile(dlg.getSelectedFile());
//                                try {
//                                    frame.setClosed(true);
//                                } catch (PropertyVetoException ex) {
//                                }
//                            } else {
//                                DialogBoxI.boxError(frame, "Sorry",
//                                        "Cannot open this file type");
//                            }
//                        }
//                    }
//                });
//        frame.setSize(new Dimension(300, 600));
//        frame.setModal();
      }

    //----------------------------------------------------------------
    // openSelectedFile (for TIFFs and PolStacks)
    //
    // DELETE - this is in PSj
//    public static void openSelectedFile(final java.io.File file) {
//        //System.out.println("::" + file);
//        try {
//            Worker.post(new Task() {
//                public Object run() throws Exception {
//                    int imageType =
//                            psj.Image.ImageType.getImageTypeFor(file.getName());
//                    edu.mbl.jif.utils.PSjUtils.statusProgress("Opening file: " +
//                            file.getName());
//                    if ((imageType == psj.Image.ImageType.POLSTACK)
//                    || (imageType == psj.Image.ImageType.RAWPOLSTACK)
//                    || (imageType == psj.Image.ImageType.BKGDSTACK)) {
//                        psj.Image.ImageManager.loadPolStackFromFile(file.
//                                getAbsolutePath());
//                    } else {
//                        ArrayList imgs = ImagingUtils.loadImageArrayList(file.getAbsolutePath());
//                        
//                        if (imgs != null) {
//                            ViewerUtil.openViewerForTIFF(file.getAbsolutePath(), imgs);
//                        } else {
//                            DialogBoxI.boxError("Error opening TIFF file",
//                                    "Cannot open: " + file.getAbsolutePath());
//                        }
//                    }
//                    return null;
//                }
//            });
//        } catch (Exception x) {
//            DialogBoxI.boxError("Error Open_InAnalyzer", x.getMessage());
//        } finally {
//            edu.mbl.jif.utils.PSjUtils.statusClear();
//        }
//        edu.mbl.jif.utils.PSjUtils.statusClear();
//    }
//    

    //////////////////////////////////////////////////////////////////////////
    //                           DIRECTORY OPERATIONS
    //////////////////////////////////////////////////////////////////////////
    //----------------------------------------------------------------
    // directoryChooser
    //
    public static String directoryChooser(String msg, String path)
      {
        String dirSelected = null;
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(path);
        java.io.File file = null;
        chooser.setFileHidingEnabled(true);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
        //int state = chooser.showDialog(PSj.deskTopFrame, msg);
        int state = chooser.showDialog(null, msg);
        if (state == javax.swing.JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            dirSelected = file.getAbsolutePath();
            System.out.println(dirSelected);
        }
        return dirSelected;
      }

    //----------------------------------------------------------------
    // Find directory
    //
    public static boolean findDir(String directoryName)
      {
        // +++ not done...
        String[] files; // Array of file names in the directory.

        // Directory name entered by the user
        directoryName = "F:\\";
        // File object referring to the directory
        File directory = new File(directoryName);
        if (directory.isDirectory() == false) {
            if (directory.exists() == false) {
                System.out.println("There is no such directory!");
            } else {
                System.out.println("That file is not a directory.");
            }
        } else {
            files = directory.list();
            System.out.println("Files in directory \"" + directory + "\":");
            for (int i = 0; i < files.length; i++) {
                System.out.println("   " + files[i]);
            }
        }
        return true;
      }

    //----------------------------------------------------------------
    // checkDirOK
    //
    public static boolean checkDirOK(String path)
      {
        File dir = new File(path);
        dumpFileInfo(dir);
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

    public static void dumpFileInfo(File f1)
      {
        System.out.println("File Name:" + f1.getName());
        System.out.println("Path:" + f1.getPath());
        System.out.println("Abs Path:" + f1.getAbsolutePath());
        System.out.println("Parent:" + f1.getParent());
        System.out.println(f1.exists() ? "exists" : "does not exist");
        System.out.println(f1.canWrite() ? "is writeable" : "is not writeable");
        System.out.println(f1.canRead() ? "is readable" : "is not readable");
        System.out.println("is a directory" + f1.isDirectory());
        System.out.println(f1.isFile() ? "is normal file" : "might be a named pipe");
        System.out.println(f1.isAbsolute() ? "is absolute" : "is not absolute");
        System.out.println("File last modified:" + f1.lastModified());
        System.out.println("File size:" + f1.length() + " Bytes");
      }

    //----------------------------------------------------------------
    // fileSystemRoots
    //
    public static File[] fileSystemRoots()
      {
        // Listing the File System Roots
        // UNIX single root, "/". Windows, each drive is a root
        File[] roots = File.listRoots();
        for (int i = 0; i < roots.length; i++) {
            // process(roots[i]);
        }
        return roots;
      }

    //----------------------------------------------------------------
    //  Traversing a Directory (recursively)
    //
    public static void traverse(File f)
      {

        if (f.isDirectory()) {
            String[] children = f.list();
            for (int i = 0; i < children.length; i++) {
                traverse(new File(f, children[i]));
            }
        } else {
            // process(f);
        }
      }

    //----------------------------------------------------------------
    // Create a Directory
    //
    public static boolean directoryCreate(String path)
      {
        return (new File(path)).mkdir();
      }

    //----------------------------------------------------------------
    // Delete a Directory - Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    //
    public static boolean directoryDelete(String path)
      {
        File dir = new File(path);
        return directoryDelete(dir);
      }

    public static boolean directoryDelete(File dir)
      {
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

    ////////////////////////////////////////////////////////////////////////
    // Wildcard match
    //
    public static boolean matchWildcard(String mask, String target)
      {
        // ported almost verbatim from rpb's ages old Turbo Pascal 1.0 routine
        // Compare two strings which may contain the DOS wildcard characters * and ?
        // and return a boolean result indicating their "equivalence".
        // Most usage will involve a filename mask (e.g. *.java) being
        // compared with some filename (e.g. WCMatcher.java).  However,
        // either mask or target or both may contain DOS wildcard characters
        // and this routine "should" provide an arguably correct result.
        // This method is case insensitive.
        int p1 = 0; // used as character index into mask
        int p2 = 0; // used as character index into target
        boolean matched = true; // Assume true to begin.
        if ((mask.length() == 0) && (target.length() == 0)) {
            matched = true;
        } else {
            if (mask.length() == 0) {
                if (target.charAt(0) == '*') {
                    matched = true;
                } else {
                    matched = false;
                }
            } else {
                if (target.length() == 0) {
                    if (mask.charAt(0) == '*') {
                        matched = true;
                    } else {
                        matched = false;
                    }
                }
            }
        }
        while ((matched) && (p1 < mask.length()) && (p2 < target.length())) {
            if ((mask.charAt(p1) == '?') || (target.charAt(p2) == '?')) {
                p1++;
                p2++;
            } else {
                if (mask.charAt(p1) == '*') {
                    p1++;
                    if (p1 < mask.length()) {
                        while ((p2 < target.length()) && (!matchWildcard(mask.substring(p1,
                            mask.length()),
                            target.substring(p2,
                            target.length())))) {
                            p2++;
                        }
                        if (p2 >= target.length()) {
                            matched = false;
                        } else {
                            p1 = mask.length();
                            p2 = target.length();
                        }
                    } else {
                        p2 = target.length();
                    }
                } else {
                    if (target.charAt(p2) == '*') {
                        p2++;
                        if (p2 < target.length()) {
                            while ((p1 < mask.length()) && (!matchWildcard(mask.substring(p1,
                                mask.length()),
                                target.substring(p2,
                                target.length())))) {
                                p1++;
                            }
                            if (p1 >= mask.length()) {
                                matched = false;
                            } else {
                                p1 = mask.length();
                                p2 = target.length();
                            }
                        } else {
                            p1 = mask.length();
                        }
                    } else {
                        if (mask.toLowerCase().charAt(p1) ==
                            target.toLowerCase().charAt(p2)) {
                            p1++;
                            p2++;
                        } else {
                            matched = false;
                        }
                    }
                }
            }
        }
        if (p1 >= mask.length()) {
            while ((p2 < target.length()) && (target.charAt(p2) == '*')) {
                p2++;
            }
            if (p2 < target.length()) {
                matched = false;
            }
        }
        if (p2 >= target.length()) {
            while ((p1 < mask.length()) && (mask.charAt(p1) == '*')) {
                p1++;
            }
            if (p1 < mask.length()) {
                matched = false;
            }
        }
        return matched;
      }

    //----------------------------------------------------------------
    // findByExt
    //
    public static File[] findByExt(File base, String ext)
      {
        // Searches recursively in base directory
        // for files that match the extension ext
        Vector result = new Vector();
        findByExtWorker(result, base, ext);
        // make generic array into a type-safe array
        Object[] objs = result.toArray();
        File[] files = new File[objs.length];
        System.arraycopy(objs, 0, files, 0, objs.length);
        return files;
      }

    private static void findByExtWorker(Vector result, File base, String ext)
      {
        File[] files = base.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (!file.isDirectory()) {
                String currentExt = getFileExtension(file);
                if (currentExt.equalsIgnoreCase(ext)) { // bingo; add to result set
                    result.add(file);
                }
            } else { // file is a directory, recurse
                findByExtWorker(result, file, ext);
            }
        }
      }

    ///////////////////////////////////////////////////////////////////////
    // getFilesSorted_____ - get the files in dir and sort them
    //
    public static String[] getFilesSorted(String dir, String filterSet)
      {
        return getFilesSortedAlpha(dir, filterSet);
      }

    public static String[] getFilesSortedAlpha(String dir, String filterSet)
      {
        File path = new File(dir);
        if (!path.exists()) {
            return null;
        }
        String[] list;
        if (filterSet == null) {
            list = path.list();
        } else {
            list = path.list(filter(filterSet));
        }

        // System.out.println("In getFilesSorted:");
        // for (int i = 0; i < list.length; i++) {
        //     System.out.println(list[i]);
        // }
        if (list != null) {
            if (list.length > 0) {
                Arrays.sort(list, new AlphabeticComparator());
            }
        }
        return list;
      }

    //--------------------------------------------------------------
    public static String[] getFilesSortedChrono(String dir, String filterSet)
      {
        File path = new File(dir);
        if (!path.exists()) {
            return null;
        }
        File[] list;
        if (filterSet == null) {
            list = path.listFiles();
            path.list();
        } else {
            list = path.listFiles(filter(filterSet));
        }
        if (list != null) {
            if (list.length > 0) {
                Arrays.sort(list, new ChronoComparator());
            }
        }
        String[] strList = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            //strList[i] = list[i].getAbsolutePath();
            strList[i] = list[i].getName();
        }
        return strList;
      }

    //--------------------------------------------------------------
    //
    public static FilenameFilter filter(final String afn)
      {
        // Creation of anonymous inner class:
        return new FilenameFilter() {

            String fn = afn;

            public boolean accept(File dir, String n)
              {
                // Strip path information:
                String f = new File(n).getName();
                return f.indexOf(fn) != -1;
              }
        }; // End of anonymous inner class
      }

    ///////////////////////////////////////////////////////////////////////////
    // FILE OPERATIONS
    ///////////////////////////////////////////////////////////////////////////
    //-----------------------------------------------------------
    // Delete a File
    //
    public static boolean fileDelete(String path)
      {
        File f = new File(path);

        //System.out.println("Path in delfile: " + path + " : " + f);
        return (new File(path)).delete();
      }

    //-----------------------------------------------------------
    // fileCopy
    //
    public static void fileCopy(String source_name,
        String dest_name) throws IOException
      {
        File source_file = new File(source_name);
        File destination_file = new File(dest_name);
        FileInputStream source = null;
        FileOutputStream destination = null;
        byte[] buffer;
        int bytes_read;
        try {
            if (!source_file.exists() || !source_file.isFile()) {
                throw new FileCopyException("FileCopy:no such source file:" + source_name);
            }
            if (!source_file.canRead()) {
                throw new FileCopyException("FileCopy:sourcefile" +
                    "is unreadable:" + source_name);
            }
            if (destination_file.exists()) {
                if (destination_file.isFile()) {
                    DataInputStream in = new DataInputStream(System.in);
                    String response;
                    if (!destination_file.canWrite()) {
                        throw new FileCopyException("FileCopy:destination" + "file is unwritable:" +
                            dest_name);
                    }
                } else {
                    throw new FileCopyException("FileCopy:destination" + "is not a file:" + dest_name);
                }
            } else {
                File parentdir = parent(destination_file);
                if (!parentdir.exists()) {
                    throw new FileCopyException("FileCopy: destination" + "directory doesn`t exist:" +
                        dest_name);
                }
                if (!parentdir.canWrite()) {
                    throw new FileCopyException("FileCopy: destination" + "directory is unwritable:" +
                        dest_name);
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
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {
                }
            }
            if (destination != null) {
                try {
                    destination.close();
                } catch (IOException e) {
                }
            }
        }
      }

    private static File parent(File f)
      {
        String dirname = f.getParent();
        if (dirname == null) {
            if (f.isAbsolute()) {
                return new File(File.separator);
            } else {
                return new File(System.getProperty("user.dir"));
            }
        }
        return new File(dirname);
      }

    // Determining If a Filename Path Is a File or a Directory
//  public static boolean isFile(String fileName) {
//    File dir = new File(fileName);
//    boolean isDir = dir.isDirectory();
//    if (isDir) {
//      return true;
//    } else {
//      return false;
//    }
//  }
    public static boolean doesExist(String fileName)
      {
        // Determining If a File or Directory Exists
        boolean exists = (new File(fileName)).exists();
        if (exists) {
            return true;
        } else {
            return false;
        }
      }

    public static boolean creatingFile(String fileName)
      {
        boolean success = false;
        try {
            File file = new File("filename");

            // Create file if it does not exist
            success = file.createNewFile();
        } catch (IOException e) {
            return false;
        }
        if (success) {
            // File did not exist and was created
            return true;
        } else {
            return false; // File already exists
        }
      }

    // +++ Not Done...
    boolean createTemporaryFile(String fileName)
      {
        try {
            // Create temp file.
            File temp = File.createTempFile("pattern", ".suffix");

            // Delete temp file when program exits.
            temp.deleteOnExit();
            // Write to temp file
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
            out.write("aString");
            out.close();
        } catch (IOException e) {
            return false;
        }
        return true;
      }

    ///////////////////////////////////////////////////////////////////////
    // saveFileDialog
    //
    public static String saveFileDialog(Component parent, String title,
        String dirDefault, String fileDefault)
      {
        String saveToFile = chooseFileSave(title, parent, dirDefault,
                fileDefault);
        if (saveToFile != null) {
            System.out.println("saveToFile: " + saveToFile);
            if (FileUtil.doesExist(saveToFile)) {
                if (DialogBoxI.boxConfirm("Save File",
                        "Overwrite existing file?\n" + saveToFile)) {
                    //DialogBoxes.boxNotify("Save File", saveToFile + " overwritten.");
                    return saveToFile;
                }
            } else {
                //DialogBoxes.boxNotify("Save File", saveToFile + " saved.");
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
        String dirName, String filename)
      {
        //Create a file chooser
        File file = null;
        String filePath = null;
        final JFileChooser fc = new JFileChooser();
        if (dirName != null) {
            try {
                File f = new File(new File(dirName).getCanonicalPath());
                fc.setCurrentDirectory(f);
                System.out.println("isDir: " + f.isDirectory() + "  path: " + f.getCanonicalPath() + "  file: " + f.getCanonicalFile());
                System.out.println("Curr: " + fc.getCurrentDirectory());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (filename != null) {
            try {
                File f = new File(new File(filename).getCanonicalPath());
                fc.setSelectedFile(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fc.setDialogTitle(title);
        int returnVal = fc.showSaveDialog(parent); // <== Save Dialog
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filePath = fc.getSelectedFile().getAbsolutePath();
            return filePath;
        } else {
            return null;
        }
      }

    ////////////////////////////////////////////////////////////////////////
    // GetDiscSpace
    // Windows ONLY
    public static long getDiscSpace(String rootPath)
      {
        File disk = new File(rootPath);
        return disk.getFreeSpace();
      }
//        Runtime rt = Runtime.getRuntime();
//        String strDiskSpace = null;
//        Process p = null;
//        String strOS = System.getProperty("os.name");
//        String availStr = null;
//        long avail = 0;
//        try {
//            if (strOS.equals("Windows 2000") || strOS.equals("Windows XP")) {
//                p = rt.exec("cmd.exe /c dir c:\\");
//            } else {
//                // Windows 95 , 98
//                p = rt.exec("command.com /c dir c:\\");
//            }
//            InputStream inStream = p.getInputStream();
//            BufferedReader reader =
//                    new BufferedReader(new InputStreamReader(inStream));
//            String strTemp;
//            while ((strTemp = reader.readLine()) != null) {
//                strDiskSpace = strTemp;
//            }
//
//            // System.out.println(strDiskSpace.trim());
//            StringTokenizer sToken = new StringTokenizer(strDiskSpace.trim(),
//                    " ");
//            sToken.nextToken();
//            sToken.nextToken();
//            availStr = sToken.nextToken();
//            String cleanStr = removeCommas(availStr);
//
//            // System.out.println(cleanStr);
//            avail = Long.parseLong(cleanStr);
//            p.destroy();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        if (availStr == null) {
//            return -1;
//        } else {
//            //System.out.println(avail);
//            return avail;
//        }
//    }

    //-----------------------------------------------------------
    // Filename Manipulations
    //
    //----------------------------------------------------------------
    //
    public static String getJustPath(String path)
      {
        String p = null;
        int i = path.lastIndexOf('\\');
        if ((i > 0) && (i < (path.length() - 1))) {
            p = path.substring(0, i);
        }
        return p;
      }

    //----------------------------------------------------------------
    //
    public static String getBaseName(String name)
      {
        // Strips path and extension from a filename example:
        // lib/venus.jnlp -> venus
        // 1) Strip path.
        String base = new File(name).getName();

        // 2) Strip possible extension.
        int index = base.lastIndexOf('.');
        if (index != -1) {
            base = base.substring(0, index);
        }
        return base;
      }

    //----------------------------------------------------------------
    //
    public static String getFileExtension(String filename)
      {
        // returns extension including .
        // Now strip of possible extension.
        String extension = "";
        int index = filename.lastIndexOf('.');
        if (index != -1) {
            extension = filename.substring(index);
        }
        return extension;
      }

    //  public static String getFileExtension(File f) {
    //    String ext = null;
    //    String s = f.getName();
    //    int i = s.lastIndexOf('.');
    //    if ( (i > 0) && (i < (s.length() - 1))) {
    //      ext = s.substring(i + 1)
    //          .toLowerCase();
    //    }
    //    return ext;
    //  }
    public static String getFileExtension(File file)
      {
        // returns extension including .
        // Strip of path first.
        String base = file.getName();
        return getFileExtension(base);
      }

    public static String getFileExtension(String filename, boolean keepDot)
      {
        filename = filename.replace('\\', '/');
        String name;
        int namePos = filename.lastIndexOf('/');
        if (namePos != -1) {
            name = filename.substring(namePos + 1);
        } else {
            // no path info found
            name = filename;
        }
        String ext;
        int extPos = name.lastIndexOf('.');
        if (extPos != -1) {
            if (keepDot) {
                ext = name.substring(extPos);
            } else {
                ext = name.substring(extPos + 1);
            }
        } else {
            // no extension found
            ext = "";
        }
        return ext;
      }

    public static String getFileExtension(File file, boolean keepDot)
      {
        // Strip path first.
        String base = file.getName();
        String extension = "";
        int index = base.lastIndexOf('.');
        if (index != -1) {
            if (keepDot) {
                extension = base.substring(index);
            } else {
                extension = base.substring(index + 1);
            }
        }
        return extension;
      }

    //---------------------------------------------------------------
    //
    public static String getJustFilename(String path)
      {
        String f = null;
        int i = path.lastIndexOf('\\');
        if ((i > 0) && (i < (path.length() - 1))) {
            f = path.substring(i + 1);
        } else {
            f = path;
        }
        return f;
      }

    public static String getJustFilenameNoExt(String path)
      {
        String f = null;
        int i = path.lastIndexOf('\\');
        if ((i > 0) && (i < (path.length() - 1))) {
            f = path.substring(i + 1);
        } else {
            f = path;
        }
        i = f.lastIndexOf('.');
        if ((i > 0) && (i < (f.length() - 1))) {
            f = f.substring(0, i);
        }
        return f;
      }

    public static String removeExtension(String f)
      {
        String ext = null;
        if (f == null) {
            return null;
        }
        int i = f.lastIndexOf('.');
        if (i > 1) {
            ext = f.substring(0, i);
            return ext;
        } else {
            return f;
        }
      }

    ///////////////////////////////////////////////////////////////////
    // String Utilities
    //
    static String removeCommas(String input)
      {
        String strA = "";
        StringCharacterIterator iter = new StringCharacterIterator(input);
        for (char current_char = iter.first();
            current_char != CharacterIterator.DONE;
            current_char =
                iter.next()) {
            if (!Character.isLetterOrDigit(current_char)) {
                continue;
            }
            strA += current_char;
        }
        return strA;
      }

    //-----------------------------------------------------------
    //
    public static String removeTrailingNewlines(String line)
      {
        // remove trailing newlines
        int cutoffPoint = line.length();
        while ((cutoffPoint > 0) && ((line.charAt(cutoffPoint - 1) == 10) || (line.charAt(cutoffPoint - 1) == 13))) {
            cutoffPoint--;
        }
        return line.substring(0, cutoffPoint);
      }

    /////////////////////////////////////////////////////////////////////////
    // checkDiskSpace
    //
//
//    public static void checkDiskSpace() {
//        // default warning at < 100MB
//        long warn = Prefs.usr.getInt("dataDiskWarn", 100);
//        long diskAvail = getDiscSpace() / 1000000;
//        if (diskAvail < warn) {
//            DialogBoxI.boxNotify("Disk Space Warning",
//                    "Available disk space less than " +
//                    String.valueOf(warn) + " MB");
//        }
//    }

    //----------------------------------------------------------------
    //----------------------------------------------------------------
    //
    public static void fileTest(String fileStr)
      {
        try {
            File f = new File(fileStr);
            long d;
            System.out.println("getName()          = " + f.getName());
            System.out.println("getAbsoluteFile().getName() = " + f.getAbsoluteFile().getName());
            boolean exists = f.exists();
            System.out.println("exists()           = " + exists);
//            if (!exists) {
//                System.exit(1);
//            }
            System.out.println("canRead()          = " + f.canRead());
            System.out.println("canWrite()         = " + f.canWrite());
            System.out.println("getPath()          = " + f.getPath());
            System.out.println("getAbsolutePath()  = " + f.getAbsolutePath());
            System.out.println("getCanonicalPath() = " + f.getCanonicalPath());
            System.out.println("getAbsoluteFile()  = " + f.getAbsoluteFile());
            System.out.println("toURL()            = " + f.toURL());
            System.out.println("toURI()            = " + f.toURI());
            System.out.println("getParent()        = " + f.getParent());
            System.out.println("isAbsolute()       = " + f.isAbsolute());
            boolean isDirectory = f.isDirectory();
            System.out.println("isDirectory()      = " + isDirectory);
            System.out.println("isFile()           = " + f.isFile());
            System.out.println("isHidden()         = " + f.isHidden());
            System.out.println("lastModified()     = " + (d = f.lastModified()) + " = " + new Date(d));
            System.out.println("length()           = " + f.length());
            if (isDirectory) {
                String[] subfiles = f.list();
                for (int i = 0; i < subfiles.length; i++) {
                    System.out.println("file in this dir   = " + subfiles[i]);
                }
            }
        } catch (IOException iox) {
            System.err.println(iox);
        }
      }

    //--------------------------------------------------------------
    //
    public static class AlphabeticComparator
        implements Comparator {

        public int compare(Object o1, Object o2)
          {
            String s1 = (String) o1;
            String s2 = (String) o2;
            return s1.toLowerCase().compareTo(s2.toLowerCase());
          }
    }

    public static class ChronoComparator
        implements Comparator {

        private final Date d1 = new Date();
        private final Date d2 = new Date();

        public int compare(Object one, Object two)
          {
            d1.setTime(((File) one).lastModified());
            d2.setTime(((File) two).lastModified());
            return d1.compareTo(d2);
          }
    }

    /**
     * Write a string to a file.
     * @param aFileName The name of the file to write to.
     * @param aValue The data to write to the file.
     * @param aAppend If true append to the end of the file. If false,
     * overwrite the file.
     * @param aNewLine A flag to indicate if an End Of Line marker should
     * be added at the end of the file.
     */
    public static void write(String aFileName, String aValue,
        boolean aAppend,
        boolean aNewLine) throws IOException
      {
        FileOutputStream fos = new FileOutputStream(aFileName, aAppend);
        PrintStream outputFile = new PrintStream(fos);
        if (aNewLine) {
            outputFile.println(aValue);
        } else {
            outputFile.print(aValue);
        }
        outputFile.close();
        fos.close();
      } // write

    /**
     * @see #write(String aFileName, String aValue, boolean aAppend, boolean aNewLine)
     */
    public static void write(String aFileName, String aValue, boolean aAppend) throws IOException
      {
        write(aFileName, aValue, aAppend, false);
      }

    /**
     * @see #write(String aFileName, String aValue, boolean aAppend, boolean aNewLine)
     */
    public static void write(String aFileName, String aValue) throws IOException
      {
        write(aFileName, aValue, false, false);
      }

    /**
     * @see #write(String aFileName, String aValue, boolean aAppend, boolean aNewLine)
     */
    public static void writeln(String aFileName, String aValue, boolean aAppend) throws IOException
      {
        write(aFileName, aValue, aAppend, true);
      }

    /**
     * @see #write(String aFileName, String aValue, boolean aAppend, boolean aNewLine)
     */
    public static void writeln(String aFileName, String aValue) throws IOException
      {
        write(aFileName, aValue, false, true);
      }

    /**
     * Read a file.
     * @param aFileName The name of the file to read.
     * @return The contents of the file as a string.
     */
    public static String read(String aFileName) throws IOException
      {
        File f = new File(aFileName);
        FileReader fr = new FileReader(f);
        char[] buf = new char[(int) f.length()];
        fr.read(buf, 0, (int) f.length());
        fr.close();
        return new String(buf);
      } // read

    /**
     * Read a binary file.
     * @param aFileName The name of the file to read.
     * @return The contents of the file as an array of bytes.
     */
    public static byte[] readBinary(String aFileName) throws IOException
      {
        File f = new File(aFileName);
        FileInputStream fis = new FileInputStream(f);
        BufferedInputStream in = new BufferedInputStream(fis);
        int len = (int) f.length();
        byte[] buf = new byte[len];
        in.read(buf, 0, len);
        in.close();
        return buf;
      }

    /**
     * Returns the length of the file.
     * @param aFileName The name of the file.
     * @return The length.
     */
    public static long size(String aFileName) throws IOException
      {
        File f = new File(aFileName);
        return f.length();
      }

    /**
     * Tests if the file exists.
     */
    public static boolean exists(String aFileName)
      {
        File f = new File(aFileName);
        return f.exists();
      } // exists
    static final String alphaLowerCase = "abcdefghijklmnopqrstuvwxyz";

    public static String nextUniqueLetterFilename(String path)
      {
        // assures unique flename by appending -a, -b, -c�
        String ext = getFileExtension(path);
        String noExt = removeExtension(path);
        String temp = path;
        int i = -1;
        while (FileUtil.exists(temp)) {
            i++;
            temp = noExt + "-" + alphaLowerCase.charAt(i) + ext;
        }
        return temp;
      }

    /**
     * @return The current user's directory
     */
    public static String userDirectory()
      {
        return System.getProperty("user.dir") +
            System.getProperty("file.separator");
      } // currentDirectory

    /**
     * Create a new directory
     * @param aDirectory The directory's name
     * @return true if succesful
     */
    public static boolean makeDir(String aDirectory)
      {
        return new File(aDirectory).mkdirs();
      } // makeDir()

    /**
     * Rename a file to a new name
     * @param aOld old file name
     * @param aNew new file name
     * @return true if succesful
     */
    public static boolean rename(String aOld, String aNew)
      {
        return new File(aOld).renameTo(new File(aNew));
      } // rename()

    /**
     * Delete a file
     * @param aName
     * @return true if succesful
     */
    public static boolean delete(String aName)
      {
        return new File(aName).delete();
      } // delete

    /**
     * Delete files fom aDirecotry, where file name starts with sFileNamePrefix.
     */
    public static boolean delete(String aDirectory, String aFileNamePrefix)
      {
        File dir = new File(aDirectory);
        File[] files = dir.listFiles();
        if (files == null) {
            return false;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().startsWith(aFileNamePrefix)) {
                files[i].delete();
            }
        }
        return true;
      } // delete()

    /**
     * Extract the extension from the file name, e.g. from pic1.jpg return jpg.
     * Return empty string if none found.
     */
    public static String getExtension(String aFileName)
      {
        return StringUtility.substringAfterLast(aFileName, ".");
      } // getExtension()

    public static int indexOfLastSlash(final String aFileWithPath)
      {
        // get the index of the last \ or /
        int p1 = aFileWithPath.lastIndexOf('\\');
        int p2 = aFileWithPath.lastIndexOf('/');
        return Math.max(p1, p2);
      }

    /**
     * Extract the parent directory from a path.
     * @param aFileWithPath A full path.
     * @return The parent directory.<br>
     * Example: getDirectory("a/b/c.d") will return "a/b/"
     */
    public static String getDirectory(String aFileWithPath)
      {
        int p = indexOfLastSlash(aFileWithPath);
        if (p == -1) {
            return StringUtility.EMPTY;
        }
        return StringUtility.left(aFileWithPath, p + 1);
      } //  getDirectory()

    /**
     * Extract the file name from a path
     * @param aFileWithPath String A full path.
     * @return String The parent directory.<br>
     * Example: getFileName("a/b/c.d") will return "c.d"
     */
    public static String getFileName(String aFileWithPath)
      {
        int p = indexOfLastSlash(aFileWithPath);
        if (p == -1) {
            return aFileWithPath;
        }
        return StringUtility.substring(aFileWithPath, p + 1);
      }

    /**
     * Replace back slashs with forward slashs
     * @param aPath a Path to convert
     * @return The converted path.
     */
    public static String replaceBackslash(final String aPath)
      {
        return aPath.replace('\\', '/');
      }

    /**
     * Get a list of all files and directories at a given path (not recursive).
     * @param aPath
     * @return a list of file names.
     */
    public static ListOfString listAll(final String aPath)
      {
        File path = new File(aPath);
        String[] files = path.list();
        ListOfString r = new ListOfString();
        if (files != null) {
            r.setArray(files);
        }
        return r;
      } // listAll

    /** Make sure aPath ends with a slash except if it is blank */
    public static String cleanPathEnd(final String aPath)
      {
        if (StringUtility.isBlank(aPath)) {
            return StringUtility.EMPTY;
        }
        char c = StringUtility.lastChar(aPath);
        if (c == '/' || c == '\\') {
            return aPath;
        }
        return aPath + '/';
      } // cleanPathEnd

    private static void listToBuf(final ListOfString aBuf,
        String aPath,
        final boolean aIsIncludeFiles,
        final boolean aIsIncludeDirs,
        final boolean aIsListFullName,
        final boolean aIsIncludeSubDirs)
      {
        aPath = cleanPathEnd(aPath);
        ListOfString all = listAll(aPath);
        for (int i = 0, n = all.size(); i < n; i++) {
            String fn = all.getItem(i);
            String full = aPath + fn;
            File file = new File(full);
            boolean isDir = file.isDirectory();
            if (aIsIncludeDirs && isDir) {
                aBuf.add(aIsListFullName ? full : fn);
            } else if (aIsIncludeFiles && file.isFile()) {
                aBuf.add(aIsListFullName ? full : fn);
            }
            if (aIsIncludeSubDirs && isDir) {
                listToBuf(aBuf, full, aIsIncludeFiles, aIsIncludeDirs,
                    aIsListFullName, aIsIncludeSubDirs);
            }
        }
      } // listToBuf

    /**
     * List the files and directories at a given path.
     * @param aPath The starting path
     * @param aIncludeFiles Include files in the listing.
     * @param aIncludeDirs Include directories in the listing.
     * @param aListFullName List the full path name of the file or directory.
     * @param aIncludeSubDirs List, recursivly, subdirectories found.
     * @return a list of all found files and directories that match the given condition.
     */
    public static ListOfString list(final String aPath,
        final boolean aIsIncludeFiles,
        final boolean aIsIncludeDirs,
        final boolean aIsListFullName,
        final boolean aIsIncludeSubDirs)
      {
        ListOfString r = new ListOfString();
        listToBuf(r, aPath, aIsIncludeFiles, aIsIncludeDirs,
            aIsListFullName,
            aIsIncludeSubDirs);
        return r;
      } // list


    // requires jdk >= 1.4
    /**
     * Requires jdk >= 1.4
     * List the files and directories at a given path.
     * @param aPath The starting path
     * @param aIncludeFiles Include files in the listing.
     * @param aIncludeDirs Include directories in the listing.
     * @param aListFullName List the full path name of the file or directory.
     * @param aIncludeSubDirs List, recursivly, subdirectories found.
     * @return a list of all found files and directories that match the given condition.
     */
    public static ListOfString list(final String aPath,
        final String aFilter,
        final boolean aIsIncludeFiles,
        final boolean aIsIncludeDirs,
        final boolean aIsListFullName,
        final boolean aIsIncludeSubDirs)
      {
        ListOfString all = list(aPath, aIsIncludeFiles, aIsIncludeDirs,
            aIsListFullName, aIsIncludeSubDirs);
        ListOfString r = new ListOfString();
        Pattern pat = Pattern.compile(aFilter);
        for (int i = 0, n = all.size(); i < n; i++) {
            String fn = getFileName(all.getItem(i));
            if (pat.matcher(fn).matches()) {
                r.add(fn);
            }
        }

        return r;
      }

    /**
     * Create the complete directory structure
     * for a complete FOLDER pathname.
     */
    public static String createDirectoryTreeForFolder(String folder) throws IOException
      {
        if (folder == null) {
            throw new IOException("The requested folder is null!");
        }

        if (!folder.endsWith(File.separator)) {
            folder += File.separator;
        }

        File f = new File(folder);
        if (!f.exists()) {
            boolean result = f.mkdirs(); // build all required directories!
            if (result == false) {
                throw new IOException("Cannot create folder structure for: " +
                    folder);
            }
        } else if (!f.isDirectory()) {
            throw new IOException("Invalid directory/Cannot create directory: " +
                folder);
        } else {
            /* the folder already exists */
        }

        return (folder);
      }

    /**
     * Create the complete directory structure
     * for a complete FILE pathname.
     */
    public static void createDirectoryTreeForFile(String file) throws IOException
      {
        if (file == null) {
            throw new IOException("null file requested!");
        }

        String path = null;
        int sep = file.lastIndexOf(File.separatorChar);
        if (sep == -1) {
            path = file;
        } else {
            path = file.substring(0, sep);
        }

        createDirectoryTreeForFolder(path);
      }

    /**
     * Determine if two filename paths refer to the same file
     *
     * See: http://javaalmanac.com/egs/java.io/Canonical.html
     */
    public static boolean isTheSameFile(String pathname1, String pathname2) throws IOException
      {
        File file1 = new File(pathname1);
        File file2 = new File(pathname2);

        // Normalize the paths
        file1 = file1.getCanonicalFile();
        file2 = file2.getCanonicalFile();

        return (file1.equals(file2));
      }

    /**
     * Determine if a file or directory exists
     */
    public static boolean fileOrDirectoryExists(String pathname)
      {
        File f = new File(pathname);
        return (f.exists());
      }

    /**
     * Determine if a file exists
     */
    public static boolean isFile(String pathname)
      {
        File f = new File(pathname);
        return (f.isFile());
      }

    /**
     * Determine if a directory exists
     */
    public static boolean isDirectory(String pathname)
      {
        File f = new File(pathname);
        return (f.isDirectory());
      }

    /**
     * Save a string to a text file
     */
    public static void saveTxtFile(String pathname, String data, boolean append) throws IOException
      {
        saveTxtFile(new File(pathname), data, append);
      }

    /**
     * Save a string to a text file
     */
    public static void saveTxtFile(File f, String data, boolean append) throws IOException
      {
        BufferedWriter out = new BufferedWriter(new FileWriter(f, append));
        out.write(data);
        out.close();
      }

    /**
     * Read a text file into a string
     */
    public static String readTxtFile(String pathname) throws IOException
      {
        return (readTxtFile(new File(pathname)));
      }

    /**
     * Read a text file into a string
     */
    public static String readTxtFile(File f) throws IOException
      {
        BufferedReader in = new BufferedReader(new FileReader(f));
        String result = "";
        String str = null;
        while ((str = in.readLine()) != null) {
            result += str;
            result += "\n";
        }
        in.close();
        return (result);
      }

    /**
     * Deletes all files and subdirectories under dir.
     * Returns true if all deletions were successful.
     * If a deletion fails, the method stops attempting
     * to delete and returns false.
     */
    public static boolean deleteDirectory(File dir)
      {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(new File(dir, children[i]));
                if (!success) {
                    return (false);
                }
            }
        }
        // This is a file or an empty directory, so just delete it
        return (dir.delete());
      }

    /**
     * Deletes all files and subdirectories under dir.
     * Returns true if all deletions were successful.
     * If a deletion fails, the method stops attempting
     * to delete and returns false.
     */
    public static boolean deleteDirectory(String pathname)
      {
        return (deleteDirectory(new File(pathname)));
      }

    /**
     * Deletes a file.
     * Returns true if successful.
     */
    public static boolean deleteFileOLD(String fileName)
      {
        // A File object to represent the filename
        File f = new File(fileName);
        // Make sure the file or directory exists and isn't write protected
        if (!f.exists()) {
            throw new IllegalArgumentException("deleteFile: cannot delete, no such file or directory: " + fileName);
        }
        if (!f.canWrite()) {
            throw new IllegalArgumentException("deleteFile: cannot delete - write protected: " + fileName);
        }
        // If it is a directory, make sure it is empty
        if (f.isDirectory()) {
            String[] files = f.list();
            if (files.length > 0) {
                throw new IllegalArgumentException("Delete: directory not empty: " + fileName);
            }
        }
        // Attempt to delete it
        boolean success = f.delete();
        if (!success) {
            //throw new IllegalArgumentException("Delete: deletion failed");
        }
        return success;
      }

    /**
     * Deletes a file.
     * Returns true if successful.
     */
    public static boolean deleteFile(String fileName)
      {
        // A File object to represent the filename
        File f = new File(fileName);
        // Make sure the file or directory exists and isn't write protected
        if (!f.exists()) {
            System.err.println("deleteFile: cannot delete, no such file or directory: " + fileName);
            return false;
        }
        if (!f.canWrite()) {
            System.err.println("deleteFile: cannot delete - write protected: " + fileName);
            return false;
        }
        // If it is a directory, make sure it is empty
        if (f.isDirectory()) {
            String[] files = f.list();
            if (files.length > 0) {
                System.err.println("Delete: directory not empty: " + fileName);
                return false;
            }
        }
        // Attempt to delete it
        boolean success = f.delete();
        if (!success) {
            System.err.println("Delete: Delete failed!!: " + fileName);
            return false;
        //throw new IllegalArgumentException("Delete: deletion failed");
        }
        return success;
      }

    // --------------------------- Copy File -------------------------------------
    /**
     * Copies a file to another location/file
     * Returns true if file copied or
     * false if not (possibly the file existed and 'overwrite' was not set).
     */
    public static boolean copyFile(String fromName, String toName,
        boolean overwrite) throws IOException
      {
        if (fromName == null) {
            throw new IOException("source filename is null!");
        }

        if (toName == null) {
            throw new IOException("destination filename is null!");
        }

        File fromFile = new File(fromName);
        File toFile = new File(toName);

        return (copyFile(fromFile, toFile, overwrite));
      }

    /**
     * Copies a file to another location/file
     * Returns true if file copied or
     * false if not (possibly the file existed and 'overwrite' was not set).
     */
    public static boolean copyFile(File fromFile, File toFile,
        boolean overwrite) throws IOException
      {
        final int IO_BUF_SIZE = 1024 * 32;

        if (fromFile == null) {
            throw new IOException("source file is null!");
        }

        if (toFile == null) {
            throw new IOException("destination file is null!");
        }

        // make sure that source file exists
        if (!fromFile.exists()) {
            throw new IOException("no such source file: " +
                fromFile.getAbsoluteFile());
        }

        if (!fromFile.isFile()) {
            throw new IOException("can't copy directory: " +
                fromFile.getAbsoluteFile());
        }

        if (!fromFile.canRead()) {
            throw new IOException("source file is unreadable: " +
                fromFile.getAbsoluteFile());
        }

        if (toFile.isDirectory()) {
            toFile = new File(toFile, fromFile.getName());
        }

        if (toFile.exists()) {
            if (!toFile.canWrite()) {
                throw new IOException("destination file is unwriteable: " +
                    toFile.getAbsoluteFile());
            }

            // check if we should overwrite it
            if (!overwrite) {
                return (false);
            }
        } else {
            // if the file dosn't exist, check if the directory exists and is
            // writeable. If getParent() returns null, then the directory is the
            // current dir. so look up the user.dir system property to find out
            // what that is.
            String parent = toFile.getParent(); // the destination dir
            if (parent == null) { // if none use the current dir
                parent = System.getProperty("user.dir");
            }

            File dir = new File(parent); // convert it to a file
            if (!dir.exists()) {
                throw new IOException("destination directory doesn't exist: " + parent);
            }

            if (dir.isFile()) {
                throw new IOException("destination is not a directory: " + parent);
            }

            if (!dir.canWrite()) {
                throw new IOException("destination directory is unwritable: " + parent);
            }
        }

        // if we've gotten this far then everything is ok
        // so we copy the file one buffer of bytes at a time
        FileInputStream from = null; // Stream to read from source
        FileOutputStream to = null; // Stream to write to destination
        try {
            from = new FileInputStream(fromFile); // Create input stream
            to = new FileOutputStream(toFile); // Create output stream
            byte[] buffer = new byte[IO_BUF_SIZE]; // to hold file data
            int bytesRead;

            // read a chunk of bytes into the buffer then write them out
            // looping until we reach the EOF (when read() returns -1)
            // Note the combination of assignment and comparison in this
            // while loop. This is a common I/O programming idiom.
            while ((bytesRead = from.read(buffer)) != -1) { // read until EOF
                to.write(buffer, 0, bytesRead); // write
            }
        } catch (IOException e) {
            throw (e);
        } finally {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException ignored) {
                }
            }
            if (to != null) {
                try {
                    to.close();
                } catch (IOException ignored) {
                }
            }
        }
        return (true);
      }

    static void copy(File src, File dst) throws IOException
      {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
      }

    //\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\//

    //   public static void main (String[] args) {
//    fileTest(
//        "C:\\PSjData\\TestImageForPSj\\TestDec1\\image\\PS_03_1201_1033_06.tif");
//      String[] files = null;
//      String path =
//            "C:\\PSjData\\project1\\June 21\\series\\PS_04_0621_1501_10";
//      files = FileUtil.getFilesSortedAlpha(path, "P");
//      int n = files.length;
//
//      for (int i = 0; i < files.length; i++) {
//         System.out.println(files[i]);
//      }
//      System.out.println("done");
//   }

    //===========================================================================
// Project Image File Copier...
//
//   public static void main (String[] aArguments) throws FileNotFoundException {
//      String projectDir =
//            "C:\\PSjData\\LaFountain May 2005\\";
//      String dest = "E:\\LaFountainMay2005\\";
//      copyProjectImageFiles(projectDir, dest, false);
//   }
    /** test */
    public static void main(String[] args)
      {
//        File vol = new File("C:");
//        long diskAvail = vol.getFreeSpace();
//        System.out.println("Free space on C: = " + diskAvail);
//        //deleteFile("C:\\deleteMe.zip");
//        //System.out.println(String.valueOf(getDiscSpace()));
//        fileTest("C:/PSjData/");
       try {
          FileUtil.createDirectoryTreeForFile("C:/this/that/those/file.ext");
       } catch (IOException ex) {
          Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
       }

      }
}

//-----------------------------------------------------------
// FileCopyException class
//
class FileCopyException
    extends IOException {

    public FileCopyException(String msg)
      {
        super(msg);
      }
}
