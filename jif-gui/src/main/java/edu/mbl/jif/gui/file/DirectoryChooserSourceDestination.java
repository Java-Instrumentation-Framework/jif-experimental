package edu.mbl.jif.gui.file;

/**
 * $ $ License.
 *
 * Copyright $ L2FProd.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import java.io.File;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import edu.mbl.jif.gui.util.StaticSwingUtils;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * A simple example showing how to use the JDirectoryChooser.
 */
public class DirectoryChooserSourceDestination
        extends JPanel {
   
      final JTextField sourceText = new JTextField("");
   final JTextField destText = new JTextField("");

   public JTextField getSourceText() {
      return sourceText;
   }

   public JTextField getDestText() {
      return destText;
   }

   public DirectoryChooserSourceDestination() {
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      JPanel sourcePanel = new JPanel();
      sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.X_AXIS));
      JLabel sourceLabel = new JLabel("       Source: ");
      
      final JButton sourceButton = new JButton("...");
      sourceButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //selectDirectory(button, "C:\\ImageJ\\", "Select a Dir", "Dir set to:");
            StaticSwingUtils.dispatchToEDT(new Runnable() {
               public void run() {
                  String selected = selectDirectory(sourceButton,  sourceText.getText(), "Select source",null);
                  if (selected != null) {
                     sourceText.setText(selected);
                  }
               }
            });
         }
      });
      sourcePanel.add(sourceLabel);
      sourcePanel.add(sourceText);
      sourcePanel.add(sourceButton);
      this.add(sourcePanel);
      JPanel destPanel = new JPanel();
      destPanel.setLayout(new BoxLayout(destPanel, BoxLayout.X_AXIS));
      JLabel destLabel = new JLabel("Destination: ");
      
      final JButton destButton = new JButton("...");
      destButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //selectDirectory(button, "C:\\ImageJ\\", "Select a Dir", "Dir set to:");
            StaticSwingUtils.dispatchToEDT(new Runnable() {
               public void run() {
                  String selected = selectDirectory(destButton, destText.getText(), "Select dest", null);
                  if (selected != null) {
                     destText.setText(selected);
                  }
               }
            });
         }
      });
      destPanel.add(destLabel);
      destPanel.add(destText);
      destPanel.add(destButton);
      //this.setMaximumSize(new Dimension(600, 54));
      this.setPreferredSize(new Dimension(600, 54));
      this.add(destPanel);

   }

   public static String selectDirectory(Component parent, String startFile,
           String message, String confirmMsg) {

      JFileChooser j = new JFileChooser();
      j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      j.setMultiSelectionEnabled(false); // <<<<<<<<
         if (startFile != null) {
         File selFile = new File(startFile);
         if (selFile.exists()) {
            j.ensureFileIsVisible(selFile);
            j.setSelectedFile(selFile);
         }
      }
      //chooser.setShowingCreateDirectory(true);
         //
      Integer status = j.showSaveDialog(parent);
      //
      if (status == JFileChooser.APPROVE_OPTION) {
         File selectedFile = j.getSelectedFile();
         return selectedFile.getAbsolutePath();
      } else if (status == JFileChooser.CANCEL_OPTION) {
         return null;
      }
      return null;
   }

   public static void main(String[] args)
           throws Exception {
            QuickFrame f = new QuickFrame("");
            
            f.add(new DirectoryChooserSourceDestination());
            f.pack();
            f.setVisible(true);
   }
   
  public static class QuickFrame extends JFrame {

    public QuickFrame(String title) {
      super(title);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(640, 480);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(
              Math.max(0, screenSize.width / 2 - getWidth() / 2),
              Math.max(0, screenSize.height / 2 - getHeight() / 2));
    }
  }


   }
//    public static void main(String[] args)
//    {
//        JFileChooser f = new JFileChooser();
//        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
//        f.showSaveDialog(null);
//
//        System.out.println(f.getCurrentDirectory());
//        System.out.println(f.getSelectedFile());
//    }      
