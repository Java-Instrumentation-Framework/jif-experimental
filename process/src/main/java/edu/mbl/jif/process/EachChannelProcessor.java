package edu.mbl.jif.process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A process invoked for each channel, at (z,t,p)
 *
 * @author GBH
 */
public abstract class EachChannelProcessor {

	public abstract void process(int c, int z, int t, int p);

	public final Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();
	public final Map<String, Object> data = new ConcurrentHashMap<String, Object>();

	public void addParameter(String key, Object value) {
		parameters.put(key, value);
	}

	public void addData(String key, Object value) {
		data.put(key, value);
	}

}