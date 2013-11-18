package edu.mbl.jif.utils;

import java.net.URL;
import javax.swing.ImageIcon;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JifUtils {

    public JifUtils() {
    }

    public static ImageIcon loadImageIcon(String imageFile, String path) {
        ResourceManager rm = new ResourceManager(path);
        return rm.getImageIcon(imageFile);
    }
    
    public static ImageIcon loadImageIcon(String gifFile, Class clazz) {
        ImageIcon img = null;
        URL url = null;
        try {
            url = JWhich.findClass(clazz.getName());
            String urlS = url.toString();
            int lastSlash = urlS.lastIndexOf("/");
            String path = urlS.substring(0, lastSlash) + "/icons/" + gifFile;
            System.out.println(path);
            img = new ImageIcon(path);            
//            url = clazz.getResource("/icons/" + gifFile);
//            img = new ImageIcon(url);
        } catch (Exception e) {
            System.out.println("Exception loading: " + url);
        }
        if (img == null) {
            System.out.println("Could Not Load Image: " + gifFile + " for class: " + clazz.getName() + " from URL: " + url);
        }
        return img;
    }

    public synchronized static void waitFor(int msecs) {
        try {
            Thread.sleep(msecs);
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
       // loadImageIcon("Movie16.gif", "edu/mbl/jif/gui/imaging/player/icons");
        loadImageIcon("test.gif", JifUtils.class);
    }
}
