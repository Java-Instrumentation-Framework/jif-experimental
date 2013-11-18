/*
 * DirWithRegExp.java
 *
 * Created on May 14, 2007, 10:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.utils.file;
import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Usage:
 * java DirWithRegExp . "*.java"
 *
 * Note: The pattern string must be quoted as platforms expand
 * wildcards before they get to program.
 */

public class DirWithRegExp {
    static int indentLevel = -1;
    
    static void listPath(File path, FileFilter filter) {
        File files[];  // list of files in a directory
        indentLevel++; // going down...
        // create list of files in this dir
        files = path.listFiles(filter);
        // Sort with help of Collections API
        Arrays.sort(files);
        for (int i=0, n=files.length; i < n; i++) {
            for (int indent=0; indent < indentLevel; indent++) {
                System.out.print("    ");
            }
            if (files[i].isDirectory()) {
                System.out.println(files[i].toString());
                // recursively descend dir tree
                listPath(files[i], filter);
            } else {
                processFile(files[i]);
            }
        }
        indentLevel--; // and going up
    }
    
    public static void processFile(File file) {
        System.out.println(file.toString() + " <<<<<< FILE");
        
    }
    public static void main(String args[]) {
        // Use current directory if none specified
//    String path = (args.length >=1 ? args[0] : ".");
        // If no wildcard specified, pass along null
//    FileFilter filter =
//      (args.length >= 2 ? new PatternFileFilter(args[1]) : null);
        FileFilter filter = new PatternFileFilter("PS*.tif");
        listPath(new File("\\PSjData"), filter);
    }
    
    /**
     * Provies a FileFilter that uses Regular Expression pattern
     * matching to restrict the set of files in the output
     * Auto-converts wildcard "*" to ".*" to match via patterns.
     */
    static class PatternFileFilter implements FileFilter {
        Pattern pattern;
        public PatternFileFilter(String pattern) {
            //
            pattern = pattern.replaceAll("\\*", ".*");
            this.pattern = Pattern.compile(pattern);
        }
        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                return true;
            } else {
                return pattern.matcher(pathname.getName()).matches();
            }
        }
    }
}