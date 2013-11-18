package tests.concurrency;

import java.util.concurrent.Callable;

public class FictionalEmailSender implements Callable<Boolean> {

	public FictionalEmailSender(String to, String subject, String body) {
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	@Override
	public Boolean call() throws InterruptedException {
		// Simulate that sending the email takes between 0 and 0.5 seconds
		Thread.sleep(Math.round(Math.random() * 0.5 * 1000));

		// Lets say we have an 80% chance of successfully sending our email
		if (Math.random() > 0.2) {
			return true;
		} else {
			return false;
		}
	}

	private String to;
	private String subject;
	private String body;
}