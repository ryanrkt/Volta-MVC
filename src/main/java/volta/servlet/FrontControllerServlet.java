package volta.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import volta.annotations.UrlMapping;
import volta.exceptions.UrlNotFoundException;
import volta.models.RouteMapping;
import volta.utils.AnnotationScanner;

public class FrontControllerServlet extends HttpServlet {

    private Map<String,RouteMapping> urlMapping;

    @Override
    public void init() {
        try {
            Map<Class<?>, List<Method>> initialScan = AnnotationScanner.getAnnotatedMethodsPerClass(UrlMapping.class);
            urlMapping = AnnotationScanner.getUrlMethodeMappings(initialScan, UrlMapping.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

   public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String pathTarget = requestURI.substring(contextPath.length());

        try {
            if (urlMapping == null || !urlMapping.containsKey(pathTarget)) {
                throw new UrlNotFoundException(pathTarget, urlMapping);
            }

            RouteMapping route = urlMapping.get(pathTarget);
            Method targetMethod = route.getMethode();
            UrlMapping mapping = targetMethod.getAnnotation(UrlMapping.class);

            out.println("<h1>URL trouvee !</h1>");
            out.println("<p><strong>URL :</strong> " + pathTarget + "</p>");
            out.println("<p><strong>Methode :</strong> " + targetMethod.getName() + "()</p>");
            out.println("<p><strong>Classe :</strong> " + route.getClazz().getName() + "</p>"); 
            out.println("<p><strong>Annotation :</strong> @" + mapping.annotationType().getSimpleName() + "(\"" + mapping.value() + "\")</p>");

        } catch (UrlNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("<h1>Erreur : UrlNotFoundException</h1>");
            out.println("<pre>");
            out.println(e.getMessage());
            out.println("</pre>");
        } finally {
            out.close();
        }
    }
}