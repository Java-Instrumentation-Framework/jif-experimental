/*
 * Created on Jun 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.html;
/**
 * @author Amit Tuli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Log {

	private String testcaseName = null;
	private int totalVariations = 0;
	private int totalRan = 0;
	private int totalPassed = 0;
	private int totalFailed = 0;
	private String result = null;
	private String logFile = null;
	/**
	 * @return
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @return
	 */
	public String getTestcaseName() {
		return testcaseName;
	}

	/**
	 * @return
	 */
	public int getTotalFailed() {
		return totalFailed;
	}

	/**
	 * @return
	 */
	public int getTotalPassed() {
		return totalPassed;
	}

	/**
	 * @return
	 */
	public int getTotalRan() {
		return totalRan;
	}

	/**
	 * @return
	 */
	public int getTotalVariations() {
		return totalVariations;
	}

	/**
	 * @param string
	 */
	public void setResult(String string) {
		result = string;
	}

	/**
	 * @param string
	 */
	public void setTestcaseName(String string) {
		testcaseName = string;
	}

	/**
	 * @param i
	 */
	public void setTotalFailed(int i) {
		totalFailed = i;
	}

	/**
	 * @param i
	 */
	public void setTotalPassed(int i) {
		totalPassed = i;
	}

	/**
	 * @param i
	 */
	public void setTotalRan(int i) {
		totalRan = i;
	}

	/**
	 * @param i
	 */
	public void setTotalVariations(int i) {
		totalVariations = i;
	}

	/**
	 * @return
	 */
	public String getLogFile() {
		return logFile;
	}

	/**
	 * @param string
	 */
	public void setLogFile(String string) {
		logFile = string;
	}

}
