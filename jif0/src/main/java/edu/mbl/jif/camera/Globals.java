package edu.mbl.jif.camera;

import edu.mbl.jif.camacq.CamAcq;

//import edu.mbl.jif.ijplugins.*;
import edu.mbl.jif.gui.ConsoleJava;
import edu.mbl.jif.utils.diag.mem.MemoryWatcher;


public class Globals
{

   private Globals () {}


   // deBug ------------------------------------------------------------
   public static boolean deBug = false;
   public static MemoryWatcher memWatcher;

   // Application information -----------------------------------------
   public final static String programTitle = "";
   public final static String version = "0.00  01-Jan-00";
   public static final String[] copyrightNotice = {
                                                  "Copyright 2003 by the Marine Biological Laboratory",
                                                  "All rights reserved worldwide."
   };
   public static final String[] patentNotice = {
                                               null};
   public static final String[] credits = {
                                          "Software design & engineering by Grant B. Harris"
   };

   // global constants ------------------------------------------------
   public static final int X = 100;
   public static final int Y = 200;

   // global "macro" --------------------------------------------------
   public static double perc (double a, double b) {
      return a * 100.0 / b;
   }


   public static boolean isDeBug () {
      return deBug;
   }


// User Preferences
   // System Preferences
   //   devices:
   //      ports
   //      instruments

// Icons -------------------------------------------------------------
//  public static ImageIcon PSJ_ICON_16 = loadImageIcon(".gif");

// Colors ------------------------------------------------------------
//  public static Color COLOR_CAMERA = new Color(135, 159, 199);

// Sounds ------------------------------------------------------------
//  public static SoundClip errorSoundClip = new SoundClip("chord.wav");
//  public static SoundClip clickSoundClip = new SoundClip("click.wav");

// Paths ------------------------------------------------------------
   public static String systemDrive = "C:";
   public static String systemRoot = "";

// Objects ----------------------------------------------------------
   public static ConsoleJava javaConsole = null;

  public static CamAcq camAcq = null;

  public static PanelDisplayController ctrlPanel = null;

//-------------------------------------------------------------------
   /*  public static ImageIcon loadImageIcon(String gifFile) {
       ImageIcon img = null;
       try {
         img = new ImageIcon(PSj.class.getResource("./icons/" + gifFile));
       }
       catch (Exception e) {
         System.out.println("Exception loading: " + gifFile);
       }
       if (img == null) {
         System.out.println("Could not load ImageIcon: " + gifFile);
       }
       return img;
     }
    */
   public static int frameInsetH = 8;
   public static int frameInsetV = 27;
   // +++ Get these from UI ???
   // [top=23,left=4,bottom=4,right=4]
   // InsetsUIResource
   // Insets bi = b.getBorder().getBorderInsets(b);

   public static ImageGetter imageGetter = null;

   public static void showMem (String msg) {
      System.out.println(" Mem: " +
                         String.valueOf(Runtime.getRuntime().freeMemory() /
                                        1000) +
                         "K / " +
                         String.valueOf(Runtime.getRuntime().totalMemory() /
                                        1000) +
                         "K  - " + msg);
   }
}
