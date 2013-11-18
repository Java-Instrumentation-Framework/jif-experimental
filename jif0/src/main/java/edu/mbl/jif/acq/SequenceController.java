/*
 * SequenceController.java
 * Created on May 8, 2006, 9:25 AM
 */
package edu.mbl.jif.acq;

import edu.mbl.jif.camacq.CamAcq;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.gui.clock.CountdownClock;
import edu.mbl.jif.gui.util.StaticSwingUtils;
import edu.mbl.jif.gui.imaging.player.SeriesPlayerZoomFrame;
import edu.mbl.jif.gui.sound.Sound;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import ij.ImagePlus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * SequenceController acts as a timer and manager of a sequence of acquisition tasks.
 *
 * if initialDelay > 0, wait that long.
 * if initialDelay = 0, start immediately
 * if initialDelay < 0, wait for monitor.buttonStart to be pressed
 * @author GBH
 */
public class SequenceController {

  long initialDelay; // milliseconds
  private long period; // milliseconds
  private int intervals;
  private long beganTime;
  private long willEndTime;
  private long toGoTime;
  private long tillNext;
  private int nDone = 0;
  private int nToGo = -1;
  private boolean cancelled = false;
  Timer timer;
  CountdownClock clock;
  PanelSequenceMonitor monitor = null;
  //
  SequenceAcqTimerTask sequenceAcqTask;
  //
  ij.ImagePlus iPlus;
  private String baseFilename;
  MultipageTiffFile tif;
  private PostProcessor postProc;
  //
  InstrumentController instrumentCtrl;

  public SequenceController(final InstrumentController instrumentCtrl, long initialDelay,
          long period, int intervals, final String baseFilename) {
    this(instrumentCtrl, initialDelay, period, intervals, baseFilename, null);
  }

  public SequenceController(final InstrumentController instrumentCtrl, long initialDelay,
          long period, int intervals, final String baseFilename,
          final ImagePlus iPlus) {

    this.baseFilename = baseFilename;
    this.iPlus = iPlus;
    this.instrumentCtrl = instrumentCtrl;
    this.initialDelay = initialDelay;
    this.period = period;
    this.intervals = intervals;
    timer = new Timer();

    createSequenceMonitorFrame();

//        if (instrumentCtrl.getDataModel().isSeqInStack()) {
//            CamAcq camAcq = CamAcq.getInstance();
//            if (instrumentCtrl.getAcqModel().getDepth() == 8) {
//                iPlus = camAcq.newCaptureStackByte();
//            } else {
//                iPlus = camAcq.newCaptureStackShort();
//            }
//        }

    sequenceAcqTask = new SequenceAcqTimerTask(timer, intervals, period, baseFilename, iPlus);

    if (initialDelay > 1500) {
      clock.createTimer(System.currentTimeMillis() + initialDelay, initialDelay);
    }
    if (initialDelay > 0) {
      monitor.startSequence();
    // otherwise, user pressing start button in monitorPanel starts the sequence
    }
  }

  void createSequenceMonitorFrame() {
    monitor = new PanelSequenceMonitor(this);
    clock = monitor.getClock();
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

  public void setPostProcessor(PostProcessor postProc) {
    this.postProc = postProc;
  }

  // Clean-up if frame is closed
  public void onCloseContainer() {
    cancel();
  }

  public void startTimer() {
    tif = new MultipageTiffFile(baseFilename);
    timer.schedule(sequenceAcqTask, initialDelay, period);
  }

  public void cancel() {
    if (sequenceAcqTask != null) {
      sequenceAcqTask.cancel();
      sequenceAcqTask = null;
    }
    if (clock != null) {
      clock.cancel();
      clock = null;
    }
    if (timer != null) {
      timer.cancel();
    }
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

  //    public void executeSeriesAcqTask(AcqModel acq, long initialDelay,
  //            long period, int intervals) {
  //    }

  // <editor-fold defaultstate="collapsed" desc="<<< Accessors >>>">
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
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="<<< SequenceAcqTask >>>">
  // === SequenceAcqTask ===============================================
  class SequenceAcqTimerTask extends TimerTask {

    Timer timer;
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    int intervals;
    long period;
    long nextTime;
    long MAX_TARDINESS = 100;
    String tifFilename;
    ij.ImagePlus iPlus;
    ij.ImageStack iStack;
    SeriesPlayerZoomFrame seriesPlayerFrame;

    public SequenceAcqTimerTask(Timer timer, int intervals, long period, String tifFilename,
            ij.ImagePlus iPlus) {
      this.timer = timer;
      this.intervals = intervals;
      this.period = period;
      this.tifFilename = tifFilename;
      this.iPlus = iPlus;
      if (iPlus != null) {
        this.iStack = iPlus.getStack();
      } else {
        this.iStack = null;
      }
    }

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
        // on each iteration:

        // +++
        // Do Acquisition
        //   For each Site...
        //     For each Mode...
        //     Do Zseries...
        //        (or AcquireMultiChannelStack)
        //       Acquire image
        //         On separate thread, or not?
        //

        if (CamAcqJ.getInstance().getPreferences().getBoolean("app.auditoryFeedback", true)) {
          Sound.soundClick();
        }

        { // Acquisition
          if (iStack != null) { // capture to ImageJ Stack
            CamAcq.getInstance().captureImageToStack(iStack, nDone);
          // SAVE !!
          } else { // capturing to CamAcqJ viewer
            BufferedImage img = ((AcquisitionController) instrumentCtrl.getController("acq")).acquireImage();
            tif.appendImage(img);
          //((AcquisitionController) instrumentCtrl.getController("acq")).acquireAnImage(tifFilename); // <<<<<<<<<<<
          }
        }
        //System.out.println("Acquired #" + nDone + " at " + (System.currentTimeMillis() - beganTime));

        nDone++;

        if (nDone == 1) {  // after first iteration...
          // Create a series viewer and attach a listener for the tiff file
          AcqModel acqModel = (AcqModel) instrumentCtrl.getModel("acq");
          if (acqModel.isDisplayWhileAcq()) {
            createSeriesViewer(tifFilename);
          }
        }
        // update status in monitor and restart countdown clock
        long now = System.currentTimeMillis();
        toGoTime = willEndTime - now;
        nextTime = (beganTime + (nDone * period));
        //  System.out.println("nextTime: " + nextTime + "  period: " + period + " Now: " + System.currentTimeMillis());
        if ((nextTime > now) && (nDone < intervals)) {
          monitor.updateStatus();
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              if (period > 999) {
                clock.createTimer(nextTime, period);
              }
            }
          });
        }
        // ------------------------------------
        // At the end of the sequence...
        if (nDone == intervals) {
          // after all iterations:
          if (CamAcqJ.getInstance().getPreferences().getBoolean("app.auditoryFeedback", true)) {
            Sound.soundAllDone();
          }
          timer.cancel();
          if (iStack != null) {  // Captured to ImageJ Stack, so finish up...
            try {
              SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                  CamAcq.getInstance().captureStackFinish(iPlus, tifFilename);
                }
              });
            } catch (InterruptedException ex) {
              ex.printStackTrace();
            } catch (InvocationTargetException ex) {
              ex.printStackTrace();
            }
          } else {
          }
          if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                ((AcquisitionController) instrumentCtrl.getController("acq")).displayResume();
              }
            });
          }
          tif.removeSeriesFileListener();
          tif.closeWrite();
          monitor.allDone();
          Application.getInstance().statusBarMessage(
                  "Series Acquisition completed at " + (System.currentTimeMillis() - beganTime));
          // Run the post-processor
          if (postProc != null) {
            Application.getInstance().statusBarMessage("Series Acquisition Post-process running...");
            Runnable runnable = new Runnable() {
              public void run() {
                postProc.run();
              }
            };
          }
        } // on last in sequence
      }
    }

    // call cancel() method to cancel this.
    // If a task is running when cancelled, the task will run to completion, then stops

    // Creates a frame that displays the series images as they are acquired.
    // @todo:
    public void createSeriesViewer(String tiffFilename) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          seriesPlayerFrame = new SeriesPlayerZoomFrame(tifFilename);
          seriesPlayerFrame.setLocation(200, 150);
          seriesPlayerFrame.setSize(
                  StaticSwingUtils.sizeFrameForDefaultScreen(
                  seriesPlayerFrame.getImageDimension()));
          StaticSwingUtils.locateUpperRight(seriesPlayerFrame);
          seriesPlayerFrame.pack();
          seriesPlayerFrame.setVisible(true);
          tif.addSeriesFileListener(seriesPlayerFrame);
          seriesPlayerFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(
                    WindowEvent e) {
              seriesPlayerFrame.dispose(); // Release the window resources
              tif.removeSeriesFileListener();
            }
          });
        }
      });
    }
  }
  // </editor-fold>
}
