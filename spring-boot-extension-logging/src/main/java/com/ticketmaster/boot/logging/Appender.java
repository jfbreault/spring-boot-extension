package com.ticketmaster.boot.logging;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Appender {

    @JsonCreator
    public Appender() {
    }

    public Appender(String name) {
        super();
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
