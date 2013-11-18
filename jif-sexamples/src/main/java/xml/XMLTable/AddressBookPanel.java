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


import java.awt.*;                  //AWT classes
import java.awt.event.*;            //AWT event classes
import javax.swing.*;               //Swing classes
import javax.swing.event.*;         //Swing events
import javax.swing.table.*;         //JTable models


/********************************************************************
 <B>AddresBookPanel</B> create a custome table model object and add
 it to JTable.

 @version      : 1.1
 @author       : Sun Koh

 @see  AddressBookModel 
********************************************************************/


public class AddressBookPanel extends JPanel {

    //
    // DATA MEMBERS
    //
    protected JTable table;
    protected AddressBook model;

    protected JButton btnAdd;
    protected JButton btnEdit;
    protected JButton btnDelete;

    protected AddressBookFrame frame;
    protected EditDialog dialog;
    protected int row;           //selected row

    //
    // METHODS
    //
    /**
     Constructor - create JTable and a custome table model
     */
    public AddressBookPanel(AddressBookFrame f){

        frame = f;

        //creatae JTable
        table = new JTable();

        //create control panel
        JPanel p = new JPanel();
        btnAdd = new JButton("Add");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        //register to listener
        btnAdd.addActionListener(new AddAction(this));
        btnEdit.addActionListener(new EditAction(this));
        btnDelete.addActionListener(new DeleteAction(this));


        p.setLayout(new GridLayout(1, 3));
        p.add(btnAdd);
        p.add(btnEdit);
        p.add(btnDelete);

        setLayout(new BorderLayout());
        add(p, BorderLayout.SOUTH);
        add( new JScrollPane( table ), BorderLayout.CENTER);

    }   //end method

    /**
     This method is called after information is entered 
     in Dialog box
     
     */
    public void add(){
        
        Person person = new Person();
        person.setLastName(dialog.getLastName());
        person.setFirstName(dialog.getFirstName());
        person.setCompany(dialog.getCompany());
        person.setEmail(dialog.getEmail());

        model.add(person);

    }

    /**
     This method is called after information is entered 
     in Dialog box
     
     */
    public void edit(){
        
        Person person = new Person();
        person.setLastName(dialog.getLastName());
        person.setFirstName(dialog.getFirstName());
        person.setCompany(dialog.getCompany());
        person.setEmail(dialog.getEmail());

        model.set(row, person);


    }

    /**
     This method is called by RemoveAction class 
     
     */
    public void remove(){
        
        model.remove(row);

    }

    /**
     Return preferrred size of this panel
     
     @return    a dimension of the panel
     */
    public Dimension getPreferredSize(){
        return new Dimension(600, 300);
    }
    /**
     Return minimun size of this panel
     
     @return    a dimension of the panel
     */
    public Dimension getMinimunSize(){
        return new Dimension(200, 200);
    } 

    /**
     Get table model given file location and update table
     
     @param     file      file location
     */
    public void openFile(String file){

        //get Address book model given file location
        //get(create) table model for Address book
        model = IoUtils.getAddressBook(file);
        
        table.setModel(model);
        table.sizeColumnsToFit(true);

    }

    /**
     Save address book as a XML file
     */

    public void saveFile(){
        model.saveFile();
        
    }

    /**
     Add a row to a table
     */
    public class AddAction implements ActionListener {

        protected AddressBookPanel panel;

        public AddAction(AddressBookPanel p){
            panel = p;
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {

                dialog = new EditDialog(panel, "", "",
                                        "", "", "Add");
                dialog.show();
            }

    }//end AddAction

    /**
     Edit a row from a table
     */
    protected class EditAction implements ActionListener {

        protected AddressBookPanel panel;

        public EditAction(AddressBookPanel p){
            panel = p;
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {

            row = table.getSelectedRow();
            
            if (row < table.getRowCount() && row >= 0 ) {
                Person p = (Person)model.get(row);
                dialog = new EditDialog(panel, p.getLastName(), p.getFirstName(),
                                        p.getCompany(), p.getEmail(), "Edit");
                dialog.show();
            }

        }
    }

    /**
     Delete a row from a table
     */
    protected class DeleteAction implements ActionListener {

        protected AddressBookPanel panel;

        public DeleteAction(AddressBookPanel p){
            panel = p;
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            row = table.getSelectedRow();
            
            if (row < table.getRowCount() && row >= 0 ) {
                panel.remove();
            }

        }
    }

}//end of AddressBookPanel class

