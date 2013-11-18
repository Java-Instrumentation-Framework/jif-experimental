/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.stateset;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author GBH
 */
public class TestModelBean {

    protected String text = "INIT";
    public static final String PROP_TEXT = "text";

    public String getText()
      {
        return text;
      }

    public void setText(String aString)
      {
        String oldAString = this.text;
        this.text = aString;
        propertyChangeSupport.firePropertyChange(PROP_TEXT, oldAString, aString);
      }


    protected String a = "A init";
    public static final String PROP_A = "a";

    public String getA()
      {
        return a;
      }

    public void setA(String aStr)
      {
        String oldA = this.a;
        this.a = a;
        propertyChangeSupport.firePropertyChange(PROP_A, oldA, a);
      }

    protected int anInteger = 0;
    public static final String PROP_ANINTEGER = "anInteger";

    public int getAnInteger()
      {
        return anInteger;
      }

    public void setAnInteger(int anInteger)
      {
        int oldAnInteger = this.anInteger;
        this.anInteger = anInteger;
        System.out.println("AnInteger set");
        propertyChangeSupport.firePropertyChange(PROP_ANINTEGER, oldAnInteger, anInteger);
      }



    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener)
      {
        propertyChangeSupport.addPropertyChangeListener(listener);
      }

    public void removePropertyChangeListener(PropertyChangeListener listener)
      {
        propertyChangeSupport.removePropertyChangeListener(listener);
      }

}
