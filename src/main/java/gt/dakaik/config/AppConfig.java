/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.config;

/**
 *
 * @author Dario Calderon
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import gt.dakaik.common.Common;
import gt.dakaik.logs.ConfiguracionLogs;
import gt.dakaik.logs.FiltroLogs;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author Dario Calderon
 */
public class AppConfig implements WebApplicationInitializer {

    private static final String DISPATCHER_SERVLET_NAME = "spring";
    private static final String DISPATCHER_SERVLET_MAPPING = "/*";
    private static final String FILTER_LOGGING = "FiltroLogs";
    private static final String FILTER_LOGGING_MAPPING = "/*";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        ConfiguracionLogs.agregarLlave();

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootContext.class);

        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.setServletContext(servletContext);
        dispatcherContext.setParent(rootContext);
        dispatcherContext.register(WebContext.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(DISPATCHER_SERVLET_MAPPING);

        FilterRegistration.Dynamic FiltroLogs = servletContext.addFilter(FILTER_LOGGING, new FiltroLogs());
        FiltroLogs.addMappingForUrlPatterns(null, true, FILTER_LOGGING_MAPPING);

        servletContext.addListener(new ContextLoaderListener(rootContext));

    }
}
