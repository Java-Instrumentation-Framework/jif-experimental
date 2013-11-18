/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.gui.button;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/*
 * // this is a trick to get hold of the client property which prevents
// closing of the popup when the down arrow is pressed.
JComboBox box = new JComboBox();
Object preventHide = box.getClientProperty("doNotCancelPopup");
putClientProperty("doNotCancelPopup", preventHide);
 */

public class PopdownButton {

    private JToggleButton fButton = new JToggleButton();
    private JPopupMenu fPopupMenu = new JPopupMenu();
    private boolean fShouldHandlePopupWillBecomeInvisible = true;

    public PopdownButton(Icon defaultIcon, Icon pressedAndSelectedIcon) {
        // setup the default button state.
        fButton.setIcon(defaultIcon);
        fButton.setPressedIcon(pressedAndSelectedIcon);
        fButton.setSelectedIcon(pressedAndSelectedIcon);
        fButton.setFocusable(false);
        fButton.putClientProperty("JButton.buttonType", "textured");

        // install a mouse listener on the button to hide and show the popup
        // menu as appropriate.
        fButton.addMouseListener(createButtonMouseListener());

        // add a popup menu listener to update the button's selection state
        // when the menu is being dismissed.
        fPopupMenu.addPopupMenuListener(createPopupMenuListener());

        // install a special client property on the button to prevent it from
        // closing of the popup when the down arrow is pressed.
        JComboBox box = new JComboBox();
        Object preventHide = box.getClientProperty("doNotCancelPopup");
        fButton.putClientProperty("doNotCancelPopup", preventHide);
    }

    private MouseListener createButtonMouseListener() {
        return new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // if the popup menu is currently showing, then hide it.
                // else if the popup menu is not showing, then show it.
                if (fPopupMenu.isShowing()) {
                    hidePopupMenu();
                } else {
                    showPopupMenu();
                }
            }
        };
    }

    private PopupMenuListener createPopupMenuListener() {
        return new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // no implementation.
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // handle this event if so indicated. the only time we don't handle
                // this event is when the button itself is pressed, the press action
                // toggles the button selected state for us. this case handles when
                // the button has been toggled, but the user clicks outside the
                // button in order to dismiss the menu.
                if (fShouldHandlePopupWillBecomeInvisible) {
                    fButton.setSelected(false);
                }
            }
            public void popupMenuCanceled(PopupMenuEvent e) {
                // the popup menu has been canceled externally (either by
                // pressing escape or clicking off of the popup menu). update
                // the button's state to reflect the menu dismissal.
                fButton.setSelected(false);
            }
        };
    }

    private void hidePopupMenu() {
        fShouldHandlePopupWillBecomeInvisible = false;
        fPopupMenu.setVisible(false);
        fShouldHandlePopupWillBecomeInvisible = true;
    }

    private void showPopupMenu() {
        // show the menu below the button, and slightly to the right.
        fPopupMenu.show(fButton, 5, fButton.getHeight());
    }

    public JComponent getComopnent() {
        return fButton;
    }

    public JPopupMenu getPopupMenu() {
        return fPopupMenu;
    }
}
