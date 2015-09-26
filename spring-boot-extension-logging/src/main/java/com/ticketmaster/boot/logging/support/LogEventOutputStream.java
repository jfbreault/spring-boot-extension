package com.ticketmaster.boot.logging.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.ticketmaster.boot.logging.LogEvent;
import com.ticketmaster.boot.logging.LogEventRepository;

public class LogEventOutputStream extends ByteArrayOutputStream{

    private LogEventRepository repository;

    public LogEventOutputStream(int queueSyze) {
        super();
        repository = new InMemoryLoggingEventRepository();
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        repository.add(LogEvent.builder().withLog(this.toString()).build());
        this.reset();
    }

    public LogEventRepository getRepository() {
        return repository;
    }

    protected void setRepository(LogEventRepository repository) {
        this.repository = repository;
    }

}
