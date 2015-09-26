package com.ticketmaster.boot.logging.builder.logback;

import com.ticketmaster.boot.logging.builder.LoggerBuilder;

import ch.qos.logback.classic.Logger;

public class LogbackLoggerBuilder extends LoggerBuilder {

    public static LogbackLoggerBuilder builder() {
        return new LogbackLoggerBuilder();
    }

    public LogbackLoggerBuilder withLogger(Logger logger) {
        this.withName(logger.getName());
        this.withLevel(logger.getEffectiveLevel().toString());
        return this;
    }

    private LogbackLoggerBuilder() {
        super();
    }
}
