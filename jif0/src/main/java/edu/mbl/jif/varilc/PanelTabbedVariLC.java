package edu.mbl.jif.varilc;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import edu.mbl.jif.utils.color.JifColor;
import java.awt.Dimension;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PanelTabbedVariLC
      extends JTabbedPane
{
   BorderLayout borderLayout1 = new BorderLayout();


   PanelConfigVariLC config = new PanelConfigVariLC();

   public PanelTabbedVariLC () {
      try {
         jbInit();
      }
      catch (Exception exception) {
         exception.printStackTrace();
      }
   }


   private void jbInit () throws Exception {
      this.setLayout(borderLayout1);
      this.setTabPlacement(JTabbedPane.BOTTOM);
      this.setBackground(JifColor.yellow[2]);
      this.setFont(new java.awt.Font("Dialog", 1, 12));
      this.setMinimumSize(new Dimension(100, 100));
      this.setPreferredSize(new Dimension(100, 100));

      //config = new PanelConfigVariLC();


      //this.add(panelpaths, "paths");
      //this.add(tab_Elements, "Settings");
      this.add(config, "Configuration");
      //this.add(tab_Command, "Command");
      //this.setSelectedComponent(tab_Elements);

   }

}
