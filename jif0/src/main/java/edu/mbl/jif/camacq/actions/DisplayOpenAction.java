package edu.mbl.jif.camacq.actions;

import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.display.DisplayModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author GBH
 */
public class DisplayOpenAction
    extends AbstractAction {

    public DisplayOpenAction() {
        putValue(NAME, "Monitor");
        putValue(Action.ACTION_COMMAND_KEY, "openLiveDisplay");
        putValue(SMALL_ICON,
            new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/liveDisplay.gif")));
        putValue(SHORT_DESCRIPTION, "Open Live Display");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
    }

    public void actionPerformed(ActionEvent ae) {
         ((DisplayModel)(((InstrumentController) CamAcqJ.getInstance().getController()).getModel("display"))).displayLiveOpen();
    }
//    public DisplayOpenAction(final InstrumentController ctrl) {
//        this.ctrl = ctrl;
//        putValue(NAME, "Monitor");
//        putValue(Action.ACTION_COMMAND_KEY, "openLiveDisplay");
//        putValue(SMALL_ICON,
//            new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/liveDisplay.gif")));
//        putValue(SHORT_DESCRIPTION, "Open Live Display");
//        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
//    }

//    public void actionPerformed(ActionEvent ae) {
//        if (((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive() != null) {
//            System.out.println("Display already open.");
//            return;
//        }
//        //if(!((CameraModel) ctrl.getModel("camera")).isOpen()) {return;}
//        System.out.println("Opening Live Display...");
//
//        Runnable runner = new Runnable() {
//
//            public void run() {
//                DisplayLiveCamera disp =
//                    new DisplayLiveCamera((StreamGenerator) ((CameraModel) ctrl.getModel("camera")).getCamera());
//                //new StreamMonitorFPS((CameraModel) ctrl.getModel("camera"), disp.getStreamSource()); // for fps updates
//                try {
//                    // for fps updates
//                    Thread.currentThread().sleep(100);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(DisplayOpenAction.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                if (disp != null) {
//                    ((InstrumentController) CamAcqJ.getInstance().getController()).setDisplayLive(disp);
//                    disp.resume();
//                }
//            }
//
//        };
//        if (!SwingUtilities.isEventDispatchThread()) {
//            SwingUtilities.invokeLater(runner);
//        } else {
//            runner.run();
//        }
//    }

}
