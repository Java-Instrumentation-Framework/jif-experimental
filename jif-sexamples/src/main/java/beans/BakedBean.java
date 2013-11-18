package beans;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.SwingPropertyChangeSupport;

public class BakedBean extends JComponent implements Externalizable {
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
  protected SwingPropertyChangeSupport propertySupporter = new SwingPropertyChangeSupport(
      this);

  // Manages all VetoableChangeListeners
  protected VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

  protected transient ChangeEvent changeEvent = null;

  protected EventListenerList listenerList = new EventListenerList();

  public BakedBean() {
    beanFont = new Font("SanSerif", Font.BOLD | Font.ITALIC, 12);
    beanDimension = new Dimension(150, 100);
    beanValue = 0;
    beanColor = Color.black;
    text = "BakedBean #";
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(beanColor);
    g.setFont(beanFont);
    g.drawString(text + beanValue, 30, 30);
  }

  public void setBeanFont(Font font) {
    beanFont = font;
  }

  public Font getBeanFont() {
    return beanFont;
  }

  public void setBeanValue(int newValue) {
    int oldValue = beanValue;
    beanValue = newValue;

    // Notify all PropertyChangeListeners
    propertySupporter.firePropertyChange(BEAN_VALUE, new Integer(oldValue),
        new Integer(newValue));
  }

  public int getBeanValue() {
    return beanValue;
  }

  public void setBeanColor(Color newColor) throws PropertyVetoException {
    Color oldColor = beanColor;

    vetoableChangeSupport.fireVetoableChange(BEAN_COLOR, oldColor, newColor);

    beanColor = newColor;
    propertySupporter.firePropertyChange(BEAN_COLOR, oldColor, newColor);
  }

  public Color getBeanColor() {
    return beanColor;
  }

  public void setBeanString(String newString) {
    text = newString;

    // Notify all ChangeListeners
    fireStateChanged();
  }

  public String getBeanString() {
    return text;
  }

  public void setPreferredSize(Dimension dim) {
    beanDimension = dim;
  }

  public Dimension getPreferredSize() {
    return beanDimension;
  }

  public void setMinimumSize(Dimension dim) {
    beanDimension = dim;
  }

  public Dimension getMinimumSize() {
    return beanDimension;
  }

  public void addPropertyChangeListener(PropertyChangeListener l) {
    propertySupporter.addPropertyChangeListener(l);
  }

  public void removePropertyChangeListener(PropertyChangeListener l) {
    propertySupporter.removePropertyChangeListener(l);
  }

  public void addVetoableChangeListener(VetoableChangeListener l) {
    vetoableChangeSupport.addVetoableChangeListener(l);
  }

  public void removeVetoableChangeListener(VetoableChangeListener l) {
    vetoableChangeSupport.removeVetoableChangeListener(l);
  }

  // Remember that EventListenerList is an array of
  // key/value pairs.
  // key = XXListener class reference
  // value = XXListener instance
  public void addChangeListener(ChangeListener l) {
    listenerList.add(ChangeListener.class, l);
  }

  public void removeChangeListener(ChangeListener l) {
    listenerList.remove(ChangeListener.class, l);
  }

  // EventListenerList dispatching code.
  protected void fireStateChanged() {
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ChangeListener.class) {
        if (changeEvent == null)
          changeEvent = new ChangeEvent(this);
        ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
      }
    }
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(beanFont);
    out.writeObject(beanDimension);
    out.writeInt(beanValue);
    out.writeObject(beanColor);
    out.writeObject(text);
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    setBeanFont((Font) in.readObject());
    setPreferredSize((Dimension) in.readObject());
    // Use preferred size for minimum size..
    setMinimumSize(getPreferredSize());
    setBeanValue(in.readInt());
    try {
      setBeanColor((Color) in.readObject());
    } catch (PropertyVetoException pve) {
      System.out.println("Color change vetoed..");
    }
    setBeanString((String) in.readObject());
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("BakedBean");
    frame.getContentPane().add(new BakedBean());
    frame.setVisible(true);
    frame.pack();
  }
}
