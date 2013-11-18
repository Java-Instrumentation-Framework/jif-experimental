
package tests.imaging;

import com.sun.media.imageio.plugins.tiff.TIFFImageReadParam;
import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;
import com.sun.media.jai.codec.ImageCodec;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.RasterFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class AppendTiffTest {
    
    String tiffFilename;
    ImageOutputStream ios;
    ImageWriter writer;
    TIFFImageWriteParam paramWrite;
    private boolean writable = false;
    private boolean sequenceWritable = false;
    ImageReader reader;
    TIFFImageReadParam paramRead;
    private boolean readable = false;
    int nextImg = -1;
    /** Creates a new instance of AppendTiffTest */
    public AppendTiffTest() {
    }
    
    public String getFilename() {
        return tiffFilename;
    }
    
    public boolean isWritable() {
        return writable;
    }
    
    public boolean canWriteSequence() {
        return sequenceWritable;
    }
    
    // Open file for writing
    public void openWrite(String _filename) {
        tiffFilename = filenameWithTifExt(_filename);
        try {
            String ext = "tif";
            Iterator writers = ImageIO.getImageWritersByFormatName(ext);
            writer = (ImageWriter) writers.next();
            File tifFile = new File(tiffFilename);
            ios = ImageIO.createImageOutputStream(tifFile);
            writer.setOutput(ios);
            paramWrite = (TIFFImageWriteParam) writer.getDefaultWriteParam();
            if (writer.canWriteSequence()) { //	i.e tiff, sff(fax)
                writer.prepareWriteSequence(null);
                sequenceWritable = true;
            } else {
                System.err.println("Cannot WriteSequence");
                sequenceWritable = false;
            }
            writable = true;
        } catch (Exception e) {
            System.err.println("Error opening to write: " + tiffFilename);
            e.printStackTrace();
            writable = false;
        }
    }
    
    
    public void appendImage(BufferedImage image) {
        if (writer == null) {
            System.err.println("Image append error: no MultipageTiff writer.");
            return;
        }
        appendImage(image, null, null);
    }
    
    public void appendImage(BufferedImage image, List thumbnails, IIOMetadata meta) {
        if (writer == null) {
            System.err.println("Image append error: no MultipageTiff writer.");
            return;
        }
        try {
            IIOImage iioimg = new IIOImage(image, thumbnails, meta);
            writer.writeToSequence(iioimg, null);
        } catch (Exception e) {
            System.err.println("Image append Error : " + e.getMessage());
        }
    }
    
    // close the file
    public void close() {
        try {
            if (writer != null) {
                writer.endWriteSequence();
                ios.close();
                ios = null;
                writer.dispose();
            }
            if (reader != null) {
                reader.dispose();
            }
        } catch (IOException ex) {
            System.err.println("Close");
            ex.printStackTrace();
        }
    }
    
    
    
    //=====================================================================
    // >>> Static utility methods
    public static String filenameWithTifExt(String filename) {
        return removeExtension(filename) + "." + "tif";
    }
    
    public static String removeExtension(String f) {
        String ext = null;
        if (f == null) {
            return null;
        }
        int i = f.lastIndexOf('.');
        if (i > 1) {
            ext = f.substring(0, i);
            return ext;
        } else {
            return f;
        }
    }
    
    static boolean exists(String aFileName) {
        File f = new File(aFileName);
        return f.exists();
    } // exists
    
    static boolean deleteFile(String fileName) {
        // A File object to represent the filename
        File f = new File(fileName);
        
        // Make sure the file or directory exists and isn't write protected
        if (!f.exists()) {
            System.err.println("deleteFile: cannot delete, no such file or directory: " + fileName);
            return false;
        }
        if (!f.canWrite()) {
            System.err.println("deleteFile: cannot delete - write protected: " + fileName);
            return false;
        }
        // If it is a directory, make sure it is empty
        if (f.isDirectory()) {
            String[] files = f.list();
            if (files.length > 0) {
                System.err.println("Delete: directory not empty: " + fileName);
                return false;
            }
        }
        
        // Attempt to delete it
        boolean success = f.delete();
        if (!success) {
            System.err.println("Delete: Delete failed!!: " + fileName);
            return false;
            //throw new IllegalArgumentException("Delete: deletion failed");
        }
        return success;
    }
    
    // <<< End of static utility methods
    
    
    
    
    public static void appendImageToTiffFile(BufferedImage outImage, String pathFile) {
        AppendTiffTest tif = new AppendTiffTest();
        tif.openWrite(pathFile);
        tif.appendImage(outImage);
        tif.close();;
    }
    
    
    public static void main(String[] args) {
        BufferedImage img = GrayScaleImageFactory.testImageByte();
        appendImageToTiffFile(img, "imageFile");
    }
    
}


class GrayScaleImageFactory {
   // create from byte[]
   public static BufferedImage createImage(int imageWidth, int imageHeight, int imageDepth,
      byte[] data) {
      ComponentColorModel  ccm =
         new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
            new int[] { imageDepth }, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
      ComponentSampleModel csm =
         new ComponentSampleModel(DataBuffer.TYPE_BYTE, imageWidth, imageHeight, 1, imageWidth,
            new int[] { 0 });
      DataBuffer           dataBuf = new DataBufferByte((byte[])data, imageWidth);
      WritableRaster       wr = Raster.createWritableRaster(csm, dataBuf, new Point(0, 0));
      Hashtable            ht = new Hashtable();
      ht.put("owner", "PSj");
      return new BufferedImage(ccm, wr, true, ht);
   }


   public static BufferedImage testImageByte() {
      return testImageByte(256, 256);
   }

   public static BufferedImage testImageByte(int wid, int ht) {
      float  max = 256f;
      int    len = wid * ht;
      byte[] data = new byte[len];
      double scale = max / (float)len;
      for (int i = 0; i < len; i++) {
         data[i] = (byte)((float)i * scale);
      }
      BufferedImage bi = createImage(wid, ht, 8, data);
      return bi;
   }

  
}



