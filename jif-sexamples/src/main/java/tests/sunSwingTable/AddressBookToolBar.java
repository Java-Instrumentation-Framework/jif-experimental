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
import javax.swing.table.*;         //JTable models


/********************************************************************
 <B>AddresBookPanel</B> create a custome table model object and add
 it to JTable.

 @version      : 1.1
 @author       : Sun Koh

********************************************************************/


public class AddressBookToolBar extends JToolBar {

    //
    // DATA MEMBERS
    //
    protected JButton btnOpen;
    protected JButton btnSave;
    protected JButton btnClose;

    protected AddressBookFrame frame;
   
    //
    // METHODS
    //
    /**
     Constructor - create JTable and a custome table model
     */
    public AddressBookToolBar(AddressBookFrame f){

        frame =f;

        btnOpen = new JButton ("   Open File   ");
        btnSave = new JButton ("   Save File   ");
        btnClose = new JButton("  Close Frame  ");

        //register listener
        btnOpen.addActionListener(new OpenAction());
        btnSave.addActionListener(new SaveAction());
        btnClose.addActionListener(new CloseAction());

        add(btnOpen);
        add(btnSave);
        add(btnClose);

    }   //end method

    /**
     Open action inner class
     */
    protected class OpenAction implements ActionListener{
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            frame.showFileOpen();
        }
    }

    /**
     Save action inner class
     */
    protected class SaveAction implements ActionListener{
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            frame.abp.saveFile();
        }
    }

    /**
     Close action inner class
     */
    protected class CloseAction implements ActionListener{
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            frame.closeFrame();
        }
    }



}//end class
