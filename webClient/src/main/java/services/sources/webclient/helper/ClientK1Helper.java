package services.sources.webclient.helper;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import services.models.httpMethods.ClientError;
import services.models.httpMethods.Success;
import services.sources.webclient.connection.WebClientConnection;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Mario Manzanarez
 */
public class ClientK1Helper {
    private static final WebClient CLIENT = WebClientConnection.getWebClient();
    private static final String NAME_HEADER = "Accept";
    private static final String VALUE_HEADER = "application/json";
    private static final Credentials CREDENTIALS_K1 = new UsernamePasswordCredentials("k2@kerno.mx", "Sekura.1234");
    private static final String HOST = "https://devkerno.sekura.mx/";

    /**
     * Crear una conexión mediante un cliente web
     *
     * @param path ruta del recurso
     * @return conexión de cliente web que espera como respuesta un JsonArray
     */
    public static HttpRequest<JsonArray> arrayRequest(String path) {
        return CLIENT
                .getAbs(HOST + path)
                .ssl(true)
                .authentication(CREDENTIALS_K1)
                .as(BodyCodec.jsonArray())
                .expect(ResponsePredicate.SC_OK)
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
                promise.fail(new NoSuchElementException("No se coincidencias"));
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
//            case ClientError.NOT_ACCEPTABLE:
//                promise.fail(new NotAcceptableException());
//                break;
            case Success.NO_CONTENT:
                promise.fail(new NoSuchElementException());
                break;
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
        }
    }
}
