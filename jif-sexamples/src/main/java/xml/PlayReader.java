package edu.mbl.jif.xml;

import org.xml.sax.*;             // The main SAX package
import org.xml.sax.helpers.*;     // SAX helper classes
import java.io.*;                 // For reading the input file
import java.util.Vector;

/**
 * Parse a play file using the SAX2 API and the Xerces parser.
 *
 */
public class PlayReader extends DefaultHandler {

   /* We use the Xerces SAX implementation */
   private static final String READER = "org.apache.xerces.parsers.SAXParser";

   private XMLReader reader;
   private StringBuffer accumulator;
   private String playTitle;
   private Vector personae;

   /** Constructor **/
   public PlayReader() throws SAXException {

      // Instantiate the reader:
      reader = XMLReaderFactory.createXMLReader(READER);

      // This class does all handling:
      reader.setContentHandler( this );
      reader.setErrorHandler( this );
   }

   public void readPlay( String playPath ) throws SAXException,IOException
   {
   reader.parse( playPath );
   }

    /** Called at the beginning of parsing **/
    public void startDocument() {
        // we initialize our string buffer, and reset the play title
        // and the personae at the beginning of parsing
        accumulator = new StringBuffer();
        playTitle = null;
        personae = new Vector();
    }

    /**
     * This method is called when the parser encounters textual data.
     * Since the parser may call this method multiple times (without
     * intervening elements), we accumulate the characters in a string buffer.
     */
    public void characters(char[] buffer, int start, int length) {
        accumulator.append(buffer, start, length);
    }

    /**
     * When beginning to parse a new element, we erase all data from
     * the string buffer.
     */
    public void startElement(String namespaceURI, String localName,
                             String fullName, Attributes attributes) {
        accumulator.setLength(0);
    }

    /**
     * We take special action when we reach the end of selected elements.
     */
    public void endElement(String namespaceURI, String localName, String fullName) {

        if ( localName.equals("TITLE") && playTitle == null ) {
         // Store the name of the play being parsed
            playTitle = accumulator.toString().trim();
        } else if ( localName.equals("PERSONA") ) {
         // Store the persona
            personae.add( accumulator.toString().trim() );
        } else if (localName.equals("PERSONAE")) {
         // We've reached the end of the PERSONAE element,
         // so we abort the parsing of this play
         try {
            reader = XMLReaderFactory.createXMLReader(READER);
         } catch ( SAXException e ) {}
         }
    }

   /**
   * Returns the personae of the play last parsed.
   */
   public String[] getPersonae() {
      String[] temp = (String[])personae.toArray(new String[0]);
      return temp;
   }

   /**
   * Returns the title of the play last parsed.
    */
   public String getPlayTitle() {
      return playTitle;
   }

   /**
   * The main can be used for command line testing.
   */
   public static void main( String[] args ) throws IOException, SAXException {

      if ( args.length == 0 ) {
         System.out.println("Usage: java PlayReader <playFile> [<playFile>...]");
         System.exit(0);
      }
      
      PlayReader pr = new PlayReader();

      // extract the personae:
      String[] playPersonae;
      try {
         for ( int i = 0; i < args.length; i++ ) {
            pr.readPlay( args[i] );
            System.out.println("**************************");
            System.out.println("Personae from \"" + pr.getPlayTitle() + 
                               "\":\n");
            playPersonae = pr.getPersonae();
            for ( int j = 0; j < playPersonae.length; j++ ) {
               System.out.println( playPersonae[j] );
            }
            System.out.println("**************************");
         }
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

