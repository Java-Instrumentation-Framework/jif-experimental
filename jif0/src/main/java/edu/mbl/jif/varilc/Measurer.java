/*
 * Measurer.java
 *
 * Created on February 11, 2007, 12:44 PM
 */
package edu.mbl.jif.varilc;

import edu.mbl.jif.gui.imaging.IntensityWatcher;
import edu.mbl.jif.camacq.InstrumentController;
import java.util.concurrent.*;

/**
 * Dispatcher
 *
 * Enables non-blocking sending of command and then waiting for completion...
 *
 * @author GBH
 */
public class Measurer {

  ExecutorService executor;
  private int timeOut;
  VLCController vlcCtrl;
  IntensityWatcher watcher;

  /**
   * Creates a new instance of Measurer
   */
  public Measurer(VLCController vlcCtrl, IntensityWatcher watcher) {
    this.vlcCtrl = vlcCtrl;
    this.watcher = watcher;
    executor = Executors.newSingleThreadExecutor();
  }

  public void setTimeOut(int timeOut) {
    this.timeOut = timeOut;
  }

  public MeasurementTask doCommand(int setting) {
    MeasurementTask task = new MeasurementTask(new MeasureIntensityForSetting(setting, 1000));
    executor.submit(task);
    return task;
  }

  public float waitFor(MeasurementTask task) {
    float response;
    try {
      response = (Float) task.get(timeOut, TimeUnit.MILLISECONDS);
      return response;
    } catch (ExecutionException ex) {
      ex.printStackTrace();
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    } catch (TimeoutException ex) {
      System.out.println("Measurement did not stablize within range = " + watcher.getStabilityLevel() + ", timeout = " + timeOut);
    } finally {
      shutDown();
    }
    return -1;
  }

  public void shutDown() {
    executor.shutdown();
  }

 public class MeasureIntensityForSetting implements Callable<Float> {

    int setting;
    long timeout;

    public MeasureIntensityForSetting(int setting, long timeout) {
      this.setting = setting;
      this.timeout = timeout;
    }

    public Float call() {
      float intensity = -1.0f;
      try {
        vlcCtrl.selectElement(setting);
        intensity = watcher.getStableMean(timeout);
        watcher = null;
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return intensity;
    }
  }

  public class MeasurementTask extends FutureTask {

    public MeasurementTask(Callable call) {
      super(call);
    }
  }

  public static void main(String[] args) {
//        Measurer sd = new Measurer(new VariLCModel(new InstrumentController()), new IntensityWatcher());
//        MeasurementTask mTask = sd.doCommand(1);
//        System.out.println("Do some other stuff...");
//        System.out.println(sd.waitFor(mTask));
//        //        try {
//        //            System.out.println(sioTask.get(3000, TimeUnit.MILLISECONDS));
//        //        } catch (ExecutionException ex) {
//        //            ex.printStackTrace();
//        //        } catch (InterruptedException ex) {
//        //            ex.printStackTrace();
//        //        } catch (TimeoutException ex) {
//        //            ex.printStackTrace();
//        //        }
//        sd.shutDown();
    }
}
