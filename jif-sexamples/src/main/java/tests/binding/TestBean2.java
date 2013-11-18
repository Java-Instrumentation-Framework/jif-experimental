/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests.binding;

/**
 *
 * @author GBH
 */
public class TestBean2 {
        /* ----- setting ----------------------------------------------*/

    private int[] setting;
    public static final String PROP_SETTING = "setting";

    /**
     * Get the value of setting
     *
     * @return the value of setting
     */
    public int[] getSetting() {
        return this.setting;
    }

    /**
     * Set the value of setting
     *
     * @param newsetting new value of setting
     */
    public void setSetting(int[] newsetting) {
        int[] oldsetting = setting;
        this.setting = newsetting;
        propertyChangeSupport.firePropertyChange(PROP_SETTING, oldsetting, newsetting);
    }

    /**
     * Get the value of setting at specified index
     *
     * @param index
     * @return the value of setting at specified index
     */
    public int getSetting(int index) {
        return this.setting[index];
    }
    /**
     * Set the value of setting at specified index.
     *
     * @param index
     * @param newsetting new value of setting at specified index
     */
    public void setSetting(int index, int newsetting) {
        int oldsetting = setting[index];
        this.setting[index] = newsetting;
        propertyChangeSupport.fireIndexedPropertyChange(PROP_SETTING, index, oldsetting, newsetting);
    }

    private java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener )
    {
        propertyChangeSupport.addPropertyChangeListener( listener );
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener )
    {
        propertyChangeSupport.removePropertyChangeListener( listener );
    }


}
