package edu.mbl.jif.xml;

import org.xml.sax.*;             // The main SAX package
import org.xml.sax.helpers.*;     // SAX helper classes
import java.io.*;                 // For reading the input file
import java.util.*;               // Vector, StringTokenizer, ...

/**
 * Parse a file using the SAX2 API and the Xerces parser, and count the
 * words contained within specific elements.
 */
public class WordCounter extends DefaultHandler {

   /* We use the Xerces SAX implementation */
   private static final String READER = "org.apache.xerces.parsers.SAXParser";

   private XMLReader reader;
   private StringBuffer accumulator;
   private int wordCount;
   private Vector elements;
   private boolean countThisElement;

   /** Constructor **/
   public WordCounter() throws SAXException {

      // Instantiate the reader:
      reader = XMLReaderFactory.createXMLReader(READER);

      // This class does all handling:
      reader.setContentHandler( this );
      reader.setErrorHandler( this );
   }

   /**
   * Counts words from the file specified, contained within
   * all elements.
   */
   public void countWords( String filePath ) throws SAXException,IOException {
      countWords( filePath,null );
   }

   /**
   * Counts words from the file specified, contained within
   * the elements specified in the Vector.
   */
   public void countWords( String filePath, Vector elements ) throws SAXException,IOException {
      this.elements = elements;
      reader.parse( filePath );
   }

   /** Returns the number of words counted in the last file parsed **/
   public int getWordCount() {
      return wordCount;
   }

   /**
   * Counts the words in the string buffer, and
   * adds to the total.
   */
   private void addToCount() {
      if ( accumulator.toString() != null ) {
         StringTokenizer strTok = new StringTokenizer(
                                           accumulator.toString().trim() );
         wordCount += strTok.countTokens();
      }
   }

    /** Called at the beginning of parsing **/
    public void startDocument() {
        // we initialize our string buffer, and
        // reset the word count at the beginning of parsing
        accumulator = new StringBuffer();
        wordCount = 0;
    }

    /**
     * This method is called when the parser encounters textual data.
     * Since the parser may call this method multiple times (without
     * intervening elements), we accumulate the characters in a string
     * buffer.
     */
    public void characters(char[] buffer, int start, int length) {
        accumulator.append(buffer, start, length);
    }

    /**
    * When beginning to parse a new element, we erase all data from
    * the string buffer. We also determine if we should count the
    * words contained within this element.
    */
    public void startElement(String namespaceURI, String localName,
                             String fullName, Attributes attributes) {
        accumulator.setLength(0);
        countThisElement = ( elements == null || elements.contains(localName) );
    }

   /**
   * After having parsed an element we count the words in the
   * string buffer.
   */
    public void endElement(String namespaceURI, String localName, String fullName) {
        if ( countThisElement ) {
           addToCount();
      }
    }

    /**
     * The main can be used for command line testing.
     */
   public static void main( String[] args ) throws IOException, SAXException {

      if ( args.length == 0 ) {
         System.out.println("Usage: java WordCounter <XMLFile> [<elementName> ...]");
         System.exit(0);
      }
      WordCounter wc = new WordCounter();

      // get the file path:
      String filePath = args[0];
      // get elements from which words are to be counted:
      // (if no elements are supplied, all words will be counted)
      Vector countElements = null;
      if ( args.length > 1 ) {
         countElements = new Vector();
         for ( int i = 1; i < args.length; i++ ) {
            countElements.add(args[i]);
         }
      }

      // count:
      try {
         wc.countWords( filePath, countElements );
         System.out.println("Total number of words: " + wc.getWordCount());
      } catch ( IOException e ) {
         System.out.println("Error reading file: " + e.getMessage());
      } catch ( SAXException e ) {
         System.out.println("Error parsing file: " + e.getMessage());
      }
    }

    /** Report a warning **/
    public void warning(SAXParseException exception) {
        System.err.println("WARNING: line " + exception.getLineNumber() + ": "+
                           exception.getMessage());
    }

    /** Report a parsing error **/
    public void error(SAXParseException exception) {
        System.err.println("ERROR: line " + exception.getLineNumber() + ": " +
                           exception.getMessage());
    }

    /** Report a fatal error and exit **/
    public void fatalError(SAXParseException exception) throws SAXException {
        System.err.println("FATAL: line " + exception.getLineNumber() + ": " +
                           exception.getMessage());
        throw(exception);
    }
}

