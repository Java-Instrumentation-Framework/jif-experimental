package tests.gui.frame;

   import java.awt.*;
   import javax.swing.*;
/**
 *
 * @author GBH
 */
public class TitleBarOnFrame {

/*  Swing provides a level of control over which title bar to use.
 * 
 However, this requires a little more work than simply directing
 JFrame to use the default style from the look and feel. You have to
 tell the frame to be undecorated and tell its root pane how to decorate the frame.
 You do this through the following methods:

   frame.setUndecorated(true);
   frame.getRootPane().setWindowDecorationStyle(XXX);

The parameter represented as XXX in setWindowDecorationStyle can be one of nine constants of the JRootPane class:
COLOR_CHOOSER_DIALOG
ERROR_DIALOG
FILE_CHOOSER_DIALOG
FRAME
INFORMATION_DIALOG
NONE
PLAIN_DIALOG
QUESTION_DIALOG
WARNING_DIALOG

By passing the proper constant to the frame or dialog, a window can
get a different  title bar. In the following program, AdornSample2,
uncomment different commented lines to try out the different styles:
 * */

     public static void main(final String args[]) {
       Runnable runner = new Runnable() {
         public void run() {
           JFrame frame = new JFrame("Adornment Example");
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.setUndecorated(true);
           frame.getRootPane().setWindowDecorationStyle(
             // JRootPane.COLOR_CHOOSER_DIALOG);
             // JRootPane.ERROR_DIALOG);
             // JRootPane.FILE_CHOOSER_DIALOG);
             // JRootPane.FRAME);
             // JRootPane.INFORMATION_DIALOG);
             // JRootPane.NONE);
             //JRootPane.PLAIN_DIALOG);
             JRootPane.QUESTION_DIALOG);
             // JRootPane.WARNING_DIALOG);
           frame.setSize(300, 100);
           frame.setVisible(true);
         }
       };
       EventQueue.invokeLater(runner);
     }
   }