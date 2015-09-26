package com.ticketmaster.boot.logging.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Resource;

import ch.qos.logback.classic.spi.ILoggingEvent;

import com.ticketmaster.boot.logging.enpoint.LogbackLoggingEndpoint;
import com.ticketmaster.boot.logging.enpoint.LoggingEndpoint;
import com.ticketmaster.boot.logging.enpoint.mvc.LoggingHateoasMvcEndpoint;
import com.ticketmaster.boot.logging.enpoint.mvc.LoggingMvcEndpoint;

@Configuration
@ConditionalOnWebApplication
public class LoggingAutoConfiguration {

    @Autowired
    private ManagementServerProperties managementServerProperties;

    @ConditionalOnClass(ILoggingEvent.class)
    public static class LogbackLoggingAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public LoggingEndpoint getLoggingEndpoint() {
            return new LogbackLoggingEndpoint(false, true);
        }

    }

    @ConditionalOnClass({ Resource.class })
    public static class HateoasLoggingAutoConfiguration {
        @Bean
        @ConditionalOnBean(LoggingEndpoint.class)
        @ConditionalOnMissingBean
        public LoggingHateoasMvcEndpoint getLoggingHateoasMvcEndpoint(LoggingEndpoint loggingEndpoint) {
            return new LoggingHateoasMvcEndpoint(loggingEndpoint);
        }

    }

    @Bean
    @ConditionalOnBean(LoggingEndpoint.class)
    @ConditionalOnMissingBean
    public LoggingMvcEndpoint getLoggingMvcEndpoint(LoggingEndpoint loggingEndpoint) {
        return new LoggingMvcEndpoint(loggingEndpoint);
    }

}
