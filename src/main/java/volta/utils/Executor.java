package volta.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.PrintWriter;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import volta.core.ModelAndView;
import volta.core.MethodControllerMapping;

public class Executor {
    
    private static final Gson gson = new Gson();

    public static void invokeViewRelatedFunction(
            MethodControllerMapping method,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            Object controller = method.getClazz()
                    .getDeclaredConstructor()
                    .newInstance();
            Method methode = method.getMethode();

            Object retour = methode.invoke(controller);

            if (Boolean.TRUE.equals(method.getIsApiRest())) {
                response.setContentType("application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");

                String jsonOutput = gson.toJson(retour);

                PrintWriter out = response.getWriter();
                out.print(jsonOutput);
                out.flush();
                out.close();
                return;
            }

            if (retour instanceof ModelAndView mv) {
                ModelAndViewHandler.processModelAndView(mv, request, response);
            }

        } catch (InvocationTargetException e) {
            Throwable realCause = e.getCause();
            realCause.printStackTrace();
            
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(500);
            response.getWriter().write("Erreur dans le controleur : " + realCause.toString());
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(500);
            response.getWriter().write("Erreur Framework : " + e.toString());
            response.getWriter().flush();
        }
    }
}