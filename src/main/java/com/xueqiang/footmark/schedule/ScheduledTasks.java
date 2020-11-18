package com.xueqiang.footmark.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	/**
	 * This example uses fixedRate, which specifies the interval between method invocations,
	 * measured from the start time of each invocation. There are other options,
	 * such as fixedDelay, which specifies the interval between invocations measured from the
	 * completion of the task. You can also use @Scheduled(cron=". . .") expressions for more
	 * sophisticated task scheduling.
	 */
	@Scheduled(fixedRate = 600000) //5000 = 5s
	public void reportCurrentTime() {
		logger.info("The time is now {}", dateFormat.format(new Date()));
	}
}
