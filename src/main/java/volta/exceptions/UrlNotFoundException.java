package volta.exceptions;

import java.lang.reflect.Method;
import java.util.Map;
import volta.annotations.UrlMapping;
import volta.models.RouteMapping; 

public class UrlNotFoundException extends Exception {
    
    public UrlNotFoundException(String urlTapee, Map<String, RouteMapping> urlMappings) {
        super(genererMessage(urlTapee, urlMappings));
    }

    private static String genererMessage(String url, Map<String, RouteMapping> urlMappings) {
        StringBuilder sb = new StringBuilder();

        sb.append("L'URL : '").append(url).append("' n'existe pas.\n\n");
        sb.append("Voici les URLs disponibles dans l'application :\n");

        if (urlMappings != null && !urlMappings.isEmpty()) {
            for (Map.Entry<String, RouteMapping> entry : urlMappings.entrySet()) {
                String registeredUrl = entry.getKey();
                RouteMapping route = entry.getValue();
                
                Method method = route.getMethode();
                UrlMapping mapping = method.getAnnotation(UrlMapping.class);

                sb.append("--------------------------------------------------\n");
                sb.append(" -> URL        : ").append(registeredUrl).append("\n");
                sb.append("    Classe     : ").append(route.getClazz().getName()).append("\n");
                sb.append("    Methode    : ").append(method.getName()).append("()\n");
                
                if (mapping != null) {
                    sb.append("    Annotation : @").append(mapping.annotationType().getSimpleName())
                      .append("(\"").append(mapping.value()).append("\")\n");
                }
            }
            sb.append("--------------------------------------------------\n");
        } else {
            sb.append(" - Aucune URL n'est actuellement enregistree.\n");
        }

        return sb.toString();
    }
}