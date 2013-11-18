package tests.gui.action;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.ArrayList;

import javax.swing.*;

/**
 * An example of using a common set of AbstractActions for handling
 * JToolBar, JMenuBar and JPopup menu responses. Support is included
 * to synchronize toggle buttons and checkboxes.
 *
 * @author    R. Kevin Cole, kcole@users.sourceforge.net
 */
public class SharedActions {

    public static final Integer ACTION1_KEY = new Integer(1);
    public static final Integer ACTION2_KEY = new Integer(2);
    public static final Integer ACTION3_KEY = new Integer(3);
    public static final Integer ACTION4_KEY = new Integer(4);
    public static final Integer ABOUT_KEY = new Integer(5);
    public static final Integer EXIT_KEY = new Integer(6);
    /** Map of AbstractActions to Action Keywords    */
    static final Hashtable<Integer, Action> actionTable = new Hashtable<Integer, Action>(89);
    /** Map of a list of toggleComponents to Actions	*/
    static final Hashtable<Action, ArrayList<AbstractButton>> toggleListTable = 
        new Hashtable<Action, ArrayList<AbstractButton>>();
    /** Our popup menu */
    protected final JPopupMenu popup = new JPopupMenu();
    /**    The frame in which we are embedded    */
    JFrame frame;

    public SharedActions(final JFrame frame) {
        this.frame = frame;

        buildActionTable();
        buildPopupMenu();

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setText(
            "\n\tUse the MenuBar, ToolBar or right-click for a popup menu.\n\n" + "\tAction1 disables itself and enables Action2.\n" + "\tAction2 disables itself and enables Action1.\n" + "\tAction3 and Action4 support toggle buttons and checkboxes.\n" + "\tThe About action enables Actions 1 and 2 and selects Actions 3 and 4.\n\n" + "\tAll components use a common set of actions.\n" + "\tThe actions and corresponding components\n" + "\tare enabled and disabled in concert.\n\n" + "\tFor larger swing applications, I place all actions\n" + "\tin a separate package which maps to a directory structure\n" + "\tsomething like this...\n\n" + "\tmypackage \n" + "\t\tSharedActions.java\n" + "\tmypackage/action\n" + "\t\tExitAction.java\n" + "\t\tAction1.java\n" + "\t\tAction2.java\n" + "\t\tAboutAction.java\n");
        frame.getContentPane().add(new MyToolBar(actionTable), BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(textPane), BorderLayout.CENTER);
        textPane.addMouseListener(new MousePopupListener());
        frame.setJMenuBar(new MyMenuBar(actionTable));
        frame.setSize(640, 480);

        //	send window closing events to our "Exit" action handler.
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                getAction(SharedActions.EXIT_KEY).actionPerformed(null);
            }

        });
    }

    /**    Enable/disable an AbstractAction
     *     @param actionName    the key that maps this action in actionTable
     *     @param b    true to enable or false to disable the action
     */
    public static synchronized void setActionEnabled(final Integer actionKey, final boolean b) {
        Action aa = actionTable.get(actionKey);

        if (aa != null) {
            aa.setEnabled(b);
        }
    }

    /**	coordinate toggle buttons such as JCheckBoxMenuItem and JToggleButton. */
    public static synchronized void registerToggleAction(AbstractButton btn, Integer key) {
        Action action = actionTable.get(key);
        ArrayList<AbstractButton> list = toggleListTable.get(action);
        if (list == null) {
            throw new IllegalArgumentException(
                "Key does not map to an existing action");
        }
        list.add(btn);
    }

    /**    Enable/disable an AbstractAction
     *     @param actionName    the key that maps this action in actionTable
     *     @param b    true to enable or false to disable the action
     */
    public static synchronized void setActionSelected(final Integer actionKey, final boolean b) {
        Action aa = actionTable.get(actionKey);

        if (aa != null) {
            ArrayList<AbstractButton> list = toggleListTable.get(aa);
            if (list != null) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    AbstractButton btn = (AbstractButton) list.get(i);
                    btn.setSelected(b);
                }
            }
        }
    }

    /**	@return    the action from the action-map that is associated with
     *	this action key
     */
    public Action getAction(final Integer actionKey) {
        return actionTable.get(actionKey);
    }

    /** Populate a map of actions.
     */
    private void buildActionTable() {
        actionTable.put(SharedActions.ABOUT_KEY, new AboutAction());
        actionTable.put(SharedActions.EXIT_KEY, new ExitAction());
        actionTable.put(SharedActions.ACTION1_KEY, new Action1());
        actionTable.put(SharedActions.ACTION2_KEY, new Action2());

        Action action = new Action3();
        toggleListTable.put(action, new ArrayList<AbstractButton>());
        actionTable.put(SharedActions.ACTION3_KEY, action);

        action = new Action4();
        toggleListTable.put(action, new ArrayList<AbstractButton>());
        actionTable.put(SharedActions.ACTION4_KEY, action);

        //	initialize the ACTION2_KEY components.
        SharedActions.setActionEnabled(SharedActions.ACTION2_KEY, false);
    }

    /**    Build our JPopupMenu
     */
    public void buildPopupMenu() {
        this.popup.add(new JMenuItem(actionTable.get(SharedActions.ACTION1_KEY)));
        this.popup.add(new JMenuItem(actionTable.get(SharedActions.ACTION2_KEY)));

        JCheckBoxMenuItem item = new JCheckBoxMenuItem(actionTable.get(SharedActions.ACTION3_KEY));
        SharedActions.registerToggleAction(item, SharedActions.ACTION3_KEY);
        this.popup.add(item);

        item = new JCheckBoxMenuItem(actionTable.get(SharedActions.ACTION4_KEY));
        SharedActions.registerToggleAction(item, SharedActions.ACTION4_KEY);
        this.popup.add(item);

        this.popup.add(new JSeparator());
        this.popup.add(new JMenuItem(actionTable.get(SharedActions.EXIT_KEY)));
    }

    
// --------------- a test routine --------------------
    public static void main(String[] argv) throws Exception {
        JFrame frame = new JFrame("Action Example");
        final SharedActions instance = new SharedActions(frame);
        frame.setVisible(true);
    }

    /**    Handle mouse clicks
     */
    class MousePopupListener extends MouseAdapter {

        public void mouseReleased(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                Point pt = SwingUtilities.convertPoint((Component) e.getSource(), e.getX(),
                    e.getY(), frame);
                popup.show(frame, pt.x, pt.y);
                return;
            }
        }

    }
// -------------- A MenuBar and ToolBar using our common actions ------------------
    /** Our implementation of a JMenuBar.
     */
    public class MyMenuBar extends JMenuBar {

        public MyMenuBar(final Hashtable<Integer, Action> actions) {
            JMenu menu = new JMenu("File");
            menu.setMnemonic('F');
            menu.add(new JMenuItem(actions.get(SharedActions.ACTION1_KEY)));
            menu.add(new JMenuItem(actions.get(SharedActions.ACTION2_KEY)));
            menu.add(new JSeparator());
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(actions.get(SharedActions.ACTION3_KEY));
            SharedActions.registerToggleAction(item, SharedActions.ACTION3_KEY);
            menu.add(item);

            item = new JCheckBoxMenuItem(actions.get(SharedActions.ACTION4_KEY));
            SharedActions.registerToggleAction(item, SharedActions.ACTION4_KEY);
            menu.add(item);

            menu.add(new JMenuItem(actions.get(SharedActions.EXIT_KEY)));
            add(menu);

            menu = new JMenu("Help");
            menu.setMnemonic('H');
            menu.add(new JMenuItem((Action) actions.get(SharedActions.ABOUT_KEY)));
            add(menu);
        }

    }

    /**    Our implementation of a JToolBar
     */
    public class MyToolBar extends JToolBar {

        public MyToolBar(final Hashtable<Integer, Action> actions) {
            super();

            add(new JButton(actions.get(SharedActions.ACTION1_KEY)));
            add(new JButton(actions.get(SharedActions.ACTION2_KEY)));

            JToggleButton btn = new JToggleButton(actions.get(SharedActions.ACTION3_KEY));
            SharedActions.registerToggleAction(btn, SharedActions.ACTION3_KEY);
            add(btn);

            btn = new JToggleButton(actions.get(SharedActions.ACTION4_KEY));
            SharedActions.registerToggleAction(btn, SharedActions.ACTION4_KEY);
            add(btn);
        }

    }
// -------------- Our common actions ------------------
    /** Close the application.
     */
    public class ExitAction extends AbstractAction {

        public ExitAction() {
            putValue(Action.NAME, "exit");
            putValue(Action.SHORT_DESCRIPTION, "Exit this application");
            putValue(Action.ACTION_COMMAND_KEY, "exit");
            putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_X));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F10"));
        //	putValue(Action.SMALL_ICON, Icons.getInstance().get(Icons.SMALL_EXIT));
        }

        public void actionPerformed(final ActionEvent ae) {
            System.out.println("Exit action performed.");
            System.exit(0);
        }

    }

    /** Test action1
     */
    public class Action1 extends AbstractAction {

        public Action1() {
            putValue(Action.NAME, "action1");
            putValue(Action.SHORT_DESCRIPTION, "Action 1");
            putValue(Action.ACTION_COMMAND_KEY, "action1");
            putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_1));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F1"));
        }

        public void actionPerformed(final ActionEvent ae) {
            System.out.println("Action 1 performed.");
            SharedActions.setActionEnabled(SharedActions.ACTION1_KEY, false);
            SharedActions.setActionEnabled(SharedActions.ACTION2_KEY, true);
        }

    }

    /** Test action2
     */
    public class Action2 extends AbstractAction {

        public Action2() {
            putValue(Action.NAME, "action2");
            putValue(Action.SHORT_DESCRIPTION, "Action 2");
            putValue(Action.ACTION_COMMAND_KEY, "action2");
            putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_2));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F2"));
        //	putValue(Action.SMALL_ICON, Icons.getInstance().get(Icons.SMALL_ACTION2));
        }

        public void actionPerformed(final ActionEvent ae) {
            System.out.println("Action 2 performed.");
            SharedActions.setActionEnabled(SharedActions.ACTION1_KEY, true);
            SharedActions.setActionEnabled(SharedActions.ACTION2_KEY, false);
        }

    }

    /** action3 is used by toggle buttons.
     */
    public class Action3 extends AbstractAction {

        public Action3() {
            putValue(Action.NAME, "action3");
            putValue(Action.SHORT_DESCRIPTION, "Toggle Action 3");
            putValue(Action.ACTION_COMMAND_KEY, "action3");
            putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_3));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F3"));
        }

        public void actionPerformed(final ActionEvent ae) {
            AbstractButton btn = (AbstractButton) ae.getSource();

            System.out.println("Action 3 performed.");
            SharedActions.setActionSelected(SharedActions.ACTION3_KEY, btn.isSelected());
        }

    }

    /** action4 is used by a different set of toggle buttons.
     */
    public class Action4 extends AbstractAction {

        public Action4() {
            putValue(Action.NAME, "action4");
            putValue(Action.SHORT_DESCRIPTION, "Toggle Action 4");
            putValue(Action.ACTION_COMMAND_KEY, "action4");
            putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_4));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F4"));
        }

        public void actionPerformed(final ActionEvent ae) {
            AbstractButton btn = (AbstractButton) ae.getSource();

            System.out.println("Action 3 performed.");
            SharedActions.setActionSelected(SharedActions.ACTION4_KEY, btn.isSelected());
        }

    }

    /**	The "About" action
     */
    public class AboutAction extends AbstractAction {

        public AboutAction() {
            putValue(Action.NAME, "about");
            putValue(Action.SHORT_DESCRIPTION, "About the author");
            putValue(Action.ACTION_COMMAND_KEY, "about");
            putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_A));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F9"));
        }

        public void actionPerformed(final ActionEvent ae) {
            System.out.println("About performed.");
            JOptionPane.showMessageDialog(frame,
                "SharedActions illustrates one way of handling\n" + "AbstractActions in a Swing application.\n\n" + "The author can be reached at kcole@users.sourceforge.net\n\n",
                "About SharedActions", JOptionPane.INFORMATION_MESSAGE);
            SharedActions.setActionEnabled(SharedActions.ACTION1_KEY, true);
            SharedActions.setActionEnabled(SharedActions.ACTION2_KEY, true);
            SharedActions.setActionSelected(SharedActions.ACTION3_KEY, true);
            SharedActions.setActionSelected(SharedActions.ACTION4_KEY, true);
        }

    }
}
