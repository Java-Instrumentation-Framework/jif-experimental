package tests.imageio;

import java.io.*;
import java.util.*;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/**
 *  Simple, functional ImageReaderSpi used to understand how
 *  information regarding format name, suffices and mime types
 *  get passed to ImageIO static methods
 */
public class ch5ImageReaderSpi extends ImageReaderSpi {

    static final String[] suffixes = {"ch5", "CH5"};
    static final String[] names = {"ch5"};
    static final String[] MIMETypes = {"image/ch5"};

    static final String version = "1.00";
    static final String readerCN = "ch5.imageio.plugins.ch5ImageReader";
    static final String vendorName = "CompanyName";

    //writerSpiNames
    static final String[] wSN={"ch5.imageio.plugins.ch5ImageWriterSpi"};

    //StreamMetadataFormatNames and StreamMetadataFormatClassNames
    static final boolean supportedStandardStreamMetadataFormat = false;
    static final String nativeStreamMFN = "ch5.imageio.ch5stream_1.00";
    static final String nativeStreamMFCN = "ch5.imageio.ch5stream";
    static final String[] extraStreamMFN = null;
    static final String[] extraStreamMFCN = null;

    //ImageMetadataFormatNames and ImageMetadataFormatClassNames
    static final boolean supportedStandardImageMetadataFormat = false;
    static final String nativeImageMFN = "ch5.imageio.ch5image1.00";
    static final String nativeImageMFCN = "ch5.imageio.ch5image";
    static final String[] extraImageMFN = null;
    static final String[] extraImageMFCN = null;

    public ch5ImageReaderSpi() {
	super(vendorName,
	      version,
	      names,
	      suffixes,
	      MIMETypes,
	      readerCN, //readerClassName
	      STANDARD_INPUT_TYPE,
	      wSN, //writerSpiNames
              false,
              nativeStreamMFN,
              nativeStreamMFCN,
              extraStreamMFN,
              extraStreamMFCN,
              false,
              nativeImageMFN,
              nativeImageMFCN,
              extraImageMFN,
              extraImageMFCN);
    }

    public String getDescription(Locale locale) {
	return "Demo ch5 image reader, version " + version;
    }

    public ImageReader createReaderInstance(Object extension) {
	return new ch5ImageReader(this);
    }

    /**
     * This method gets called when an application wants to see if
     * the input image's format can be decoded by this ImageReader.
     * In this case, we'll simply check the first byte of data to
     * see if its a 5 which is the format type's magic number
     */
    public boolean canDecodeInput(Object input) {
	boolean reply = false;

	ImageInputStream iis = (ImageInputStream)input;
	iis.mark(); // mark where we are in ImageInputStream
	try {
	    String magicNumber = iis.readLine().trim();
	    iis.reset(); // reset stream back to marked location
	    if (magicNumber.equals("5"))
		reply = true;
	}
	catch (IOException exception) {
	}
	return reply;
    }
}
