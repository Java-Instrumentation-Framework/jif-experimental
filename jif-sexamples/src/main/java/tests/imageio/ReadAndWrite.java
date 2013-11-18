/*
 * ReadAndWrite.java
 *
 * Created on December 13, 2006, 3:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.imageio;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author GBH
 */
public class ReadAndWrite {
    
    /** Creates a new instance of ReadAndWrite */
    public ReadAndWrite() {
    }
    

// Read and Write
public static void main(String[] args) throws IOException {
  File f = new File(args[0]);
  ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("tiff").next();
  ImageInputStream iis = ImageIO.createImageInputStream(f);
  reader.setInput(iis,false);
  
  ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("tiff").next();
  File f1 = new File(args[1]);
  ImageOutputStream ios = ImageIO.createImageOutputStream(f1);
  writer.setOutput(ios);
  ImageWriteParam param = writer.getDefaultWriteParam();
  writer.write(reader.getImageMetadata(1),reader.readAll(1, reader.getDefaultReadParam()),param); // 2'nd page
  writer.writeInsert(-1,reader.readAll(2 , reader.getDefaultReadParam()),param); // 3'rd page
  writer.writeInsert(-1,reader.readAll(3, reader.getDefaultReadParam()),param); // 4'th page
  System.out.println("Successfully write the image");
  
  iis.close();
  ios.close();
 }
 }
