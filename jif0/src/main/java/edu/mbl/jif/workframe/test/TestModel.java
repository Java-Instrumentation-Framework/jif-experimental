package edu.mbl.jif.workframe.test;

import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.beans.Model;               
import com.jgoodies.binding.list.ArrayListModel;       
import com.jgoodies.binding.list.ObservableList;       
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;
import java.util.Arrays;                               
import java.util.List;                                 
import java.util.prefs.Preferences;                    
import javax.swing.ListModel;                          


public class TestModel extends Model {

    Preferences prefs = Application.getInstance().getPreferences();
    
    // <editor-fold defaultstate="collapsed" desc="<<< Declarations >>>">

    // --- bool ---
    public static final String PROPERTYNAME_BOOL = "bool";
    private boolean bool;
    static final boolean BOOL_DEFAULT = false;
    PreferencesAdapter boolPrefAdapter = 
        new PreferencesAdapter(prefs, TestModel.PROPERTYNAME_BOOL, BOOL_DEFAULT);

    // --- number ---
    public static final String PROPERTYNAME_NUMBER = "number";
    private int number;
    static final int NUMBER_DEFAULT = 1;
    PreferencesAdapter numberPrefAdapter = 
        new PreferencesAdapter(prefs, TestModel.PROPERTYNAME_NUMBER, NUMBER_DEFAULT);

    // --- value ---
    public static final String PROPERTYNAME_VALUE = "value";
    private double value;
    static final double VALUE_DEFAULT = 5.5;
    PreferencesAdapter valuePrefAdapter = 
        new PreferencesAdapter(prefs, TestModel.PROPERTYNAME_VALUE, VALUE_DEFAULT);

    // --- string ---
    public static final String PROPERTYNAME_STRING = "string";
    private String string;
    static final String STRING_DEFAULT = "Something";
    PreferencesAdapter stringPrefAdapter = 
        new PreferencesAdapter(prefs, TestModel.PROPERTYNAME_STRING, STRING_DEFAULT);

    // --- numberSelect ---
    public static final String PROPERTYNAME_NUMBERSELECT = "numberSelect";
    private int numberSelect;
    static final int NUMBERSELECT_DEFAULT = 8;
    PreferencesAdapter numberSelectPrefAdapter = 
        new PreferencesAdapter(prefs, TestModel.PROPERTYNAME_NUMBERSELECT, NUMBERSELECT_DEFAULT);
    public static final Integer  NUMBERSELECT_ENUM_1 = new Integer(8);
    public static final Integer  NUMBERSELECT_ENUM_2 = new Integer(12);
    public static final List  NUMBERSELECT_OPTIONS = Arrays.asList(new Integer[] {
        NUMBERSELECT_ENUM_1,
        NUMBERSELECT_ENUM_2
    });
    private ObservableList numberSelectListModel;
    private Object         numberSelectListSelection;

    // --- valueSelect ---
    public static final String PROPERTYNAME_VALUESELECT = "valueSelect";
    private double valueSelect;
    static final double VALUESELECT_DEFAULT = 7.2;
    PreferencesAdapter valueSelectPrefAdapter = 
        new PreferencesAdapter(prefs, TestModel.PROPERTYNAME_VALUESELECT, VALUESELECT_DEFAULT);
    public static final Double  VALUESELECT_ENUM_1 = new Double(20.0);
    public static final Double  VALUESELECT_ENUM_2 = new Double(10.0);
    public static final Double  VALUESELECT_ENUM_3 = new Double(5.0);
    public static final Double  VALUESELECT_ENUM_4 = new Double(2.5);
    public static final List  VALUESELECT_OPTIONS = Arrays.asList(new Double[] {
        VALUESELECT_ENUM_1,
        VALUESELECT_ENUM_2,
        VALUESELECT_ENUM_3,
        VALUESELECT_ENUM_4
    });
    private ObservableList valueSelectListModel;
    private Object         valueSelectListSelection;

    // --- stringSelect ---
    public static final String PROPERTYNAME_STRINGSELECT = "stringSelect";
    private String stringSelect;
    static final String STRINGSELECT_DEFAULT = "Option One";
    PreferencesAdapter stringSelectPrefAdapter = 
        new PreferencesAdapter(prefs, TestModel.PROPERTYNAME_STRINGSELECT, STRINGSELECT_DEFAULT);
    public static final String  STRINGSELECT_ENUM_1 = new String("Option One");
    public static final String  STRINGSELECT_ENUM_2 = new String("Option Two");
    public static final String  STRINGSELECT_ENUM_3 = new String("Option Three");
    public static final String  STRINGSELECT_ENUM_4 = new String("Option Four");
    public static final List  STRINGSELECT_OPTIONS = Arrays.asList(new String[] {
        STRINGSELECT_ENUM_1,
        STRINGSELECT_ENUM_2,
        STRINGSELECT_ENUM_3,
        STRINGSELECT_ENUM_4
    });
    private ObservableList stringSelectListModel;
    private Object         stringSelectListSelection;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="<<< Constructor >>>">
    
    //InstrumentController instrumentCtrl;

  /**
     * Creates a new instance of Test
     */
    public TestModel() { //InstrumentController instrumentCtrl) {

        //this.instrumentCtrl = instrumentCtrl;

        // Initialize enumerated list for numberSelect
        numberSelectListModel = new ArrayListModel();
        numberSelectListModel.addAll(NUMBERSELECT_OPTIONS);
        numberSelectListSelection = numberSelectListModel.get(0);

        // Initialize enumerated list for valueSelect
        valueSelectListModel = new ArrayListModel();
        valueSelectListModel.addAll(VALUESELECT_OPTIONS);
        valueSelectListSelection = valueSelectListModel.get(0);

        // Initialize enumerated list for stringSelect
        stringSelectListModel = new ArrayListModel();
        stringSelectListModel.addAll(STRINGSELECT_OPTIONS);
        stringSelectListSelection = stringSelectListModel.get(0);

    }
    // </editor-fold>

    /**
     * Gets the current value of bool from preferences
     * @return Current value of bool
     */
    public boolean isBool() {
        bool = boolPrefAdapter.getBoolean();
         return bool;
    }

    /**
     * Sets the value of bool to preferences
     * @param bool New value for bool
     */
    public void setBool(boolean bool) {
        boolean oldBool = isBool();
        this.bool = bool;
        boolPrefAdapter.setBoolean(bool);
        firePropertyChange(PROPERTYNAME_BOOL, oldBool, bool);
    }

    /**
     * Gets the current value of number from preferences
     * @return Current value of number
     */
    public int getNumber() {
        number = numberPrefAdapter.getInt();
         return number;
    }

    /**
     * Sets the value of number to preferences
     * @param number New value for number
     */
    public void setNumber(int number) {
        int oldNumber = getNumber();
        this.number = number;
        numberPrefAdapter.setInt(number);
        firePropertyChange(PROPERTYNAME_NUMBER, oldNumber, number);
    }

    /**
     * Gets the current value of value from preferences
     * @return Current value of value
     */
    public double getValue() {
        value = valuePrefAdapter.getDouble();
         return value;
    }

    /**
     * Sets the value of value to preferences
     * @param value New value for value
     */
    public void setValue(double value) {
        double oldValue = getValue();
        this.value = value;
        valuePrefAdapter.setDouble(value);
        firePropertyChange(PROPERTYNAME_VALUE, oldValue, value);
    }

    /**
     * Gets the current value of string from preferences
     * @return Current value of string
     */
    public String getString() {
        string = stringPrefAdapter.getString();
         return string;
    }

    /**
     * Sets the value of string to preferences
     * @param string New value for string
     */
    public void setString(String string) {
        String oldString = getString();
        this.string = string;
        stringPrefAdapter.setString(string);
        firePropertyChange(PROPERTYNAME_STRING, oldString, string);
    }

    /**
     * Gets the current value of numberSelect from preferences
     * @return Current value of numberSelect
     */
    public int getNumberSelect() {
        numberSelect = numberSelectPrefAdapter.getInt();
         return numberSelect;
    }

    /**
     * Sets the value of numberSelect to preferences
     * @param numberSelect New value for numberSelect
     */
    public void setNumberSelect(int numberSelect) {
        int oldNumberSelect = getNumberSelect();
        this.numberSelect = numberSelect;
        numberSelectPrefAdapter.setInt(numberSelect);
        firePropertyChange(PROPERTYNAME_NUMBERSELECT, oldNumberSelect, numberSelect);
    }

    /**
     * ListModel accessor for numberSelect
     * @return ListModel of numberSelect
     */
    public ListModel getNumberSelectListModel() {
        return numberSelectListModel;
    }

    /**
     * ListSelection accessor for numberSelect
     * @return ListSelection of numberSelect
     */
    public Object getNumberSelectListSelection() {
        return numberSelectListSelection;
    }

    /**
     * Gets the current value of valueSelect from preferences
     * @return Current value of valueSelect
     */
    public double getValueSelect() {
        valueSelect = valueSelectPrefAdapter.getDouble();
         return valueSelect;
    }

    /**
     * Sets the value of valueSelect to preferences
     * @param valueSelect New value for valueSelect
     */
    public void setValueSelect(double valueSelect) {
        double oldValueSelect = getValueSelect();
        this.valueSelect = valueSelect;
        valueSelectPrefAdapter.setDouble(valueSelect);
        firePropertyChange(PROPERTYNAME_VALUESELECT, oldValueSelect, valueSelect);
    }

    /**
     * ListModel accessor for valueSelect
     * @return ListModel of valueSelect
     */
    public ListModel getValueSelectListModel() {
        return valueSelectListModel;
    }

    /**
     * ListSelection accessor for valueSelect
     * @return ListSelection of valueSelect
     */
    public Object getValueSelectListSelection() {
        return valueSelectListSelection;
    }

    /**
     * Gets the current value of stringSelect from preferences
     * @return Current value of stringSelect
     */
    public String getStringSelect() {
        stringSelect = stringSelectPrefAdapter.getString();
         return stringSelect;
    }

    /**
     * Sets the value of stringSelect to preferences
     * @param stringSelect New value for stringSelect
     */
    public void setStringSelect(String stringSelect) {
        String oldStringSelect = getStringSelect();
        this.stringSelect = stringSelect;
        stringSelectPrefAdapter.setString(stringSelect);
        firePropertyChange(PROPERTYNAME_STRINGSELECT, oldStringSelect, stringSelect);
    }

    /**
     * ListModel accessor for stringSelect
     * @return ListModel of stringSelect
     */
    public ListModel getStringSelectListModel() {
        return stringSelectListModel;
    }

    /**
     * ListSelection accessor for stringSelect
     * @return ListSelection of stringSelect
     */
    public Object getStringSelectListSelection() {
        return stringSelectListSelection;
    }

}