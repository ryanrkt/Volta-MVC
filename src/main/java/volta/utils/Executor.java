package volta.utils;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import volta.core.ModelAndView;
import volta.core.MethodControllerMapping;

public class Executor {
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
            if (retour instanceof ModelAndView mv) {
                ModelAndViewHandler.processModelAndView(mv, request, response);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
