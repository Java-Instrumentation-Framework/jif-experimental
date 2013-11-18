package xml.XMLTable;

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


import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;

/********************************************************************
 <B>AddressBookModel</B> implements TableModel.  Changes personal
 information. 


 @version      : 1.1
 @author       : Sun Koh

 @see   AddressBook
********************************************************************/

public class AddressBook implements TableModel {


    public static final String[] colNames ={
        "LASTNAME",
        "FIRSTNAME",
        "COMPANY",
        "EMAIL"
    };

    public static final Class[] colClasses ={
        String.class,
        String.class,
        String.class,
        String.class
    };

    public static final int 
    LASTNAME_COL    = 0,
    FIRSTNAME_COL   = 1,
    COMPANY_COL     = 2,
    EMAIL_COL       = 3;

    //used to hold a list of TableModelListeners
    protected java.util.List tableModelListeners = new ArrayList();        

    protected java.util.List data = new ArrayList();

    /**
     main method
     */
    public static void main(String []args){
        new AddressBook();
    }//end main

    /**
     Constructor - create a DOM
     */
    public AddressBook(){
    }

    //
    // TableModel implementation
    //
    
    /**
     Return the number of columns for the model.
    
     @return    the number of columns in the model
    */
    public int getColumnCount() {
        return colClasses.length;
    }
    /**
     Return the number of persons in an addressbook
     
     @return    the number or rows in the model
    */
    public int getRowCount() {
        return data.size();
    }

    /**
     Return data given its location
     
     @param	    r   the row whose value is to be looked up
     @param	    c 	the column whose value is to be looked up
     @return	the value Object at the specified cell
    */
    public Object getValueAt(int r, int c) {

        //must get row first
        Person person = (Person)data.get(r);
        //must get value for column in this row

        switch ( c ) {
        case LASTNAME_COL:
            return person.getLastName();
        case FIRSTNAME_COL:
            return person.getFirstName();
        case COMPANY_COL:
            return person.getCompany();
        case EMAIL_COL:
            return person.getEmail();
        }

        return null;

    }

    /**
     Return the name of column for the table.
     
     @param	    c   the index of column
     @return    the name of the column
    */
    public String getColumnName(int c) {
        return colNames[ c ];
    }
    /**
     Return column class
     
     @param      c the index of column
     @return    the common ancestor class of the object values in the model.
    */
    public Class getColumnClass(int c) {
        return colClasses[ c ];
    }

    /**
     Return false - table is not editable
     
     @param	    r	the row whose value is to be looked up
     @param	    c	the column whose value is to be looked up
     @return	true if the cell is editable.
    */
    public boolean isCellEditable(int r, int c) {
        return false;
    }

    /**
     This method is not implemented, because the table is not editable.
     
     @param	    value		 the new value
     @param	    r	 the row whose value is to be changed
     @param	    c 	 the column whose value is to be changed
    */
    public void setValueAt(Object value, int r, int c) {
    }

    /**
     Add a listener to the list that's notified each time a change
     to the data model occurs.
    
     @param	l		the TableModelListener
    */
    public void addTableModelListener(TableModelListener l) {
        //add a listener only if the listener is not already registered
        if ( !tableModelListeners.contains(l) ) {
            tableModelListeners.add(l);
        }
    }
    /**
     Remove a listener from the list that's notified each time a
     change to the data model occurs.
    
     @param	l		the TableModelListener
    */
    public void removeTableModelListener(TableModelListener l) {
        //remove a listener only if the listener is already registered
        if ( tableModelListeners.contains(l) ) {
            tableModelListeners.remove(l);
        }
    }

    
    
    //
    // Manipulation of AddressBook
    //

    /**
     Add a person to the address book
     
     @param     p   a Person object
     */
    public void add(Person p){
        data.add(p);

        //refresh table view
        fireContentsChanged();
    }

    
    /**
     Remove a person given a location of the person
     in the address book
     
     @param     i   a location of person
     */
    public void remove(int i){
        data.remove(i);

        //refresh table view
        fireContentsChanged();
    }

    /**
     Set a Person object given a location of the person
     in the address book
     
     @param     i   a location of person
     @param     p   a Person object
     */
    public void set(int i, Person p){
        
        data.set(i, p);
        
        //refresh table view
        fireContentsChanged();
           }

    /**
     Return a Person Object given an location
     of the person in the address book
     
     @param     i   a location of person
     @return    a Person object
     */
    public Person get(int i){
        return (Person)data.get(i);    
    }

    /**
     Return the number of person in the address book
     
     @return    an int number
     */
    public int size(){
        return data.size();
    }


    //
    // Utility CONTROLLER method
    //

    /**
     Save address book as a XML file
     */
    public void saveFile(){

        //save address book information to an xml file
        IoUtils.saveAddressBook(data); 

    }


    /**
     Underlying personal information has changed
     Create a TableModel event
     Notify all registerd listeners
     */
    public void fireContentsChanged()
    {
        TableModelListener ldl; //temp var

        TableModelEvent e = new TableModelEvent(this); 

        for(int i=0; i<tableModelListeners.size(); i++){
            ldl = (TableModelListener)tableModelListeners.get(i);
            ldl.tableChanged(e);
        }

    }//end method

    

}//end class
