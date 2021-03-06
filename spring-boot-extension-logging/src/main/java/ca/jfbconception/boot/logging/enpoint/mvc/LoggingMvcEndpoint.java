package ca.jfbconception.boot.logging.enpoint.mvc;

import java.util.Date;
import java.util.List;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ca.jfbconception.boot.logging.Appender;
import ca.jfbconception.boot.logging.LogEvent;
import ca.jfbconception.boot.logging.Logger;
import ca.jfbconception.boot.logging.builder.LoggerBuilder;
import ca.jfbconception.boot.logging.enpoint.LoggingEndpoint;

public class LoggingMvcEndpoint implements MvcEndpoint {

    private final LoggingEndpoint<?> delegate;

    public LoggingMvcEndpoint(LoggingEndpoint<?> delegate) {
        this.delegate = delegate;
    }

    @RequestMapping(value = "/logger", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Logger> getLoggers() {
        return delegate.getLoggers();
    }

    @RequestMapping(value = "/logger/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Logger getLogger(@PathVariable String name) {
        return delegate.getLogger(name);
    }

    @RequestMapping(value = "/logger/{name}/{level}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeLevel(@PathVariable String name, @PathVariable String level) {
        delegate.changeLevel(LoggerBuilder.builder().withName(name).withLevel(level).build());
    }

    @RequestMapping(value = "/log", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Appender> getAppenders() {
        return delegate.getAppenders();
    }

    @RequestMapping(value = "/log/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<LogEvent> getLogEvent(@PathVariable String name) {
        return delegate.getLogEvents(name);
    }

    @RequestMapping(value = "/log/{name}/{after}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<LogEvent> getLogEvent(@PathVariable String name, @PathVariable Date after) {
        return delegate.getLogEvents(name, after);
    }

    @Override
    public String getPath() {
        return "/" + this.delegate.getId();
    }

    @Override
    public boolean isSensitive() {
        return delegate.isSensitive();
    }

    @Override
    public Class<? extends Endpoint> getEndpointType() {
        return delegate.getClass();
    }

}
