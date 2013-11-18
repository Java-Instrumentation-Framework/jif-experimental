package edu.mbl.jif.process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract Image Processor
 *
 * @author GBH
 */
public abstract class AbstractProcess<T> implements IProcess<T> {

	protected Map inputParameters;
	protected Map inputData;
	protected Map outputParameters = new ConcurrentHashMap();
	protected Map outputData = new ConcurrentHashMap();

	public AbstractProcess() {}
        
	public AbstractProcess(Map inputParameters, Map inputData) {
		this.inputParameters = inputParameters;
		this.inputData = inputData;
	}

	// Set Inputs...
	@Override
	public void setInputParameters(Map inputParameters) {
		this.inputParameters = inputParameters;
	}

	@Override
	public Map getInputParameters() {
		return inputParameters;
	}

	@Override
	public Object getInputParameter(String key) {
		return inputParameters.get(key);
	}

	@Override
	public void setInputData(Map data) {
		this.inputData = data;
	}

	@Override
	public Map getInputData() {
		return inputData;
	}
	
@Override
	public Object getInputDatum(String key) {
		return inputData.get(key);
	}
	//
	// Get Outputs... 
	protected void addOutputParameter(Object key, Object obj) {
		outputParameters.put(key, obj);
	}

	@Override
	public Map getOutputParameters() {
		return outputParameters;
	}

	protected void addOutputData(Object key, Object obj) {
		outputData.put(key, obj);
	}

	@Override
	public Map getOutputData() {
		return outputData;
	}

}
