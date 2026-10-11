package volta.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import jakarta.servlet.http.HttpServletRequest;

public class ParameterResolver {

    public static Object[] resolveParameters(Method method, HttpServletRequest request) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> paramType = param.getType();

            if (isSimpleType(paramType)) {
                String paramName = param.getName(); 
                String requestValue = request.getParameter(paramName);
                args[i] = TypeConverter.convert(requestValue, paramType);
            } 
            else {
                args[i] = bindObject(paramType, request);
            }
        }

        return args;
    }

 
    private static boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() ||
               clazz.equals(String.class) ||
               clazz.equals(Integer.class) ||
               clazz.equals(Double.class) ||
               clazz.equals(Boolean.class) ||
               clazz.equals(Long.class);
    }


    private static Object bindObject(Class<?> clazz, HttpServletRequest request) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();

            Class<?> currentClass = clazz;
            while (currentClass != null && !currentClass.equals(Object.class)) {
                Field[] fields = currentClass.getDeclaredFields();
                
                for (Field field : fields) {
                    String fieldName = field.getName(); 
                    
                    String value = request.getParameter(fieldName);

                    if (value != null) {
                        String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                        
                        try {
                            Method setter = currentClass.getMethod(setterName, field.getType());
                            Object convertedValue = TypeConverter.convert(value, field.getType());
                            setter.invoke(instance, convertedValue);
                        } catch (NoSuchMethodException e) {
                        }
                    }
                }
                currentClass = currentClass.getSuperclass();
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}