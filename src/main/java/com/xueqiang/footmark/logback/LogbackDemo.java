package com.xueqiang.footmark.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LogbackDemo {

    // 两种方法都能拿到slf4j 的实例，建议用类名，好读一些
    private static final Logger logger = LoggerFactory.getLogger(LogbackDemo.class);
//    private static final Logger logger = LoggerFactory.getLogger("com.xueqiang.footmark.logback.LogbackUtils");

    // get a logger instance named "com.foo". Let us further assume that the
    // logger is of type  ch.qos.logback.classic.Logger so that we can
    // set its level
    private ch.qos.logback.classic.Logger logger2 =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.xueqiang.footmark");

    public void patternLayout() {
        logger.debug("Hello world.");

        // Only after evaluating whether to log or not, and only if the decision is positive,
        // will the logger implementation format the message and replace the '{}' pair with the string value of entry.
        // In other words, this form does not incur the cost of parameter construction when the log statement is disabled.
        List<String> entry = new ArrayList<>();
        entry.add("abc");
        logger.debug("The new entry is {}.", entry);

        // A two argument variant is also available
        String oldEntry = "efg";
        logger.debug("The new entry is {}. It replaces {}.", entry, oldEntry);

        // If three or more arguments need to be passed, an Object[] variant is also available.
        String newVal = "newVal";
        String below = "below";
        String above = "above";
        Object[] paramArray = {newVal, below, above};
        logger.debug("Value {} was inserted between {} and {}.", paramArray);
    }

    // Printing Logger Status
    public void printLoggerStatus() {
        // print internal state
        // it configured itself using its default policy, which is a basic 'ConsoleAppender'
        // Logback's internal status information can be very useful in diagnosing logback-related problems.
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }

    // This rule is at the heart of logback. It assumes that levels are ordered as follows:
    // TRACE < DEBUG < INFO <  WARN < ERROR.
    public void setBasicSelectionRule() {
        // set its Level to INFO. The setLevel() method requires a logback logger
        logger2.setLevel(Level.INFO);
        // This request is enabled, because WARN >= INFO
        logger2.warn("Low fuel level.");
        // This request is disabled, because DEBUG < INFO.
        logger2.debug("Starting search for nearest gas station.");

        // The logger instance barlogger, named "com.foo.Bar",
        // will inherit its level from the logger named
        // "com.foo" Thus, the following request is enabled
        // because INFO >= INFO.
        logger.info("Located nearest gas station.");

        // This request is disabled, because DEBUG < INFO.
        logger.debug("Exiting gas station search");
    }

    public static void main(String[] args) {
        LogbackDemo utils = new LogbackDemo();
        utils.patternLayout();
        utils.printLoggerStatus();
        utils.setBasicSelectionRule();
    }
}
