package com.ticketmaster.boot.logging.hateoas;

import org.springframework.hateoas.Resource;

import com.ticketmaster.boot.logging.Logger;

public class LoggerResource extends Resource<Logger> {

    public LoggerResource(Logger content) {
        super(content);
    }

}
