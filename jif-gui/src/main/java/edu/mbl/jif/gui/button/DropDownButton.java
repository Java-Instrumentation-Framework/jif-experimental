package edu.mbl.jif.gui.button;

import edu.mbl.jif.gui.test.FrameForTest;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionEvent;
// @author  santhosh kumar - santhosh@in.fiorano.com
//
public abstract class DropDownButton
        extends JButton implements ChangeListener, PopupMenuListener,
        ActionListener, PropertyChangeListener {
    private final JButton mainButton = this;
    private final JButton arrowButton =
            new JButton(new ImageIcon(getClass().getResource("dropdown.gif")));
    private boolean popupVisible = false;

    public DropDownButton() {
        mainButton.getModel().addChangeListener(this);
        arrowButton.getModel().addChangeListener(this);
        arrowButton.addActionListener(this);
        mainButton.addActionListener(this);
        arrowButton.setMargin(new Insets(3, 0, 3, 0));
        mainButton.addPropertyChangeListener("enabled", this); //NOI18N
    }


    /*------------------------------[ PropertyChangeListener ]---------------------------------------------------*/
    public void propertyChange(PropertyChangeEvent evt) {
        arrowButton.setEnabled(mainButton.isEnabled());
    }


    /*------------------------------[ ChangeListener ]---------------------------------------------------*/
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == mainButton.getModel()) {
            if (popupVisible && !mainButton.getModel().isRollover()) {
                mainButton.getModel().setRollover(true);
                return;
            }
            arrowButton.getModel().setRollover(mainButton.getModel().isRollover());
            arrowButton.setSelected(
                    mainButton.getModel().isArmed() && mainButton.getModel().isPressed());
        } else {
            if (popupVisible && !arrowButton.getModel().isSelected()) {
                arrowButton.getModel().setSelected(true);
                return;
            }
            mainButton.getModel().setRollover(arrowButton.getModel().isRollover());
        }
    }


    /*------------------------------[ ActionListener ]---------------------------------------------------*/
    public void actionPerformed(ActionEvent ae) {
        JPopupMenu popup = getPopupMenu();
        popup.addPopupMenuListener(this);
        popup.show(mainButton, 0, mainButton.getHeight());
    }


    /*------------------------------[ PopupMenuListener ]---------------------------------------------------*/
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        popupVisible = true;
        mainButton.getModel().setRollover(true);
        arrowButton.getModel().setSelected(true);
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        popupVisible = false;
        mainButton.getModel().setRollover(false);
        arrowButton.getModel().setSelected(false);
        ((JPopupMenu) e.getSource()).removePopupMenuListener(this); // act as good programmer :)
    }

    public void popupMenuCanceled(PopupMenuEvent e) {
        popupVisible = false;
    }


    /*------------------------------[ Other Methods ]---------------------------------------------------*/
    protected abstract JPopupMenu getPopupMenu();

    public JButton addToToolBar(JToolBar toolbar) {
        JToolBar tempBar = new JToolBar();
        tempBar.setAlignmentX(0.5f);
        tempBar.setRollover(true);
        tempBar.add(mainButton);
        tempBar.add(arrowButton);
        tempBar.setFloatable(false);
        toolbar.add(tempBar);
        return mainButton;

    }

    public static void main(String[] args) {
        //  if (!SwingUtilities.isEventDispatchThread()) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FrameForTest f = new FrameForTest();
                JToolBar tBar = new JToolBar();
                DropDownButton b = (new MyDropDownButton());
                b.setText("MyButton");
                b.addToToolBar(tBar);
                f.add(tBar);
                f.pack();
                f.setVisible(true);

            }
        });
        //}

    }
}

class MyDropDownButtonOLD extends DropDownButton {
    @Override
    protected JPopupMenu getPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("A popup menu item");
        menuItem.setIcon((Icon) UIManager.get("OptionPane.warningIcon"));
        popup.add(menuItem);
        JMenuItem menuItem2 = new JMenuItem("Another popup menu item");
        popup.add(menuItem2);
        return popup;
    }
}

class MyDropDownButton extends DropDownButton {
    @Override
    protected JPopupMenu getPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem((Icon) UIManager.get("OptionPane.warningIcon"));
        JMenuItem menuItem2 = new JMenuItem((Icon) UIManager.get("FileView.fileIcon"));
        JMenuItem menuItem3 = new JMenuItem((Icon) UIManager.get("FileView.directoryIcon"));
        JMenuItem menuItem4 = new JMenuItem((Icon) UIManager.get("FileView.computerIcon"));
        menuItem4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("menuItem4 clicked");
            }
        });
        popup.add(menuItem1);
        popup.add(menuItem2);
        popup.add(menuItem3);
        popup.add(menuItem4);
        return popup;
    }
}
