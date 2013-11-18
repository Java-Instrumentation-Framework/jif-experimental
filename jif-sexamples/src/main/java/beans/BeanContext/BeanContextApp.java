/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.BeanContext;

import java.awt.*;

import java.awt.event.*;

//import ju.ch09.*;
import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextChildSupport;
import java.beans.beancontext.BeanContextServiceProvider;
import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.beans.beancontext.BeanContextServiceRevokedListener;
import java.beans.beancontext.BeanContextServices;
import java.beans.beancontext.BeanContextServicesSupport;
import java.util.Iterator;
import java.util.TooManyListenersException;

public class BeanContextApp extends Frame {

    Object menuItems[][] = {{"File", "Exit"}};
    MenuItemHandler mih = new MenuItemHandler();
    MyMenuBar menuBar = new MyMenuBar(menuItems, mih, mih);
    int screenWidth = 400;
    int screenHeight = 400;
    TextArea textArea = new TextArea();

    public static void main(String args[])
      {

        BeanContextApp app = new BeanContextApp();
        //MenuApp app = new MenuApp();

      }

    public BeanContextApp()
      {

        super("BeanContextApp");

        setMenuBar(menuBar);

        setup();

        setSize(screenWidth, screenHeight);

        addWindowListener(new WindowEventHandler());

        show();

      }

    void setup()
      {

        add("Center", textArea);

        ParentBean parent = new ParentBean(textArea);

        ChildBean child = new ChildBean();

        parent.add(child);

        child.useContext();

      }

    public class ChildBean extends BeanContextChildSupport
        implements BeanContextServiceRevokedListener {

        TextArea textArea = new TextArea();

        public void useContext()
          {

            BeanContextServices beanContext =
                (BeanContextServices) getBeanContext();

            if (beanContext.hasService(textArea.getClass())) {

                try {

                    textArea = (TextArea) beanContext.getService(this, this,
                        textArea.getClass(), null, this);

                } catch (Exception ex) {

                    System.out.println(ex.toString());

                }

                String msg = "The child was able to access the services of its parent.";

                textArea.setText(msg);

            }

          }

        public void serviceRevoked(BeanContextServiceRevokedEvent ev)
          {
          }

    }

    public class ParentBean extends BeanContextServicesSupport
        implements BeanContextServiceProvider {

        TextArea textArea;

        public ParentBean(TextArea textArea)
          {

            this.textArea = textArea;

            addService(textArea.getClass(), this);

          }

        public Object getService(BeanContextChild child,
            Object requestor, Class serviceClass, Object serviceSelector,
            BeanContextServiceRevokedListener bcsrl)
            throws TooManyListenersException
          {

            return textArea;

          }

        public Object getService(BeanContextServices bcs,
            Object requestor, Class serviceClass, Object serviceSelector)
          {

            return textArea;

          }

        public Iterator getCurrentServiceSelectors(BeanContextServices bcs,
            Class serviceClass)
          {

            return null;

          }

        public void releaseService(BeanContextServices bcs,
            Object requestor, Object service)
          {
          }

    }

    class MenuItemHandler implements ActionListener, ItemListener {

        public void actionPerformed(ActionEvent ev)
          {

            String s = ev.getActionCommand();

            if (s.equals("Exit")) {

                System.exit(0);

            }

          }

        public void itemStateChanged(ItemEvent e)
          {
          }

    }

    class WindowEventHandler extends WindowAdapter {

        public void windowClosing(WindowEvent e)
          {

            System.exit(0);

          }

    }
}

class MyMenu extends Menu {

    public MyMenu(Object labels[], ActionListener al, ItemListener il)
      {

        super((String) labels[0]);

        String menuName = (String) labels[0];

        char firstMenuChar = menuName.charAt(0);

        if (firstMenuChar == '~' || firstMenuChar == '!') {

            setLabel(menuName.substring(1));

            if (firstMenuChar == '~') {
                setEnabled(false);
            }
        }

        for (int i = 1; i < labels.length; ++i) {

            if (labels[i] instanceof String) {


                if ("-".equals(labels[i])) {
                    addSeparator();
                } else {

                    String label = (String) labels[i];

                    char firstChar = label.charAt(0);

                    switch (firstChar) {

                        case '+':

                            CheckboxMenuItem checkboxItem = new CheckboxMenuItem(label.substring(1));

                            checkboxItem.setState(true);

                            add(checkboxItem);

                            checkboxItem.addItemListener(il);

                            break;

                        case '#':

                            checkboxItem = new CheckboxMenuItem(label.substring(1));

                            checkboxItem.setState(true);

                            checkboxItem.setEnabled(false);

                            add(checkboxItem);

                            checkboxItem.addItemListener(il);

                            break;

                        case '-':

                            checkboxItem = new CheckboxMenuItem(label.substring(1));

                            checkboxItem.setState(false);

                            add(checkboxItem);

                            checkboxItem.addItemListener(il);

                            break;

                        case '=':

                            checkboxItem = new CheckboxMenuItem(label.substring(1));

                            checkboxItem.setState(false);

                            checkboxItem.setEnabled(false);

                            add(checkboxItem);

                            checkboxItem.addItemListener(il);

                            break;

                        case '~':

                            MenuItem menuItem = new MenuItem(label.substring(1));

                            menuItem.setEnabled(false);

                            add(menuItem);

                            menuItem.addActionListener(al);

                            break;

                        case '!':

                            menuItem = new MenuItem(label.substring(1));

                            add(menuItem);

                            menuItem.addActionListener(al);

                            break;

                        default:

                            menuItem = new MenuItem(label);

                            add(menuItem);

                            menuItem.addActionListener(al);

                    }

                }

            } else {

                add(new MyMenu((Object[]) labels[i], al, il));

            }

        }

      }

    public MenuItem getItem(String menuItem)
      {

        int numItems = getItemCount();

        for (int i = 0; i < numItems; ++i) {
            if (menuItem.equals(getItem(i).getLabel())) {
                return getItem(i);
            }
        }
        return null;

      }

}

class MyMenuBar extends MenuBar {

    public MyMenuBar(Object labels[][], ActionListener al,
        ItemListener il)
      {

        super();

        for (int i = 0; i < labels.length; ++i) {
            add(new MyMenu(labels[i], al, il));
        }
      }

    public MyMenu getMenu(String menuName)
      {

        int numMenus = getMenuCount();

        for (int i = 0; i < numMenus; ++i) {
            if (menuName.equals(getMenu(i).getLabel())) {
                return ((MyMenu) getMenu(i));
            }
        }
        return null;

      }

}

class MenuApp extends Frame {

    MyMenuBar menuBar;
    MenuApp.EventHandler eh = new MenuApp.EventHandler();

    public MenuApp()
      {

        super("Menu Madness");

        setup();

        setSize(400, 400);

        addWindowListener(eh);

        show();

      }

    void setup()
      {

        setBackground(Color.white);

        setupMenuBar();

      }

    void setupMenuBar()
      {

        String gotoMenu[] = {"Go To", "Beginning", "End", "-", "Line Number"};

        Object menuItems[][] = {
            {"File", "New", "Open", "-", "~Save", "~Save As", "-", "Exit"},
            {"Edit", "Copy", "Cut", "-", "~Paste"},
            {"Search", "Find", "~Find Next", "~Find Previous", "-", gotoMenu},
            {"View", "-Hex", "+Line Number", "+Column Number"},
            {"Help", "About Menu Madness"},
        };

        menuBar = new MyMenuBar(menuItems, eh, eh);

        setMenuBar(menuBar);

      }

    class EventHandler extends WindowAdapter implements ActionListener,
        ItemListener {

        public void actionPerformed(ActionEvent e)
          {

            String selection = e.getActionCommand();

            if ("Exit".equals(selection)) {

                System.exit(0);

            } else if ("New".equals(selection) || "Open".equals(selection)) {

                menuBar.getMenu("File").getItem("Save").setEnabled(true);

                menuBar.getMenu("File").getItem("Save As").setEnabled(true);

            } else if ("Copy".equals(selection) || "Cut".equals(selection)) {

                menuBar.getMenu("Edit").getItem("Paste").setEnabled(true);

            } else if ("Find".equals(selection)) {

                menuBar.getMenu("Search").getItem("Find Next").setEnabled(true);

                menuBar.getMenu("Search").getItem("Find Previous").setEnabled(true);

            } else if ("About Menu Madness".equals(selection)) {

                menuBar.getMenu("Help").setEnabled(false);

            }

          }

        public void itemStateChanged(ItemEvent e)
          {
          }

        public void windowClosing(WindowEvent e)
          {

            System.exit(0);

          }

    }
}

