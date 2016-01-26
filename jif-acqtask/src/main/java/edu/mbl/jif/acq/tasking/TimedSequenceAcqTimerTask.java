/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.acq.PostProcessor;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;

import edu.mbl.jif.gui.imaging.player.SeriesPlayerZoomFrame;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import edu.mbl.jif.utils.StaticSwingUtils;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

/**
 *
 * @author GBH
 */

/*
 * TODO - Multiple files...
 */
public class TimedSequenceAcqTimerTask extends TimerTask {

   Timer timer;
   Toolkit toolkit = Toolkit.getDefaultToolkit();
   private long period; // milliseconds
   private int intervals;
   long initialDelay; // milliseconds
   private long beganTime;
   private long willEndTime;
   private long toGoTime;
   private long tillNext;
   private long nextTime;
   private int nDone = 0;
   private int nToGo = -1;
   long MAX_TARDINESS = 100;
   String tifFilename;
   private String baseFilename;
   MultipageTiffFile tif;
   private PostProcessor postProc;
   ij.ImagePlus iPlus;
   ij.ImageStack iStack;
   SeriesPlayerZoomFrame seriesPlayerFrame;
   TimedSequenceController seqCtrl;
   final InstrumentController instrumentCtrl;

   public TimedSequenceAcqTimerTask(final InstrumentController instrumentCtrl,
           TimedSequenceController seqCtrl, int intervals, long period, String tifFilename,
           ij.ImagePlus iPlus) {
      this.instrumentCtrl = instrumentCtrl;
      this.seqCtrl = seqCtrl;
      this.intervals = intervals;
      this.period = period;
      this.tifFilename = tifFilename;
      this.iPlus = iPlus;
      if (iPlus != null) {
         this.iStack = iPlus.getStack();
      } else {
         this.iStack = null;
      }
      this.timer = new Timer();
   }

   public void startTimer() {
      tif = new MultipageTiffFile(baseFilename);
      timer.schedule(this, initialDelay, period);
   }

   // Allow resetting intervals during sequence
   public void setIntervals(int intervals) {
      this.intervals = intervals;
   }

   
   public long getPeriod() {
      return period;
   }

   public int getIntervals() {
      return intervals;
   }

   public long getBeganTime() {
      return beganTime;
   }

   public long getWillEndTime() {
      return willEndTime;
   }

   public long getToGoTime() {
      return toGoTime;
   }

   public long getTillNext() {
      return tillNext;
   }

   public int getNDone() {
      return nDone;
   }

   public int getNToGo() {
      return nToGo;
   }

   public void doAcq(int nDone, MultipageTiffFile tif) {
      System.out.println("Do Acq.");
//      BufferedImage img = ((AcquisitionController) instrumentCtrl.getController("acq")).acquireImage();
//      tif.appendImage(img);
   }

   @Override
   public void run() {
      //System.out.println("Milli: " + System.currentTimeMillis());
      //System.out.println("Nano: " + System.nanoTime());
      if (nDone == 0) { // first time
         beganTime = System.currentTimeMillis();
         willEndTime = beganTime + (intervals * period);
      }
      nToGo = intervals - nDone;
      if (nToGo > 0) {
         if ((System.currentTimeMillis() - scheduledExecutionTime()) >= MAX_TARDINESS) {
            // @todo: Add option to skip or not
            System.out.println("Too late, skipping interval " + intervals);
            nDone++;
            return; // Too late; skip this execution.
         }

         // on each iteration...
//      if (CamAcqJ.getInstance().getPreferences().getBoolean("app.auditoryFeedback", true)) {
//        Sound.soundClick();
//      }
         {
            // +++
            // Do Acquisition
            // Passing: nDone (current iteration), the TiffFile
            doAcq(nDone, tif);
//          BufferedImage img = ((AcquisitionController) instrumentCtrl.getController("acq")).acquireImage();
//          tif.appendImage(img);
         }
      }
      //System.out.println("Acquired #" + nDone + " at " + (System.currentTimeMillis() - beganTime));

      nDone++;

      if (nDone == 1) {  // after first iteration...
//        AcqModel acqModel = (AcqModel) instrumentCtrl.getModel("acq");
//        if (acqModel.isDisplayWhileAcq()) {
//          createSeriesViewer(tifFilename);
//        }
      }

      // update status in seqCtrl and restart countdown clock
      long now = System.currentTimeMillis();
      toGoTime = willEndTime - now;
      nextTime = (beganTime + (nDone * period));
      //  System.out.println("nextTime: " + nextTime + "  period: " + period + " Now: " + System.currentTimeMillis());
      if ((nextTime > now) && (nDone < intervals)) {
         seqCtrl.updateStatus(beganTime,
                 willEndTime,
                 toGoTime,
                 intervals,
                 nDone);
//      SwingUtilities.invokeLater(new Runnable() {
//
//        public void run() {
         if (period > 999) {
            seqCtrl.createCountdownTimer(nextTime, period);
         }
//        }
//      });
      }
      //
      // At the end of the sequence...
      if (nDone == intervals) {
         // after all iterations:
//      if (CamAcqJ.getInstance().getPreferences().getBoolean("app.auditoryFeedback", true)) {
//        Sound.soundAllDone();
//      }
         timer.cancel();
//      if (iStack != null) {  // Captured to ImageJ Stack, so finish up...
//        try {
//          SwingUtilities.invokeAndWait(new Runnable() {
//
//            public void run() {
//              CamAcq.getInstance().captureStackFinish(iPlus, tifFilename);
//            }
//          });
//        } catch (InterruptedException ex) {
//          ex.printStackTrace();
//        } catch (InvocationTargetException ex) {
//          ex.printStackTrace();
//        }
//      } else {
//      }
//      if (!SwingUtilities.isEventDispatchThread()) {
//        SwingUtilities.invokeLater(new Runnable() {
//
//          public void run() {
//            ((AcquisitionController) instrumentCtrl.getController("acq")).displayResume();
//          }
//        });
//      }
//      tif.removeSeriesFileListener();
//      tif.closeWrite();
         seqCtrl.allDone();
//      Application.getInstance().statusBarMessage(
//              "Series Acquisition completed at " + (System.currentTimeMillis() - beganTime));
         // Run the post-processor
         if (postProc != null) {
            Application.getInstance().statusBarMessage("Series Acquisition Post-process running...");
            Runnable runnable = new Runnable() {
               @Override
               public void run() {
                  postProc.run();
               }
            };
         }
      } // on last in sequence
   }

// call cancel() method to cancel this.
// If a task is running when cancelled, the task will run to completion, then stops
// Creates a frame that displays the series images as they are acquired.
// @todo:
   public void createSeriesViewer(String tiffFilename) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            seriesPlayerFrame = new SeriesPlayerZoomFrame(tifFilename);
            seriesPlayerFrame.setLocation(200, 150);
            seriesPlayerFrame.setSize(
                    StaticSwingUtils.sizeFrameForDefaultScreen(
                    seriesPlayerFrame.getImageDimension()));
            StaticSwingUtils.locateUpperRight(seriesPlayerFrame);
            seriesPlayerFrame.pack();
            seriesPlayerFrame.setVisible(true);
//        tif.addSeriesFileListener(seriesPlayerFrame);
//        seriesPlayerFrame.addWindowListener(new WindowAdapter() {
//
//          public void windowClosing(
//                  WindowEvent e) {
//            seriesPlayerFrame.dispose(); // Release the window resources
//            tif.removeSeriesFileListener();
//          }
//        });
         }
      });
   }
}
