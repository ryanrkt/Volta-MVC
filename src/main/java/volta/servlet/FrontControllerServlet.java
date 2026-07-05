package volta.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import volta.annotations.UrlMapping;
import volta.enums.MethodHttp;
import volta.exceptions.DuplicateUrlAndMethodException;
import volta.exceptions.UrlNotFoundException;
import volta.models.RouteMapping;
import volta.models.UrlMethodeHttpMapping;
import volta.utils.AnnotationScanner;

public class FrontControllerServlet extends HttpServlet {

    private Map<UrlMethodeHttpMapping, RouteMapping> urlMapping;

    @Override
    public void init() throws ServletException {
        this.urlMapping = new HashMap<>();
        try {
            AnnotationScanner.getUrlMethodeMappings(this.urlMapping, UrlMapping.class);
        } catch (Exception e) {
            throw new ServletException("Echec lors de l'initialisation des routes: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String pathTarget = requestURI.substring(contextPath.length());

        String httpMethodStr = request.getMethod();
        MethodHttp currentHttpMethod = MethodHttp.valueOf(httpMethodStr);

        UrlMethodeHttpMapping searchKey = new UrlMethodeHttpMapping();
        searchKey.setUrl(pathTarget);
        searchKey.setMethode(currentHttpMethod);

        try {
            if (urlMapping == null || !urlMapping.containsKey(searchKey)) {
                throw new UrlNotFoundException(pathTarget, urlMapping);
            }

            RouteMapping route = urlMapping.get(searchKey);
            Method targetMethod = route.getMethode();
            UrlMapping mapping = targetMethod.getAnnotation(UrlMapping.class);

            out.println("<h1>URL trouvee !</h1>");
            out.println("<p><strong>URL :</strong> " + pathTarget + "</p>");
            out.println("<p><strong>Methode HTTP :</strong> " + currentHttpMethod + "</p>");
            out.println("<p><strong>Methode Java :</strong> " + targetMethod.getName() + "()</p>");
            out.println("<p><strong>Classe :</strong> " + route.getClazz().getName() + "</p>");
            out.println("<p><strong>Annotation :</strong> @" + mapping.annotationType().getSimpleName() + "(\""
                    + mapping.value() + "\")</p>");

            Class<?> clazz = route.getClazz();

            Object instance = clazz.getDeclaredConstructor().newInstance();

            targetMethod.invoke(instance);
            


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