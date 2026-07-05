package volta.exceptions;

import java.util.Arrays;
import volta.enums.MethodHttp;

public class MethodHttpNotFoundException extends Exception {

    public MethodHttpNotFoundException(String unknownMethod) {
        super(genererMessage(unknownMethod));
    }

    private static String genererMessage(String unknownMethod) {
        StringBuilder sb = new StringBuilder();
        sb.append("Méthode HTTP non supportée !\n")
          .append("Le verbe HTTP '").append(unknownMethod).append("' n'est pas reconnu par le framework Volta-MVC.\n\n")
          .append("Méthodes HTTP configurées et valides : ")
          .append(Arrays.toString(MethodHttp.values())).append("\n")
          .append("Veuillez utiliser un verbe HTTP valide ou mettre à jour l'énumération MethodHttp.");
        return sb.toString();
    }
}