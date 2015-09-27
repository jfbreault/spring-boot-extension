package ca.jfbconception.boot.logging.enpoint;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "endpoints.logging", ignoreUnknownFields = false)
public abstract class BaseLoggingEndpoint<T> extends AbstractEndpoint<T> {

    public BaseLoggingEndpoint() {
        super("logging");
    }

    public BaseLoggingEndpoint(boolean sensitive) {
        super("logging", sensitive);
    }

    public BaseLoggingEndpoint(boolean sensitive, boolean enabled) {
        super("logging", sensitive, enabled);
    }
    
}
