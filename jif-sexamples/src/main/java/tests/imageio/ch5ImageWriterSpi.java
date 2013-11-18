package tests.imageio;

import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageWriter;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageInputStream;

/**
 *  Simple, functional ImageWriterSpi used to understand how
 *  information regarding format name, suffices and mime types
 *  get passed to ImageIO static methods
 */
public class ch5ImageWriterSpi extends ImageWriterSpi {

    static final String[] suffixes = {"ch5", "CH5"};
    static final String[] names = {"ch5"};
    static final String[] MIMETypes = {"image/ch5" };

    static final String version = "1.00";
    static final String writerClassName = "ch5.imageio.plugins.ch5ImageWriter";
    static final String vendorName = "Company Name";
    static final String[] readerSpiNames = {"ch5.imagio.plugins.ch5ImageReaderSpi"};
    /*
    static final String nativeStreamMetadataFormatName = "ch5.imageio.ch5stream_1.0";
    static final String[] streamMetadataFormatNames = {nativeStreamMetadataFormatName};
    static final String nativeImageMetadataFormatName = "ch5.imageio.ch5image_1.0";
    static final String[] imageMetadataFormatNames = {nativeImageMetadataFormatName};
    */

    static final String nativeStreamMetadataFormatName = "ch5.imageio.ch5stream_1.00";
    static final String nativeStreamMetadataFormatClassName = "ch5.imageio.ch5stream";
    static final String[] extraStreamMetadataFormatNames = {null};
    static final String[] extraStreamMetadataFormatClassNames = {null};

    static final String nativeImageMetadataFormatName = "ch5.imageio.ch5image_1.00";
    static final String nativeImageMetadataFormatClassName = "ch5.imageio.ch5image";
    static final String[] extraImageMetadataFormatNames = {null};
    static final String[] extraImageMetadataFormatClassNames = {null};

    public ch5ImageWriterSpi() {
	super(vendorName,
	      version,
	      names,
	      suffixes,
	      MIMETypes,
	      writerClassName,
	      STANDARD_OUTPUT_TYPE,
	      readerSpiNames,
              false,
              nativeStreamMetadataFormatName,
              nativeStreamMetadataFormatClassName,
              extraStreamMetadataFormatNames,
              extraStreamMetadataFormatClassNames,
              false,
              nativeImageMetadataFormatName,
              nativeImageMetadataFormatClassName,
              extraImageMetadataFormatNames,
              extraImageMetadataFormatClassNames);

    }

    public String getDescription(Locale locale) {
	return "Demo ch5 image writer, version " + version;
    }


    public ImageWriter createWriterInstance(Object extension) {
	return new ch5ImageWriter(this);
    }

    /**
     * This method gets called when an application wants to see if
     * the corresponding ImageWriter can encode an image with
     * a ColorModel and SampleModel specified by the ImageTypeSpecifier
     */
    public boolean canEncodeImage(ImageTypeSpecifier its) {
	if (its.getBufferedImageType() == BufferedImage.TYPE_BYTE_GRAY)
	    return true;
	else
	    return false;
    }
}
