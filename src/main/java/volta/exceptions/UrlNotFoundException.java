package volta.exceptions;

import java.util.Map;
import volta.models.MethodControllerMapping;
import volta.models.UrlMethodeHttpMapping;

public class UrlNotFoundException extends Exception {
    
    public UrlNotFoundException(String urlTapee, Map<UrlMethodeHttpMapping, MethodControllerMapping> urlMappings) {
        super(genererMessage(urlTapee, urlMappings));
    }

    private static String genererMessage(String url, Map<UrlMethodeHttpMapping, MethodControllerMapping> urlMappings) {
        StringBuilder sb = new StringBuilder();

        sb.append("L'URL : '").append(url).append("' n'existe pas.\n\n");
        sb.append("Voici les URLs disponibles dans l'application :\n");

        if (urlMappings != null && !urlMappings.isEmpty()) {
            for (Map.Entry<UrlMethodeHttpMapping, MethodControllerMapping> entry : urlMappings.entrySet()) {
                UrlMethodeHttpMapping key = entry.getKey();
                MethodControllerMapping route = entry.getValue();

                sb.append("--------------------------------------------------\n");
                sb.append(" -> URL        : ").append(key.getUrl()).append("\n");
                sb.append("    HTTP Method: ").append(key.getMethode()).append("\n");
                sb.append("    Classe     : ").append(route.getClazz().getName()).append("\n");
                sb.append("    Methode    : ").append(route.getMethode().getName()).append("()\n");
            }
            sb.append("--------------------------------------------------\n");
        } else {
            sb.append(" - Aucune URL n'est actuellement enregistree.\n");
        }

        return sb.toString();
    }
}