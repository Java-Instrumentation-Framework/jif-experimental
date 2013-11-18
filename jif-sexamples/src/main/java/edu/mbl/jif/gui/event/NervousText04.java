package edu.mbl.jif.gui.event;

/*
 * @(#)NervousText04.java    1.1 97/04/01
 *
 * Copyright (c) 1994-1996 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

/*  Daniel Wyszynski
    Center for Applied Large-Scale Computing (CALC)
    04-12-95

    Test of text animation.

    kwalrath: Changed string; added thread suspension. 5-9-95
 */
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.beans.*;


public class NervousText04
      extends Panel implements Runnable, MouseListener
{

   char separated[];
   String s = null;
   transient Thread killme = null;
   int i;
   int x_coord = 0, y_coord = 0;
   String num;
   int speed = 35;
   int counter = 0;
   boolean threadSuspended = false; //added by kwalrath
   boolean lefttoright = true;

// (06/28/97 08:21:45 AM)
   public NervousText04 () {
      System.err.println("ENTER---> NervousText04 constructor");
      addMouseListener(this);
      // s = getParameter("text");
      if (s == null) {
         //s = "HotJava";
         s = "Nervous Bean";
      }

      separated = new char[s.length()];
      s.getChars(0, s.length(), separated, 0);
      resize((s.length() + 1) * 15, 50);
      setFont(new Font("TimesRoman", Font.BOLD, 36));
      start();
      System.err.println("EXIT----> NervousText04 constructor");
   }


   public void setText (String newstring) {
      String oldstring = s;
      s = new String(newstring);
      separated = new char[s.length()];
      s.getChars(0, s.length(), separated, 0);
      support.firePropertyChange("text", oldstring, newstring);
   }


   public String getText () {
      return (s);
   }


   public void changeDirection (ActionEvent x) {
      if (lefttoright) {
         lefttoright = false;
      } else {
         lefttoright = true;
      }
   }


   private void writeObject (ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      if (killme == null) {
         s.writeBoolean(false);
      } else {
         s.writeBoolean(true);
      }

   }


   private void readObject (ObjectInputStream s) throws ClassNotFoundException,
         IOException {
      s.defaultReadObject();
      if (s.readBoolean() == false) {
         killme = null;
      } else {
         start();
      }
   }


   public void start () {
      if (killme == null) {
         killme = new Thread(this);
         killme.start();
      }
   }


// public Dimension preferredSize() {  // use this for a fixed size bean
   public Dimension getMinimumSize () { // use this for a variable size bean
      return (new Dimension(150, 150));
   }


   public void stop () {
      killme = null;
   }


   public void run () {
      while (killme != null) {
         try {
            Thread.sleep(100); }
         catch (InterruptedException e) {}
         repaint();
      }
      killme = null;
   }


   public synchronized void paint (Graphics g) {
      for (i = 0; i < s.length(); i++) {
         x_coord = (int) (Math.random() * 10 + 15 * i);
         y_coord = (int) (Math.random() * 10 + 36);
         //g.drawChars(separated, i,1,x_coord,y_coord);
         if (lefttoright) {
            g.drawChars(separated, i, 1, x_coord, y_coord);
         } else {
            g.drawChars(separated, (s.length()) - i - 1, 1, x_coord, y_coord);
         }
      }
   }


   public void mousePressed (MouseEvent e) {
      /*
           e.consume();
           if (threadSuspended) {
        killme.resume();
           }
           else {
        killme.suspend();
           }
           threadSuspended = !threadSuspended;
       */
   }


   public void mouseReleased (MouseEvent e) {
   }


   public void mouseEntered (MouseEvent e) {
   }


   public void mouseExited (MouseEvent e) {
   }


   public void mouseClicked (MouseEvent e) {
   }


   //----------------------------------------------------------------------

   public void addPropertyChangeListener (PropertyChangeListener l) {
      support.addPropertyChangeListener(l);
   }


   public void removePropertyChangeListener (PropertyChangeListener l) {
      support.removePropertyChangeListener(l);
   }


   private PropertyChangeSupport support = new PropertyChangeSupport(this);

   //----------------------------------------------------------------------

   public void reportChange (PropertyChangeEvent evt) {
      System.err.println("ENTER---> NervousText04 reportChange");

      String oldValue = evt.getPropertyName() + " := " + evt.getOldValue();
      String newValue = evt.getPropertyName() + " := " + evt.getNewValue();

      System.err.println("New value: " + oldValue);
      System.err.println("Old value: " + newValue);
      System.err.println("EXIT----> NervousText04 reportChange");
   }


   public void makeChange (PropertyChangeEvent evt) {
      System.err.println("ENTER---> NervousText04 makeChange");

      String oldValue = (String) evt.getOldValue(); // (String) cast required since
      String newValue = (String) evt.getNewValue(); // evt.getOldValue() returns an Object
      setText("*" + newValue + "*");
      // if (oldValue != newValue) {
      //    setText("*" + newValue + "*");
      // }

      System.err.println("New value: " + oldValue);
      System.err.println("Old value: " + newValue);
      System.err.println("EXIT----> NervousText04 makeChange");
   }

}
