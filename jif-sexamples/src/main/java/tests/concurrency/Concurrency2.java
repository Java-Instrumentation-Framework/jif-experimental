/*
 * 
 * From http://ricardozuasti.com/2012/java-concurrency-examples-getting-feedback-from-concurrent-tasks/
 */
package tests.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Concurrency2 {

	public static void main(String[] args) {
		try {
			ThreadPoolExecutor executor = new ThreadPoolExecutor(30, 30, 1, TimeUnit.SECONDS,
					new LinkedBlockingQueue());

			List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>(9000);

			// Lets spam every 4 digit numeric user on that silly domain
			for (int i = 1000; i < 10000; i++) {
				futures.add(executor.submit(new FictionalEmailSender(i + "@wesellnumericusers.com",
						"Knock, knock, Neo", "The Matrix has you...")));
			}

			// All tasks have been submitted, wen can begin the shutdown of our executor
			System.out.println("Starting shutdown...");
			executor.shutdown();

			// Every second we print our progress
			while (!executor.isTerminated()) {
				executor.awaitTermination(1, TimeUnit.SECONDS);
				int progress = Math.round((executor.getCompletedTaskCount() * 100)
						/ executor.getTaskCount());

				System.out.println(progress + "% done (" + executor.getCompletedTaskCount()
						+ " emails have been sent).");
			}

			// Now that we are finished sending all the emails, we can review the futures
			// and see how many were successfully sent
			int errorCount = 0;
			int successCount = 0;
			for (Future<Boolean> future : futures) {
				if (future.get()) {
					successCount++;
				} else {
					errorCount++;
				}
			}

			System.out.println(successCount + " emails were successfully sent, but "
					+ errorCount + " failed.");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}