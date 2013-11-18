package edu.mbl.jif;

import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import ij.ImagePlus;
import ij.ImageStack;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

/**
 * Example Acquisition Routine
 * @author GBH
 */
public class PSAcqRoutine extends AbstractAcqRoutine {

  ImagePlus lastAcquiredImgPlus = null;
  private boolean isBackground;

  public PSAcqRoutine() {
    setup();
  }

  public void setIsBackground(boolean t) {
    isBackground = true;
  }

  public void doAcquirePolStack(ImageStack _stack, boolean background) {
    int nSlices = 5;
    System.out.println("Acquiring PolStack...");
    int multiFrame;
    if (background) {
      multiFrame = acqModel.getMultiFrameBkgd();
    } else {
      multiFrame = acqModel.getMultiFrame();
    }
    acqCtrl.start(true,
            cameraModel.getExposureAcq(),
            cameraModel.getGainAcq(),
            acqModel.getDepth(),
            multiFrame,
            acqModel.isMirrorImage());
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
    }
    for (int i = 0; i < nSlices; i++) {
      //camAcq.vlcSelect(i); // change LC...
      vlcCtrl.selectElementWait(i);
      //acqCtrl.acquireImage()
      int slice = i + 2;
      long acqTime = 0;
      if (_stack.getPixels(1) instanceof byte[]) {
        byte[] pixelsByte = (byte[]) _stack.getPixels(slice + 1);
        pixelsByte[0] = 0;
        acqTime = acqCtrl.acquireImage(pixelsByte);
      } else if (_stack.getPixels(1) instanceof short[]) {
        short[] pixelsShort = (short[]) _stack.getPixels(slice + 1);
        pixelsShort[0] = 0;
        acqTime = acqCtrl.acquireImage(pixelsShort);
      }
    }
    acqCtrl.finish();
  }


  public void doAcquirePolStackInSeries(String path, int iteration, boolean background) {
    int nSlices = 5;
    System.out.println("Acquiring PolStack...");

    // create sample data file
    MultipageTiffFile  tif = new MultipageTiffFile(path + "/" + String.valueOf(iteration));
    int multiFrame;
    if (background) {
      multiFrame = acqModel.getMultiFrameBkgd();
    } else {
      multiFrame = acqModel.getMultiFrame();
    }
    Object imageArray;
    BufferedImage img;
    if (acqModel.getDepth() == 8) {
      imageArray = new byte[cameraModel.getWidth() * cameraModel.getHeight()];
    } else {
      imageArray = new short[cameraModel.getWidth() * cameraModel.getHeight()];
    }
    //

    acqCtrl.start(true,
            cameraModel.getExposureAcq(),
            cameraModel.getGainAcq(),
            acqModel.getDepth(),
            multiFrame,
            acqModel.isMirrorImage());
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
    }
    for (int i = 0; i < nSlices; i++) {
      vlcCtrl.selectElementWait(i);
      long acqTime = acqCtrl.acquireImage(imageArray);
      if (imageArray instanceof byte[]) {
        img = createBufferedImage(cameraModel.getWidth(), cameraModel.getHeight(), (byte[]) imageArray);
      } else {
        img = createBufferedImage(cameraModel.getWidth(), cameraModel.getHeight(), (short[]) imageArray);
      }
      tif.appendImage(img);
    }
    acqCtrl.finish();
    vlcCtrl.selectElement(1);
  }

  public void setExposure(int exp) {
    cameraModel.setExposureAcq((double) exp);
  }

  public void shutterXmis(boolean on) {
    // ?? illum.setLevelXmis(0);
    illumModel.setOpenXmis(on);
  }

  public void shutterEpi(boolean open) {
    illumModel.setOpenEpi(open);
  }

  public void acq1Frame() {
    camAcq.captureToImagePlus();
  }

  public void acqPolStack5Frame() {
    // assumes that VariLC settings 0-4 are defined
    }

  public BufferedImage createBufferedImage(int width, int height, byte[] pixels) {
    SampleModel sampleModel;
    IndexColorModel icm = getDefaultColorModel();
    WritableRaster wr = icm.createCompatibleWritableRaster(1, 1);
    sampleModel = wr.getSampleModel();
    sampleModel = sampleModel.createCompatibleSampleModel(width, height);
    DataBuffer db = new DataBufferByte(pixels, width * height, 0);
    WritableRaster raster = Raster.createWritableRaster(sampleModel, db, null);
    BufferedImage image = new BufferedImage(icm, raster, false, null);
    return image;
  }

  public BufferedImage createBufferedImage(int width, int height, short[] pixels) {
    SampleModel sampleModel;
    IndexColorModel icm = getDefaultColorModel();
    WritableRaster wr = icm.createCompatibleWritableRaster(1, 1);
    sampleModel = wr.getSampleModel();
    sampleModel = sampleModel.createCompatibleSampleModel(width, height);
    DataBuffer db = new DataBufferUShort(pixels, width * height, 0);
    WritableRaster raster = Raster.createWritableRaster(sampleModel, db, null);
    BufferedImage image = new BufferedImage(icm, raster, false, null);
    return image;
  }

  /** Returns the default grayscale IndexColorModel. */
  public IndexColorModel getDefaultColorModel() {
    byte[] r = new byte[256];
    byte[] g = new byte[256];
    byte[] b = new byte[256];
    for (int i = 0; i < 256; i++) {
      r[i] = (byte) i;
      g[i] = (byte) i;
      b[i] = (byte) i;
    }
    IndexColorModel defaultColorModel = new IndexColorModel(8, 256, r, g, b);
    return defaultColorModel;
  }
}
