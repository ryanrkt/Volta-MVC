package volta.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import jakarta.servlet.http.HttpServletRequest;

public class ParameterResolver {

    public static Object[] resolveParameters(Method method, HttpServletRequest request) {

        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            
            String paramName = param.getName(); 
            
            String requestValue = request.getParameter(paramName);

            Class<?> paramType = param.getType();
            if (paramType.equals(String.class)) {
                args[i] = requestValue;
            } else if (paramType.equals(Integer.class) || paramType.equals(int.class)) {
                args[i] = (requestValue != null && !requestValue.isEmpty()) ? Integer.parseInt(requestValue) : 0;
            } else if (paramType.equals(Double.class) || paramType.equals(double.class)) {
                args[i] = (requestValue != null && !requestValue.isEmpty()) ? Double.parseDouble(requestValue) : 0.0;
            } else {
                args[i] = null;
            }
        }

        // arguments alefa invoke
        return args; 
    }
}