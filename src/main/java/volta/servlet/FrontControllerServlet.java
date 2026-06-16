package volta.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import volta.annotations.Controller;
import volta.utils.AnnotationScanner;

public class FrontControllerServlet extends HttpServlet {

    private String packageName;
    private List<Class<?>> listeClasses;

    public void init(){
        packageName = this.getInitParameter("packageName");
        try {
            listeClasses = AnnotationScanner.getClassesAnnotated(Controller.class, packageName);
        } catch (ClassNotFoundException | IOException e) {
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
        
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("text/html;charset=UTF-8");
        String url =  request.getRequestURL().toString();
        PrintWriter out = response.getWriter();
        out.println("<h1>"+url+"<h1>");

        out.println("<h2> Package Name:" + packageName+ "</h2");
        out.println("<ul>");
        for(Class clazz: listeClasses){
            out.println("<li>"+clazz.getName()+"</li>");
        }
        out.println("</ul>");
        out.close();
    }
}