package tests.binding;


public class TestBean extends MemorableBean{

    // @todo default autoSaveFilename ??

    public TestBean() {}


    private String name = "MyName";
    public static final String PROP_NAME = "name";


    public String getName() {
        return this.name;
    }


    public void setName(String newname) {
        String oldname = name;
        this.name = newname;
        propertyChangeSupport.firePropertyChange(PROP_NAME, oldname, newname);
    }


    private int number = 3;
    public static final String PROP_NUMBER = "number";


    public int getNumber() {
        return this.number;
    }


    public void setNumber(int newnumber) {
        int oldnumber = number;
        this.number = newnumber;
        propertyChangeSupport.firePropertyChange(PROP_NUMBER, oldnumber, newnumber);
    }

    // Indexed Property Change
        /* ----- setting ----------------------------------------------*/

    private int[] setting = new int[]{1,2,3,4,5};
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

//    private java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

//    /**
//     * Add PropertyChangeListener.
//     *
//     * @param listener
//     */
//    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener )
//    {
//        propertyChangeSupport.addPropertyChangeListener( listener );
//    }
//
//    /**
//     * Remove PropertyChangeListener.
//     *
//     * @param listener
//     */
//    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener )
//    {
//        propertyChangeSupport.removePropertyChangeListener( listener );
//    }
//

    

}
