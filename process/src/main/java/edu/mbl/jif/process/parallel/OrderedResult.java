package edu.mbl.jif.process.parallel;

import edu.mbl.jif.process.IProcess;

/**
 * Container of results with sequence tag for ordering...
 *
 * @author GBH
 */
import java.util.concurrent.Callable;

public class OrderedResult implements Callable {

	private int seq;
	private IProcess process;

	public OrderedResult(final int seq, final IProcess process) {
		this.seq = seq;
		this.process = process;
	}

	public int getSeq() {
		return seq;
	}

	public IProcess getProcess() {
		return process;
	}

	public OrderedResult call() {
		long begTest = new java.util.Date().getTime();
//		System.out.println("      Called " + seq);
//		System.out.flush();
		process.process();
		return this;
	}

}
