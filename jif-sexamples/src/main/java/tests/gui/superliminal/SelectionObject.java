package tests.gui.superliminal;

import java.util.ArrayList;


/**
 * Description: Selection objects are shared containers used to coordinate selection events
 * between graphical viewers and objects dependant upon selections made in viewers.
 * Copyright 2005 - Superliminal Software
 * @author Melinda Green
 */
public class SelectionObject<type> {
    private type selected;
    private ArrayList<Listener> listeners = new ArrayList<Listener>();

    public void setSelection(type newsel) {
        if ((selected != null) && selected.equals(newsel)) {
            return;
        }

        selected = newsel;
        fireSelectionChanged();
    }

    public type getSelection() {
        return selected;
    }

    public void addSelectionListener(Listener<type> sl) {
        listeners.add(sl);
    }

    public void removeSelectionListener(Listener<type> sl) {
        listeners.remove(sl);
    }

    protected void fireSelectionChanged() {
        for (Listener sl : listeners) {
            sl.selectionChanged(selected);
        }
    }

    public interface Listener<type> {
        public void selectionChanged(type newSelection);
    }
}
