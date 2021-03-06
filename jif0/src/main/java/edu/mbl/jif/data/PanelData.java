/*
 * PanelData.java
 *
 * Created on May 26, 2006, 3:42 PM
 */

package edu.mbl.jif.data;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.camacq.*;
import edu.mbl.jif.data.DataDirSelect;
import edu.mbl.jif.data.DataPresentation;
import edu.mbl.jif.data.DataModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ConverterFactory;
import java.io.IOException;
import java.text.NumberFormat;

/**
 *
 * @author  GBH
 */
public class PanelData extends BasePanelForm {
    InstrumentController ctrl;
    DataPresentation dataPresentation;
    
    /** Creates new form PanelData */
    public PanelData(InstrumentController ctrl) {
        this.ctrl = ctrl;
        
        this.dataPresentation = new DataPresentation((DataModel) ctrl.getModel("data"));
   
        initComponents();
//        panelDataDir.add(
//                new DataDirSelect(dataPresentation.getModel(DataModel.PROPERTYNAME_IMAGEDIRECTORY), this), 
//                BorderLayout.CENTER);
        checkTimeStamp.setSelected(!checkPrefix.isSelected());
       // comboFileFormat.setEnabled(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        panelImageJ = new javax.swing.JPanel();
        checkAutoSave =    BasicComponentFactory.createCheckBox(dataPresentation.getModel(DataModel.PROPERTYNAME_AUTOSAVE), "Auto-Save");
        checkIjSeqInStack = BasicComponentFactory.createCheckBox(dataPresentation.getModel(DataModel.PROPERTYNAME_SEQINSTACK), "Sequence in Stack");
        checkIjPutOnDesk = BasicComponentFactory.createCheckBox(dataPresentation.getModel(DataModel.PROPERTYNAME_IJPUTONDESK), "Create IJ window");
        comboFileFormat = BasicComponentFactory.createComboBox(new SelectionInList(DataModel.IMAGEFILEFORMAT_OPTIONS, dataPresentation.getModel(DataModel.PROPERTYNAME_IMAGEFILEFORMAT)));
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        dataDirSelect1 = new DataDirSelect(dataPresentation.getModel(DataModel.PROPERTYNAME_IMAGEDIRECTORY),this);
        jButton1 = new javax.swing.JButton();
        fieldPrefix = BasicComponentFactory.createTextField(dataPresentation.getModel(DataModel.PROPERTYNAME_FILEPREFIX));
        jLabel2 = new javax.swing.JLabel();
        buttonZeroSerial = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        valueSerialNumber =    BasicComponentFactory.createLabel (ConverterFactory.
            createStringConverter(dataPresentation.getModel(DataModel.PROPERTYNAME_FILECOUNTER),      NumberFormat.getIntegerInstance()));
        checkPrefix = BasicComponentFactory.createCheckBox(
            dataPresentation.getModel(DataModel.PROPERTYNAME_USEFILENAMEPREFIX),
            "Prefix+#");
        ;
        checkTimeStamp = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(224, 210, 228));
        setPreferredSize(new java.awt.Dimension(420, 150));

        jPanel1.setBackground(new java.awt.Color(185, 214, 200));
        jPanel1.setPreferredSize(new java.awt.Dimension(420, 150));

        panelImageJ.setBackground(new java.awt.Color(215, 198, 197));
        panelImageJ.setBorder(javax.swing.BorderFactory.createTitledBorder("Image Data"));
        panelImageJ.setOpaque(false);

        checkAutoSave.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        checkAutoSave.setText("Auto-Save");
        checkAutoSave.setAlignmentX(1.0F);
        checkAutoSave.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkAutoSave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        checkAutoSave.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        checkAutoSave.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkAutoSave.setOpaque(false);
        checkAutoSave.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                AutoSaveChanged(evt);
            }
        });

        checkIjSeqInStack.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        checkIjSeqInStack.setText("Sequence in Stack");
        checkIjSeqInStack.setAlignmentX(1.0F);
        checkIjSeqInStack.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkIjSeqInStack.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        checkIjSeqInStack.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        checkIjSeqInStack.setMargin(new java.awt.Insets(4, 4, 4, 4));
        checkIjSeqInStack.setOpaque(false);

        checkIjPutOnDesk.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        checkIjPutOnDesk.setAlignmentX(1.0F);
        checkIjPutOnDesk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkIjPutOnDesk.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        checkIjPutOnDesk.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        checkIjPutOnDesk.setLabel("Create IJ window");
        checkIjPutOnDesk.setOpaque(false);

        comboFileFormat.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        comboFileFormat.setAlignmentX(1.0F);
        comboFileFormat.setMaximumSize(new java.awt.Dimension(75, 20));
        comboFileFormat.setMinimumSize(new java.awt.Dimension(26, 16));
        comboFileFormat.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        jLabel3.setText("Image file format:");
        jLabel3.setAlignmentX(1.0F);

        org.jdesktop.layout.GroupLayout panelImageJLayout = new org.jdesktop.layout.GroupLayout(panelImageJ);
        panelImageJ.setLayout(panelImageJLayout);
        panelImageJLayout.setHorizontalGroup(
            panelImageJLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelImageJLayout.createSequentialGroup()
                .add(4, 4, 4)
                .add(panelImageJLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelImageJLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, checkIjPutOnDesk)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, checkIjSeqInStack)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, checkAutoSave))
                    .add(jLabel3))
                .add(8, 8, 8))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelImageJLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(comboFileFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelImageJLayout.setVerticalGroup(
            panelImageJLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelImageJLayout.createSequentialGroup()
                .add(checkAutoSave)
                .add(5, 5, 5)
                .add(checkIjSeqInStack)
                .add(5, 5, 5)
                .add(checkIjPutOnDesk)
                .add(5, 5, 5)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(comboFileFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Data"));
        jPanel2.setOpaque(false);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/explore.png"))); // NOI18N
        jButton1.setToolTipText("Open folder in explorer");
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenDir(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        jLabel2.setText("File Naming:");

        buttonZeroSerial.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        buttonZeroSerial.setText("Reset#");
        buttonZeroSerial.setToolTipText("Reset serial# to zero");
        buttonZeroSerial.setMargin(new java.awt.Insets(2, 2, 2, 2));
        buttonZeroSerial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSerialNumberToZero(evt);
            }
        });

        jLabel1.setText(" + ");

        valueSerialNumber.setText("-");

        buttonGroup1.add(checkPrefix);
        checkPrefix.setText("Prefix + #:");
        checkPrefix.setOpaque(false);

        buttonGroup1.add(checkTimeStamp);
        checkTimeStamp.setText("TimeStamp");
        checkTimeStamp.setOpaque(false);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(dataDirSelect1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .add(10, 10, 10)
                        .add(jButton1))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel2)
                            .add(buttonZeroSerial))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(checkTimeStamp)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(checkPrefix)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(fieldPrefix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel1)
                                .add(10, 10, 10)
                                .add(valueSerialNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(dataDirSelect1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(checkTimeStamp))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(checkPrefix)
                    .add(buttonZeroSerial)
                    .add(fieldPrefix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1)
                    .add(valueSerialNumber))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelImageJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelImageJ, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 470, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void setSerialNumberToZero(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSerialNumberToZero
       ((DataModel) ctrl.getModel("data")).setFileCounter(0);
    }//GEN-LAST:event_setSerialNumberToZero

    private void OpenDir(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenDir
        try {
            Process proc = new ProcessBuilder("c:/windows/EXPLORER.EXE", //" /n, /e, "+
                    dataPresentation.getModel(DataModel.PROPERTYNAME_IMAGEDIRECTORY).getString()).start(); 
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }//GEN-LAST:event_OpenDir
    
    private void AutoSaveChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_AutoSaveChanged
        comboFileFormat.setEnabled(checkAutoSave.isSelected());
        //panelDataDir.setEnabled(checkAutoSave.isSelected());
        fieldPrefix.setEnabled(checkPrefix.isSelected());
        //radioPrefix.setEnabled(checkAutoSave.isSelected());
        //radioTimestamp.setEnabled(checkAutoSave.isSelected());
    }//GEN-LAST:event_AutoSaveChanged
                
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton buttonZeroSerial;
    private javax.swing.JCheckBox checkAutoSave;
    private javax.swing.JCheckBox checkIjPutOnDesk;
    private javax.swing.JCheckBox checkIjSeqInStack;
    private javax.swing.JCheckBox checkPrefix;
    private javax.swing.JCheckBox checkTimeStamp;
    private javax.swing.JComboBox comboFileFormat;
    private edu.mbl.jif.data.DataDirSelect dataDirSelect1;
    private javax.swing.JTextField fieldPrefix;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel panelImageJ;
    private javax.swing.JLabel valueSerialNumber;
    // End of variables declaration//GEN-END:variables
    
}
