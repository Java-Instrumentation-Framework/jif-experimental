/*
 * FileSet.java
 *
 * Created on May 24, 2007, 2:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.utils.file;

import java.io.*;

import java.text.*;

import java.util.*;
import java.util.regex.*;


/** This class is part of the
<A HREF=http://www.mpi-inf.mpg.de/~suchanek/downloads/javatools target=_blank>
          Java Tools
</A> by <A HREF=http://www.mpi-inf.mpg.de/~suchanek target=_blank>
          Fabian M. Suchanek</A>
  You may use this class if (1) it is not for commercial purposes,
  (2) you give credit to the author and (3) you use the class at your own risk.
  If you use the class for scientific purposes, please cite our paper
  "Combining Linguistic and Statistical Analysis to Extract Relations from Web Documents"
  (<A HREF=http://www.mpi-inf.mpg.de/~suchanek/publications/kdd2006.pdf target=_blank>pdf</A>,
  <A HREF=http://www.mpi-inf.mpg.de/~suchanek/publications/kdd2006.bib target=_blank>bib</A>,
  <A HREF=http://www.mpi-inf.mpg.de/~suchanek/publications/kdd2006.ppt target=_blank>ppt</A>
  ). If you would like to use the class for commercial purposes, please contact
  <A HREF=mailto:f.m.suchanek@web.de>Fabian M. Suchanek</A><P>

  The class represents a set of files as given by a wildcard string.
  It does not include folders and is not case-sensitive.<BR>
   Example:
   <PRE>
         for(File f : new FileSet("c:\\myfiles\\*.jAvA"))
                 System.out.println(f);
         -->
             c:\myfiles\FileSet.java
             c:\myfiles\HTMLReader.java
             ...
   </PRE>
*/
public class FileSet extends ArrayList<File> {
    /** Constructs a file from a folder, subfolder names and a filename */
    public static File file(File f, String... s) {
        for (String n : s) {
            f = new File(f, n);
        }
        return (f);
    }

    /** Constructs a FileSet from a wildcard string (including path) */
    public FileSet(String s) {
        this(new File(s));
    }

    /** Constructs a FileSet from a wildcard string (including path) */
    public FileSet(File f) {
        this((f.getParentFile() == null) ? new File(".") : f.getParentFile(), f.getName());
    }

    /** Constructs a FileSet from a wildcard string */
    public FileSet(File path, String fname) {
        String regex = "";
        for (int i = 0; i < fname.length(); i++) {
            switch (fname.charAt(i)) {
            case '.':
                regex += "\\.";
                break;

            case '?':
                regex += '.';
                break;

            case '*':
                regex += ".*";
                break;

            default:
                regex += ("" + '[' + Character.toLowerCase(fname.charAt(i)) +
                Character.toUpperCase(fname.charAt(i)) + ']');
            }
        }
        final Pattern wildcard = Pattern.compile(regex);
        File[] files = path.listFiles(
                    new FileFilter() {
                        public boolean accept(File pathname) {
                            return (wildcard.matcher(pathname.getName()).matches());
                        }
                    });

        // Stupid, but the internal array is inaccessible
        if (files == null) {
            throw new RuntimeException("Can't find files in " + path);
        }
        ensureCapacity(files.length);
        for (File f1 : files)
            add(f1);
    }

    /** Exchanges the extension of a filename */
    public static File newExtension(File f, String newex) {
        return (new File(newExtension(f.getPath(), newex)));
    }

    /** Exchanges the extension of a filename */
    public static String newExtension(String f, String newex) {
        // Extension may be given with preceding dot or without
        if (newex.startsWith(".")) {
            newex = newex.substring(1);
        }
        int i = f.lastIndexOf('.');

        // If the task is to delete the extension...
        if (newex.length() == 0) {
            if (i == -1) {
                return (f);
            }
            return (f.substring(0, i));
        }

        // Else add or replace the extension
        if (i == -1) {
            return (f + '.' + newex);
        }
        return (f.substring(0, i) + '.' + newex);
    }

    /** Test routine */
    public static void main(String[] argv) {
        //    D.p("Enter a filename with wildcards and hit ENTER. Press CTRL+C to abort");
        //    while(true) {
        //      for(File f: new FileSet(D.r())) D.p(f);
        //    }
    }
}
