package tests.gui;

import edu.mbl.jif.gui.dialog.DialogBoxI;
import edu.mbl.jif.gui.*;
import edu.mbl.jif.gui.file.DirectoryChooserPanel;
import edu.mbl.jif.gui.internal.InternalFrameModal;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.beans.PropertyVetoException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import edu.mbl.jif.utils.FileUtil;
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
public class TestInternalModal
{
   public TestInternalModal () {
   }


   public static void fileOpenerTree (String msg, String path) {
      JPanel panelFileChooser = new JPanel();
      panelFileChooser.setLayout(new BorderLayout());

      final DirectoryChooserPanel dlg = new DirectoryChooserPanel(path);
      panelFileChooser.add(dlg, BorderLayout.CENTER);

      JPanel choices = new JPanel(new FlowLayout());
      JButton openButton = new JButton("Open");
      choices.add(openButton);
      panelFileChooser.add(choices, BorderLayout.SOUTH);

      final InternalFrameModal frame = new InternalFrameModal("Open File",
            panelFileChooser, 20, 50);
      //
      openButton.addActionListener(new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            if (dlg.getSelectedFile() != null) {
               String ext = FileUtil.getFileExtension(dlg.getSelectedFile(), false);

               // System.out.println("dlg.getSelectedFile(): " + dlg.getSelectedFile());
               if (dlg.getSelectedFile().isFile() && (ext.equalsIgnoreCase("tif")
                     || ext.equalsIgnoreCase("tiff"))) {
                  //openSelectedFile(dlg.getSelectedFile());
                  try {
                     frame.setClosed(true);
                  }
                  catch (PropertyVetoException ex) {}
               } else {
                  try {
                     frame.setClosed(true);
                  }
                  catch (PropertyVetoException ex) {}
                  DialogBoxI.boxError("Sorry", "Cannot open this file type");
               }
               try {
                  frame.setClosed(true);
               }
               catch (PropertyVetoException ex) {}
            }
         }
      });
      frame.setSize(new Dimension(300, 600));
      frame.setModal();
   }


   public static void main (String[] args) {
      TestInternalModal.fileOpenerTree("Open me", ".");
   }
}
