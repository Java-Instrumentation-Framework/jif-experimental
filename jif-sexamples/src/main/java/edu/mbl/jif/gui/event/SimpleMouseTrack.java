/*
 * SimpleMouseTrack.java
 *
 * Created on October 5, 2006, 11:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.gui.event;
/*
    A simple applet that displays information about mouse events as
    they occur.  Since double-buffering is not used, the display
    flickers quite a bit.
*/

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class SimpleMouseTrack extends Applet implements MouseListener, MouseMotionListener {

   int mouse_x, mouse_y;        // Position of mouse.
   String modifierKeys = "";    // If non-null, gives special keys that are held down.
   String eventType = null;     // If non-null, gives the type of the most recent mouse event.
   
   public void init() { 
         // Set background color and arrange for the applet to listen for mouse events.
      setBackground(Color.white);
      addMouseListener(this);
      addMouseMotionListener(this);
   }
   
   public void paint(Graphics g) {
         // Draw applet, showing information about mouse events.
      g.setColor(Color.blue);
      g.drawRect(0 ,0, getSize().width - 1 ,getSize().height - 1);  // draw a frame for the applet area
      g.setColor(Color.red);
      if (eventType != null)
         g.drawString("Mouse event type:  " + eventType, 4, 17);
      if (modifierKeys.length() > 0)
         g.drawString("Modifier keys:     " + modifierKeys,4,32);
      g.setColor(Color.black);
      g.drawString("(" + mouse_x + "," + mouse_y + ")",
           mouse_x, mouse_y);
   }  // end of paint()
   
   void setInfo(MouseEvent evt) {
         // set up the information about event for display
      mouse_x = evt.getX();
      mouse_y = evt.getY();
      modifierKeys = "";
      if (evt.isShiftDown())
         modifierKeys += "Shift  ";
      if (evt.isControlDown())
         modifierKeys += "Control  ";
      if (evt.isMetaDown())
         modifierKeys += "Meta  ";
      if (evt.isAltDown())
         modifierKeys += "Alt";
      repaint();
   }
   
   // Implement all the events of the MouseListener and MouseMotionListener
   // interfaces.  Each method sets eventType to record the type of event and
   // calls the setInfo method to extract other information from the event
   // for display.
   
   public void mousePressed(MouseEvent evt) {
      eventType = "mousePressed";
      setInfo(evt);
   }
      
   public void mouseReleased(MouseEvent evt) {
      eventType = "mouseReleased";
      setInfo(evt);
   }
      
   public void mouseClicked(MouseEvent evt) {
      eventType = "mouseClicked";
      setInfo(evt);
   }
      
   public void mouseEntered(MouseEvent evt) {
      eventType = "mouseEntered";
      setInfo(evt);
   }
      
   public void mouseExited(MouseEvent evt) {
      eventType = "mouseExited";
      setInfo(evt);
   }
      
   public void mouseMoved(MouseEvent evt) {
      eventType = "mouseMoved";
      setInfo(evt);
   }
      
   public void mouseDragged(MouseEvent evt) {
      eventType = "mouseDragged";
      setInfo(evt);
   }
      
}  // end of class SimpleMouseTracker
