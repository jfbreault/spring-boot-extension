package ca.jfbconception.boot.logging.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ca.jfbconception.boot.logging.LogEvent;
import ca.jfbconception.boot.logging.LogEventRepository;

public class LogEventOutputStream extends ByteArrayOutputStream{

    private LogEventRepository repository;

    public LogEventOutputStream(int queueSyze) {
        super();
        repository = createLogEventRepository(queueSyze);
    }

    protected InMemoryLoggingEventRepository createLogEventRepository(int queueSyze) {
        return new InMemoryLoggingEventRepository(queueSyze);
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
