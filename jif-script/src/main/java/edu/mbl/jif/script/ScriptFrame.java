package edu.mbl.jif.script;

// import edu.mbl.jif.camera.camacq.CamAcq;
import edu.mbl.jif.script.jython.JythonConsole;
import edu.mbl.jif.script.jython.ScriptRunner;
import edu.mbl.jif.utils.FileUtil;
import edu.mbl.jif.utils.prefs.Prefs;
import edu.mbl.jif.utils.props.PropertiesViewer;


import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
//import edu.mbl.jif.camera.*;
//import edu.mbl.jif.camera.PropertiesViewer;
//import edu.mbl.jif.gui.*;
//import edu.mbl.jif.script.jython.*;

import java.awt.Rectangle;
import java.awt.Dimension;
//import org.python.core.util.FileUtil;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ScriptFrame
      extends JFrame
{
   JPanel contentPane;
   BorderLayout borderLayout1 = new BorderLayout();
   Icon icon1;
   JButton buttonNew = new JButton();
   JButton buttonEdit = new JButton();
   JButton buttonDelete = new JButton();
   private DefaultListModel listModel;
   String scriptName = "";
   String scriptToExec = null;
   JButton buttonScriptExec = new JButton();
   JButton buttonScriptSelect = new JButton();
   JButton buttonScripts = new JButton();
   JButton buttonBsh = new JButton();
   JButton buttonJython = new JButton();
   JLabel labelScript = new JLabel();
   JLabel labelScriptName = new JLabel();
   JPanel panelScript = new JPanel();
   JPanel panelScriptExec = new JPanel();
   JPanel panelScriptManage = new JPanel();

   JPanel panelBeanShell = new JPanel();
   JButton buttonJinSitu = new JButton();
   JButton buttonJconsole = new JButton();
   JButton buttonTestInterp = new JButton();

   public ScriptFrame () {
      try {
         jbInit();
      }
      catch (Exception exception) {
         exception.printStackTrace();
      }
   }


   private void jbInit () throws Exception {
      contentPane = (JPanel)this.getContentPane();
      contentPane.setLayout(null);
      setSize(new Dimension(429, 277));
      contentPane.setBackground(new Color(157, 157, 223));
      this.setForeground(Color.black);

      contentPane.setLayout(borderLayout1);
      icon1 = buttonBsh.getDisabledSelectedIcon();
      buttonScriptExec.setBounds(new Rectangle(12, 6, 32, 32));
      buttonScriptExec.setFont(new java.awt.Font("Dialog", 0, 10));
      buttonScriptExec.setToolTipText("Execute Script");
      buttonScriptExec.setIcon(new ImageIcon(this.getClass().getResource(
            "icons/scriptRun.gif")));
      buttonScriptExec.setMargin(new Insets(0, 0, 0, 0));
      buttonScriptExec.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            buttonScriptExec_actionPerformed(e);
         }
      });
      labelScriptName.setFont(new java.awt.Font("Dialog", 0, 10));
      labelScriptName.setText("Script:");
      labelScriptName.setBounds(new Rectangle(50, 5, 45, 14));
      panelScriptExec.setBackground(new Color(228, 240, 194));
      panelScriptExec.setBorder(BorderFactory.createEtchedBorder());
      panelScriptExec.setBounds(new Rectangle(7, 10, 257, 46));
      panelScriptExec.setLayout(null);
      labelScript.setFont(new java.awt.Font("Dialog", Font.BOLD, 10));
      labelScript.setText(Prefs.usr.get("scriptToExec", ""));
      labelScript.setBounds(new Rectangle(50, 21, 177, 18));

      buttonJython.setMargin(new Insets(1, 1, 1, 1));
      buttonJython.setToolTipText("Jython Interpreter");
      buttonJython.setIcon(new ImageIcon(this.getClass().getResource(
            "icons/jython32.gif")));

      buttonJython.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            buttonJython_actionPerformed(e);
         }
      });

      buttonScripts.setBounds(new Rectangle(126, 65, 41, 32));
      buttonScripts.setToolTipText("Open Scripting Tools");
      buttonScripts.setIcon(new ImageIcon(this.getClass().getResource(
            "icons/scripts24.gif")));
      buttonScripts.setMargin(new Insets(1, 1, 1, 1));
      buttonScripts.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            buttonScripts_actionPerformed(e);
         }
      });
      buttonScriptSelect.setBounds(new Rectangle(273, 15, 38, 36));
      buttonScriptSelect.setPreferredSize(new Dimension(32, 32));
      buttonScriptSelect.setToolTipText("Select Script to be executed");
      buttonScriptSelect.setIcon(new ImageIcon(this.getClass().getResource(
            "icons/scriptSelect.gif")));
      buttonScriptSelect.setMargin(new Insets(0, 0, 0, 0));
      buttonScriptSelect.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            buttonScriptSelect_actionPerformed(e);
         }
      });
      panelScriptManage.setBorder(BorderFactory.createEtchedBorder());
      panelScriptManage.setBounds(new Rectangle(7, 104, 223, 51));
      panelBeanShell.setBorder(BorderFactory.createEtchedBorder());
      panelBeanShell.setBounds(new Rectangle(244, 104, 56, 52));
      panelBeanShell.setLayout(null);
      buttonBsh.setBounds(new Rectangle(13, 11, 32, 32));
      buttonBsh.setMaximumSize(new Dimension(32, 32));
      buttonBsh.setMinimumSize(new Dimension(32, 32));
      buttonBsh.setPreferredSize(new Dimension(32, 32));
      buttonBsh.setToolTipText("BeanShell");
      buttonBsh.setIcon(new ImageIcon(this.getClass().getResource(
            "icons/beanShell.gif")));
      buttonBsh.setMargin(new Insets(2, 2, 2, 4));
      buttonBsh.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            buttonBsh_actionPerformed(e);
         }
      });
      buttonJinSitu.setIcon(null);
      buttonJinSitu.setMargin(new Insets(2, 4, 2, 4));
      buttonJinSitu.setMinimumSize(new Dimension(32, 32));
      buttonJinSitu.setPreferredSize(new Dimension(32, 32));
      buttonJinSitu.setToolTipText("JinSitu");
      buttonJinSitu.setIcon(new ImageIcon(this.getClass().getResource(
            "icons/jinSitu.gif")));
      buttonJinSitu.addActionListener(new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            buttonJinSitu_actionPerformed(e);
         }
      });
      buttonJconsole.setMargin(new Insets(0, 0, 0, 0));
      buttonJconsole.setMaximumSize(new Dimension(32, 32));
      buttonJconsole.setMinimumSize(new Dimension(32, 32));
      buttonJconsole.setPreferredSize(new Dimension(32, 32));
      buttonJconsole.setToolTipText("Open Java Console");
      buttonJconsole.setIcon(new ImageIcon(this.getClass().getResource(
            "icons/jConsole.gif")));
      buttonTestInterp.setBounds(new Rectangle(184, 65, 56, 26));
      buttonTestInterp.setText("test");
      buttonTestInterp.addActionListener(new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            buttonTestInterp_actionPerformed(e);
         }
      });
      panelScriptExec.add(buttonScriptExec);
      panelScriptExec.add(labelScript, null);
      panelScriptExec.add(labelScriptName, null);
      panelScriptManage.add(buttonJython, null);
      panelScriptManage.add(buttonJinSitu);
      panelScriptManage.add(buttonJconsole);
      panelScript.add(panelBeanShell, null);
      panelBeanShell.add(buttonBsh, null);
      panelScript.add(buttonEdit, null);
      panelScript.add(buttonDelete, null);
      panelScript.add(buttonScripts);
      panelScript.add(panelScriptManage, null);
      panelScript.add(buttonScriptSelect);
      panelScript.add(panelScriptExec, null);

      buttonNew.setMargin(new Insets(0, 0, 0, 0));
      buttonNew.setBounds(new Rectangle(12, 64, 32, 32));
      buttonNew.setToolTipText("New script");
      buttonNew.setIcon(
            new ImageIcon(this.getClass().getResource(
                  "icons/scriptNew.gif")));
      buttonNew.setText("");
      buttonEdit.setText("");
      buttonEdit.setIcon(
            new ImageIcon(this.getClass().getResource(
                  "icons/scriptEdit.gif")));
      buttonEdit.setBounds(new Rectangle(51, 65, 32, 32));
      buttonEdit.setToolTipText("Edit script");
      buttonEdit.setMargin(new Insets(0, 0, 0, 0));
      buttonDelete.setMargin(new Insets(0, 0, 0, 0));
      buttonDelete.setBounds(new Rectangle(90, 65, 32, 32));
      buttonDelete.setToolTipText("Delete");
      buttonDelete.setIcon(
            new ImageIcon(this.getClass().getResource("icons/trash.gif")));
      buttonDelete.setText("");
      //
      listModel = new DefaultListModel();
      panelScript.add(buttonNew, null);
      panelScript.add(buttonTestInterp);
      panelScript.setLayout(null);
      panelScript.setLayout(null);
      panelScript.setOpaque(true);
      panelScript.setBackground(new Color(228, 240, 194));

      contentPane.add(panelScript, java.awt.BorderLayout.CENTER); }


//========================================================================
// Scripts...
//

   void buttonScripts_actionPerformed (ActionEvent e) {

   }


//======================================================================
//
   void buttonScriptSelect_actionPerformed (ActionEvent e) {
      String path = Prefs.usr.get("scriptPath", ".\\scripts") + "\\";
      System.out.println("scriptPath =  " + path);
     // scriptName = FileUtil.openFile(null, path);
      if (scriptName == null) {
         return;
      }
      System.out.println("scriptName = " + scriptName);
      Prefs.usr.put("scriptToExec", scriptName);
      labelScript.setText(Prefs.usr.get("scriptToExec", ""));
   }


   void buttonScriptExec_actionPerformed (ActionEvent e) {
      /** @todo Only create a new output window if this is a different script */
//      if (Prefs.usr.get("scriptToExec", "") != "") {
//         //final EmbeddedJython ej = new EmbeddedJython();
//         //new ScriptConsole(ej.interp, "Script Execution: " + scriptName);
//         //final String scriptFile = ".\\scripts\\" + scriptName;
//         final edu.mbl.jif.gui.swingthread.SwingWorker3 worker = new edu.mbl.jif.gui.swingthread.SwingWorker3()
//         {
//            public Object construct () {
//               // Put Script files in the .\scripts directory
//               Utils.status("Executing script: "
//                               + Prefs.usr.get("scriptToExec", ""));
//               new ScriptRunner(Prefs.usr.get("scriptToExec", ""));
//               // ej.runScript(scriptFile);
//               return null;
//            }
//
//
//            public void finished () {
//               if (Camera.display != null) {
//                  Camera.display.vPanel.clearMessage();
//                  Camera.setDisplayOn();
//               }
//            }
//         };
//         worker.start();
//      }
   }


//////////////////////////////////////////////////////////////////////
// getListOfScripts
//
   /*
     try{
     while ((s = in.readLine()) != null)

     if (s.equals(ch)) {
     bx = new JComboBox(c_item);
     c_data.addElement(c_item);

     bx.removeAllItems();
     c_item.removeAllElements();
     c_item.clear();

     }

     else
     {

     c_item.addElement(s);
     }
    */


   public boolean getScriptList () {
      File directory;
      String[] filename;
      directory = new File(".\\scripts");
//    if (directory.isDirectory() == false) {
//      if (directory.exists() == false) {
//        System.out.println("No such directory: " + DataAccess.pathSeries());
//      } else {
//        System.out.println("Not a directory: " + DataAccess.pathSeries());
//      }
//      return false;
//    } else {
//      filename = directory.list();
      filename = FileUtil.getFilesSorted(".\\scripts", null);
      listModel.removeAllElements();
      if (filename != null) {
         for (int i = 0; i < filename.length; i++) {
            if (!filename[i].equalsIgnoreCase("null")) {
               listModel.addElement(filename[i]);
            }
         }
      }
      return true;
   }


//========================================================================
//
   void buttonJython_actionPerformed (ActionEvent e) {
      //JythonEvaluator py = new JythonEvaluator("");
      (new JythonConsole()).setVisible(true);
   }


   public void buttonJinSitu_actionPerformed (ActionEvent e) {
      // Open JinSitu introspection window
      new ScriptRunner(".\\jinsitu\\testJinSitu.py");
   }


   public void buttonTestInterp_actionPerformed (ActionEvent e) {
      String script = "import JinSitu\nJinSitu.runshell()";
//      try {
//         //InterpreterDriverManager.executeScriptFile(_scriptFile);
//         //JPythonInterpreterDriver.executeScript(script);
//      }
//      catch (InterpreterDriver.InterpreterException ex) {
//         System.out.println(ex);
//         ex.printStackTrace();
//      }
   }


   void buttonBsh_actionPerformed (ActionEvent e) {
      BeanShellConsole bshConsole = new BeanShellConsole();
   }


//==================================================================
// Diagnostics
   void buttonViewProperties_actionPerformed (ActionEvent e) {
      JFrame frame = new JFrame("Properties Viewer Demo");
      PropertiesViewer pv = new PropertiesViewer();
      frame.getContentPane().add(pv);
      frame.pack();
      frame.setVisible(true);
   }


   void buttonPrefs_actionPerformed (ActionEvent e) {
      try {
         // TODO new de.jppietsch.prefedit.PrefEdit().main(null);
      }
      catch (Exception ex) {}
   }


   public static void main (String[] args) {
      ScriptFrame scriptFrame = new ScriptFrame();
      scriptFrame.setVisible(true);
   }

}
