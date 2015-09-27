package ca.jfbconception.boot.logging.hateoas;

import org.springframework.hateoas.Resource;

import ca.jfbconception.boot.logging.Appender;

public class AppenderResource extends Resource<Appender> {

    public AppenderResource(Appender appender) {
        super(appender);
    }

}
