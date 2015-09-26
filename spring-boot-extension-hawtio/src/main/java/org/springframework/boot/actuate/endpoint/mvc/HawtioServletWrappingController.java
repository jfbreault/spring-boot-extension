package org.springframework.boot.actuate.endpoint.mvc;

import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public interface HawtioServletWrappingController {

	void registerServlet(String name, Class<? extends HttpServlet> servlet,
			String... paths);

	void registerServlet(String name, Class<? extends HttpServlet> servlet,
			Properties initParameters, String... paths);
	
	ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, String mapping) throws Exception;

}