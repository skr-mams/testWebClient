package services.conreteConfigurations;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Mario Manzanarez
 * Clase que agrega la configuración real a los Cors del router
 */
public abstract class Cors {
    /**
     * Método que agrega CORS a las rutas
     *
     * @param router Router principal creado en la clase MainVerticle
     */
    public static void generateCors(Router router) {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add(HttpHeaderNames.X_REQUESTED_WITH.toString());
        allowedHeaders.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN.toString());
        allowedHeaders.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS.toString());
        allowedHeaders.add(HttpHeaderNames.ORIGIN.toString());
        allowedHeaders.add(HttpHeaderNames.CONTENT_TYPE.toString());
        allowedHeaders.add(HttpHeaderNames.ACCEPT.toString());
        allowedHeaders.add("AuditId");
        allowedHeaders.add(HttpHeaderNames.AUTHORIZATION.toString());


        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);
        router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));

    }

}
