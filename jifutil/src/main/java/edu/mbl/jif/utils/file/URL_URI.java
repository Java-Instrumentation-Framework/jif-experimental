/*
 * URL_URI.java
 * Created on February 16, 2007, 1:19 PM
 */

package edu.mbl.jif.utils.file;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author GBH
 */
public class URL_URI {
    
    /** Creates a new instance of URL_URI */
    public URL_URI() {
        
    }
    
    
    static File getInstallationDirectory() {
        java.net.URL url = URL_URI.class.getResource("URL_URI.class");
        java.net.URI uri = java.net.URI.create(url.toString());
        String scheme = uri.getScheme();
        File f = null;
        if (scheme.equals("file")) {
            // Go up twice for jude/Main.class
            f = new File(uri.getPath()).getParentFile().getParentFile();
        } else if (scheme.equals("jar")) {
            String s = uri.getSchemeSpecificPart();
            int pos = s.lastIndexOf('!'); // ! marks beginning of jar entry
            s = s.substring(5, pos); // 5 is the length of "file:"
            f = new File(s).getParentFile();
        }
        
        return f;
    }
    
    // Building Absolute/Relative URIs
// how to build absolute/relative URIs by using the java.net.URI methods.
    
    //Building an absolute URI:
    
    public static void buildAbsoluteURI() {
        try{
            URI uri_absolute=new URI("http://www.java.sun.com/");
            URI uri_relative=new URI("index.html");
            URI uri_absolute_result=uri_absolute.resolve(uri_relative);
            System.out.println(uri_absolute_result);
        }catch(URISyntaxException e) {
            System.out.println(e.getMessage());}
        
        // The result:
        //http://www.java.sun.com/index.html
    }
    
    //Building a relative URI:
    
    public static void buildRelativeURI() {
        try{
            URI uri=new URI("/docs/imagini/mare/");
            URI uri_relative=new URI("eforie/discoteca.jpg");
            URI uri_relative_result=uri.resolve(uri_relative);
            System.out.println(uri_relative_result);
        }catch(URISyntaxException e) {
            System.out.println(e.getMessage());}
        
        // The result:
        // /docs/imagini/mare/eforie/discoteca.jpg
    }
    
    //Getting a relative URI:
    public static void getRelativeURI() {
        
        try{
            URI uri_absolute_1=new URI("http://www.java.sun.com/index.html");
            URI uri_absolute_2=new URI("http://www.java.sun.com/");
            // relativize !!
            URI uri_relative_result=uri_absolute_2.relativize(uri_absolute_1);
            System.out.println(uri_relative_result);
        }catch(URISyntaxException e) {
            System.out.println(e.getMessage());}
        
        //Result:
        // index.html
    }
    public static void main(String[] args) {
        buildRelativeURI();
        getRelativeURI();
    }
    
}
