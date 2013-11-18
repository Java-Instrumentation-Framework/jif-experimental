package freixas.jwizard;

import java.io.File;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class simulates an installation Wizard using the FLib JWizard component.
 * <hr>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * Artistic License. You should have received a copy of the Artistic License along with this
 * program. If not, a copy is available at
 * <a href="http://opensource.org/licenses/artistic-license.php">
 * opensource.org</a>.
 *
 * @author Antonio Freixas
 */
// Copyright  2004 Antonio Freixas
// All Rights Reserved.
class Example1
        extends JWizardDialog {

//**********************************************************************
// Public Constants
//**********************************************************************
//**********************************************************************
// Private Constants
//**********************************************************************
   static final String LICENSE =
           "Artistic License\n"
           + "\n"
           + "Preamble\n"
           + "\n"
           + "The intent of this document is to state the conditions under which a\n"
           + "Package may be copied, such that the Copyright Holder maintains some\n"
           + "semblance of artistic control over the development of the package,\n"
           + "while giving the users of the package the right to use and distribute\n"
           + "the Package in a more-or-less customary fashion, plus the right to\n"
           + "make reasonable modifications.\n"
           + "\n"
           + "Definitions:\n"
           + "\n"
           + "    * \"Package\" refers to the collection of files distributed by the\n"
           + "      Copyright Holder, and derivatives of that collection of files\n"
           + "      created through textual modification.\n"
           + "\n"
           + "    * \"Standard Version\" refers to such a Package if it has not been\n"
           + "      modified, or has been modified in accordance with the wishes of\n"
           + "      the Copyright Holder.\n"
           + "\n"
           + "    * \"Copyright Holder\" is whoever is named in the copyright or\n"
           + "      copyrights for the package.\n"
           + "\n"
           + "    * \"You\" is you, if you're thinking about copying or distributing\n"
           + "      this Package.\n"
           + "\n"
           + "    * \"Reasonable copying fee\" is whatever you can justify on the\n"
           + "      basis of media cost, duplication charges, time of people\n"
           + "      involved, and so on. (You will not be required to justify it to\n"
           + "      the Copyright Holder, but only to the computing community at\n"
           + "      large as a market that must bear the fee.)\n"
           + "\n"
           + "    * \"Freely Available\" means that no fee is charged for the item\n"
           + "      itself, though there may be fees involved in handling the item.\n"
           + "      It also means that recipients of the item may redistribute it\n"
           + "      under the same conditions they received it.\n"
           + "\n"
           + "1. You may make and give away verbatim copies of the source form of\n"
           + "   the Standard Version of this Package without restriction, provided\n"
           + "   that you duplicate all of the original copyright notices and\n"
           + "   associated disclaimers.\n"
           + "\n"
           + "2. You may apply bug fixes, portability fixes and other modifications\n"
           + "   derived from the Public Domain or from the Copyright Holder. A\n"
           + "   Package modified in such a way shall still be considered the\n"
           + "   Standard Version.\n"
           + "\n"
           + "3. You may otherwise modify your copy of this Package in any way,\n"
           + "   provided that you insert a prominent notice in each changed file\n"
           + "   stating how and when you changed that file, and provided that you\n"
           + "   do at least ONE of the following:\n"
           + "\n"
           + "    a) place your modifications in the Public Domain or otherwise make\n"
           + "       them Freely Available, such as by posting said modifications to\n"
           + "       Usenet or an equivalent medium, or placing the modifications on\n"
           + "       a major archive site such as ftp.uu.net, or by allowing the\n"
           + "       Copyright Holder to include your modifications in the Standard\n"
           + "       Version of the Package.\n"
           + "\n"
           + "    b) use the modified Package only within your corporation or\n"
           + "       organization.\n"
           + "\n"
           + "    c) rename any non-standard executables so the names do not\n"
           + "       conflict with standard executables, which must also be\n"
           + "       provided, and provide a separate manual page for each\n"
           + "       non-standard executable that clearly documents how it differs\n"
           + "       from the Standard Version.\n"
           + "\n"
           + "    d) make other distribution arrangements with the Copyright Holder.\n"
           + "\n"
           + "4. You may distribute the programs of this Package in object code or\n"
           + "   executable form, provided that you do at least ONE of the\n"
           + "   following:\n"
           + "\n"
           + "    a) distribute a Standard Version of the executables and library\n"
           + "       files, together with instructions (in the manual page or\n"
           + "       equivalent) on where to get the Standard Version.\n"
           + "\n"
           + "    b) accompany the distribution with the machine-readable source of\n"
           + "       the Package with your modifications.\n"
           + "\n"
           + "    c) accompany any non-standard executables with their corresponding\n"
           + "       Standard Version executables, giving the non-standard\n"
           + "       executables non-standard names, and clearly documenting the\n"
           + "       differences in manual pages (or equivalent), together with\n"
           + "       instructions on where to get the Standard Version.\n"
           + "\n"
           + "    d) make other distribution arrangements with the Copyright Holder.\n"
           + "\n"
           + "5. You may charge a reasonable copying fee for any distribution of\n"
           + "   this Package. You may charge any fee you choose for support of this\n"
           + "   Package. You may not charge a fee for this Package itself. However,\n"
           + "   you may distribute this Package in aggregate with other (possibly\n"
           + "   commercial) programs as part of a larger (possibly commercial)\n"
           + "   software distribution provided that you do not advertise this\n"
           + "   Package as a product of your own.\n"
           + "\n"
           + "6. The scripts and library files supplied as input to or produced as\n"
           + "   output from the programs of this Package do not automatically fall\n"
           + "   under the copyright of this Package, but belong to whomever\n"
           + "   generated them, and may be sold commercially, and may be aggregated\n"
           + "   with this Package.\n"
           + "\n"
           + "7. C or perl subroutines supplied by you and linked into this Package\n"
           + "   shall not be considered part of this Package.\n"
           + "\n"
           + "8. Aggregation of this Package with a commercial distribution is\n"
           + "   always permitted provided that the use of this Package is embedded;\n"
           + "   that is, when no overt attempt is made to make this Package's\n"
           + "   interfaces visible to the end user of the commercial distribution.\n"
           + "   Such use shall not be construed as a distribution of this Package.\n"
           + "\n"
           + "9. The name of the Copyright Holder may not be used to endorse or\n"
           + "   promote products derived from this software without specific prior\n"
           + "   written permission.\n"
           + "\n"
           + "10. THIS PACKAGE IS PROVIDED \"AS IS\" AND WITHOUT ANY EXPRESS OR\n"
           + "    IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED\n"
           + "    WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR\n"
           + "    PURPOSE.\n"
           + "\n"
           + "The End\n";
//**********************************************************************
// Private Members
//**********************************************************************
   private File installationFolder;
   private boolean standardInstall = true;
   private boolean installProgram = true;
   private boolean installDocumentation = true;
   private boolean installExamples = true;

//**********************************************************************
// main
//**********************************************************************
   public static void main(
           String[] args) {
      new Example1();
      System.exit(0);
   }

//**********************************************************************
// Constructors
//**********************************************************************
   /**
    * Create an instance of Example1. Note that this class is a subclass of JWizardDialog.
    */
   public Example1() {
      // We want the dialog modal -- when the dialog is finished, we
      // exit the program

      setModal(true);

      // Set the dialog title. This is the title for the wizard as a
      // whole

      setTitle("Install Example1");

      // Set the logo image

      URL url = getClass().getResource("images/Help16.gif");
      setWizardIcon(new ImageIcon(url));

      // Create each step

      addWizardPanel(new Step0());
      addWizardPanel(new Step1());
      addWizardPanel(new Step2());
      addWizardPanel(new Step3());
      addWizardPanel(new Step4());
      addWizardPanel(new Step5());
      addWizardPanel(new Step6());

      // We don't want to have the cancel button enabled when we're done

      disableCancelAtEnd();

      // Make the dialog visible

      pack();
      setVisible(true);
   }

//**********************************************************************
// Protected
//**********************************************************************
   /**
    * If the user presses cancel, we want to give him/her the option of continuing with the
    * installation.
    */
   protected void cancel() {
      int response =
              JOptionPane.showConfirmDialog(
              this,
              "Cancel the installation of Example1?",
              "Cancel Installation",
              JOptionPane.OK_CANCEL_OPTION);

      if (response == JOptionPane.OK_OPTION) {
         super.cancel();
      }
   }

//**********************************************************************
// Inner Classes
//**********************************************************************
//**********************************************************************
// Step0
//**********************************************************************
// This panel just introduces the Example1 wizard (which pretends that
// its going to install a package called Example1)
   private class Step0
           extends JWizardPanel {

      public Step0() {
         setStepTitle("Welcome to the Example1 Installation!");

         JPanel contentPane = getContentPane();
         contentPane.setLayout(new BorderLayout());

         JLabel label =
                 new JLabel(
                 "<html>"
                 + "The following steps will install the Example1 program.<br>"
                 + "Press <b>Next</b> to begin the installation.<br><br>"
                 + "<i>Note: This is an example of the JWizard component.<br>"
                 + "Nothing will be installed and no directories will be<br>"
                 + "created.</i>");

         contentPane.add(label, BorderLayout.NORTH);

         // Set the previous (none) and next steps

         setBackStep(-1);
         setNextStep(1);
      }
   }

//**********************************************************************
// Step1
//**********************************************************************
// This panel displays the installation license
   private class Step1
           extends JWizardPanel {

      public Step1() {
         setStepTitle("License Terms");

         JPanel contentPane = getContentPane();
         contentPane.setLayout(new BorderLayout());

         JTextArea text = new JTextArea(20, 40);
         text.setText(LICENSE);
         JScrollPane scroll = new JScrollPane(text);

         JCheckBox checkbox = new JCheckBox("I agree.");
         checkbox.addItemListener(
                 new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
               if (e.getStateChange() == ItemEvent.SELECTED) {
                  setNextStep(2);
               } else {
                  setNextStep(-1);
               }
            }
         });

         contentPane.add(scroll);
         contentPane.add(checkbox, BorderLayout.SOUTH);

         // Set the previous and next steps. Note that we don't have a next
         // step until the user agrees to the license terms

         setBackStep(0);
         setNextStep(-1);
      }
   }

//**********************************************************************
// Step2
//**********************************************************************
// Select the installation location
   private class Step2
           extends JWizardPanel {

      private JTextField field;

      public Step2() {
         setStepTitle("Select Installation Location");

         JPanel contentPane = getContentPane();
         contentPane.setLayout(new BorderLayout());

         // Field to enter install location

         field =
                 new JTextField(
                 "C:\\Program Files\\Example1");

         // Browse button for browsing to install location

         final JButton browse = new JButton("Browse");
         browse.addActionListener(
                 new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               File defaultDir = new File("C:\\Program Files");
               JFileChooser chooser = new JFileChooser(defaultDir);
               chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
               int result = chooser.showDialog(browse, "Select");
               if (result == JFileChooser.APPROVE_OPTION) {
                  field.setText(chooser.getSelectedFile().getAbsolutePath());
               }
            }
         });

         JPanel filePanel = new JPanel(new BorderLayout());
         filePanel.add(field);
         filePanel.add(browse, BorderLayout.EAST);

         contentPane.add(filePanel, BorderLayout.NORTH);

         // Set the previous and next steps

         setBackStep(1);
         setNextStep(3);
      }

// We're going to override the next button so we can check if the
// specified directory exists. If it does, we go on to the next step.
// If not, we ask whether we should create it. If the answer is No or
// if we can't create it, we remain on the current step
      protected void next() {
         String folderName = field.getText().trim();
         installationFolder = new File(folderName);

         // If valid, go on to next step

         if (installationFolder.isDirectory()) {
            super.next();
            return;
         }

         // If it exists, check that it is a folder and not a file

         if (installationFolder.exists()) {
            JOptionPane.showMessageDialog(
                    Step2.this,
                    "The given path points to a file, not a folder!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
         }

         // If nothing was entered, report an error

         if (folderName.length() < 1) {
            JOptionPane.showMessageDialog(
                    Step2.this,
                    "Please enter a folder name.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
         }


         // If it doesn't exist, query whether we should create it

         int response =
                 JOptionPane.showConfirmDialog(
                 Step2.this,
                 installationFolder.getName() + " does not exist. Create?",
                 "Create Folder",
                 JOptionPane.OK_CANCEL_OPTION);

         if (response == JOptionPane.OK_OPTION) {
// This is what we'd do if we were really creating the folder
// 	try {
// 	    installationFolder.mkdirs();
// 	}

// 	// Unsuccessful creation, report error

// 	catch (Exception e) {
// 	    JOptionPane.showMessageDialog(
// 		Step2.this,
// 		e.getMessage(),
// 		"Error",
// 		JOptionPane.ERROR_MESSAGE);
//	    return;
// 	}

            // Successful creation, continue to next step

            super.next();
            return;
         }
      }
   }

//**********************************************************************
// Step3
//**********************************************************************
// Select the installation type: standard or custom
   private class Step3
           extends JWizardPanel {

      public Step3() {
         setStepTitle("Select Installation Type");

         JPanel contentPane = getContentPane();
         contentPane.setLayout(new BorderLayout());

         JRadioButton standard = new JRadioButton("Standard (Recommended)");
         standard.setSelected(standardInstall);
         standard.addItemListener(
                 new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
               if (e.getStateChange() == ItemEvent.SELECTED) {
                  standardInstall = true;
               }
            }
         });
         JRadioButton custom = new JRadioButton("Custom");
         custom.setSelected(!standardInstall);
         custom.addItemListener(
                 new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
               if (e.getStateChange() == ItemEvent.SELECTED) {
                  standardInstall = false;
               }
            }
         });

         ButtonGroup group = new ButtonGroup();
         group.add(standard);
         group.add(custom);

         JPanel radioBox = new JPanel(new GridLayout(2, 1));
         radioBox.add(standard);
         radioBox.add(custom);

         contentPane.add(radioBox, BorderLayout.NORTH);

         // Set the previous and next steps. The next step will change if
         // the user selects "Custom" installation

         setBackStep(2);
         setNextStep(5);
      }

// Override the next() method so we can decide whether to go to step 4
// (custom installation) or 5 (standard installation)
      protected void next() {
         // If we have a custom installation, go to step 4, rather than 5

         if (!standardInstall) {
            setNextStep(4);
         }
         super.next();
      }
   }

//**********************************************************************
// Step4
//**********************************************************************
// If the user wanted a custom installation, select the components to
// install
   private class Step4
           extends JWizardPanel {

      private JCheckBox program;
      private JCheckBox documentation;
      private JCheckBox examples;

      public Step4() {
         setStepTitle("Select Components to Install");

         JPanel contentPane = getContentPane();
         contentPane.setLayout(new BorderLayout());

         program = new JCheckBox("Install program (890K)");
         program.setSelected(installProgram);
         documentation = new JCheckBox("Install documentation (500K)");
         documentation.setSelected(installDocumentation);
         examples = new JCheckBox("Install examples (103K)");
         examples.setSelected(installExamples);

         JPanel choices = new JPanel(new GridLayout(3, 1));
         choices.add(program);
         choices.add(documentation);
         choices.add(examples);

         contentPane.add(choices, BorderLayout.NORTH);

         // Set the previous and next steps

         setBackStep(3);
         setNextStep(5);
      }

// We override the next() method just to determine what the user
// selected
      protected void next() {
         installProgram = program.isSelected();
         installDocumentation = documentation.isSelected();
         installExamples = examples.isSelected();
         super.next();
      }
   }

//**********************************************************************
// Step5
//**********************************************************************
// Summarize the installation -- the user can proceed or cancel.
   private class Step5
           extends JWizardPanel {

      private JLabel label;

      public Step5() {
         setStepTitle("Installation Summary");

         JPanel contentPane = getContentPane();
         contentPane.setLayout(new BorderLayout());

         label = new JLabel();
         contentPane.add(label, BorderLayout.NORTH);

         // Set the previous and next steps

         setBackStep(standardInstall ? 3 : 4);
         setNextStep(6);
      }

// We need to set the label text when the panel is displayed, not when
// the panel is constructed
      protected void makingVisible() {
         label.setText(
                 "<html>"
                 + "<b>Installation Summary:</b><br><br>"
                 + "Installation folder: "
                 + installationFolder.getAbsolutePath()
                 + "<br>"
                 + (standardInstall
                 ? "Standard installation<br><br>"
                 : "Custom installation<ul>"
                 + (installProgram ? "<li>Install program</li>" : "")
                 + (installDocumentation ? "<li>Install documentation</li>" : "")
                 + (installExamples ? "<li>Install examples</li>" : "")
                 + "</ul>")
                 + "Press <b>Next</b> to start installing.");
      }
   }

//**********************************************************************
// Step6
//**********************************************************************
// We're done installing
   private class Step6
           extends JWizardPanel {

      public Step6() {
         setStepTitle("Installation Complete!");

         JPanel contentPane = getContentPane();
         contentPane.setLayout(new BorderLayout());

         JLabel label =
                 new JLabel(
                 "The Example1 installation is complete.");

         contentPane.add(label, BorderLayout.NORTH);

         // There's nothing more to do and there's no point going back a
         // step either

         setBackStep(-1);
         setNextStep(-1);
      }
   }
//**********************************************************************
// End Inner Classes
//**********************************************************************
}
