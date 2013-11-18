
package edu.mbl.jif.gui.icon;

import java.net.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Utilities for getting Icons and related resources.
 * Icon file names are assumed to be rooted in a folder named "resources"
 * expected to be found in the classpath.<br>
 * Copyright (c) 2004, Superliminal Software
 * @author Melinda Green
 */
public class IconUtils {
    /**
     * @param name is either a fully qualified url path or a relative file path.
     * @return URL version of name if a fully qualified url path otherwise a url
     * relative to name with 'resources/' prepended and expected to be found in
     * the classpath.
     */
    public static URL getResource(String name) {
        if(name == null)
            return null;
        URL url = null;
        try {
            if(name.indexOf(':') == -1) {
                String path = "resources" + File.separatorChar + name; 
                // getResource always looks for forward slash separators.
                url = IconUtils.class.getClassLoader().getResource(path);
            }
            else
                url = new URL(name);
        }
        catch(Exception e){
            System.err.println("IconUtils.getResource: can't load resource: " + name);
        }
        return url;
    }

    
    public static Icon getIcon(String fname) {
        URL iurl = getResource(fname);
        if(iurl == null)
            return new LedIcon(Color.blue); // an error indication  
        return new ImageIcon(iurl);
    }

    public static void tryToSetIcon(String name, AbstractButton into) {
        URL iurl = getResource(name);
        if(iurl != null)
            into.setIcon(new ImageIcon(iurl));
    }

    public static void tryToSetIcon(String name, JFrame into) {
        URL iurl = getResource(name);
        if(iurl != null)
            into.setIconImage(new ImageIcon(iurl).getImage());
    }

    public static void tryToSetIcon(String name, JLabel into) {
        URL iurl = getResource(name);
        if(iurl != null)
            into.setIcon(new ImageIcon(iurl));
    }

    public static Image iconToImage(Icon icon) {
          if (icon instanceof ImageIcon) {
              return ((ImageIcon)icon).getImage();
          } else {
              int w = icon.getIconWidth();
              int h = icon.getIconHeight();
              GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
              GraphicsDevice gd = ge.getDefaultScreenDevice();
              GraphicsConfiguration gc = gd.getDefaultConfiguration();
              BufferedImage image = gc.createCompatibleImage(w, h);
              Graphics2D g = image.createGraphics();
              icon.paintIcon(null, g, 0, 0);
              g.dispose();
              return image;
          }
      }

    
    // TODO Sort these out...

    
    public Image getImage(String imageName, String url){
        String baseURL = url+"/";
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream ins;
        BufferedImage bi;
        ins =cl.getResourceAsStream(baseURL+imageName);
        try{
            bi = ImageIO.read(ins);
        }catch(Exception e){
            bi = null;
        }
        return bi;
    }

    public ImageIcon getImageIcon(String imageName, String url) {
        Image i = getImage(imageName, url);
        if(i==null)
            return null;
        else
            return new ImageIcon(getImage(imageName, url));
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
    
    private IconUtils() {} // disallows construction of utility class
    
    public static void main(String[] args) {
             loadImageIcon("Movie16.gif", "edu/mbl/jif/gui/imaging/player/icons");
   }
}

