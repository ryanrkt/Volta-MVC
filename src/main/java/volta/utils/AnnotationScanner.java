package volta.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import volta.exceptions.DuplicateUrlAndMethodException;
import volta.core.MethodControllerMapping;
import volta.core.UrlMethodeHttpMapping;
import volta.enums.MethodHttp;

public class AnnotationScanner {

    public static void getUrlMethodeMappings(
            Map<UrlMethodeHttpMapping, MethodControllerMapping> urlMap, Class<? extends Annotation> annotationClass)
            throws Exception {

        if (urlMap == null || annotationClass == null) {
            return;
        }

        Map<Class<?>, List<Method>> methodeMappings = getAnnotatedMethodsPerClass(annotationClass);

        if (methodeMappings == null) {
            return;
        }

        for (Map.Entry<Class<?>, List<Method>> entry : methodeMappings.entrySet()) {
            Class<?> clazz = entry.getKey();
            for (Method method : entry.getValue()) {
                Annotation annotation = method.getAnnotation(annotationClass);

                if (annotation != null) {
                    String url = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                    String httpMethodString = (String) annotation.annotationType()
                            .getMethod("methodHttp").invoke(annotation);
                    MethodHttp httpMethod = MethodHttp.valueOf(httpMethodString.toUpperCase().trim());

                    UrlMethodeHttpMapping key = new UrlMethodeHttpMapping();
                    key.setUrl(url);
                    key.setMethode(httpMethod);

                    if (urlMap.containsKey(key)) {
                        MethodControllerMapping existingRoute = urlMap.get(key);
                        throw new DuplicateUrlAndMethodException(key, existingRoute, clazz, method.getName());
                    }

                    MethodControllerMapping route = new MethodControllerMapping();
                    route.setClazz(clazz);
                    route.setMethode(method);

                    urlMap.put(key, route);
                }
            }
        }
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