package com.ticketmaster.boot.logging.enpoint.mvc;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ticketmaster.boot.logging.Appender;
import com.ticketmaster.boot.logging.LogEvent;
import com.ticketmaster.boot.logging.Logger;
import com.ticketmaster.boot.logging.builder.LoggerBuilder;
import com.ticketmaster.boot.logging.enpoint.LoggingEndpoint;
import com.ticketmaster.boot.logging.hateoas.AppenderResource;
import com.ticketmaster.boot.logging.hateoas.LoggerResource;

public class LoggingHateoasMvcEndpoint implements MvcEndpoint {

    private final LoggingEndpoint<?> delegate;

    @Autowired
    private ManagementServerProperties management;

    public LoggingHateoasMvcEndpoint(LoggingEndpoint<?> delegate) {
        this.delegate = delegate;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResourceSupport links() {
        ResourceSupport resource = new ResourceSupport();

        resource.add(linkTo(LoggingHateoasMvcEndpoint.class).slash(this.getPath()).withSelfRel());
        resource.add(linkTo(LoggingHateoasMvcEndpoint.class).slash(this.getPath() + "/logger").withRel("logger"));
        resource.add(linkTo(LoggingHateoasMvcEndpoint.class).slash(this.getPath() + "/log").withRel("log"));

        return resource;
    }

    @RequestMapping(value = "/logger", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resources<LoggerResource> getLoggers() {
        List<Logger> loggers = delegate.getLoggers();
        List<LoggerResource> loggerResources = new ArrayList<>();
        for (Logger logger : loggers) {
            LoggerResource loggerResource = new LoggerResource(logger);
            loggerResource.add(linkTo(LoggingHateoasMvcEndpoint.class).slash(this.getPath() + "/logger/" + logger.getName()).withSelfRel());
            loggerResources.add(loggerResource);
        }

        return new Resources<LoggerResource>(loggerResources, linkTo(LoggingHateoasMvcEndpoint.class).slash(this.getPath() + "/logger").withSelfRel());
    }

    @RequestMapping(value = "/logger/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public LoggerResource getLogger(@PathVariable String name) {
        Logger logger = delegate.getLogger(name);
        LoggerResource loggerResource = new LoggerResource(logger);
        loggerResource.add(linkTo(LoggingHateoasMvcEndpoint.class).slash(this.getPath() + "/logger/" + logger.getName()).withSelfRel());

        return loggerResource;
    }

    @RequestMapping(value = "/logger/{name}/{level}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeLevel(@PathVariable String name, @PathVariable String level) {
        delegate.changeLevel(LoggerBuilder.builder().withName(name).withLevel(level).build());
    }

    @RequestMapping(value = "/log", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resources<AppenderResource> getAppenders() {
        List<Appender> appenders = delegate.getAppenders();
        List<AppenderResource> appenderResources = new ArrayList<>();
        for (Appender appender : appenders) {
            AppenderResource appenderResource = new AppenderResource(appender);
            appenderResource.add(linkTo(LoggingHateoasMvcEndpoint.class).slash(this.getPath() + "/log/" + appender.getName()).withSelfRel());
            appenderResources.add(appenderResource);
        }

        return new Resources<AppenderResource>(appenderResources, linkTo(LoggingHateoasMvcEndpoint.class).slash(this.getPath() + "/log").withSelfRel());
    }

    @RequestMapping(value = "/log/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<LogEvent> getLogEvent(@PathVariable String name) {
        return delegate.getLogEvents(name);
    }

    @RequestMapping(value = "/log/{name}/{after}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<LogEvent> getLogEvent(@PathVariable String name, @PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) Date after) {
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
