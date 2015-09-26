package com.ticketmaster.boot.logging.enpoint;

import java.util.Date;
import java.util.List;

import org.springframework.boot.actuate.endpoint.Endpoint;

import com.ticketmaster.boot.logging.Appender;
import com.ticketmaster.boot.logging.LogEvent;
import com.ticketmaster.boot.logging.Logger;

public interface LoggingEndpoint<T> extends Endpoint<T>{

    List<Logger> getLoggers();
    
    Logger getLogger(String name);

    List<LogEvent> getLogEvents(String name);

    List<LogEvent> getLogEvents(String name, Date after);

    List<Appender> getAppenders();

    void changeLevel(Logger logger);

}