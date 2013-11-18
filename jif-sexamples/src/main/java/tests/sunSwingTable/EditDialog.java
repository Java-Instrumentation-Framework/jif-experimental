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

import java.awt.*;                  //AWT classes
import java.awt.event.*;            //AWT event classes
import javax.swing.*;               //Swing classes
import javax.swing.event.*;         //Swing events

 
 /********************************************************************
  <B>EditDialog</B> edits person information 
 
  @version      : 1.1
  @author       : Sun Koh
 
 ********************************************************************/
 public class EditDialog extends JDialog{
 //
 // Data Members
 //
 
    protected JLabel lLastName;
    protected JLabel lFirstName;
    protected JLabel lCompany;
    protected JLabel lEmail;

    protected JTextField tfLastName;
    protected JTextField tfFirstName;
    protected JTextField tfCompany;
    protected JTextField tfEmail;

    protected JButton btnSet;
    protected JButton btnCancel;

    protected AddressBookPanel panel;
    protected String command;


 //
 // Methods
 //
 /**
  Default Constructor 
 
  @param
  */
    public EditDialog(AddressBookPanel ap, String lastName, String firstName,
                      String company, String email, String command){

        panel = ap;
        this.command= command;
        this.setModal(true);

        Container c = this.getContentPane();

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(5,2));

        lLastName  = new JLabel("Last Name");
        lFirstName = new JLabel("First Name");
        lCompany   = new JLabel("Company");
        lEmail     = new JLabel("Email");

        tfLastName  = new JTextField(lastName);
        tfFirstName = new JTextField(firstName);
        tfCompany   = new JTextField(company);
        tfEmail     = new JTextField(email);

        if(command.equals("Add")){
            btnSet = new JButton("Add");
        }else btnSet = new JButton("Set");
        
        btnCancel = new JButton("Cancel");

        //register listener
        btnSet.addActionListener(new SetAction(this));
        btnCancel.addActionListener(new CancelAction(this));

        p.add(lLastName);
        p.add(tfLastName);
        p.add(lFirstName);
        p.add(tfFirstName);
        p.add(lCompany);
        p.add(tfCompany);
        p.add(lEmail);
        p.add(tfEmail);
        p.add(btnSet);
        p.add(btnCancel);

        c.add(p);
        pack();
        setLocationRelativeTo(ap);


    }

    /**
     Return a last name
     
     @return    lastName    a last name
     */
    public String getLastName(){
        return tfLastName.getText();
    }

    /**
     Return a first name
     
     @return    firstName    a first name
     */
    public String getFirstName(){
        return tfFirstName.getText();
    }
    
    /**
     Return a company name
     
     @return    company    a company name
     */
    public String getCompany(){
        return tfCompany.getText();
    }

    /**
     Return an email address
     
     @return    email    an email address
     */
    public String getEmail(){
        return tfEmail.getText();
    }

    /**
     Preferred size
     */
    public Dimension getPreferredSize(){
        return new Dimension(400, 180);
    }
    
    /**
     Preferred size
     */
    public Dimension getMinimunSize(){
        return new Dimension(400, 180);
    }

    /**
     Set Action inner class
     */
    protected class SetAction implements ActionListener{
        
        EditDialog dialog;
        /**
         Default constructor
         */
        public SetAction(EditDialog d){
            dialog = d;
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {

            dialog.dispose();
            
            if(command.equals("Add")){
                dialog.panel.add();
            }
            else dialog.panel.edit();
        }
    }

    /**
     Cancel Action inner class
     */
    protected class CancelAction implements ActionListener{
        
        EditDialog dialog;
        /**
         Default constructor
         */
        public CancelAction(EditDialog d){
            dialog = d;
        }
        
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
        }
    }

}//end class

 

