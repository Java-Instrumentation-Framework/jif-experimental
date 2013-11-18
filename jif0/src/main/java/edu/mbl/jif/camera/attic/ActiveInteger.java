package edu.mbl.jif.camera.attic;

import java.util.Observable;
import java.util.Observer;

// Use:
// ActiveInteger x = new ActiveInteger("prefKey", 0);
// ActiveInteger x = new ActiveInteger(0);

public class ActiveInteger extends Observable {
   int value;
   String prefKey;

   public ActiveInteger(int initialValue) {
	  prefKey = null;
	  value = initialValue;
   }

   public ActiveInteger(String _prefKey, int defaultValue) {
	  this.prefKey = _prefKey;
	  //jif.utils.PSjUtils.getInt(this.prefKey, defaultValue);
   }

   public synchronized void setTo(int _value) {
	  if (_value != this.value) {
		 this.value = _value;
		 if (prefKey != null) {
			//jif.utils.PSjUtils.changepref(prefKey, (float) this.value);
		 }
		 this.setChanged();
		 notifyObservers(new Integer(this.value));
	  }
   }

   public int getValue() {
          return value;
   }

   public void addWatcher(Observer o) {
     this.addObserver(o);
   }

   public void deleteWatcher(Observer o) {
     this.deleteObserver(o);
   }
}

