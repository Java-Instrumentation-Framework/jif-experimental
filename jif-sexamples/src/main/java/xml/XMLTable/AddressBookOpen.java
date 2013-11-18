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
 <B>AddresBookPanel</B> gets an XML file location to display
 on JTable
 
 @version      : 1.1
 @author       : Sun Koh

********************************************************************/


public class AddressBookOpen extends JDialog {

    //
    // DATA MEMBERS
    //
    protected JLabel lFile;
    protected JTextField tfFile;
    protected JButton btnOpen;
    protected JButton btnCancel;

    protected AddressBookFrame frame;

    //
    // METHODS
    //
    /**
     Constructor - create JTable and a custome table model
     */
    public AddressBookOpen(AddressBookFrame f){

        frame = f;
        this.setModal(true);

        Container c = this.getContentPane();

        lFile = new JLabel("XML file location: ");
        tfFile = new JTextField();
        tfFile.setColumns(10);
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS ));
        p1.add(lFile);
        p1.add(tfFile);

        btnOpen = new JButton("Open");
        btnCancel = new JButton("Cancel");
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(1, 2));
        p2.add(btnOpen);
        p2.add(btnCancel);

        //register listener
        btnOpen.addActionListener(new OpenAction(this));
        btnCancel.addActionListener(new CancelAction(this));

        c.setLayout(new BorderLayout());
        c.add(p2, BorderLayout.SOUTH);
        c.add(p1, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(frame);
        show();

    }   //end method

    /**
     Return preferrred size of this panel
     
     @return    a dimension of the panel
     */
    public Dimension getPreferredSize(){
        return new Dimension(500, 80);
    }
    /**
     Return minimun size of this panel
     
     @return    a dimension of the panel
     */
    public Dimension getMinimunSize(){
        return new Dimension(200, 80);
    } 

    /**
     Open Action inner class
     */
    protected class OpenAction implements ActionListener{
        
        AddressBookOpen dialog;
        /**
         Constructor
         */
        public OpenAction(AddressBookOpen f){
            dialog = f;
        }
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
            frame.abp.openFile(tfFile.getText());
        }
    }

    /**
     Cancel Action inner class
     */
    protected class CancelAction implements ActionListener{
        
        AddressBookOpen dialog;
        /**
         Constructor
         */
        public CancelAction(AddressBookOpen f){
            dialog = f;
        }
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
        }
    }



}//end of AddressBookOpen class

