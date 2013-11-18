package edu.mbl.jif.testimageio;
/*
 * #%L
 * OME Bio-Formats package for reading and converting biological file formats.
 * %%
 * Copyright (C) 2005 - 2013 Open Microscopy Environment:
 *   - Board of Regents of the University of Wisconsin-Madison
 *   - Glencoe Software, Inc.
 *   - University of Dundee
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

import java.io.IOException;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.ImageWriter;
import loci.formats.meta.IMetadata;
import loci.formats.services.OMEXMLService;

import ome.xml.model.enums.DimensionOrder;
import ome.xml.model.enums.EnumerationException;
import ome.xml.model.enums.PixelType;
import ome.xml.model.primitives.PositiveInteger;

/**
 * Example class that shows how to export raw pixel data to OME-TIFF using Bio-Formats version 4.2
 * or later.
 */
public class FileExport {

   /**
    * The file writer.
    */
   private ImageWriter writer;
   /**
    * The name of the output file.
    */
   private String outputFile;

   /**
    * Construct a new FileExport that will save to the specified file.
    *
    * @param outputFile the file to which we will export
    */
   public FileExport(String outputFile) {
      this.outputFile = outputFile;
   }

   /**
    * Save a single 512x512 uint16 plane of random data.
    */
   public void export() {
      int width = 512, height = 512;
      int pixelType = FormatTools.UINT16;

      int channelCount = 1;
      int samplesPerChannel = 1;
      int zSectionCount = 1;
      int timepointCount = 1;
      IMetadata omexml = initializeMetadata(width, height, pixelType,
              channelCount, samplesPerChannel, zSectionCount, timepointCount);

      // only save a plane if the file writer was initialized successfully
      boolean initializationSuccess = initializeWriter(omexml);
      if (initializationSuccess) {
         int n = 1000;
         saveManyPlanes(width, height, pixelType, omexml, n);
      }
      cleanup();
   }

   /**
    * Set up the file writer.
    *
    * @param omexml the IMetadata object that is to be associated with the writer
    * @return true if the file writer was successfully initialized; false if an error occurred
    */
   private boolean initializeWriter(IMetadata omexml) {
      // create the file writer and associate the OME-XML metadata with it
      writer = new ImageWriter();
      writer.setMetadataRetrieve(omexml);

      Exception exception = null;
      try {
         writer.setId(outputFile);
      } catch (FormatException e) {
         exception = e;
      } catch (IOException e) {
         exception = e;
      }
      if (exception != null) {
         System.err.println("Failed to initialize file writer.");
         exception.printStackTrace();
      }
      return exception == null;
   }

   /**
    * Populate the minimum amount of metadata required to export an image.
    *
    * @param width the width (in pixels) of the image
    * @param height the height (in pixels) of the image
    * @param pixelType the pixel type of the image;
    * @see loci.formats.FormatTools
    */
   private IMetadata initializeMetadata(int width, int height, int pixelType,
           int channelCount, int samplesPerChannel, int zSectionCount, int timepointCount) {
      Exception exception = null;
      try {
         // create the OME-XML metadata storage object
         ServiceFactory factory = new ServiceFactory();
         OMEXMLService service = factory.getInstance(OMEXMLService.class);
         IMetadata omexml = service.createOMEXMLMetadata();
         omexml.createRoot();

//         // define each stack of images - this defines a single stack of images
//         meta.setImageID("Image:0", 0);
//         meta.setPixelsID("Pixels:0", 0);
//
//         // specify that the pixel data is stored in big-endian format
//         // change 'TRUE' to 'FALSE' to specify little-endian format
//         meta.setPixelsBinDataBigEndian(Boolean.TRUE, 0, 0);
//
//         // specify that the images are stored in ZCT order
//         meta.setPixelsDimensionOrder(DimensionOrder.XYZCT, 0);
//
//         // specify that the pixel type of the images
//         meta.setPixelsType(
//                 PixelType.fromString(FormatTools.getPixelTypeString(pixelType)), 0);
//
//         // specify the dimensions of the images
//         meta.setPixelsSizeX(new PositiveInteger(width), 0);
//         meta.setPixelsSizeY(new PositiveInteger(height), 0);
//         meta.setPixelsSizeZ(new PositiveInteger(1), 0);
//         meta.setPixelsSizeC(new PositiveInteger(1), 0);
//         meta.setPixelsSizeT(new PositiveInteger(1), 0);
//
//         // define each channel and specify the number of samples in the channel
//         // the number of samples is 3 for RGB images and 1 otherwise
//         meta.setChannelID("Channel:0:0", 0, 0);
//         meta.setChannelSamplesPerPixel(new PositiveInteger(1), 0, 0);
         //
         omexml.setImageID("Image:0", 0);
         omexml.setPixelsID("Pixels:0", 0);

         // specify that the pixel data is stored in big-endian order
         // replace 'TRUE' with 'FALSE' to specify little-endian order
         omexml.setPixelsBinDataBigEndian(Boolean.TRUE, 0, 0);

         omexml.setPixelsDimensionOrder(DimensionOrder.XYCZT, 0);
         omexml.setPixelsType(PixelType.UINT16, 0);
         omexml.setPixelsSizeX(new PositiveInteger(width), 0);
         omexml.setPixelsSizeY(new PositiveInteger(height), 0);
         omexml.setPixelsSizeZ(new PositiveInteger(zSectionCount), 0);
         omexml.setPixelsSizeC(new PositiveInteger(channelCount
                 * samplesPerChannel), 0);
         omexml.setPixelsSizeT(new PositiveInteger(timepointCount), 0);

         for (int channel = 0; channel < channelCount; channel++) {
            omexml.setChannelID("Channel:0:" + channel, 0, channel);
            omexml.setChannelSamplesPerPixel(new PositiveInteger(samplesPerChannel),
                    0, channel);
         }

         return omexml;
      } catch (DependencyException e) {
         exception = e;
      } catch (ServiceException e) {
         exception = e;
//      } catch (EnumerationException e) {
//         exception = e;
      }

      System.err.println("Failed to populate OME-XML metadata object.");
      exception.printStackTrace();
      return null;
   }

   /**
    * Generate a random plane of pixel data and save it to the output file.
    *
    * @param width the width of the image in pixels
    * @param height the height of the image in pixels
    * @param pixelType the pixel type of the image;
    * @see loci.formats.FormatTools
    */
   private void saveManyPlanes(int width, int height, int pixelType,  IMetadata omexml, int n) {
      for (int i = 0; i < n; i++) {
         savePlane(width, height, pixelType, omexml, i);
      }
   }

   // TODO this has not been worked out in terms of what to pass....
   
   private void savePlane(int width, int height, int pixelType, IMetadata omexml, int i) {
      byte[] plane = createImage(width, height, pixelType);
      Exception exception = null;
      try {
         int sizeC = omexml.getPixelsSizeC(0).getValue();
         int sizeZ = omexml.getPixelsSizeZ(0).getValue();
         int sizeT = omexml.getPixelsSizeT(0).getValue();
         omexml.setChannelID("Channel:0:0", 0, 0);
         omexml.setChannelSamplesPerPixel(new PositiveInteger(1), 0, 0);

//         sizeC /= samplesPerChannel;

         int imageCount = sizeC * sizeZ * sizeT;

         for (int image = 0; image < imageCount; image++) {
            writer.saveBytes(image, plane);
         }

      } catch (FormatException e) {
         exception = e;
      } catch (IOException e) {
         exception = e;
      }
      if (exception != null) {
         System.err.println("Failed to save plane.");
         exception.printStackTrace();
      }
   }

   /**
    * Generate a random plane of pixel data.
    *
    * @param width the width of the image in pixels
    * @param height the height of the image in pixels
    * @param pixelType the pixel type of the image;
    * @see loci.formats.FormatTools
    */
   private byte[] createImage(int width, int height, int pixelType) {
      // create a blank image of the specified size
      byte[] img =
              new byte[width * height * FormatTools.getBytesPerPixel(pixelType)];

      // fill it with random data
      for (int i = 0; i < img.length; i++) {
         img[i] = (byte) (256 * Math.random());
      }
      return img;
   }

   /**
    * Close the file writer.
    */
   private void cleanup() {
      try {
         writer.close();
      } catch (IOException e) {
         System.err.println("Failed to close file writer.");
         e.printStackTrace();
      }
   }

   /**
    * To export a file to OME-TIFF:
    *
    * $ java FileExport output-file.ome.tiff
    */
   public static void main(String[] args)
           throws Exception {
      String fileOut = "output-file.ome.tiff";
      FileExport exporter = new FileExport(fileOut);
      exporter.export();
   }
}
