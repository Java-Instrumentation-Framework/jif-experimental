package edu.mbl.jif.gui.action;

// @author Santhosh Kumar T - santhosh@in.fiorano.com 

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;

/* Usage:
 * 
 JList list = new JList(listData);
 JPanel listPanel = new JPanel(new BorderLayout());
 listPanel.add(new JScrollPane(list), BorderLayout.CENTER);
 Action action = new ContextSensitiveAction(list.getActionMap().get("selectAll"), list);
 listPanel.add(new JButton(action), BorderLayout.SOUTH);

 */


public class ContextSensitiveAction implements Action { 
    protected Action delegate; 
    protected Object source; 
 
    public ContextSensitiveAction(Action delegate, Object source){ 
        this.delegate = delegate; 
        this.source = source; 
    } 
 
    @Override
    public boolean isEnabled(){ 
        return delegate.isEnabled(); 
    } 
 
    @Override
    public void setEnabled(boolean enabled){ 
        delegate.setEnabled(enabled); 
    } 
 
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener){ 
        delegate.addPropertyChangeListener(listener); 
    } 
 
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener){ 
        delegate.removePropertyChangeListener(listener); 
    } 
 
    @Override
    public Object getValue(String key){ 
        return delegate.getValue(key); 
    } 
 
    @Override
    public void putValue(String key, Object value){ 
        delegate.putValue(key, value); 
    } 
 
    @Override
    public void actionPerformed(ActionEvent ae){ 
        delegate.actionPerformed(new ActionEvent(source, ae.getID(), ae.getActionCommand(), ae.getWhen(), ae.getModifiers())); 
    } 
}