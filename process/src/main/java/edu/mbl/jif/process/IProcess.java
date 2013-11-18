package edu.mbl.jif.process;

import java.util.Map;

/**
 * Image Processor Interface
 *
 * @author GBH
 */
public interface IProcess<T> {

	String getName();

	void setInputParameters(Map inParameters);

	Map getInputParameters();

	Object getInputParameter(String key);

	void setInputData(Map data);

	Map getInputData();

	Object getInputDatum(String key);

	void process();
      
      void cancel();

	Map getOutputParameters();

	Map getOutputData();

}
