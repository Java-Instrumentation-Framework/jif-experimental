package edu.mbl.jif.utils.classloaders;
/*
 * ClasspathHacker - Originally by Antoni Miguel, 2002
 */
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Useful class for dynamically changing the classpath, adding classes during runtime.
 * @author unknown
 */
public class ClassPathHacker {
    /**
     * Parameters of the method to add an URL to the System classes.
     */
    private static final Class[] parameters = new Class[]{URL.class};
    
    /**
     * Adds a file to the classpath.
     * @param s a String pointing to the file
     * @throws IOException
     */
    public static void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }
    
    /**
     * Adds a file to the classpath
     * @param f the file to be added
     * @throws IOException
     */
    public static void addFile(File f) throws IOException {
        addURL(f.toURL());
    }
    
    /**
     * Adds the content pointed by the URL to the classpath.
     * @param u the URL pointing to the content to be added
     * @throws IOException
     */
    public static void addURL(URL u) throws IOException {
        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL",parameters);
            method.setAccessible(true);
            method.invoke(sysloader,new Object[]{ u });
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }
    
    public static void initLibraries() {
        File libFolder = new File("lib");
        if (libFolder.exists() && libFolder.isDirectory()) {
            File[] files = libFolder.listFiles();
            for (int a = 0; a < files.length; a++) {
                if (files[a].isFile()
                && (files[a].getName().toLowerCase().endsWith(".jar"))) {
                    System.out.println("Added to classpath: " + files[a].getPath());
                    try {
                        ClassPathHacker.addFile(files[a]);
                    } catch (java.io.IOException io) {
                        io.printStackTrace();
                    }
                }
            }
        }
    }
    
    
    public static void main(String[] args) {
        
        System.out.println("classpath before " + System.getProperty("java.class.path"));
        ClassPathHacker.initLibraries();
//        try {
//            ClassPathHacker.addFile(new File("C:\\_dev\\__Dev06\\JIExplorer\\jiexplorer-1.14-src\\dist\\lib\\hsqldb.jar"));
//        } catch (IOException e) {
//            System.err.println("Error - " + e.toString());
//        }
        System.out.println("classpath after  " + System.getProperty("java.class.path"));
//        ClassPathHacker.initLibraries();
//        System.out.println("classpath after  " + System.getProperty("java.class.path"));
        
        
    }
    
/*    public class ClassPathAdder {
	public static void main(String[] args) {
		try {
			ClassPathHacker.add(new File("/home/user/newClassPath"));
		} catch (IOException e) {
			System.err.println("Error - " + e.toString());
		}
	}
}
 */
    
    
}