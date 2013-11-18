package edu.mbl.jif.process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A process invoked for each (z,t,p) - probably a multi-channel calculation
 *
 * @author GBH
 */
public abstract class ChannelSetProcessor {

	public final Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();
	public final Map<String, Object> data = new ConcurrentHashMap<String, Object>();

	public abstract void process(int z, int t, int p);

	public void addParameter(String key, Object value) {
		parameters.put(key, value);
	}

	public void addData(String key, Object value) {
		data.put(key, value);
	}

}
