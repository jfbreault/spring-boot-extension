package com.ticketmaster.boot.logging;

import java.util.Date;
import java.util.List;

public interface LogEventRepository {

    void add(LogEvent event);

    List<LogEvent> find(Date after);
    
    List<LogEvent> findAll();

}