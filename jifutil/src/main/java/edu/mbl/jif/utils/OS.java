package edu.mbl.jif.utils;

/*
 * OperatingSystem.java - OS detection
 * Copyright (C) 2002 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */


import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.UIManager;
import java.io.File;


/**
 * Operating system detection routines.
 * @author Slava Pestov
 * @version $Id: OperatingSystem.java,v 1.8 2003/02/04 01:19:51 spestov Exp $
 * @since jEdit 4.0pre4
 */
public class OS
{
   public static final Rectangle getScreenBounds () {
      int screenX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
      int screenY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
      int x, y, w, h;

      if (isMacOS()) {
         x = 0;
         y = 22;
         w = screenX;
         h = screenY - y - 4; //shadow size
      }
      else if (isWindows()) {
         x = -4;
         y = -4;
         w = screenX - 2 * x;
         h = screenY - 2 * y;
      }
      else {
         x = 0;
         y = 0;
         w = screenX;
         h = screenY;
      }

      return new Rectangle(x, y, w, h);
   }


   //{{{ isDOSDerived() method
   /**
    * Returns if we're running Windows 95/98/ME/NT/2000/XP, or OS/2.
    */
   public static final boolean isDOSDerived () {
      return isWindows() || isOS2();
   } //}}}


   //{{{ isWindows() method
   /**
    * Returns if we're running Windows 95/98/ME/NT/2000/XP.
    */
   public static final boolean isWindows () {
      return os == WINDOWS_9x || os == WINDOWS_NT;
   } //}}}


   //{{{ isWindows9x() method
   /**
    * Returns if we're running Windows 95/98/ME.
    */
   public static final boolean isWindows9x () {
      return os == WINDOWS_9x;
   } //}}}


   //{{{ isWindowsNT() method
   /**
    * Returns if we're running Windows NT/2000/XP.
    */
   public static final boolean isWindowsNT () {
      return os == WINDOWS_NT;
   } //}}}


   //{{{ isOS2() method
   /**
    * Returns if we're running OS/2.
    */
   public static final boolean isOS2 () {
      return os == OS2;
   } //}}}


   //{{{ isUnix() method
   /**
    * Returns if we're running Unix (this includes MacOS X).
    */
   public static final boolean isUnix () {
      return os == UNIX || os == MAC_OS_X;
   } //}}}


   //{{{ isMacOS() method
   /**
    * Returns if we're running MacOS X.
    */
   public static final boolean isMacOS () {
      return os == MAC_OS_X;
   } //}}}


   //{{{ isMacOSLF() method
   /**
    * Returns if we're running MacOS X and using the native look and feel.
    */
   public static final boolean isMacOSLF () {
      return (isMacOS() && UIManager.getLookAndFeel().isNativeLookAndFeel());
   } //}}}


   //{{{ isJava14() method
   /**
    * Returns if Java 2 version 1.4 is in use.
    */
   public static final boolean hasJava14 () {
      return java14;
   } //}}}


   //{{{ Private members
   private static final int UNIX = 0x31337;
   private static final int WINDOWS_9x = 0x640;
   private static final int WINDOWS_NT = 0x666;
   private static final int OS2 = 0xDEAD;
   private static final int MAC_OS_X = 0xABC;
   private static final int UNKNOWN = 0xBAD;

   private static int os;
   private static boolean java14;

   //{{{ Class initializer
   static {
      if (System.getProperty("mrj.version") != null) {
         os = MAC_OS_X;
      }
      else {
         String osName = System.getProperty("os.name");
         if (osName.indexOf("Windows 9") != -1
             || osName.indexOf("Windows M") != -1) {
            os = WINDOWS_9x;
         }
         else if (osName.indexOf("Windows") != -1) {
            os = WINDOWS_NT;
         }
         else if (osName.indexOf("OS/2") != -1) {
            os = OS2;
         }
         else if (File.separatorChar == '/') {
            os = UNIX;
         }
         else {
            os = UNKNOWN;
            System.err.println("Unknown operating system: " + osName);
         }
      }

      if (System.getProperty("java.version").compareTo("1.4") >= 0
          && System.getProperty("jedit.nojava14") == null) {
         java14 = true;
      }
   } //}}}

/**
    * Ensures Java runtime version e.g. 1.1.7 is sufficiently recent.
    * Based on code by Dr. Tony Dahlman <adahlman@jps.net>
    * from http://mindprod.com
    * @param wantedMajor
    *               java major version e.g. 1
    * @param wantedMinor
    *               Java minor version e.g. 1
    * @param wantedBugFix
    *               Java bugfix version e.g. 7
    *
    * @return true if JVM version running is equal to or more recent than
    *               (higher than) the level specified.
    */
   
   public static boolean isJavaVersionOK( int wantedMajor, int wantedMinor, int wantedBugFix)
      {
      try
         {
         try
            {
            // java.version will have form 1.1.7A, 11, 1.1., 1.1 or 1.3beta or 1.4.1-rc
            // It may be gibberish. It may be undefined.
            // We have do deal with all this malformed garbage.
            // Because incompetents run the world,
            // it is not nicely formatted for us in three fields.
            String ver = System.getProperty("java.version");

            if ( ver == null )
               {
               return false;
               }

            ver = ver.trim();

            if ( ver.length() < 2 )
               {
               return false;
               }

            int dex = ver.indexOf('.');

            if ( dex < 0 )
               {
               // provide missing dot
               ver = ver.charAt(0) + '.' + ver.substring(1);
               dex = 1;
               }

            int gotMajor = Integer.parseInt(ver.substring(0, dex));

            if ( gotMajor < wantedMajor ) return false;
            if ( gotMajor > wantedMajor ) return true;

            // chop off major and first dot.
            ver = ver.substring( dex + 1 );

            // chop trailing "beta"
            if ( ver.endsWith("beta") )
               {
               ver = ver.substring(0, ver.length() - "beta".length());
               }
            // chop trailing "-rc"
            if ( ver.endsWith("-rc") )
               {
               ver = ver.substring(0, ver.length() - "-rc".length());
               }
            // chop any trailing letter as in 1.1.7A,
            // but convert 1.1.x or 1.1.X to 1.1.9
            char ch = ver.charAt(ver.length()-1);
            if ( ! Character.isDigit(ch) )
               {
               ver = ver.substring(0, ver.length()-1);
               if ( ch == 'x' || ch == 'X' ) ver += '9';
               }
            // check minor version
            dex = ver.indexOf('.');
            if ( dex < 0 )
               {
               // provide missing BugFix number as in 1.2 or 1.0
               ver += ".0";
               dex = ver.indexOf('.');
               }

            int gotMinor = Integer.parseInt( ver.substring(0, dex) );
            if ( gotMinor < wantedMinor )  return false;
            if ( gotMinor > wantedMinor )  return true;
            // was equal, need to examine third field.
            // check bugfix version
            ver = ver.substring( dex + 1 );
            int gotBugFix = Integer.parseInt( ver );
            return( gotBugFix >= wantedBugFix );

            }
         catch ( NumberFormatException e )
            {
            return false;
            } // end catch

         }
      catch ( StringIndexOutOfBoundsException e )
         {
         return false;
         } // end catch

      } // end isJavaVersionOK

   
   public static void main (String[] args) {
      System.out.println("isDOSDerived: " + isDOSDerived());
      System.out.println("isWindowsNT: " + isWindowsNT());
      System.out.println("hasJava14: " + hasJava14());

   }
   //}}}
}
