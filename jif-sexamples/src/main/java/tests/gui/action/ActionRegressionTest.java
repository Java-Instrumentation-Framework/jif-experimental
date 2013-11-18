/*
 * ActionRegressionTest.java
 *
 * Created on July 3, 2006, 10:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui.action;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;


public final class ActionRegressionTest extends JFrame {
   private JTextArea       textarea;
   private MyMenuAction    myMenuAction;
   private MyToolBarAction myToolBarAction;

   public ActionRegressionTest() {
      super("Action regression test");

      myMenuAction       = new MyMenuAction();
      myToolBarAction    = new MyToolBarAction(myMenuAction);

      textarea           = new JTextArea(10, 40);
      textarea.setEditable(false);
      textarea.setLineWrap(false);
      textarea.setWrapStyleWord(false);
      textarea.append("Java version: " + System.getProperty("java.version") + '\n');
      add(new JScrollPane(textarea));

      setJMenuBar(createMenuBar());
      add(createToolBar(), BorderLayout.PAGE_START);
      add(createButtons(), BorderLayout.PAGE_END);

      pack();
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   public static void main(String[] args) {
      new ActionRegressionTest().setVisible(true);
   }

   private JMenuBar createMenuBar() {
      JMenu menu = new JMenu("Actions");
      menu.setMnemonic('a');
      menu.add(myMenuAction);

      JMenuBar menubar = new JMenuBar();
      menubar.add(menu);
      return menubar;
   }

   private JToolBar createToolBar() {
      JToolBar toolbar = new JToolBar();
      toolbar.setFloatable(false);
      toolbar.setRollover(true);
      toolbar.add(myToolBarAction);
      return toolbar;
   }

   private JPanel createButtons() {
      JPanel panel = new JPanel(new GridLayout(1, 3, 4, 4));
      panel.add(new JButton(new EnableAction()));
      panel.add(new JButton(new DisableAction()));
      panel.add(new JButton(new ClearTextAreaAction()));
      return panel;
   }

   final class MyMenuAction extends AbstractAction {
      public MyMenuAction() {
         super("My action");
         putValue(MNEMONIC_KEY, KeyEvent.VK_M);
         // here, I'd provide a 16x16 icon
      }

      public void actionPerformed(ActionEvent evt) {
         textarea.append("Action performed\n");
      }
   }

   public final class MyToolBarAction extends AbstractAction implements PropertyChangeListener {
      private MyMenuAction wrappedAction;

      MyToolBarAction(final MyMenuAction wrappedAction) {
         super((String)wrappedAction.getValue(NAME));
         for (Object objectKey : wrappedAction.getKeys()) {
            String stringKey = objectKey.toString();
            putValue(stringKey, wrappedAction.getValue(stringKey));
         }
         setEnabled(wrappedAction.isEnabled());
         wrappedAction.addPropertyChangeListener(this);
         this.wrappedAction = wrappedAction;
         // here, I'd override any 16x16 icon with a corresponding 24x24 icon
      }

      public void propertyChange(final PropertyChangeEvent evt) {
         // the commented-out code is my workaround for Java 6
         // no need for it in Java 5

         // if ("enabled".equals(evt.getPropertyName()))
         // setEnabled(Boolean.TRUE.equals(evt.getNewValue()));
         // else
         // putValue(evt.getPropertyName(),evt.getNewValue());

         // this works fine in Java 5, but not in Java 6
         putValue(evt.getPropertyName(), evt.getNewValue());
         textarea.append("[ToolBar] Changed " + evt.getPropertyName() + " to " + evt.getNewValue() +
            '\n');
      }

      public void actionPerformed(final ActionEvent evt) {
         textarea.append("[ToolBar] ");
         wrappedAction.actionPerformed(evt);
      }
   }

   final class EnableAction extends AbstractAction {
      public EnableAction() {
         super("Enable actions");
         putValue(MNEMONIC_KEY, KeyEvent.VK_E);
      }

      public void actionPerformed(ActionEvent evt) {
         myMenuAction.setEnabled(true);
      }
   }

   final class DisableAction extends AbstractAction {
      public DisableAction() {
         super("Disable actions");
         putValue(MNEMONIC_KEY, KeyEvent.VK_D);
      }

      public void actionPerformed(ActionEvent evt) {
         myMenuAction.setEnabled(false);
      }
   }

   final class ClearTextAreaAction extends AbstractAction {
      public ClearTextAreaAction() {
         super("Clear text");
         putValue(MNEMONIC_KEY, KeyEvent.VK_C);
      }

      public void actionPerformed(ActionEvent evt) {
         textarea.setText("");
      }
   }
}
