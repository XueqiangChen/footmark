package com.example.footmark.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtils {
	protected AtomicInteger nextId = new AtomicInteger();

	public Thread newThread(String namePrefix, Runnable r) {
		Thread thread = new Thread(r, namePrefix + '-' + nextId.getAndIncrement());
		thread.setDaemon(false);
		thread.setPriority(Thread.NORM_PRIORITY);
		return thread;
	}

	public static ThreadFactory createThreadFactory(String namePrefix) {
		return new ThreadFactoryBuilder().setNameFormat(namePrefix + "-%d").setDaemon(false)
				.setPriority(Thread.NORM_PRIORITY).build();
	}

	public static ThreadFactory createThreadFactory(String namePrefix, boolean daemon, int priority) {
		return new ThreadFactoryBuilder().setNameFormat(namePrefix + "-%d").setDaemon(daemon).setPriority(priority)
				.build();
	}
}
