package org.springframework.boot.actuate.endpoint.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.ServletWrappingController;

public class HawtioServletWrappingControllerImpl implements InitializingBean, ApplicationContextAware, ServletContextAware, HawtioServletWrappingController {

    private List<ControllerWithPath> controllers = new ArrayList<HawtioServletWrappingControllerImpl.ControllerWithPath>();

    @Override
    public void setServletContext(ServletContext servletContext) {
        for (ControllerWithPath controllerWithPath : controllers) {
            controllerWithPath.getWrappedController().setServletContext(servletContext);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        for (ControllerWithPath controllerWithPath : controllers) {
            controllerWithPath.getWrappedController().setApplicationContext(applicationContext);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (ControllerWithPath controllerWithPath : controllers) {
            controllerWithPath.getWrappedController().afterPropertiesSet();
        }
    }

    @Override
    public void registerServlet(String name, Class<? extends HttpServlet> servlet, String... paths) {
        registerServlet(name, servlet, null, paths);
    }

    @Override
    public void registerServlet(String name, Class<? extends HttpServlet> servlet, Properties initParameters, String... paths) {
        ServletWrappingController controller = new ServletWrappingController();
        controller.setServletClass(servlet);
        controller.setServletName(name);
        if (initParameters != null) {
            controller.setInitParameters(initParameters);
        }

        for (String path : paths) {
            controllers.add(new ControllerWithPath(controller, path));
        }

    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, String mapping) throws Exception {

        for (ControllerWithPath controllerWithPath : controllers) {
            if (controllerWithPath.canHandle(mapping)) {
                return controllerWithPath.handleRequest(request, response);
            }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }

    public static class ControllerWithPath implements Controller {

        private final String path;
        private final ServletWrappingController controller;

        public ControllerWithPath(ServletWrappingController controller, String path) {
            this.path = path;
            this.controller = controller;
        }

        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            return controller.handleRequest(request, response);
        }

        public ServletWrappingController getWrappedController() {
            return controller;
        }

        public boolean canHandle(String pathInfo) {
            return path.equals(pathInfo);
        }
    }

}
