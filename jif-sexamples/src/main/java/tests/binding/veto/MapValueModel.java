/*
 * From: http://aneesjavaee-osgi.blogspot.com/2008/10/how-to-bind-map-with-jgoodies-binding.html
 */
package tests.binding.veto;

import com.jgoodies.binding.value.AbstractValueModel;
import java.util.Map;

/**
 * This is the Map Based Value model which will replace the Bean's ValueModel given my JGoodies By
 * using this model we can avoid the usage of many different kind of domain object instead just use
 * Map/HashMap which will hold the key-value. It will help us to standardizing the UI
 *
 * @author Anees
 * 
*/
public class MapValueModel extends AbstractValueModel {

	private static final long serialVersionUID = 1L;
	protected Map map;
	protected Object key;

	/**
	 *
	 * @param map
	 * @param key
	 */
	public MapValueModel(Map map, Object key) {
		this.key = key;
		this.map = map;
	}

	/**
	 *
	 */
	public MapValueModel() {
	}

	/**
	 *
	 * @return
	 */
	public Object getKey() {
		return key;
	}

	/**
	 *
	 * @param key
	 */
	public void setKey(Object key) {
		Object oldKey = this.key;
		this.key = key;
		firePropertyChange("key", oldKey, key);
	}

	/**
	 * Retrieve the value.
	 *	 
* @return the value.
	 */
	public Object getValue() {
		return map.get(key);
	}

	/**
	 * Set the value.
	 *	 
* @param newValue the new value.
	 */
	public void setValue(Object newValue) {
		Object oldValue = map.get(key);
		map.put(key, newValue);
		fireValueChange(oldValue, newValue);
	}

	/**
	 *
	 * @param map
	 */
	public void setMap(Map map) {
		Map oldMap = this.map;
		Object oldValue = oldMap.get(key);
		this.map = map;
		firePropertyChange("map", oldMap, map);
		fireValueChange(oldValue, getValue());
	}

}
