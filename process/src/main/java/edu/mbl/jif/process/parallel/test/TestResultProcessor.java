package edu.mbl.jif.process.parallel.test;

import edu.mbl.jif.imaging.dataset.MMgrDatasetAccessor;
import edu.mbl.jif.imaging.dataset.MMgrDatasetGenerator;
import edu.mbl.jif.process.parallel.OrderedResult;
import edu.mbl.jif.process.parallel.ResultProcessor;

/**
 *
 * @author GBH
 */
public class TestResultProcessor implements ResultProcessor {

	private MMgrDatasetGenerator mdg;
	private MMgrDatasetAccessor mda;

	public TestResultProcessor( MMgrDatasetAccessor mda, MMgrDatasetGenerator mdg) {
		this.mdg = mdg;
		this.mda = mda;
	}

	@Override
	public void processResult(OrderedResult result) {
		// check result
		System.out.println("processResult: " + result.getSeq());
		System.out.println("");
		result.getProcess().getOutputParameters();
		
	}

	@Override
	public void complete() {
		mda.close();
		mdg.stop();
	}

	@Override
	public void cancel() {
		mda.close();
		mdg.cancel();
	}
}