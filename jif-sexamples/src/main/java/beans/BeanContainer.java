package beans;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.SwingPropertyChangeSupport;


public class BeanContainer
      extends JFrame implements FocusListener
{
   protected File currentDir = new File(".");

   protected Component currentBean;

   protected String className = "BakedBean";

   protected JFileChooser fileChooser = new JFileChooser();

   public BeanContainer () {
      super("Simple Bean Container");
      getContentPane().setLayout(new FlowLayout());

      setSize(300, 300);

      JPopupMenu.setDefaultLightWeightPopupEnabled(false);

      JMenuBar menuBar = createMenuBar();
      setJMenuBar(menuBar);

      WindowListener wndCloser = new WindowAdapter()
      {
         public void windowClosing (WindowEvent e) {
            System.exit(0);
         }
      };
      addWindowListener(wndCloser);

      setVisible(true);
   }


   protected JMenuBar createMenuBar () {
      JMenuBar menuBar = new JMenuBar();

      JMenu mFile = new JMenu("File");

      JMenuItem mItem = new JMenuItem("New...");
      ActionListener lst = new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            NewBeanThread t = new NewBeanThread();
            t.start();
         }
      };
      mItem.addActionListener(lst);
      mFile.add(mItem);

      mItem = new JMenuItem("Load...");
      lst = new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            LoadBeanThread t = new LoadBeanThread();
            t.start();
         }
      };
      mItem.addActionListener(lst);
      mFile.add(mItem);

      mItem = new JMenuItem("Save...");
      lst = new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            SaveBeanThread t = new SaveBeanThread();
            t.start();
         }
      };
      mItem.addActionListener(lst);
      mFile.add(mItem);

      mFile.addSeparator();

      mItem = new JMenuItem("Exit");
      lst = new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            System.exit(0);
         }
      };
      mItem.addActionListener(lst);
      mFile.add(mItem);
      menuBar.add(mFile);

      JMenu mEdit = new JMenu("Edit");

      mItem = new JMenuItem("Delete");
      lst = new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            if (currentBean == null) {
               return;
            }
            getContentPane().remove(currentBean);
            currentBean = null;
            validate();
            repaint();
         }
      };
      mItem.addActionListener(lst);
      mEdit.add(mItem);
      menuBar.add(mEdit);

      JMenu mLayout = new JMenu("Layout");
      ButtonGroup group = new ButtonGroup();

      mItem = new JRadioButtonMenuItem("FlowLayout");
      mItem.setSelected(true);
      lst = new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            getContentPane().setLayout(new FlowLayout());
            validate();
            repaint();
         }
      };
      mItem.addActionListener(lst);
      group.add(mItem);
      mLayout.add(mItem);

      mItem = new JRadioButtonMenuItem("GridLayout");
      lst = new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            int col = 3;
            int row = (int) Math.ceil(getContentPane().getComponentCount()
                                      / (double) col);
            getContentPane().setLayout(new GridLayout(row, col, 10, 10));
            validate();
            repaint();
         }
      };
      mItem.addActionListener(lst);
      group.add(mItem);
      mLayout.add(mItem);

      mItem = new JRadioButtonMenuItem("BoxLayout - X");
      lst = new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            getContentPane().setLayout(
                  new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
            validate();
            repaint();
         }
      };
      mItem.addActionListener(lst);
      group.add(mItem);
      mLayout.add(mItem);

      mItem = new JRadioButtonMenuItem("BoxLayout - Y");
      lst = new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            getContentPane().setLayout(
                  new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            validate();
            repaint();
         }
      };
      mItem.addActionListener(lst);
      group.add(mItem);
      mLayout.add(mItem);

      group.add(mItem);
      mLayout.add(mItem);

      menuBar.add(mLayout);

      return menuBar;
   }


   public void focusGained (FocusEvent e) {
      currentBean = e.getComponent();
      repaint();
   }


   public void focusLost (FocusEvent e) {
   }


   // This is a heavyweight component so we override paint
   // instead of paintComponent. super.paint(g) will
   // paint all child components first, and then we
   // simply draw over top of them.
   public void paint (Graphics g) {
      super.paint(g);

      if (currentBean == null) {
         return;
      }

      Point pt = getLocationOnScreen();
      Point pt1 = currentBean.getLocationOnScreen();
      int x = pt1.x - pt.x - 2;
      int y = pt1.y - pt.y - 2;
      int w = currentBean.getWidth() + 2;
      int h = currentBean.getHeight() + 2;

      g.setColor(Color.black);
      g.drawRect(x, y, w, h);
   }


   public static void main (String argv[]) {
      new BeanContainer();
   }


   class SaveBeanThread
         extends Thread
   {
      public void run () {
         if (currentBean == null) {
            return;
         }
         fileChooser
               .setDialogTitle("Please choose file to serialize bean");
         fileChooser.setCurrentDirectory(currentDir);
         int result = fileChooser
                      .showSaveDialog(BeanContainer.this);
         repaint();
         if (result != JFileChooser.APPROVE_OPTION) {
            return;
         }
         currentDir = fileChooser.getCurrentDirectory();
         File fChoosen = fileChooser.getSelectedFile();
         try {
            FileOutputStream fStream = new FileOutputStream(
                  fChoosen);
            ObjectOutput stream = new ObjectOutputStream(
                  fStream);
            stream.writeObject(currentBean);
            stream.close();
            fStream.close();
         }
         catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(BeanContainer.this,
                  "Error: " + ex.toString(), "Warning",
                  JOptionPane.WARNING_MESSAGE);
         }
      }
   }



   class LoadBeanThread
         extends Thread
   {
      public void run () {
         fileChooser.setCurrentDirectory(currentDir);
         fileChooser
               .setDialogTitle("Please select file with serialized bean");
         int result = fileChooser
                      .showOpenDialog(BeanContainer.this);
         repaint();
         if (result != JFileChooser.APPROVE_OPTION) {
            return;
         }
         currentDir = fileChooser.getCurrentDirectory();
         File fChoosen = fileChooser.getSelectedFile();
         try {
            FileInputStream fStream = new FileInputStream(
                  fChoosen);
            ObjectInput stream = new ObjectInputStream(fStream);
            Object obj = stream.readObject();
            if (obj instanceof Component) {
               currentBean = (Component) obj;
               currentBean
                     .addFocusListener(BeanContainer.this);
               currentBean.requestFocus();
               getContentPane().add(currentBean);
            }
            stream.close();
            fStream.close();
            validate();
         }
         catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(BeanContainer.this,
                  "Error: " + ex.toString(), "Warning",
                  JOptionPane.WARNING_MESSAGE);
         }
         repaint();
      }
   }



   class NewBeanThread
         extends Thread
   {
      public void run () {
         String result = (String) JOptionPane.showInputDialog(
               BeanContainer.this,
               "Use the following name for demanstration",
               "Input", JOptionPane.INFORMATION_MESSAGE, null,
               null, className);
         repaint();
         if (result == null) {
            return;
         }
         try {
            className = result;
            Class cls = Class.forName(result);
            Object obj = cls.newInstance();
            if (obj instanceof Component) {
               currentBean = (Component) obj;
               currentBean
                     .addFocusListener(BeanContainer.this);
               currentBean.requestFocus();
               getContentPane().add(currentBean);
            }
            validate();
         }
         catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(BeanContainer.this,
                  "Error: " + ex.toString(), "Warning",
                  JOptionPane.WARNING_MESSAGE);
         }
      }
   }

}



class MyBean
      extends JComponent implements Externalizable
{
   // Property names
   public static final String BEAN_VALUE = "Value";

   public static final String BEAN_COLOR = "Color";

   // Properties
   private Font beanFont; // simple

   private Dimension beanDimension; // simple

   private int beanValue; // bound

   private Color beanColor; // constrained

   private String text; // change

   // Manages all PropertyChangeListeners
   protected SwingPropertyChangeSupport propertySupporter = new
         SwingPropertyChangeSupport(
               this);

   // Manages all VetoableChangeListeners
   protected VetoableChangeSupport vetoableChangeSupport = new
         VetoableChangeSupport(this);

   protected transient ChangeEvent changeEvent = null;

   protected EventListenerList listenerList = new EventListenerList();

   public MyBean () {
      beanFont = new Font("SanSerif", Font.BOLD | Font.ITALIC, 12);
      beanDimension = new Dimension(150, 100);
      beanValue = 0;
      beanColor = Color.black;
      text = "BakedBean #";
   }


   public void paintComponent (Graphics g) {
      super.paintComponent(g);
      g.setColor(beanColor);
      g.setFont(beanFont);
      g.drawString(text + beanValue, 30, 30);
   }


   public void setBeanFont (Font font) {
      beanFont = font;
   }


   public Font getBeanFont () {
      return beanFont;
   }


   public void setBeanValue (int newValue) {
      int oldValue = beanValue;
      beanValue = newValue;

      // Notify all PropertyChangeListeners
      propertySupporter.firePropertyChange(BEAN_VALUE, new Integer(oldValue),
            new Integer(newValue));
   }


   public int getBeanValue () {
      return beanValue;
   }


   public void setBeanColor (Color newColor) throws PropertyVetoException {
      Color oldColor = beanColor;

      vetoableChangeSupport.fireVetoableChange(BEAN_COLOR, oldColor, newColor);

      beanColor = newColor;
      propertySupporter.firePropertyChange(BEAN_COLOR, oldColor, newColor);
   }


   public Color getBeanColor () {
      return beanColor;
   }


   public void setBeanString (String newString) {
      text = newString;

      // Notify all ChangeListeners
      fireStateChanged();
   }


   public String getBeanString () {
      return text;
   }


   public void setPreferredSize (Dimension dim) {
      beanDimension = dim;
   }


   public Dimension getPreferredSize () {
      return beanDimension;
   }


   public void setMinimumSize (Dimension dim) {
      beanDimension = dim;
   }


   public Dimension getMinimumSize () {
      return beanDimension;
   }


   public void addPropertyChangeListener (PropertyChangeListener l) {
      propertySupporter.addPropertyChangeListener(l);
   }


   public void removePropertyChangeListener (PropertyChangeListener l) {
      propertySupporter.removePropertyChangeListener(l);
   }


   public void addVetoableChangeListener (VetoableChangeListener l) {
      vetoableChangeSupport.addVetoableChangeListener(l);
   }


   public void removeVetoableChangeListener (VetoableChangeListener l) {
      vetoableChangeSupport.removeVetoableChangeListener(l);
   }


   // Remember that EventListenerList is an array of
   // key/value pairs.
   // key = XXListener class reference
   // value = XXListener instance
   public void addChangeListener (ChangeListener l) {
      listenerList.add(ChangeListener.class, l);
   }


   public void removeChangeListener (ChangeListener l) {
      listenerList.remove(ChangeListener.class, l);
   }


   // EventListenerList dispatching code.
   protected void fireStateChanged () {
      Object[] listeners = listenerList.getListenerList();
      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == ChangeListener.class) {
            if (changeEvent == null) {
               changeEvent = new ChangeEvent(this);
            }
            ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
         }
      }
   }


   public void writeExternal (ObjectOutput out) throws IOException {
      out.writeObject(beanFont);
      out.writeObject(beanDimension);
      out.writeInt(beanValue);
      out.writeObject(beanColor);
      out.writeObject(text);
   }


   public void readExternal (ObjectInput in) throws IOException,
         ClassNotFoundException {
      setBeanFont((Font) in.readObject());
      setPreferredSize((Dimension) in.readObject());
      // Use preferred size for minimum size..
      setMinimumSize(getPreferredSize());
      setBeanValue(in.readInt());
      try {
         setBeanColor((Color) in.readObject());
      }
      catch (PropertyVetoException pve) {
         System.out.println("Color change vetoed..");
      }
      setBeanString((String) in.readObject());
   }


   public static void main (String[] args) {
      JFrame frame = new JFrame("BakedBean");
      frame.getContentPane().add(new MyBean());
      frame.setVisible(true);
      frame.pack();
   }
}
