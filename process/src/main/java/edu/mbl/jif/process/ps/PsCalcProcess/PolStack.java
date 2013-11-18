/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.process.ps.PsCalcProcess;

import edu.mbl.jif.imaging.mmtiff.MDUtils;
import edu.mbl.jif.imaging.mmtiff.TaggedImage;
import edu.mbl.jif.imaging.util.ImagingUtils;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferShort;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

// Jan 2013... can you believe it?  I can't...
//
// Aug 2005 -- New Polstack for 8 and 12-bit
// 8-bit PolStack Object
//
public class PolStack
		implements Cloneable {

	// polStackID:: -1: unidentified, 0: from a file, xxxxx: timeAcquired
	public String polStackID = null;
	// fileSource:: null: not saved, else name of the file it was loaded from
	public String fileSource = null; // No extension... always .tif
	//
	public int width = 0;
	public int height = 0;
	public int depth = 0;
	public int size = 0;
	public int numSlices = 0;
	public int dataType = 0;
	// byte[] or short[] arrays containing slices
	public Object imgMagnitude;
	public Object imgAzimuth;
	public Object slice0;
	public Object slice1;
	public Object slice2;
	public Object slice3;
	public Object slice4;
	// for BkgdStack arrays for Background correction
	public double[] bg21;
	public double[] bg31;
	public double[] bg21_5frame;
	public double[] bg31_5frame;
	// kFor2Frame = mean(bg3 + bg4 - 2*bg1);  k = ~125.
	public float kFor2Frame = 0f;
	// Acquisition Parameters - saved to a .prm file, in XML format.
	public PolStackParms psParms;

	/////////////////////////////////////////////////////////////////////////
	// Create a Really Empty PolStack - for cloning
	//
	public PolStack() {
	}

	public PolStack(List<TaggedImage> images) {
		try {
			width = MDUtils.getWidth(images.get(0).tags);
			height = MDUtils.getHeight(images.get(0).tags);
			depth = MDUtils.getBitDepth(images.get(0).tags);
		} catch (JSONException jSONException) {
		}
		size = width * height;
		numSlices = images.size();
		if (numSlices > 0) {
			slice0 = images.get(0).pix;
		}
		if (numSlices > 1) {
			slice1 = images.get(1).pix;
		}
		if (numSlices > 2) {
			slice2 = images.get(2).pix;
		}
		if (numSlices > 3) {
			slice3 = images.get(3).pix;
		}
		if (numSlices > 4) {
			slice4 = images.get(4).pix;
		}
		// Add Mag & Ort arrays
		if (images.get(0).pix instanceof Byte[]) {
			imgMagnitude = new byte[size];
			imgAzimuth = new byte[size];
		} else if (images.get(0).pix instanceof Short[]) {
			imgMagnitude = new short[size];
			imgAzimuth = new short[size];
		}
	}

	// Create from an array of images arrays.
	public PolStack(List images, int _width, int _height, int _depth, String _stackID) {
		width = _width;
		height = _height;
		depth = _depth;
		size = width * height;
		numSlices = images.size();
		if (numSlices > 0) {
			slice0 = images.get(0);
		}
		if (numSlices > 1) {
			slice1 = images.get(1);
		}
		if (numSlices > 2) {
			slice2 = images.get(2);
		}
		if (numSlices > 3) {
			slice3 = images.get(3);
		}
		if (numSlices > 4) {
			slice4 = images.get(4);
		}
		// Add Mag & Ort arrays
		if (images.get(0) instanceof Byte[]) {
			imgMagnitude = new byte[size];
			imgAzimuth = new byte[size];
		} else if (images.get(0) instanceof Short[]) {
			imgMagnitude = new short[size];
			imgAzimuth = new short[size];
		}

	}

	/////////////////////////////////////////////////////////////////////////
	// Create a New, Empty PolStack for an Acquisition
	//
	public PolStack(int _numSlices, int _width, int _height, int _depth, String _stackID) {

		width = _width;
		height = _height;
		depth = _depth;
		size = width * height;
		numSlices = _numSlices;

		if (depth == 8) {
			dataType = DataBuffer.TYPE_BYTE;
			if (numSlices > 0) {
				slice0 = new byte[size];
			}
			if (numSlices > 1) {
				slice1 = new byte[size];
			}
			if (numSlices > 2) {
				slice2 = new byte[size];
			}
			if (numSlices > 3) {
				slice3 = new byte[size];
			}
			if (numSlices > 4) {
				slice4 = new byte[size];
			}
		} else if (depth == 12) {
			dataType = DataBuffer.TYPE_USHORT;
			if (numSlices > 0) {
				slice0 = new short[size];
			}
			if (numSlices > 1) {
				slice1 = new short[size];
			}
			if (numSlices > 2) {
				slice2 = new short[size];
			}
			if (numSlices > 3) {
				slice3 = new short[size];
			}
			if (numSlices > 4) {
				slice4 = new short[size];
			}

		}
		polStackID = _stackID;
		fileSource = null;
		// add PolStackParms and set to the current Acquisition Parameters
//      psParms = new PolStackParms(polStackID);
//      psParms.setToAcquisitionSettingsPS();
//      psParms.width = width;
//      psParms.height = height;
//      psParms.depth = depth;
	}

	/////////////////////////////////////////////////////////////////////////
	// Loads a PolStack from a TIFF file
	//
	public PolStack(String tiffPathFile) throws Exception {
		this(tiffPathFile, false);
	}

	public PolStack(String tiffPathFile, boolean raw) {
		// + + Check for startsWith("PS") || ("BG")
		// Up to 7 images, 5 samples and 2 calculated

		ArrayList imgs =
				ImagingUtils.loadImageArrayList(ImagingUtils.filenameWithTifExt(tiffPathFile));
		if (imgs == null) {
			return;
		}
		//Inspector.inspectWait("Loaded from Tiff", imgs.get(0));
		dataType = ImagingUtils.getImageDataType(imgs);

		width = ((BufferedImage) imgs.get(0)).getWidth();
		height = ((BufferedImage) imgs.get(0)).getHeight();
		size = width * height;
		int numImages = imgs.size();
		if (raw) {
			numSlices = numImages;
		} else {
			numSlices = numImages - 2;
		}
		int extSlice = 0;
		if (dataType == DataBuffer.TYPE_BYTE) {
			depth = 8;
		} else if (dataType == DataBuffer.TYPE_SHORT) {
			depth = 12;
		} else {
			System.err.println(
					"Attempted to load a PolStack from other than Byte or Short Image");
		}

		if (raw) {
			// Create image arrays for retardance and orientation images
			if (dataType == DataBuffer.TYPE_BYTE) {
				imgMagnitude = new byte[size];
				imgAzimuth = new byte[size];
			} else if (dataType == DataBuffer.TYPE_SHORT) {
				imgMagnitude = new short[size];
				imgAzimuth = new short[size];
			}
			extSlice = 0;
		} else { // not a Raw PolStack, load the retardance and orientation images
			if (dataType == DataBuffer.TYPE_BYTE) {
				if (numImages > 0) {
					imgMagnitude = ((DataBufferByte) ((BufferedImage) imgs.get(0)).getRaster().getDataBuffer()).getData();
				}
				if (numImages > 1) {
					imgAzimuth = ((DataBufferByte) ((BufferedImage) imgs.get(1)).getRaster().getDataBuffer()).getData();
				}
			} else if (dataType == DataBuffer.TYPE_SHORT) {
				if (numImages > 0) {
					imgMagnitude = ((DataBufferShort) ((BufferedImage) imgs.get(0)).getRaster().getDataBuffer()).getData();
				}
				if (numImages > 1) {
					imgAzimuth = ((DataBufferShort) ((BufferedImage) imgs.get(1)).getRaster().getDataBuffer()).getData();
				}
			}
			extSlice = 2;
		}

		// Load the sample data slices
		if (dataType == DataBuffer.TYPE_BYTE) {
			if (numImages > extSlice) {
				slice0 = ((DataBufferByte) ((BufferedImage) imgs.get(extSlice)).getRaster().getDataBuffer()).getData();
			}
			if (numImages > extSlice + 1) {
				slice1 = ((DataBufferByte) ((BufferedImage) imgs.get(extSlice + 1)).getRaster().getDataBuffer()).
						getData();
			}
			if (numImages > extSlice + 2) {
				slice2 = ((DataBufferByte) ((BufferedImage) imgs.get(extSlice + 2)).getRaster().getDataBuffer()).
						getData();
			}
			if (numImages > extSlice + 3) {
				slice3 = ((DataBufferByte) ((BufferedImage) imgs.get(extSlice + 3)).getRaster().getDataBuffer()).
						getData();
			}
			if (numImages > extSlice + 4) {
				slice4 = ((DataBufferByte) ((BufferedImage) imgs.get(extSlice + 4)).getRaster().getDataBuffer()).
						getData();
			}

		} else if (dataType == DataBuffer.TYPE_SHORT) {
			if (numImages > extSlice) {
				slice0 = ((DataBufferShort) ((BufferedImage) imgs.get(extSlice)).getRaster().getDataBuffer()).getData();
			}
			if (numImages > extSlice + 1) {
				slice1 = ((DataBufferShort) ((BufferedImage) imgs.get(extSlice + 1)).getRaster().getDataBuffer()).getData();
			}
			if (numImages > extSlice + 2) {
				slice2 = ((DataBufferShort) ((BufferedImage) imgs.get(extSlice + 2)).getRaster().getDataBuffer()).
						getData();
			}
			if (numImages > extSlice + 3) {
				slice3 = ((DataBufferShort) ((BufferedImage) imgs.get(extSlice + 3)).getRaster().getDataBuffer()).getData();
			}
			if (numImages > extSlice + 4) {
				slice4 = ((DataBufferShort) ((BufferedImage) imgs.get(extSlice + 4)).getRaster().getDataBuffer()).getData();
			}
		}
		//polStackID = FileUtil.getJustFilenameNoExt(tiffPathFile);

		fileSource = tiffPathFile;

		// Set  to default settings
		/**
		 * @todo Get PolStackParms from .prm file...
		 */
//      psParms = new PolStackParms(polStackID);
//
//      // psParms.setToAcquisitionSettingsPS();
//      
//     // @todo *** setPSParms(polStackID);
//      psParms.width = width;
//      psParms.height = height;
//      psParms.depth = depth;
		//edu.mbl.jif.utils.PSjUtils.event("PolStack " + polStackID +
		//                                 " loaded from: " + tiffPathFile);
	}

	//------------------------------------------------------------------
	public BufferedImage sliceAsImage(Object slice) {
		BufferedImage img = null;
		if (depth == 8) {
			//img = ImagingUtils.createImage(width, height, (byte[]) slice);
			img = ImagingUtils.byteArrayToBufferedImage((byte[]) slice, width, height);
		} else {
			//img = ImagingUtils.createImage(width, height, (short[]) slice);
			img = ImagingUtils.shortArrayToBufferedImage((short[]) slice, width, height);
		}
		return img;
	}

	//--------------------------------------------------------------
// @todo ***
   /*
	 void setPSParms (String polStackID) {
	 * 
	 void setPSParms (String polStackID) {
	 // sets to parameters from .prm file, if there is one.
	 String prmFile = DataAccess.pathLog() + FileUtil.removeExtension(polStackID) + ".prm";
	 if (FileUtil.doesExist(prmFile) &&
	 Prefs.usr.getBoolean("data.savePolStackParms", true)) {
	 psParms = PSParmsDataMgr.restoreFromFile(prmFile);
	 if (psParms == null) {
	 psj.PSjUtils.event("Failure opening .prm file: " + prmFile);
	 psParms = new PolStackParms(polStackID);
	 // Set PolStackParms to default settings
	 psParms.setToAcquisitionSettingsPS();
	 }
	 } else {
	 psParms = new PolStackParms(polStackID);
	 // Set PolStackParms to default settings
	 psParms.setToAcquisitionSettingsPS();
	 }
	 }


	 public String getFileSource () {
	 return fileSource;
	 }


	 //--------------------------------------------------------------
	 public void saveToFile () {
	 if (fileSource != null) {
	 saveToFile(fileSource);
	 }
	 }

	 public void saveToFile (String _path) {
	 saveToFile (_path, false);
	 }
	 public void saveToFile (String _path, boolean overWrite) {

	 // @todo synchronize(this){} 

	 // remove .tiff extension
	 final String path = FileUtil.getJustPath(_path) + "\\" +
	 FileUtil.getJustFilenameNoExt(_path);
	 FileUtil.removeExtension(_path);

	 //      new Thread(new Runnable()
	 //      {
	 //         public void run () {
	 //setSaving(true);
	 psj.PSjUtils.statusProgress("Saving PolStack: " + path + "...", 75);
	 int w = width;
	 int h = height;
	 try {
	 ArrayList imgs = new ArrayList();
	 // save as PolStack
	 if ((imgMagnitude != null) &&
	 (imgAzimuth != null)) {
	 BufferedImage retImage =
	 ImagingUtils.createImage(w, h, dataType, imgMagnitude);
	 if (Prefs.usr.getBoolean("acPS.addStripe", true)) {
	 PolStackUtil.addDataStripeToPSimage(retImage, psParms);
	 }
	 imgs.add(retImage);
	 imgs.add(ImagingUtils.createImage(w, h, dataType, imgAzimuth));

	 }
	 // save as Raw PolStack
	 if (numSlices > 0) {
	 imgs.add(ImagingUtils.createImage(w, h, dataType, slice0));
	 }
	 if (numSlices > 1) {
	 imgs.add(ImagingUtils.createImage(w, h, dataType, slice1));
	 }
	 if (numSlices > 2) {
	 imgs.add(ImagingUtils.createImage(w, h, dataType, slice2));
	 }
	 if (numSlices > 3) {
	 imgs.add(ImagingUtils.createImage(w, h, dataType, slice3));
	 }
	 if (numSlices > 4) {
	 imgs.add(ImagingUtils.createImage(w, h, dataType, slice4));
	 }
	 if(overWrite) {
	 MultipageTiffFile.saveImageArrayList(imgs, path, true);
	 } else {
	 MultipageTiffFile.saveImageArrayList(imgs, path);
	 }
	 }
	 catch (Exception ex) {
	 ex.printStackTrace();
	 //               DialogBoxI.boxError("Error saving PolStack",
	 //                                   "Exception in savePolStackToDisk: " +
	 //                                   ex.getMessage());
	 //               edu.mbl.jif.utils.PSjUtils.event(
	 //                     "Exception in savePolStackToDisk: " + ex.getMessage());
	 }

	 // save parameters to logNotes file
	 if (Prefs.usr.getBoolean("acq_recordLogNotes", true)) {
	 PolStackUtil.createPSDataFile(psParms, polStackID);
	 }

	 // Save parameters to .prm file
	 if (Prefs.usr.getBoolean("data.savePolStackParms", true)) {
	 String prmFile = DataAccess.pathLog() +
	 FileUtil.removeExtension(polStackID) + ".prm";
	 if (Globals.isDeBug()) {
	 System.out.println("polStackID: " + polStackID);
	 System.out.println("prmFile: " + prmFile);
	 }
	 PSParmsDataMgr.saveToFile(psParms, prmFile);
	 }

	 // add it to the ImageManager
	 PolStackUtil.updateImageManager(path);
	 psj.PSjUtils.event("PolStack Saved: " + path);
	 psj.PSjUtils.statusClear();
	 //setSaving(false);
	 //         }
	 //      }).start();
	 }


	 //-----------------------------------------------------------
	 // clone
	 public Object clone () {
	 PolStack o = null;
	 try {
	 o = (PolStack)super.clone();
	 }
	 catch (CloneNotSupportedException e) {
	 throw new InternalError(e.toString());
	 }
	 o.psParms = (PolStackParms) o.psParms.clone();
	 return o;
	 }
	 */
	public class PolStackParms
			implements Serializable, Cloneable {
		// ID

		public String polStackID = "0000";
		// Project / Session (original)
//	public String project;
//	public String session;
		// Acquisition
		// Camera settings
//	public long width;
//	public long height;
//	public long binning;
//	public long depth;
//	public long exposure;
//	public long gain;
//	public long offset;
//	public Rectangle roi;
//	// Multi-frame integration or averaging
//	public int frames = 1;
//	public int averaging = 1;
//	// Series acq.
//	public int NthInSeries = 0;
//	public int inSeriesOf = 0;
//	// Other
//	public long timeAcquired = 0;
//	public long timeToAcquire = 0;
//	// Averaging (old, 4:1 type, no longer used)
//	public boolean averaged = false;
//	// VariLC settings
//	public float[] retarderA = {
//		0,
//		0,
//		0,
//		0,
//		0
//	};
//	public float[] retarderB = {
//		0,
//		0,
//		0,
//		0,
//		0
//	};
		// PolStack parameters
		public float wavelength = 546f; // wavelength in nm ~ 546 nm
		public float swingFraction = 0.03f; // =swing/wavelength ~ 0.03
		public int algorithm = 4;
		public int retCeiling = 10; // maximum image retardance in nm
		public float retardanceMax = 0; // Maximum,regardless of retCeiling.
		public int azimuthRef = 0; // azimuth reference in (whole) degrees
		public float dynamicRange = 1.0f; // dynamic range of ...
		public String backGroundStack = null;  // filename
		public boolean doBkgdCorrection = false;
		// Ratioing Correction
		public float zeroIntensity = 0.0f;  // from camera measurement
		public Rectangle roiRatioing = new Rectangle(0, 0, 0, 0);
		public double[] ratioingAvg = null; // for each slice
		public boolean doRatioing = false;
	}
}