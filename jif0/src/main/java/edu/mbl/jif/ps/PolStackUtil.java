package edu.mbl.jif.ps;

import edu.mbl.jif.ps.orient.OrientationGlyphs;
import edu.mbl.jif.gui.imaging.FrameImageDisplay;
import edu.mbl.jif.gui.imaging.GraphicOverlay;
import edu.mbl.jif.gui.imaging.zoom.core.ZoomGraphics;
import java.awt.*;
import java.awt.image.*;
import edu.mbl.jif.imaging.util.ImageUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.graphics.GraphicsUtilities;

public class PolStackUtil {

   static boolean saving = false;
   //
   static public int BYTE_TYPE = 8;
   static public int SHORT12_TYPE = 12;
   static public int SHORT16_TYPE = 16;
   //
   // static Object overlayPixels = null;
   private static BufferedImage outImageColor;
   //private static BufferedImage outImageGray;
   //
   // Color Mapping Parameters
   static boolean addColorScale = true;
   static int orientColorSaturation = 50;  // / by 100
   // Orient Line Parameters
   static boolean lineShowPoint = false;
   static int orientSeparation = 3;
   static int orientVectorThreshold = 0;
   static boolean vectorProportional = false;
   static int vectorLength = 3;
   static Color shadowOrientColor = Color.white;
   static Color lineOrientColor = Color.BLACK;

   //
   private static AlphaComposite makeComposite(float alpha) {
      int type = AlphaComposite.SRC_OVER;
      return (AlphaComposite.getInstance(type, alpha));
   }

   //
   public static void setColorMappingParameters(boolean _addColorScale,
           int _orientColorSaturation) {
      addColorScale = _addColorScale;
      orientColorSaturation = _orientColorSaturation;
   }

   public static void setOrientLineParameters(
           boolean _lineShowPoint,
           int _orientSeparation,
           int _vectorLength,
           boolean _vectorProportional,
           int _orientVectorThreshold,
           Color lineColor, Color shadowColor) {
      lineShowPoint = _lineShowPoint;
      orientSeparation = _orientSeparation;
      orientVectorThreshold = _orientVectorThreshold;
      vectorProportional = _vectorProportional;
      vectorLength = _vectorLength;
      lineOrientColor = lineColor;
      shadowOrientColor = shadowColor;
   }

   static public void initialize(PolStack _pStack) {
      outImageColor = makeRGBOutImage(_pStack);
      //	outImageGray = makeGrayOutImage(_pStack);
   }

   static public BufferedImage makeRGBOutImage(PolStack _pStack) {
      return GraphicsUtilities.createCompatibleImage(_pStack.width, _pStack.height);
//		return GraphicsUtilities.createCompatibleImage( 
//				new BufferedImage(_pStack.width, _pStack.height, BufferedImage.TYPE_INT_RGB));
   }

   //===============================================================================
   // Color Mapping 
   //===============================================================================
   //----------------------------------------------------------------
   // makeOrientColorImage
   static void makeOrientColorImage(PolStack _pStack, BufferedImage outImage) {
      // an experiment... ((BufferedImage) outImage).getRaster().getDataBuffer()
      //overlayPixels = new int[_pStack.size];
      float saturation = (float) (orientColorSaturation) / 100f;
      WritableRaster wrOut = ((BufferedImage) outImage).getRaster();
      int[] dataArray = ((DataBufferInt) wrOut.getDataBuffer()).getData();
      if (_pStack.depth == 8) {
         for (int i = 0; i < dataArray.length; i++) {
            int v = (int) (((byte[]) _pStack.imgMagnitude)[i] & 0xFF);
            if (v > 255) {
               v = 255;
            }
            if (v < 0) {
               v = 0;
            }
            dataArray[i] = Color.HSBtoRGB((float) ((int) (((byte[]) _pStack.imgAzimuth)[i] & 0xFF) / 180f),
                    saturation, (float) (v / 255.0f));
         }
      } else {
         for (int i = 0; i < dataArray.length; i++) {
            int v = (int) (((short[]) _pStack.imgMagnitude)[i] & 0xffff);
            if (v > 4095) {
               v = 4095;
            }
            if (v < 0) {
               v = 0;
            }
            dataArray[i] = Color.HSBtoRGB((float) ((int) (((short[]) _pStack.imgAzimuth)[i] & 0xffff) / 180f),
                    // hue=f(orient)
                    saturation, (float) (v / 4095.0f));
         }
      }
      //wrOut.setDataElements(0, 0, _pStack.width, _pStack.height, dataArray);
      if (addColorScale) {
         drawColorScale(((BufferedImage) outImage).getGraphics(), _pStack.width - 45,
                 _pStack.height - 45, 40);
      }
   }

   // Draw a color scale on the image...
   static void drawColorScale(Graphics g, int x, int y, int diameter) {
      // this is redundant with the version in PanalPSView
      float value = 1.0f;
      float saturation = (float) (orientColorSaturation) / 100f;
      int n = 180;
      int arc = 180 / n;
      g.setColor(Color.darkGray);
      g.drawLine(x - 5, (y + (diameter / 2)) - 1, x + diameter + 5,
              (y + (diameter / 2)) - 1);
      g.drawLine(x + (diameter / 2) + 1, y - 5, x + (diameter / 2) + 1,
              y + diameter + 5);
      g.setColor(Color.lightGray);
      g.drawLine(x - 5, y + (diameter / 2), x + diameter + 5,
              y + (diameter / 2));
      g.drawLine(x + (diameter / 2), y - 5, x + (diameter / 2),
              y + diameter + 5);
      for (int i = 0; i < n; i++) {
         int d = i * arc;
         g.setColor(new Color(Color.HSBtoRGB((float) d / 180f,
                 saturation, value)));
         g.fillArc(x, y, diameter, diameter, d, arc);
         g.fillArc(x, y, diameter, diameter, d + 180, arc);
      }
   }

   //===============================================================================
   // Orientation Lines Image Generation...
   //===============================================================================
   //
   // TODO Add ROI...
   static void makeOrientLineImage(PolStack _pStack, BufferedImage outImage) {
      if (_pStack.depth == 8) {
         makeOrientLineImage((byte[]) _pStack.imgMagnitude, (byte[]) _pStack.imgAzimuth,
                 outImage, BYTE_TYPE);
      } else {
         makeOrientLineImage((short[]) _pStack.imgMagnitude, (short[]) _pStack.imgAzimuth,
                 outImage, SHORT16_TYPE);
      }
   }

   static public void makeOrientGlyphOverlay(
           Object mag,
           Object orientAngle,
           float[] variance,
           int scaling,
           int w, int h,
           ZoomGraphics g2,
           int type,
           OrientationGlyphs glyphs,
           float ellipticity) {
      //
//		int w = ((BufferedImage) outImage).getWidth();
//		int h = ((BufferedImage) outImage).getHeight();
      int nX = (int) ((float) (w) / (float) orientSeparation);
      int nY = (int) ((float) (h) / (float) orientSeparation);
      float dX = (float) (w) / nX;
      float dY = (float) (h) / nY;
      int cutoffLow = orientVectorThreshold;
      // int cutoffHi;  
      int length = vectorLength;
      float xLen = (float) length / 256.0f;
      int x1;
      int x2;
      int y1;
      int y2;
      int x = 0;
      int y = 0;
      int offset = 0;
      int a = 0;
      //Graphics2D g2 = (Graphics2D) ((BufferedImage) outImage).getGraphics();
      g2.getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
      g2.getGraphics().setRenderingHint(RenderingHints.KEY_RENDERING,
              RenderingHints.VALUE_RENDER_QUALITY);
      int ret = 0;
      double angle = 0;
      for (int i = 0; i < (nY - 1); i++) {
         for (int j = 0; j < (nX - 1); j++) {
            x = (int) ((float) (j + 1) * dX);
            //if (x > w)
            y = (int) ((float) (i + 1) * dY);
            offset = (y * w) + x;
            if (type == BYTE_TYPE) {
               ret = (int) (((byte[]) mag)[offset] & 0xFF);
               if (ret > cutoffLow) {
                  a = (int) (((byte[]) orientAngle)[offset] & 0xFF);
                  angle = ((double) a / 180) * Math.PI;
                  if (vectorProportional) {
                     length = (int) (xLen * (float) ret);
                  }
                  if (variance != null) {
                     ellipticity = variance[offset];
                  }
                  //glyphs.drawEllipseGlyph(g2, x, y, (float) angle, length, ellipticity);
                  glyphs.drawFanGlyph(g2, x, y, (float) angle, length, ellipticity);
                  //glyphs.drawLineGlyph(g2, dY, dY, xLen, xLen, dY);
               }
            } else if (type == SHORT16_TYPE) {
               ret = (int) (((short[]) mag)[offset] & 0xFF);
               if (ret > cutoffLow) {
                  a = (int) (((short[]) orientAngle)[offset] & 0xFF);
                  angle = ((double) a / 180) * Math.PI;
                  if (vectorProportional) {
                     length = (int) (xLen * (float) ret);
                  }
                  if (variance != null) {
                     ellipticity = variance[offset];
                  }
                  //glyphs.drawEllipseGlyph(g2, x, y, (float) angle, length, ellipticity);
                  glyphs.drawFanGlyph(g2, x, y, (float) angle, length, ellipticity);
                  //glyphs.drawLineGlyph(g2, dY, dY, xLen, xLen, dY);
               }
            }
         }
      }
   }

   static public void makeOrientLineOverlay(Object mag, Object orient,
           int scaling,
           int w, int h, ZoomGraphics g2, int type, int lineWidth) {
//		int w = ((BufferedImage) outImage).getWidth();
//		int h = ((BufferedImage) outImage).getHeight();
      int nX = (int) ((float) (w) / (float) orientSeparation);
      int nY = (int) ((float) (h) / (float) orientSeparation);
      float dX = (float) (w) / nX;
      float dY = (float) (h) / nY;
      int cutoffLow = orientVectorThreshold;
      // int cutoffHi;  
      int length = vectorLength;
      float xLen = (float) length / 256.0f;
      int x1;
      int x2;
      int y1;
      int y2;
      int x = 0;
      int y = 0;
      int offset = 0;
      int a = 0;
      //Graphics2D g2 = (Graphics2D) ((BufferedImage) outImage).getGraphics();
      g2.getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
      g2.getGraphics().setRenderingHint(RenderingHints.KEY_RENDERING,
              RenderingHints.VALUE_RENDER_QUALITY);
      int ret = 0;
      double angle = 0;
      for (int i = 0; i < (nY - 1); i++) {
         for (int j = 0; j < (nX - 1); j++) {
            x = (int) ((float) (j + 1) * dX);
            //if (x > w)
            y = (int) ((float) (i + 1) * dY);
            offset = (y * w) + x;
            if (type == BYTE_TYPE) {
               ret = (int) (((byte[]) mag)[offset] & 0xFF);
               if (ret > cutoffLow) {
                  a = (int) (((byte[]) orient)[offset] & 0xFF);
                  //a = getAngle((byte[]) orient, offset, x,y,dX,dY, orientSeparation);
                  angle = ((double) a / 180) * Math.PI;
                  if (vectorProportional) {
                     length = (int) (xLen * (float) ret);
                  }
                  //System.out.println(x + ", " + y + ": " + offset + " -- " + a
                  //    + " : " + orient);

                  drawOrientLine(g2, angle, x, y, length, lineWidth);
               }
            } else if (type == SHORT16_TYPE) {
               ret = (int) (((short[]) mag)[offset] & 0xFF);
               if (ret > cutoffLow) {
                  a = (int) (((short[]) orient)[offset] & 0xFF);
                  angle = ((double) a / 180) * Math.PI;
                  if (vectorProportional) {
                     length = (int) (xLen * (float) ret);
                  }
                  drawOrientLine(g2, angle, x, y, length, lineWidth);
               }
            }
         }
      }
   }

   static int getAngle(byte[] orient, int offset, int x, int y, float dX, float dY, int scale) {
      int angle = (int) (orient[offset] & 0xFF);
      return 0;
   }

   static public void makeOrientLineImage(Object mag, float[] orient, int scaling,
           Object outImage, int type) {
      int w = ((BufferedImage) outImage).getWidth();
      int h = ((BufferedImage) outImage).getHeight();
      int nX = (int) ((float) (w) / (float) orientSeparation);
      int nY = (int) ((float) (h) / (float) orientSeparation);
      float dX = (float) (w) / nX;
      float dY = (float) (h) / nY;
      int cutoffLow = orientVectorThreshold;
      // int cutoffHi;  
      int length = vectorLength;
      float xLen = (float) length / 256.0f;
      int x1;
      int x2;
      int y1;
      int y2;
      int x = 0;
      int y = 0;
      int offset = 0;
      int a = 0;
      Graphics2D g2 = (Graphics2D) ((BufferedImage) outImage).getGraphics();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING,
              RenderingHints.VALUE_RENDER_QUALITY);
      int ret = 0;
      double angle = 0;
      for (int i = 0; i < (nY - 1); i++) {
         for (int j = 0; j < (nX - 1); j++) {
            x = (int) ((float) (j + 1) * dX);
            //if (x > w)
            y = (int) ((float) (i + 1) * dY);
            offset = (y * w) + x;
            if (type == BYTE_TYPE) {
               ret = (int) (((byte[]) mag)[offset] & 0xFF);
            } else if (type == SHORT16_TYPE) {
               ret = (int) (((short[]) mag)[offset] & 0xFF);
            }
            if (ret > cutoffLow) {
               if (vectorProportional) {
                  length = (int) (xLen * (float) ret);
               }
            }
            drawOrientLine(g2, orient[offset], x, y, length, 1.0f);
         }
      }
   }

   // Colors -----------------
   static private void makeOrientLineImage(Object mag, Object orient, Object outImage, int type) {
      int w = ((BufferedImage) outImage).getWidth();
      int h = ((BufferedImage) outImage).getHeight();
      int nX = (int) ((float) (w) / (float) orientSeparation);
      int nY = (int) ((float) (h) / (float) orientSeparation);
      float dX = (float) (w) / nX;
      float dY = (float) (h) / nY;
      int cutoffLow = orientVectorThreshold;
      // int cutoffHi;  

      int length = vectorLength;
      float xLen = (float) length / 256.0f;
      int x1;
      int x2;
      int y1;
      int y2;
      int x = 0;
      int y = 0;
      int offset = 0;
      int a = 0;
      Graphics2D g2 = (Graphics2D) ((BufferedImage) outImage).getGraphics();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING,
              RenderingHints.VALUE_RENDER_QUALITY);
      int ret = 0;
      double angle = 0;
      for (int i = 0; i < (nY - 1); i++) {
         for (int j = 0; j < (nX - 1); j++) {
            x = (int) ((float) (j + 1) * dX);
            //if (x > w)
            y = (int) ((float) (i + 1) * dY);
            offset = (y * w) + x;
            if (type == BYTE_TYPE) {
               ret = (int) (((byte[]) mag)[offset] & 0xFF);
               if (ret > cutoffLow) {
                  a = (int) (((byte[]) orient)[offset] & 0xFF);
                  angle = ((double) a / 180) * Math.PI;
                  if (vectorProportional) {
                     length = (int) (xLen * (float) ret);
                  }
                  //System.out.println(x + ", " + y + ": " + offset + " -- " + a
                  //    + " : " + orient);
                  drawOrientLine(g2, angle, x, y, length, 1.0f);
               }
            } else if (type == SHORT16_TYPE) {
               ret = (int) (((short[]) mag)[offset] & 0xFF);
               if (ret > cutoffLow) {
                  a = (int) (((short[]) orient)[offset] & 0xFF);
                  angle = ((double) a / 180) * Math.PI;
                  if (vectorProportional) {
                     length = (int) (xLen * (float) ret);
                  }
                  drawOrientLine(g2, angle, x, y, length, 1.0f);
               }
            }
         }
      }

   }

   static void drawOrientLine(Graphics2D g, double angle, int x, int y,
           float length, float width) {
      int x1;
      int x2;
      int y1;
      int y2;
      int dx = (int) (length * Math.cos(angle));
      int dy = (int) (length * Math.sin(angle));
      x1 = x - dx;
      x2 = x + dx;
      y1 = y + dy;
      y2 = y - dy;
      g.setStroke(new BasicStroke(width));
      if (angle < (Math.PI / 2)) {
         g.setColor(shadowOrientColor);
         g.drawLine(x1 + 1, y1 + 1, x2 + 1, y2 + 1);
         g.setColor(lineOrientColor);
         g.drawLine(x1, y1, x2, y2);
      } else {
         g.setColor(shadowOrientColor);
         g.drawLine(x1 - 1, y1 + 1, x2 - 1, y2 + 1);
         g.setColor(lineOrientColor);
         g.drawLine(x1, y1, x2, y2);
      }
      if (lineShowPoint) {
         g.setColor(shadowOrientColor);
         g.drawArc(x - 1, y - 1, 1, 1, 0, 360);
         g.setColor(lineOrientColor);
         g.drawArc(x, y, 1, 1, 0, 360);
      }
   }

   static void drawOrientLine(ZoomGraphics g, double angle, int x, int y,
           float length, float width) {
      int x1;
      int x2;
      int y1;
      int y2;
      int dx = (int) (length * Math.cos(angle));
      int dy = (int) (length * Math.sin(angle));
      x1 = x - dx;
      x2 = x + dx;
      y1 = y + dy;
      y2 = y - dy;
      g.setStroke(new BasicStroke(width));
      if (angle < (Math.PI / 2)) {
         g.setColor(shadowOrientColor);
         g.drawLine(x1 + 1, y1 + 1, x2 + 1, y2 + 1);
         g.setColor(lineOrientColor);
         g.drawLine(x1, y1, x2, y2);
      } else {
         g.setColor(shadowOrientColor);
         g.drawLine(x1 - 1, y1 + 1, x2 - 1, y2 + 1);
         g.setColor(lineOrientColor);
         g.drawLine(x1, y1, x2, y2);
      }
      if (lineShowPoint) {
         g.setColor(shadowOrientColor);
         g.drawArc(x - 1, y - 1, 1, 1, 0, 360);
         g.setColor(lineOrientColor);
         g.drawArc(x, y, 1, 1, 0, 360);
      }
   }

   //===============================================================================
   // Averaging of Orientation...
   //===============================================================================
   //
   // Scaled/Interpolated ----------------------------------------------------------
   static public void makeOrientLineInterpolated(short[] mag, short[] orient,
           int scaling, Object outImage) {
      float[] sineOrient = createAveragedOrientArray(orient);
      // GraphicsUtil.createThumbnailFast()
      //makeOrientLineImage(mag, sineOrient, scaling, outImage, SHORT16_TYPE);
   }

   static public void makeOrientLineInterpolated(byte[] mag, byte[] orient,
           int scaling, Object outImage) {
      //float[] sineOrient = createAveragedOrientArray(orient);
      // GraphicsUtil.createThumbnailFast()
      //makeOrientLineImage(mag, sineOrient, scaling, outImage, BYTE_TYPE);
   }

   private static float[] createAveragedOrientArray(byte[] orient, int w, int h, int scale) {
      // NOT...
      int[][] window = new int[scale][scale];
      for (int i = 0; i < w; i++) {
         for (int j = 0; j < h; j++) {
            int[] is = window[i];
         }
      }
      return null;
   }

   private static float[] createAveragedOrientArray(short[] orient) {
      //double v = Math.sin((double) ((byte) ip.get(x, y) & 0xff) / 180 * 2 * Math.PI);
      return new float[20];
   }

   //===============================================================================
   // addDataStripeToImage(): adds data stripe to
   //
   public static BufferedImage addDataStripeToImage(
           BufferedImage rImage,
           PolStackParms psParms) {
      //      String data =
      //         psParms.polStackID  //"   Exposure: " + (psParms.exposure / 1000) + " ms." +
      //         + "  Retardance Ceiling: " + psParms.retCeiling + " nm.  ";
      //      if (psParms.backGroundStack != null) {
      //         data = data + "Bkgd: " + psParms.backGroundStack;
      //      }
      //      if (Ratioing.isSet()) {
      //         data =
      //            data + "RatioROI: " + jif.utils.PSjUtils.rectangleToPropString(Ratioing.roi);
      //      }
      //      rImage = ImageUtils.dataStripe(rImage, data);
      //      return rImage;
      //   }
      //
      //psParms.polStackID test if Background
      String data = psParms.polStackID + ": "
              + (psParms.exposure / 1000) + " ms, " + psParms.retCeiling
              + " nm.  ceiling, ";
      if (psParms.averaging == 4) {
         data = data + "(4:1Avg)  ";
      }
      if ((psParms.backGroundStack != null)) {
         data = data + "Bkgd: " + psParms.backGroundStack;
      }
      if (psParms.roiRatioing.getWidth() > 0) {
         data = data + ", RatioROI: "
                 + rectangleToString(psParms.roiRatioing);
      }
      if (psParms.frames > 1) {
         data = data + ", " + psParms.frames + "-frames"
                 + (psParms.averaged ? " (avgd)" : "");
      }
      if (psParms.inSeriesOf > 1) {
         data = data + ", Series: " + psParms.NthInSeries + 1 + " / " + psParms.inSeriesOf;
      }
      rImage = ImageUtils.dataStripe(rImage, data);
      return rImage;
   }

   public static String rectangleToString(Rectangle r) {
      String s = String.valueOf(r.x) + "," + String.valueOf(r.y) + ","
              + String.valueOf(r.width) + "," + String.valueOf(r.height);
      return s;
   }

   // =======================================================================
   // Testing 
   //===============================================================================
   //
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            runTest();
         }
      });
   }
   static String testFile =
           "C:\\_Dev\\_Dev_Data\\TestImages\\testData\\PS_Aster\\PS_03_0825_1753_24.tiff";

   private static void display(BufferedImage img, String title) {
      FrameImageDisplay fr = new FrameImageDisplay(img, title);
   }

   public static void runTest2() {
      createAveragedOrientArray(new byte[32 * 32], 32, 32, 3);
   }

   public static void runTest() {
      try {
         final PolStack ps = new PolStack(testFile);
         final OrientationGlyphs oGlyphs = new OrientationGlyphs();
         display(ps.sliceAsImage(ps.imgMagnitude), "Mag");
         display(ps.sliceAsImage(ps.imgAzimuth), "Ort");
         PolStackUtil.setColorMappingParameters(true, 50);
         PolStackUtil.setOrientLineParameters(lineShowPoint, 1,
                 orientVectorThreshold, vectorProportional, 7,
                 lineOrientColor, shadowOrientColor);
         BufferedImage biC = PolStackUtil.makeRGBOutImage(ps);
         BufferedImage biL = PolStackUtil.makeRGBOutImage(ps);
         //
         // Color map...
         PolStackUtil.setColorMappingParameters(true, 50);
         PolStackUtil.makeOrientColorImage(ps, biC);
         display(biC, "ColorMap");
         //
         // Lines...
         PolStackUtil.setOrientLineParameters(
                 false, // lineShowPoint
                 7, // orientSeparation
                 5, //vectorLength
                 false, // vectorProportional
                 10, // orientVectorThreshold
                 Color.red, Color.white //lineColor, Color shadowColor
                 );
//			PolStackUtil.makeOrientLineImage(ps, biL);
//			display(biL, "Orient Lines");

         // Overlay with glyphs....
         FrameImageDisplay fidLines = new FrameImageDisplay(ps.sliceAsImage(ps.imgMagnitude));
         // Add an overlay...
         GraphicOverlay overlayLines = new GraphicOverlay() {
            public void drawGraphicOverlay(ZoomGraphics zg) {
//					zg.setColor(Color.red);
//					zg.drawRect(20, 20, 40, 40);
//					PolStackUtil.makeOrientLineOverlay(
//							(byte[]) ps.imgMagnitude, (byte[]) ps.imgAzimuth,
//							0, ps.width, ps.height, zg, BYTE_TYPE, 4);
               //zg.setColor(Color.red);
               Composite originalComposite = zg.getComposite();
               float alpha = 0.5f;
               zg.setComposite(makeComposite(alpha));
               float[] variance = null;
               PolStackUtil.makeOrientLineOverlay(
                       (byte[]) ps.imgMagnitude, (byte[]) ps.imgAzimuth,
                       0, ps.width, ps.height, zg, BYTE_TYPE, 2);
            }
         };
         fidLines.getImageDisplayPanel().imagePane.addGraphicOverlay(overlayLines);
         //
         // Overlay with glyphs....
         FrameImageDisplay fid = new FrameImageDisplay(ps.sliceAsImage(ps.imgMagnitude));
         // Add an overlay...
         GraphicOverlay overlay = new GraphicOverlay() {
            public void drawGraphicOverlay(ZoomGraphics zg) {
//					zg.setColor(Color.red);
//					zg.drawRect(20, 20, 40, 40);
//					PolStackUtil.makeOrientLineOverlay(
//							(byte[]) ps.imgMagnitude, (byte[]) ps.imgAzimuth,
//							0, ps.width, ps.height, zg, BYTE_TYPE, 4);
               zg.setColor(Color.red);
               Composite originalComposite = zg.getComposite();
               float alpha = 0.5f;
               zg.setComposite(makeComposite(alpha));
               float[] variance = null;
               PolStackUtil.makeOrientGlyphOverlay(
                       (byte[]) ps.imgMagnitude, (byte[]) ps.imgAzimuth, variance,
                       0, ps.width, ps.height, zg, BYTE_TYPE, oGlyphs, 30f);
            }
         };
         fid.getImageDisplayPanel().imagePane.addGraphicOverlay(overlay);
      } catch (Exception ex) {
         Logger.getLogger(PolStackUtil.class.getName()).log(Level.SEVERE, null, ex);
      }

   }
//	static void setSaving(boolean t) {
//		saving = t;
//	}
//
//	public static boolean isSaving() {
//		return saving;
// }
}
//	static public BufferedImage makeGrayOutImage(PolStack _pStack) {
//		if (_pStack.depth == 8) {
//			//overlayPixels = new byte[_pStack.size];
//			return GraphicsUtilities.createCompatibleImage(
//					new BufferedImage(_pStack.width, _pStack.height, BufferedImage.TYPE_BYTE_GRAY));
//		} else {
//			//overlayPixels = new short[_pStack.size];
//			return GraphicsUtilities.createCompatibleImage(
//					new BufferedImage(_pStack.width, _pStack.height, BufferedImage.TYPE_USHORT_GRAY));
//		}
//	}
//---------------------------------------------------------------------------
// Orientation Color Mapped Image Generation...
//---------------------------------------------------------------------------
//		public static BufferedImage makeOrientMapImage(PolStack _pStack,
//			boolean overRet,
//			boolean colorMap,
//			boolean orientLines) {
//		Object outImage = null;
//		if (colorMap) {
//			outImage = new BufferedImage(_pStack.width, _pStack.height, BufferedImage.TYPE_INT_RGB);
//			// an experiment... ((BufferedImage) outImage).getRaster().getDataBuffer()
//			overlayPixels = new int[_pStack.size];
//			makeOrientColorImage(_pStack, outImage);
//		} else if (overRet) {
//			outImage = makeOutImage(_pStack);
//			overlayPixels = new byte[_pStack.size];
//			makeRetardanceImage(_pStack, outImage);
//		}
//		if (outImage == null) {
//			outImage = makeOutImage(_pStack); // Orient lines without ret. image
//		}
//		if (orientLines) {
//			// makeOrientLineImage(_pStack, outImage);
//		}
////    if (colorMap && Prefs.usr.getBoolean("psView.indexColorModel", false)) {
////      BufferedImage indexImage = ImageUtils.convertToIndexColor(outImage);
////      ImgInfoDumper.ImgInfoDumper(indexImage);
////      return indexImage;
////    }
//		return (BufferedImage) outImage;
//	}
//----------------------------------------------------------------
//	static void makeRetardanceImage(PolStack _pStack, Object outImage) {
//		WritableRaster wr2 = ((BufferedImage) outImage).getRaster();
//		if (_pStack.depth == 8) {
//			for (int i = 0; i < ((byte[]) overlayPixels).length; i++) {
//				((byte[]) overlayPixels)[i] = ((byte[]) _pStack.imgMagnitude)[i];
//			}
//		} else {
//			for (int i = 0; i < ((short[]) overlayPixels).length; i++) {
//				((short[]) overlayPixels)[i] = ((short[]) _pStack.imgMagnitude)[i];
//			}
//		}
//		wr2.setDataElements(0, 0, _pStack.width, _pStack.height, overlayPixels);
//    ComponentColorModel  ccm =
//      new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
//        new int[] {8}, false, false, Transparency.OPAQUE,
//        DataBuffer.TYPE_USHORT);
//    ComponentSampleModel csm =
//      new ComponentSampleModel(DataBuffer.TYPE_BYTE, _pStack.width,
//        _pStack.height, 1, _pStack.height, new int[] {0});
//    DataBuffer           dataBuf =
//      new DataBufferByte((byte[]) _pStack.imgMagnitude, 8);
//    wr2.setDataElements(0, 0, _pStack.width, _pStack.height, dataBuf);
//}
//
//
//
///////////////////////////////////////////////////////////////////////
// savePolStackToDisk... on a separate SwingWorker thread...
//
//----------------------------------------------------------------
// savePolStackToDisk
//
/*   public static void savePolStackToDiskNOT (PolStack _pStack,
 String _path) {
 final PolStack pStack = _pStack;

 // remove .tiff extension
 final String path = FileUtil.getJustPath(_path) + "\\" +
 FileUtil.getJustFilenameNoExt(_path);
 FileUtil.removeExtension(_path);

 //      new Thread(new Runnable()
 //      {
 //         public void run () {
 setSaving(true);
 edu.mbl.jif.utils.PSjUtils.statusProgress("Saving PolStack: " + path + "...", 75);
 int w = pStack.width;
 int h = pStack.height;
 int dataType = pStack.dataType;
 try {
 ArrayList imgs = new ArrayList();
 // save as PolStack
 if ((pStack.imgMagnitude != null) &&
 (pStack.imgAzimuth != null)) {
 BufferedImage retImage =
 ImageUtils.createImage(w, h, dataType, pStack.imgMagnitude);
 if (Prefs.usr.getBoolean("acq_addStripePS", true)) {
 addDataStripeToImage(retImage, pStack.psParms);
 }
 imgs.add(retImage);
 imgs.add(ImageUtils.createImage(w, h, dataType, pStack.imgAzimuth));

 }
 // save as Raw PolStack
 if (pStack.numSlices > 0) {
 imgs.add(ImageUtils.createImage(w, h, dataType, pStack.slice0));
 }
 if (pStack.numSlices > 1) {
 imgs.add(ImageUtils.createImage(w, h, dataType, pStack.slice1));
 }
 if (pStack.numSlices > 2) {
 imgs.add(ImageUtils.createImage(w, h, dataType, pStack.slice2));
 }
 if (pStack.numSlices > 3) {
 imgs.add(ImageUtils.createImage(w, h, dataType, pStack.slice3));
 }
 if (pStack.numSlices > 4) {
 imgs.add(ImageUtils.createImage(w, h, dataType, pStack.slice4));
 }
 MultipageTiffFile.saveImageArrayList(imgs, path + ".tif");
 }
 catch (Exception ex) {
 ex.printStackTrace();
 //               DialogBoxI.boxError("Error saving PolStack",
 //                                   "Exception in savePolStackToDisk: " +
 //                                   ex.getMessage());
 //               edu.mbl.jif.utils.PSjUtils.event(
 //                     "Exception in savePolStackToDisk: " + ex.getMessage());
 }

 // save parameters to logNotes testFile
 if (Prefs.usr.getBoolean("acq_recordLogNotes", true)) {
 createPSDataFile(pStack.psParms, pStack.polStackID);
 }

 // Save parameters to .prm testFile
 if (Prefs.usr.getBoolean("data.savePolStackParms", true)) {
 String prmFile = DataAccess.pathLog() +
 FileUtil.removeExtension(pStack.polStackID) + ".prm";

 if (Globals.isDeBug()) {
 System.out.println("pStack.polStackID: " + pStack.polStackID);
 System.out.println("prmFile: " + prmFile);
 }
 PSParmsDataMgr.saveToFile(pStack.psParms, prmFile);
 }

 // add it to the ImageManager
 updateImageManager(path);
 edu.mbl.jif.utils.PSjUtils.event("PolStack Saved: " + path);
 edu.mbl.jif.utils.PSjUtils.statusClear();
 setSaving(false);
 //         }
 //      }).start();
 }
 */
//----------------------------------------------------------------
// saveRawPolStackToDisk * RAW *
//
/* public static void saveRawPolStackToDisk (PolStack _pStack, String _path) {
 final PolStack pStack = _pStack;
 // remove .tiff extension
 final String path = FileUtil.getJustPath(_path) + "\\" +
 FileUtil.getJustFilenameNoExt(_path);

 //    new Thread(new Runnable() {
 //        public void run() {
 setSaving(true);

 edu.mbl.jif.utils.PSjUtils.statusProgress("Saving PolStack: " + path + "...",
 75);

 try {
 if (pStack.imgMagnitude == null) { // save as Raw PolStack
 //TiffMultipage.saveAsMultipageTIFF(pStack.imageArrayRaw(), path);
 ArrayList imgs = new ArrayList();

 if (pStack.numSlices > 0) {
 imgs.add((BufferedImage) pStack.slice0);
 }
 if (pStack.numSlices > 1) {
 imgs.add((BufferedImage) pStack.slice1);
 }
 if (pStack.numSlices > 2) {
 imgs.add((BufferedImage) pStack.slice2);
 }
 if (pStack.numSlices > 3) {
 imgs.add((BufferedImage) pStack.slice3);
 }
 if (pStack.numSlices > 4) {
 imgs.add((BufferedImage) pStack.slice4);
 }
 MultipageTiffFile.saveImageArrayList(imgs, path);

 }
 }
 catch (Exception ex) {
 DialogBoxI.boxError("Error saving PolStack",
 "Exception in savePolStackToDisk: " + ex.getMessage());
 edu.mbl.jif.utils.PSjUtils.event(
 "Exception in savePolStackToDisk: " + ex.getMessage());
 }
 edu.mbl.jif.utils.PSjUtils.event("PolStackRaw Saved: " + path);

 edu.mbl.jif.utils.PSjUtils.statusClear();

 setSaving(false);
 //        }
 //      }).start();
 }
 */
/////////////////////////////////////////////////////////////////////////////
// Saves this PolStack to a Tiff testFile
//
//   public static void saveToFile(PolStack8 pStack, String filename) {
//      // Add DataStripe
//      try {
//         if (Prefs.usr.getBoolean("acq_addStripe", true)) {
//            TiffMultipage.saveAsMultipageTIFF(pStack.imageArrayWithStripe(),
//               filename);
//         } else {
//            TiffMultipage.saveAsMultipageTIFF(pStack.imageArray(), filename);
//         }
//      } catch (Exception ex) {
//         System.out.println("Exception in saveToFile");
//      }
//   }
/////////////////////////////////////////////////////////////////////////
// Saves RawPolStack to a Tiff testFile - no Mag or Azim images/slices
//
//   public static void saveRawToFile (PolStack8 pStack, String filename) {
//      // Add DataStripe
//      try {
//         TiffMultipage.saveAsMultipageTIFF(pStack.imageArrayRaw(),
//               filename);
//      }
//      catch (Exception ex) {
//         edu.mbl.jif.utils.PSjUtils.event("Exception in saveRawToFile");
//      }
//   }
