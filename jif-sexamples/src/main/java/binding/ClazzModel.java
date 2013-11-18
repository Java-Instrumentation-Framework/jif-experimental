package binding;

import binding.MMgrPropertiesAdapter;
import com.jgoodies.binding.beans.Model;               
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.common.collect.ObservableList;
  
import java.util.Arrays;                               
import java.util.List;                                 
import javax.swing.ListModel;                          
import java.util.prefs.Preferences;


public class ClazzModel extends Model {

    Preferences prefs = Preferences.userRoot();


    // --- number
    public static final String PROPERTYNAME_NUMBER = "number";
    private int number = 12;

    // --- bigNumber
    public static final String PROPERTYNAME_BIGNUMBER = "bigNumber";
    private double bigNumber;
    static final Double bigNumber_DEFAULT = 2.2222;
    MMgrPropertiesAdapter bigNumberPrefAdapter = 
        new MMgrPropertiesAdapter(prefs, PROPERTYNAME_BIGNUMBER, bigNumber_DEFAULT);

    // --- selectableNumber
    public static final String PROPERTYNAME_SELECTABLENUMBER = "selectableNumber";
    private int selectableNumber = 2;
    public static final Integer  SELECTABLENUMBER_ENUM_1 = new Integer(1);
    public static final Integer  SELECTABLENUMBER_ENUM_2 = new Integer(2);
    public static final Integer  SELECTABLENUMBER_ENUM_3 = new Integer(3);
    public static final List  SELECTABLENUMBER_OPTIONS = Arrays.asList(new Integer[] {
        SELECTABLENUMBER_ENUM_1,
        SELECTABLENUMBER_ENUM_2,
        SELECTABLENUMBER_ENUM_3
    });
    private ObservableList selectableNumberListModel;
    private Object         selectableNumberListSelection;

    // --- numFloat
    public static final String PROPERTYNAME_NUMFLOAT = "numFloat";
    private float numFloat = 22.3f;

    // --- stringer
    public static final String PROPERTYNAME_STRINGER = "stringer";
    private String stringer = "This";

    // --- characterism
    public static final String PROPERTYNAME_CHARACTERISM = "characterism";
    private char characterism = 'c';

    // --- aintItSo
    public static final String PROPERTYNAME_AINTITSO = "aintItSo";
    private boolean aintItSo = true;

  /**
     * Creates a new instance of Clazz
     */
    public ClazzModel() {

    // Initialize enumerated list for selectableNumber
    selectableNumberListModel = new ArrayListModel();
    selectableNumberListModel.addAll(SELECTABLENUMBER_OPTIONS);
    selectableNumberListSelection = selectableNumberListModel.get(0);

  }

    /**
     * ListModel accessor for selectableNumber
     * @return ListModel of selectableNumber
     */
    public ListModel getSelectableNumberListModel() {
        return selectableNumberListModel;
    }

    /**
     * ListSelection accessor for selectableNumber
     * @return ListSelection of selectableNumber
     */
    public Object getSelectableNumberListSelection() {
        return selectableNumberListSelection;
    }

    /**
     * Gets the current value of number
     * @return Current value of number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the value of number
     * @param number New value for number
     */
    public void setNumber(int number) {
        int oldNumber = getNumber();
         this.number=number;
         firePropertyChange(PROPERTYNAME_NUMBER, oldNumber, number);
    }

    /**
     * Gets the current value of bigNumber from preferences
     * @return Current value of bigNumber
     */
    public double getBigNumber() {
        return bigNumberPrefAdapter.getDouble();
    }

    /**
     * Sets the value of bigNumber to preferences
     * @param bigNumber New value for bigNumber
     */
    public void setBigNumber(double bigNumber) {
        double oldBigNumber = getBigNumber();
        bigNumberPrefAdapter.setDouble(bigNumber);
        firePropertyChange(PROPERTYNAME_BIGNUMBER, oldBigNumber, bigNumber);
    }

    /**
     * Gets the current value of selectableNumber
     * @return Current value of selectableNumber
     */
    public int getSelectableNumber() {
        return selectableNumber;
    }

    /**
     * Sets the value of selectableNumber
     * @param selectableNumber New value for selectableNumber
     */
    public void setSelectableNumber(int selectableNumber) {
        int oldSelectableNumber = getSelectableNumber();
         this.selectableNumber=selectableNumber;
         firePropertyChange(PROPERTYNAME_SELECTABLENUMBER, oldSelectableNumber, selectableNumber);
    }

    /**
     * Gets the current value of numFloat
     * @return Current value of numFloat
     */
    public float getNumFloat() {
        return numFloat;
    }

    /**
     * Sets the value of numFloat
     * @param numFloat New value for numFloat
     */
    public void setNumFloat(float numFloat) {
        float oldNumFloat = getNumFloat();
         this.numFloat=numFloat;
         firePropertyChange(PROPERTYNAME_NUMFLOAT, oldNumFloat, numFloat);
    }

    /**
     * Gets the current value of stringer
     * @return Current value of stringer
     */
    public String getStringer() {
        return stringer;
    }

    /**
     * Sets the value of stringer
     * @param stringer New value for stringer
     */
    public void setStringer(String stringer) {
        String oldStringer = getStringer();
         this.stringer=stringer;
         firePropertyChange(PROPERTYNAME_STRINGER, oldStringer, stringer);
    }

    /**
     * Gets the current value of characterism
     * @return Current value of characterism
     */
    public char getCharacterism() {
        return characterism;
    }

    /**
     * Sets the value of characterism
     * @param characterism New value for characterism
     */
    public void setCharacterism(char characterism) {
        char oldCharacterism = getCharacterism();
         this.characterism=characterism;
         firePropertyChange(PROPERTYNAME_CHARACTERISM, oldCharacterism, characterism);
    }

    /**
     * Gets the current value of aintItSo
     * @return Current value of aintItSo
     */
    public boolean isAintItSo() {
        return aintItSo;
    }

    /**
     * Sets the value of aintItSo
     * @param aintItSo New value for aintItSo
     */
    public void setAintItSo(boolean aintItSo) {
        boolean oldAintItSo = isAintItSo();
         this.aintItSo=aintItSo;
         firePropertyChange(PROPERTYNAME_AINTITSO, oldAintItSo, aintItSo);
    }

}