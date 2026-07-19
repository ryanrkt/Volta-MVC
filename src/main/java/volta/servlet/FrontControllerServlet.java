package volta.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import volta.utils.Executor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import volta.annotations.UrlMapping;
import volta.exceptions.UrlNotFoundException;
import volta.core.MethodControllerMapping;
import volta.core.UrlMethodeHttpMapping;
import volta.enums.MethodHttp;

public class FrontControllerServlet extends HttpServlet {

    private Map<UrlMethodeHttpMapping, MethodControllerMapping> urlMapping;

    @Override
    public void init() throws ServletException {
        urlMapping = (Map<UrlMethodeHttpMapping, MethodControllerMapping>) this.getServletContext()
                .getAttribute("urlMapping");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (IllegalArgumentException | SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (IllegalArgumentException | SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        try {
            MethodControllerMapping method = processPath(path);
            if (method == null) {
                throw new UrlNotFoundException(path, urlMapping);
            } else {
                Method m = method.getMethode();
                System.out.printf("[VOLTA-MVC]");
                System.out.println("    URL : " + path);
                System.out.println("    METHODE : " + m.getName() + " / CONTROLLER : " + m.getDeclaringClass());
                Executor.invokeViewRelatedFunction(method, request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }

    }

    public MethodControllerMapping processPath(String path) {
        return urlMapping.get(new UrlMethodeHttpMapping(path, MethodHttp.valueOf("GET")));
    }

}