package volta.listener;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import volta.annotations.UrlMapping;
import volta.core.MethodControllerMapping;
import volta.core.UrlMethodeHttpMapping;
import volta.core.ViewParameter;
import volta.exceptions.ViewParameterNotFound;
import volta.utils.AnnotationScanner;
@WebListener
public class MyServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    Map<UrlMethodeHttpMapping, MethodControllerMapping> urlMapping = new HashMap<>();
    ServletContext servletContext = servletContextEvent.getServletContext();
    try {
            AnnotationScanner.getUrlMethodeMappings(urlMapping, UrlMapping.class);
            servletContext.setAttribute("urlMapping", urlMapping);
        } catch (Exception e) {
            throw new IllegalStateException("Echec lors de l'initialisation des routes: " + e.getMessage(), e);
        }

    try {
        String prefix = servletContext.getInitParameter("prefix");
        String suffix = servletContext.getInitParameter("suffix");

        if(prefix == null || suffix == null){
            throw new ViewParameterNotFound();
        } 
        else{
            ViewParameter.setPrefix(prefix);
            ViewParameter.setSuffix(suffix);
        }
    }
        catch (Exception e) {
        // TODO: handle exception
    }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}