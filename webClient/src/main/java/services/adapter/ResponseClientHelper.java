package services.adapter;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.models.GenericResponse;
import services.models.httpMethods.ServerError;
import services.models.httpMethods.Success;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Mario Manzanarez
 * Contiene métodos estáticos para responder
 * al cliente web cuando la respuesta es satisfactoria
 */
abstract public class ResponseClientHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseClientHelper.class);

    /**
     * Responde al cliente web con un JsonArray, envía un código de estatus 200 OK
     *
     * @param ctx             contexto de la ruta para responder
     * @param listForResponse listado de datos a transformar en JsonArray para responder a cliente web
     * @param <T>             tipo de dato genérico en lista, podemos enviar al cliente web una lista de cualquier tipo
     * @author Mario Manzanarez
     */
    public static <T> void responseOKList(RoutingContext ctx, List<T> listForResponse) {
        JsonArray response = Adapter.listToJsonArray(listForResponse);
        ctx.response().setStatusCode(200).end(response.encodePrettily());
    }

    /**
     * Responde al cliente web con un JsonObject parseado desde en objeto genérico de respuesta
     *
     * @param ctx         contexto de la respuesta
     * @param forResponse objeto a ser parseado a JsonObject para ser enviado al cliente web
     * @param <T>         tipo de dato genérico, se puede parsear cualquier dato
     * @author Mario Manzanarez
     */
    public static <T> void responseOKObject(RoutingContext ctx, T forResponse) {
        JsonObject response = Adapter.ObjectToJSON(forResponse);
        ctx.response().setStatusCode(200).end(response.encodePrettily());
    }

    /**
     * Envía un código de estado por algún error ocurrido
     *
     * @param ctx   contexto de la ruta
     * @param error excepción lanzada y la cual decidirá el código de estatus a responder
     */
    public static void responseFailure(RoutingContext ctx, Throwable error) {
        LOG.error("Error ocurrido en petición: " + error.toString(), error);
        int statusCode;
        if (error instanceof NoSuchElementException)
            statusCode = Success.NO_CONTENT;
            //else if (error instanceof )
            //  statusCode = ClientError.NOT_ACCEPTABLE;
        else
            statusCode = ServerError.INTERNAL_SERVER_ERROR;
        ctx.response().setStatusCode(statusCode).end();
    }

    /**
     * Envía un código de estado por algún error ocurrido
     * mas la estructura de una respuesta dinámica
     *
     * @param ctx     contexto de la ruta
     * @param error   excepción lanzada y la cual decidirá el código de estatus a responder
     * @param message mensaje de error para el objeto dinámico
     */
    public static void responseFailureDynamicObject(RoutingContext ctx, Throwable error, String message) {
        LOG.error("Error ocurrido en petición: " + error.toString(), error);
        var dynamicResponse = new GenericResponse();
        int statusCode;
        if (error instanceof NoSuchElementException)
            statusCode = Success.NO_CONTENT;
        else
            statusCode = ServerError.INTERNAL_SERVER_ERROR;

        if (message != null)
            dynamicResponse.setMessage(message);
        else
            dynamicResponse.setMessage(error.getMessage());
        dynamicResponse.setState(statusCode);
        ctx.response().setStatusCode(statusCode).end(Adapter.ObjectToJSON(dynamicResponse).encodePrettily());
    }

    /**
     * Envía respuesta al cliente web con el formato del objeto dinámico
     *
     * @param ctx
     * @param status
     * @param message
     * @param response
     */
    public static void responseDynamicObject(RoutingContext ctx, int status, String message, Object response) {
        var dynamicResponse = new GenericResponse(message, status, response);
        ctx.response().putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON).setStatusCode(status).end(Adapter.ObjectToJSON(dynamicResponse).encodePrettily());
    }
}
