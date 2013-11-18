package edu.mbl.jif.process.parallel;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 *
 * @author GBH
 */
public class TaskSubmitter {

	private CompletionService< OrderedResult> cservice;
	private int submitted = 0;
	private boolean complete = false;
	private int taken = 0;

	public int getSubmitted() {
		return submitted;
	}

	void setTaken(final int taken) {
		this.taken = taken;
	}

	public int getTaken() {
		return taken;
	}

	public TaskSubmitter(CompletionService<OrderedResult> cservice) {
		this.cservice = cservice;
	}

	public void submit(OrderedResult task) {
		Future<OrderedResult> future = cservice.submit(task);
		//System.out.println("Submit: " + task.getSeq());
		submitted++;
	}

	public void setComplete() {
		complete = true;

	}

	public boolean isComplete() {
		return complete;
	}

}
