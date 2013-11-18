package edu.mbl.jif.varilc.multi;

public class Retarder {
    public final static int CELL_0_DEG = 0;
    public final static int CELL_45_DEG = 1;
    public final static int ATTENUATOR = 2;
		// Todo: add another type (e.g. for Abrio 'C' retarder...)
    //
    public int type;
    public float value;
    public float currentValue;
    public float min;
    public float max;
    public boolean toChange = false;
    
    // A single variable retarder or attenuator
    
    public Retarder(int _type,
            float _value, float _min, float _max) {
        type = _type;
        value = _value;
        currentValue = value;
        min = _min;
        max = _max;
    }
    
    
    public void setTo(float set) {
        value = set;
        toChange = true;
    }
    
    
    public void confirmUpdated() {
        currentValue = value;
        toChange = false;
    }
    
    
    public float getValue() {
        return currentValue; }
    
    
    public float getMax() {
        return max; }
    
    
    public float getMin() {
        return min; }
    
    
    public boolean isToChange() {
        return toChange; }
    
    
    public int getType() {
        return type; }
    
    
}
