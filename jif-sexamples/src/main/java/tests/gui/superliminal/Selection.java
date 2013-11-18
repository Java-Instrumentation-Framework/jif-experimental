package tests.gui.superliminal;

import java.util.ArrayList;

/**
 * Description: Selection objects are shared containers used to coordinate selection events
 * between graphical viewers and objects dependant upon selections made in viewers.
 * Copyright 2005 - Superliminal Software
 *
 * Selection Coordination:
 * Share instances of this class among several parts of your code that need to coordinate
 * on some sort of “Selected” object. Each such code section can change the currently
 * selected object (usually due to user selection events) and they can add selection
 * listeners to stay informed of changes made by other code sections. JDK 1.5
 * generics made it possible to create this surprisingly simple yet powerful utility.
 * @author Melinda Green
 */
public class Selection<type> {

    private type selected;

    public void setSelection(type newsel) {
        if ((selected == null && (newsel == null) || selected != null && selected.equals(newsel))) {
            return; /// no change/
        }
        selected = newsel;
        fireSelectionChanged();
    }

    public type getSelection() {
        return selected;
    }

    //

    private ArrayList<Listener<type>> listeners = new ArrayList<Listener<type>>();

    public interface Listener<type> {

        public void selectionChanged(type newSelection);
    }

    public void addSelectionListener(Listener<type> sl) {
        listeners.add(sl);
    }

    public void removeSelectionListener(Listener<type> sl) {
        listeners.remove(sl);
    }

    protected void fireSelectionChanged() {
        for (Listener<type> sl : listeners) {
            sl.selectionChanged(selected);
        }
    }
}
