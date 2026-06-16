package volta.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class AnnotationScanner {
    
 public static List<Class<?>> getClassesAnnotated(Class<? extends Annotation> annotationClass, String packageName) 
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