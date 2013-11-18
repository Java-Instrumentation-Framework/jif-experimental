 /*
 * CalibratorNew.java
 *
 * Created on March 2, 2007, 5:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.varilc;

import edu.mbl.jif.acq.AcqModel;
import edu.mbl.jif.acq.AcquisitionController;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.camera.ImageAnalyzer;
import edu.mbl.jif.camera.ImageStatistics;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.utils.analytic.BrentsMethod;
import edu.mbl.jif.utils.analytic.Function;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc=">>>--- DataSet ---<<<" >
/**
 *
 * @author GBH
 */
public class CalibratorNew {

	private VariLCModel vlcModel;
	private VLCController vlcCtrl;
	private VLCMeasurer vlcMeasurer;
	InstrumentController instrumentCtrl;
	AcquisitionController acqCtrl;
	AcqModel acqModel;
	Rectangle roi;
	double setting;
	double measurement;
	//double target;
	double measuredIntensity0;
	double measuredIntensity1;
	double measuredIntensity2;
	double measuredIntensity3;
	double measuredIntensity4;
	//
	boolean doPlots = false;
	private long timeToStable;
	//IntensityWatcher iWatcher;
	MockRetarder mockRet;
	private boolean useMockRetarders = false;
	MeasurementPlotter_VLC mPlot;
	float lastSettingA;
	float lastSettingB;
	byte[] data;
	double a0;
	double a1;
	double a2;
	double a3;
	double a4;
	double b0;
	double b1;
	double b2;
	double b3;
	double b4;
	double a0_was;
	double a1_was;
	double a2_was;
	double a3_was;
	double a4_was;
	double b0_was;
	double b1_was;
	double b2_was;
	double b3_was;
	double b4_was;
	private double target;
	private boolean doRetarderA;
	double swingAngleHalf;
	private boolean isDisplayLastAcquired = true;

	/**
	 * Creates a new instance of CalibratorNew
	 */
	public CalibratorNew(VLCController vlcCtrl, 
			VariLCModel vlcModel,
			VLCMeasurer vlcMeasurer,
			Rectangle roi,
			InstrumentController instrumentCtrl) {
		this.vlcCtrl = vlcCtrl;
		this.vlcModel = vlcModel;
		this.vlcMeasurer = vlcMeasurer;
		this.roi = roi;
		this.instrumentCtrl = instrumentCtrl;
		acqCtrl = (AcquisitionController) instrumentCtrl.getController("acq");
		acqModel = (AcqModel) instrumentCtrl.getModel("acq");
		// iWatcher = new IntensityWatcher();
		data = new byte[acqCtrl.getImageWidth() * acqCtrl.getImageHeight()];
		setup();
	}

// <editor-fold defaultstate="collapsed" desc=">>>--- Setup ---<<<" >
	public void setup() {
		if (useMockRetarders) {
			createMockRetarder();
		}
		String filename = null; //com.myjavatools.lib.Files.getcwd() + "/" + "RetarderCalibration" + ".dat";
		mPlot = new MeasurementPlotter_VLC("RetarderCalibration",
				filename, 0, 255, new Rectangle(10, 10, 400, 300));
		rememberCurrentSettings();
		swingAngleHalf = vlcModel.getSwing() / 2;
	}

	public void doCalibration() {
		TaskCalibration task = new TaskCalibration();
		task.execute();
	}

	class TaskCalibration
			extends SwingWorker {

		public TaskCalibration() {
		}

		int acqDepthWas;
		int multiFramesWas;

		@Override
		public Object doInBackground() {
			if (!useMockRetarders) {
				// stop streaming.
				// try this....
//                acqCtrl.displaySuspend();
//                acqDepthWas = acqModel.getDepth();
//                acqModel.setDepth(8);
//                multiFramesWas = acqModel.getMultiFrame();
//                acqModel.setMultiFrame(1);
				acqCtrl.start();
			}
			calibrateRetarders();
			// Find Extinction, setting 0
			//findExtinction();
			if (!useMockRetarders) {
				acqCtrl.finish();
//                acqModel.setDepth(acqDepthWas);
//                acqModel.setMultiFrame(multiFramesWas);
			}
			return null;
		}

		@Override
		protected void done() {
			if (isCancelled()) {
				// cancelled
				// restore, but...
			} else {
				defineAllNewSettings();
				vlcMeasurer.measureAll();
				vlcModel.setLastCalibrated(System.currentTimeMillis());
			}
		}

	}

	private void rememberCurrentSettings() {
		a0_was = vlcModel.getA0();
		a1_was = vlcModel.getA1();
		a2_was = vlcModel.getA2();
		a3_was = vlcModel.getA3();
		a4_was = vlcModel.getA4();
		b0_was = vlcModel.getB0();
		b1_was = vlcModel.getB1();
		b2_was = vlcModel.getB2();
		b3_was = vlcModel.getB3();
		b4_was = vlcModel.getB4();
	}

	// if cancelled, restorePriorSettings();
	private void restorePriorSettings() {
		vlcModel.setA0(a0_was);
		vlcModel.setA1(a1_was);
		vlcModel.setA2(a2_was);
		vlcModel.setA3(a3_was);
		vlcModel.setA4(a4_was);
		vlcModel.setB0(b0_was);
		vlcModel.setB1(b1_was);
		vlcModel.setB2(b2_was);
		vlcModel.setB3(b3_was);
		vlcModel.setB4(b4_was);
	}

	private void defineAllNewSettings() {
		vlcModel.setA0(a0);
		vlcModel.setA1(a1);
		vlcModel.setA2(a2);
		vlcModel.setA3(a3);
		vlcModel.setA4(a4);
		vlcModel.setB0(b0);
		vlcModel.setB1(b1);
		vlcModel.setB2(b2);
		vlcModel.setB3(b3);
		vlcModel.setB4(b4);
		// @todo Add record calibration settings()
		rememberCurrentSettings();
	}

	private void createMockRetarder() {
		double parameterA = 4000;
		double minXA = .25;
		double minimumA = 7;
		double parameterB = 3500;
		double minXB = .5;
		double minimumB = 8;
		mockRet = new MockRetarder(
				parameterA,
				minXA,
				minimumA,
				parameterB,
				minXB,
				minimumB);
	}
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc=">>>--- Retarder Calibration ---<<<" >
	public void calibrateRetarders() {
		//      setElement(0, extinctA, extinctB);
		//      setElement(1, extinctA + LC_Swing, extinctB);
		//      setElement(2, extinctA, extinctB + LC_Swing);
		//      setElement(3, extinctA, extinctB - LC_Swing);
		//      setElement(4, extinctA - LC_Swing, extinctB);
		// Find Extinction, setting 0
		// ??? setNominalSwing(0);
		System.out.println("findExtinction  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		findExtinction();
		System.out.println("measureSetting 1 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		measureSetting1();
		if (mPlot != null) {
			mPlot.setColor(Color.RED);
		}
		System.out.println("matchSetting 2 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		matchSetting2();
		System.out.println("matchSetting 3 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		matchSetting3();
		System.out.println("matchSetting 4 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		matchSetting4();
		// if not cancelled
	}

	public void findExtinction() {
		//vlcCtrl.setRetardersToNominalSwing();
		a0 = vlcModel.getA0();
		b0 = vlcModel.getB0();
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> findExtinction A coarse ...");
		setRetardance(a0, b0);
		double a = a0 - swingAngleHalf;
		double b = a0 + swingAngleHalf;
		System.out.println("a,b " + a + ", " + b);
		findExtinctionA(a, b, vlcModel.getToleranceGross());

		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> findExtinction B coarse ...");
		a = b0 - swingAngleHalf;
		b = b0 + swingAngleHalf;
		findExtinctionB(a, b, vlcModel.getToleranceGross());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> findExtinction A fine ...");
		a = a0 - swingAngleHalf / 2;
		b = a0 + swingAngleHalf / 2;
		findExtinctionA(a, b, vlcModel.getToleranceFine());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> findExtinction B fine ...");
		a = b0 - swingAngleHalf / 2;
		b = b0 + swingAngleHalf / 2;
		findExtinctionB(a, b, vlcModel.getToleranceFine());
		measuredIntensity0 = this.acquireAndMeasure();
	}

	public void findExtinctionA(double a, double b, double tol) {
		this.minimizeToTargets(a, b, tol, 0, true);
		a0 = lastSettingA;
		b0 = lastSettingB;
	}

	public void findExtinctionB(double a, double b, double tol) {
		this.minimizeToTargets(a, b, tol, 0, false);
		a0 = lastSettingA;
		b0 = lastSettingB;
	}

	public void measureSetting1() {
		a1 = a0 + vlcModel.getSwing();
		b1 = b0;
		setRetardance(a1, b1);
		measuredIntensity1 = acquireAndMeasure();
		System.out.println("measureSetting1: >>>>>>>>>>>>>>>>>>> " + measuredIntensity1);
		if (mPlot != null) {
			mPlot.recordData(measuredIntensity1);
		}
	}

	private void matchSetting2() {
		a2 = a0;
		b2 = b0 + vlcModel.getSwing();
		setRetardance(a2, b2);
		// B is varied
		double a = b2 - swingAngleHalf;
		double b = b2 + swingAngleHalf;
		minimizeToTargets(a, b, vlcModel.getToleranceFine(), measuredIntensity1,
				false);
		measuredIntensity2 = acquireAndMeasure();
		a2 = a0;
		b2 = lastSettingB;
	}

	private void matchSetting3() {
		a3 = a0;
		b3 = b0 - vlcModel.getSwing();
		setRetardance(a3, b3);
		// B is varied
		double a = b3 - swingAngleHalf;
		double b = b3 + swingAngleHalf;
		minimizeToTargets(a, b, vlcModel.getToleranceFine(), measuredIntensity1,
				false);
		measuredIntensity2 = acquireAndMeasure();
		a3 = a0;
		b3 = lastSettingB;
	}

	private void matchSetting4() {
		a4 = a0 - vlcModel.getSwing();
		b4 = b0;
		setRetardance(a4, b4);
		// A is varied
		double a = a4 - swingAngleHalf;
		double b = a4 + swingAngleHalf;
		minimizeToTargets(a, b, vlcModel.getToleranceFine(), measuredIntensity1,
				true);
		measuredIntensity2 = acquireAndMeasure();
		a4 = lastSettingA;
		b4 = b0;
	}

	void setRetardance(double A, double B) {
		vlcCtrl.setRetardance(A, B);
		// System.out.println(">>> " + A + ", " + B);
		lastSettingA = (float) A;
		lastSettingB = (float) B;
	}
// </editor-fold> 

// <editor-fold defaultstate="collapsed" desc=">>>--- Minimizations... ---<<<" >
	public void minimizeToTargets(double startMin,
			double startMax,
			double tol,
			double target,
			boolean doRetarderA) {
		this.target = target;
		this.doRetarderA = doRetarderA;
		final MinimizeFunction function = new MinimizeFunction();
		BrentsMethod minimization = new BrentsMethod(function);
		int maxIters = vlcModel.getMaxIterations();
		minimization.minimize(startMin, startMax, tol, maxIters);
		//mPlot.closeTextFile();
	}

	public static void testMminimizeToTargets(double startMin,
			double startMax,
			double tol,
			int maxIters) {
		double parameterA = 400;
		double minXA = .25;
		double minimumA = 7;
		double parameterB = 200;
		double minXB = .5;
		double minimumB = 8;
		final MockRetarder mockRet = new MockRetarder(
				parameterA,
				minXA,
				minimumA,
				parameterB,
				minXB,
				minimumB);

		final Function function = new Function() {

			public double f(double x) {
				double measure = mockRet.calc(x);
				System.out.println(x + " -> " + measure);
				return measure;
			}

		};
		BrentsMethod minimization = new BrentsMethod(function);
		minimization.minimize(startMin, startMax, tol, maxIters);
	}

	// Defines the function for minimization... 
	class MinimizeFunction
			implements Function {

		public MinimizeFunction() {
		}

		public double f(double x) {
			// incoming value is stored in settings[] for application by acquireImageAndMeasure
			setting = x;
			acquireAndMeasure(x);
			//System.out.printf("set: " + x + "          " + measurement);
			System.out.printf("set: %.3f       %.3f\n", x, measurement);
			return measurement;
		}

	}

// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc=" Acquire and Measure ">
	public void acquireAndMeasure(double setting) {
		try {
			//System.out.println("acquireImageAndMeasure");

			// for testing using MockRetarders
			if (doRetarderA) {
				lastSettingA = (float) setting;
				setRetardance(lastSettingA, lastSettingB);
			} else {
				lastSettingB = (float) setting;
				setRetardance(lastSettingA, lastSettingB);
			}
			if (!useMockRetarders) {
				Thread.currentThread().sleep(vlcModel.getSettleTime());
				//byte[] data = acqCtrl.acquireSampleImage();
				acqCtrl.acquireImage(data);
				// test
//                    BufferedImage img = ImageFactoryGrayScale.createImage(acqCtrl.getImageWidth(), acqCtrl.getImageHeight(), 8, (byte[]) data);
//                    edu.mbl.jif.gui.imaging.FrameImageDisplay id = new edu.mbl.jif.gui.imaging.FrameImageDisplay(img, "Sample");
//                    id.setVisible(true);

				// measure intensity at ROIs
				ImageStatistics iStats = ImageAnalyzer.getStatsOnlyROI(
						data,
						roi,
						new Dimension(acqCtrl.getImageWidth(),
						acqCtrl.getImageHeight()));
				double intensity = iStats.meanInROI;
				measurement = Math.abs(intensity - target);
				if (mPlot != null) {
					mPlot.recordData(intensity);
				}

			} else {  // useMockRetarders
				// for testing using MockRetarders
				double intensity = mockRet.calc(lastSettingA, lastSettingB);
				measurement = Math.abs(intensity - target);
//                if (doRetarderA) {
//                    measurement = mockRet.calcA(setting);
//                } else {
//                    measurement = mockRet.calcB(setting);
//                }
				System.out.println("============================================================ " + intensity);

				if (mPlot != null) {
					mPlot.recordData(intensity);
				}
			}
		} catch (InterruptedException ex) {
			Logger.getLogger(CalibratorNew.class.getName()).log(Level.SEVERE,
					null,
					ex);
		}
	}

	public double acquireAndMeasure() {
		double measured = 0;
		try {
			//System.out.println("acquireImageAndMeasure");
			if (!useMockRetarders) {
				Thread.currentThread().sleep(vlcModel.getSettleTime());
				//byte[] data = acqCtrl.acquireSampleImage();
				acqCtrl.acquireImage(data);
				// test

				if (isDisplayLastAcquired) {
					BufferedImage img = acqCtrl.byteArrayToBufferedImage(data);
					DisplayLiveCamera disp = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
					if (disp != null) {
						disp.getImageDisplayPanel().showImage(img);
					}
				}

				// measure intensity at ROIs
				ImageStatistics iStats = ImageAnalyzer.getStatsOnlyROI(
						data,
						roi,
						new Dimension(acqCtrl.getImageWidth(),
						acqCtrl.getImageHeight()));

				measured = iStats.meanInROI;
				if (mPlot != null) {
					mPlot.recordData(measured);
				}
			} else {  // useMockRetarders
				// for testing using MockRetarders
				measured = mockRet.calc(lastSettingA, lastSettingB);
				System.out.println(
						"=*========================================================== " + measurement);
				if (mPlot != null) {
					mPlot.recordData(measured);
				}
			}
		} catch (InterruptedException ex) {
			Logger.getLogger(CalibratorNew.class.getName()).log(Level.SEVERE,
					null,
					ex);
		}
		return measured;
	}

// </editor-fold>
	public static void main(String[] args) {
		testMminimizeToTargets(.2, .5, 0.0001, 50);
	}

}
