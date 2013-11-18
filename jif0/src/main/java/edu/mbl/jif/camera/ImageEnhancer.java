
package edu.mbl.jif.camera;

import  java.awt.image.*;

/*
 * ((Unused))
 */
public class ImageEnhancer {
  static LookupTable LUT;

  public ImageEnhancer () {
  }
/**
   * @todo pass the LUT and return a LUT
    public LookupTable brightenLUT (LookupTable lut) {

 */

// Create a look-up table for brightening pixels.
  public void brightenLUT () {
    short brighten[] = new short[256];
    for (int i = 0; i < 256; i++) {
      short pixelValue = (short)(i + 10);
      if (pixelValue > 255)
        pixelValue = 255;
      else if (pixelValue < 0)
        pixelValue = 0;
      brighten[i] = pixelValue;
    }
    LUT = new ShortLookupTable(0, brighten);
  }


  // Create a look-up table to increase the contrast.
  public void contrastIncLUT () {
    short brighten[] = new short[256];
    for (int i = 0; i < 256; i++) {
      short pixelValue = (short)(i*1.2);
      if (pixelValue > 255)
        pixelValue = 255;
      else if (pixelValue < 0)
        pixelValue = 0;
      brighten[i] = pixelValue;
    }
    LUT = new ShortLookupTable(0, brighten);
  }


  // This method creates a look-up filter and applies it to
  // the buffered image.
  public void applyFilter (BufferedImage bi) {
	LookupOp lop = new LookupOp(LUT, null);
	lop.filter(bi, bi);
  }
}



