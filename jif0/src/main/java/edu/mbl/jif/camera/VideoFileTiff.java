package edu.mbl.jif.camera;

import java.text.SimpleDateFormat;
import java.util.*;

import java.awt.*;
import java.awt.image.*;

import edu.mbl.jif.imaging.tiff.MultipageTiffFile;


//import psj.DataAccess.DataAccess;

public class VideoFileTiff
{
   String file = null;
   String fileName = null;
   MultipageTiffFile tif;
   int frameH;
   int frameW;
   int bitDepth;
   int framePixels = 0;
   int bytesPerFrame;
   long fileSize = 0;
   int totalFrames = 0;
   boolean openedToRecord = false;
   long timeStamp = 0;
   //int size = width * height;
   BufferedImage image = null;
   WritableRaster wr;
   Graphics graphics;
   Font f = new Font("Monospaced", Font.BOLD, 12);
   SimpleDateFormat formatter =
         new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.getDefault());
   String dateStr;

   //DataBuffer db;

   ////////////////////////////////////////////////////////////////////////////
   // Open video file for Record/Write !*!*!*! This OVERWRITEs existing file !!
   //
   public VideoFileTiff (String _fileName, int _frameW, int _frameH, int _bitDepth) {
      fileName = _fileName;
      frameW = _frameW;
      frameH = _frameH;
      framePixels = frameW * frameH;
      bitDepth = _bitDepth;
      if (bitDepth == 8) {
         bytesPerFrame = framePixels;
      }
      if (bitDepth == 16) {
         return;
         //bytesPerFrame = 2 * framePixels;
      }

      //db = new DataBufferByte(image_data, image_data.length);
      try {
         image = new BufferedImage(frameW, frameH, BufferedImage.TYPE_BYTE_GRAY);
         wr = image.getRaster();
         graphics = image.getGraphics();
         graphics.setFont(f);
      }
      catch (Exception e) {
      }

      // create/open the file
      tif = new MultipageTiffFile(fileName);

      totalFrames = 0;
      openedToRecord = true;

   }


   ////////////////////////////////////////////////////////////////////////////
   // Open video file for Play/Read
   //
   public VideoFileTiff (String _file) { //throws Exception {
      file = _file;
      // create/open the file
      tif = new MultipageTiffFile(file);
      totalFrames = 0;
      openedToRecord = true;
   }


/////////////////////////////////////////////////////////////////////

   public int writeFrame (byte[] frameArray, long timeStamp) {
      wr.setDataElements(0, 0, frameW, frameH, frameArray);
      dateStr = formatter.format(new Date());
      graphics.setColor(Color.black);
      graphics.drawString(dateStr, 10, 15);
      graphics.setColor(Color.white);
      graphics.drawString(dateStr, 9, 14);
      tif.appendImage(image);
      totalFrames++;
      return 0;
   }


   public int writeFrame (Image img) {
      tif.appendImage((BufferedImage) img);
      totalFrames++;
      return 0;
   }


/////////////////////////////////////////////////////////////////////

   public BufferedImage readFrame (int frameNumber) throws Exception {
      return null;
   }


/////////////////////////////////////////////////////////////////////

   public String getFileName () {
      return file;
   }


   public long getTimeStamp () {
      return timeStamp;
   }


   public int getWidth () {
      return frameW;
   }


   public int getHeight () {
      return frameH;
   }


   public int getBitDepth () {
      return bitDepth;
   }


   public int getTotalFrames () {
      return totalFrames;
   }


   public int close () {
      tif.close();
      return 0;
   }


   //----------------------------------------------------------------------------

   public static void main (String[] args) {
      String tempFile = "";
      //DataAccess.pathVideo() + "VID_"
      //   + edu.mbl.jif.utils.PSjUtils.timeStamp() + ".tif";
      byte[] frameArray = new byte[512 * 512];
      for (int i = 0; i < frameArray.length; i++) {
         frameArray[i] = 32;
      }

      VideoFileTiff vf = new VideoFileTiff(tempFile, 511, 511, 8);
      for (int i = 0; i < 100; i++) {
         long timeStamp = System.nanoTime();
         vf.writeFrame(frameArray, timeStamp);
      }
      vf.close();
   }
}
