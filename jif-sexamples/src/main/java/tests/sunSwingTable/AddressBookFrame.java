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

/********************************************************************
 <B>AddressBookFrame</B> create a panel which includes a JTable 
 and the panel on the frame

 @version      : 1.1
 @author       : Sun Koh
 @see   AddressBookPanel 
********************************************************************/


public class AddressBookFrame extends JFrame{


//
// DATA MEMBER
//

    protected AddressBookPanel abp;
    protected AddressBookToolBar abt;



//
// METHODS
//


    /**
     main method
     */
    public static void main(String []args){
        new AddressBookFrame();
    }

    /**
     Constructor - create a panel and add it on the frame
     */
    public AddressBookFrame(){
        setTitle( "XML and Java2 Tutorial Part 2: Sun Parser" );

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        abp = new AddressBookPanel(this);
        abt = new AddressBookToolBar(this);

        c.add(abp, BorderLayout.CENTER);
        c.add(abt, BorderLayout.NORTH);

        pack();
        show();
        this.addWindowListener( new WindowAction());

        }

    /**
     Show file dialog box
     */
    public void showFileOpen(){

        new AddressBookOpen(this);
    }


    /**
     Close and Exit the frame
    
     */
    public  void closeFrame(){

        System.exit(0);
        
    }
    

    /**
     Window events inner class
     */
    private class WindowAction extends WindowAdapter{

        /**
         Close the frame
         */
        public void windowClosing(WindowEvent e){
            System.exit(0);
        }

    }

}//end class
