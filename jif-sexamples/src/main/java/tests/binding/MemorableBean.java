package tests.binding;


import edu.mbl.jif.io.xml.ObjectStoreXML;
import java.beans.PropertyChangeEvent;

public class MemorableBean { //extends AbstractBean{

    // @todo default autoSaveFilename ??
    protected String autoSaveFilename = "testbean"; //


    public MemorableBean() {
    }

    //<editor-fold defaultstate="collapsed" desc=">>>--- PropertyChangeSupport ---<<<" >
    public java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);


    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }


    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc=">>>--- Save / Restore ---<<<" >
    public void setToAutoSave(String filename) {
        this.autoSaveFilename = filename;
        propertyChangeSupport.addPropertyChangeListener(new ChangeSaver());
    }


    public void save(String filename) {
        try {
            ObjectStoreXML.write(this, filename);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }


    public <T extends Object> T restore(String filename) {
        Object obj = null;
        try {
            obj = ObjectStoreXML.read(filename);
        } catch (Throwable ex) {
            System.out.println("No file found, create new bean");
        //ex.printStackTrace();
        }
        if (obj != null) {
            return (T) this.getClass().cast(obj);
        } else {
            System.out.println("obj == null, using default object values");
            return (T) this;
        }
    }


    class ChangeSaver
            implements java.beans.PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println("Auto-saved." + evt.getSource().toString());
            save(autoSaveFilename);
        }


    }
    // </editor-fold>
}
