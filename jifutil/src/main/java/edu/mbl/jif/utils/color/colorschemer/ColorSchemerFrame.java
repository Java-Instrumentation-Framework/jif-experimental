/*
 * ColorSchemerFrame.java
 *
 * Created on December 29, 2007, 2:36 PM
 */
package edu.mbl.jif.utils.color.colorschemer;

/**
 *
 * @author  GBH
 */
public class ColorSchemerFrame
        extends javax.swing.JFrame {

    int n = 9;
    String[] name = {
        "red", "orange", "yellow", "green", "aqua", "blue", "purple", "violet",
        "fusia"
    };
    int[] hues = {
        0, 32, 50, 120, 160, 230, 275, 300, 345
    };

    ColorTestPanel[] panes = new ColorTestPanel[n];
    float hueShift = 0f;
    float saturation = 0.33f;
    float v0 = 0.3f;
    float v1 = 0.6f;
    float v2 = 0.7f;
    float v3 = 0.8f;
    float v4 = 0.9f;


    /** Creates new form ColorSchemerFrame */
    public ColorSchemerFrame() {
        initComponents();
        float h = 0;
        //
        for (int i = 0; i < n; i++) {
            String string = name[i];

        }

        for (int i = 0; i < n; i++) {
            panes[i] = new ColorTestPanel(name[i]);
            h = (float) hues[i] / 360.0f;
            //  h = ( (float) i / n) + 0.1f;
            //  h = (h > 0.99999f) ? 0.99999f : h;
            panes[i].setHueSat(h,
                    saturation);
            panes[i].setV(v0,
                    v1,
                    v2,
                    v3,
                    v4);
            this.jPanel1.add(panes[i]);
        }
        jPanel1.revalidate();
        this.pack();

    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ColorSchemerFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
