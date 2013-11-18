/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.varilc.camacq;

import edu.mbl.jif.acq.AcquisitionController;
import edu.mbl.jif.camacq.CamAcq;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.camera.ImageAnalyzer;
import edu.mbl.jif.camera.ImageStatistics;
import edu.mbl.jif.gui.imaging.IntensityWatcher;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.utils.DialogBox;
import edu.mbl.jif.varilc.*;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author GBH
 */
public class VariLCMeasurer implements VLCMeasurer {
	
	private static final boolean USE_STREAMING_MEASURE = true;
	
	private VariLCModel vlcModel;
	private VLCController vlcCtrl;
	private Measurer measurer;
	private AcquisitionController acqCtrl;
	private InstrumentController instrumentCtrl;
	//IntensityWatcher iWatcher;
	
	
	public VariLCMeasurer(VariLCModel vlcm, VLCController vlcCtrl, AcquisitionController acqCtrl) {
		this.vlcModel = vlcm;
		this.vlcCtrl = vlcCtrl;
		this.acqCtrl = acqCtrl;
	}
	
	
	// Called from CompensatorSettingPanel when button is pushed for a setting
	@Override
	public void setElementAndMeasure(final int n) {
		if (setupForMeasurements()) {
			vlcModel.setSelectedSetting(n);
			MeasureTask measureTask = new MeasureTask(n, true);
			measureTask.execute();
		}
	}

	@Override
	public void measureSetting(final int n) {
		if (setupForMeasurements()) {
			MeasureTask measureTask = new MeasureTask(n, false);
			measureTask.execute();
		}
	}

	@Override
	public float calcExtinctionRatio() {
		int zeroIntensity = 5;
		// @todo hook this to actual zeroIntensity
		// Math.round(Prefs.usr.getFloat("acq_zeroIntensity", 6));
		double intensityRatio = (vlcModel.getIntensity1() - zeroIntensity)
				/ (vlcModel.getIntensity0() - zeroIntensity);
		float extinctionRatio =
				(float) (intensityRatio / Math.pow(Math.tan(vlcModel.getSwing() * Math.PI), 2));
		return extinctionRatio;
	}

	@Override
	public double checkVariance() {
		double[] intensities = {vlcModel.getIntensity1(), vlcModel.getIntensity2(),
			vlcModel.getIntensity3(), vlcModel.getIntensity4()};
		return stdDev(intensities);
	}

	@Override
	public double acquireAndMeasure(Rectangle roi, boolean isDisplayLastAcquired) {
		double measured = 0;
		//try {
		//System.out.println("acquireImageAndMeasure");
		//Thread.currentThread().sleep(this.vlcModel.getSettleTime());
		byte[] data = acqCtrl.acquireSampleImage();
		//this.acqCtrl.acquireImage(data);
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
//        } catch (InterruptedException ex) {
//            Logger.getLogger(CalibratorNew.class.getName()).log(Level.SEVERE,
//                null,
//                ex);
//        }
		return measured;
	}

	
	
	// <editor-fold defaultstate="collapsed" desc=">>>--- Run Calibration ---<<<" >
	@Override
	public void runCalibration() {
		// if (setupForMeasurements()) {
		// Check for ROI set... if not, use default
		Rectangle roi = CamAcq.getInstance().getSelectedROI();
		if (roi.getWidth() == 0) {
			int w = CamAcq.getInstance().getWidth();
			int h = CamAcq.getInstance().getHeight();
			roi = new Rectangle(w / 2, h / 2, 32, 32);
			CamAcq.getInstance().setSelectedROI(roi);
			// May have an EDT issue here...
		}
		acqCtrl.displaySuspend();
		CalibratorNew calib = new CalibratorNew(vlcCtrl, vlcModel, this, roi, instrumentCtrl);
		calib.doCalibration();
		//   }
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="<<< Measurement >>>-----------------------">
    /*
	 * ### This was an experiment to do calib while streaming... using IntensityWatcher
	 */
	@Override
	public void measureAll() {
		if (setupForMeasurements()) {
			System.out.println("runningTest");
			(new MeasureAllTask()).execute();
		}
	}

	@Override
	public boolean setupForMeasurements() {
		DisplayLiveCamera dlc = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
		if (dlc == null) {
			DialogBox.boxNotify("Note",
					"Display must be open for VariLC measurements");
			return false;
		}
		if (dlc.getSelectedROI().getX() == 0) {
			DialogBox.boxNotify("Note",
					"No ROI selected for measurement");
			return false;
		}

//        if (iWatcher == null) {
//            iWatcher = new IntensityWatcher(); //(1, "./intensity.csv");
//            dlc.getImageDisplayPanel().addRoiChangeListener(iWatcher);
//            iWatcher.setStabilityLevel((float) vlcModel.getStabilityLevel());
//            iWatcher.setMeasurementFreq(1);
//            measurer = new Measurer(this, iWatcher);
//        }
		//System.out.println("setupForMeasurements complete");
		return true;
	}

	// <editor-fold defaultstate="collapsed" desc="<<< MeasureAllTask SwingWorker >>>">
	//
	// SettingMeasurement just holds the setting number along with the measured intensity
	class SettingMeasurement {

		final int setting;
		final float measurement;

		SettingMeasurement(int setting, float measurement) {
			this.setting = setting;
			this.measurement = measurement;
		}

	}

	// Measure intensity for all settings
	class MeasureAllTask extends SwingWorker<Void, SettingMeasurement> {

		@Override
		protected Void doInBackground() {
			DisplayLiveCamera dlc = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
			if (dlc == null) {
				//iWatcher = null;
				DialogBox.boxNotify("Note",
						"Display must be open for VariLC measurements");
				return null;
			}
//      Rectangle roi = CamAcq.getInstance().getSelectedROI();
//      if (roi.getWidth() == 0) {
//        int w = CamAcq.getInstance().getWidth();
//        int h = CamAcq.getInstance().getHeight();
//        roi = new Rectangle(w / 2, h / 2, 32, 32);
//      }
			for (int i = 0; i < 5; i++) {
				float intensity;
				if (USE_STREAMING_MEASURE) {
					//selectElement(i);
					intensity = getStableIntensity(i, dlc);
				} else {
//          selectElementWait(i);
//          intensity = (float) acquireAndMeasure(roi, true);
				}
				publish(new SettingMeasurement(i, intensity));
			}
			return null;
		}

		@Override
		protected void process(List<SettingMeasurement> measurements) {
			for (SettingMeasurement measure : measurements) {
				int setting = measure.setting;
				float mean = measure.measurement;
				switch (setting) {
					case 0:
						vlcModel.setIntensity0(mean);
						break;
					case 1:
						vlcModel.setIntensity1(mean);
						break;
					case 2:
						vlcModel.setIntensity2(mean);
						break;
					case 3:
						vlcModel.setIntensity3(mean);
						break;
					case 4:
						vlcModel.setIntensity4(mean);
						break;
					default:
						;
				}
			}
		}

		@Override
		protected void done() {
			double stdDev = checkVariance();
			float extinct = calcExtinctionRatio();
			vlcModel.setExtinctionRatio(extinct);
			DisplayLiveCamera disp = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
			if (disp != null) {
				disp.restart();
			}
		}

	}

	// Measure intensity for a single setting
	// with or without selecting the setting first (selectFirst)
	// (i.e. it is already set to the desired setting (as in having just been defined))
	class MeasureTask
			extends SwingWorker<Void, SettingMeasurement> {

		private int setting;
		private boolean selectFirst;

		public MeasureTask(int setting, boolean selectFirst) {
			this.setting = setting;
			this.selectFirst = selectFirst;
		}

		@Override
		protected Void doInBackground() {
			DisplayLiveCamera dlc = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
			if (dlc == null) {
				//iWatcher = null;
				DialogBox.boxNotify("Note",
						"Display must be open for VariLC measurements");
				return null;
			}

			Rectangle roi = CamAcq.getInstance().getSelectedROI();
			if (roi.getWidth() == 0) {
				int w = CamAcq.getInstance().getWidth();
				int h = CamAcq.getInstance().getHeight();
				roi = new Rectangle(w / 2, h / 2, 32, 32);
			}
			float intensity;
			if (USE_STREAMING_MEASURE) {
			// if (selectFirst) {
			//	selectElement(setting);
			// }
				intensity = getStableIntensity(setting, dlc);
			} else {
				if (selectFirst) {
					vlcCtrl.selectElementWait(setting);
				}
				intensity = (float) acquireAndMeasure(roi, false);
			}
			publish(new SettingMeasurement(setting, intensity));
			return null;
		}

		@Override
		protected void process(List<SettingMeasurement> measurements) {
			for (SettingMeasurement measure : measurements) {
				int setting = measure.setting;
				float mean = measure.measurement;
				switch (setting) {
					case 0:
						vlcModel.setIntensity0(mean);
						break;
					case 1:
						vlcModel.setIntensity1(mean);
						break;
					case 2:
						vlcModel.setIntensity2(mean);
						break;
					case 3:
						vlcModel.setIntensity3(mean);
						break;
					case 4:
						vlcModel.setIntensity4(mean);
						break;
					default:
						;
				}
			}
		}

		@Override
		protected void done() {
		}

	}
	// </editor-fold>

	
	// <editor-fold defaultstate="collapsed" desc=">>>--- Statistical functions ---<<<" >
	public static double stdDev(double[] data) {
		return Math.sqrt(variance(data));
	}

	public static double variance(double[] data) {
		final int n = data.length;
		if (n < 2) {
			return Double.NaN;
		}
		double avg = data[0];
		double sum = 0;
		for (int i = 1; i < data.length; i++) {
			double newavg = avg + ((data[i] - avg) / (i + 1));
			sum += ((data[i] - avg) * (data[i] - newavg));
			avg = newavg;
		}
		return sum / (double) (n - 1);
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc=" Streaming Measurement - TEST ">
	// Streaming Measurement - TEST
	public boolean testMeasurementStreaming() {
		testMeasurementStreaming2(vlcCtrl);
		return true;
	}
	
	public boolean testMeasurementStreaming2(final VLCController vlc) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				DisplayLiveCamera dlc = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
				if (dlc == null) {
					//iWatcher = null;
					DialogBox.boxNotify("Note",
							"Display must be open for VariLC measurements");
					return;
				}
				float intensity = getStableIntensity(0, dlc);
				System.out.println("Result of Measurement: " + intensity);
			}
			
		};
		(new Thread(runnable)).start();
		return true;
	}
	
	public float getStableIntensity(int setting, DisplayLiveCamera dlc) {
		IntensityWatcher iWatcher = new IntensityWatcher();
		dlc.getImageDisplayPanel().addRoiChangeListener(iWatcher);
		iWatcher.setStabilityLevel((float) vlcModel.getStabilityLevel());
		iWatcher.setMeasurementFreq(1);
		measurer = new Measurer(vlcCtrl, iWatcher);
		//measurer.setTimeOut((int)getTimeToStable());
		measurer.setTimeOut(1000);
		Measurer.MeasurementTask mTask = measurer.doCommand(setting);
		float intensity = measurer.waitFor(mTask);
		return intensity;
	}
	
	@Override
	public long getTimeToStable() {
		CameraModel cam = (CameraModel) ((InstrumentController) CamAcqJ.getInstance().getController()).getModel("camera");
		if (cam == null) {
			System.err.println("cameraModel null");
			return -1;
		}
		long exp = (long) cam.getExposureStream();
		long maxTimeToStable = (2 * vlcModel.getSettleTime() + exp);
		return maxTimeToStable;
	}

//  public float getMean() {
//    if (iWatcher != null) {
//      long maxTimeToStable = getTimeToStable();
//      try {
////            System.out.println("maxTimeToStable: " + maxTimeToStable);
//        Thread.sleep(maxTimeToStable);
//      } catch (InterruptedException ex) {
//        ex.printStackTrace();
//      }
//      float mean = iWatcher.getMean();
//      //float mean = iWatcher.getStableMean(maxTimeToStable);
//      //long time = iWatcher.getTimeToStable();
//      //System.out.println("mean: " + mean + "  time: " + time);
//      return mean;
//    }
//    return -1.0f;
//  }
// </editor-fold>
//==================================================	
}
