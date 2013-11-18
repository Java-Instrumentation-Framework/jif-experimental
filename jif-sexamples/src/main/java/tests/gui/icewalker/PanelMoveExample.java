package tests.gui.icewalker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class PanelMoveExample extends JPanel {
    
    public PanelModel model;
    public JPanel selectedPanel, central;
    public JButton moveUp, moveDown;
    private int DOWN = 1, UP = 2;
    
    public PanelMoveExample() {
        super( new BorderLayout() );
        model = new PanelModel();
        createPanels();
        
        moveUp = new JButton("Move Up");
        moveUp.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                movePanel(UP);
            }
        });
        moveDown = new JButton("Move Down");
        moveDown.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                movePanel(DOWN);
            }
        });
        
        JPanel butPanel = new JPanel( new FlowLayout(FlowLayout.RIGHT) );
            butPanel.add(moveUp);
            butPanel.add(moveDown);
            
        add(butPanel, BorderLayout.NORTH);
    }
    
    public void layoutPanels() {
        central.removeAll();
        for(int i = 0; i < model.getSize(); i++) {
            central.add( model.getPanel(i) );
        }
        central.validate();
        central.repaint();
    }
    
    public void createPanels() {
        if(central != null) {
            remove(central);
        }
        
        Border border = new LineBorder( Color.black, 1);
        JPanel component = new JPanel( new FlowLayout(FlowLayout.LEFT) );
        	component.add( new JCheckBox("Check Box") );
        	component.add( new JButton("Button") );
        	component.setPreferredSize( new Dimension(300,50) );
        
        central = new JPanel( new GridLayout(0,1,5,10) );
        //central.setBorder( new ComponentTitledBorder(component, central, border) );
        
        for(int i = 0; i < 5; i++) {
            JPanel panel = new JPanel();
                panel.addMouseListener( new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        JPanel thisPanel = (JPanel)e.getSource();
                        JPanel oldPanel;
                        
                        if(selectedPanel != null) {
                            oldPanel = selectedPanel;
                            oldPanel.setBorder( new MatteBorder(1,1,1,1, Color.black) );
                            oldPanel.setBackground(null);
                        }
                        
                        selectedPanel = thisPanel;
                        selectedPanel.setBorder( new MatteBorder(1,1,1,1,Color.red) );
                        selectedPanel.setBackground( new Color(220,220,230) );
                    }
                });
                panel.setBorder( new MatteBorder(1,1,1,1, Color.black) );
                panel.setToolTipText("Click To Select");
                panel.add( new JLabel("Selectable Panel " + (i+1))) ;
            
            //for(int x = 0; x < 3; x++) {
            //    panel.add( new JLabel("Item " + (x+1) ) );
            //}
            
            model.addPanel( panel );
        }
        
        layoutPanels();
        add(central, BorderLayout.CENTER);
    }
    
    public void movePanel(int direction) {
        if(selectedPanel == null) {
            return;
        }
        
        if(direction == DOWN) {
            if( model.getPanelIndex(selectedPanel) == (model.getSize() - 1)) {
                return;
            }
            
            int panelIndex = model.getPanelIndex( selectedPanel );
            model.removePanel(selectedPanel);
            model.insertPanelAt(selectedPanel, panelIndex + 1);
        } else {
            if( model.getPanelIndex(selectedPanel) == 0) {
                return;
            }
            
            int panelIndex = model.getPanelIndex( selectedPanel );
            model.removePanel(selectedPanel);
            model.insertPanelAt(selectedPanel, panelIndex - 1);
        }
        
        layoutPanels();
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Panel Move Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add( new PanelMoveExample() );
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
    }
    
    
}