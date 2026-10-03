package volta.core;

import java.lang.reflect.Method;

public class MethodControllerMapping {
    private Class<?> clazz;
    private Method methode;
    private Boolean isApiRest;

    
   
    public Boolean getIsApiRest() {
        return isApiRest;
    }
    public void setIsApiRest(Boolean isApiRest) {
        this.isApiRest = isApiRest;
    }
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
