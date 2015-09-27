/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ticketmaster.boot.logging.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.ticketmaster.boot.logging.LogEvent;
import com.ticketmaster.boot.logging.LogEventRepository;

/**
 * In-memory implementation of {@link TraceRepository}.
 *
 * @author Dave Syer
 * @author Olivier Bourgain
 */
public class InMemoryLoggingEventRepository implements LogEventRepository {

    private int capacity = 1000;

    private final List<LogEvent> logEvents = new LinkedList<LogEvent>();
    
    public InMemoryLoggingEventRepository(){}
    
    public InMemoryLoggingEventRepository(int capacity){
        this.capacity = capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        synchronized (this.logEvents) {
            this.capacity = capacity;
        }
    }

    @Override
    public List<LogEvent> findAll() {
        List<LogEvent> events;
        synchronized (this.logEvents) {
            events = new ArrayList<LogEvent>(this.logEvents);
        }
        return Collections.unmodifiableList(events);
    }

    @Override
    public void add(LogEvent logEvent) {
        synchronized (this.logEvents) {
            while (this.logEvents.size() >= this.capacity) {
                this.logEvents.remove(0);
            }
            this.logEvents.add(logEvent);
        }
    }

    @Override
    public synchronized List<LogEvent> find(Date after) {
        if (after == null) {
            return findAll();
        }

        LinkedList<LogEvent> events = new LinkedList<LogEvent>();
        for (LogEvent logEvent : logEvents) {
            if (isMatch(logEvent, after)) {
                events.addFirst(logEvent);
            }
        }

        return events;
    }

    private boolean isMatch(LogEvent auditEvent, Date after) {
        return (after == null || auditEvent.getDate().compareTo(after) >= 0);
    }

}
