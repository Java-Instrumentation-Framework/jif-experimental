package tests.imageio;

import java.io.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.event.*;
import javax.imageio.stream.*;

public class IIOTest1 

{
    PrintStream out;
    String iname, oname;
    BufferedImage im;

    public IIOTest1(String i) {
	this(System.out, i, null);
    }
    public IIOTest1(PrintStream ps, String i, String o) {
	out = ps;
	iname = i; 
	oname = o;
	im = null;
    }

    public void msg(String s) { out.println(s); }
    public void msg(String p, String [] l) {
	for(int i = 0; i < l.length; i++) { out.print(p + (i+1) + ". "); out.println(l[i]); }
    }
    public void printInfo() {
	String fmts[];

	msg("IIO API Info:");
	msg("    Available read formats:");
	fmts = ImageIO.getReaderFormatNames();
	msg("\t", fmts);
	msg("    Available read MIME Types:");
	fmts = ImageIO.getReaderMIMETypes();
	msg("\t", fmts);
	msg("    Available write formats:");
	fmts = ImageIO.getWriterFormatNames();
	msg("\t", fmts);
    }

    public boolean doRead() {
	ImageReader r = null;
	boolean ret = false;
	try {
	    String ext = getExtension(iname);
	    msg("Got extension: '" + ext + "'");
	    Iterator readers = ImageIO.getImageReadersBySuffix(ext);
	    if (!(readers.hasNext())) {
		msg("No readers for extension " + ext);
		return false;
	    }
	    r = (ImageReader)(readers.next());
	    if (r == null) {
		msg("No readers for extension " + ext);
		return false;
	    }
	    File fi = new File(iname);
	    ImageInputStream iis = ImageIO.createImageInputStream(fi);
	    r.setInput(iis, true);
	    msg("Created reader and stream.");
	    r.addIIOReadProgressListener(new ReadProgressReporter(out));
	    im = r.read(0);
	    if (im == null) {
		msg("Unable to read image 0 ?!");
		ret = false;
	    }
	    else {
		msg("Read image " + iname + " okay, size=" +
		    im.getWidth() + "x" + im.getHeight());
		ret = true;
	    }
	}
	catch (IIOException iioe) {
	    msg("ImageIO read Exception: " + iioe);
	    iioe.printStackTrace(out);
	    ret = false;
	}
	catch (Exception e) {
	    msg("Other exception: " + e);
	    e.printStackTrace(out);
	    ret = false;
	}
	finally {
	    if (r != null) r.dispose();
	}
	return ret;
    }

    public boolean doWrite() {
	boolean ret = false;
	ImageWriter w = null;
	if (oname == null) return false;

	try {
	    String ext = getExtension(oname);
	    msg("Got output extension: '" + ext + "'");
	    Iterator writers = ImageIO.getImageWritersBySuffix(ext);
	    if (!(writers.hasNext())) {
		msg("No readers for extension " + ext);
		return false;
	    }
	    w = (ImageWriter)(writers.next());
	    if (w == null) {
		msg("No writers for extension " + ext);
		return false;
	    }
	    File fo = new File(oname);
	    ImageOutputStream ios = ImageIO.createImageOutputStream(fo);
	    w.setOutput(ios);
	    WriteProgressReporter wpr = new WriteProgressReporter(out);
	    w.addIIOWriteProgressListener(wpr);
	    msg("Created stream and writer.");
	    w.write(im);
	    msg("Apparently able to write image to " + oname);
	    ret = true;
	}
	catch (IIOException iioe) {
	    msg("ImageIO write Exception: " + iioe);
	    iioe.printStackTrace(out);
	    ret = false;
	}
	catch (Exception e) {
	    msg("Other exception: " + e);
	    e.printStackTrace(out);
	    ret = false;
	}
	finally {
	    if (w != null) w.dispose();
	}
	return ret;
    }

    public static float blur[] = {
	0.001F, 0.005F, 0.005F, 0.005F, 0.001F,
	0.005F, 0.1F, 0.1F, 0.1F, 0.005F,
	0.005F, 0.1F, 0.3F, 0.1F, 0.005F,
	0.005F, 0.1F, 0.1F, 0.1F, 0.005F,
	0.001F, 0.005F, 0.005F, 0.005F, 0.001F
    };


    public void doProcessing() {
	if (im == null) return;
	int wid = im.getWidth();
	int hgt = im.getHeight();
	Raster imr = im.getData();
	msg("Beginning processing");
	WritableRaster newr = imr.createCompatibleWritableRaster();
	Kernel blurkernel = new Kernel(5,5,blur);
	ConvolveOp op = new ConvolveOp(blurkernel, ConvolveOp.EDGE_ZERO_FILL, null);
	op.filter(imr, newr);
	msg("Creating new image");
	BufferedImage im2 = new BufferedImage(im.getColorModel(), newr, 
					      im.isAlphaPremultiplied(), null);
	im = im2;
    }

    public class ReadProgressReporter 
	implements IIOReadProgressListener
    {
	PrintStream ps;
	public ReadProgressReporter(PrintStream p) { ps = p; }

	// event handling methods for reading 
	public void sequenceStarted(ImageReader source, int minIndex) { 
	    ps.println("    listener: Read sequence started.");
	} 
	public void sequenceComplete(ImageReader source) { 
	    ps.println("    listener: Read sequence completed.");
	} 
	public void imageStarted(ImageReader source, int imageIndex) { 
	    ps.println("    listener: image started, index=" + imageIndex);
	} 
	public void imageProgress(ImageReader source, float percentageDone) { 
	    ps.print(".");
	} 
	public void imageComplete(ImageReader source) { 
	    ps.println("\n    listener: Image read completed.");
	} 
	public void thumbnailStarted(ImageReader source, int imageIndex, int thumbnailIndex) { } 
	public void thumbnailProgress(ImageReader source, float percentageDone) { } 
	public void thumbnailComplete(ImageReader source) { } 
	public void readAborted(ImageReader source)  { 
	    ps.println("    listener: Image read aborted.");
	}
    }    

    public class WriteProgressReporter 
	implements IIOWriteProgressListener
    {
	PrintStream ps;
	public WriteProgressReporter(PrintStream p) { ps = p; }
	// event handling methods for writing
	public void imageStarted(ImageWriter source, int imageIndex) {
	    ps.println("    listener: Image write started.");
	}
	public void imageProgress(ImageWriter source, float percentageDone)  {
	    ps.print(">");
	}
	public void imageComplete(ImageWriter source)  {
	    ps.println("\n    listener: Image write completed.");
	}
	public void thumbnailStarted(ImageWriter source, int imageIndex, int thumbnailIndex) { } 
	public void thumbnailProgress(ImageWriter source, float percentageDone) { } 
	public void thumbnailComplete(ImageWriter source) { } 
	public void writeAborted(ImageWriter source) {
	    ps.println("\n    listener: Image write aborted.");
	}
    }

    public final static String getExtension(String name) {
	int eix = name.lastIndexOf('.');
	if (eix < 0) return null;
	else return name.substring(eix + 1);
    }

    public static void main(String [] args) {
	String iname = null;
	String oname = null;
	if (args.length < 1) {
	    System.out.println("Usage: java IIOTest1 infile [outfile]");
	    System.exit(0);
	}

	iname = args[0];
	if (args.length >1) oname = args[1];

	IIOTest1 t = new IIOTest1(System.out, iname, oname);
	t.printInfo();
	if (t.doRead()) {
	    System.out.println("Read succeeded");
	    t.doProcessing();
	    if (t.doWrite()) {
		System.out.println("Write succeeded.");
	    }
	    else {
		System.out.println("Write failed.");
	    }
	}
	else {
	    System.out.println("Read failed.");
	}
    }
}
