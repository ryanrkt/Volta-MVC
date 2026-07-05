package volta.exceptions;

import volta.models.UrlMethodeHttpMapping;
import volta.models.RouteMapping;

public class DuplicateUrlAndMethodException extends Exception {

    public DuplicateUrlAndMethodException(UrlMethodeHttpMapping duplicateKey, RouteMapping existingRoute, Class<?> newClass, String newMethodName) {
        super(genererMessage(duplicateKey, existingRoute, newClass, newMethodName));
    }

    private static String genererMessage(UrlMethodeHttpMapping key, RouteMapping existing, Class<?> newClass, String newMethodName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Doublon détecté: \n")
          .append("La route [").append(key.getMethode()).append("] '").append(key.getUrl()).append("' est déjà enregistrée.\n\n")
          .append("Emplacement 1 : ").append(existing.getClazz().getName()).append(".").append(existing.getMethode().getName()).append("()\n")
          .append("Emplacement 2 : ").append(newClass.getName()).append(".").append(newMethodName).append("()\n")
          .append("Action requise : Changez l'URL ou la méthode HTTP de l'une de ces deux méthodes.");
        return sb.toString();
    }
}