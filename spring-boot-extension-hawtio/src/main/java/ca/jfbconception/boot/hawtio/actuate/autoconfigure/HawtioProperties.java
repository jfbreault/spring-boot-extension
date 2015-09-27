package ca.jfbconception.boot.hawtio.actuate.autoconfigure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hawtio")
public class HawtioProperties {

	/**
	 * hawtio settings. These are traditionally set using servlet parameters, refer to
	 * the documentation of hawtio for more details.
	 */
	private Map<String, String> config = new HashMap<String, String>();

	public Map<String, String> getConfig() {
		return this.config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}
	
}
