package ca.jfbconception.boot.logging.hateoas;

import org.springframework.hateoas.Resource;

import ca.jfbconception.boot.logging.Logger;

public class LoggerResource extends Resource<Logger> {

    public LoggerResource(Logger content) {
        super(content);
    }

}
