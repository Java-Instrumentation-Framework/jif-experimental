
package edu.mbl.jif.process.parallel;

/**
 * ResultProcessor processes/handles the OrderedResults.
 * 
 * @author GBH
 */
public interface ResultProcessor {
	
	void processResult(OrderedResult result);

	// Call when there are no more results to handle
	public void complete();
	
	public void cancel();
	
}
