package edu.mbl.jif.camera.attic;

import java.util.*;

// ActiveDouble x = new ActiveDouble("propKey", 0);
// ActiveDouble x = new ActiveDouble(0);

public class ActiveDouble extends Observable {
   double value;
   String propKey;

   public ActiveDouble(double initialValue) {
	  propKey = null;
	  value = initialValue;
   }

   public ActiveDouble(String _propKey, double defaultValue) {
	  this.propKey = _propKey;
	  //jif.utils.PSjUtils.getDouble(this.propKey, defaultValue);
   }

   public synchronized void setTo(double _value) {
	  if (_value != this.value) {
		 this.value = _value;
		 if (propKey != null) {
			//jif.utils.PSjUtils.changeProp(propKey, (float) this.value);
		 }
		 this.setChanged();
		 notifyObservers(new Double(this.value));
	  }
   }

   public double getValue() {
	  return value;
   }
}
