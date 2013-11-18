package edu.mbl.jif.process.parallel;

import edu.mbl.jif.process.parallel.listen.CompletionListener;
import edu.mbl.jif.process.parallel.test.TestResultProcessor;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * OrderedParallelProcessManager To run a batch of tasks in parallel (on multi-core) Pass in:
 * ResultProcessor and Number of threads, which if 0, use all available processors/cores
 *
 * @author GBH
 */
public class OrderedParallelProcessManager {

	private ExecutorService eservice;
	private int numThreads;
	private CompletionService< OrderedResult> cservice;
	private TaskSubmitter submitter;
	private OrderedResultsProcessor orderer;
	private TaskTaker taker;
	private ResultProcessor resultProcessor;   // what to do with in-order result.

	// Check necessary memory as needed, given parallel processes...
	// ?? how to specify required
	public OrderedParallelProcessManager(ResultProcessor resultProcessor) {
		this(resultProcessor, 0);
	}

	public OrderedParallelProcessManager(ResultProcessor resultProcessor, int numThreads) {
		this.resultProcessor = resultProcessor;
		if (numThreads == 0) { // use all available cores
			this.numThreads = Runtime.getRuntime().availableProcessors();
		} else {
			this.numThreads = numThreads;
		}
		eservice = Executors.newFixedThreadPool(this.numThreads);
		cservice = new ExecutorCompletionService< OrderedResult>(eservice);
		orderer = new OrderedResultsProcessor(this.resultProcessor);  // releases results in order
		submitter = new TaskSubmitter(cservice);
		taker = new TaskTaker(submitter, cservice, orderer);
		taker.start();
		orderer.start();
	}

	public void submit(OrderedResult result) {
		
		// System.out.println("(submitter.getSubmitted() - submitter.getTaken())" + 
		//		(submitter.getSubmitted() - submitter.getTaken()));
		
		// throttle the submission...
		while ((submitter.getSubmitted() - taker.getTaken()) > numThreads) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
			}
		}
		submitter.submit(result);
	}

	public void submissionsComplete() {
		// When complete (all tasks submitted)
		submitter.setComplete();
		System.out.println(" ---------------------------------------------------- submissions Completed");
		eservice.shutdown();
	}

	public void cancelAll() {
		submitter.setComplete();
		taker.cancel();
		orderer.cancel();
		eservice.shutdownNow();
	}

   public void addCompletionListener(CompletionListener listener) {
      taker.addCompletionListener(listener);
   }
   
	// for testing...
	public static void main(String[] args) {
		// create DatasetGenerator...
		ResultProcessor resultProcessor = new TestResultProcessor(null, null);
		OrderedParallelProcessManager oppm = new OrderedParallelProcessManager(resultProcessor);
		for (int i = 0; i < 10; i++) {
			// for each iteration
			oppm.submit(new OrderedResult(i, null));
		}
		oppm.submissionsComplete();
	}

}
