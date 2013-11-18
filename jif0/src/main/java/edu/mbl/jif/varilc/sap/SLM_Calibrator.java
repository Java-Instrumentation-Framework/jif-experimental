/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.varilc.sap;

import edu.mbl.jif.varilc.*;
import edu.mbl.jif.acq.AcquisitionController;
import edu.mbl.jif.camera.ImageAnalyzer;
import edu.mbl.jif.camera.ImageStatistics;
import edu.mbl.jif.camacq.InstrumentController;

import edu.mbl.jif.io.csv.CSVFileWrite;
import edu.mbl.jif.utils.FileUtil;
import edu.mbl.jif.utils.analytic.BrentsMethod_IntDouble;
import edu.mbl.jif.utils.analytic.Function_int_double;
import ij.IJ;
import info.monitorenter.util.Range;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 *  Exercise
Calibration
Find Extinction
 * @author GBH
 */
public class SLM_Calibrator {

  SLMModel slmModel;
  SLMController slmCtrl;
  SLM_Roi slmROI;
  Rectangle[] rois;
  Layer layer;
  InstrumentController instrumentCtrl;
  AcquisitionController acqCtrl;
  CyclicBarrier barrier;
  CyclicBarrier allDoneBarrier;
  CountDownLatch stopSignal;
  BrentsMethod_IntDouble[] minimizations;
  int numIDs;
  double[] settings;
  double[] measurements;
  double[] targets;
  double[] measuredIntensity0;
  double[] measuredIntensity1;
  double[] measuredIntensity2;
  double[] measuredIntensity3;
  double[] measuredIntensity4;
  DataSet[] dataSets;
  //
  MockRetarder[] mockRet;
  boolean doPlots = false;

// <editor-fold defaultstate="collapsed" desc=">>>--- Constructor ---<<<" >
  public SLM_Calibrator(SLMModel slmModel, SLMController slm,
          SLM_Roi slmROI,
          InstrumentController instrumentCtrl) {
    this.slmModel = slmModel;
    this.slmCtrl = slm;
    this.slmROI = slmROI;
    this.instrumentCtrl = instrumentCtrl;
    acqCtrl = (AcquisitionController) instrumentCtrl.getController("acq");
  }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc=">>>--- Setup ---<<<" >
  public void setup(Layer layer) {
    this.layer = layer;
    switch (layer) {
      case retarderA:
        rois = slmROI.getRetarderROIs();
        break;
      case retarderB:
        rois = slmROI.getRetarderROIs();
        break;
      case elliptical:
        rois = slmROI.getEllipticalROIs();
        break;
      case circular:
        rois = slmROI.getCircularROIs();
        break;
    }
    numIDs = rois.length;
    settings = new double[numIDs];
    measurements = new double[numIDs];
    createDataSets(numIDs);


    if (SLMModel.useMockRetarders) {
      mockRet = MockRetarder.create(numIDs);
    }
  }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc=">>>--- Characterize ---<<<" >
  public void responseCurves() {
    switch (layer) {
      case retarderA:
        targets = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        break;
      case retarderB:
        targets = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        break;
      case elliptical:
        targets = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
        break;
      case circular:
        targets = new double[]{0, 0};
        break;
    }
    String filename = FileUtil.getcwd() + "/" + layer + ".dat";
    MeasurementPlotter mPlot;
    if (doPlots) {
      mPlot = new MeasurementPlotter("Response - " + layer.toString(),
              numIDs, filename, new Range(0, 254), new Rectangle(10, 10,
              400,
              300));
    } else {
      mPlot = new MeasurementPlotter(numIDs, filename);
    }

    filename = FileUtil.getcwd() + "/Normalized-" + layer + ".dat";
    MeasurementPlotter mNPlot;
    if (doPlots) {
      mNPlot = new MeasurementPlotter("NormalizedResponse - " + layer.toString(),
              numIDs, filename, new Range(0, 1), new Rectangle(410, 10,
              400,
              300));
    } else {
      mNPlot = new MeasurementPlotter(numIDs, filename);
    }
    TaskCharacterize task = new TaskCharacterize(mPlot, mNPlot);
    task.execute();
  }

  class TaskCharacterize
          extends SwingWorker {

    MeasurementPlotter mPlot;
    MeasurementPlotter mNPlot;

    public TaskCharacterize() {
    }

    public TaskCharacterize(MeasurementPlotter mPlot,
            MeasurementPlotter mNPlot) {
      this.mPlot = mPlot;
      this.mNPlot = mNPlot;
    }
    final MultiSectorMeasurer acquireImageAndMeasure = new MultiSectorMeasurer();

    @Override
    public Object doInBackground() {
      int point = 0;
      System.out.println("measureResponseCurves for layer: " + layer);
      int step = (int) ((SLMModel.startMax - SLMModel.startMin) / SLMModel.numPoints);
      acqCtrl.start();
      for (int s = 0; s < SLMModel.numPoints; s++) { // for each setting
        int setTo = (int) (SLMModel.startMin + s * step);
        for (int i = 0; i < numIDs; i++) { //for each sector
          settings[i] = setTo;
        }
        //currentPoint = s;
        acquireImageAndMeasure.run();
        point++;
        for (int i = 0; i < numIDs; i++) {
          // mPlot.recordData(i, point, settings[i], measurements[i]);
          dataSets[i].addPoint(point, settings[i], measurements[i]);
        }
        System.out.println(">> point: " + point);
      }
      // code here never executes...

      System.out.println("doInBackground completed");
      return null;
    }

    @Override
    protected void done() {
      acqCtrl.finish();
      System.out.println("------------------TaskCharacterize - DONE");
      // dumpDataSet();
      writeOutMatrix(dataSets, "Raw");
    //normalize(dataSets);
    }

    private void normalize(DataSet[] dataSets) {
      double[] max = new double[numIDs];

      // find max
      for (int i = 0; i < numIDs; i++) {
        for (int j = 0; j < SLMModel.numPoints; j++) {
          if (max[i] < dataSets[i].getMeasurement(j)) {
            max[i] = dataSets[i].getMeasurement(j);
          }
        }
      }
      // normalize
      for (int i = 0; i < numIDs; i++) {
        for (int j = 0; j < SLMModel.numPoints; j++) {
          dataSets[i].measurement[j] = dataSets[i].measurement[j] / max[i];
        }
      }
      // dumpDataSet();

      // Plot & record Normalized Intensity
      for (int i = 0; i < numIDs; i++) {
        System.out.println("\n" + ">>>>>>> ID: " + i);
        for (int j = 0; j < SLMModel.numPoints; j++) {
          System.out.println(dataSets[i].setting[j] + " => " + dataSets[i].measurement[j]);
          double s = dataSets[i].getSetting(j);
          double m = dataSets[i].getMeasurement(j);
          mNPlot.recordData(i, j, s, m);
        }
      }
      mNPlot.closeTextFile();
      writeOutMatrix(dataSets, "Normalized");


      // Plot & record RetardanceCurve
      for (int i = 0; i < numIDs; i++) {
        System.out.println("\n" + ">>>>>>> ID: " + i);
        double lastValue = 0;
        boolean inSection1 = true;
        boolean inSection2 = false;
        boolean inSection3 = false;
        for (int j = 0; j < SLMModel.numPoints; j++) {
          System.out.println(dataSets[i].setting[j] + " => " + dataSets[i].measurement[j]);
          double s = dataSets[i].getSetting(j);
          double m = dataSets[i].getMeasurement(j);
          if (inSection1 && (lastValue > m)) {
            inSection1 = false;
            inSection2 = true;
          }
          if (inSection2 && (lastValue < m)) {
            inSection2 = false;
            inSection3 = true;
          }
          lastValue = m;
          double ret = 0;
          if (inSection1) {
            ret = 3.0 * Math.PI - 2 * Math.asin(Math.sqrt(m));
          }
          if (inSection2) {
            ret = Math.PI + 2 * Math.asin(Math.sqrt(m));
          }
          if (inSection3) {
            ret = Math.PI - 2 * Math.asin(Math.sqrt(m));
          }

          mNPlot.recordData(i, j, s, ret);

          dataSets[i].measurement[j] = ret;
        }
      }
      mNPlot.closeTextFile();

      writeOutMatrix(dataSets, "Retardance");

    }

    private void writeOutMatrix(DataSet[] dataSets, String type) {
      // Write out raw data as matrix
      CSVFileWrite dataFile;
      String filename =FileUtil.getcwd() + "/" + type + "-Matrix-" + layer + ".csv";
      if (filename != null) {
        dataFile = new CSVFileWrite(filename);
        Object[] head = new Object[]{"zeroIntensity", slmModel.getZeroIntensity()};
        dataFile.writeRow(head);
        if (dataFile != null) {
          for (int j = 0; j < SLMModel.numPoints; j++) {
            double s = dataSets[0].getSetting(j);
            //System.out.println("\n" + j + ": " + s + " = ");
            Object[] d = new Object[numIDs + 2];
            d[0] = j;
            d[1] = (int) s;
            for (int i = 0; i < numIDs; i++) {
              double m = dataSets[i].getMeasurement(j);
              d[i + 2] = m;
            }
            dataFile.writeRow(d);
          }
        }
        dataFile.close();
      }
    }
  }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc=">>>--- Mask Calibration ---<<<" >
  public void calibrateMasks() {
//        SwingWorker<Void, Void> worker =
//                new SwingWorker<Void, Void>() {
//
//                    @Override
//                    Void doInBackground() {
//                        slmCtrl.setFullTransmission();
//                        calibrateEllipticalMask();
//                        calibrateCircularMask();
//                        return null;
//                    }
//
//
//                    @Override
//                    void done() {
//                        slmCtrl.showSettings();
//                    }
//
//
//                };
//        worker.execute();
  }

  public void calibrateEllipticalMask() {
    this.setup(Layer.elliptical);
    String filename = FileUtil.getcwd() + "/" + "Elliptical Mask Calibration" + ".dat";
    MeasurementPlotter mPlot = new MeasurementPlotter("Elliptical Mask Calibration",
            numIDs, filename, new Range(0, 254), new Rectangle(10, 10, 400,
            300));
    // minimize, i.e. find max. extinction
    double[] targets = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
    double[] a = new double[numIDs];
    double[] b = new double[numIDs];
    for (int i = 0; i < a.length; i++) {
      a[i] = (double) SLMModel.HuntRangeMasksMin_a;
      b[i] = (double) SLMModel.HuntRangeMasksMin_b;
    }
    this.minimizeToTargets(a, b, slmModel.getTolCoarse(), targets, mPlot);
    slmCtrl.setEllipticalExtinctSettings(this.getSettings());
    System.out.println("calibrateEllipticalMask, min done.");

    // Maximize, i.e. find max. transmission
    targets = new double[]{254, 254, 254, 254, 254, 254, 254, 254};
    for (int i = 0; i < a.length; i++) {
      a[i] = (double) SLMModel.HuntRangeMasksMax_a;
      b[i] = (double) SLMModel.HuntRangeMasksMax_b;
    }
    this.minimizeToTargets(a, b, slmModel.getTolCoarse(), targets, mPlot);
    slmCtrl.setEllipticalTransmitSettings(this.getSettings());
    System.out.println("calibrateEllipticalMask, max done.");
  }

  public void calibrateCircularMask() {
    this.setup(Layer.circular);
    String filename = FileUtil.getcwd() + "/" + "Circular Mask Calibration" + ".dat";
    MeasurementPlotter mPlot = new MeasurementPlotter("Circular Mask Calibration",
            numIDs, filename, new Range(0, 254), new Rectangle(10, 10, 400,
            300));
    // minimize, i.e. find max. extinction
    double[] targets = new double[]{0, 0};
    double[] a = new double[numIDs];
    double[] b = new double[numIDs];
    for (int i = 0; i < a.length; i++) {
      a[i] = (double) SLMModel.HuntRangeMasksMin_a;
      b[i] = (double) SLMModel.HuntRangeMasksMin_b;
    }
    this.minimizeToTargets(a, b, slmModel.getTolCoarse(), targets, mPlot);
    slmCtrl.setCircularExtinctSettings(this.getSettings());
    // Maximize, i.e. fine max. transmission
    targets = new double[]{254, 254};
    for (int i = 0; i < a.length; i++) {
      a[i] = (double) SLMModel.HuntRangeMasksMax_a;
      b[i] = (double) SLMModel.HuntRangeMasksMax_b;
    }
    this.minimizeToTargets(a, b, slmModel.getTolCoarse(), targets, mPlot);
    slmCtrl.setCircularTransmitSettings(this.getSettings());
  }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc=">>>--- Retarder Calibration ---<<<" >
  public void calibrateRetarders() {
    setup(Layer.retarderA); // just to init. values for MeasurementPlotter
    String filename = FileUtil.getcwd() + "/" + "RetarderCalibration" + ".dat";
    MeasurementPlotter mPlot =
            new MeasurementPlotter("RetarderCalibration", numIDs, filename,
            new Range(0, 254), new Rectangle(10, 10, 400, 300));
    // Find Extinction, setting 0
    // ??? setNominalSwing(0);
    findExtinction(mPlot);

    // Measure Setting 1
    // set retarders to setting1 - (extinctionA+swing, extinctionB)
    setNominalSwing(1);
    // Measure intensities with these settings
    setup(Layer.retarderA);  // or B, doesn't matter
    this.measureSetting1(mPlot);
    measuredIntensity1 = this.getMeasuredValues().clone();
    System.out.println("Measured Intensity One..................................................");
    this.displayMeasuredIntensities();
    // Setting 2
    setNominalSwing(2);
    matchSettingN(2, mPlot);
    measuredIntensity2 = this.getMeasuredValues().clone();
    // Setting 3
    setNominalSwing(3);
    matchSettingN(3, mPlot);
    measuredIntensity3 = this.getMeasuredValues().clone();
    // Setting 4
    setNominalSwing(4);
    matchSettingN(4, mPlot);
    measuredIntensity4 = this.getMeasuredValues().clone();
  }

  public void setNominalSwing(int n) {
    for (int sector = 0; sector < slmCtrl.settings.retardSetA[0].length; sector++) {

      //      setElement(0, extinctA, extinctB);
      //      setElement(1, extinctA + LC_Swing, extinctB);
      //      setElement(2, extinctA, extinctB + LC_Swing);
      //      setElement(3, extinctA, extinctB - LC_Swing);
      //      setElement(4, extinctA - LC_Swing, extinctB);

      //  swing of 0.01 = 0.0628 shift in retardance
      // ??? Does swingEquivalent vary with Sector? Yes.
      double swingeAngle = slmModel.getSwing() * slmModel.getWavelength() * 2 * Math.PI;

      switch (n) {
        case 0: // Extinction, nominal 0.25, 0.50
          // ++ >>>>>>>> default extinction settings
          // slmCtrl.settings.retardSetA[0][sector] = 1559;
          // slmCtrl.settings.retardSetB[0][sector] = 1559;
          slmCtrl.setRetA(sector,
                  slmCtrl.settings.retardSetA[0][sector]);
          slmCtrl.setRetB(sector,
                  slmCtrl.settings.retardSetB[0][sector]);
          break;
        case 1:
          slmCtrl.settings.retardSetA[1][sector] =
                  slmCtrl.settings.retardSetA[0][sector] + swingeAngle;
          slmCtrl.settings.retardSetB[1][sector] =
                  slmCtrl.settings.retardSetB[0][sector];
          slmCtrl.setRetA(sector,
                  slmCtrl.settings.retardSetA[1][sector]);
          slmCtrl.setRetB(sector,
                  slmCtrl.settings.retardSetB[1][sector]);
          break;
        case 2:
          slmCtrl.settings.retardSetA[2][sector] =
                  slmCtrl.settings.retardSetA[0][sector];
          slmCtrl.settings.retardSetB[2][sector] =
                  slmCtrl.settings.retardSetB[0][sector] + swingeAngle;
          slmCtrl.setRetA(sector,
                  slmCtrl.settings.retardSetA[2][sector]);
          slmCtrl.setRetB(sector,
                  slmCtrl.settings.retardSetB[2][sector]);
          break;
        case 3:
          slmCtrl.settings.retardSetA[3][sector] =
                  slmCtrl.settings.retardSetA[0][sector];
          slmCtrl.settings.retardSetB[3][sector] =
                  slmCtrl.settings.retardSetB[0][sector] - swingeAngle;
          slmCtrl.setRetA(sector,
                  slmCtrl.settings.retardSetA[3][sector]);
          slmCtrl.setRetB(sector,
                  slmCtrl.settings.retardSetB[3][sector]);
          break;
        case 4:
          slmCtrl.settings.retardSetA[4][sector] =
                  slmCtrl.settings.retardSetA[0][sector] - swingeAngle;
          slmCtrl.settings.retardSetB[4][sector] =
                  slmCtrl.settings.retardSetB[0][sector];
          slmCtrl.setRetA(sector,
                  slmCtrl.settings.retardSetA[4][sector]);
          slmCtrl.setRetB(sector,
                  slmCtrl.settings.retardSetB[4][sector]);
          break;
      }
    }
  }

  void findExtinctionOnly() {
    setup(Layer.retarderA); // just to init. values for MeasurementPlotter
    String filename =FileUtil.getcwd() + "/" + "RetarderCalibration" + ".dat";
    MeasurementPlotter mPlot =
            new MeasurementPlotter("RetarderCalibration", numIDs, filename,
            new Range(0, 50), new Rectangle(10, 10, 400, 300));
    TaskFindExtinction task = new TaskFindExtinction(mPlot);
    task.execute();
  }

  class TaskFindExtinction
          extends SwingWorker {

    MeasurementPlotter mPlot;

    public TaskFindExtinction(MeasurementPlotter mPlot) {
      this.mPlot = mPlot;
    }
    final MultiSectorMeasurer acquireImageAndMeasure = new MultiSectorMeasurer();

    @Override
    public Object doInBackground() {
      // Find Extinction, setting 0
      findExtinction(mPlot);
      return null;
    }
  }

  public void findExtinction(MeasurementPlotter mPlot) {
    slmCtrl.setAllRetarderATo(SLMModel.extA);
    slmCtrl.setAllRetarderBTo(SLMModel.extB);

    double[] a = new double[numIDs];
    double[] b = new double[numIDs];

    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> findExtinction A coarse ...");
    for (int i = 0; i < a.length; i++) {
      a[i] = (double) SLMModel.HuntRangeRetarderA_a;
      b[i] = (double) SLMModel.HuntRangeRetarderA_b;
    }
    findExtinctionA(mPlot, a, b, slmModel.getTolCoarse());

    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> findExtinction B coarse ...");
    for (int i = 0; i < a.length; i++) {
      a[i] = (double) SLMModel.HuntRangeRetarderB_a;
      b[i] = (double) SLMModel.HuntRangeRetarderB_b;
    }
    findExtinctionB(mPlot, a, b, slmModel.getTolCoarse());

    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> findExtinction A fine ...");
    for (int i = 0; i < a.length; i++) {
      a[i] = (double) slmCtrl.settings.retardSetA[0][i] - SLMModel.deltaExtinct;
      b[i] = (double) slmCtrl.settings.retardSetA[0][i] + SLMModel.deltaExtinct;
    }
    findExtinctionA(mPlot, a, b, slmModel.getTolFine());

    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> findExtinction B fine ...");
    for (int i = 0; i < a.length; i++) {
      a[i] = (double) slmCtrl.settings.retardSetB[0][i] - SLMModel.deltaExtinct;
      b[i] = (double) slmCtrl.settings.retardSetB[0][i] + SLMModel.deltaExtinct;
    }
    findExtinctionB(mPlot, a, b, slmModel.getTolFine());

    measuredIntensity0 = this.getMeasuredValues().clone();
  }

  public void findExtinctionA(MeasurementPlotter mPlot, double[] a,
          double[] b, double tol) {
    double[] targets = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    this.setup(Layer.retarderA);
    this.minimizeToTargets(a, b, tol, targets, mPlot);
    slmCtrl.setCalibratedRetarderASettings(0, this.getSettings());
  }

  public void findExtinctionB(MeasurementPlotter mPlot, double[] a,
          double[] b, double tol) {
    double[] targets = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    this.setup(Layer.retarderB);
    this.minimizeToTargets(a, b, tol, targets, mPlot);
    slmCtrl.setCalibratedRetarderBSettings(0, this.getSettings());
  }

  public void measureSetting1(MeasurementPlotter mPlot) {
//        switch (layer) {
//            case retarderA:
//                targets = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
//                break;
//            case retarderB:
//                targets = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
//                break;
//            case elliptical:
//                targets = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
//                break;
//            case circular:
//                targets = new double[]{0, 0};
//                break;
//            }
    final MultiSectorMeasurer acquireImageAndMeasure = new MultiSectorMeasurer();
    System.out.println("measure setting 1");
    int step = (int) ((SLMModel.startMax - SLMModel.startMin) / SLMModel.numPoints);
    for (int s = 0; s < SLMModel.numPoints; s++) { // for each setting
      int setTo = (int) (SLMModel.startMin + s * step);
      for (int i = 0; i < numIDs; i++) { //for each sector
        settings[i] = setTo;
      }
      acquireImageAndMeasure.run();
      for (int i = 0; i < numIDs; i++) {
        mPlot.recordData(i, s, measurements[i]);
      }

    }

//        TaskMeasureSetting1 task = new TaskMeasureSetting1(mPlot);
//        task.execute();
  }


//    class TaskMeasureSetting1
//            extends SwingWorker {
//
//        MeasurementPlotter mPlot;
//
//
//        public TaskMeasureSetting1(MeasurementPlotter mPlot) {
//            this.mPlot = mPlot;
//        }
//
//
//        final MultiSectorMeasurer acquireImageAndMeasure = new MultiSectorMeasurer();
//
//
//        @Override
//        public Object doInBackground() {
//
//            System.out.println("measure setting 1");
//            int step = (int) ((SLMConstants.startMax - SLMConstants.startMin) / SLMConstants.numPoints);
//            for (int s = 0; s < SLMConstants.numPoints; s++) { // for each setting
//                int setTo = (int) (SLMConstants.startMin + s * step);
//                for (int i = 0; i < numIDs; i++) { //for each sector
//                    settings[i] = setTo;
//                }
//                acquireImageAndMeasure.run();
//                for (int i = 0; i < numIDs; i++) {
//                    mPlot.recordData(i, s, measurements[i]);
//                }
//
//            }
//            return null;
//        }
//
//
//    }
  private void matchSettingN(int n, MeasurementPlotter mPlot) {

    double[] a = new double[numIDs];
    double[] b = new double[numIDs];

    this.setup(Layer.retarderA);
    for (int i = 0; i < a.length; i++) {
      a[i] = slmCtrl.settings.retardSetA[n][i] - SLMModel.deltaExtinct;
      b[i] = slmCtrl.settings.retardSetA[n][i] + SLMModel.deltaExtinct;
    }
    this.minimizeToTargets(a, b, slmModel.getTolCoarse(), measuredIntensity1,
            mPlot);
    slmCtrl.setCalibratedRetarderASettings(n, this.getSettings());

    this.setup(Layer.retarderB);
    for (int i = 0; i < a.length; i++) {
      a[i] = slmCtrl.settings.retardSetB[n][i] - SLMModel.deltaExtinct;
      b[i] = slmCtrl.settings.retardSetB[n][i] + SLMModel.deltaExtinct;
    }
    this.minimizeToTargets(a, b, slmModel.getTolCoarse(), measuredIntensity1,
            mPlot);
    slmCtrl.setCalibratedRetarderBSettings(n, this.getSettings());
  // @todo repeat with tolFine
  }

  public void displayMeasuredIntensities() {
    System.out.println(">>> MeasuredIntensities: ");
    for (int i = 0; i < measuredIntensity1.length; i++) {
      System.out.println("    " + measuredIntensity1[i] + ", ");
    }
    System.out.println("  >> 2");
    for (int i = 0; i < measuredIntensity2.length; i++) {
      System.out.println("    " + measuredIntensity2[i] + ", ");
    }
    System.out.println("  >> 3");
    for (int i = 0; i < measuredIntensity3.length; i++) {
      System.out.println("    " + measuredIntensity3[i] + ", ");
    }
    System.out.println("  >> 4");
    for (int i = 0; i < measuredIntensity4.length; i++) {
      System.out.println("    " + measuredIntensity4[i] + ", ");
    }
    System.out.println("");
    System.out.println("<<< MeasuredIntensities");
  }


// </editor-fold> 

// <editor-fold defaultstate="collapsed" desc=">>>--- Minimizations... ---<<<" >
  public void minimizeToTargets(double[] startMin,
          double[] startMax,
          double tol,
          double[] targets,
          MeasurementPlotter mPlot) {
    // AcquireAndMeasure waits for all minimizers to send next setting,
    // using CyclicBarrier, then aquires an image, measures all the roi's,
    // then returns measurement to each minimizer.
    //
    // This is where the target retarder is indicated...
    // For balancing intensity (settings 2-4), pass in the setting1 intensities
    // in targets[]


    acqCtrl.start();

    MultiSectorMeasurer acquireImageAndMeasure = new MultiSectorMeasurer(mPlot);
    this.targets = targets;
    barrier = new CyclicBarrier(numIDs, acquireImageAndMeasure);

    final MinimizeFunction function = new MinimizeFunction();

    stopSignal = new CountDownLatch(numIDs);

    // create and launch minimizations
    minimizations = new BrentsMethod_IntDouble[numIDs];
    for (int i = 0; i < numIDs; i++) {
      minimizations[i] = new BrentsMethod_IntDouble(function, stopSignal);
    }

    for (int i = 0; i < numIDs; i++) {
      createMinimizationThread(i, startMin[i], startMax[i], tol);
    }
    // Await completion of all of the minimizations- CountDownLatch is released...
    System.out.println("* Waiting for stopSignal...");
    try {
      stopSignal.await();
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }

    System.out.println(">>>>> All minimizations done <<<<<");
    for (int i = 0; i <
            numIDs; i++) {
      //measurements[i] = minimizations[i].getMinX();
      System.out.println("    " + i + " setting: " + settings[i] + ", measure: " + measurements[i]);
    }

    mPlot.closeTextFile();
    acqCtrl.finish();
  }

  public double[] getMeasuredValues() {
    return measurements;
  }

  public double[] getSettings() {
    return settings;
  }

  public void createMinimizationThread(final int sector,
          final double a,
          final double b,
          final double tol) {
    new Thread() {

      @Override
      public void run() {
        double min = 0;
        min = minimizations[sector].minimize(sector, a, b, tol);
      }
    }.start();
  }

// Defines the function for minimization... 
  class MinimizeFunction
          implements Function_int_double {

    public MinimizeFunction() {
    }

    public double f(int i,
            double x) {
      // incoming value is stored in settings[] for application by acquireImageAndMeasure
      settings[i] = x;
      try {
        // barrier.await();  // wait for acquireImageAndMeasure to run
        barrier.await(100, TimeUnit.MILLISECONDS);
      //!TEST barrier.reset();
      } catch (BrokenBarrierException ez) {
        return -1;
      } catch (InterruptedException ex) {
        return -1;
      } catch (TimeoutException ex) {
        System.err.println("barrier timed-out, id=" + i);
        return -1;
      }
      // At this point, acquireImageAndMeasure has run and the
      // measured result is in measurements[]
      System.out.println("setting/measured: " + i + "  :  " +
              (int) settings[i] + "  /  " + (int) measurements[i]);
      return measurements[i];
    }
  }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc=">>>--- MultiSectorMeasurer ---<<<" >
  class MultiSectorMeasurer
          implements Runnable {

    MeasurementPlotter mPlot;
    int iteration = 0;
    byte[] data;

    public MultiSectorMeasurer() {
    }

    private MultiSectorMeasurer(MeasurementPlotter mPlot) {
      this.mPlot = mPlot;
      data = new byte[acqCtrl.getImageWidth() * acqCtrl.getImageHeight()];
    }

    public void run() {
      try {
        System.out.println("acquireImageAndMeasure");
        for (int i = 0; i < numIDs; i++) {
          // for testing using MockRetarders
          switch (layer) {
            case retarderA:
              slmCtrl.setRetA(i, (int) settings[i]);
              break;
            case retarderB:
              slmCtrl.setRetB(i, (int) settings[i]);
              break;
            case elliptical:
              slmCtrl.setElliptical(i, (int) settings[i]);
              break;
            case circular:
              slmCtrl.setCircular(i, (int) settings[i]);
              break;
          }
        }

        if (!SLMModel.useMockRetarders) {
          Thread.currentThread().sleep(slmModel.getSettlingtime());
          //byte[] data = acqCtrl.acquireSampleImage();
          acqCtrl.acquireImage(data);
          // test
//                    BufferedImage img = ImageFactoryGrayScale.createImage(acqCtrl.getImageWidth(), acqCtrl.getImageHeight(), 8, (byte[]) data);
//                    edu.mbl.jif.gui.imaging.FrameImageDisplay id = new edu.mbl.jif.gui.imaging.FrameImageDisplay(img, "Sample");
//                    id.setVisible(true);

          // measure intensity at ROIs
          ImageStatistics[] iStats = measureROIsIn(data,
                  acqCtrl.getImageWidth(),
                  acqCtrl.getImageHeight(),
                  rois);
          iteration++;
          for (int i = 0; i < numIDs; i++) {
            measurements[i] = Math.abs(iStats[i].meanInROI - slmModel.getZeroIntensity() - targets[i]);
            if (mPlot != null) {
              mPlot.recordData(i, iteration, measurements[i]);
            }
          }
        } else {  // useMockRetarders
          for (int i = 0; i < numIDs; i++) {
            // for testing using MockRetarders
            switch (layer) {
              case retarderA:
                measurements[i] = mockRet[i].calcA(settings[i]);
                break;
              case retarderB:
                measurements[i] = mockRet[i].calcB(settings[i]);
                break;
              case elliptical:
                measurements[i] = mockRet[i].calc(settings[i]);
                break;
              case circular:
                measurements[i] = mockRet[i].calc(settings[i]);
                break;
            }
            if (mPlot != null) {
              mPlot.recordData(i, iteration, measurements[i]);
            }
          }
        }
      } catch (InterruptedException ex) {
        Logger.getLogger(SLM_Calibrator.class.getName()).log(Level.SEVERE,
                null,
                ex);
      }
    }
  }

  public ImageStatistics[] measureROIsIn(byte[] data,
          int imageW, int imageH,
          Rectangle[] rois) {
    ImageStatistics[] stats = new ImageStatistics[numIDs];
    for (int i = 0; i <
            numIDs; i++) {
      stats[i] = ImageAnalyzer.getStatsOnlyROI(data,
              rois[i], new Dimension((int) imageW, (int) imageH));
    }

    return stats;
  }

  public void measureROIs() {
    targets = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    // System.out.println("acquireImageAndMeasure");
    byte[] data = acqCtrl.acquireSampleImage();
    // measure intensity at ROIs
    ImageStatistics[] iStats = measureROIsIn(data,
            acqCtrl.getImageWidth(), acqCtrl.getImageHeight(),
            rois);
    for (int i = 0; i <
            numIDs; i++) {
      measurements[i] = Math.abs(iStats[i].meanInROI - slmModel.getZeroIntensity() - targets[i]);
    }
    IJ.log("\nMeasured ROIs:");
    for (int i = 0; i <
            numIDs; i++) {
      IJ.log("   sector " + i + ": " //+ settings[i]
              + " = " + measurements[i]);
    }

  }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc=">>>--- DataSet ---<<<" >
  class DataSet {

    int id;
    double[] setting;
    double[] measurement;

    public DataSet(final int id,
            int numPoints) {
      this.id = id;
      setting = new double[numPoints];
      measurement = new double[numPoints];
    }

    public void addPoint(int point,
            final double setting,
            final double measurement) {
      this.setting[point] = setting;
      this.measurement[point] = measurement;
    }

    public double getSetting(int point) {
      return setting[point];
    }

    public double getMeasurement(int point) {
      return measurement[point];
    }
  }

  private void createDataSets(int numIDs) {
    dataSets = new DataSet[numIDs];
    for (int i = 0; i <
            numIDs; i++) {
      dataSets[i] = new DataSet(i, SLMModel.numPoints);
    }

  }

  public void dumpDataSet() {
    IJ.log(">>>>>>>>>>>>>>>>>>>>>>>> DataSet Dump:");
    if (dataSets != null) {
      for (int i = 0; i <
              numIDs; i++) {
        for (int j = 0; j <
                SLMModel.numPoints; j++) {
          IJ.log(i + ", " + j + " == " +
                  dataSets[i].getMeasurement(j) + " @ " +
                  dataSets[i].getSetting(j));
        }

      }
    }
    IJ.log("<<<<<<<<<<<<<<<<<<<<<<<< DataSet Dump");

  }
// </editor-fold>
}
