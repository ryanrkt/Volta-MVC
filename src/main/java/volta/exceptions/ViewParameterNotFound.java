package volta.exceptions;

public class ViewParameterNotFound extends Exception {
    public ViewParameterNotFound() {
        super("Veuillez initialiser les parametres des Views dans web.xml : Prefix: /WEB-INF/View et Suffix:.jsp" );

    }
}
