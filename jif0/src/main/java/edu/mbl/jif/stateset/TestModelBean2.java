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
public class TestModelBean2 {

    protected String aString = "INIT2";
    public static final String PROP_ASTRING = "aString";

    public String getAString()
      {
        return aString;
      }

    public void setAString(String aString)
      {
        String oldAString = this.aString;
        this.aString = aString;
        propertyChangeSupport.firePropertyChange(PROP_ASTRING, oldAString, aString);
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
        propertyChangeSupport.firePropertyChange(PROP_ANINTEGER, oldAnInteger, anInteger);
      }

    protected String aBoolean;
    public static final String PROP_ABOOLEAN = "aBoolean";

    public String getABoolean()
      {
        return aBoolean;
      }

    public void setABoolean(String aBoolean)
      {
        String oldABoolean = this.aBoolean;
        this.aBoolean = aBoolean;
        propertyChangeSupport.firePropertyChange(PROP_ABOOLEAN, oldABoolean, aBoolean);
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
