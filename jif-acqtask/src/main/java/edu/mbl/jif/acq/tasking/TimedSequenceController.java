/*
 * SequenceController.java
 * Created on May 8, 2006, 9:25 AM
 */
package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.acq.*;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.gui.util.StaticSwingUtils;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import ij.ImagePlus;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;

import java.util.Timer;

import javax.swing.JFrame;

/**
 * SequenceController acts as a timer and manager of a sequence of acquisition tasks.
 *
 * if initialDelay > 0, wait that long. if initialDelay = 0, start immediately if initialDelay < 0,
 * wait for monitor.buttonStart to be pressed @author GBH
 */
public class TimedSequenceController {

   long initialDelay; // milliseconds
   private long period; // milliseconds
   private int intervals;
//  private long beganTime;
//  private long willEndTime;
//  private long toGoTime;
//  private long tillNext;
   private boolean cancelled = false;
   //Timer timer;
   //CountdownClock clock;
   TimedSequenceAcqTimerTask sequenceAcqTask;
   PanelTimedSequenceMonitor monitor = null;
   //
   ij.ImagePlus iPlus;
   private String baseFilename;
   MultipageTiffFile tif;
   private PostProcessor postProc;
   //
   InstrumentController instrumentCtrl;

   public TimedSequenceController(final InstrumentController instrumentCtrl, long initialDelay,
           long period, int intervals, final String baseFilename) {
      this(instrumentCtrl, initialDelay, period, intervals, baseFilename, null);
   }

   public TimedSequenceController(final InstrumentController instrumentCtrl, long initialDelay,
           long period, int intervals, final String baseFilename,
           final ImagePlus iPlus) {
      this.instrumentCtrl = instrumentCtrl;
      this.initialDelay = initialDelay;
      this.period = period;
      this.intervals = intervals;
      this.baseFilename = baseFilename;
      this.iPlus = iPlus;
      //timer = new Timer();

//        if (instrumentCtrl.getDataModel().isSeqInStack()) {
//            CamAcq camAcq = CamAcq.getInstance();
//            if (instrumentCtrl.getAcqModel().getDepth() == 8) {
//                iPlus = camAcq.newCaptureStackByte();
//            } else {
//                iPlus = camAcq.newCaptureStackShort();
//            }
//        }
      monitor = new PanelTimedSequenceMonitor(intervals, period, initialDelay, this);
      sequenceAcqTask = new TimedSequenceAcqTimerTask(instrumentCtrl, this,
              intervals, period, baseFilename, iPlus);
      createSequenceMonitorFrame(monitor);
      if (initialDelay > 0) {
         monitor.getClock().createTimer(System.currentTimeMillis() + initialDelay, initialDelay);
      }
      if (initialDelay == 0) {
         monitor.startSequence();
         // otherwise, user pressing start button in monitorPanel starts the sequence
      }
   }

   void createSequenceMonitorFrame(final PanelTimedSequenceMonitor monitor) {
      StaticSwingUtils.dispatchToEDT(new Runnable() {
         public void run() {
            JFrame f = new JFrame();
            //f.setSize(100, 100);
            f.setAlwaysOnTop(true);
            //f.setUndecorated(true);
            f.setTitle("SequenceAcq: " + baseFilename);
            f.setIconImage(
                    new javax.swing.ImageIcon(getClass().getResource(
                    "/edu/mbl/jif/camera/icons/time_green.png")).getImage());
            f.getContentPane().setLayout(new BorderLayout());
            f.getContentPane().add(monitor, BorderLayout.CENTER);
            // On Close JFrame, kill timer (and sequenceAcqTask?)
            f.addWindowListener(new java.awt.event.WindowAdapter() {
               public void windowClosing(WindowEvent winEvt) {
                  onCloseContainer();
               }
            });
            f.pack();
            f.setLocation(5, 5);
            f.setVisible(true);
         }
      });
   }

// <editor-fold defaultstate="collapsed" desc=" Monitor interaction ">
   public void updateStatus(
           final long beganTime,
           final long willEndTime,
           final long toGoTime,
           final int intervals,
           final int nDone) {
      monitor.updateStatus(
              beganTime,
              willEndTime,
              toGoTime,
              nDone);
   }

   public void allDone() {
      monitor.allDone();
   }

   public void createCountdownTimer(long nextTime, long period) {
      monitor.getClock().createTimer(nextTime, period);
   }
// </editor-fold>

   public void startTimer() {
      sequenceAcqTask.startTimer();
   }

   public void setPostProcessor(PostProcessor postProc) {
      this.postProc = postProc;
   }

   // Clean-up if frame is closed
   public void onCloseContainer() {
      cancel();
   }

   public void cancel() {
      // TODO add prompt for confirmation...
      //
      if (sequenceAcqTask != null) {
         sequenceAcqTask.cancel();
         sequenceAcqTask = null;
      }
//    if (clock != null) {
//      clock.cancel();
//      clock = null;
//    }
//    if (timer != null) {
//      timer.cancel();
//    }
      cancelled = true;
      // cleanup
      if (tif != null) {
         tif.removeSeriesFileListener();
         tif.closeWrite();
         //@todo restart display?
      }
   }

   public boolean wasCancelled() {
      return cancelled;
   }

   public static void main(String[] args) {
      
      TimedSequenceController seqCtrl = new TimedSequenceController(
              null,
              -1,
              3000,
              5,
              "");
      
//    StaticSwingUtils.dispatchToEDT(new Runnable() {
//      public void run() {
//        TimedSequenceController seqCtrl = new TimedSequenceController(null,
//                0,
//                2000,
//                3,
//                "");
//      }
//    });
   }
}
