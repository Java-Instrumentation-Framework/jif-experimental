/*
from  http://javafaq.nu/java-example-code-890.html
 */
package tests.gui.keyboard;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class MyKeyEventDispatcher
        implements KeyEventDispatcher {

    public HashSet<Component> comps = new HashSet<Component>();
    public HashMap<Integer, Character> swaps = new HashMap<Integer, Character>();

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        Component comp = keyEvent.getComponent();
        if (comps.contains(comp) && keyEvent.getID() == KeyEvent.KEY_PRESSED) {
            Set<Integer> swapSet = swaps.keySet();
            Iterator<Integer> it = swapSet.iterator();
            while (it.hasNext()) {
                Integer keyCode = it.next();
                if (keyCode.intValue() == keyEvent.getKeyCode()) {
                    KeyEvent ev = new KeyEvent(comp, KeyEvent.KEY_TYPED,
                            System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED,
                            swaps.get(keyCode).charValue());
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(comp, ev);
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {

            public void run() {
                JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JTextField tf = new JTextField();
                f.add(tf, BorderLayout.NORTH);
                JTextField tf2 = new JTextField();
                f.add(tf2, BorderLayout.SOUTH);

                MyKeyEventDispatcher med = new MyKeyEventDispatcher();
                med.swaps.put(new Integer(KeyEvent.VK_F4), new Character('\u00bd'));
                med.comps.add(tf);

                KeyboardFocusManager kbfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                kbfm.addKeyEventDispatcher(med);

                f.pack();
                f.setVisible(true);
            }

        };
        EventQueue.invokeLater(r);
    }

}
