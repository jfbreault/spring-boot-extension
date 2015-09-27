package ca.jfbconception.boot.logging.hateoas;

import java.util.Collections;

import org.springframework.hateoas.Resources;

public class LinksResource extends Resources<Object> {

    public LinksResource() {
        super(Collections.emptyList());
    }

}
