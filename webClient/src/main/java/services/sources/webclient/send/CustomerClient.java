package services.sources.webclient.send;

import io.vertx.core.Future;
import io.vertx.ext.web.codec.BodyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.models.Customer;
import services.sources.webclient.connection.WebClientConnection;
import services.sources.webclient.helper.ClientHelper;

/**
 * @author Mario Manzanarez
 * @since 8/22/22
 */
public class CustomerClient {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerClient.class);

    public static Future<Customer> getCustomer(long idCustomer) {
        return Future.future(promise -> WebClientConnection.getWebClient()
                .get(9004, "localhost", "/krn/v2/customers/" + idCustomer)
                .ssl(true)
                .as(BodyCodec.jsonObject())
                .putHeader("accept", "application/json")
                .send()
                .onFailure(promise::fail)
                .onSuccess(response -> {
                    var jsonCustomer = response.body();
                    var customer = new Customer();
                    customer.setPersonType(jsonCustomer.getString("personType"));
                    promise.complete(customer);
                })
                .onComplete(completed -> WebClientConnection.closeWebClient())
        );
    }
}
