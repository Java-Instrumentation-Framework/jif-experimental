package edu.mbl.jif.gui.panel;

import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import java.beans.*;
import javax.swing.JFrame;

/**
 * <p>Title: </p> <p>Description: </p> 
 * <p>Copyright: Copyright (c) 2003</p> <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PanelEnclosed
		extends JPanel {

//    public PanelEnclosed (IFrame _container)
//    {
//        this();
//        container = _container;
//    }
	public PanelEnclosed() {
	}

	public void closeParent() {
		if (this.getParent() instanceof JInternalFrame) {
			/* Even in this case, setClosed(true) will work.
			 * JInternalFrame jif = new JInternalFrame("JIF",
			 false, //resizable
			 false, //closable
			 false, //maximizable
			 false); //iconifiable
			 */
			try {
				((JInternalFrame) this.getParent()).setClosed(true);
				// or //dispatchEvent(new InternalFrameEvent(InternalFrameEvent. ));
				System.out.println("Parent Closed");
			} catch (PropertyVetoException ex) {
			}
		} else if (this.getParent() instanceof JFrame) {
			((JFrame) this.getParent()).dispatchEvent(
					new WindowEvent(((JFrame) this.getParent()), WindowEvent.WINDOW_CLOSING));
			System.out.println("Parent Closed");
		}
		
	}

	public void parentClosed() {
	}

}
