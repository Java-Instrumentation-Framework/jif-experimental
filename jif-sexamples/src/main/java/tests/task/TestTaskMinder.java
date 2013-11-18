package tests.task;

public class TestTaskMinder {

	public static void main(String[] args) {
		// Create and start a TaskMinder.
		// This TaskMinder checks for things to do every 500 ms
		// and allows a maximum of 100 tasks.
		TaskMinder tm = new TaskMinder(500, 100);
		// Create a Fortune Teller Task.
		FortuneTask fortuneTask = new FortuneTask();
		// Have the Fortune Teller execute every 3 seconds.
		tm.addTask(fortuneTask, 3000);
		// Create a Fibonacci Sequence Task.
		FibonacciTask fibonacciTask = new FibonacciTask();
		// Have the Fibonacci Sequence execute every 700
		// milliseconds.
		tm.addTask(fibonacciTask, 700);
	}

}
