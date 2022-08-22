package services.conreteConfigurations;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author Mario Manzanarez
 * Clase que agrega los headers de respuesta al router
 * */
public class Headers {

    /**
     * Método que asigna los headers al router a partir de un json con la configuración global
     * @param router - router a asignar headers
     * @param globalConfig - configuración global previamente creadas
     * */
    public static void headers(Router router, JsonObject globalConfig) {

        router.route().produces("application/json").handler(BodyHandler.create("*"));
    }
}
