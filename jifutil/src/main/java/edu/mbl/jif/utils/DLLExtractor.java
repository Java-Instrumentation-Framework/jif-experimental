/*
 * DLLExtractor.java
 */
package edu.mbl.jif.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.security.CodeSource;
import java.security.ProtectionDomain;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Extracts a .dll from a .jar and writes it to the file system so that
 * JNI-dependent classes can access it.
 * @author GBH
 */
public class DLLExtractor {
    /** Creates a new instance of DLLExtractor */
    public DLLExtractor() {
    }
    
    public static void extractDLL() throws IOException {
        // install .dll from .jar file
        
        //        String system = System.getProperty("os.name");
        //        String libExtension =".dll";
        ////   "Windows".equals(system) ? ".dll" :
        ////   "Unix".equals(system) ? ".so"
        ////...;
        //        String mylibName = "myLibrary" + libExtension;
        //        URL libUrl = ResourceLocator.class.getResource(mylibName);
        //        File file = new File(mylibName);
        //        if (!file.exists())
        //            file.createNewFile();
        //        FileReader in = new FileReader(new File(libUrl.getFile()));
        //        FileWriter out = new FileWriter(file);
        //        byte[] buffer = new byte[1048];
        //        while(in.available() > 0) {
        //            int read = in.read(buffer);
        //            out.write(buffer, 0, read);
        //        }
        //        out.close();
        //        System.loadLibrary("myLibrary");
        InputStream inputStream = DLLExtractor.class.getResourceAsStream("/mydll.dll");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("temporaryDll");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        byte[] array = new byte[8192];
        for (int i = inputStream.read(array); i != -1; i = inputStream.read(array))
            outputStream.write(array, 0, i);
        outputStream.close();
    }
    
    /*
     System.load(getClass().getResource("/theDLL.dll").toString());
     
    Another solution is to extract the dll from JAR and
     1) install under %USER_HOME% and
     then load it via System.load() method with the full path, or
     2) install it to one path included in java.library.path
     and load it via System.loadLibrary() method with the dll name.
     *
     **/
    private boolean setupDlls(String jarName, String dllName) {
        String jarDir = ".";
        try {
            JarFile jar = new JarFile(jarDir + "/" + jarName);
            Enumeration<JarEntry> jarEntries = jar.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry entry = jarEntries.nextElement();
                // if (entry.getName().matches("[^(org)(edu)(src)].+\\.dll")) {
                if (entry.getName().matches(dllName)) {
                    File dll = new File(jarDir + "/" + entry.getName());
                    FileOutputStream out = new FileOutputStream(dll);
                    //dll.deleteOnExit();
                    InputStream dllFile = jar.getInputStream(entry);
                    int c;
                    while ((c = dllFile.read()) != -1) {
                        out.write(c);
                    }
                    out.close();
                    dllFile.close();
                }
            }
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public void andAnotherApproach(String jarName, String dllName) {
        //    DummyClass app = new DummyClass();
        //    Class cls = app.getClass();
        Class cls = this.getClass();
        ProtectionDomain pDomain = cls.getProtectionDomain();
        CodeSource cSource = pDomain.getCodeSource();
        URL loc = cSource.getLocation();
        
        //System.out.println( "Loc:\t" + loc.toString() );
        //System.out.println( "Starting pathUpdate." );
        //System.setProperty( "java.library.path", System.getProperty( "java.library.path" ) + ";"
        //		+ loc.toString().replaceAll( "JTest.jar", "" ).replaceAll( "file:/", "" ).replaceAll( "%20", " " ).replaceAll( "/", "\\" ) );
        //System.out.println( "pathUpdated." );
        try {
            File outFile = new File(loc.toString().replaceAll(jarName, dllName)
            .replaceAll("file:", "").replaceAll("%20", " "));
            if (!outFile.exists()) {
                //outFile.deleteOnExit();
                File inFile = new File(loc.toString().replaceAll("file:", "").replaceAll("%20", " "));
                FileOutputStream out = new FileOutputStream(outFile);
                JarFile jar = new JarFile(inFile);
                InputStream dllFile = jar.getInputStream(jar.getEntry(dllName));
                int c;
                while ((c = dllFile.read()) != -1) {
                    out.write(c);
                }
                out.close();
                dllFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        (new DLLExtractor()).andAnotherApproach("all.jar", "QCamJNI.dll");
        (new DLLExtractor()).setupDlls("all.jar", "QCamJNI.dll");
    }
}
