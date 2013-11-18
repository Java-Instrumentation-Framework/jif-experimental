package tests.guava;

// From https://github.com/stuartgunter/guava-demo/tree/master/src/main/java/org/stuartgunter/demo/guava/concurrent


//import javax.annotation.Nullable;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: gunters Created: 05/12/2011 10:35
 */
public class ListenableFutureTest {

	public static void main(String... args) throws Exception {
		final ExecutorService executorService = Executors.newFixedThreadPool(1);

		ListenableFutureTest app = new ListenableFutureTest(executorService);
		app.runWithListener();
		//app.runWithCallback();
		//app.runWithChain();

		app.stop();
	}

	private final ListeningExecutorService listeningExecutorService;

	public ListenableFutureTest(ExecutorService executorService) {
		this.listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
	}

	public void stop() {
		listeningExecutorService.shutdown();
	}

	public void runWithListener() throws Exception {
		// calling addListener directly
		ListenableFuture<String> future1 = listeningExecutorService.submit(new SleepingCallable("Hello", 2000));
		future1.addListener(new Runnable() {
			public void run() {
				System.out.println(Thread.currentThread().getName() + ": Listener: DONE");
			}

		}, listeningExecutorService);

		System.out.println(Thread.currentThread().getName() + ": Future1: " + future1.get());
	}

	public void runWithCallback() throws Exception {
		ListenableFuture<String> future2 = listeningExecutorService.submit(new SleepingCallable("Hello", 2000));
		Futures.addCallback(future2,
				new FutureCallback<String>() {
			public void onSuccess(String result) {
				System.out.println(Thread.currentThread().getName() + ": FutureCallback: SUCCESS - " + result);
			}

			public void onFailure(Throwable t) {
				System.out.println(Thread.currentThread().getName() + ": FutureCallback: FAILURE - " + t.getMessage());
			}

		});

		System.out.println(Thread.currentThread().getName() + ": Future2: " + future2.get());
	}

//	public void runWithChain() throws Exception {
//		ListenableFuture<String> future3 = listeningExecutorService.submit(new SleepingCallable("Hello", 2000));
//		String future4 = Futures.transform(
//				future3,
//				new Function<String, String>() {
//			public String apply(String input) {
//				return listeningExecutorService.submit(new SleepingCallable(input + " World", 2000));
//			}
//
//		});
//
//		System.out.println(Thread.currentThread().getName() + ": Future4: " + future4.get());
//	}

}