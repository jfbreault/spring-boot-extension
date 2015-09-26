package com.ticketmaster.boot.logging;

import java.util.Date;

public class LogEvent {

    private final String log;
    private final Date date;

    public static LogEventBuilder builder() {
        return new LogEventBuilder();
    }

    public static class LogEventBuilder {
        private LogEventBuilder() {
        }

        private String log;

        public LogEventBuilder withLog(String log) {
            this.log = log;
            return this;
        }

        public LogEvent build() {
            return new LogEvent(log);
        }
    }

    public LogEvent(String log) {
        super();
        this.log = log;
        this.date = new Date();
    }

    public String getLog() {
        return log;
    }

    public Date getDate() {
        return date;
    }
    
    @Override
    public String toString() {
        return "LogEvent [log=" + log + ", date=" + date + "]";
    }

}
