/*
 * FindJarForClass.java
 *
 * Created on February 24, 2007, 1:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.utils.classloaders;

/*
 Locate the jar in the classpath for a given class
 This class has a single static method which when passed a class will 
 return the location of the Jar file that loaded that class. 
 Basically a refactoring of Laird Nelson's code from 
 http://weblogs.java.net/blog/ljnelson/archive/2004/09/cheap_hack_i_re.html
*/
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindJarForClass
{
  private FindJarForClass() {}

  public static String locate( Class c ) throws ClassNotFoundException
  {
    final URL location;
    final String classLocation = c.getName().replace('.', '/') + ".class";
    final ClassLoader loader = c.getClassLoader();
    if( loader == null )
    {
      location = ClassLoader.getSystemResource(classLocation);
    }
    else
    {
      location = loader.getResource(classLocation);
    }
    if( location != null ) 
    {
      Pattern p = Pattern.compile( "^.*:(.*)!.*$" ) ;
      Matcher m = p.matcher( location.toString() ) ;
      if( m.find() )
        return m.group( 1 ) ;
      else
        throw new ClassNotFoundException( "Cannot parse location of '" + location + "'.  Probably not loaded from a Jar" ) ;
    }
    else 
      throw new ClassNotFoundException( "Cannot find class '" + c.getName() + " using the classloader" ) ;
  }
}
