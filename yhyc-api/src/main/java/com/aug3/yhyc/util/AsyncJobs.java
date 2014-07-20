package com.aug3.yhyc.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncJobs {

	private static ExecutorService executor = Executors.newCachedThreadPool();

	public static void submit(Runnable job) {
		executor.submit(job);
	}

}
