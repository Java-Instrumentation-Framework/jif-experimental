/*
 * IDMAction.java
 */

package edu.mbl.jif.camacq.actions;

import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.data.DataModel;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author GBH
 */
public  class IDMAction extends AbstractAction {
    public IDMAction() {
        putValue(NAME, "openIDM");
        putValue(Action.ACTION_COMMAND_KEY, "openIDM");
        putValue(SMALL_ICON,
                new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/idx24.gif")));
        putValue(SHORT_DESCRIPTION, "Open ImageDataManager");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
    }
    public void actionPerformed(ActionEvent ae) {
        Application.getInstance().statusBarMessage("Opening IDM...");
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
               
        // TODO 
//                com.jiexplorer.JILoader.main(new String[]{});
                InstrumentController instCtrl = ((CamAcqJ)CamAcqJ.getInstance()).getInstrumentController();
//                String path = ((DataModel)instCtrl.getModel("data")).getImageDirectory();
//                //idx.run(path);
//                Application.getInstance().statusBarMessage("");
//            }
//        });
        
    }
}


