package ca.jfbconception.boot.hawtio.actuate.endpoint.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.JolokiaMvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UrlPathHelper;

@ConfigurationProperties(prefix = "endpoints.hawtio", ignoreUnknownFields = false)
public class HawtioMvcEndpoint extends WebMvcConfigurerAdapter implements MvcEndpoint {
    
    private ManagementServerProperties management;

	/**
	 * Endpoint URL path.
	 */
	@NotNull
	@Pattern(regexp = "/[^/]*", message = "Path must start with /")
	private String path;
	
	@Autowired
	private JolokiaMvcEndpoint jolokiaMvcEndpoint;

	/**
	 * Enable security on the endpoint.
	 */
	private boolean sensitive = true;

	/**
	 * Enable the endpoint.
	 */
	private boolean enabled = true;

	@Autowired
	private HawtioServletWrappingController servletWrappingControllerSelector;

	public HawtioMvcEndpoint(ManagementServerProperties management) {
	    this.management = management;
		this.path = "/hawtio";
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean isSensitive() {
		return this.sensitive;
	}

	public void setSensitive(boolean sensitive) {
		this.sensitive = sensitive;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Class<? extends Endpoint> getEndpointType() {
		return null;
	}
	
	@RequestMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String browse() {
        return "redirect:" + this.management.getContextPath() + this.path + "/index.html";
    }

    @RequestMapping(value = "", produces = MediaType.TEXT_HTML_VALUE)
    public String redirect() {
        return "redirect:" + this.management.getContextPath() + this.path + "/";
    }
	
	@RequestMapping("user")
	public ModelAndView handleUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.servletWrappingControllerSelector.handleRequest(new PathStripper(request,
				getPath()), response, "user/*");
	}
	
	
	/**
	 * hack for the ui
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("refresh")
    public void handleRefresh(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
	    response.setStatus(200);
	    response.getOutputStream().write("ok".getBytes());
	    response.flushBuffer();
    }
	
	@RequestMapping("jolokia/**")
    public ModelAndView handleJolokia(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
	    return jolokiaMvcEndpoint.handle(new PathStripper(request, path), response);
    }
	
	@RequestMapping("service/*")
    public ModelAndView handleService(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return this.servletWrappingControllerSelector.handleRequest(new PathStripper(request,
                getPath()), response, "service/*");
    }
	
	@RequestMapping("pod/*")
    public ModelAndView handlePod(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return this.servletWrappingControllerSelector.handleRequest(new PathStripper(request,
                getPath()), response, "pod/*");
    }
	

    @RequestMapping("file-upload/*")
    public ModelAndView handleFileUpload(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return this.servletWrappingControllerSelector.handleRequest(new PathStripper(request,
                getPath()), response, "file-upload/*");
    }
    
    @RequestMapping("exportContext/*")
    public ModelAndView handleExportContext(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return this.servletWrappingControllerSelector.handleRequest(new PathStripper(request,
                getPath()), response, "exportContext/*");
    }
    
    @RequestMapping("javadoc/*")
    public ModelAndView handleJavadoc(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return this.servletWrappingControllerSelector.handleRequest(new PathStripper(request,
                getPath()), response, "javadoc/*");
    }
    
    @RequestMapping("contextFormatter/*")
    public ModelAndView handleContextFormatter(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return this.servletWrappingControllerSelector.handleRequest(new PathStripper(request,
                getPath()), response, "contextFormatter/*");
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(this.management.getContextPath() + this.path + "/plugins/**").addResourceLocations("/app/", "classpath:/static/hawtio/app/");
        registry.addResourceHandler(this.management.getContextPath() + this.path + "/**").addResourceLocations("/", "/app/", "classpath:/static/hawtio/",
                "classpath:/static/hawtio/app/");
        registry.addResourceHandler(this.management.getContextPath() + this.path + "/img/**").addResourceLocations("classpath:/static/hawtio/img/");
    }

	private static class PathStripper extends HttpServletRequestWrapper {

		private final String path;
		private final UrlPathHelper urlPathHelper;

		public PathStripper(HttpServletRequest request, String path) {
			super(request);
			this.path = path;
			this.urlPathHelper = new UrlPathHelper();
		}

		@Override
		public String getPathInfo() {
			String value = this.urlPathHelper.decodeRequestString(
					(HttpServletRequest) getRequest(), super.getRequestURI());
			if (value.contains(this.path)) {
				value = value.substring(value.indexOf(this.path)
						+ this.path.length());
			}
			int index = value.indexOf("?");
			if (index > 0) {
				value = value.substring(0, index);
			}
			while (value.startsWith("/")) {
				value = value.substring(1);
			}
			return value;
		}
	}

}
