package ca.jfbconception.boot.logging.builder;

import ca.jfbconception.boot.logging.Logger;

public class LoggerBuilder {

    public static LoggerBuilder builder() {
        return new LoggerBuilder();
    }

    private String name;

    private String level;

    protected LoggerBuilder() {
    }

    public LoggerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LoggerBuilder withLevel(String level) {
        this.level = level;
        return this;
    }

    public Logger build() {
        Logger logger = new Logger();
        logger.setLevel(level);
        logger.setName(name);
        return logger;
    }

}
