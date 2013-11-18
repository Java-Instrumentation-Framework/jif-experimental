package tests.gui.keyboard;


import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ActionMapUIResource;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Keyboard_ParkingLot {
    
 /*
 * InputMap maps a KeyStroke to an object
 * ActionMap maps from an object to an Action.

Object actionMapKey = inputMap.get(KeyStroke.getKeyStroke(keyEvent));
if (actionMapKey != null) {
Action action = actionMap.get(actionMapKey);
if (action != null) {
// run the actions actionPerformed() method
}
}

WHEN_FOCUSED
WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
WHEN_IN_FOCUSED_WINDOW.

 *
myComponent.getInputMap().put(KeyStroke.getKeyStroke("F10"), 
"cut");
 *
 *
Action myAction = new AbstractAction("doSomething") {
public void actionPerformed() {
doSomething();
}
};

myComponent.getActionMap().put(myAction.get(Action.NAME), myAction);
  */ 
    
        
// bindKeyToButtonAction - regardless of focus
// Usage: bindKeyToButtonAction(button,  actionName, new SomeAction(), KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));

    public void bindKeyToButtonAction(JComponent button, String actionName, Action action, KeyStroke keyStroke) {
        // setting the button to receive action when F3 is pressed
        InputMap keyMap = new ComponentInputMap(button);
        keyMap.put(keyStroke, actionName);

        ActionMap actionMap = new ActionMapUIResource();
        actionMap.put(actionName, action);

        SwingUtilities.replaceUIActionMap(button, actionMap);
        SwingUtilities.replaceUIInputMap(button, JComponent.WHEN_IN_FOCUSED_WINDOW, keyMap);
    // button action setting done
    }

    // setting the button to receive action when F3 is pressed
//InputMap keyMap = new ComponentInputMap(button);
//keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "action");
//
//ActionMap actionMap = new ActionMapUIResource();
//actionMap.put("action", new YourAction());
//
//SwingUtilities.replaceUIActionMap(button, actionMap);
//SwingUtilities.replaceUIInputMap(button, JComponent.WHEN_IN_FOCUSED_WINDOW, keyMap);
    
/**
* Binds an Action to a JComponent via the Action's configured ACCELERATOR_KEY.
*/
    public static void initKeyBinding(JComponent component, Action action) {
        String name = (String) action.getValue(Action.NAME);
        KeyStroke keyStroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        component.getActionMap().put(name, action);
        component.getInputMap().put(keyStroke, name);
    }

    public Keyboard_ParkingLot() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(
                "focusOwner",
                new PropertyChangeListener() {

                    Color FOCUS_COLOR = Color.CYAN;
                    Component last;
                    Color background = null;

                    public void propertyChange(PropertyChangeEvent evt) {
                        Component current = (Component) evt.getOldValue();
                        Component future = (Component) evt.getNewValue();

                        if ((current == last) && (current != null)) {
                            current.setBackground(background);
                        }

                        last = future;
                        if (last != null) {
                            background = last.getBackground();
                            last.setBackground(FOCUS_COLOR);
                        }
                    }

                });

    /*  ....and voila!you get EVERY component that can ever get focus hightligted
    with your favorite color!
    This is great for debugging focus traversal problems -
    and it actually also looks cool(at least if you find a better color, )
     */
    }

}
