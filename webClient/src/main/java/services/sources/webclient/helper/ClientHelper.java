package services.sources.webclient.helper;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.models.httpMethods.ClientError;
import services.models.httpMethods.Success;
import services.sources.webclient.connection.WebClientConnection;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Mario Manzanarez
 * Contiene métodos para hacer más simple la interacción con
 * los micro servicios
 */
public class ClientHelper {

    private static final Logger logger = LoggerFactory.getLogger(ClientHelper.class);
    private static final WebClient CLIENT = WebClientConnection.getWebClient();
    private static final String NAME_HEADER = "Accept";
    private static final String VALUE_HEADER = "application/json";
    private static final Credentials CREDENTIALS_K1 = new UsernamePasswordCredentials("k2@kerno.mx", "Sekura.1234");

    /**
     * Crear una conexión mediante un cliente web
     *
     * @param port puerto por el que está disponible el recurso
     * @param host host por el que está disponible el recurso
     * @param path ruta del recurso
     * @return conexión de cliente web que espera como respuesta un JsonArray
     */
    public static HttpRequest<JsonArray> arrayRequest(int port, String host, String path) {
        return CLIENT
                .post(port, host, path)
                .ssl(true)
                .as(BodyCodec.jsonArray())
                .putHeader(NAME_HEADER, VALUE_HEADER);
    }

    /**
     * Crear una conexión mediante un cliente web
     *
     * @param port puerto por el que está disponible el recurso
     * @param host host por el que está disponible el recurso
     * @param path ruta del recurso
     * @return conexión de cliente web que espera como respuesta un JsonObject
     */
    public static HttpRequest<JsonObject> jsonRequest(int port, String host, String path) {
        return CLIENT
                .post(port, host, path)
                .ssl(true)
                .as(BodyCodec.jsonObject())
                .putHeader(NAME_HEADER, VALUE_HEADER);
    }

    /**
     * Crea una lista a partir de una respuesta obtenida como JsonArray
     *
     * @param response respuesta recibida
     * @param promise  promesa a cumplir
     * @param <T>      tipo de dato al que se convertirá cada elemento de la lista
     */
    public static <T> void generateList(HttpResponse<JsonArray> response, Promise<List<T>> promise) {
        switch (response.statusCode()) {
            case Success.OK:
                JsonArray arraySubCustomers = response.body();
                promise.complete(arraySubCustomers.getList());
                break;
            case Success.NO_CONTENT:
                promise.fail(new NoSuchElementException("No se encontraron coincidencias"));
                break;
            case ClientError.BAD_REQUEST:
                promise.fail(new NullPointerException("Error al enviar parámetros"));
                break;
        }
    }

    /**
     * crea un objeto a partir de una respuesta obtenida por el cliente web
     *
     * @param response  respuesta recibida
     * @param promise   promesa a cumplir
     * @param classType clase a la que se mapeará el objeto Json
     * @param <T>       clase para Cast del json
     */
    public static <T> void generateJson(HttpResponse<JsonObject> response, Promise<T> promise, T classType) {
        switch (response.statusCode()) {
            case Success.OK:
            case Success.CREATED:
                JsonObject jsonObject = response.body();
                promise.complete((T) jsonObject.mapTo(classType.getClass()));
                break;

            case Success.NO_CONTENT:
                promise.fail(new NoSuchElementException());
                break;
        }
    }

    /**
     * crea un objeto a partir de una respuesta obtenida por el cliente web
     *
     * @param response  respuesta recibida
     * @param promise   promesa a cumplir
     * @param classType clase a la que se mapeará el objeto Json
     * @param <T>       clase a la que pertenece la respuesta y a la que se transformara el json
     */
    public static <T> void generateJson(HttpResponse<JsonObject> response, Promise<T> promise, Class<T> classType) {
        switch (response.statusCode()) {
            case Success.OK:
            case Success.CREATED:
                JsonObject jsonObject = response.body();
                promise.complete(jsonObject.mapTo(classType));
                break;

            case Success.NO_CONTENT:
                promise.fail(new NoSuchElementException());
                break;
            default:
                promise.fail(new Throwable("ERROR IN RESPONSE: " + response.bodyAsString()));
        }
    }

    /**
     * @param response
     * @param promise
     */
    public static void decideLogicValue(HttpResponse<?> response, Promise<Boolean> promise) {
        if (response.statusCode() == Success.ACCEPTED || response.statusCode() == Success.OK
                || response.statusCode() == Success.CREATED) {
            promise.complete(true);
        } else if (response.statusCode() == Success.NO_CONTENT)
            promise.complete(false);
        else
            promise.fail(new Throwable("ERROR"));
    }

    /**
     * jmoreno
     * Método global usado para consumir servicios que devuelven listas y consumen json
     *
     * @param requestBody Cuerpo de la petición
     * @param host        Host
     * @param port        Puerto
     * @param requestUri  Uri
     * @return JsonArray
     */
    public static Future<JsonArray> makePost(Object requestBody, String host, Integer port, String requestUri) {
        String completeUri = host + (port != null ? ":" + port : "") + requestUri;
        logger.info("clientHelper makePost to: POST -> " + completeUri);
        Promise<JsonArray> promise = Promise.promise();
        CLIENT.request(HttpMethod.POST, port, host, requestUri)
                .ssl(true)
                .putHeader(HttpHeaderNames.ACCEPT.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .as(BodyCodec.jsonArray())
                .sendJson(requestBody == null ? new JsonObject() : requestBody)
                .onFailure(fail -> {
                    logger.error("Ha ocurrido un error al consumir servicio: " + completeUri, fail);
                    promise.fail(fail);
                }).onSuccess(response -> {
                    switch (response.statusCode()) {
                        case Success.OK:
                        case Success.CREATED:
                            promise.complete(response.body());
                            break;
//                        case ClientError.NOT_ACCEPTABLE:
//                            promise.fail(new NotAcceptableException());
//                            break;
                        case Success.NO_CONTENT:
                            promise.fail(new NoSuchElementException());
                            break;
                        default:
                            promise.fail(new Throwable("ERROR IN RESPONSE: " + response.bodyAsString()));
                    }
                });
        return promise.future();
    }

    /**
     * jmoreno
     * Método global usado para consumir servicios que devuelven listas y consumen json
     *
     * @param multipartForm Cuerpo de la petición
     * @param host          Host
     * @param port          Puerto
     * @param requestUri    Uri
     * @return JsonArray
     */
    public static Future<JsonArray> makePostMultiPart(MultipartForm multipartForm, String host, Integer port, String requestUri) {
        String completeUri = host + (port != null ? ":" + port : "") + requestUri;
        logger.info("clientHelper makePost to: POST -> " + completeUri);
        Promise<JsonArray> promise = Promise.promise();
        CLIENT.request(HttpMethod.POST, port, host, requestUri)
                .ssl(true)
                .putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.MULTIPART_FORM_DATA.toString())
                .as(BodyCodec.jsonArray())
                .sendMultipartForm(multipartForm)
                .onFailure(fail -> {
                    logger.error("Ha ocurrido un error al consumir servicio: " + completeUri, fail);
                    promise.fail(fail);
                }).onSuccess(response -> {
                    switch (response.statusCode()) {
                        case Success.OK:
                        case Success.CREATED:
                            promise.complete(response.body());
                            break;
//                        case ClientError.NOT_ACCEPTABLE:
//                            promise.fail(new NotAcceptableException());
//                            break;
                        case Success.NO_CONTENT:
                            promise.fail(new NoSuchElementException());
                            break;
                        default:
                            promise.fail(new Throwable("ERROR IN RESPONSE: " + response.bodyAsString()));
                    }
                });
        return promise.future();
    }

    /**
     * jmoreno
     * Método global usado para consumir servicios que devuelven listas y consumen json
     *
     * @param requestBody Cuerpo de la petición
     * @param host        Host
     * @param port        Puerto
     * @param requestUri  Uri
     * @return JsonArray
     */
    public static Future<JsonObject> makePostReturnJsonObject(Object requestBody, String host, Integer port, String requestUri) {
        String completeUri = host + (port != null ? ":" + port : "") + requestUri;
        logger.info("clientHelper makePost to: POST -> " + completeUri);
        Promise<JsonObject> promise = Promise.promise();
        CLIENT.request(HttpMethod.POST, port, host, requestUri)
                .ssl(true)
                .putHeader(HttpHeaderNames.ACCEPT.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .as(BodyCodec.jsonObject())
                .sendJson(requestBody == null ? new JsonObject() : requestBody)
                .onFailure(fail -> {
                    logger.error("Ha ocurrido un error al consumir servicio: " + completeUri, fail);
                    promise.fail(fail);
                }).onSuccess(response -> {
                    switch (response.statusCode()) {
                        case Success.OK:
                        case Success.CREATED:
                            promise.complete(response.body());
                            break;
//                        case ClientError.NOT_ACCEPTABLE:
//                            promise.fail(new NotAcceptableException());
//                            break;
                        case Success.NO_CONTENT:
                            promise.fail(new NoSuchElementException());
                            break;
                        default:
                            promise.fail(new Throwable("ERROR IN RESPONSE: " + response.bodyAsString()));
                    }
                });
        return promise.future();
    }

    /**
     * jmoreno
     * Método global usado para consumir servicios que devuelven listas y consumen json
     *
     * @param requestBody Cuerpo de la petición
     * @param host        Host
     * @param port        Puerto
     * @param requestUri  Uri
     * @return JsonArray
     */
    public static Future<JsonArray> makePut(Object requestBody, String host, Integer port, String requestUri) {
        String completeUri = host + (port != null ? ":" + port : "") + requestUri;
        logger.info("clientHelper makePost to: PUT -> " + completeUri);
        logger.info("body: " + requestBody.toString());
        Promise<JsonArray> promise = Promise.promise();
        CLIENT.request(HttpMethod.PUT, port, host, requestUri)
                .ssl(true)
                .putHeader(HttpHeaderNames.ACCEPT.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .as(BodyCodec.jsonArray())
                .sendJson(requestBody)
                .onFailure(fail -> {
                    logger.error("Ha ocurrido un error al consumir servicio: " + completeUri, fail);
                    promise.fail(fail);
                }).onSuccess(response -> {
                    switch (response.statusCode()) {
                        case Success.OK:
                        case Success.CREATED:
                            promise.complete(response.body());
                            break;
//                        case ClientError.NOT_ACCEPTABLE:
//                            promise.fail(new NotAcceptableException());
//                            break;
                        case Success.NO_CONTENT:
                            promise.complete(null);
                            break;
                        default:
                            promise.fail(new Throwable("ERROR IN RESPONSE: " + response.bodyAsString()));
                    }
                });
        return promise.future();
    }

    /**
     * jmoreno
     * Método global usado para consumir servicios que devuelven listas y consumen json
     *
     * @param requestBody Cuerpo de la petición
     * @param host        Host
     * @param port        Puerto
     * @param requestUri  Uri
     * @return JsonArray
     */
    public static Future<JsonArray> makePatch(Object requestBody, String host, Integer port, String requestUri) {
        String completeUri = host + (port != null ? ":" + port : "") + requestUri;
        logger.info("clientHelper makePost to: PATCH -> " + completeUri);
        logger.info("body: " + requestBody.toString());
        Promise<JsonArray> promise = Promise.promise();
        CLIENT.request(HttpMethod.PATCH, port, host, requestUri)
                .ssl(true)
                .putHeader(HttpHeaderNames.ACCEPT.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .as(BodyCodec.jsonArray())
                .sendJson(requestBody)
                .onFailure(fail -> {
                    logger.error("Ha ocurrido un error al consumir servicio: " + completeUri, fail);
                    promise.fail(fail);
                }).onSuccess(response -> {
                    switch (response.statusCode()) {
                        case Success.OK:
                        case Success.CREATED:
                            promise.complete(response.body());
                            break;
//                        case ClientError.NOT_ACCEPTABLE:
//                            promise.fail(new NotAcceptableException());
//                            break;
                        case Success.NO_CONTENT:
                            promise.complete(null);
                            break;
                        default:
                            promise.fail(new Throwable("ERROR IN RESPONSE: " + response.bodyAsString()));
                    }
                });
        return promise.future();
    }
}
