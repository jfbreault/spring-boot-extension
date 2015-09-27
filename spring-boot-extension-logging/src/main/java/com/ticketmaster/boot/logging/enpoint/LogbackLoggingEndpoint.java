package com.ticketmaster.boot.logging.enpoint;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

import com.ticketmaster.boot.logging.LogEvent;
import com.ticketmaster.boot.logging.LogEventRepositorySupplier;
import com.ticketmaster.boot.logging.Logger;
import com.ticketmaster.boot.logging.builder.logback.LogbackLoggerBuilder;

public class LogbackLoggingEndpoint extends BaseLoggingEndpoint<Object> implements LoggingEndpoint<Object> {
    
    public LogbackLoggingEndpoint(boolean sensitive) {
        super(sensitive);
    }

    public LogbackLoggingEndpoint(boolean sensitive, boolean enabled) {
        super(sensitive, enabled);
    }

    @Override
    public List<Logger> getLoggers() {
        return getLoggerContext().getLoggerList().stream().map(logger -> LogbackLoggerBuilder.builder().withLogger(logger).build())
                .collect(Collectors.toList());
    }

    @Override
    public Logger getLogger(String name) {
        return LogbackLoggerBuilder.builder().withLogger(getLoggerContext().getLogger(name)).build();
    }

    public Appender<ILoggingEvent> getAppender(String name) {

        for (ch.qos.logback.classic.Logger logger : getLoggerContext().getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext();) {
                Appender<ILoggingEvent> appender = index.next();
                if (appender.getName().equals(name)) {
                    return appender;
                }
            }
        }

        return null;

    }

    @Override
    public List<LogEvent> getLogEvents(String name) {

        Appender<ILoggingEvent> appender = getAppender(name);
        if (appender != null) {
            if (appender instanceof LogEventRepositorySupplier) {
                return ((LogEventRepositorySupplier) appender).getRepository().findAll();
            }
        }

        return Collections.emptyList();

    }

    @Override
    public List<LogEvent> getLogEvents(String name, Date after) {

        Appender<ILoggingEvent> appender = getAppender(name);
        if (appender != null) {
            if (appender instanceof LogEventRepositorySupplier) {
                return ((LogEventRepositorySupplier) appender).getRepository().find(after);
            }
        }

        return Collections.emptyList();

    }

    @Override
    public List<com.ticketmaster.boot.logging.Appender> getAppenders() {

        Set<String> appenderName = new HashSet<>();

        for (ch.qos.logback.classic.Logger logger : getLoggerContext().getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext();) {
                Appender<ILoggingEvent> appender = index.next();
                if (appender instanceof LogEventRepositorySupplier) {
                    appenderName.add(appender.getName());
                }
            }
        }

        return appenderName.stream().map(com.ticketmaster.boot.logging.Appender::new).collect(Collectors.toList());

    }

    LoggerContext getLoggerContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    @Override
    public void changeLevel(Logger logger) {
        LoggerContext context = getLoggerContext();
        context.getLogger(logger.getName()).setLevel(Level.valueOf(logger.getLevel()));
    }

    @Override
    public List<Logger> invoke() {
        return getLoggers();
    }

}
