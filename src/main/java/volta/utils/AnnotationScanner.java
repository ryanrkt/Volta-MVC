package volta.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import volta.models.RouteMapping;




public class AnnotationScanner {

    public static Map<String, RouteMapping> getUrlMethodeMappings(Map<Class<?>, List<Method>> methodeMappings, Class<? extends Annotation> annotationClass) {
        Map<String, RouteMapping> urlMap = new HashMap<>();
        
        if (methodeMappings == null || annotationClass == null) {
            return urlMap;
        }

        for (Map.Entry<Class<?>, List<Method>> entry : methodeMappings.entrySet()) {
            Class<?> clazz = entry.getKey(); 
            List<Method> methods = entry.getValue();

            for (Method method : methods) {
                Annotation annotation = method.getAnnotation(annotationClass);
                
                if (annotation != null) {
                    try {
                        Method valueMethod = annotation.annotationType().getMethod("value");
                        String url = (String) valueMethod.invoke(annotation);
                        
                        RouteMapping route = new RouteMapping();
                        route.setClazz(clazz);
                        route.setMethode(method);
            
                        urlMap.put(url, route);
                        
                    } catch (Exception e) {
                        System.err.println("L'annotation " + annotationClass.getSimpleName() + " n'a pas d'attribut 'value()'.");
                    }
                }
            }
        }
        
        return urlMap;
    }
    public static Map<Class<?>, List<Method>> getAnnotatedMethodsPerClass(Class<? extends Annotation> annotationClass)
            throws ClassNotFoundException, IOException {

        Map<Class<?>, List<Method>> mapping = new HashMap<>();
        List<Class<?>> allClasses = PackageScanner.getAllClasses();

        for (Class<?> clazz : allClasses) {
            List<Method> annotatedMethods = new ArrayList<>();

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotationClass)) {
                    annotatedMethods.add(method);
                }
            }
            if (!annotatedMethods.isEmpty()) {
                mapping.put(clazz, annotatedMethods);
            }
        }

        return mapping;
    }

    public static List<Class<?>> getClassesAnnotated(Class<? extends Annotation> annotationClass)
            throws ClassNotFoundException, IOException {
        List<Class<?>> annotatedClasses = new ArrayList<>();

        List<Class<?>> allClasses = PackageScanner.getAllClasses();
        for (Class<?> c : allClasses) {
            if (c.isAnnotationPresent(annotationClass)) {
                annotatedClasses.add(c);
            }
        }
        return annotatedClasses;
    }

    public static List<Class<?>> getClassesAnnotatedInPackage(Class<? extends Annotation> annotationClass,
            String packageName)
            throws ClassNotFoundException, IOException {

        List<Class<?>> allClasses = PackageScanner.getClassesInPackage(packageName);

        List<Class<?>> annotatedClasses = new ArrayList<>();

        for (Class<?> c : allClasses) {
            if (c.isAnnotationPresent(annotationClass)) {
                annotatedClasses.add(c);
            }
        }

        return annotatedClasses;
    }
}