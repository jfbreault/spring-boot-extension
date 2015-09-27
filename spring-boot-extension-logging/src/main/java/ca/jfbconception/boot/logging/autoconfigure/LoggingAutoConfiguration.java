package ca.jfbconception.boot.logging.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.actuate.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.jfbconception.boot.logging.enpoint.LogbackLoggingEndpoint;
import ca.jfbconception.boot.logging.enpoint.LoggingEndpoint;
import ca.jfbconception.boot.logging.enpoint.mvc.LoggingHateoasMvcEndpoint;
import ca.jfbconception.boot.logging.enpoint.mvc.LoggingMvcEndpoint;
import ch.qos.logback.classic.spi.ILoggingEvent;

@Configuration
@ConditionalOnWebApplication
public class LoggingAutoConfiguration {

    @Autowired
    private ManagementServerProperties managementServerProperties;

    @ConditionalOnClass(ILoggingEvent.class)
    public static class LogbackLoggingAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnEnabledEndpoint("logging")
        public LoggingEndpoint getLoggingEndpoint() {
            return new LogbackLoggingEndpoint(false, true);
        }

    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.hateoas.Link")
    @ConditionalOnBean(LoggingEndpoint.class)
    @ConditionalOnMissingBean
    public LoggingHateoasMvcEndpoint getLoggingHateoasMvcEndpoint(LoggingEndpoint loggingEndpoint) {
        return new LoggingHateoasMvcEndpoint(loggingEndpoint);
    }

    @ConditionalOnBean(LoggingEndpoint.class)
    @ConditionalOnMissingBean(LoggingHateoasMvcEndpoint.class)
    public static class LoggingMvcAutoConfiguration {
        @Bean
        @ConditionalOnBean(LoggingEndpoint.class)
        @ConditionalOnMissingBean
        public LoggingMvcEndpoint getLoggingHateoasMvcEndpoint(LoggingEndpoint loggingEndpoint) {
            return new LoggingMvcEndpoint(loggingEndpoint);
        }

    }

}
