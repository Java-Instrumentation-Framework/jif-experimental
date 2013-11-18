/*
 * from http://www.jroller.com/santhosh/date/20050526
 */
package edu.mbl.jif.gui.error;

/**
 *
 * @author GBH
 */
public class EDTExceptionHandler {

	static {
		// see the documentation of the private method handleException of java.awt.EventDispatchThread 
		System.setProperty("sun.awt.exception.handler", EDTExceptionHandler.class.getName());
	}

	public static void handle(Throwable thr) {
		// log to file or do whatever you want 
	}

	public static void main(String[] args) {
		try {
			Class.forName("santhosh.blog.EDTExceptionHandler");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// ..... 
	}

}