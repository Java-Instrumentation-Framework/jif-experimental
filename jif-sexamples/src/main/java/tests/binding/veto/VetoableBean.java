
package tests.binding.veto;

import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

public class VetoableBean { 
    private final VetoableChangeSupport vcs = new VetoableChangeSupport(this); 

    public void addVetoableChangeListener(VetoableChangeListener listener) { 
        this.vcs.addVetoableChangeListener(listener); 
    } 

    public void removeVetoableChangeListener(VetoableChangeListener listener) { 
        this.vcs.removeVetoableChangeListener(listener); 
    } 

    private int value; 

    public int getValue() { 
        return this.value; 
    } 

    public void setValue(int value) throws PropertyVetoException { 
        this.vcs.fireVetoableChange("value", this.value, value); 
        this.value = value; 
    } 
}
