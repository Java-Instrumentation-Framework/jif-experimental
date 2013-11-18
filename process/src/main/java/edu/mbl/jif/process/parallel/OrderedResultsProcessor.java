package edu.mbl.jif.process.parallel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OrderedResultsProcessor - releases results in sequential order 
 * to the ResultProcessor assigned
 * 
 * @author GBH
 */
public class OrderedResultsProcessor extends Thread {

	private int next = 0;
	private ResultProcessor resultProcessor;
	volatile boolean stillProcessing = true;
	private Map<Integer, OrderedResult> waiting = 	new ConcurrentHashMap<Integer, OrderedResult>();
	private boolean cancelled = false;

	public OrderedResultsProcessor(ResultProcessor processor) {
		this.resultProcessor = processor;
	}

	public void cancel() {
		cancelled = true;
	}
	
	void setStillProcessing(boolean still) {
		stillProcessing = still;
	}

	public void addToWaitingResults(OrderedResult result) {
		waiting.put(result.getSeq(), result);
	}

	public int getNumWaiting() {
		return waiting.size();
	}
	
	@Override
	public void run() {
		while (stillProcessing || !waiting.isEmpty()) {
			// wait for next to appear
			OrderedResult result = waiting.get(next);
			if (result != null) {
				resultProcessor.processResult(result);
				waiting.remove(next);
//				System.out.println("                                          " +
//						"Waiting: " + waiting.size() + ", next: " + next);
				next++;
			}
			if(cancelled) {
				resultProcessor.cancel();  //???
				break;
			}
		}
		resultProcessor.complete();
		System.out.println("Done. ------- OrderedResultsProcessor stopped.");
	}

}
