package binding;

import binding.MMgrPropertiesAdapter;
import com.jgoodies.binding.beans.Model;
import java.util.prefs.Preferences;

/**
 *
 * @author GBH
 */
public class PrefsBindingTest extends Model {

    Preferences prefs = Preferences.userRoot();
        
    public static final String PROPERTYNAME_PROP_ = "_prop_";
    private boolean _prop_;
    
    static final Boolean _prop_DEFAULT = Boolean.TRUE;
    MMgrPropertiesAdapter _prop_PrefAdapter = new MMgrPropertiesAdapter(prefs, PROPERTYNAME_PROP_, _prop_DEFAULT);
    
    
    public PrefsBindingTest() {
    }
    
    public static void main(String[] args) {
        PrefsBindingTest pbt = new PrefsBindingTest();
        pbt.get_Prop_();
        pbt.set_Prop_(false);
        //JCheckBox box = BasicComponentFactory.createCheckBox(_prop_PrefAdapter, "Boolean Value");
    }
    
    public boolean get_Prop_() {
        return _prop_PrefAdapter.getBoolean();
    }
    
    public void set_Prop_(boolean newValue) {
        boolean old_Prop_ = get_Prop_();
        _prop_ = newValue;
        _prop_PrefAdapter.setBoolean(newValue);
        firePropertyChange(PROPERTYNAME_PROP_, old_Prop_, newValue);
    }
}


