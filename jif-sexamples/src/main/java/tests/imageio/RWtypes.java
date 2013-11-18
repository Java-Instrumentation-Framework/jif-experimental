package tests.imageio;
import javax.imageio.ImageIO;

/**
 * RWtypes.java - a class to display available ImageReaders and
 * ImageWriters by image format and MIME type
 */
public class RWtypes {
    public static void main(String[] args) {
	String[] readers, writers;

	System.out.println("For Reading:");
	readers = ImageIO.getReaderFormatNames();
	System.out.println("\tBy format:");
	for (int i=0; i<readers.length;i++)
	    System.out.println("\t\t" + readers[i]);

	readers = ImageIO.getReaderMIMETypes();
	System.out.println("\tBy MIME Types:");
	for (int i=0; i<readers.length;i++)
	    System.out.println("\t\t" + readers[i]);

	System.out.println("For Writing:");
	writers = ImageIO.getWriterFormatNames();
	System.out.println("\tBy format:");
	for (int i=0; i<writers.length;i++)
	    System.out.println("\t\t" + writers[i]);

	writers = ImageIO.getWriterMIMETypes();
	System.out.println("\tBy MIME Types:");
	for (int i=0; i<writers.length;i++)
	    System.out.println("\t\t" + writers[i]);
    }
}

