package volta.listener;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import volta.annotations.UrlMapping;
import volta.models.MethodControllerMapping;
import volta.models.UrlMethodeHttpMapping;
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
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}