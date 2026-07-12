package volta.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import volta.annotations.UrlMapping;
import volta.exceptions.UrlNotFoundException;
import volta.models.MethodControllerMapping;
import volta.models.UrlMethodeHttpMapping;

public class FrontControllerServlet extends HttpServlet {

    private Map<UrlMethodeHttpMapping, MethodControllerMapping> urlMapping;

    @Override
    public void init() throws ServletException {
        urlMapping= (Map<UrlMethodeHttpMapping, MethodControllerMapping>) this.getServletContext().getAttribute("urlMapping");
        
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
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String pathTarget = requestURI.substring(contextPath.length());

        informationUrl(pathTarget, response);

        try {
            executeUrl(pathTarget);
        } catch (UrlNotFoundException e) {
            System.err.println("[Volta-MVC] Erreur d'exécution : " + e.getMessage());
        }
    }

  
    public void informationUrl(String pathTarget, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        UrlMethodeHttpMapping foundKey = chercherCleParUrl(pathTarget);

        if (foundKey != null) {
            MethodControllerMapping route = urlMapping.get(foundKey);
            Method targetMethod = route.getMethode();
            UrlMapping mapping = targetMethod.getAnnotation(UrlMapping.class);

            out.println("<h1>URL trouvee !</h1>");
            out.println("<p><strong>URL appelée :</strong> " + pathTarget + "</p>");
            out.println("<p><strong>Methode  exécutée :</strong> " + targetMethod.getName() + "()</p>");
            out.println("<p><strong>Classe :</strong> " + route.getClazz().getName() + "</p>");
            
            try {
                Method valueMethod = mapping.annotationType().getMethod("value");
                out.println("<p><strong>Annotation :</strong> @" + mapping.annotationType().getSimpleName() + "(\"" + valueMethod.invoke(mapping) + "\")</p>");
            } catch (Exception e) {
                out.println("<p><strong>Annotation :</strong> @" + mapping.annotationType().getSimpleName() + "</p>");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("<h1>Erreur : UrlNotFoundException</h1>");
            out.println("<pre>L'URL : '" + pathTarget + "' n'existe pas dans l'application.</pre>");
        }
    }

 
    public void executeUrl(String pathTarget) throws UrlNotFoundException {
        UrlMethodeHttpMapping foundKey = chercherCleParUrl(pathTarget);

        if (foundKey == null) {
            throw new UrlNotFoundException(pathTarget, urlMapping);
        }

        try {
            MethodControllerMapping route = urlMapping.get(foundKey);
            Method targetMethod = route.getMethode();

            Object controllerInstance = route.getClazz().getDeclaredConstructor().newInstance();
            targetMethod.invoke(controllerInstance);
            System.out.println("[Volta-MVC] Invocation réussie : " + route.getClazz().getSimpleName() + "." + targetMethod.getName() + "()");

        } catch (Exception e) {
            System.err.println("[Volta-MVC] Erreur lors de l'invocation de la méthode : " + e.getMessage());
            e.printStackTrace();
        }
    }

   
    private UrlMethodeHttpMapping chercherCleParUrl(String url) {
        if (urlMapping != null) {
            for (UrlMethodeHttpMapping key : urlMapping.keySet()) {
                if (key.getUrl().equals(url)) {
                    return key;
                }
            }
        }
        return null;
    }
}