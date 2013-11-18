package edu.mbl.jif.gui.dialog;

import edu.mbl.jif.gui.sound.SoundClip;
import edu.mbl.jif.utils.prefs.Prefs;
import java.awt.Component;
import javax.swing.*;

//import edu.mbl.jif.gui.sound.SoundClip;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DialogBoxI
{

   static Component defaultParent = null;

   public static void setDefaultParent (Component _parent) {
      defaultParent = _parent;
   }


   private DialogBoxI () {
   }


   /*
     showConfirmDialog Asks a confirming question, like yes/no/cancel.
     showInputDialog Prompt for some input.
     void showMessageDialog - something that has happened. (OK button)
     showOptionDialog The Grand Unification of the above three
     returns: int result =
     YES_OPTION
     NO_OPTION
     CANCEL_OPTION
     OK_OPTION
     CLOSED_OPTION
    */

//---------------------------------------------------------------------------
// Error dialog
   public static void boxError (String title, Object message) {
      boxError(defaultParent, title, message);
   }


   public static void boxError (final Component parent, final String title,
         final Object message) {
      dispatchToEDT(new Runnable()
      {
         public void run () {
            if (Prefs.usr.getBoolean("soundFeedback", true)) {
               try {
                  SoundClip clickSoundClip = new SoundClip("ding.wav");
                  clickSoundClip.play();
               }
               catch (Exception x) {}
            }
            JOptionPane.showInternalMessageDialog(parent, message, title,
                  JOptionPane.ERROR_MESSAGE);
         }
      });
   }
   public static void dispatchToEDT(Runnable runnable) {
      if (!SwingUtilities.isEventDispatchThread()) {
         try {
            SwingUtilities.invokeAndWait(runnable);
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      } else {
         runnable.run();
      }
   }

//--------------------------------------------------------------------------
// Message dialog
   public static void boxNotify (String title, Object message) {
      boxNotify(defaultParent, title, message);
   }


   public static void boxNotify (final Component parent, final String title,
         final Object message) {
      dispatchToEDT(new Runnable()
      {
         public void run () {
            JOptionPane.showInternalMessageDialog(parent, message, title,
                  JOptionPane.INFORMATION_MESSAGE); }
      });
   }


//--------------------------------------------------------------------------
// Confirm dialog with the options yes/no
   public static boolean boxYesNo (String title, Object message) {
      return boxConfirm(defaultParent, title, message);
   }


   public static boolean boxYesNo (Component parent, String title, Object message) {
      int result = JOptionPane.showInternalConfirmDialog(parent, message, title,
            JOptionPane.YES_NO_OPTION);
      return result == JOptionPane.YES_OPTION;
   }


   //--------------------------------------------------------------------------
// Confirm dialog with the options yes/no
   public static boolean boxConfirm (String title, Object message) {
      return boxConfirm(defaultParent, title, message);
   }


   public static boolean boxConfirm (Component parent, String title, Object message) {
      int result = JOptionPane.showInternalConfirmDialog(parent, message, title,
            JOptionPane.OK_CANCEL_OPTION);
      return result == JOptionPane.YES_OPTION;
   }


//--------------------------------------------------------------------------
// Confirm dialog with the options yes/no/cancel
   public static int boxYesNoCancel (String title, Object message) {
      return boxYesNoCancel(defaultParent, title, message);
   }


   public static int boxYesNoCancel (Component parent, String title, Object message) {
      int result = JOptionPane.showInternalConfirmDialog(parent, message, title,
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
      return result;
   }


//--------------------------------------------------------------------------
// Input dialog (string)
   public static String boxInputString (Component parent, String title, Object message) {
      String inputValue = JOptionPane.showInternalInputDialog(parent,
            "Please input a value");
      return inputValue;
   }


//--------------------------------------------------------------------------
// Selection dialog (from a list)
//    Object[] possibleValues = { "First", "Second", "Third" };

   public static String boxSelectFromList (Component parent, String title, Object message,
         Object[] possibleValues) {
      return _boxSelectFromList(parent, title, message, possibleValues);
   }


   public static String _boxSelectFromList (Component parent, String title,
         Object message, Object[] possibleValues) {
      Object selectedValue = JOptionPane.showInternalInputDialog(parent, message, title,
            JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
      return (String) selectedValue;
   }




}
