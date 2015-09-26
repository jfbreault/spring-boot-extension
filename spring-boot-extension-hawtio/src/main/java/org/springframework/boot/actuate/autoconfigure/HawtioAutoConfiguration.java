package org.springframework.boot.actuate.autoconfigure;

import io.hawt.system.ConfigManager;
import io.hawt.web.AuthenticationFilter;
import io.hawt.web.ContextFormatterServlet;
import io.hawt.web.ExportContextServlet;
import io.hawt.web.GitServlet;
import io.hawt.web.JavaDocServlet;
import io.hawt.web.LoginServlet;
import io.hawt.web.LogoutServlet;
import io.hawt.web.PodServlet;
import io.hawt.web.ProxyServlet;
import io.hawt.web.ServiceServlet;
import io.hawt.web.UploadServlet;
import io.hawt.web.UserServlet;
import io.hawt.web.keycloak.KeycloakServlet;

import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.jolokia.http.AgentServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.mvc.HawtioMvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.HawtioServletWrappingController;
import org.springframework.boot.actuate.endpoint.mvc.HawtioServletWrappingControllerImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@ConditionalOnWebApplication
@Configuration
// need jolokia and Hawtio in class path
@ConditionalOnClass({ AgentServlet.class, ServiceServlet.class })
// need jolokia enabled also
@ConditionalOnExpression("${endpoints.hawtio.enabled:${endpoints.jolokia.enabled:${endpoints.enabled:true}}}")
@AutoConfigureAfter(EmbeddedServletContainerAutoConfiguration.class)
@EnableConfigurationProperties(HawtioProperties.class)
public class HawtioAutoConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ServletContext servletContext;
    
    @Autowired
    private HawtioProperties properties = new HawtioProperties();
    
    @PostConstruct
    public void init() {
        // disable security, let spring management security handle it 
        System.setProperty("hawtio.offline", Boolean.TRUE.toString());
        System.setProperty(AuthenticationFilter.HAWTIO_AUTHENTICATION_ENABLED, Boolean.FALSE.toString());
        final ConfigManager configManager = new ConfigManager();
        configManager.init();
        servletContext.setAttribute("ConfigManager", configManager);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public HawtioServletWrappingController hawtioServletWrappingController() {
        HawtioServletWrappingController controllers = new HawtioServletWrappingControllerImpl();

        Properties initParameters = getInitParameters();

        controllers.registerServlet("user", UserServlet.class, initParameters, "user/*");
        controllers.registerServlet("proxy", ProxyServlet.class, initParameters, "proxy/*");
        controllers.registerServlet("service", ServiceServlet.class, initParameters, "service/*");
        controllers.registerServlet("pod", PodServlet.class, initParameters ,"pod/*");
        controllers.registerServlet("file-upload", UploadServlet.class, initParameters, "file-upload/*");
        controllers.registerServlet("exportContext", ExportContextServlet.class, initParameters, "exportContext/*");
        controllers.registerServlet("javadoc", JavaDocServlet.class, initParameters, "javadoc/*");
        controllers.registerServlet("contextFormatter", ContextFormatterServlet.class, initParameters, "contextFormatter/*");
        controllers.registerServlet("git", GitServlet.class, initParameters, "git/*");
        controllers.registerServlet("login", LoginServlet.class, initParameters, "auth/login/*");
        controllers.registerServlet("logout", LogoutServlet.class, initParameters, "auth/logout/*");
        controllers.registerServlet("keycloak", KeycloakServlet.class, initParameters, "keycloak/*");
        
        return controllers;
    }

    @Bean
    @ConditionalOnMissingBean
    public HawtioMvcEndpoint hawtioMvcEndpoint(ManagementServerProperties management) {
        return new HawtioMvcEndpoint(management);
    }
    
    private Properties getInitParameters() {
        Properties initParameters = new Properties();
        for (Entry<String, String> entry : this.properties.getConfig().entrySet()) {
            initParameters.put("hawtio." + entry.getKey(), entry.getValue());
        }
        
        // override some properties to disable security, remote config, etc, let spring-boot handle this
        initParameters.put("hawtio.offline", Boolean.TRUE.toString());
        initParameters.put("hawtio.authenticationEnabled", Boolean.FALSE.toString());
        
        return initParameters;
    }

}
