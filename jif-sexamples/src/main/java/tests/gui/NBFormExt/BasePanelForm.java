package tests.gui.NBFormExt;
      
public class BasePanelForm extends javax.swing.JPanel {
    
    /** Creates new form BasePanelForm */
    public BasePanelForm() {
        initComponents();
    }

    public java.awt.Container getContentPane() {
        return panelContent;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        panelOKCanelButtons = new javax.swing.JPanel();
        buttonOK = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        buttonHelp = new javax.swing.JButton();
        panelContent = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        buttonOK.setText("Ok");
        panelOKCanelButtons.add(buttonOK);

        buttonCancel.setText("Cancel");
        panelOKCanelButtons.add(buttonCancel);

        buttonHelp.setText("Help");
        panelOKCanelButtons.add(buttonHelp);

        add(panelOKCanelButtons, java.awt.BorderLayout.SOUTH);

        add(panelContent, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonHelp;
    private javax.swing.JButton buttonOK;
    private javax.swing.JPanel panelContent;
    private javax.swing.JPanel panelOKCanelButtons;
    // End of variables declaration//GEN-END:variables
    
}
