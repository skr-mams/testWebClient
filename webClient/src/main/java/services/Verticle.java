package services;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SelfSignedCertificate;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.conreteConfigurations.Headers;
import services.models.httpMethods.ClientError;
import services.models.httpMethods.ServerError;
import services.reactorUnit.CustomerReactor;

/**
 * @author Mario Manzanarez
 * @since 8/22/22
 */
public class Verticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(Verticle.class);
    public static Vertx vertxInstance;

    @Override
    public void start() throws Exception {
        vertxInstance = vertx;
        Router router = Router.router(vertxInstance);
        Headers.headers(router, null);
        router.post("/customer");
        router.post("/customer/data").handler(CustomerReactor::getCustomer);
        router.put("/customer");
        router.delete("/customer");

        router.post("/customer/address/all");
        router.post("/customer/address");

        //router.post("address");
        router.post("/address/data/");
        router.put("/address");
        router.delete("/address");
        errorClient(router);
        startServer(router);
    }

    /**
     * Atrapa una excepción de un usuario sin autorización y responde al cliente
     *
     * @param router objeto con rutas que recibirán esta solicitud
     */
    private void errorClient(Router router) {
        router.errorHandler(ClientError.FORBIDDEN, error -> {
            error.response().setStatusCode(ClientError.FORBIDDEN).end();
        });
        router.errorHandler(ServerError.INTERNAL_SERVER_ERROR, error -> {
            LOG.error("Ocurrió un error inesperado: ", error);
            if (error.failure() instanceof NumberFormatException)
                error.response().setStatusCode(ClientError.NOT_ACCEPTABLE).end();
            if (error.failure() instanceof IllegalArgumentException || error.failure() instanceof NullPointerException) {
                LOG.error("Error al tomar parámetros: " + error.failure(), error.failure());
                error.response().setStatusCode(ClientError.BAD_REQUEST).end("Parámetros de solicitud incorrectos");
            }
        });
        /*
         * Error capturado por el schema parser de vert.x
         */
        router.errorHandler(ClientError.BAD_REQUEST, error -> {
            LOG.error("Error en parámetros: ", error.failure());
            error.response().setStatusCode(ClientError.BAD_REQUEST).end("La solicitud es incorrecta");
        });
    }

    /**
     * Inicializa el servidor web con la configuración correspondiente
     *
     * @param router objeto router pasado como referencia para obtener las solicitudes
     */
    public void startServer(Router router) {


        // certificado ssl para pruebas
        SelfSignedCertificate certificate = SelfSignedCertificate.create();
        vertx.createHttpServer(new HttpServerOptions()
                        .setSsl(true)
                        .setKeyCertOptions(certificate.keyCertOptions())
                        .setTrustOptions(certificate.trustOptions()))
                .requestHandler(router)
                .listen(8080)
                .onSuccess(server -> {

                    LOG.debug("HTTP server started on port from kerno-2 api_kerno_2: " + server.actualPort());
                }).onFailure(fail -> {
                    LOG.error("Error al inciar el servidor web: " + fail.toString());
                });

    }

}
