package tests.sunSwingTable;

/********************************************************************
 * Copyright (c) 1999 The Bean Factory, LLC. 
 * All Rights Reserved.
 *
 * The Bean Factory, LLC. makes no representations or 
 * warranties about the suitability of the software, either express
 * or implied, including but not limited to the implied warranties of 
 * merchantableness, fitness for a particular purpose, or 
 * non-infringement. The Bean Factory, LLC. shall not be 
 * liable for any damages suffered by licensee as a result of using,
 * modifying or distributing this software or its derivatives.
 *
 *******************************************************************/

import com.sun.xml.tree.*;
import org.w3c.dom.*;

import java.io.*;
import java.util.*;
import java.net.*;

/********************************************************************
 <B>IoUtils</B> get Java object or save a Java object to 
 an XML file 

 @version      : 1.1
 @author       : Sun Koh
********************************************************************/
public class IoUtils {
//
// Data Members
//

    //xml file location - from user's input
    public static String fileLocation;


    //TABLE META DATA
    public static final String ROOT_ELEMENT_TAG = "PERSON";


//
// Methods
//
/**
 Default Constructor 

 @param
 */
    public IoUtils(){

    }


    /**
     Return AddressBook Table Model object
     
     @return    a AddressBook Object
     */
    public static AddressBook getAddressBook(String file){
        
        fileLocation = file;

        //instantiate AddressBook object
        AddressBook addressBook = new AddressBook();
        Person person;

        try {

            //convert comming file location into input stream
            InputStream is = new FileInputStream(file);

            //create xml document
            //use appropriate version of parser
            ///////////////////////////////////////////////////////
            //for parser version 1
            //Document doc = XmlDocumentBuilder.createXmlDocument(is);
            //for parser version 2
            Document doc = XmlDocumentBuilder.createXmlDocument(is, true);
            ///////////////////////////////////////////////////////

            //get the number of person
            int size = XmlUtils.getSize( doc , ROOT_ELEMENT_TAG );

            for ( int i = 0; i < size; i++ ) {
                //instanticate a Person object
                person = new Person();

                //get information about a person and set information
                Element row = XmlUtils.getElement( doc , ROOT_ELEMENT_TAG , i );
                person.setLastName(  XmlUtils.getValue( row , "LASTNAME" ) );
                person.setFirstName( XmlUtils.getValue( row , "FIRSTNAME" ) );
                person.setEmail(     XmlUtils.getValue( row , "EMAIL" ) );
                person.setCompany(   XmlUtils.getValue( row , "COMPANY" ) );

                //add a person to an address book
                addressBook.add(person);
            }//end for

        }
        catch ( Exception e ) {
            System.out.println( e );
            return addressBook;

        }

        return addressBook;

    }//end method

    /**
     Generating an xml file given a Java object
     
     @param     data     the collection of Person object
     */
    public static void saveAddressBook(java.util.List data){

        Person person;

        try {
            FileOutputStream fo = new FileOutputStream(fileLocation);
            PrintWriter pw = new PrintWriter(fo);

            //start writing XML file
            pw.println("<?xml version= '1.0'?>");
            pw.println("<!DOCTYPE ADDRESSBOOK [");
            pw.println("<!ELEMENT ADDRESSBOOK (PERSON)*>");
            pw.println("<!ELEMENT PERSON (LASTNAME, FIRSTNAME, COMPANY, EMAIL)>");
            pw.println("<!ELEMENT LASTNAME (#PCDATA)>");
            pw.println("<!ELEMENT FIRSTNAME (#PCDATA)>");
            pw.println("<!ELEMENT COMPANY (#PCDATA)>");
            pw.println("<!ELEMENT EMAIL (#PCDATA)>");
            pw.println("]>");
            pw.println("<ADDRESSBOOK>");
            pw.println("");


            int size = data.size();
            for ( int i = 0; i < size; i++ ) {

                //get a personal information and write it to a file
                person = (Person)data.get(i);

                pw.println("  <PERSON>");
                pw.println("    <LASTNAME>"+person.getLastName()+"</LASTNAME>");
                pw.println("    <FIRSTNAME>"+person.getFirstName()+"</FIRSTNAME>");
                pw.println("    <COMPANY>"+person.getCompany()+"</COMPANY>");
                pw.println("    <EMAIL>"+person.getEmail()+"</EMAIL>");
                pw.println("  </PERSON>");
                pw.println("");

            }//end for

            pw.println("</ADDRESSBOOK>");

            pw.flush();
            pw.close();

        }
        catch ( Exception e ) {
            System.out.println(e);
        }

    }


}//end of IoUtils class

