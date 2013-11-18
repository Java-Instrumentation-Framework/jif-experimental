/*
 * CountDown.java
 *
 * Created on February 15, 2007, 10:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.timing;

// Java Countdown Applet
//
// (C) 1997-1998 John R. Hawksley
// PERMISSION TO USE as per current GPL
//
// john@gazunda.bb.bawue.de

import java.applet.*;
import java.awt.*;
import java.util.*;

public class CountDown extends Applet implements Runnable
{

   private Date nowDate;
   private Date endDate;
   private long diffLong;

   private static long Day, Hrs, Min, Sec;
   private String outString, oldString;
   private String sHrs, sMins, sSecs;
   private char c;

   private static long msecsDay = (1000 * 60 * 60 * 24);
   private static long msecsHrs = (1000 * 60 * 60);
   private static long msecsMin = (1000 * 60);
   private static long msecsSec = (1000);

   Font font;
   Font smfont;

   Thread timer = null;
   

   public void init()
   {                            
      font = new Font("TimesRoman", Font.BOLD, 58);
      smfont = new Font("Helvetica", Font.BOLD, 12);
      resize(600,100);
      
      endDate = new Date(System.currentTimeMillis() + 10000);
//this.getParameter("DATE"));
   
   }

   public void paint(Graphics g)
   {
      nowDate = new Date();
      
      diffLong = (endDate.getTime() - nowDate.getTime());

      // Compute offset

      Day = diffLong / msecsDay;
      Hrs = (diffLong - (msecsDay * Day)) / msecsHrs;
      Min = (diffLong - ((msecsHrs * Hrs) + (msecsDay * Day))) / msecsMin; 
      Sec = (diffLong - ((msecsHrs * Hrs) + (msecsDay * Day) + (msecsMin * Min))) / msecsSec;      

      // System.out.println("DHMS: "+Day+" "+Hrs+" "+Min+" "+Sec);

      sHrs=Long.toString(Hrs);
      sMins=Long.toString(Min);
      sSecs=Long.toString(Sec);

      // System.out.println("S-DHMS 1: "+Day+" "+sHrs+" "+sMins+" "+sSecs);

      if (sSecs.length() == 1)
      {
         c = sSecs.charAt(0);
         sSecs="0"+c;
      }

      if (sMins.length() == 1)
      {
         c = sMins.charAt(0);
         sMins="0"+c;
      }

      if (sHrs.length() == 1)
      {
         c = sHrs.charAt(0);
         sHrs="0"+c;
      }

      // System.out.println("S-DHMS 2: "+Day+" "+sHrs+" "+sMins+" "+sSecs);

      if (Day > 1)
         outString = Day+" days, "+sHrs+":"+sMins+":"+sSecs;
      else if (Day == 1)
         outString = Day+" day, "+sHrs+":"+sMins+":"+sSecs;
      else if (Day == 0)
         outString = sHrs+":"+sMins+":"+sSecs;

      if (Day == 0 && Hrs == 0 && Min == 0 && Sec == 7)
      {
         play(getDocumentBase(),"count2.au");
         System.out.println("BING!");
      }  

      if (diffLong < 0)
         outString = "Time Elapsed";

      g.setColor(Color.black);
      
      g.setFont(smfont);
      g.drawString("Time until: "+endDate.toString(), 0,20);
      /* System.out.println("old: "+oldString);
      System.out.println("new: "+outString); */
      
      if (!outString.equals(oldString))
      {
         g.setFont(font);
         oldString = outString;
         g.clearRect(0,30,600,100);
         g.drawString(outString,10,70);         
      }

      
   }

   public void update (Graphics g)
   {
      paint(g);
   }

   public void start() 
   {
     if(timer == null)
      {
            timer = new Thread(this);
            timer.start();
      }
   }

   public void stop() 
   {
      timer = null;
   }

   public void run() 
   {
      while (timer != null) 
      {
          try {Thread.sleep(100);} catch (InterruptedException e){System.out.println("Error");}
         repaint();
      }
      timer = null;
   }

}

