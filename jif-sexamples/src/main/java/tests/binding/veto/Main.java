
package tests.binding.veto;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
 
public class Main {
    private double interest;
 
    private VetoableChangeSupport vcs = new VetoableChangeSupport(this);
 
    public Main() {
        vcs.addVetoableChangeListener(new VetoChangeListener());
    }
 
    public double getInterest() {
        return interest;
    }
 
    public void setInterest(double interest) {
        try {
            vcs.fireVetoableChange("interest", new Double(this.interest), new Double(interest));
             this.interest = interest;
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        Main bean = new Main();
        bean.setInterest(10.99);
        bean.setInterest(15.99);
        
        bean.setInterest(20.99);
				System.out.println("finally: " + bean.getInterest());
    }
}
 
class VetoChangeListener implements VetoableChangeListener {
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        String eventName = evt.getPropertyName();
        if (eventName.equalsIgnoreCase("interest")) {
            double interest = ((Double) evt.getNewValue()).doubleValue();
            if (interest > 20.00) {
                throw new PropertyVetoException("Interest must be below 20.00", evt);                
            }
            System.out.println("Interest applied = " + interest);
        }
    }
}