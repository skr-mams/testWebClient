package services.sources.webclient.connection;

import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import services.Verticle;

/**
 * @author Mario Manzanarez
 * Usando el patrón singleton otorga una conexión para que pueda ser usada
 * por las clases que acceden a los demas servicios
 * @date 28-01-20220
 */
public class WebClientConnection {

    private static WebClient webClient;

    private WebClientConnection() {
        WebClientOptions options = new WebClientOptions();
        options.setSsl(true).setVerifyHost(false).setTrustAll(true);
        webClient = WebClient.create(Verticle.vertxInstance, options);
    }

    public static WebClient getWebClient() {
        if (webClient == null)
            new WebClientConnection();
        return webClient;
    }

    public static void closeWebClient() {
        webClient = null;
    }

}
