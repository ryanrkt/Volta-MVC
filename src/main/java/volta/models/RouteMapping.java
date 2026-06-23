package volta.models;

import java.lang.reflect.Method;

public class RouteMapping {
    private Class<?> clazz;
    private Method methode;

    
    public Class<?> getClazz() {
        return clazz;
    }
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
    public Method getMethode() {
        return methode;
    }
    public void setMethode(Method methode) {
        this.methode = methode;
    }

    
}
