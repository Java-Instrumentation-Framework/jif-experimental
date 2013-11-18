package tests.gui.action;

/*
 * Mapper.java
 */

/**
 * A simple class to list all of the InputMap and ActionMap entries
 * for a given component.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.util.*;

public class Mapper extends JFrame implements ActionListener {

  JTextField nameF = new JTextField("Type in a class name");
  JTextArea results = new JTextArea();
  String condNames[] = {"Focused", "In Focused Window", "Focused Ancestor"};
  int conditions[] = { JComponent.WHEN_FOCUSED,
                       JComponent.WHEN_IN_FOCUSED_WINDOW,
                       JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT };
  JComboBox stateC = new JComboBox(condNames);
  JCheckBox inputB = new JCheckBox("Show InputMap");
  JCheckBox actionB = new JCheckBox("Show ActionMap");
  JCheckBox bindingB = new JCheckBox("Show Bindings", true);

  public Mapper() {
    super("Action/Input Mapper");
    setSize(500, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel setupPane = new JPanel(new GridLayout(0,1));
    JPanel classPane = new JPanel(new BorderLayout());
    classPane.add(new JLabel("Class: "), BorderLayout.WEST);
    classPane.add(nameF, BorderLayout.CENTER);
    classPane.add(stateC, BorderLayout.EAST);
    JPanel mapPane = new JPanel();
    mapPane.add(inputB);
    mapPane.add(actionB);
    mapPane.add(bindingB);

    setupPane.add(classPane);
    setupPane.add(mapPane);

    getContentPane().add(setupPane, BorderLayout.NORTH);
    getContentPane().add(new JScrollPane(results), BorderLayout.CENTER);

    nameF.addActionListener(this);
    stateC.addActionListener(this);
    stateC.setEditable(false);
    new Probe().loadAudioActions();  // !!!
    setVisible(true);
  }

  class Probe extends javax.swing.plaf.basic.BasicLookAndFeel {
      public void loadAudioActions() {
          loadActionMap(getAudioActionMap(), "");
      }

      /**
       * Return a short string that identifies this look and feel, e.g.
       * "CDE/Motif".  This string should be appropriate for a menu item.
       * Distinct look and feels should have different names, e.g.
       * a subclass of MotifLookAndFeel that changes the way a few components
       * are rendered should be called "CDE/Motif My Way"; something
       * that would be useful to a user trying to select a L&F from a list
       * of names.
       */
      public String getName() {
          return "probe";
      }
      /**
       * If the underlying platform has a "native" look and feel, and this
       * is an implementation of it, return true.  For example a CDE/Motif
       * look and implementation would return true when the underlying
       * platform was Solaris.
       */
      public boolean isNativeLookAndFeel() {
          return false;
      }
      /**
       * Return a string that identifies this look and feel.  This string
       * will be used by applications/services that want to recognize
       * well known look and feel implementations.  Presently
       * the well known names are "Motif", "Windows", "Mac", "Metal".  Note
       * that a LookAndFeel derived from a well known superclass
       * that doesn't make any fundamental changes to the look or feel
       * shouldn't override this method.
       */
      public String getID() {
          return "probe";
      }
      /**
       * Return a one line description of this look and feel implementation,
       * e.g. "The CDE/Motif Look and Feel".   This string is intended for
       * the user, e.g. in the title of a window or in a ToolTip message.
       */
      public String getDescription() {
          return "A trick to get BasicLookAndFeel's audio action map.";
      }
      /**
       * Return true if the underlying platform supports and or permits
       * this look and feel.  This method returns false if the look
       * and feel depends on special resources or legal agreements that
       * aren't defined for the current platform.
       *
       * @see UIManager#setLookAndFeel
       */
      public boolean isSupportedLookAndFeel() {
          return false;
      }
  }

  public void actionPerformed(ActionEvent ae) {
    String cname = nameF.getText().trim();
    int state = conditions[stateC.getSelectedIndex()];
    try {
      if (isSpecialCase(cname)) {
        handleSpecialCase(cname, state);
      }
      else {
        JComponent comp = (JComponent)Class.forName(cname).newInstance();
        ComponentUI cui = UIManager.getUI(comp);
        cui.installUI(comp);
        results.setText("Map entries for " + cname + ":\n\n");
        if (inputB.isSelected()) {
          loadInputMap(comp.getInputMap(state), "");
          results.append("\n");
        }
        if (actionB.isSelected()) {
          loadActionMap(comp.getActionMap(), "");
          results.append("\n");
        }
        if (bindingB.isSelected()) {
          loadBindingMap(comp, state);
        }
      }
    }
    catch (ClassCastException cce) {
      results.setText(cname + " is not a subclass of JComponent.");
    }
    catch (ClassNotFoundException cnfe) {
      results.setText(cname + " was not found.");
    }
    catch (InstantiationException ie) {
      results.setText(cname + " could not be instantiated.");
    }
    catch (Exception e) {
      results.setText("Exception found:\n" + e);
      e.printStackTrace();
    }
  }

  public void loadInputMap(InputMap im, String indent) {
    KeyStroke[] k = im.allKeys();
    if (k == null) {
      results.append(indent + "No InputMap defined\n");
    }
    else {
      results.append(indent + "\nInputMap (" + k.length + " local keys)\n");
    }
    if (k != null) {
      for (int i = 0; i < k.length; i++) {
        results.append(indent + "  Key:  " + k[i] + ", binding: "
                       + im.get(k[i]) + "\n");
      }
    }
  }

  public void loadActionMap(ActionMap am, String indent) {
    Object[] k = am.allKeys();
    if (k == null) {
      results.append(indent + "No ActionMap defined\n");
    }
    else {
      results.append(indent + "\nActionMap (" + k.length + " local keys)\n");
    }
    if (k != null) {
      for (int i = 0; i < k.length; i++) {
        results.append(indent + "  Action:  " + k[i] + "\n");
      }
    }
  }

  public void loadBindingMap(JComponent c, int condition) {
    loadBindingMap(c.getInputMap(condition), c.getActionMap());
  }

  public void loadBindingMap(InputMap im, ActionMap am) {
    results.append("Bound Actions\n");
    String unboundActions = "";
    String unboundInputKeys = "";
    Hashtable mi = buildReverseMap(im);
    Object[] k = am.allKeys();
    if (k != null) {
      for (int i = 0; i < k.length; i++) {
        if (mi.containsKey(k[i])) {
          results.append("  " + getActionName(k[i]));
          results.append(";" + mi.get(k[i]) + "\n");
        }
        else {
          unboundActions += ("  " + getActionName(k[i]) + "\n");
        }
      }
      results.append("\nUnbound Actions\n\n");
      results.append(unboundActions);
    }

    results.append("\nUnbound InputMap Entries\n");
    k = im.allKeys();
    if (k != null) {
      for (int i = 0; i < k.length; i++) {
        KeyStroke key = (KeyStroke)k[i];
        Object actionKey = im.get(key);
        if (am.get(actionKey) == null) {
          results.append("  " + im.get((KeyStroke)k[i]) + ": " + k[i] + "\n");
        }
      }
    }
  }

  private boolean isSpecialCase(String className) {
    if (className.equals("javax.swing.JApplet") ||
        className.equals("javax.swing.JDialog") ||
        className.equals("javax.swing.JFrame") ||
        className.equals("javax.swing.JWindow") ||
        className.equals("javax.swing.JInternalFrame"))
    {
      return true;
    }
    return false;
  }

  private String getActionName(Object o) {
    if (o instanceof Action) {
      return ((Action)o).getValue(Action.NAME).toString();
    }
    return o.toString();
  }

  private Hashtable buildReverseMap(InputMap im) {
    KeyStroke k[] = im.allKeys();
    Hashtable h = new Hashtable();
    if (k != null) {
      for (int i = 0; i < k.length; i++) {
        Object nk = im.get(k[i]);
        Object cv = h.get(nk);
        if (h.containsKey(nk)) {
          ((Vector)cv).add(k[i]);
        }
        else {
          Vector v = new Vector();
          v.add(k[i]);
          h.put(nk, v);
        }
      }
    }
    return h;
  }

  private void handleSpecialCase(String cname, int condition) {
    Component comp = null;
    JComponent mapComp = null;
    try {
      if (cname.equals("javax.swing.JApplet") ||
          cname.equals("javax.swing.JDialog") ||
          cname.equals("javax.swing.JFrame") ||
          cname.equals("javax.swing.JWindow")) {
        comp = (Component)Class.forName(cname).newInstance();
        mapComp = (JComponent)((JApplet)comp).getLayeredPane();
        results.setText("Map entries for " + cname
                        + " (delegated to JRootPane):\n\n");
      }
      else if (cname.equals("javax.swing.JInternalFrame")) {
        JDesktopPane jdp = new JDesktopPane();
        JInternalFrame jif = new JInternalFrame("Demo");
        jif.setVisible(true);
        jdp.add(jif);
        mapComp = jif;
      }
      if (inputB.isSelected()) {
        loadInputMap(mapComp.getInputMap(condition), "");
        results.append("\n");
      }
      if (actionB.isSelected()) {
        loadActionMap(mapComp.getActionMap(), "");
        results.append("\n");
      }
      if (bindingB.isSelected()) {
        loadBindingMap(mapComp, condition);
      }
    }
    catch (ClassCastException cce) {
      results.setText(cname + " is not a subclass of JComponent.");
    }
    catch (ClassNotFoundException cnfe) {
      results.setText(cname + " was not found.");
    }
    catch (InstantiationException ie) {
      results.setText(cname + " could not be instantiated.");
    }
    catch (Exception e) {
      results.setText("Exception found:\n" + e);
      e.printStackTrace();
    }
  }


  public static void main(String args[]) {
    new Mapper();
  }
}
