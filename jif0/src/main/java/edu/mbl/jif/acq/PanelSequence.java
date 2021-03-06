/*
 * SeriesPanel.java
 *
 * Created on May 26, 2006, 3:36 PM
 */

package edu.mbl.jif.acq;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.camacq.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.camera.Utils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;

/**
 *
 * @author  GBH
 */
public class PanelSequence extends JPanel {
    private final InstrumentController ctrl;
    
    private final CameraModel.CameraPresentation cameraPresentation;
    private final AcqModel.AcqPresentation acqPresentation;
    
    /**
     * Creates new form PanelAcq
     */
    
    /** Creates new form SeriesPanel */
    public PanelSequence(InstrumentController ctrl) {
        this.ctrl = ctrl;
        this.cameraPresentation = new CameraModel.CameraPresentation((CameraModel) ctrl.getModel("camera"));
        this.acqPresentation = new AcqModel.AcqPresentation((AcqModel) ctrl.getModel("acq"));
        initComponents();
        
        spinNumber.setModel(SpinnerAdapterFactory.createNumberAdapter(
                acqPresentation.getComponentModel(AcqModel.PROPERTYNAME_IMAGESINSEQUENCE),
                ((AcqModel)acqPresentation.getBean()).getImagesInSequence(),   // defaultValue
                1, 9999, 1));
        spinInterval.setModel(
                SpinnerAdapterFactory.createNumberAdapter(acqPresentation.getModel(AcqModel.PROPERTYNAME_INTERVAL),
                ((AcqModel)acqPresentation.getBean()).getImagesInSequence(),
                0.1,
                10000.0,
                1.0));
        
        spinInitialDelay.setModel(
                SpinnerAdapterFactory.createNumberAdapter(acqPresentation.getModel(AcqModel.PROPERTYNAME_INITDELAY),
                ((AcqModel)acqPresentation.getBean()).getInitDelay(),
                0.0,
                1000.0,
                1.0));
        initEventHandling();
    }
    
    private void initEventHandling() {
        acqPresentation.getModel(AcqModel.PROPERTYNAME_IMAGESINSEQUENCE).addValueChangeListener(new
                DurationHandler());
        acqPresentation.getModel(AcqModel.PROPERTYNAME_INTERVAL).addValueChangeListener(new
                DurationHandler());
    }
    
    
    private final class DurationHandler
            implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            // update duration
            labelDuration.setText(Utils.durationHhMmSs((long)(
                    acqPresentation.getModel(AcqModel.PROPERTYNAME_IMAGESINSEQUENCE).intValue() *
                    acqPresentation.getModel(AcqModel.PROPERTYNAME_INTERVAL).doubleValue() * 1000)));
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    checkFlushFirst = BasicComponentFactory.createCheckBox(acqPresentation.getModel(AcqModel.PROPERTYNAME_FLUSHFIRSTFRAME), "Flush first frame");
    jPanel1 = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    spinNumber = new javax.swing.JSpinner();
    jLabel1 = new javax.swing.JLabel();
    spinInterval = new javax.swing.JSpinner();
    jLabel5 = new javax.swing.JLabel();
    labelDuration = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    spinInitialDelay = new javax.swing.JSpinner();
    jCheckBox1 = BasicComponentFactory.createCheckBox(acqPresentation.getModel(AcqModel.PROPERTYNAME_DISPLAYWHILEACQ), "Display While Acquiring");
    jPanel2 = new javax.swing.JPanel();
    jCheckBox2 = new javax.swing.JCheckBox();
    jLabel4 = new javax.swing.JLabel();

    setBackground(new java.awt.Color(203, 208, 203));

    checkFlushFirst.setFont(new java.awt.Font("SansSerif", 0, 11));
    checkFlushFirst.setText("Flush 1st frame");
    checkFlushFirst.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    checkFlushFirst.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    checkFlushFirst.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    checkFlushFirst.setMargin(new java.awt.Insets(0, 0, 0, 0));
    checkFlushFirst.setOpaque(false);

    jPanel1.setOpaque(false);

    jLabel2.setFont(new java.awt.Font("SansSerif", 0, 11));
    jLabel2.setText("Number");

    jLabel1.setFont(new java.awt.Font("SansSerif", 0, 11));
    jLabel1.setText("Interval (sec)");

    jLabel5.setText("Duration");

    labelDuration.setFont(new java.awt.Font("SansSerif", 0, 12));
    labelDuration.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labelDuration.setText("00:00:00");

    jLabel3.setFont(new java.awt.Font("SansSerif", 0, 11));
    jLabel3.setText("Initial Delay");

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(jPanel1Layout.createSequentialGroup()
        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jPanel1Layout.createSequentialGroup()
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
              .add(jPanel1Layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabel1))
              .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel5))
              .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
              .add(spinNumber, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
              .add(spinInterval, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
              .add(org.jdesktop.layout.GroupLayout.TRAILING, labelDuration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
          .add(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .add(jLabel3)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 32, Short.MAX_VALUE)
            .add(spinInitialDelay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(jPanel1Layout.createSequentialGroup()
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
          .add(spinNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(jLabel2))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(jLabel1)
          .add(spinInterval, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(jLabel5)
          .add(labelDuration))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(jLabel3)
          .add(spinInitialDelay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
    );

    jCheckBox1.setText("Live display while waiting");
    jCheckBox1.setOpaque(false);

    jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jCheckBox2.setText("Perform Z-Scan");

    jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/acqZscan.gif"))); // NOI18N

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .add(jLabel4)
        .add(6, 6, 6)
        .add(jCheckBox2)
        .addContainerGap(8, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(jPanel2Layout.createSequentialGroup()
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(org.jdesktop.layout.GroupLayout.TRAILING, jCheckBox2)
          .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel4))
        .addContainerGap())
    );

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
            .add(checkFlushFirst)
            .add(jCheckBox1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(32, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(layout.createSequentialGroup()
            .addContainerGap()
            .add(checkFlushFirst)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(jCheckBox1)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents
    
    
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox checkFlushFirst;
  private javax.swing.JCheckBox jCheckBox1;
  private javax.swing.JCheckBox jCheckBox2;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JLabel labelDuration;
  private javax.swing.JSpinner spinInitialDelay;
  private javax.swing.JSpinner spinInterval;
  private javax.swing.JSpinner spinNumber;
  // End of variables declaration//GEN-END:variables
    
}
