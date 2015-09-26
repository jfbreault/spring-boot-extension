package com.ticketmaster.boot.logging.appender.logback;

import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.status.ErrorStatus;

import com.ticketmaster.boot.logging.LogEventRepository;
import com.ticketmaster.boot.logging.LogEventRepositoryAccessor;
import com.ticketmaster.boot.logging.support.LogEventOutputStream;

public class MemoryAppender<E> extends OutputStreamAppender<E> implements LogEventRepositoryAccessor  {

    protected LogEventOutputStream outputStream;
    protected int size = 100;

    @Override
    public void start() {
        outputStream = new LogEventOutputStream(size);
        super.setOutputStream(outputStream);
        try {
            super.start();
        } catch (RuntimeException e) {
            started = false;
            addStatus(new ErrorStatus("Failed to initialize encoder for appender named [" + name + "].", this, e));
        }
    }

    @Override
    public LogEventRepository getRepository() {
        return outputStream.getRepository();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
