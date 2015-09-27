package ca.jfbconception.boot.logging.enpoint;

import java.util.Date;
import java.util.List;

import org.springframework.boot.actuate.endpoint.Endpoint;

import ca.jfbconception.boot.logging.Appender;
import ca.jfbconception.boot.logging.LogEvent;
import ca.jfbconception.boot.logging.Logger;

public interface LoggingEndpoint<T> extends Endpoint<T>{

    List<Logger> getLoggers();
    
    Logger getLogger(String name);

    List<LogEvent> getLogEvents(String name);

    List<LogEvent> getLogEvents(String name, Date after);

    List<Appender> getAppenders();

    void changeLevel(Logger logger);

}