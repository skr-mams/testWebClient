package services.models.httpMethods;

/**
 * @author Mario Manzanarez
 * Contiene los códigos http 300
 */
final public class Redirect {

    public static final int MULTIPLE_CHOICE = 300;
    public static final int MOVED_PERMANENTLY = 301;
    public static final int FOUND = 302;
    public static final int SEE_OTHER = 303;
    public static final int NOT_MODIFIED = 304;
    public static final int TEMPORARY_REDIRECT = 307;
    public static final int PERMANENT_REDIRECT = 308;
}
