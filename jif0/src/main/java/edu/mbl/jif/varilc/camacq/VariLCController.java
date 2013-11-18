package edu.mbl.jif.varilc.camacq;

import edu.mbl.jif.varilc.camacq.VariLC_RT;
import edu.mbl.jif.acq.AcquisitionController;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.device.SerialDevice;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.ps.PSAcquisitionController;
import edu.mbl.jif.varilc.VLCController;
import edu.mbl.jif.varilc.VariLCModel;

/**
 *
 * @author GBH
 */
public class VariLCController extends SerialDevice implements VLCController {

	private InstrumentController instrumentCtrl;
	private VariLCModel vlcModel;
	private VariLC_RT vlcRT;
	public String PORT_OWNER_NAME = "VariLC";
	private boolean initialized;
	private boolean isCalibrated;
	private AcquisitionController acqCtrl;
	PSAcquisitionController pSAcquisitionController;


	public VariLCController(InstrumentController instrumentCtrl,
			String portOwnerName, String commPortName) {
		super(instrumentCtrl, portOwnerName, commPortName);
		this.instrumentCtrl = instrumentCtrl;
		this.acqCtrl = (AcquisitionController) instrumentCtrl.getController("acq");
		// ?? Does pSAcquisitionController need to be added to instrumentCtrl controllers ?
		initialized = true;
	}

	@Override
	public boolean setupVLC() {
		if (!isOpen()) {
			return false;
		}
		this.vlcModel = (VariLCModel) instrumentCtrl.getModel("variLC");
		if (vlcModel == null) {
			Application.getInstance().error("VariLCModel = null");
			return false;
		}
		try {
			vlcRT = new VariLC_RT(this.getSerialPortConnection());
		} catch (Exception ex2) {
			System.err.println("Exception on new VariLC:" + ex2.getMessage());
			return false;
		}
		// openMonitor();
//        new Thread(new Runnable() {
//            public void run() {
		initializeController();
//            }
//        }).start();
		initialized = true;
		return true;
	}

	@Override
	public boolean isInitialized() {
		return initialized;

	}

	@Override
	public boolean initializeController() {
		// If (Include VariLC_RT in Initialization)
		String resp;
		boolean responsive = vlcRT.checkIfResponsive();
		if (!responsive) {
			//      DialogBoxes.boxError("VariLC_RT Initialization Error",
			//          "Port opened, but VariLC_RT does not respond.\n"
			//          +
			//          "(Quit and re-start PSjCheck after checking VariLC_RT and connections.)");
			//      PSjUtils.event("VariLC_RT initialization failed");
			System.err.println("VariLC_RT initialization failed");
			Application.getInstance().error(
					"No response from VariLC on port " + this.getCommPortName());
			//isFunctional = false;
			return false;
		}
		vlcRT.reset();
		vlcRT.setCommandFormat(vlcModel.getCommandFormat());
		vlcRT.setUnits(0); // nanometers

		vlcRT.setWavelength((float) vlcModel.getWavelength());
		vlcRT.setSettlingTime(vlcModel.getSettleTime());
		resp = vlcRT.sendCommandAndWait("I?", 500); // initialize

		isCalibrated = false;
		transmitAllElements();
		//isFunctional = true;
		System.out.println("VariLC_RT initialized");
		return true;
	}

	// <editor-fold defaultstate="collapsed" desc=">>>--- Define & Set  ---<<<" >
	@Override
	public int setRetardance(double retA, double retB) {
		return vlcRT.setRetardance((float) retA, (float) retB);
	}

	@Override
	public int setRetardance(float retA, float retB) {
		return vlcRT.setRetardance(retA, retB);
	}

	/**
	 * Sets default values for Extinction to the current value of A0 and B0
	 */
	@Override
	public void setExtinctionDefaultsToCurrent() {
		vlcModel.setExtinctA(vlcModel.getA0());
		vlcModel.setExtinctB(vlcModel.getB0());
	}

	@Override
	public void setRetardersToNominalSwing() {
		double extA = vlcModel.getExtinctA();
		double extB = vlcModel.getExtinctB();
		double LC_Swing = vlcModel.getSwing();
		vlcModel.setA0(extA);
		vlcModel.setB0(extB);
		vlcModel.setA1(extA + LC_Swing);
		vlcModel.setB1(extB);
		vlcModel.setA2(extA);
		vlcModel.setB2(extB + LC_Swing);
		vlcModel.setA3(extA);
		vlcModel.setB3(extB - LC_Swing);
		vlcModel.setA4(extA - LC_Swing);
		vlcModel.setB4(extB);
		transmitAllElements();
		//vlcRT.getDefinedElements();
		//vlcRT.showElements();
	}

	@Override
	public void transmitAllElements() {
		defineElement(0);
		defineElement(1);
		defineElement(2);
		defineElement(3);
		defineElement(4);
	}

	@Override
	public void defineElementAndMeasure(int element) {
		defineElement(element);
		//this.measureSetting(element);
	}

	@Override
	public void defineElement(int element) {
		switch (element) {
			case 0:
				vlcRT.setRetardance((float) vlcModel.getA0(),
						(float) vlcModel.getB0());
				break;
			case 1:
				vlcRT.setRetardance((float) vlcModel.getA1(),
						(float) vlcModel.getB1());
				break;
			case 2:
				vlcRT.setRetardance((float) vlcModel.getA2(),
						(float) vlcModel.getB2());
				break;
			case 3:
				vlcRT.setRetardance((float) vlcModel.getA3(),
						(float) vlcModel.getB3());
				break;
			case 4:
				vlcRT.setRetardance((float) vlcModel.getA4(),
						(float) vlcModel.getB4());
				break;
			default:
				;
		}
		vlcRT.defineElement(element);

		//TimerHR.waitMillisecs(defineLatency);
		//TimerHR.waitMillisecs(getSettlingTime());
	}

	@Override
	public void reset() {
		vlcRT.reset();
	}

	@Override
	public void statusCheck() {
		if (vlcRT != null) {
			if (vlcRT.statusCheck()) {
				Application.getInstance().error("VariLC reports an error.  (Do a reset)");
			}
		}
	}
	// </editor-fold>


	@Override
	public void selectElement(int n) {
		vlcRT.selectElement(n);
	}

	@Override
	public void selectElementWait(int n) {
		vlcRT.selectElementWait(n);
	}



// ?????????????
	public void test() {
		acqCtrl.testMeasurement();
	}

}