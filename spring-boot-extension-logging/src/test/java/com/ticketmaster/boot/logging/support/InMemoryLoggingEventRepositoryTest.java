package com.ticketmaster.boot.logging.support;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ticketmaster.boot.logging.LogEvent;

public class InMemoryLoggingEventRepositoryTest {

    private InMemoryLoggingEventRepository repository = new InMemoryLoggingEventRepository();

    @Test
    public void repositoryShouldReturnInOrder() {
        this.repository.setCapacity(2);
        this.repository.add(new LogEvent("bar"));
        this.repository.add(new LogEvent("foo"));
        List<LogEvent> logEvents = this.repository.findAll();
        assertEquals(2, logEvents.size());
        assertEquals("bar", logEvents.get(0).getLog());
        assertEquals("foo", logEvents.get(1).getLog());
    }
    
    @Test
    public void repositoryShouldReturnTheLastEntryWhenLimitIsReached() {
        this.repository.setCapacity(2);
        this.repository.add(new LogEvent("bar"));
        this.repository.add(new LogEvent("foo"));
        this.repository.add(new LogEvent("foo"));
        List<LogEvent> logEvents = this.repository.findAll();
        assertEquals(2, logEvents.size());
        assertEquals("foo", logEvents.get(0).getLog());
        assertEquals("foo", logEvents.get(1).getLog());
    }
    
    @Test
    public void findWithNullDateShouldReturnAll() {
        this.repository.setCapacity(2);
        this.repository.add(new LogEvent("bar"));
        this.repository.add(new LogEvent("foo"));
        List<LogEvent> logEvents = this.repository.find(null);
        assertEquals(2, logEvents.size());
        assertEquals("bar", logEvents.get(0).getLog());
        assertEquals("foo", logEvents.get(1).getLog());
    }
    
    @Test
    public void findWithDateShouldReturnFound() throws InterruptedException {
        this.repository.setCapacity(3);
        this.repository.add(new LogEvent("bar"));
        Thread.sleep(5);
        Date date = new Date();
        this.repository.add(new LogEvent("foo"));
        this.repository.add(new LogEvent("foo"));
        List<LogEvent> logEvents = this.repository.find(date);
        assertEquals(2, logEvents.size());
        assertEquals("foo", logEvents.get(0).getLog());
        assertEquals("foo", logEvents.get(1).getLog());
    }

}
