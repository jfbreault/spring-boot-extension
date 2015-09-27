package com.ticketmaster.boot.logging.hateoas;

import org.springframework.hateoas.Resource;

import com.ticketmaster.boot.logging.Appender;

public class AppenderResource extends Resource<Appender> {

    public AppenderResource(Appender appender) {
        super(appender);
    }

}
